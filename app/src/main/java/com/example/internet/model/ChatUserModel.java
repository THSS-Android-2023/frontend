package com.example.internet.model;

import com.stfalcon.chatkit.commons.models.IUser;

/*
 * Created by troy379 on 04.04.17.
 */
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
