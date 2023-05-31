package com.example.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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

public class NotificationActivity extends AppCompatActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    private List<NotificationModel> data;
    private NotificationListAdapter adapter;

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
                    data.add(new NotificationModel(object));
                }
                runOnUiThread(() -> adapter.notifyDataSetChanged());
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

//        data.add(new NotificationModel("pc20", "评论了：“123”", "10:31", R.drawable.avatar4,R.drawable.pyq_41));
//        data.add(new NotificationModel("xuhb20", "赞了你的动态", "昨天 21:43", R.drawable.avatar1,R.drawable.pyq_31));
        adapter = new NotificationListAdapter(data, this);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Log.d("123", "Clicked on " + position);
            NotificationModel model = data.get(position);
            if (model.type != 0){
                new GetMomentRequest(getDetailsCallback, jwt, model.id);
            }
        });
        adapter.setManager(recyclerView);
        recyclerView.setAdapter(adapter);

        new GetNoticeRequest(getNotificationCallback, jwt);
    }
}
