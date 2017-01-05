package com.nova.apps.trinitylocker.startup.setup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.nova.apps.trinitylocker.R;
import com.nova.apps.trinitylocker.core.MainSettingsActivity;
import com.nova.apps.trinitylocker.util.Constants;

public class FirstSetupActivity extends AppIntro {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setWizardMode(true);

		addSlide(new DisableSystemLockFragment());
		addSlide(new EnableNotificationsFragment());


		addSlide(AppIntroFragment.newInstance(getString(R.string.setupDone), getString(R.string.setupFinished), R.drawable.ic_done_white, ResourcesCompat.getColor(getResources(), R.color.colorAccent, getTheme())));

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