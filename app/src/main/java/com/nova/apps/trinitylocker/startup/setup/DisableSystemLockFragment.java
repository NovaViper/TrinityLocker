package com.nova.apps.trinitylocker.startup.setup;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nova.apps.trinitylocker.R;

public class DisableSystemLockFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_disable_system_lock, container, false);
		Button signInButton = (Button) v.findViewById(R.id.disableSystemLockBtn);
		signInButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(Settings.ACTION_SECURITY_SETTINGS), 0);
			}
		});

		return v;
	}
}
