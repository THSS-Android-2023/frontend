package com.example.internet.request;

import com.example.internet.util.Global;

import okhttp3.Callback;

public class ChangePasswordRequest extends BaseRequest{
    String url = Global.API_URL + "/account/change_password/";

    public ChangePasswordRequest(Callback callback, String username, String oldPassword, String newPassword) {
        super();
        addParam("username", username);
        addParam("old_password", oldPassword);
        addParam("new_password", newPassword);
        post(url, callback);
    }
}
