package com.example.internet.holder;

import android.content.Context;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.internet.R;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.squareup.picasso.Picasso;

import org.commonmark.node.Node;

import java.util.List;

import io.noties.markwon.Markwon;

public class MomentDataHolder extends BaseViewHolder {
    private NineGridImageView<String> nineGridImageView;
    private TextView textView;
    private Context ctx;

    private NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
        @Override
        protected void onDisplayImage(Context context, ImageView imageView, String photo) {
            assert imageView != null;
//            Uri uri = Uri.parse(photo);
//
//            Picasso.get()
//                    .load(uri)
//                    .into(imageView);
            Picasso.get().load(photo).into(imageView);
        }
        @Override
        protected ImageView generateImageView(Context context) {
            return super.generateImageView(context);
        }

        @Override
        protected void onItemImageClick(Context context, int index, List<String> photoList) {
        }
    };

    public MomentDataHolder(@NonNull View itemView) {
        super(itemView);
        nineGridImageView = itemView.findViewById(R.id.iv_nine_grid);
        nineGridImageView.setAdapter(mAdapter);

        textView = itemView.findViewById(R.id.content);
        ctx = itemView.getContext();
    }

    public void bindData(List<String> imageList, String content) {
        nineGridImageView.setImagesData(imageList);

        final Markwon markwon = Markwon.create(ctx);
        final Node node = markwon.parse(content);
        final Spanned markdown = markwon.render(node);

        markwon.setParsedMarkdown(textView, markdown);
        Toast.makeText(ctx, markdown, Toast.LENGTH_LONG).show();
    }
}
