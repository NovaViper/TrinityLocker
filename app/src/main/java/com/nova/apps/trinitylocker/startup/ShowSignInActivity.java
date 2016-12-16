package com.nova.apps.trinitylocker.startup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.nova.apps.trinitylocker.R;
import com.nova.apps.trinitylocker.core.MainScreenActivity;
import com.nova.apps.trinitylocker.util.GoogleSignInSingleton;

public class ShowSignInActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView profilePic;
    private TextView startApp;
    private TextView accountName;
    private TextView accountEmail;
    private Button getStarted;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sign_in);

        String s = getIntent().getExtras().getString("SignInType");
        checkAccountSignIn(s);
    }

    private void checkAccountSignIn(String signInType){
        if(String.valueOf(signInType).matches("google")){
            GoogleSignInSingleton signInSingleton = GoogleSignInSingleton.getInstance(null);
            GoogleSignInAccount acct = signInSingleton.getGoogleSignIn();
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            Uri personPhoto = acct.getPhotoUrl();

            profilePic = (ImageView)findViewById(R.id.profileImage);
            startApp = (TextView)findViewById(R.id.startAppLabel);
            accountName = (TextView)findViewById(R.id.profileName);
            accountEmail = (TextView)findViewById(R.id.profileEmail);
            getStarted = (Button)findViewById(R.id.getStartedButton);

            //Set Stuff for Google Sign In
            Glide.with(getApplicationContext()).load(personPhoto)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profilePic);
            startApp.setText(R.string.startAppWithGoogle);
            accountName.setText(personName);
            accountEmail.setText(personEmail);

        }else if(String.valueOf(signInType).matches("facebook")){

        }else{

        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    //getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isfirstfrun", false).commit();
                    Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
                    startActivity(i);
                    finish();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getStartedButton:

                break;
        }
    }

    public void createButton(int id){
        Button button = (Button)findViewById(id);
        button.setOnClickListener(this);
    }
}