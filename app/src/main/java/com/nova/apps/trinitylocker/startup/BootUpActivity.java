package com.nova.apps.trinitylocker.startup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nova.apps.trinitylocker.R;
import com.nova.apps.trinitylocker.core.MainSettingsActivity;
import com.nova.apps.trinitylocker.util.Constants;

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
                    Boolean isFirstRun = getSharedPreferences(Constants.preferenceKey, MODE_PRIVATE).getBoolean(Constants.preferenceFirstRun, true);
                    if(isFirstRun) {
                        Intent i = new Intent(getApplicationContext(), ChooseSignInActivity.class);
                        startActivity(i);
                        finish();
                    }else {
                        Intent i = new Intent(getApplicationContext(), MainSettingsActivity.class);
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