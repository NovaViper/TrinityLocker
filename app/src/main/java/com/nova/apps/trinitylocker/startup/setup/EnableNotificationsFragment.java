package com.nova.apps.trinitylocker.startup.setup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;
import com.github.paolorotolo.appintro.ISlidePolicy;
import com.nova.apps.trinitylocker.R;


public class EnableNotificationsFragment extends Fragment implements ISlideBackgroundColorHolder {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.enable_notify_intro, container, false);
		return v;
	}

	@Override
	public int getDefaultBackgroundColor() {
		return Color.parseColor("#CBE456");
	}

	@Override
	public void setBackgroundColor(@ColorInt int backgroundColor) {
		View layoutContainer = getView();

		if(layoutContainer != null){
			layoutContainer.setBackgroundColor(backgroundColor);
		}
	}
}
