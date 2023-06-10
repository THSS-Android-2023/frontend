package com.example.internet.adapter.list;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.internet.R;
import com.example.internet.model.NotificationModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationListAdapter extends BaseListAdapter<NotificationModel>{
    public NotificationListAdapter(List<NotificationModel> data, Context ctx){
        super(R.layout.model_notification, data, ctx);
    }
    @Override
    protected void convert(BaseViewHolder holder, NotificationModel item){
        holder.setText(R.id.username, item.nickname)
                .setText(R.id.content, item.content)
                .setText(R.id.timestamp, item.timestamp);

        Picasso.get().load(item.avatar).into((ImageView) holder.getView(R.id.avatar));
        if (!item.img.isEmpty())
            Picasso.get().load(item.img).into((ImageView) holder.getView(R.id.img));
    }
}
