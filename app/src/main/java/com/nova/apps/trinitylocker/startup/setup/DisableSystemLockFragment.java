package com.nova.apps.trinitylocker.startup.setup;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;
import com.github.paolorotolo.appintro.ISlidePolicy;
import com.nova.apps.trinitylocker.R;
import com.nova.apps.trinitylocker.util.AlertDialogUtil;
import com.nova.apps.trinitylocker.util.AppLogger;

import static android.content.Context.KEYGUARD_SERVICE;

public class DisableSystemLockFragment extends Fragment implements ISlideBackgroundColorHolder, ISlidePolicy {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.disable_lock_intro, container, false);
		return v;
	}

	@Override
	public int getDefaultBackgroundColor() {
		return Color.parseColor("#FF5733");
	}

	@Override
	public void setBackgroundColor(@ColorInt int backgroundColor) {
		View layoutContainer = getView();

		if (layoutContainer != null) {
			layoutContainer.setBackgroundColor(backgroundColor);
		}
	}

	@Override
	public boolean isPolicyRespected() {
		KeyguardManager keyguardManager = (KeyguardManager) getContext().getSystemService(KEYGUARD_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			if (!keyguardManager.isKeyguardSecure()) {
				AppLogger.info("Lockscreen is turned off!");
				return true;
			}
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (!keyguardManager.isDeviceSecure()) {
				AppLogger.info("Lockscreen is turned off!");
				return true;
			}
		}
		AppLogger.info("Lockscreen is turned on!");
		return false;
	}

	@Override
	public void onUserIllegallyRequestedNextPage() {
		AlertDialogUtil alertDialogUtil = new AlertDialogUtil();

		//alertDialogUtil
	}
}
