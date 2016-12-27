package com.nova.apps.trinitylocker.core;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nova.apps.trinitylocker.BuildConfig;
import com.nova.apps.trinitylocker.R;

public class AboutAppActivity extends AppCompatActivity {

	int versionCode = BuildConfig.VERSION_CODE;
	String versionName = BuildConfig.VERSION_NAME;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_app);

	}
}