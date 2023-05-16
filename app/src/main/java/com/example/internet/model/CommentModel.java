package com.example.internet.model;

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
}
