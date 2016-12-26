package com.nova.apps.trinitylocker.util.permission;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import com.nova.apps.trinitylocker.util.AppLogger;

import java.util.ArrayList;

public abstract class PermissionActivity extends AppCompatActivity implements
		ActivityCompat.OnRequestPermissionsResultCallback, PermissionResultCallback {

	// list of permissions
	public ArrayList<String> permissions = new ArrayList<>();
	public PermissionUtils permissionUtils;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();
		permissionUtils = new PermissionUtils(this);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		// redirects to utils
		permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	// Callback functions
	@Override
	public void PermissionGranted(int request_code) {
		AppLogger.info("PERMISSION >> GRANTED");
	}

	@Override
	public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
		AppLogger.info("PERMISSION >> PARTIALLY GRANTED");
	}

	@Override
	public void PermissionDenied(int request_code) {
		AppLogger.info("PERMISSION >> DENIED");
	}

	@Override
	public void NeverAskAgain(int request_code) {
		AppLogger.info("PERMISSION >> NEVER ASK AGAIN");
	}
}