package com.example.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.internet.R;
import com.example.internet.adapter.list.FollowingListAdapter;
import com.example.internet.adapter.list.NotificationListAdapter;
import com.example.internet.model.FollowingModel;
import com.example.internet.model.NotificationModel;
import com.example.internet.model.TimelineModel;
import com.example.internet.request.GetMomentRequest;
import com.example.internet.request.GetNoticeRequest;
import com.example.internet.util.ErrorDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NotificationActivity extends BaseActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.noticed_recyclerview)
    RecyclerView notifiedRecyclerView;

    @BindView(R.id.seperator1)
    TextView seperator1;

    @BindView(R.id.seperator2)
    TextView seperator2;

    private List<NotificationModel> data;
    private NotificationListAdapter adapter;

    private List<NotificationModel> notifiedData;
    private NotificationListAdapter notifiedAdapter;

    Callback getNotificationCallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            Log.d("123", "onFailure: " + e.getMessage());
            new ErrorDialog(NotificationActivity.this, "获取通知失败");
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            int code = response.code();
            if (code != 200 && code != 201) {
                new ErrorDialog(NotificationActivity.this, "获取动态失败：" + response.message());
                return;
            }
            try {
                if (response.isSuccessful()) Log.d("response", "successful");
                String res = response.body().string();
                JSONArray jsonArray = new JSONArray(res);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    NotificationModel notificationModel = new NotificationModel(object);
                    if (notificationModel.hasNoticed) {
                        notifiedData.add(notificationModel);
                    } else {
                        data.add(notificationModel);
                    }
                }
                runOnUiThread(() -> {
                    if (data.size() == 0) {
                        seperator1.setText("暂无新通知");
                    }
                    if (notifiedData.size() == 0) {
                        seperator2.setText("暂无历史通知");
                    }
                    adapter.notifyDataSetChanged();
                    notifiedAdapter.notifyDataSetChanged();
                });
            }
             catch (Exception e) {
                e.printStackTrace();
            }
        };

    };

    Callback getDetailsCallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            new ErrorDialog(NotificationActivity.this, "获取动态失败");
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            int code = response.code();
            if (code != 200 && code != 201) {
                new ErrorDialog(NotificationActivity.this, "获取动态失败：" + response.message());
                return;
            }
            try {
                if (response.isSuccessful()) Log.d("response", "successful");
                String res = response.body().string();
                JSONObject object = new JSONObject(res);
                TimelineModel timelineModel = new TimelineModel(object);

                runOnUiThread(() -> {
                    String jsonString = new Gson().toJson(timelineModel);
                    Intent intent = new Intent(NotificationActivity.this, DetailsActivity.class);
                    intent.putExtra("timelineModelJson", jsonString);
                    intent.putExtra("jwt", jwt);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                });
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    String jwt;
    String username;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);

        jwt = getIntent().getStringExtra("jwt");
        username = getIntent().getStringExtra("username");
        data = new ArrayList<>();
        notifiedData = new ArrayList<>();

        adapter = new NotificationListAdapter(data, this);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            NotificationModel model = data.get(position);
            if (model.type != 0){
                new GetMomentRequest(getDetailsCallback, jwt, model.id);
            }
            else{
                Intent intent = new Intent(NotificationActivity.this, ChattingActivity.class);
                intent.putExtra("jwt", jwt);
                intent.putExtra("username", username);
                intent.putExtra("target", model.username);
                startActivity(intent);
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });
        adapter.setManager(recyclerView);
        recyclerView.setAdapter(adapter);

        notifiedAdapter = new NotificationListAdapter(notifiedData, this);
        notifiedAdapter.setOnItemClickListener((adapter, view, position) -> {
            NotificationModel model = notifiedData.get(position);
            if (model.type != 0){
                new GetMomentRequest(getDetailsCallback, jwt, model.id);
            }
            else{
                Intent intent = new Intent(NotificationActivity.this, ChattingActivity.class);
                intent.putExtra("jwt", jwt);
                intent.putExtra("username", username);
                intent.putExtra("target", model.username);
                startActivity(intent);
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });
        notifiedAdapter.setManager(notifiedRecyclerView);
        notifiedRecyclerView.setAdapter(notifiedAdapter);

        new GetNoticeRequest(getNotificationCallback, jwt);
    }
}
