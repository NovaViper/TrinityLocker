package com.nova.apps.trinitylocker.startup.setup;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.LayoutInflaterCompat;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.nova.apps.trinitylocker.R;
import com.nova.apps.trinitylocker.core.MainSettingsActivity;
import com.nova.apps.trinitylocker.util.Constants;

public class FirstSetupActivity extends AppIntro {

	IconicsDrawable doneButton = new IconicsDrawable(this)
			.icon(GoogleMaterial.Icon.gmd_done)
			.color(Color.RED)
			.sizeDp(200);
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));

		super.onCreate(savedInstanceState);
		setWizardMode(true);
		//TODO Finish up Intro
		addSlide(new DisableSystemLockFragment());
		addSlide(new EnableNotificationsFragment());


		addSlide(new SetUpDoneFragment());

		showStatusBar(false);
		showSkipButton(false);

		setDepthAnimation();
	}

	@Override
	public void onSkipPressed(Fragment currentFragment) {
		super.onSkipPressed(currentFragment);
		// Do something when users tap on Skip button
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