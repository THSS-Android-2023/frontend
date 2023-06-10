package com.example.internet.adapter.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.internet.R;
import com.example.internet.holder.MomentDataHolder;
import com.example.internet.model.TimelineModel;
import com.example.internet.util.Global;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TimelineListAdapter extends BaseListAdapter<TimelineModel>{
    public TimelineListAdapter(List<TimelineModel> data, Context ctx){
        super(R.layout.model_timeline, data, ctx);

    }

    @Override
    protected void convert(BaseViewHolder holder, TimelineModel item){
        holder.setText(R.id.username, item.nickname)
                .setText(R.id.timestamp, item.timestamp)
                .setText(R.id.title, item.title)
                .setText(R.id.content, item.content)
                .setText(R.id.like_num, ""+item.numLikes)
                .setText(R.id.comment_num, ""+item.numComments)
                .setText(R.id.star_num, ""+item.numStars);

        if (item.isLike)
            holder.setImageResource(R.id.like_view, R.drawable.like_red);
        else
            holder.setImageResource(R.id.like_view, R.drawable.like_grey);

        if (item.isStar)
            holder.setImageResource(R.id.star_view, R.drawable.star_blue);
        else
            holder.setImageResource(R.id.star_view, R.drawable.star_grey);

        if (!item.location.isEmpty())
            holder.setText(R.id.location_text, item.location);
        else
            holder.getView(R.id.location_text).setVisibility(View.GONE);


        ImageView avatarIv = holder.getView(R.id.avatar);
        Picasso.get().load(item.avatar).into(avatarIv);

        if (item.mp4Url.isEmpty()) {
            ((MomentDataHolder) holder).bindData(item.imgUrls, item.content);
        } else {
            ((MomentDataHolder) holder).bindData(item.mp4Url, item.content);
        }

        ((TextView)holder.getView(R.id.tag)).setText(Global.TAG_CODE2STR_MAP.get(item.tag));
    }

    @Override
    protected MomentDataHolder onCreateDefViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_timeline, parent, false);
        return new MomentDataHolder(view);
    }

}
