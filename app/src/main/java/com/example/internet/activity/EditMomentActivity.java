package com.example.internet.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.internet.R;
import com.example.internet.request.PublishMomentRequest;
import com.example.internet.util.ErrorDialog;
import com.example.internet.util.FileUtils;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.squareup.picasso.Picasso;

import org.commonmark.node.Node;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.noties.markwon.Markwon;
import io.noties.markwon.editor.MarkwonEditor;
import io.noties.markwon.editor.MarkwonEditorTextWatcher;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EditMomentActivity extends AppCompatActivity {

    final Context ctx = this;
    private static final int SELECT_PHOTO = 100;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 101;

    private EditText editText;
    private TextView textView;

    private String jwt;

    // Name of shared preferences file
    private String sharedPrefFile =
            "com.example.android.hellosharedprefs";
    private String mSharedText;
    private String mSharedImg;
    // Shared preferences object
    private SharedPreferences mPreferences;
    private Uri imageUri = null;

    private List<String> uriList = new ArrayList<>();
    private NineGridImageView<String> nineGridImageView;
    private NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
        @Override
        protected void onDisplayImage(Context context, ImageView imageView, String photo) {
            assert imageView != null;
            Uri uri = Uri.parse(photo);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Log.d("uri", uri.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            imageView.setImageBitmap(bitmap);
//            Picasso.with(context)
//                    .load(photo.getSmallUrl)
//                    .placeholder(R.drawable.ic_default_image)
//                    .into(imageView);
        }

        @Override
        protected ImageView generateImageView(Context context) {
            Log.d("generate", " imageview");
            return super.generateImageView(context);
        }

        @Override
        protected void onItemImageClick(Context context, int index, List<String> photoList) {
//            photoList.remove(index);
//            showBigPicture(context, photoList.get(index).getBigUrl());
        }
    };


        @SuppressLint("MissingInflatedId")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_moment);
        Boolean b = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
        Log.d("123", String.valueOf(b));
        if (checkSelfPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 请求权限
            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    102);
        }

        jwt = getIntent().getStringExtra("jwt");

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        mSharedText = mPreferences.getString("text", "");
        mSharedImg = mPreferences.getString("img","");

        editText = findViewById(R.id.et_post_text);
        if (mSharedText != "")
            editText.setText(mSharedText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mSharedText = s.toString();
            }
        });

        ImageView img = findViewById(R.id.iv_post_image);
        if (mSharedImg != ""){
            try {
                Uri uri = Uri.parse((String) mSharedImg);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        final Markwon markwon = Markwon.create(this);
        final MarkwonEditor editor = MarkwonEditor.create(markwon);
        editText.addTextChangedListener(MarkwonEditorTextWatcher.withProcess(editor));

        textView = findViewById(R.id.render_post_text);

        nineGridImageView = findViewById(R.id.iv_nine_grid);
        nineGridImageView.setAdapter(mAdapter);
        nineGridImageView.setImagesData(uriList);
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
                    Log.d("Editor", "权限拒绝，不能执行任务");
                }
                return;
            }
            default:
                // 其他权限的处理
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            imageUri = data.getData();
            mSharedImg = imageUri.toString();
            ImageView mImageView = findViewById(R.id.iv_post_image);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                mImageView.setImageBitmap(bitmap);
                getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                uriList.add(imageUri.toString());
//                nineGridImageView.setImagesData(uriList);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString("text", mSharedText);
        preferencesEditor.putString("img", mSharedImg);
        preferencesEditor.apply();
    }

    public void onPublishClick(View view) {
//        Intent data = new Intent();
//        data.putExtra("text", mSharedText);
//        data.putExtra("img", mSharedImg);
//        setResult(RESULT_OK, data);
        List<File> imageFiles = new ArrayList<>();
        Log.d("test", "111111");
        Log.d("uri_0", uriList.get(0));
        for (String uri : uriList) {
            Uri imageUri = Uri.parse(uri);
            String path = FileUtils.getPath(this, imageUri);
            File imageFile = new File(path);
            imageFiles.add(imageFile);
        }
        File[] imageFileArray = imageFiles.toArray(new File[0]);
        new PublishMomentRequest("title", mSharedText, "校园资讯", "China",
                uriList.size(), imageFileArray, publishCallback, jwt);

        finish();
    }

    public void onRenderClick(View view) {
        final Markwon markwon = Markwon.create(this);
        final Node node = markwon.parse(editText.getText().toString());
        final Spanned markdown = markwon.render(node);

        markwon.setParsedMarkdown(textView, markdown);
        Toast.makeText(this, markdown, Toast.LENGTH_LONG).show();
    }

    Callback publishCallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            int code = response.code();
            Log.d("code", String.valueOf(code));
            if (code != 200 && code != 201)
                new ErrorDialog(ctx, "发表失败：" + response.message());
            try{
                AppCompatActivity appCtx = (AppCompatActivity) ctx;
                appCtx.finish();
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    };
}
