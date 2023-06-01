package com.example.internet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.internet.R;
import com.example.internet.adapter.list.TimelineListAdapter;
import com.example.internet.model.TimelineModel;
import com.example.internet.request.BlacklistRequest;
import com.example.internet.request.FollowUserRequest;
import com.example.internet.request.GetInfoRequest;
import com.example.internet.request.GetMomentRequest;
import com.example.internet.request.UnblacklistRequest;
import com.example.internet.request.UnfollowUserRequest;
import com.example.internet.util.ErrorDialog;
import com.example.internet.util.Global;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserInfoActivity extends BaseActivity {

    String curUsername;
    String username;

    String introduction;
    String avatar_url;

    int followers_num, followings_num;

    Boolean isFollow = false, isBlacklist = false;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    private List<TimelineModel> data;
    private TimelineListAdapter adapter;

    @BindView(R.id.follower)
    LinearLayout follower_button;

    @BindView(R.id.following)
    LinearLayout following_button;

    @BindView(R.id.followings_num)
    TextView followings_num_text;

    @BindView(R.id.followers_num)
    TextView followers_num_text;

    @BindView(R.id.img_avatar)
    ImageView img_avatar;

    @BindView(R.id.introduction)
    TextView intro_textview;

    @BindView(R.id.username)
    TextView username_textview;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    String jwt;

    Callback followUserCallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            new ErrorDialog(UserInfoActivity.this, "关注失败");
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            // check return code
            if (response.code() != 200 && response.code() != 201) {
                new ErrorDialog(UserInfoActivity.this, "关注失败");
                return;
            }
            isFollow = true;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    changeBtnState();
                }

            });
        }
    };


    Callback unfollowUserCallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            new ErrorDialog(UserInfoActivity.this, "取消关注失败");
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            // check return code
            if (response.code() != 200 && response.code() != 201) {
                new ErrorDialog(UserInfoActivity.this, "取消关注失败");
                return;
            }
            isFollow = false;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    changeBtnState();
                }
            });
        }
    };

    Callback blacklistCallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            new ErrorDialog(UserInfoActivity.this, "拉黑失败");
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            // check return code
            if (response.code() != 200 && response.code() != 201) {
                new ErrorDialog(UserInfoActivity.this, "拉黑失败");
                return;
            }
            isBlacklist = true;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    changeBtnState();
                }
            });
        }
    };

    Callback unblacklistCallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            new ErrorDialog(UserInfoActivity.this, "取消拉黑失败");
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            // check return code
            if (response.code() != 200 && response.code() != 201) {
                new ErrorDialog(UserInfoActivity.this, "取消拉黑失败");
                return;
            }
            isBlacklist = false;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    changeBtnState();
                }
            });
        }
    };

    Callback updateInfoCallback = new Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.d("Error", e.toString());}
        @Override
        public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
            String resStr = Objects.requireNonNull(response.body()).string();
            int code = response.code();
            Log.d("in", "in");
            Log.d("code", String.valueOf(code));
            try {
                Log.d("resStr", resStr);
                JSONObject jsonObject = new JSONObject(resStr);
                introduction = jsonObject.getString("intro");
                intro_textview.setText(introduction);
                avatar_url = jsonObject.getString("avatar");
                followers_num = jsonObject.getInt("followers_num");
                followings_num = jsonObject.getInt("followings_num");
                isFollow = jsonObject.getBoolean("is_following");
                isBlacklist = jsonObject.getBoolean("is_blacked");
                Log.d("followings_num", ""+followings_num);
//                Log.d("infoFragmentAvatar", avatar_url);
                if (avatar_url.isEmpty())
                    avatar_url = Global.EMPTY_AVATAR_URL;
                UserInfoActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 在此处执行需要在主线程中进行的 UI 操作
                        Picasso.get().load(avatar_url).into(img_avatar);
                        followers_num_text.setText("" + followers_num);
                        followings_num_text.setText("" + followings_num);
                        changeBtnState();
                    }
                });
            }
            catch (JSONException e){
                Log.d("Error", e.toString());
            }
        }
    };

    Callback getUserMomentCallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            new ErrorDialog(UserInfoActivity.this,  "Failed to get user's moment");
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            int code = response.code();
            if (code != 200 && code != 201)
                new ErrorDialog(UserInfoActivity.this, "获取动态失败：" + response.message());
            try{
                if (response.isSuccessful()) Log.d("INFOFRAGMENT", "successful");
                String responseBody = response.body().string();
                Log.d("responseBody", responseBody);

                JSONArray jsonArray = new JSONArray(responseBody);

                List<TimelineModel> newData = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Log.d("responseBody", jsonObject.toString());
                    TimelineModel moment = new TimelineModel(jsonObject);
                    newData.add(moment);
                }
                if (newData.size() == 0) return;
                for (int i = 0; i < data.size(); i++){
                    if (data.get(i).id == newData.get(0).id)
                        return;
                }
                data.addAll(newData);

            } catch(Exception e){
                e.printStackTrace();
            } finally {
                UserInfoActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    };


    void changeBtnState(){
        if(isFollow){
            followBtn.setText("取消关注");
        }
        else{
            followBtn.setText("+ 关注");
        }
        if(isBlacklist){
            blacklistBtn.setText("取消拉黑");
        }
        else{
            blacklistBtn.setText("⛔ ︎拉黑");
        }
    }

    @BindView(R.id.follow_btn)
    Button followBtn;

    @BindView(R.id.blacklist_btn)
    Button blacklistBtn;

    @BindView(R.id.chat_btn)
    Button chatBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        curUsername = intent.getStringExtra("curUsername");
        username_textview.setText(curUsername);
        jwt = intent.getStringExtra("jwt");

        if(username.equals(curUsername)){
            followBtn.setVisibility(Button.GONE);
            blacklistBtn.setVisibility(Button.GONE);
            chatBtn.setVisibility(Button.GONE);
        }
        else {

        }
        data = new ArrayList<>();

        adapter = new TimelineListAdapter(data, this);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Gson gson = new Gson();
            String jsonString = gson.toJson(data.get(position));
            Intent newIntent = new Intent(UserInfoActivity.this, DetailsActivity.class);
            newIntent.putExtra("jwt", jwt);
            newIntent.putExtra("username", username);
            newIntent.putExtra("timelineModelJson", jsonString);
            startActivity(newIntent);
        });
        adapter.setManager(recyclerView);
        recyclerView.setAdapter(adapter);

        blacklistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBlacklist){
                    new UnblacklistRequest(unblacklistCallback, username, jwt);
                }
                else{
                    new BlacklistRequest(blacklistCallback, username, jwt);
                }
            }
        });

        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFollow){
                    new UnfollowUserRequest(unfollowUserCallback, username, jwt);
                }
                else{
                    new FollowUserRequest(followUserCallback, username, jwt);
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 在这里触发对后端的请求
                data.clear();
                new GetInfoRequest(updateInfoCallback, username, jwt);
                new GetMomentRequest(getUserMomentCallback, username, jwt, -1);
            }
        });


        new GetInfoRequest(updateInfoCallback, username, jwt);

        new GetMomentRequest(getUserMomentCallback, username, jwt, -1);

    }
}
