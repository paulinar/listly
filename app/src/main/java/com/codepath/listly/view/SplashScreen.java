package com.codepath.listly.view;

/**
 * Created by paulina.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.codepath.listly.R;

public class SplashScreen extends Activity {

    // splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // this method will be executed once the timer is over
                // start the app main activity
                Intent i = new Intent(SplashScreen.this, AllTasksActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}