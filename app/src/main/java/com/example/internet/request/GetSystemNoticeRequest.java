package com.example.internet.request;

import com.example.internet.util.Global;

import okhttp3.Callback;

public class GetSystemNoticeRequest extends BaseRequest{
    String url = Global.API_URL + "/notice/get_notice_system/";
    public GetSystemNoticeRequest(Callback saveCallback, String jwt){
        super();
        try{
            get(url, saveCallback, jwt);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
