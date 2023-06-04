package com.example.internet.model;

import com.stfalcon.chatkit.commons.models.IDialog;

import java.util.ArrayList;

/*
 * Created by troy379 on 04.04.17.
 */
public class DialogModel implements IDialog<MessageModel> {

    private String id;
    private String dialogPhoto;
    private String dialogName;
    private String lastMessage;

    private ArrayList<ChatUserModel> users;

    private int unreadCount;

    public DialogModel(String name, String photo, String lastMessage, int unreadCount) {
        this.id = name;
        this.dialogName = name;
        this.users = new ArrayList<>();
        users.add(new ChatUserModel( name, null, false));

        this.dialogPhoto = photo;
        this.lastMessage = lastMessage;
        this.unreadCount = unreadCount;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDialogPhoto() {
        return dialogPhoto;
    }

    @Override
    public String getDialogName() {
        return dialogName;
    }

    @Override
    public ArrayList<ChatUserModel> getUsers() {
        return users;
    }

    @Override
    public MessageModel getLastMessage() {
        return new MessageModel("0", users.get(0), this.lastMessage, null);
    }

    @Override
    public void setLastMessage(MessageModel lastMessage) {
//        this.lastMessage = lastMessage;
        this.lastMessage = lastMessage.getText();
    }

    @Override
    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}
