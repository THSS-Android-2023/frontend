package com.example.internet.request;

import com.example.internet.util.Global;

import okhttp3.Callback;

public class GetFollowingRequest extends BaseRequest{
    String url = Global.API_URL + "/account/get_following/";
    String url2 = Global.API_URL + "/account/get_follower/";

    public GetFollowingRequest(Callback callback, String jwt) {
        super();
        get(url, callback, jwt);
    }

    public GetFollowingRequest(Callback callback, String jwt, int x) {
        super();
        get(url2, callback, jwt);
    }
}
