package com.example.internet.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.internet.R;
import com.example.internet.request.RegisterRequest;
import com.example.internet.util.ErrorDialog;
import com.example.internet.request.BaseRequest;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterActivity extends BaseActivity {

    private EditText username, password, password2;

    Callback registerCallback = new Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.d("Error", e.toString());}
        @Override
        public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
            String resStr = Objects.requireNonNull(response.body()).string();
            int code = response.code();
            Log.d("code", String.valueOf(code));
            try {
                JSONObject jsonObject = new JSONObject(resStr);
                Boolean status = jsonObject.getBoolean("status");
            }
            catch (JSONException e){
                Log.d("Error", e.toString());
            }
            SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("login", true);
            editor.putString("username", usr);
            editor.putString("password", pwd);
            editor.apply();

            Intent intent = new Intent();
            intent.putExtra("username", usr);
            intent.putExtra("password", pwd);
            setResult(RESULT_OK, intent);

            finish();

            finish();
            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.login_account);
        password = findViewById(R.id.login_password);
        password2 = findViewById(R.id.login_repeat_password);
    }
    String usr;
    String pwd;
    public void onRegisterClick(View v){
        usr = username.getText().toString();
        pwd = password.getText().toString();
        String pwd2 = password2.getText().toString();
        if (!pwd.equals(pwd2)){
            ErrorDialog error = new ErrorDialog(this, "两次密码不一致");
            return;
        }
        if(usr.isEmpty()||pwd.isEmpty()||pwd2.isEmpty()||!pwd.equals(pwd2)){
            return;
        }
        new RegisterRequest(usr, pwd, registerCallback);
    }

}
