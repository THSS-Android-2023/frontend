package com.example.internet.request;

import com.example.internet.util.Global;

import okhttp3.Callback;

public class GetInfoRequest extends BaseRequest{


    String saveUrl = Global.API_URL + "/account/get_info/";


    public GetInfoRequest(Callback saveCallback, String jwt){
        super();
        try{
            BaseRequest saveRequest = new BaseRequest();
            saveRequest.get(saveUrl, saveCallback, jwt);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
