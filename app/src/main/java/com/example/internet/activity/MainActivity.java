package com.example.internet.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.internet.R;
import com.example.internet.adapter.pager.BottomAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private String username;
    public String jwt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        username = getIntent().getStringExtra("username");
        jwt = getIntent().getStringExtra("jwt");

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
    public void editMoment(View v){

        Intent intent = new Intent(this, EditMomentActivity.class);
        intent.putExtra("jwt", jwt);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getUsername() {
        return username;
    }

    public String getJwt() {
        return jwt;
    }

}
