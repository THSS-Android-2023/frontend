package com.example.internet.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.internet.R;
import com.example.internet.adapter.list.FollowingListAdapter;
import com.example.internet.adapter.list.NotificationListAdapter;
import com.example.internet.model.FollowingModel;
import com.example.internet.model.NotificationModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationActivity extends AppCompatActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    private List<NotificationModel> data;
    private NotificationListAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        data = new ArrayList<>();

        data.add(new NotificationModel("pc20", "评论了：“123”", "10:31", R.drawable.avatar4,R.drawable.pyq_41));
        data.add(new NotificationModel("xuhb20", "赞了你的动态", "昨天 21:43", R.drawable.avatar1,R.drawable.pyq_31));
        adapter = new NotificationListAdapter(data, this);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Log.d("123", "Clicked on " + position);
        });
        adapter.setManager(recyclerView);
        recyclerView.setAdapter(adapter);
    }
}
