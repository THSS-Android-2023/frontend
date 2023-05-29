package com.example.internet.request;

import com.example.internet.util.Global;

import okhttp3.Callback;

public class CheckFollowshipRequest extends BaseRequest{
    String url = Global.API_URL + "/account/check_followship/";

    public CheckFollowshipRequest(Callback callback, String username, String jwt) {
        super();
        addParam("target_username", username);
        get(url, callback, jwt);
    }
}
