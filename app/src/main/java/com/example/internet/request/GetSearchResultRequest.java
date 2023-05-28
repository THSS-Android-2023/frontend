package com.example.internet.request;

import com.example.internet.util.Global;

import okhttp3.Callback;

public class GetSearchResultRequest extends BaseRequest{


    String searchUrl = Global.API_URL + "/moment/search_moment/";


    public GetSearchResultRequest(Callback saveCallback, String keyWords, String jwt){
        super();
        try{
            searchUrl += keyWords + "/0" + "/";
            BaseRequest saveRequest = new BaseRequest();
            saveRequest.get(searchUrl, saveCallback, jwt);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}