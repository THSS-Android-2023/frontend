package com.example.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import com.example.internet.model.TimelineModel;
import com.example.internet.request.GetSearchResultRequest;
import com.example.internet.util.ErrorDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.btn_search)
    Button searchButton;

    @BindView(R.id.et_search)
    EditText edit;

    String jwt;

    ArrayList<TimelineModel> timelineModels = new ArrayList<>();

    Callback getSearchResultCallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {

        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                int code = response.code();
                Log.d("code", String.valueOf(code));
                if (code != 200 && code != 201)
                    new ErrorDialog(SearchActivity.this, "获取搜索结果失败：" + response.message());
                try{
                    if (response.isSuccessful()) Log.d("response", "successful");
                    String responseBody = response.body().string();
//                    Log.d("responseBody", responseBody);


                    timelineModels.clear();
                    data.clear();
                    JSONArray jsonArray = new JSONArray(responseBody);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String content = jsonObject.getString("content");
                        String time = jsonObject.getString("time");
                        data.add(new SearchResultModel(id, content, time));
                        timelineModels.add(new TimelineModel(jsonObject));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    };
    private List<SearchResultModel> data;
    private SearchResultListAdapter adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        jwt = getIntent().getStringExtra("jwt");


        data = new ArrayList<>();

//        data.add(new SearchResultModel("这件事有人知道吗？", "10:31"));
//        data.add(new SearchResultModel("有人要一起去吗", "昨天 20:19"));
        adapter = new SearchResultListAdapter(data, this);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Log.d("123", "Clicked on " + position);
//            Log.d("timelineModels", timelineModels.get(position).toString());
            Intent intent = new Intent(this, DetailsActivity.class);
            Gson gson = new Gson();
            String json = gson.toJson(timelineModels.get(position));
            intent.putExtra("timelineModelJson", json);
            intent.putExtra("jwt", jwt);
            startActivity(intent);
        });

        adapter.setManager(recyclerView);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String keyWords = edit.getText().toString();
                if (!keyWords.isEmpty()){
                    new GetSearchResultRequest(getSearchResultCallback ,keyWords, jwt);
                }
            }
        });
        recyclerView.setAdapter(adapter);
//        recyclerView.setVisibility(View.GONE);


    }
}
