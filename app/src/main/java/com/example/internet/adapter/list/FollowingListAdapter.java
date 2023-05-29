package com.example.internet.adapter.list;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.internet.R;
import com.example.internet.model.FollowingModel;
import com.example.internet.request.FollowUserRequest;
import com.example.internet.request.UnfollowUserRequest;
import com.example.internet.util.ErrorDialog;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class FollowingListAdapter extends BaseListAdapter<FollowingModel>{


    Context ctx;
    String jwt;

    String username;

    Button btn;
//    Callback followCallback =
    public FollowingListAdapter(List<FollowingModel> data, Context ctx, String jwt){
        super(R.layout.model_following, data, ctx);
        this.ctx = ctx;
        this.jwt = jwt;
        this.username = username;
    }
    @Override
    protected void convert(BaseViewHolder holder, FollowingModel item){
        holder.setText(R.id.username, item.username)
                .setText(R.id.intro, item.intro);


        Picasso.get().load(item.avatar).into((ImageView) holder.getView(R.id.avatar));
        btn = holder.getView(R.id.following_btn);



        String text;
        String color;
        if (item.following) {
            text = "√ 已关注";
            color = "#E4AAEA";
        } else {
            text = "+ 关注";
            color = "#FFC0CB";
        }
        btn.setText(text);
        btn.setBackgroundColor(Color.parseColor(color));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.following) {
                    new UnfollowUserRequest(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            new ErrorDialog((AppCompatActivity) ctx, "关注失败");
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            // check return code
                            if (response.code() != 200 && response.code() != 201) {
                                new ErrorDialog((AppCompatActivity) ctx, "关注失败");
                                return;
                            }
                            item.following = false;
                            ((AppCompatActivity) ctx).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btn.setText("+  关注");
                                    btn.setBackgroundColor(Color.parseColor("#cccccc"));
                                }
                            });
                        }
                    }, item.username, jwt);
                } else {
                    new FollowUserRequest(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            new ErrorDialog((AppCompatActivity) ctx, "关注失败");
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            // check return code
                            if (response.code() != 200 && response.code() != 201) {
                                new ErrorDialog((AppCompatActivity) ctx, "关注失败");
                                return;
                            }
                            item.following = true;
                            ((AppCompatActivity) ctx).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btn.setText("√ 已关注");
                                    btn.setBackgroundColor(Color.parseColor("#E4AAEA"));
                                }

                            });
                        }
                    }, item.username, jwt);
                }
            }
        });

    }
}
