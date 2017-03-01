package com.nova.apps.trinitylocker.core.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import com.nova.apps.trinitylocker.R;

/**
 * Created by NovaViper on 2/13/2017.
 */
public class LockProfilePref extends PreferenceFragmentCompat implements PreferenceFragmentCompat.OnPreferenceStartScreenCallback{

	@Override
	public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
		addPreferencesFromResource(R.xml.pref_lock_profile);
	}

	@Override
	public Fragment getCallbackFragment() {
		return this;
	}

	@Override
	public boolean onPreferenceStartScreen(PreferenceFragmentCompat caller, PreferenceScreen pref) {
		caller.setPreferenceScreen(pref);
		return true;
	}
}