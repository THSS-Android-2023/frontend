package com.example.internet.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.internet.R;
import com.example.internet.util.ErrorDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditInfoActivity extends AppCompatActivity {

    private ImageView avatar_image;
    private String mSharedImg;
    private Uri imageUri = null;
    private static final int SELECT_PHOTO = 100;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        if (checkSelfPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 请求权限
            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    102);
        }

        avatar_image = findViewById(R.id.avatar_edit);

        avatar_image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });


//        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.avatar1);
//        String encodedImage = encodeImageToBase64(image);
//        Log.d("img2base64", encodedImage);
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




}