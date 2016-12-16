package com.nova.apps.trinitylocker.util;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class GoogleSignInSingleton {
    private static final String TAG = GoogleSignInSingleton.class.getSimpleName();
    private static GoogleSignInSingleton instance = null;
    private static GoogleSignInAccount mGoogleSignIn = null;

    protected GoogleSignInSingleton(){}

    public static GoogleSignInSingleton getInstance(GoogleSignInAccount aGoogleSignIn){
        if(instance == null){
            instance = new GoogleSignInSingleton();
            if(mGoogleSignIn == null){
                mGoogleSignIn = aGoogleSignIn;
            }
        }
        return instance;
    }

    public GoogleSignInAccount getGoogleSignIn(){
        return mGoogleSignIn;
    }
}