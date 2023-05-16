package com.example.internet.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.internet.R;
import com.example.internet.adapter.list.NotificationListAdapter;
import com.example.internet.adapter.list.SearchResultListAdapter;
import com.example.internet.model.NotificationModel;
import com.example.internet.model.SearchResultModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.btn_search)
    Button searchButton;
    private List<SearchResultModel> data;
    private SearchResultListAdapter adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        data = new ArrayList<>();

        data.add(new SearchResultModel("这件事有人知道吗？", "10:31"));
        data.add(new SearchResultModel("有人要一起去吗", "昨天 20:19"));
        adapter = new SearchResultListAdapter(data, this);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Log.d("123", "Clicked on " + position);
        });
        adapter.setManager(recyclerView);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.GONE);
    }
}
