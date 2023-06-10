package com.example.internet.model;

import android.os.Parcelable;
import android.util.Log;

import com.example.internet.R;
import com.example.internet.util.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TimelineModel {

    public String nickname = "";
    public String title = "";
    public String content = "";
    public String username = "";
    public String timestamp = "";
    public String tag = "";
    public int id;
    public String location = "";
    public int numComments = 0;
    public int numImages = 0;
    public int numStars = 0;
    public int numLikes = 0;

    public String avatar;

    public Boolean isStar = false;
    public Boolean isLike = false;

    public Boolean isFollow = false;
    public List<String> imgUrls = new ArrayList<>();

    public String mp4Url;

    public TimelineModel(JSONObject jsonObject) {
        try {
//            Log.d("TimelineModel", jsonObject.toString());
            username = jsonObject.getString("username");
            nickname = jsonObject.getString("nickname");
            id = jsonObject.getInt("id");
            tag = jsonObject.getString("tag");
            title = jsonObject.getString("title");
            content = jsonObject.getString("content");
            timestamp = jsonObject.getString("time");
            location = jsonObject.getString("location");
            avatar = jsonObject.getString("avatar");

            numComments = jsonObject.getInt("comment_nums");
            numImages = jsonObject.getInt("img_nums");
            numStars = jsonObject.getInt("star_nums");
            numLikes = jsonObject.getInt("like_nums");

            isStar = jsonObject.getBoolean("is_current_user_star");
            isLike = jsonObject.getBoolean("is_current_user_like");
            isFollow = jsonObject.getBoolean("is_current_user_following");
            mp4Url = jsonObject.getString("mp4url");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        for (int i = 1; i <= numImages; i++) {
            String url = Global.API_URL + "/static/moment_imgs/" + id + "_" + i + ".jpg";
            imgUrls.add(url);
            Log.d("url", url);
        }
    }
}