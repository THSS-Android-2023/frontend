package com.example.internet.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.internet.R;
import com.example.internet.activity.EditInfoActivity;
import com.example.internet.activity.FollowingActivity;
import com.example.internet.activity.MainActivity;
import com.example.internet.util.Global;
import com.example.internet.util.HTTPRequest;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class InfoFragment extends Fragment {

    Button edit_button;

    String username;
    String introduction;
    String base64Image;

    @BindView(R.id.follower)
    LinearLayout follower_button;

    @BindView(R.id.following)
    LinearLayout following_button;

    @BindView(R.id.img_avatar)
    ImageView img_avatar;

    @BindView(R.id.introduction)
    TextView intro_textview;

    @BindView(R.id.username)
    TextView username_textview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);

        ButterKnife.bind(this, rootView);
        base64Image = Global.base64Test.split(",")[1];
        byte[] imageAsBytes = Base64.decode(base64Image.getBytes(), Base64.DEFAULT);
        img_avatar.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));


        username = ((MainActivity) getActivity()).getUsername();
        username_textview.setText(username);

        introduction = intro_textview.getText().toString();


        Context ctx = getActivity();
        following_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, FollowingActivity.class);
                startActivity(intent);
            }
        });
        updateInfo();
        edit_button = rootView.findViewById(R.id.edit_button);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditInfoActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("avatar", base64Image);
                intent.putExtra("intro", introduction);
                startActivityForResult(intent, 0);
            }
        });


        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            updateInfo();
        }
    }

    private void updateInfo(){
        try{
            String saveUrl = "http://129.211.216.10:5000/login/get_info/";
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
                        Log.d("1234", resStr);
                        JSONObject jsonObject = new JSONObject(resStr);
//                        introduction = jsonObject.getString("intro");
//                        base64Image = jsonObject.getString("avatar").split(",")[1];;
//                        byte[] imageAsBytes = Base64.decode(base64Image.getBytes(), Base64.DEFAULT);
//                        img_avatar.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
//                        username_textview.setText(username);
                    }
                    catch (JSONException e){
                        Log.d("Error", e.toString());
                    }
                }
            };
            HTTPRequest saveRequest = new HTTPRequest();
            saveRequest.addParam("username", username);
            saveRequest.get(saveUrl, saveCallback);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
