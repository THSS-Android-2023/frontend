package com.example.internet.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.internet.R;
import com.example.internet.activity.DetailsActivity;
import com.example.internet.activity.MainActivity;
import com.example.internet.adapter.list.TimelineListAdapter;
import com.example.internet.model.TimelineModel;
import com.example.internet.request.GetMomentRequest;
import com.example.internet.util.ErrorDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TimelineFragment extends Fragment {

    private final static int JMP_TO_DETAILS = 100;

    public final static int NEWEST_PAGE = 100;
    public final static int HOTTEST_PAGE = 101;
    public final static int FOLLOWINGS_PAGE = 102;
    public final static int PERSONAL_PAGE = 103;

    public final static int STARRED_PAGE = 104;


    int pageAttr = 0;

    public static TimelineFragment newInstance(int param, Context ctx) {
        TimelineFragment fragment = new TimelineFragment();
        fragment.pageAttr = param;
        fragment.ctx = ctx;
        return fragment;
    }

    Context ctx = getContext();
    private RecyclerView recyclerView;
    private List<TimelineModel> data;
    private TimelineListAdapter adapter;
    private String jwt;

    private String username;

    Callback refreshMomentCallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            int code = response.code();
            Log.d("code", String.valueOf(code));
            if (code != 200 && code != 201)
                new ErrorDialog(ctx, "获取动态失败：" + response.message());
            try{
                if (response.isSuccessful()) Log.d("response", "successful");
                String responseBody = response.body().string();
                Log.d("responseBody", responseBody);

                JSONArray jsonArray = new JSONArray(responseBody);

                data.clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Log.d("responseBody", jsonObject.toString());

                    TimelineModel moment = new TimelineModel(jsonObject);
                    data.add(moment);
                    Log.d("moment len", data.size() + "");
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_timeline, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerview);

        jwt = ((MainActivity) getActivity()).getJwt();
        username = ((MainActivity) getActivity()).getUsername();

        data = new ArrayList<>();

        adapter = new TimelineListAdapter(data, getContext());
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Log.d("123", "Clicked on " + position);
            Gson gson = new Gson();
            String jsonString = gson.toJson(data.get(position));
            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra("timelineModelJson", jsonString);
            intent.putExtra("jwt", jwt);
            intent.putExtra("username", username);
            startActivityForResult(intent, JMP_TO_DETAILS);
        });
        adapter.setManager(recyclerView);
        recyclerView.setAdapter(adapter);

        new GetMomentRequest(refreshMomentCallback, pageAttr, jwt);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();

                if (firstVisibleItemPosition == 0) {
                    Log.d("123", "refresh");
                    new GetMomentRequest(refreshMomentCallback, pageAttr, jwt);

                }

                if (lastVisibleItemPosition == totalItemCount - 1) {
                    Log.d("123", "load more");
                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == JMP_TO_DETAILS && resultCode == Activity.RESULT_OK) {
                String jsonString = data.getStringExtra("timelineModelJson");
                Gson gson = new Gson();
                TimelineModel timelineModel = gson.fromJson(jsonString, TimelineModel.class);
                int id = timelineModel.id;
                int index = -1;
                for (int i = 0; i < this.data.size(); i++) {
                    if (this.data.get(i).id == id) {
                        index = i;
                        break;
                    }
                }
                if (index == -1) return;
                this.data.set(index, timelineModel);

                adapter.notifyDataSetChanged();

        }
    }


}
