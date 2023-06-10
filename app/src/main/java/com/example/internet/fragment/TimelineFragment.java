package com.example.internet.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.internet.R;
import com.example.internet.activity.DetailsActivity;
import com.example.internet.activity.MainActivity;
import com.example.internet.adapter.list.TimelineListAdapter;
import com.example.internet.adapter.style.SpinnerTextAdapter;
import com.example.internet.model.TimelineModel;
import com.example.internet.request.GetMomentRequest;
import com.example.internet.util.ErrorDialog;
import com.example.internet.util.Global;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;

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

public class TimelineFragment extends Fragment {

    private final static int JMP_TO_DETAILS = 100;

    public final static int NEWEST_PAGE = 100;
    public final static int HOTTEST_PAGE = 101;
    public final static int FOLLOWINGS_PAGE = 102;
    public final static int PERSONAL_PAGE = 103;
    public final static int STARRED_PAGE = 104;
    public final static int TAGGED_PAGE = 105;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.tag_spinner)
    Spinner spinner;

    @BindView(R.id.filter_group)
    SingleSelectToggleGroup filterGroup;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.empty_hint)
    TextView emptyHint;

    @BindView(R.id.loadingAnimationView)
    LottieAnimationView loadingAnimationView;


    int pageAttr = 0;
    String tagItem = Global.TAG_STR2CODE_MAP.get(Global.TAG_LIST.get(0));

    String filterItem = "time";

    public static TimelineFragment newInstance(int param, Context ctx) {
        TimelineFragment fragment = new TimelineFragment();
        fragment.pageAttr = param;
        fragment.ctx = ctx;
        return fragment;
    }

    Context ctx = getContext();
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
            if (code != 200 && code != 201) {
                new ErrorDialog(ctx, "获取动态失败：" + response.message());
                return;
            }
            try{
                if (response.isSuccessful()) Log.d("response", "successful");
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
                        if (data.isEmpty())
                            emptyHint.setVisibility(View.VISIBLE);
                        else
                            emptyHint.setVisibility(View.GONE);
                        loadingAnimationView.cancelAnimation();
                        loadingAnimationView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
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
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_timeline, container, false);
        ButterKnife.bind(this, rootView);


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
            getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        });
        adapter.setManager(recyclerView);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // load more
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();

                if (lastVisibleItemPosition == totalItemCount - 1) {
                  pullMoment();
                }
            }
        });

        swipeRefreshLayout.bringToFront();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 在这里触发对后端的请求
//                loadingAnimationView.playAnimation();
                data.clear();
                pullMoment();
            }
        });


        // for tag spinner
        if (pageAttr != TAGGED_PAGE){
            spinner.setVisibility(View.GONE);
        }
        else {
            ArrayAdapter<String> tagAdapter =
                    new SpinnerTextAdapter(getActivity(), Global.TAG_LIST, 14);
            tagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(tagAdapter);
            spinner.setSelection(0);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("123", spinner.getSelectedItem().toString());
                    tagItem = Global.TAG_STR2CODE_MAP.get(spinner.getSelectedItem().toString());
                    data.clear();
                    pullMoment();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

        // for filter group
        if (pageAttr != FOLLOWINGS_PAGE && pageAttr != TAGGED_PAGE){
            filterGroup.setVisibility(View.GONE);
        }
        else {
            filterGroup.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                    if (checkedId == R.id.newest)
                        filterItem = Global.FILTER_LIST.get(0);
                    else if (checkedId == R.id.like)
                        filterItem = Global.FILTER_LIST.get(1);
                    else if (checkedId == R.id.comment)
                        filterItem = Global.FILTER_LIST.get(2);
                    Log.d("filterItem", filterItem);
                    data.clear();
                    pullMoment();
                }
            });
        };
        pullMoment();
        return rootView;
    }

    private void pullMoment(){


        emptyHint.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (data.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    loadingAnimationView.setVisibility(View.VISIBLE);
                    loadingAnimationView.playAnimation();
                    if (pageAttr == FOLLOWINGS_PAGE) {
                        new GetMomentRequest(refreshMomentCallback, pageAttr, filterItem, jwt, -1);
                    } else if (pageAttr == TAGGED_PAGE)
                        new GetMomentRequest(refreshMomentCallback, pageAttr, tagItem, filterItem, jwt, -1);
                    else
                        new GetMomentRequest(refreshMomentCallback, pageAttr, jwt, -1);
                }
                else {
                    int lastId = data.get(data.size() - 1).id;
                    if (pageAttr == FOLLOWINGS_PAGE) {
                        new GetMomentRequest(refreshMomentCallback, pageAttr, filterItem, jwt, lastId);
                    } else if (pageAttr == TAGGED_PAGE)
                        new GetMomentRequest(refreshMomentCallback, pageAttr, tagItem, filterItem, jwt, lastId);
                    else
                        new GetMomentRequest(refreshMomentCallback, pageAttr, jwt, lastId);
                }
            }
        }, 500);



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
