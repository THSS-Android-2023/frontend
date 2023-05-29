package com.example.internet.activity;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Callback;
import okhttp3.Response;

public class FollowingActivity extends AppCompatActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private List<FollowingModel> data;
    private FollowingListAdapter adapter;

    String jwt;

    Callback getFollowingCallback = new Callback() {
        @Override
        public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
            Log.d("123", "onFailure: " + e.getMessage());
            runOnUiThread(() -> new ErrorDialog(FollowingActivity.this, "网络错误"));
        }

        @Override
        public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {
            String res = response.body().string();
            Log.d("123", "onResponse: " + res);
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
        data = new ArrayList<>();

//        data.add(new FollowingModel("pc20", "我叫pc，很吊的pc", R.drawable.avatar4));
//        data.add(new FollowingModel("xuhb20", "我好菜qwq", R.drawable.avatar1));
        adapter = new FollowingListAdapter(data, this, jwt);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Log.d("123", "Clicked on " + position);
        });
        adapter.setManager(recyclerView);
        recyclerView.setAdapter(adapter);

        new GetFollowingRequest(getFollowingCallback, jwt);
    }
}
