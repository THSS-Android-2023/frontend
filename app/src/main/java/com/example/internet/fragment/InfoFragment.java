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
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.internet.R;
import com.example.internet.activity.EditInfoActivity;
import com.example.internet.util.Global;
import com.squareup.picasso.Picasso;

public class InfoFragment extends Fragment {

    Button edit_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);
        ImageView imageView = rootView.findViewById(R.id.img_avatar);
        String base64Image = Global.base64Test.split(",")[1];
        byte[] imageAsBytes = Base64.decode(base64Image.getBytes(), Base64.DEFAULT);
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

        edit_button = rootView.findViewById(R.id.edit_button);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), EditInfoActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

}
