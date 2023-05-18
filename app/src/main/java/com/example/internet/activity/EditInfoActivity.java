package com.example.internet.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.internet.R;
import com.example.internet.util.ErrorDialog;
import com.example.internet.util.HTTPRequest;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EditInfoActivity extends AppCompatActivity {

    private String username;
    private String introduction;
    private String avatar_base64;

    private ImageView avatar_image;
    private EditText username_edit;
    private EditText intro_edit;
    private String mSharedImg;
    private Uri imageUri = null;
    private static final int SELECT_PHOTO = 100;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        introduction = intent.getStringExtra("intro");
        avatar_base64 = intent.getStringExtra("avatar");

        if (checkSelfPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 请求权限
            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    102);
        }

        avatar_image = findViewById(R.id.avatar_edit);

        byte[] imageAsBytes = Base64.decode(avatar_base64.getBytes(), Base64.DEFAULT);
        avatar_image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

        avatar_image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        username_edit = findViewById(R.id.username_edit);
        username_edit.setText(username);

        intro_edit = findViewById(R.id.intro_edit);
        intro_edit.setText(introduction);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            imageUri = data.getData();
            mSharedImg = imageUri.toString();
            ImageView mImageView = findViewById(R.id.avatar_edit);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                mImageView.setImageBitmap(bitmap);
                getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // 如果请求被取消，结果数组将为空
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限已授予，开始执行任务
                    Log.d("Editor", "给予权限");
                } else {
                    // 权限拒绝，不能执行任务
                    ErrorDialog error = new ErrorDialog(this, "请求权限失败");
                    Log.d("Editor", "权限拒绝，不能执行任务");
                }
                return;
            }
            default:
                // 其他权限的处理
        }
    }


    public String encodeImageToBase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public void onSaveClick(View v){
        String usr = username_edit.getText().toString();
        String intro = intro_edit.getText().toString();

        avatar_image.setDrawingCacheEnabled(true);
        avatar_image.buildDrawingCache();
        Bitmap avatar_bitmap = Bitmap.createBitmap(avatar_image.getDrawingCache());
        avatar_image.setDrawingCacheEnabled(false);

//        int width = avatar_bitmap.getWidth();
//        int height = avatar_bitmap.getHeight();
//        Log.d("width", width + "");
//        Log.d("height", height + "");

        String avatar_base64 = encodeImageToBase64(avatar_bitmap);

        if(usr.isEmpty()){
            ErrorDialog error = new ErrorDialog(this, "用户名不能为空");
            return;
        }
        try{
            String saveUrl = "http://129.211.216.10:5000/login/change_info/";
            Callback saveCallback = new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("Error", e.toString());}
                @Override
                public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                    String resStr = Objects.requireNonNull(response.body()).string();
                    int code = response.code();
                    Log.d("code", String.valueOf(code));
                    try {
                        JSONObject jsonObject = new JSONObject(resStr);
                        Boolean status = jsonObject.getBoolean("status");
                    }
                    catch (JSONException e){
                        Log.d("Error", e.toString());
                    }
                    finish();
                }
            };
            HTTPRequest saveRequest = new HTTPRequest();
            saveRequest.addParam("username", usr);
            saveRequest.addParam("intro", intro);
            saveRequest.addParam("avatar", avatar_base64);
            saveRequest.post(saveUrl, saveCallback);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onResetPasswordClick(View view) {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }


}