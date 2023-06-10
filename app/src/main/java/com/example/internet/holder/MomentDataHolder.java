package com.example.internet.holder;

import android.content.Context;
import android.net.Uri;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.internet.R;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
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

    private PlayerView playerView;
    private SimpleExoPlayer player;

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

        playerView = itemView.findViewById(R.id.video_player);
        player = new SimpleExoPlayer.Builder(ctx, new DefaultRenderersFactory(ctx)).build();
        playerView.setPlayer(player);

        playerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                // 获取视频的宽高
                int videoWidth = player.getVideoSize().width;
                int videoHeight = player.getVideoSize().height;

                // 获取 PlayerView 的宽高
                int viewWidth = right - left;
                int viewHeight = bottom - top;

                // 计算视频的宽高比
                float videoAspectRatio = (videoHeight == 0) ? 1 : (float) videoWidth / videoHeight;

                // 根据视频的宽高比调整 PlayerView 的大小
                if (viewHeight <= viewWidth / videoAspectRatio && (int) (viewWidth / videoAspectRatio) <= 1600) {
                    ViewGroup.LayoutParams layoutParams = playerView.getLayoutParams();
                    layoutParams.height = (int) (viewWidth / videoAspectRatio);
                    playerView.setLayoutParams(layoutParams);
                } else {
                    ViewGroup.LayoutParams layoutParams = playerView.getLayoutParams();
                    layoutParams.width = (int) (viewHeight * videoAspectRatio);
                    playerView.setLayoutParams(layoutParams);
                }
            }
        });
    }

    public void bindData(List<String> imageList, String content) {
        nineGridImageView.setImagesData(imageList);
        nineGridImageView.setVisibility(View.VISIBLE);
        playerView.setVisibility(View.GONE);
        if (imageList.isEmpty())
            nineGridImageView.setVisibility(View.GONE);
        bindContent(content);
//        Toast.makeText(ctx, markdown, Toast.LENGTH_LONG).show();
    }

    public void bindData(String videoUrl, String content) {
        nineGridImageView.setVisibility(View.GONE);
        playerView.setVisibility(View.VISIBLE);
        Uri videoUri = Uri.parse(videoUrl);
        Log.d("vid url", videoUrl);
        Log.d("vid uri", videoUri.toString());
        MediaSource mediaSource = buildMediaSource(videoUri);
        // 准备播放器并设置媒体源
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
        bindContent(content);
//        Toast.makeText(ctx, markdown, Toast.LENGTH_LONG).show();
    }

    public void bindContent(String content) {
        final Markwon markwon = Markwon.create(ctx);
        final Node node = markwon.parse(content);
        final Spanned markdown = markwon.render(node);

        markwon.setParsedMarkdown(textView, markdown);
    }

    private MediaSource buildMediaSource(Uri uri) {
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(ctx,
                Util.getUserAgent(ctx, "YourApplicationName"));
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }
}
