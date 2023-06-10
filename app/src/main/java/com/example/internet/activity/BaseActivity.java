package com.example.internet.activity;

import android.content.Intent;
import android.content.IntentFilter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.internet.R;
import com.example.internet.service.NotificationReceiver;

public abstract class BaseActivity extends AppCompatActivity {
    private NotificationReceiver receiver;

    @Override
    protected void onResume() {
        super.onResume();
        receiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter("com.example.ACTION_NOTIFICATION");
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

}
