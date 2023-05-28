package com.example.internet.request;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.internet.activity.MainActivity;
import com.example.internet.util.ErrorDialog;
import com.example.internet.util.Global;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginRequest extends BaseRequest{

    String loginUrl = Global.API_URL + "/account/";
    AppCompatActivity ctx;
    String usr, pwd, jwt;



    public LoginRequest(String usr, String pwd, Callback callback){
        super();
        this.ctx = (AppCompatActivity) ctx;
        this.usr = usr;
        this.pwd = pwd;
        try{
            BaseRequest loginRequest = new BaseRequest();
            loginRequest.addParam("username", usr);
            loginRequest.addParam("password", pwd);
            loginRequest.post(loginUrl, callback);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
