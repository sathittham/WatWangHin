package com.sathittham.watwanghin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

/**
 * Created by scuta_000 on 6/8/2557.
 */
public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash_screen);

        Handler myHandler = new Handler();
        myHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();
                Intent goMain = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(goMain);

            }
        }, 3000);
    }
}
