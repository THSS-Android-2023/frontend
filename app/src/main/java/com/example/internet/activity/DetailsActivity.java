package com.example.internet.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.internet.R;
import com.example.internet.adapter.list.CommentListAdapter;
import com.example.internet.model.CommentModel;
import com.example.internet.model.TimelineModel;
import com.example.internet.request.GetCommentRequest;
import com.example.internet.request.GetSearchResultRequest;
import com.example.internet.request.PostCommentRequest;
import com.example.internet.util.ErrorDialog;
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

public class DetailsActivity extends AppCompatActivity {
    @BindView(R.id.detail_username)TextView usernameView;

    @BindView(R.id.detail_avatar)ImageView avatarView;

    @BindView(R.id.detail_timestamp) TextView timestampView;

    @BindView(R.id.detail_title) TextView titleView;

    @BindView(R.id.detail_content) TextView contentView;


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

    @BindView(R.id.comment_edit)
    EditText commentEdit;

    @BindView(R.id.comment_submit)
    Button commentSubmit;

    private List<CommentModel> commentData;
    private CommentListAdapter adapter;

    private String jwt;

    TimelineModel timelineModel;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        Log.d("DetailsActivity", "onCreate: ");

        Intent intent = getIntent();
        String jsonString = intent.getStringExtra("timelineModelJson");
        jwt = intent.getStringExtra("jwt");

        Gson gson = new Gson();
        timelineModel = gson.fromJson(jsonString, TimelineModel.class);

        usernameView.setText(timelineModel.username);
        avatarView.setImageResource(timelineModel.avatar);
        timestampView.setText(timelineModel.timestamp);

        titleView.setText(timelineModel.title);
        contentView.setText(timelineModel.content);

        final Markwon markwon = Markwon.create(this);
        final Node node = markwon.parse(timelineModel.content);
        final Spanned markdown = markwon.render(node);
        markwon.setParsedMarkdown(contentView, markdown);

//        for (int i = 0; i < timelineModel.img.length; i++) {
//            img[i].setImageResource(timelineModel.img[i]);
//        }
//
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
        nine_grid.setImagesData(timelineModel.imgUrls);

        commentNum.setText("" + timelineModel.numComments);
        likeNum.setText("" + timelineModel.numLikes);
        starNum.setText("" + timelineModel.numStars);




        commentData = new ArrayList<>();

//        commentData.add(new CommentModel("pc20", "支持！！", "14:31", R.drawable.avatar4));
//        commentData.add(new CommentModel("cjz20", "支持！！", "14:29", R.drawable.avatar2));
//        commentData.add(new CommentModel("xuhb20", "哪天一起出去玩", "14:03", R.drawable.avatar1));
//        commentData.add(new CommentModel("xuhb20", "彭老师也带带我", "13:51", R.drawable.avatar1));
//        commentData.add(new CommentModel("cjz20", "彭老师带带我", "13:49", R.drawable.avatar2));
//        commentData.add(new CommentModel("cjz20", "666", "13:31", R.drawable.avatar2));
//        commentData.add(new CommentModel("xuhb20", "确实强", "13:29", R.drawable.avatar1));
        adapter = new CommentListAdapter(commentData, this);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Log.d("123", "Clicked on " + position);
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

        new GetCommentRequest(getCommentCallback, timelineModel.id, jwt);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
