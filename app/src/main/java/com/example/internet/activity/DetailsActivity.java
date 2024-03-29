package com.example.internet.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.internet.R;
import com.example.internet.adapter.list.CommentListAdapter;
import com.example.internet.model.CommentModel;
import com.example.internet.model.TimelineModel;
import com.example.internet.request.CheckFollowshipRequest;
import com.example.internet.request.DeleteMomentRequest;
import com.example.internet.request.FollowUserRequest;
import com.example.internet.request.GetCommentRequest;
import com.example.internet.request.LikeMomentRequest;
import com.example.internet.request.PostCommentRequest;
import com.example.internet.request.StarMomentRequest;
import com.example.internet.request.UnfollowUserRequest;
import com.example.internet.request.UnlikeMomentRequest;
import com.example.internet.request.UnstarMomentRequest;
import com.example.internet.util.ErrorDialog;
import com.example.internet.util.Global;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.squareup.picasso.Picasso;

import org.commonmark.node.Node;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.noties.markwon.Markwon;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DetailsActivity extends BaseActivity{
    @BindView(R.id.detail_username)TextView usernameView;

    @BindView(R.id.detail_avatar)ImageView avatarView;

    @BindView(R.id.detail_timestamp) TextView timestampView;

    @BindView(R.id.detail_title) TextView titleView;

    @BindView(R.id.detail_content) TextView contentView;

    @BindView(R.id.location_text) TextView locationView;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.iv_nine_grid)
    NineGridImageView nine_grid;

    @BindView(R.id.comment_num) TextView commentNum;

    @BindView(R.id.like_num) TextView likeNum;

    @BindView(R.id.star_num) TextView starNum;

    @BindView(R.id.comment_view) ImageView commentView;

    @BindView(R.id.like_view) ImageView likeView;

    @BindView(R.id.star_view) ImageView starView;

    @BindView(R.id.comment_layout)
    LinearLayout commentLayout;

    @BindView(R.id.like_layout)
    LinearLayout likeLayout;

    @BindView(R.id.star_layout)
    LinearLayout starLayout;

    @BindView(R.id.share_layout)
    LinearLayout shareLayout;

    @BindView(R.id.comment_edit)
    EditText commentEdit;

    @BindView(R.id.comment_submit)
    ImageView commentSubmit;

    @BindView(R.id.following_btn)
    Button follow_btn;

    @BindView(R.id.deleting_btn)
    ImageView delete_btn;


    @BindView(R.id.userinfo)
    LinearLayout userinfoArea;


    private List<CommentModel> commentData;
    private CommentListAdapter adapter;

    private String jwt;
    private String username;

    TimelineModel timelineModel;

    private Context ctx = this;

    @BindView(R.id.video_player)
    PlayerView playerView;

    private MediaSource buildMediaSource(Uri uri) {
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "YourApplicationName"));
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }

    void changeBtnState(Boolean following){
        if (!following){
            timelineModel.isFollow = false;
            String text = "+  关注";
            String color = "#cccccc";
            follow_btn.setText(text);
            follow_btn.setBackgroundColor(Color.parseColor(color));
        }
        else {
            timelineModel.isFollow = true;
            String text = "√ 已关注";
            String color = "#E4AAEA";
            follow_btn.setText(text);
            follow_btn.setBackgroundColor(Color.parseColor(color));
        }
    }

    Callback checkFollowshipCallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            new ErrorDialog((AppCompatActivity) ctx, "获取关注信息失败");
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            // check return code
            if (response.code() != 200) {
                new ErrorDialog((AppCompatActivity) ctx, "获取关注信息失败");
                return;
            }
            String resStr = response.body().string();
            try {
                JSONArray jsonArray = new JSONArray(resStr);
                Boolean following = jsonArray.getBoolean(0);
                ((AppCompatActivity) ctx).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        changeBtnState(following);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Callback followUserCallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            new ErrorDialog((AppCompatActivity) ctx, "关注失败");
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            // check return code
            if (response.code() != 200 && response.code() != 201) {
                new ErrorDialog((AppCompatActivity) ctx, "关注失败");
                return;
            }
            timelineModel.isFollow = true;
            ((AppCompatActivity) ctx).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    changeBtnState(true);
                }

            });
        }
    };

    Callback unfollowUserCallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            new ErrorDialog((AppCompatActivity) ctx, "取消关注失败");
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            // check return code
            if (response.code() != 200 && response.code() != 201) {
                new ErrorDialog((AppCompatActivity) ctx, "取消关注失败");
                return;
            }
            timelineModel.isFollow = false;
            ((AppCompatActivity) ctx).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    changeBtnState(false);
                }

            });
        }
    };

    Callback getCommentCallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {

        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                int code = response.code();
                Log.d("code", String.valueOf(code));
                if (code != 200 && code != 201)
                    new ErrorDialog(DetailsActivity.this, "获取评论失败：" + response.message());
                try{
                    if (response.isSuccessful()) Log.d("response", "successful");
                    String responseBody = response.body().string();
                    Log.d("responseBody", responseBody);

                    JSONArray jsonArray = new JSONArray(responseBody);
                    Log.d("getCommentCallback", jsonArray.toString());
                    commentData.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        commentData.add(new CommentModel(jsonObject));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            commentNum.setText(commentData.size() + "");
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    };

    Callback postCommentCallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            new ErrorDialog(DetailsActivity.this, "评论失败：" + e.getMessage());
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            int code = response.code();
            Log.d("code", String.valueOf(code));
            if (code != 200 && code != 201)
                new ErrorDialog(DetailsActivity.this, "评论失败：" + response.message());
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        commentEdit.setText("");
                        new GetCommentRequest(getCommentCallback, timelineModel.id, jwt);
                    }
                });
            }

        }
    };

    Callback likeMomentCallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            new ErrorDialog(DetailsActivity.this, "点赞失败：" + e.getMessage());
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            int code = response.code();
            Log.d("code", String.valueOf(code));
            if (code != 200 && code != 201)
                new ErrorDialog(DetailsActivity.this, "点赞失败：" + response.message());
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timelineModel.isLike = true;
                        timelineModel.numLikes++;
                        likeNum.setText("" + timelineModel.numLikes);
                        likeView.setImageResource(R.drawable.like_red);
                    }
                });
            }

        }
    };

    Callback unlikeMomentCallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            new ErrorDialog(DetailsActivity.this, "取消点赞失败：" + e.getMessage());
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            int code = response.code();
            Log.d("code", String.valueOf(code));
            if (code != 200 && code != 201)
                new ErrorDialog(DetailsActivity.this, "取消点赞失败：" + response.message());
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timelineModel.isLike = false;
                        timelineModel.numLikes--;
                        likeNum.setText("" + timelineModel.numLikes);
                        likeView.setImageResource(R.drawable.like_grey);
                    }
                });
            }
        }
    };

    Callback starMomentCallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            new ErrorDialog(DetailsActivity.this, "收藏失败：" + e.getMessage());
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            int code = response.code();
            Log.d("code", String.valueOf(code));
            if (code != 200 && code != 201)
                new ErrorDialog(DetailsActivity.this, "收藏失败：" + response.message());
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timelineModel.isStar = true;
                        timelineModel.numStars++;
                        starNum.setText("" + timelineModel.numStars);
                        starView.setImageResource(R.drawable.star_blue);
                    }
                });
            }
        }
    };

    Callback unstarMomentCallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            new ErrorDialog(DetailsActivity.this, "取消收藏失败：" + e.getMessage());
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            int code = response.code();
            Log.d("code", String.valueOf(code));
            if (code != 200 && code != 201)
                new ErrorDialog(DetailsActivity.this, "取消收藏失败：" + response.message());
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timelineModel.isStar = false;
                        timelineModel.numStars--;
                        starNum.setText("" + timelineModel.numStars);
                        starView.setImageResource(R.drawable.star_grey);
                    }
                });
            }
        }
    };

    Callback deleteMomentCallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            new ErrorDialog(DetailsActivity.this, "删除失败：" + e.getMessage());
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            int code = response.code();
            Log.d("code", String.valueOf(code));
            if (code != 200 && code != 201)
                new ErrorDialog(DetailsActivity.this, "删除失败：" + response.message());
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DetailsActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        }
    };

    SimpleExoPlayer player;

    int momentId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);



        player = new SimpleExoPlayer.Builder(this, new DefaultRenderersFactory(this)).build();
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


        Log.d("DetailsActivity", "onCreate: ");

        Intent intent = getIntent();
        String jsonString = intent.getStringExtra("timelineModelJson");
        jwt = intent.getStringExtra("jwt");
        username = intent.getStringExtra("username");

        Gson gson = new Gson();
        timelineModel = gson.fromJson(jsonString, TimelineModel.class);
        momentId = timelineModel.id;

        usernameView.setText(timelineModel.nickname);
