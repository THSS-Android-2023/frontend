package com.example.internet.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.internet.R;
import com.example.internet.adapter.list.FollowingListAdapter;
import com.example.internet.model.FollowingModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowingActivity extends AppCompatActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    private List<FollowingModel> data;
    private FollowingListAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        ButterKnife.bind(this);
        data = new ArrayList<>();

        data.add(new FollowingModel("pc20", "我叫pc，很吊的pc", R.drawable.avatar4));
        data.add(new FollowingModel("xuhb20", "我好菜qwq", R.drawable.avatar1));
        adapter = new FollowingListAdapter(data, this);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Log.d("123", "Clicked on " + position);
        });
        adapter.setManager(recyclerView);
        recyclerView.setAdapter(adapter);


    }
}
