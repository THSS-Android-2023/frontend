package com.example.internet.request;

import com.example.internet.util.Global;

import okhttp3.Callback;

public class UnlikeMomentRequest extends BaseRequest{
    public int momentId;
    public int userId;

    String url = Global.API_URL +  "/moment/unlike_moment/";

    public UnlikeMomentRequest(Callback callback, int momentId, String jwt) {
        super();
        addParam("moment_id", ""+momentId);
        post(url, callback, jwt);
    }
}
