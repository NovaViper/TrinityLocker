package com.nova.apps.trinitylocker.util;

import com.google.android.gms.common.api.GoogleApiClient;

public class GoogleAPIClientSingleton {
    private static final String TAG = GoogleAPIClientSingleton.class.getSimpleName();
    private static GoogleAPIClientSingleton instance = null;
    private static GoogleApiClient mGoogleApiClient = null;

    protected GoogleAPIClientSingleton(){}

    public static GoogleAPIClientSingleton getInstance(GoogleApiClient aGoogleApiClient){
        if(instance == null){
            instance = new GoogleAPIClientSingleton();
            if(mGoogleApiClient == null){
                mGoogleApiClient = aGoogleApiClient;
            }
        }
        return instance;
    }

    public GoogleApiClient getGoogleApiClient(){
        return mGoogleApiClient;
    }
}