package com.example.internet.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.internet.R;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class NineGridImageViewHolder extends BaseViewHolder {
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
            return super.generateImageView(context);
        }

        @Override
        protected void onItemImageClick(Context context, int index, List<String> photoList) {
        }
    };

    public NineGridImageViewHolder(@NonNull View itemView) {
        super(itemView);
        nineGridImageView = itemView.findViewById(R.id.iv_nine_grid);
        nineGridImageView.setAdapter(mAdapter);
    }

    public void bindData(List<String> data) {
        nineGridImageView.setImagesData(data);
    }
}
