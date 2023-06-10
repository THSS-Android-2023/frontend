package com.example.internet.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.internet.R;
import com.example.internet.activity.DetailsActivity;
import com.example.internet.activity.EditInfoActivity;
import com.example.internet.activity.FollowingActivity;
import com.example.internet.activity.LoginActivity;
import com.example.internet.activity.MainActivity;
import com.example.internet.adapter.list.TimelineListAdapter;
import com.example.internet.model.TimelineModel;
import com.example.internet.request.GetInfoRequest;
import com.example.internet.request.GetMomentRequest;
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

public class InfoFragment extends Fragment {
    String username;
    String nickname;
    String introduction;
    String avatar_url;
    int followers_num, followings_num;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    private List<TimelineModel> data;
    private TimelineListAdapter adapter;
    MainActivity ctx;


    @BindView(R.id.follower)
    LinearLayout follower_button;

    @BindView(R.id.following)
    LinearLayout following_button;

    @BindView(R.id.followings_num)
    TextView followings_num_text;

    @BindView(R.id.followers_num)
    TextView followers_num_text;

    @BindView(R.id.exit_button)
    Button exit_button;
    @BindView(R.id.edit_button)
    Button edit_button;


    @BindView(R.id.img_avatar)
    ImageView img_avatar;

    @BindView(R.id.introduction)
    TextView intro_textview;

    @BindView(R.id.username)
    TextView username_textview;


    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    final int EDIT_ACTIVITY_REQUEST_CODE = 100;

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
                nickname = jsonObject.getString("nickname");
                intro_textview.setText(introduction);
                avatar_url = jsonObject.getString("avatar");
                followers_num = jsonObject.getInt("followers_num");
                followings_num = jsonObject.getInt("followings_num");
                Log.d("followings_num", ""+followings_num);
//                Log.d("infoFragmentAvatar", avatar_url);
                if (avatar_url.isEmpty())
                    avatar_url = Global.EMPTY_AVATAR_URL;
                ctx.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 在此处执行需要在主线程中进行的 UI 操作
                        Picasso.get().load(avatar_url).into(img_avatar);
                        followers_num_text.setText("" + followers_num);
                        followings_num_text.setText("" + followings_num);
                        username_textview.setText(nickname);
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
            new ErrorDialog(ctx,  "Failed to get user's moment");
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            int code = response.code();
            if (code != 200 && code != 201)
                new ErrorDialog(ctx, "获取动态失败：" + response.message());
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);

        ButterKnife.bind(this, rootView);

        ctx = ((MainActivity) getActivity());

        username = ctx.getUsername();
        username_textview.setText(username);



        following_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, FollowingActivity.class);
                intent.putExtra("jwt", ctx.jwt);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditInfoActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("nickname", nickname);
                intent.putExtra("intro", introduction);
                intent.putExtra("avatar", avatar_url);
                intent.putExtra("jwt", ctx.jwt);
                startActivityForResult(intent, EDIT_ACTIVITY_REQUEST_CODE);
                ctx.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });



        data = new ArrayList<>();

        adapter = new TimelineListAdapter(data, getContext());
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Log.d("123", "Clicked on " + position);
            Gson gson = new Gson();
            String jsonString = gson.toJson(data.get(position));
            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra("jwt", ctx.jwt);
            intent.putExtra("username", username);
            intent.putExtra("timelineModelJson", jsonString);
            startActivity(intent);
            ctx.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        });
        adapter.setManager(recyclerView);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 在这里触发对后端的请求
                data.clear();
                new GetInfoRequest(updateInfoCallback, username, ctx.jwt);
                new GetMomentRequest(getUserMomentCallback, username, ctx.jwt, -1);
            }
        });

        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ctx)
                        .setTitle("退出登录")
                        .setMessage("确定要退出登录吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences sharedPreferences = ctx.getSharedPreferences("user", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("login", false);
                                editor.putString("username", "");
                                editor.putString("password", "");
                                editor.putString("jwt", "");
                                editor.apply();
                                Intent intent = new Intent(ctx, LoginActivity.class);
                                startActivity(intent);
                                ctx.finish();
                                ctx.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });


        new GetInfoRequest(updateInfoCallback, ctx.jwt);
        new GetMomentRequest(getUserMomentCallback, username, ctx.jwt, -1);

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_ACTIVITY_REQUEST_CODE) {
            new GetInfoRequest(updateInfoCallback, ctx.jwt);
        }
    }
}
