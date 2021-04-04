package com.leetsoft.weremobile;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class SplashActivity extends Activity {
    ImageView logoLeft;
    ImageView logoRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);



        Thread timerThread = new Thread() {
            public void run() {
                try {
                    logoLeft = findViewById(R.id.logoLeft);
                    logoRight = findViewById(R.id.logoRight);

                    logoLeft.startAnimation(AnimationUtils.loadAnimation(
                            getApplicationContext(),
                            R.anim.slide_right
                    ));

                    logoRight.startAnimation(AnimationUtils.loadAnimation(
                            getApplicationContext(),
                            R.anim.slide_left
                    ));

                    sleep(2700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();




    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
