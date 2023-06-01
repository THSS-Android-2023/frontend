package com.example.internet.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.internet.model.NotificationModel;
import com.example.internet.util.NotificationUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 在这里处理接收到的广播消息，包括创建系统通知
        // ...
        try {
            AppCompatActivity activity = (AppCompatActivity) context;
            String jsonStr = intent.getStringExtra("notifications");
            String jwt = intent.getStringExtra("jwt");
            String username = intent.getStringExtra("username");

            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                NotificationModel notificationModel = new NotificationModel(jsonObject);
                NotificationUtil.notify(activity, activity, 0, notificationModel.id, jwt, username, "CampusXpress", notificationModel.content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}