package com.example.internet.model;

import org.json.JSONObject;

public class CommentModel {
    public int avatar;
    public String username;
    public String content;
    public String timestamp;

    public CommentModel(String username, String content, String timestamp, int avatar){
        this.username = username;
        this.content = content;
        this.timestamp = timestamp;
        this.avatar = avatar;
    }

    public CommentModel(JSONObject jsonObject){
        try{
            username = jsonObject.getString("username");
            content = jsonObject.getString("content");
            timestamp = jsonObject.getString("time");
//            avatar = jsonObject.getInt("avatar");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
