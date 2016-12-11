package com.nova.apps.trinitylocker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BootUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot_up);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isfirstfrun", true);
                    if(isFirstRun) {
                        //getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isfirstfrun", false).commit();
                        Intent i = new Intent(getApplicationContext(), ChooseSignInActivity.class);
                        startActivity(i);
                        finish();
                    }else {
                        Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*@Override
    public void onSaveInstanceState(Bundle outState) {
    //Save your data to be restored here
        outState.putBoolean("setupMode" , setupMode);
        super.onSaveInstanceState(outState);
    }*/
}