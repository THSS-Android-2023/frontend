package com.example.internet.adapter.list;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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

        Button btn = holder.getView(R.id.following_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = btn.getText().toString();
                String color = "";
                if (text.equals("+  关注")){
                    text = "√ 已关注";
                    color = "#E4AAEA";
                }
                else {
                    text = "+  关注";
                    color = "#cccccc";
                }
                btn.setText(text);
                btn.setBackgroundColor(Color.parseColor(color));
            }
        });
    }
}
