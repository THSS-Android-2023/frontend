package com.example.internet.model;

import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class TimelineModel {
    public String title = "";
    public String content = "";
    public String username = "";
    public String timestamp = "";

    public int avatar = 0;
    public int img[] = {0,0,0};

    public List<String> imgUris = new ArrayList<>();

    public TimelineModel(String username, int avatar, String timestamp, String title, String content, int[] img) {
        this.title = title;
        this.content = content;
        this.username = username;
        this.avatar = avatar;
        this.timestamp = timestamp;
        this.img = img;

        int random_num = (int) (Math.random() * 9) + 1;
        for (int i = 0; i < random_num; i++) {
            imgUris.add("content://com.android.providers.media.documents/document/image%3A31");
        }
    }
}

