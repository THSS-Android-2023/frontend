package com.example.internet.request;

import com.example.internet.util.Global;

import okhttp3.Callback;

public class UnblacklistRequest extends BaseRequest{
    String url = Global.API_URL +  "/account/unblack_user/";

    public UnblacklistRequest(Callback callback, String username, String jwt) {
        super();
        addParam("target_username", username);
        post(url, callback, jwt);
    }
}
