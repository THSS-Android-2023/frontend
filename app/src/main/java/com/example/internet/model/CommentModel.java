package com.example.internet.model;

import org.json.JSONObject;

public class CommentModel {
    public String avatar;
    public String username;
    public String content;
    public String timestamp;

    public CommentModel(JSONObject jsonObject){
        try{
            username = jsonObject.getString("username");
            content = jsonObject.getString("content");
            timestamp = jsonObject.getString("time");
            avatar = jsonObject.getString("avatar");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
