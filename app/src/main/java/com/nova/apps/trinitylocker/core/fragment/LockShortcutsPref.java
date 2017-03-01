package com.nova.apps.trinitylocker.core.fragment;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.nova.apps.trinitylocker.R;


public class LockShortcutsPref extends PreferenceFragmentCompat {

	@Override
	public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
		addPreferencesFromResource(R.xml.pref_lock_shortcuts);
	}
}
