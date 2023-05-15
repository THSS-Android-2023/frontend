package com.example.internet.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.internet.R;
import com.example.internet.activity.FollowingActivity;
import com.example.internet.util.Global;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoFragment extends Fragment {

    @BindView(R.id.follower)
    LinearLayout follower_button;

    @BindView(R.id.following)
    LinearLayout following_button;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);

        ButterKnife.bind(this, rootView);
        ImageView imageView = rootView.findViewById(R.id.img_avatar);
        String base64Image = Global.base64Test.split(",")[1];
        byte[] imageAsBytes = Base64.decode(base64Image.getBytes(), Base64.DEFAULT);
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));


        Context ctx = getActivity();
        following_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ctx, FollowingActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
