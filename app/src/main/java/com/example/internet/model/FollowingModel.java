package com.example.internet.model;

public class FollowingModel {
    public String username;
    public String intro;
    public int avatar;
    public Boolean following;

    public FollowingModel(String username, String intro, int avatar){
        this.username = username;
        this.intro = intro;
        this.avatar = avatar;
        this.following = false;
    }
}
