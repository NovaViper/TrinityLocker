package com.nova.apps.trinitylocker.startup.setup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.nova.apps.trinitylocker.R;
import com.nova.apps.trinitylocker.core.MainSettingsActivity;
import com.nova.apps.trinitylocker.util.Constants;

import java.util.ArrayList;

public class FirstSetupActivity extends AppIntro {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));

		setWizardMode(true);
		//TODO Finish up Intro
		addSlide(new DisableSystemLockFragment());
		addSlide(new EnableNotificationsFragment());
		//addSlide(new LockProfilePickerFragment());
		//addSlide(new UnlockStylePickerFragment());
		addSlide(new SetUpDoneFragment());

		setColorTransitionsEnabled(true);

		showStatusBar(false);
		showSkipButton(false);
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
	}

	public void disableLock(View v) {
		startActivityForResult(new Intent(Settings.ACTION_SECURITY_SETTINGS), 0);
	}

	public void enableAccess(View v) {
		startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS), 0);
	}

	@Override
	public void onSkipPressed(Fragment currentFragment) {
		super.onSkipPressed(currentFragment);
	}

	@Override
	public void onDonePressed(Fragment currentFragment) {
		super.onDonePressed(currentFragment);
		//Set 'isfirstrun' to false so app does not go back into the sign in screen again
		//getSharedPreferences(Constants.preferenceKey, MODE_PRIVATE).edit().putBoolean(Constants.preferenceFirstRun, false).commit();
		Intent i = new Intent(getApplicationContext(), MainSettingsActivity.class);
		startActivity(i);
	}

	@Override
	public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
		super.onSlideChanged(oldFragment, newFragment);
		// Do something when the slide changes
	}
}