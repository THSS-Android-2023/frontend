package com.example.internet.model;

import android.os.Parcelable;
import android.util.Log;

import com.example.internet.util.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TimelineModel {
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

    public int avatar = 0;

    public List<String> imgUris = new ArrayList<>();
    public List<String> imgUrls = new ArrayList<>();

    public TimelineModel(String username, int avatar, String timestamp, String title, String content) {

        this.title = title;
        this.content = content;
        this.username = username;
        this.avatar = avatar;
        this.timestamp = timestamp;

        int random_num = (int) (Math.random() * 9) + 1;
        for (int i = 0; i < random_num; i++) {
            imgUris.add("content://com.android.providers.media.documents/document/image%3A31");
        }
    }

    public TimelineModel(JSONObject jsonObject) {
        try {
//            Log.d("TimelineModel", jsonObject.toString());
            username = jsonObject.getString("username");
            id = jsonObject.getInt("id");
            tag = jsonObject.getString("tag");
            title = jsonObject.getString("title");
            content = jsonObject.getString("content");
            timestamp = jsonObject.getString("time");
            location = jsonObject.getString("location");

            numComments = jsonObject.getInt("comment_nums");
            numImages = jsonObject.getInt("img_nums");
            numStars = jsonObject.getInt("star_nums");
            numLikes = jsonObject.getInt("like_nums");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        for (int i = 1; i <= numImages; i++) {
            imgUrls.add(Global.API_URL + "/static/" + id + "_" + i + ".jpg");
        }

    }
}

