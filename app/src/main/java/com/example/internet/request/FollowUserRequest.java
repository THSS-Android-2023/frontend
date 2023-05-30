package com.example.internet.request;

import com.example.internet.util.Global;

import okhttp3.Callback;

public class FollowUserRequest extends BaseRequest{
    String url = Global.API_URL +  "/account/follow_user/";

    public FollowUserRequest(Callback callback, String username, String jwt) {
        super();
        addParam("target_username", username);
        post(url, callback, jwt);
    }
}
