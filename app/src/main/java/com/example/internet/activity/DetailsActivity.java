package com.example.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.internet.R;
import com.example.internet.adapter.list.CommentListAdapter;
import com.example.internet.adapter.list.NotificationListAdapter;
import com.example.internet.model.CommentModel;
import com.example.internet.model.NotificationModel;
import com.example.internet.model.TimelineModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {
    @BindView(R.id.detail_username)TextView usernameView;

    @BindView(R.id.detail_avatar)ImageView avatarView;

    @BindView(R.id.detail_timestamp) TextView timestampView;

    @BindView(R.id.detail_title) TextView titleView;

    @BindView(R.id.detail_content) TextView contentView;

    @BindViews({R.id.detail_img1, R.id.detail_img2, R.id.detail_img3})
    ImageView[] img;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private List<CommentModel> data;
    private CommentListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String jsonString = intent.getStringExtra("timelineModelJson");

        Gson gson = new Gson();
        TimelineModel timelineModel = gson.fromJson(jsonString, TimelineModel.class);

        usernameView.setText(timelineModel.username);
        avatarView.setImageResource(timelineModel.avatar);
        timestampView.setText(timelineModel.timestamp);

        titleView.setText(timelineModel.title);
        contentView.setText(timelineModel.content);

        img[0].setImageResource(timelineModel.img[0]);
        img[1].setImageResource(timelineModel.img[1]);
        img[2].setImageResource(timelineModel.img[2]);

        data = new ArrayList<>();

        data.add(new CommentModel("pc20", "支持！！", "14:31", R.drawable.avatar4));
        data.add(new CommentModel("cjz20", "支持！！", "14:29", R.drawable.avatar2));
        data.add(new CommentModel("xuhb20", "哪天一起出去玩", "14:03", R.drawable.avatar1));
        data.add(new CommentModel("xuhb20", "彭老师也带带我", "13:51", R.drawable.avatar1));
        data.add(new CommentModel("cjz20", "彭老师带带我", "13:49", R.drawable.avatar2));
        data.add(new CommentModel("cjz20", "666", "13:31", R.drawable.avatar2));
        data.add(new CommentModel("xuhb20", "确实强", "13:29", R.drawable.avatar1));
        adapter = new CommentListAdapter(data, this);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Log.d("123", "Clicked on " + position);
        });
        adapter.setManager(recyclerView);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
