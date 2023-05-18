package com.example.internet.request;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.example.internet.util.Global;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class getInfoRequest extends BaseRequest{


    String saveUrl = Global.API_URL + "/account/get_info/";


    public getInfoRequest(Callback saveCallback, String jwt){
        try{
            BaseRequest saveRequest = new BaseRequest();
            saveRequest.get(saveUrl, saveCallback, jwt);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
