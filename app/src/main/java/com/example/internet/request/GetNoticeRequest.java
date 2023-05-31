package com.example.internet.request;

import com.example.internet.util.Global;

import okhttp3.Callback;

public class GetNoticeRequest extends BaseRequest{
    String url = Global.API_URL + "/notice/get_notice/";
    public GetNoticeRequest(Callback saveCallback,  String jwt){
        super();
        try{
            get(url, saveCallback, jwt);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
