package com.example.internet.request;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.internet.util.ErrorDialog;
import com.example.internet.util.Global;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UploadAvatarRequest extends BaseRequest{


    String uploadAvatarUrl = Global.API_URL + "/account/change_avatar/";

    public UploadAvatarRequest(String jwt, File imageFile, Callback callback){
        super();
        postMedia(uploadAvatarUrl, imageFile, callback, jwt);
    }
}
