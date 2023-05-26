package com.example.internet.request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Callback;
import okhttp3.RequestBody;

public class BaseRequest {
    private HashMap<String, String> param = new HashMap<>();

    public void BaseRequest(){
        clearParam();
    }
    public void addParam(String k, String v){
        if (k.length() != 0)
            param.put(k, v);
    }
    public void clearParam(){
        param.clear();
    }

    public void get(String requestUrl, Callback callback){
        get(requestUrl, callback, "");
    }
    public void get(String requestUrl, Callback callback, String jwt){
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(requestUrl)).newBuilder();
        for (HashMap.Entry < String, String > entry: param.entrySet()){
            urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
        }
        Request.Builder builder = new Request.Builder().url(urlBuilder.build()).get();


        if (jwt != null && !jwt.isEmpty()) {
            builder.header("Authorization", jwt);
        }

        client.newCall(builder.build()).enqueue(callback);
    }

    public void postMedia(String requestUrl, File imageFile, Callback callback){
        postMedia(requestUrl, imageFile, callback, "");
    }
    public void postMedia(String requestUrl, File imageFile, Callback callback, String jwt) {
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        OkHttpClient client = new OkHttpClient();

        // 添加图片文件到请求体
        RequestBody imageBody = RequestBody.create(imageFile ,MediaType.parse("image/jpeg"));
        requestBodyBuilder.addFormDataPart("file", imageFile.getName(), imageBody);

        // 添加其他参数到请求体
        for (HashMap.Entry<String, String> entry : param.entrySet()) {
            requestBodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        Request.Builder requestBuilder = new Request.Builder()
                .url(requestUrl)
                .post(requestBodyBuilder.build());

        if (jwt != null && !jwt.isEmpty()) {
            requestBuilder.header("Authorization", jwt);
        }

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(callback);
    }

    public void postMedia(String requestUrl, File[] imageFiles, Callback callback, String jwt) {
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        OkHttpClient client = new OkHttpClient();

        for (File imageFile : imageFiles) {
            // 添加图片文件到请求体
            RequestBody imageBody = RequestBody.create(imageFile, MediaType.parse("image/jpeg"));
            requestBodyBuilder.addFormDataPart("file[]", imageFile.getName(), imageBody);
        }

        // 添加其他参数到请求体
        for (HashMap.Entry<String, String> entry : param.entrySet()) {
            requestBodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        Request.Builder requestBuilder = new Request.Builder()
                .url(requestUrl)
                .post(requestBodyBuilder.build());

        if (jwt != null && !jwt.isEmpty()) {
            requestBuilder.header("Authorization", jwt);
        }

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(callback);
    }

    public void post(String requestUrl, Callback callback){
        post(requestUrl, callback, "");
    }
    public void post(String requestUrl, Callback callback, String jwt) {
        OkHttpClient client = new OkHttpClient();
        JSONObject jsonParams = new JSONObject();
        for (HashMap.Entry<String, String> entry : param.entrySet()) {
            try {
                jsonParams.put(entry.getKey(), entry.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // Create request body with JSON data
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, jsonParams.toString());
        // Build request
        Request.Builder requestBuilder = new Request.Builder()
                .url(requestUrl)
                .post(requestBody);

        if (jwt != null && !jwt.isEmpty()) {
            requestBuilder.header("Authorization", jwt);
        }

        Request request = requestBuilder.build();
        // Send request
        client.newCall(request).enqueue(callback);
    }
}
