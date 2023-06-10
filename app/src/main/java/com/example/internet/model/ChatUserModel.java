package com.example.internet.model;

import com.stfalcon.chatkit.commons.models.IUser;

public class ChatUserModel implements IUser {

    private String id;
    private String name;
    private String avatar;

    public ChatUserModel(String name, String avatar, boolean online) {
        this.id = name;
        this.name = name;
        this.avatar = avatar;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

}
