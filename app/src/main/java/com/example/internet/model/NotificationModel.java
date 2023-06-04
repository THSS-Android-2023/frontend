package com.example.internet.model;

import org.json.JSONObject;

public class NotificationModel {
    public String avatar = "";
    public String username = "";
    public String content = "";
    public String timestamp = "";
    public int id;
    public String img = "";
    public int type;

//    public NotificationModel(String username, String content, String timestamp, int avatar, int img){
//        this.username = username;
//        this.content = content;
//        this.timestamp = timestamp;
//        this.avatar = avatar;
//        this.img = img;
//    }
    public NotificationModel(JSONObject jsonObject){
        try{
            this.username = jsonObject.getString("sender");
            this.avatar = jsonObject.getString("sender_avatar");
//            this.timestamp = jsonObject.getString("timestamp");

            this.type = Integer.valueOf(jsonObject.getString("_type"));
            String str = jsonObject.getString("content");
            if (type == 0){
                if (str.length() > 10)
                    this.content = username + "私信了你：" + str.subSequence(0, 10) + "...";
                else
                    this.content = username + "私信了你：" + str;
            }
            else if (type == 1){
                id = Integer.valueOf(str);
                this.content = username + "赞了你的帖子";
            }
            else if (type == 2){
                id = Integer.valueOf(str);
                this.content = username + "评论了你的帖子";
            }
            else if (type == 3){
                id = Integer.valueOf(str);
                this.content = "你关注的用户"+ username + "发布了新帖子";
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
