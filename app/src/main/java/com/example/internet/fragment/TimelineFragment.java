package com.example.internet.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


    int pageAttr = 0;
    String tagItem = "xyzx";

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
        });
        adapter.setManager(recyclerView);
        recyclerView.setAdapter(adapter);

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


        // for tag spinner
        if (pageAttr != TAGGED_PAGE){
            spinner.setVisibility(View.GONE);
        }
        else {
            ArrayAdapter<String> tagAdapter =
                    new SpinnerTextAdapter(getActivity(), new ArrayList<>(Global.TAG_CODE2STR_MAP.values()), 14);
            tagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(tagAdapter);
            spinner.setSelection(3);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("123", spinner.getSelectedItem().toString());
                    tagItem = Global.TAG_STR2CODE_MAP.get(spinner.getSelectedItem().toString());
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
                    pullMoment();
                }
            });
        };
        pullMoment();
        return rootView;
    }

    private void pullMoment(){

        if (pageAttr == FOLLOWINGS_PAGE) {
            new GetMomentRequest(refreshMomentCallback, pageAttr, filterItem, jwt);
            Log.d("HERE","HERE");
        }
        else if (pageAttr == TAGGED_PAGE)
            new GetMomentRequest(refreshMomentCallback, pageAttr, tagItem, filterItem, jwt);
        else
            new GetMomentRequest(refreshMomentCallback, pageAttr, jwt);
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
