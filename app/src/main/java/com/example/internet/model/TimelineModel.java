package com.example.internet.model;

import android.os.Parcelable;

public class TimelineModel {
    public String title = "";
    public String content = "";
    public String username = "";
    public String timestamp = "";

    public int avatar = 0;
    public int img[] = {0,0,0};

    public TimelineModel(String username, int avatar, String timestamp, String title, String content, int[] img) {
        this.title = title;
        this.content = content;
        this.username = username;
        this.avatar = avatar;
        this.timestamp = timestamp;
        this.img = img;
    }
}

