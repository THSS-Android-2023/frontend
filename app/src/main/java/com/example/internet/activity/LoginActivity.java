package com.example.internet.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.internet.R;
import com.example.internet.request.LoginRequest;
import com.example.internet.service.NotificationService;
import com.example.internet.util.ErrorDialog;
import com.example.internet.request.BaseRequest;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_account)
    EditText username_edit;

    @BindView(R.id.login_password)
    EditText password_edit;

    final Context context = this;
    String usr = "";
    String pwd = "";
    String jwt = "";

    String nickname = "";

    Boolean withoutAnim = false;

    Callback loginCallback = new Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.d("Error", e.toString());}
        @Override
        public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
            int statusCode = response.code();
            if (statusCode == 401){
                ErrorDialog error = new ErrorDialog(context, "账号/密码不正确");
            } else if (statusCode == 200) {
                String resStr = Objects.requireNonNull(response.body()).string();
                try {
                    JSONObject jsonObject = new JSONObject(resStr);
                    jwt = jsonObject.getString("jwt");
                    nickname = jsonObject.getString("nickname");
                } catch(Exception e){
                    e.printStackTrace();
                }
                SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("login", true);
                editor.putString("username", usr);
                editor.putString("password", pwd);
                editor.putString("nickname", nickname);
                editor.putString("jwt", jwt);
                editor.apply();
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("username", usr);
                intent.putExtra("jwt", jwt);
                intent.putExtra("nickname", nickname);

                Intent serviceIntent = new Intent(context, NotificationService.class);
                serviceIntent.putExtra("jwt", jwt);
                serviceIntent.putExtra("username", usr);
                startService(serviceIntent);

                startActivity(intent);
                if (!withoutAnim)
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        boolean hasLogin = sharedPreferences.getBoolean("login", false);
        if (hasLogin){
            withoutAnim = true;
            usr = sharedPreferences.getString("username", "");
            pwd = sharedPreferences.getString("password", "");
            new LoginRequest(usr, pwd, loginCallback);
        }
    }

    public void onRegisterClick(View v){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    public void onLoginClick(View v){
        usr = username_edit.getText().toString();
        pwd = password_edit.getText().toString();
        if(usr.isEmpty()||pwd.isEmpty()){
            return;
        }
        LoginRequest request = new LoginRequest(usr, pwd, loginCallback);
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
