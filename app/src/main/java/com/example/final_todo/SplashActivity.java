package com.example.final_todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
                String login = sharedPreferences.getString("loginToken", null);
                Intent intent;
                if (Objects.isNull(login)) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("activity", "todoList");
                    startActivity(intent);
                } else {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        }, 3000);
    }
}