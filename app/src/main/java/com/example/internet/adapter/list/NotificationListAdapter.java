package com.example.internet.adapter.list;

import android.content.Context;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.internet.R;
import com.example.internet.model.NotificationModel;

import java.util.List;

public class NotificationListAdapter extends BaseListAdapter<NotificationModel>{
    public NotificationListAdapter(List<NotificationModel> data, Context ctx){
        super(R.layout.model_notification, data, ctx);
    }
    @Override
    protected void convert(BaseViewHolder holder, NotificationModel item){
        holder.setImageResource(R.id.avatar, item.avatar)
                .setImageResource(R.id.img, item.img)
                .setText(R.id.username, item.username)
                .setText(R.id.content, item.content)
                .setText(R.id.timestamp, item.timestamp);
    }
}
