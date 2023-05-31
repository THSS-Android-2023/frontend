package com.example.internet.request;

import com.example.internet.util.Global;

import okhttp3.Callback;

public class GetInfoRequest extends BaseRequest{


    String saveUrl = Global.API_URL + "/account/get_info/";


    public GetInfoRequest(Callback saveCallback, String jwt){
        super();
        try{
            get(saveUrl, saveCallback, jwt);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public GetInfoRequest(Callback saveCallback, String username, String jwt){
        super();
        try{
            saveUrl += username + "/";
            get(saveUrl, saveCallback, jwt);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
