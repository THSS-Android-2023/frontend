package com.example.internet.adapter.list;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.internet.R;
import com.example.internet.model.CommentModel;
import com.example.internet.model.NotificationModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentListAdapter extends BaseListAdapter<CommentModel>{
    public CommentListAdapter(List<CommentModel> data, Context ctx){
        super(R.layout.model_comment, data, ctx);
    }
    @Override
    protected void convert(BaseViewHolder holder, CommentModel item){
        holder.setText(R.id.username, item.nickname)
                .setText(R.id.content, item.content)
                .setText(R.id.timestamp, item.timestamp);
        ImageView avatarIv = holder.getView(R.id.avatar);
        Picasso.get().load(item.avatar).into(avatarIv);
    }
}
