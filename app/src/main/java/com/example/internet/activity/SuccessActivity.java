package com.example.internet.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.internet.R;

public class SuccessActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        Bundle extras = getIntent().getExtras();
        String username = "";
        if (extras != null) {
            username = extras.getString("username");
        }
        TextView textView = findViewById(R.id.text);
        textView.setText(username + " successfully login!!");
    }

}
