package com.example.internet.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.internet.request.GetSystemNoticeRequest;
import com.example.internet.util.Global;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NotificationService extends Service {

    private static final long POLL_INTERVAL = Global.POLLING_INTERVAL * 1000; // 10秒钟的轮询间隔
    private Handler handler;
    private Runnable runnable;

    private String jwt = "";
    private String username = "";


    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // 在这里执行轮询任务，包括调用后端的get_notification接口
                // 处理返回的通知数据并创建显示通知
                // ...
                // 设置下一次轮询任务

//                NotificationUtil.notify(this, this);

                new GetSystemNoticeRequest(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.code() != 200) {
                            return;
                        }
                        String resStr = response.body().string();
                        try {
                            Intent intent = new Intent("com.example.ACTION_NOTIFICATION");
                            intent.putExtra("notifications", resStr);
                            intent.putExtra("jwt", jwt);
                            intent.putExtra("username", username);

                            sendBroadcast(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, jwt);
                handler.postDelayed(this, POLL_INTERVAL);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 在这里执行服务的启动逻辑
        startPolling(); // 启动轮询任务
        jwt = intent.getStringExtra("jwt");
        Log.d("GETJWT", jwt);

        username = intent.getStringExtra("username");
        Log.d("GETJWT", username);
        return START_STICKY; // 指定服务在被杀死后的重启策略
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPolling(); // 停止轮询任务
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startPolling() {
        handler.postDelayed(runnable, 0); // 立即执行第一次轮询任务
    }

    private void stopPolling() {
        handler.removeCallbacks(runnable);
    }
}