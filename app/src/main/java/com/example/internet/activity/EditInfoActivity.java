package com.example.internet.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.widget.TextView;

import com.example.internet.R;
import com.example.internet.request.ChangeInfoRequest;
import com.example.internet.request.UploadAvatarRequest;
import com.example.internet.util.ErrorDialog;
import com.example.internet.util.FileUtils;
import com.example.internet.util.Global;
import com.example.internet.request.BaseRequest;
import com.squareup.picasso.Picasso;
import com.zhihu.matisse.Matisse;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EditInfoActivity extends BaseActivity {


    final Context ctx = this;
    private String username;
    private String nickname;

    private String avatar_url;

    private String jwt;
    private String introduction;

    @BindView(R.id.avatar_edit)
    ImageView avatar_image;

    @BindView(R.id.username_edit)
    TextView username_edit;

    @BindView(R.id.nickname_edit)
    EditText nickname_edit;

    @BindView(R.id.intro_edit)
    EditText intro_edit;
    private Uri imageUri = null;
    private static final int SELECT_PHOTO = 100;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 101;

    Callback uploadAvatarCallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            int code = response.code();
            Log.d("code", String.valueOf(code));
            if (code != 200 && code != 201)
                new ErrorDialog(ctx, "修改失败：" + response.message());
            try{
                avatar_url = response.body().string();
                Log.d("getUrl", avatar_url);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 在此处执行需要在主线程中进行的 UI 操作
                        Picasso.get().load(avatar_url).into(avatar_image);
                    }
                });
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    };


    Callback changeResultCallback = new Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.d("Error", e.toString());}
        @Override
        public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
            int code = response.code();
            Log.d("code", String.valueOf(code));
            if (code != 200 && code != 201) {
                Log.d("error", response.message());
                new ErrorDialog(ctx, "修改失败");
            }
            else {
                AppCompatActivity appCtx = (AppCompatActivity) ctx;
                appCtx.finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        nickname = intent.getStringExtra("nickname");
        introduction = intent.getStringExtra("intro");
        avatar_url = intent.getStringExtra("avatar");
        jwt = intent.getStringExtra("jwt");


        if (checkSelfPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 请求权限
            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    102);
        }

        avatar_image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        Picasso.get().load(avatar_url).into(avatar_image);
        username_edit.setText(username);
        nickname_edit.setText(nickname);
        intro_edit.setText(introduction);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            imageUri = data.getData();
            try {
                String path = FileUtils.getPath(this, imageUri);
                Log.d("path", path);
                File imageFile = new File(path);
                new UploadAvatarRequest(jwt, imageFile, uploadAvatarCallback);
            } catch (Exception e) {
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

    public void onSaveClick(View v){
        String usr = username_edit.getText().toString();
        String intro = intro_edit.getText().toString();
        String nickname = nickname_edit.getText().toString();

        if(usr.isEmpty()){
            new ErrorDialog(this, "用户名不能为空");
            return;
        }

        new ChangeInfoRequest(intro, nickname, changeResultCallback, jwt);
    }

    public void onResetPasswordClick(View view) {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("jwt", jwt);
        startActivity(intent);
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }
}