package com.example.internet.model;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.util.Date;

/*
 * Created by troy379 on 04.04.17.
 */
public class MessageModel implements IMessage,
        MessageContentType.Image, /*this is for default image messages implementation*/
        MessageContentType /*and this one is for custom content type (in this case - voice message)*/
{

    private String id;
    private String text;
    private Date createdAt;
    private ChatUserModel user;

    public MessageModel(String id, ChatUserModel user, String text) {
        this(id, user, text, new Date());
    }

    public MessageModel(String id, ChatUserModel user, String text, Date createdAt) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.createdAt = createdAt;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public ChatUserModel getUser() {
        return this.user;
    }


    public void setText(String text) {
        this.text = text;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getImageUrl() {
        return null;
    }

}
