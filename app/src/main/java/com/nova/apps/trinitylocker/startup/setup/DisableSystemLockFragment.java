package com.nova.apps.trinitylocker.startup.setup;

import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;
import com.github.paolorotolo.appintro.ISlidePolicy;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;
import com.nova.apps.trinitylocker.R;
import com.nova.apps.trinitylocker.util.AlertDialogUtil;
import com.nova.apps.trinitylocker.util.AppLogger;

import static android.content.Context.KEYGUARD_SERVICE;

public class DisableSystemLockFragment extends Fragment implements ISlideBackgroundColorHolder, ISlidePolicy {

	private boolean forceEnabledSystemLockScreen;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		return inflater.inflate(R.layout.intro_disable_lock, container, false);
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
		TextView textView = (TextView) getView().findViewById(R.id.disableLockTxtBt);
		IconicsImageView imageView = (IconicsImageView) getView().findViewById(R.id.intro_disableSLImage);
		KeyguardManager keyguardManager = (KeyguardManager) getContext().getSystemService(KEYGUARD_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			if (!keyguardManager.isKeyguardSecure()) {
				AppLogger.info("Lockscreen is turned off!");
				textView.setText(R.string.disableSLBTLockDisabled);
				textView.setClickable(false);
				imageView.setImageDrawable(new IconicsDrawable(getActivity()).icon(GoogleMaterial.Icon.gmd_check_circle));


				return true;

			} else {
				AppLogger.info("Lockscreen is turned on!");
				if (forceEnabledSystemLockScreen == true) {
					AppLogger.info("User wants to keep system lock on!");
					textView.setText(R.string.disableSLBTLockForced);
					textView.setClickable(false);
					imageView.setIcon(new IconicsDrawable(getActivity()).icon(GoogleMaterial.Icon.gmd_check_circle));

					return true;
				} else {
					return false;
				}
			}
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (!keyguardManager.isDeviceSecure()) {
				AppLogger.info("Lockscreen is turned off!");
				textView.setText(R.string.disableSLBTLockDisabled);
				textView.setClickable(false);
				imageView.setImageDrawable(new IconicsDrawable(getActivity()).icon(GoogleMaterial.Icon.gmd_check_circle));
				return true;
			} else {
				AppLogger.info("Lockscreen is turned on!");
				if (forceEnabledSystemLockScreen == true) {
					AppLogger.info("User wants to keep system lock on!");
					textView.setText(R.string.disableSLBTLockForced);
					textView.setClickable(false);
					imageView.setIcon(new IconicsDrawable(getActivity()).icon(GoogleMaterial.Icon.gmd_check_circle));
					return true;
				} else {
					return false;
				}
			}
		}
		AppLogger.info("Lockscreen is turned on!");
		return false;
	}

	@Override
	public void onUserIllegallyRequestedNextPage() {
		AlertDialogUtil alertDialogUtil = new AlertDialogUtil();

		alertDialogUtil.showMessageAgreeDisagreeWithTitle(getActivity(), R.string.warnKeepSystemLockOnTitle, R.string.warnKeepSystemLockOn, new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						forceEnabledSystemLockScreen = true;
						isPolicyRespected();
						break;
					case DialogInterface.BUTTON_NEGATIVE:
						forceEnabledSystemLockScreen = false;
						TextView text = (TextView) getView().findViewById(R.id.disableLockTxtBt);
						text.callOnClick();
						isPolicyRespected();
						break;
				}
			}
		});
	}
}