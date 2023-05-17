package com.example.internet.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.internet.R;
import com.example.internet.util.ErrorDialog;
import com.example.internet.util.HTTPRequest;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    private String username;
    private EditText old_password, new_password, repeat_password;

    final Context context = this;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        username = getIntent().getStringExtra("username");
        old_password = findViewById(R.id.old_password);
        new_password = findViewById(R.id.new_password);
        repeat_password = findViewById(R.id.new_password_repeat);

    }

    public void onResetPasswordClick(View v){
        String usr = username;
        String pwd = old_password.getText().toString();
        String pwd1 = new_password.getText().toString();
        String pwd2 = repeat_password.getText().toString();
        if (!pwd1.equals(pwd2)){
            ErrorDialog error = new ErrorDialog(this, "两次密码不一致");
            return;
        }
        if(usr.isEmpty()||pwd.isEmpty()||pwd1.isEmpty()||pwd2.isEmpty()){
            return;
        }
        try{
            String resetPasswordUrl = "http://129.211.216.10:5000/login/change_password/";
            Callback resetPasswordCallback = new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("Error", e.toString());}
                @Override
                public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                    String resStr = Objects.requireNonNull(response.body()).string();
                    int statusCode = response.code();
                    if (statusCode == 401){
                        ErrorDialog error = new ErrorDialog(context, "旧密码不正确");
                    } else if (statusCode == 200) {
                        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", usr);
                        editor.putString("password", pwd1);
                        editor.apply();
                    }
                    finish();
                }
            };
            HTTPRequest loginRequest = new HTTPRequest();
            loginRequest.addParam("username", username);
            loginRequest.addParam("old_password", pwd);
            loginRequest.addParam("new_password", pwd1);
            loginRequest.post(resetPasswordUrl, resetPasswordCallback);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}