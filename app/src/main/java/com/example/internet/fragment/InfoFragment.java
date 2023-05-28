package com.example.internet.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.example.internet.R;
import com.example.internet.activity.DetailsActivity;
import com.example.internet.activity.EditInfoActivity;
import com.example.internet.activity.FollowingActivity;
import com.example.internet.activity.MainActivity;
import com.example.internet.adapter.list.TimelineListAdapter;
import com.example.internet.model.TimelineModel;
import com.example.internet.request.GetInfoRequest;
import com.example.internet.util.Global;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
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
    String introduction;
    String avatar_url;


    private RecyclerView recyclerView;
    private List<TimelineModel> data;
    private TimelineListAdapter adapter;
    MainActivity ctx;

    @BindView(R.id.follower)
    LinearLayout follower_button;

    @BindView(R.id.following)
    LinearLayout following_button;

    @BindView(R.id.edit_button)
    Button edit_button;


    @BindView(R.id.img_avatar)
    ImageView img_avatar;

    @BindView(R.id.introduction)
    TextView intro_textview;

    @BindView(R.id.username)
    TextView username_textview;

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
                intro_textview.setText(introduction);
                avatar_url = jsonObject.getString("avatar");
                Log.d("infoFragmentAvatar", avatar_url);
                if (avatar_url.isEmpty())
                    avatar_url = Global.EMPTY_AVATAR_URL;
                ctx.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 在此处执行需要在主线程中进行的 UI 操作
                        Picasso.get().load(avatar_url).into(img_avatar);
                    }
                });
            }
            catch (JSONException e){
                Log.d("Error", e.toString());
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


        new GetInfoRequest(updateInfoCallback, ctx.jwt);

        following_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, FollowingActivity.class);
                intent.putExtra("jwt", ctx.jwt);
                startActivity(intent);
            }
        });
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditInfoActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("intro", introduction);
                intent.putExtra("avatar", avatar_url);
                intent.putExtra("jwt", ctx.jwt);
                startActivityForResult(intent, EDIT_ACTIVITY_REQUEST_CODE);
            }
        });


        recyclerView = rootView.findViewById(R.id.recyclerview);

        data = new ArrayList<>();

//        data.add(new TimelineModel("xuhb20", R.drawable.avatar1,"11:05","【打卡美好生活】" , "校园春日即景，还看到了可爱的猫猫~",));
//        data.add(new TimelineModel("xuhb20",R.drawable.avatar1, "11:19","【打卡美好生活】" , "和女朋友来吃火锅，看着真不错"));
//        data.add(new TimelineModel("xuhb20",R.drawable.avatar1,"11:25","【打卡美好生活】" , "喝一杯美式，唤起美好一天~"));
//        data.add(new TimelineModel("xuhb20", R.drawable.avatar1, "11:49","【打卡美好生活】" , "天津之旅，看到了天津之眼和漂亮的夜景！"));
        adapter = new TimelineListAdapter(data, getContext());
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Log.d("123", "Clicked on " + position);
            Gson gson = new Gson();
            String jsonString = gson.toJson(data.get(position));
            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra("timelineModelJson", jsonString);
            startActivity(intent);
        });
        adapter.setManager(recyclerView);
        recyclerView.setAdapter(adapter);

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
