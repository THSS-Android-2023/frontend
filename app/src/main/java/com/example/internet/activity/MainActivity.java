package com.example.internet.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.internet.R;
import com.example.internet.adapter.BottomAdapter;
import com.example.internet.util.ErrorDialog;
import com.example.internet.util.HTTPRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);

        BottomAdapter bottomAdapter = new BottomAdapter(this);
        ViewPager2 viewPager = findViewById(R.id.bottom_pager);
        viewPager.setUserInputEnabled(false);
        viewPager.setAdapter(bottomAdapter);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.bottom_home:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.bottom_topic:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.bottom_guide:
                        viewPager.setCurrentItem(2);
                        return true;
                    case R.id.bottom_mine:
                        viewPager.setCurrentItem(3);
                        return true;
                }
                return false;
            }
        });
    }


}