//        avatarView.setImageResource(timelineModel.avatar);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Picasso.get()
                        .load(timelineModel.avatar)
                        .into(avatarView);
            }
        });
        timestampView.setText(timelineModel.timestamp);

        titleView.setText(timelineModel.title);
        contentView.setText(timelineModel.content);
        if (!timelineModel.location.isEmpty())
            locationView.setText(timelineModel.location);
        else
            locationView.setVisibility(View.GONE);


        final Markwon markwon = Markwon.create(this);
        final Node node = markwon.parse(timelineModel.content);
        final Spanned markdown = markwon.render(node);
        markwon.setParsedMarkdown(contentView, markdown);

        nine_grid.setAdapter(new NineGridImageViewAdapter<String>() {
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, String url) {
                Picasso.get()
                        .load(url)
                        .into(imageView);
            }

            @Override
            protected ImageView generateImageView(Context context) {
                return super.generateImageView(context);
            }

            @Override
            protected void onItemImageClick(Context context, int index, List<String> list) {
                super.onItemImageClick(context, index, list);
            }
        });

        if (timelineModel.mp4Url.isEmpty()) {
            if (timelineModel.imgUrls.isEmpty()) {
                nine_grid.setVisibility(View.GONE);
            } else {
                nine_grid.setVisibility(View.VISIBLE);
            }
            nine_grid.setImagesData(timelineModel.imgUrls);
            playerView.setVisibility(View.GONE);
        } else {
            MediaSource mediaSource = buildMediaSource(Uri.parse(timelineModel.mp4Url));
            // 准备播放器并设置媒体源
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
            nine_grid.setVisibility(View.GONE);
            playerView.setVisibility(View.VISIBLE);
        }

        commentNum.setText("" + timelineModel.numComments);
        likeNum.setText("" + timelineModel.numLikes);
        starNum.setText("" + timelineModel.numStars);

        commentData = new ArrayList<>();

        adapter = new CommentListAdapter(commentData, this);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Log.d("123", "Clicked on " + position);
            Intent newIntent = new Intent(this, UserInfoActivity.class);
            newIntent.putExtra("curUsername", username);
            newIntent.putExtra("jwt", jwt);
            newIntent.putExtra("username", commentData.get(position).username);
            startActivity(newIntent);
            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        });
        adapter.setManager(recyclerView);
        recyclerView.setAdapter(adapter);

        commentSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentText = commentEdit.getText().toString();
                if (commentText.length() == 0) {
                    new ErrorDialog(DetailsActivity.this, "评论不能为空");
                    return;
                }
                new PostCommentRequest(postCommentCallback,  timelineModel.id, commentText, jwt);

            }
        });

        if (timelineModel.isLike)
            likeView.setImageResource(R.drawable.like_red);
        else
            likeView.setImageResource(R.drawable.like_grey);
        likeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timelineModel.isLike) {
                    new UnlikeMomentRequest(unlikeMomentCallback, timelineModel.id, jwt);
                } else {
                    new LikeMomentRequest(likeMomentCallback, timelineModel.id, jwt);
                }
            }
        });

        if (timelineModel.isStar)
            starView.setImageResource(R.drawable.star_blue);
        else
            starView.setImageResource(R.drawable.star_grey);

        starLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timelineModel.isStar) {
                    new UnstarMomentRequest(unstarMomentCallback, timelineModel.id, jwt);
                } else {
                    new StarMomentRequest(starMomentCallback, timelineModel.id, jwt);
                }
            }
        });

        userinfoArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, UserInfoActivity.class);
                intent.putExtra("username", timelineModel.username);
                intent.putExtra("curUsername", username);
                intent.putExtra("nickname", timelineModel.nickname);
                intent.putExtra("jwt", jwt);
                startActivityForResult(intent, 101);
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });

        shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, timelineModel.title);
                sendIntent.putExtra(Intent.EXTRA_TEXT,   timelineModel.content + "\n--------\n" + "来自" + timelineModel.username + "的分享");
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });

        changeBtnState(timelineModel.isFollow);

        if (timelineModel.username.equals(username)) {
            follow_btn.setVisibility(View.GONE);
            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(DetailsActivity.this)
                            .setTitle("确定删除这条动态吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    new DeleteMomentRequest(deleteMomentCallback, timelineModel.id, jwt);
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
//                    new DeleteMomentRequest(deleteMomentCallback, timelineModel.id, jwt);
                }
            });
        }
        else {
            delete_btn.setVisibility(View.GONE);

            follow_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (timelineModel.isFollow) {
                        new UnfollowUserRequest(unfollowUserCallback, timelineModel.username, jwt);
                    } else {
                        new FollowUserRequest(followUserCallback, timelineModel.username, jwt);
                    }
                }
            });
            new CheckFollowshipRequest(checkFollowshipCallback, timelineModel.username, jwt);
        }
        new GetCommentRequest(getCommentCallback, timelineModel.id, jwt);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            new CheckFollowshipRequest(checkFollowshipCallback, timelineModel.username, jwt);
            changeBtnState(timelineModel.isFollow);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("DetailsActivity", "onDestroy: ");
        player.release();
    }

    @Override
    public void onBackPressed() {
        Log.d("DetailsActivity", "onBackPressed: ");
        Intent intent = new Intent();
        String json = new Gson().toJson(timelineModel);
        intent.putExtra("timelineModelJson", json);
        setResult(Activity.RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }
}
