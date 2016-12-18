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

        String signIn = getIntent().getExtras().getString("SignInType");
        checkAccountSignIn(signIn);
    }

    private void checkAccountSignIn(String signInType){
        if(String.valueOf(signInType).matches("google")){
            //GoogleSignInSingleton signInSingleton = GoogleSignInSingleton.getInstance(null);
            //GoogleSignInAccount acct = signInSingleton.getGoogleSignIn();
	        GoogleSignInAccount googleAccount = getIntent().getExtras().getParcelable("Account");

            String personName = googleAccount.getDisplayName();
            String personEmail = googleAccount.getEmail();
            Uri personPhoto = googleAccount.getPhotoUrl();

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
            getStarted.setEnabled(false);

	        //getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isfirstfrun", false).commit();
	        Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
	        i.putExtra("SignInType", signInType);
	        i.putExtra("Account", googleAccount);
	        startActivity(i);
	        finish();

        }else if(String.valueOf(signInType).matches("facebook")){

        }else{

        }
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