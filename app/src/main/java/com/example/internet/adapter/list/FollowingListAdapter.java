package com.example.internet.adapter.list;

import android.content.Context;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.internet.R;
import com.example.internet.model.FollowingModel;
import com.example.internet.model.TimelineModel;

import java.util.List;

public class FollowingListAdapter extends BaseListAdapter<FollowingModel>{
    public FollowingListAdapter(List<FollowingModel> data, Context ctx){
        super(R.layout.model_following, data, ctx);
    }
    @Override
    protected void convert(BaseViewHolder holder, FollowingModel item){
        holder.setImageResource(R.id.avatar, item.avatar)
                .setText(R.id.username, item.username)
                .setText(R.id.intro, item.intro);
    }
}
