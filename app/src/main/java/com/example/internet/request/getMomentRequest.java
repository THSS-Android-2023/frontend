package com.example.internet.request;

import com.example.internet.util.Global;

import okhttp3.Callback;

public class getMomentRequest extends BaseRequest{

    String saveUrl;

    public getMomentRequest(String username, Callback saveCallback, String jwt){
        saveUrl = Global.API_URL + "/moment/" + username + "/0/";
        try{
            get(saveUrl, saveCallback, jwt);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
