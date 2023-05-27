package com.example.internet.request;

import android.util.Log;

import com.example.internet.util.Global;

import okhttp3.Callback;

public class GetCommentRequest extends BaseRequest{
    String getCommentUrl = Global.API_URL + "/comment/get_comment/";

    public GetCommentRequest(Callback callback, int momentId, String jwt){
        super();
        try{
            Log.d("PostCommentRequest", "jwt: " + jwt);
            Log.d("PostCommentRequest", "momentId: " + momentId);
            getCommentUrl += momentId + "/";
            BaseRequest getRequest = new BaseRequest();
            getRequest.get(getCommentUrl, callback, jwt);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
