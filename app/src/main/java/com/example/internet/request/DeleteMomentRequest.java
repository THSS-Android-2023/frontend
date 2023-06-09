package com.example.internet.request;

import com.example.internet.util.Global;

import okhttp3.Callback;

public class DeleteMomentRequest extends BaseRequest{
    private String momentId;

    String url = Global.API_URL + "/moment/del_moment/";

    public DeleteMomentRequest(Callback callback, int momentId, String jwt){
        super();
        addParam("moment_id", momentId+"");
        post(url, callback, jwt);
    }

}
