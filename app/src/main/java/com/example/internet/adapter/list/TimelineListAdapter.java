package com.example.internet.adapter.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.internet.R;
import com.example.internet.holder.MomentDataHolder;
import com.example.internet.model.TimelineModel;

import java.util.List;

public class TimelineListAdapter extends BaseListAdapter<TimelineModel>{
    public TimelineListAdapter(List<TimelineModel> data, Context ctx){
        super(R.layout.model_timeline, data, ctx);
    }

    @Override
    protected void convert(BaseViewHolder holder, TimelineModel item){
        holder.setImageResource(R.id.avatar, item.avatar)
                .setText(R.id.username, item.username)
                .setText(R.id.timestamp, item.timestamp)
                .setText(R.id.title, item.title);
//                .setText(R.id.content, item.content);
//                .setImageResource(R.id.img1, item.img[0])
//                .setImageResource(R.id.img2, item.img[1])
//                .setImageResource(R.id.img3, item.img[2]);
        ((MomentDataHolder) holder).bindData(item.imgUrls, item.content);

    }

    @Override
    protected MomentDataHolder onCreateDefViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_timeline, parent, false);
        return new MomentDataHolder(view);
    }

}
