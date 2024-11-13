package com.example.mi_app_habitos;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_splash_screen);

        Thread thread = new Thread() {
            @Override
            public void run(){
                try {
                    SystemClock.sleep(3000);
                    startActivity(new Intent(SplashScreen.this , login.class));
                    finish();
                }catch (Exception e){

                }
            }
        }; thread.start();
    }

}