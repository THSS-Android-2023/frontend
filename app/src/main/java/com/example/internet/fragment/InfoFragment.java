package com.example.internet.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
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
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        TextView username_textview = rootView.findViewById(R.id.username);
        username_textview.setText(username);

        TextView intro_textview = rootView.findViewById(R.id.introduction);
        introduction = intro_textview.getText().toString();


        Context ctx = getActivity();
        following_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, FollowingActivity.class);
                startActivity(intent);
            }
        });


        edit_button = rootView.findViewById(R.id.edit_button);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditInfoActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("avatar", base64Image);
                intent.putExtra("intro", introduction);
                startActivity(intent);
            }
        });


        return rootView;
    }

}
