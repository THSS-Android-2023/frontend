package com.example.internet.request;

import com.example.internet.util.Global;

import okhttp3.Callback;

public class GetNewMomentRequest extends BaseRequest{

    String saveUrl = Global.API_URL + "/moment/get_new_moment/0/";

    public GetNewMomentRequest(Callback saveCallback, String jwt){
        try{
//            get(saveUrl, saveCallback, jwt);
            post(saveUrl, saveCallback, jwt);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
