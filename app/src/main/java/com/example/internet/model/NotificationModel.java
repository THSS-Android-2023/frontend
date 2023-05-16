package com.example.internet.model;

public class NotificationModel {
    public int avatar;
    public String username;
    public String content;
    public String timestamp;
    public int img;

    public NotificationModel(String username, String content, String timestamp, int avatar, int img){
        this.username = username;
        this.content = content;
        this.timestamp = timestamp;
        this.avatar = avatar;
        this.img = img;
    }
}
