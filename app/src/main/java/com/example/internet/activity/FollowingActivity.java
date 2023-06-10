package com.example.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.internet.R;
import com.example.internet.adapter.list.FollowingListAdapter;
import com.example.internet.model.FollowingModel;
import com.example.internet.request.GetFollowingRequest;
import com.example.internet.util.ErrorDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Callback;
import okhttp3.Response;

public class FollowingActivity extends BaseActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private List<FollowingModel> data;
    private FollowingListAdapter adapter;

    String jwt;
    String username;

    Callback getFollowingCallback = new Callback() {
        @Override
        public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
            Log.d("123", "onFailure: " + e.getMessage());
            runOnUiThread(() -> new ErrorDialog(FollowingActivity.this, "网络错误"));
        }

        @Override
        public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {
            String res = response.body().string();
            try {
                JSONArray followingList = new JSONArray(res);
                data.clear();
                for (int i = 0; i < followingList.length(); i++) {
                    JSONObject following = followingList.getJSONObject(i);
                    data.add(new FollowingModel(following, true));
                }
                runOnUiThread(() -> adapter.notifyDataSetChanged());
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> new ErrorDialog(FollowingActivity.this, "数据解析错误"));
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        ButterKnife.bind(this);

        jwt = getIntent().getStringExtra("jwt");
        username = getIntent().getStringExtra("username");
        data = new ArrayList<>();

        adapter = new FollowingListAdapter(data, this, jwt);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Log.d("123", "Clicked on " + position);
        });
        adapter.setManager(recyclerView);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Gson gson = new Gson();
            String jsonString = gson.toJson(data.get(position));
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("curUsername", username);
            intent.putExtra("jwt", jwt);
            intent.putExtra("username", data.get(position).username);
            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        });
        recyclerView.setAdapter(adapter);

        new GetFollowingRequest(getFollowingCallback, jwt);
    }
}
