package com.nova.apps.trinitylocker.core.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nova.apps.trinitylocker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LockProfileOptionFragment extends Fragment {

	View myView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		myView = inflater.inflate(R.layout.fragment_lock_profile_option, container, false);

		return myView;
	}

}
