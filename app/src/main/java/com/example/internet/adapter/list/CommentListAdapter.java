package com.example.internet.adapter.list;

import android.content.Context;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.internet.R;
import com.example.internet.model.CommentModel;
import com.example.internet.model.NotificationModel;

import java.util.List;

public class CommentListAdapter extends BaseListAdapter<CommentModel>{
    public CommentListAdapter(List<CommentModel> data, Context ctx){
        super(R.layout.model_comment, data, ctx);
    }
    @Override
    protected void convert(BaseViewHolder holder, CommentModel item){
        holder.setImageResource(R.id.avatar, item.avatar)
                .setText(R.id.username, item.username)
                .setText(R.id.content, item.content)
                .setText(R.id.timestamp, item.timestamp);
    }
}
