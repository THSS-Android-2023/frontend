package com.example.internet.request;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.internet.util.ErrorDialog;
import com.example.internet.util.Global;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChangeInfoRequest extends BaseRequest{

    String saveUrl = Global.API_URL + "/account/change_info/";
    Context ctx;

    public ChangeInfoRequest(String intro, String nickname, Callback callback, String jwt){
        super();
        this.ctx = ctx;
        try{
            addParam("intro", intro);
            addParam("nickname", nickname);
            post(saveUrl, callback, jwt);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
