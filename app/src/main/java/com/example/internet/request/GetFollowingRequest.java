package com.example.internet.request;

import com.example.internet.util.Global;

import okhttp3.Callback;

public class GetFollowingRequest extends BaseRequest{
    String url = Global.API_URL + "/account/get_following/";

    public GetFollowingRequest(Callback callback, String jwt) {
        super();
        get(url, callback, jwt);
    }
}
