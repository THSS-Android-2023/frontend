package com.example.internet.model;

import org.json.JSONObject;

public class FollowingModel {
    public String username;
    public String intro;
    public String avatar;
    public Boolean following;

//    public FollowingModel(String username, String intro, int avatar){
//        this.username = username;
//        this.intro = intro;
//        this.avatar = avatar;
//        this.following = false;
//    }

    public FollowingModel(JSONObject jsonObject){
        try {
            this.username = jsonObject.getString("username");
            this.intro = jsonObject.getString("intro");
            this.avatar = jsonObject.getString("avatar");
            this.following = jsonObject.getBoolean("is_current_user_following");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public FollowingModel(JSONObject jsonObject, Boolean following){
        try {
            this.username = jsonObject.getString("username");
            this.intro = jsonObject.getString("intro");
            this.avatar = jsonObject.getString("avatar");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.following = following;
    }
}
