package com.example.internet.request;

import android.content.Context;
import android.util.Log;

import com.example.internet.util.Global;

import java.io.File;

import okhttp3.Callback;

public class PublishMomentRequest extends BaseRequest{

    String publishUrl = Global.API_URL + "/moment/publish_moment/";
    Context ctx;

    public PublishMomentRequest(String title, String content, String tag, String location,
                                int numImages, File[] imageFiles, Callback callback, String jwt){
        super();
        this.ctx = ctx;
        try{
            addParam("title", title);
            addParam("content", content);
            addParam("tag", tag);
            addParam("location", location);
            addParam("img_num", numImages + "");
            postMedia(publishUrl, imageFiles, callback, jwt);
            Log.d("publish", publishUrl);
            Log.d("title", title);
            Log.d("content", content);
            Log.d("location", location);
            Log.d("img_num", numImages + "");
            Log.d("img", imageFiles[0].toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
