package com.example.internet.request;

import android.util.Log;

import com.example.internet.util.Global;

import org.json.JSONObject;

import okhttp3.Callback;

public class PostCommentRequest extends BaseRequest{
    String postCommentUrl = Global.API_URL + "/comment/publish_comment/";

    public PostCommentRequest(Callback callback, int momentId, String content, String jwt){
        super();
        try{
            addParam("content", content);
            addParam("moment_id", momentId + "");
            post(postCommentUrl, callback, jwt);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
