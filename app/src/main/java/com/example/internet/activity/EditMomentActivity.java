package com.example.internet.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.internet.R;
import com.example.internet.request.PublishMomentRequest;
import com.example.internet.util.ErrorDialog;
import com.example.internet.util.FileUtils;
import com.example.internet.util.Global;
import com.google.android.material.snackbar.Snackbar;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.picasso.Picasso;

import org.commonmark.node.Node;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.noties.markwon.Markwon;
import io.noties.markwon.editor.MarkwonEditor;
import io.noties.markwon.editor.MarkwonEditorTextWatcher;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EditMomentActivity extends BaseActivity implements LocationListener {

    final Context ctx = this;
    private Dialog mDialog;
    private static final int SELECT_PHOTO = 100;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 101;

    private EditText editText;
    private EditText editTitle;
    private TextView textView;
    private TextView locationView;

    private String jwt;

    // Name of shared preferences file
    private String sharedPrefFile =
            "com.example.android.hellosharedprefs";
    private String mSharedText = "";
    private String mSharedTitle = "";
    private String mSharedImg = "";
    private String mSharedTag = "";
    private String mSharedLoc = "";
    // Shared preferences object
    private SharedPreferences mPreferences;
    private Uri imageUri = null;

    private MaterialSpinner spinner;

    private List<String> uriList = new ArrayList<>();
    private NineGridImageView<String> nineGridImageView;
    private NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
        @Override
        protected void onDisplayImage(Context context, ImageView imageView, String photo) {
            assert imageView != null;
            Uri uri = Uri.parse(photo);
            Picasso.get()
                    .load(uri)
                    .into(imageView);
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
        if (checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    103);
        }
        if (checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    104);
        }

        jwt = getIntent().getStringExtra("jwt");

        String TAG = "loc";
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        editText = findViewById(R.id.et_post_text);
        editTitle = findViewById(R.id.et_post_title);
        locationView = findViewById(R.id.location);
        if (!mSharedText.isEmpty())
            editText.setText(mSharedText);
        if (!mSharedTitle.isEmpty())
            editTitle.setText(mSharedTitle);
        if (!mSharedLoc.isEmpty())
            locationView.setText(mSharedLoc);
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

        editTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mSharedTitle = s.toString();
            }
        });

        spinner = findViewById(R.id.tag_spinner);
        spinner.setItems(Global.TAG_LIST);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                mSharedTag = item;
            }
        });
        if (!mSharedTag.isEmpty()) {
            spinner.setSelectedIndex(Global.TAG_LIST.indexOf(mSharedTag));
        }

        ImageView img = findViewById(R.id.iv_post_image);
        if (!mSharedImg.isEmpty()) {
            try {
                Uri uri = Uri.parse((String) mSharedImg);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                nineGridImageView.setImagesData(uriList);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        putPreference();
    }

    void loadPreference(){
        mSharedText = mPreferences.getString("text", "");
        mSharedTitle = mPreferences.getString("title", "");
        mSharedImg = mPreferences.getString("img", "");
        mSharedTag = mPreferences.getString("tag", "");
        mSharedLoc = mPreferences.getString("loc", "");
    }
    void putPreference(){
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString("text", mSharedText);
        preferencesEditor.putString("title", mSharedTitle);
        preferencesEditor.putString("img", mSharedImg);
        preferencesEditor.putString("tag", mSharedTag);
        preferencesEditor.putString("loc", mSharedLoc);
        preferencesEditor.apply();
    }

    public void onPublishClick(View view) {
//        Intent data = new Intent();
//        data.putExtra("text", mSharedText);
//        data.putExtra("img", mSharedImg);
//        setResult(RESULT_OK, data);
        List<File> imageFiles = new ArrayList<>();
        Log.d("test", "111111");
//        Log.d("uri_0", uriList.get(0));
        for (String uri : uriList) {
            Uri imageUri = Uri.parse(uri);
            String path = FileUtils.getPath(this, imageUri);
            File imageFile = new File(path);
            imageFiles.add(imageFile);
        }
        File[] imageFileArray = imageFiles.toArray(new File[0]);
        Log.d("length", imageFileArray.length + "");

        new PublishMomentRequest(mSharedTitle, mSharedText, Global.TAG_STR2CODE_MAP.get(mSharedTag), mSharedLoc,
                uriList.size(), imageFileArray, publishCallback, jwt);

//        finish();
    }

    public void onRenderClick(View view) {
        final Markwon markwon = Markwon.create(this);
        final Node node = markwon.parse(editText.getText().toString());
        final Spanned markdown = markwon.render(node);

        markwon.setParsedMarkdown(textView, markdown);
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
            try {
                mSharedText="";
                mSharedImg="";
                mSharedTitle="";
                putPreference();
                AppCompatActivity appCtx = (AppCompatActivity) ctx;
                appCtx.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onLocationChanged(@NonNull Location location) {
//        double latitude = location.getLatitude();
//        double longitude = location.getLongitude();
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);

        double latitude = Double.parseDouble(df.format(location.getLatitude()));
        double longitude = Double.parseDouble(df.format(location.getLongitude()));

        mSharedLoc = "Lat: " + latitude + ", Lon: " + longitude;
        Log.d("loc", mSharedLoc);
        locationView.setText(mSharedLoc);

//        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
//            String TAG = "location";
//            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
//            if (addresses != null && !addresses.isEmpty()) {
//                Address address = addresses.get(0);
//                String countryName = address.getCountryName();
//                String countryCode = address.getCountryCode();
//                Log.d(TAG, "Country name: " + countryName);
//                Log.d(TAG, "Country code: " + countryCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

}
