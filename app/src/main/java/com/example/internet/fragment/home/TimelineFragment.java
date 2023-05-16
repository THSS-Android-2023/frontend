package com.example.internet.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.internet.R;
import com.example.internet.activity.DetailsActivity;
import com.example.internet.activity.SearchActivity;
import com.example.internet.adapter.list.TimelineListAdapter;
import com.example.internet.model.TimelineModel;
import com.google.gson.Gson;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TimelineFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<TimelineModel> data;
    private TimelineListAdapter adapter;




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

        data = new ArrayList<>();

        data.add(new TimelineModel("pc20", R.drawable.avatar4,"11:05","【打卡美好生活】" , "校园春日即景，还看到了可爱的猫猫~", new int[]{R.drawable.pyq_41, R.drawable.pyq_42, R.drawable.pyq_43}));
        data.add(new TimelineModel("Pharos",R.drawable.avatar3, "11:19","【打卡美好生活】" , "和女朋友来吃火锅，看着真不错", new int[]{R.drawable.pyq_1, R.drawable.null_img, R.drawable.null_img}));
        data.add(new TimelineModel("Felix",R.drawable.avatar2,"11:25","【打卡美好生活】" , "喝一杯美式，唤起美好一天~", new int[]{R.drawable.pyq_2, R.drawable.null_img, R.drawable.null_img}));
        data.add(new TimelineModel("Hsu1023", R.drawable.avatar1, "11:49","【打卡美好生活】" , "天津之旅，看到了天津之眼和漂亮的夜景！", new int[]{R.drawable.pyq_31,R.drawable.pyq_32,R.drawable.pyq_33}));
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


}
