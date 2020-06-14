package com.example.morgan.surf_spot_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.morgan.surf_spot_app.R;

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /* Apply fade animation to splash screen image */
        iv = findViewById(R.id.splash_screen_image);
        Animation fade = AnimationUtils.loadAnimation(this, R.anim.splash_transition);
        iv.startAnimation(fade);


        new Timer().schedule(new TimerTask(){
            @Override
            public void run(){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);


    }

}
