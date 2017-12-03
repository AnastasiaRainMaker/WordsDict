package com.dmitry.wordsdict.main.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.dmitry.wordsdict.R;


public class SplashActivity extends Activity implements Runnable {

    Thread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);
        mThread = new Thread(this);

        mThread.start();
    }

    @Override
    public void run(){
        try {
            Thread.sleep(2000);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));

            finish();
        }
    }
}