package com.example.internet.request;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.internet.util.Global;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterRequest extends BaseRequest{
    public RegisterRequest(String usr, String pwd, Callback callback){
        String loginUrl = Global.API_URL + "/account/register/";

        addParam("username", usr);
        addParam("password", pwd);
        post(loginUrl, callback);
    }
}
