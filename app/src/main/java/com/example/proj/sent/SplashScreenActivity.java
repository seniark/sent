package com.example.proj.sent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    protected void signInClicked(View v)
    {
        Intent i = new Intent();
        i.setClass(SplashScreenActivity.this, LoginActivity.class);
        startActivity(i);
    }



}
