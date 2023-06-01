package com.example.internet.util;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.internet.R;
import com.example.internet.activity.DetailsActivity;
import com.example.internet.model.TimelineModel;
import com.example.internet.request.GetMomentRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.sql.Time;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NotificationUtil {
    static String createNotificationChannel(AppCompatActivity ctx, String channelID, String channelNAME, int level) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(channelID, channelNAME, level);
            manager.createNotificationChannel(channel);
            return channelID;
        } else {
            return null;
        }
    }

    // type = 0 for details page, type = 1 for chatting page
    public static void notify(AppCompatActivity fromActivity, AppCompatActivity toActivity, int type, int id, String jwt, String username, String title, String content){
        if (type == 0) {
            new GetMomentRequest(new Callback() {
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
                        JSONObject resJson = new JSONObject(resStr);
                        TimelineModel timelineModel = new TimelineModel(resJson);

                        Intent intent = new Intent(fromActivity, DetailsActivity.class);
                        intent.putExtra("jwt", jwt);
                        intent.putExtra("username", username);
                        intent.putExtra("timelineModelJson", new Gson().toJson(timelineModel));
                        PendingIntent pendingIntent = PendingIntent.getActivity(fromActivity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        String channelId = createNotificationChannel(fromActivity, "my_channel_ID", "my_channel_NAME", NotificationManager.IMPORTANCE_HIGH);
                        NotificationCompat.Builder notification = new NotificationCompat.Builder(fromActivity, channelId)
                                .setContentTitle(title)
                                .setContentText(content)
                                .setContentIntent(pendingIntent)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setAutoCancel(true);
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(fromActivity);
                        notificationManager.notify(100, notification.build());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, jwt, id);

        }
    }

}
