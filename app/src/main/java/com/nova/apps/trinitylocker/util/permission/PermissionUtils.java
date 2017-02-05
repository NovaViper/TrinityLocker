package com.nova.apps.trinitylocker.util.permission;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.nova.apps.trinitylocker.util.AlertDialogUtil;
import com.nova.apps.trinitylocker.util.AppLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PermissionUtils {

	//The Context of the Activity
	Context context;
	//The Current Activity
	Activity currentActivity;
	//The Permission Results callback
	PermissionResultCallback permissionResultCallback;

	ArrayList<String> permission_list = new ArrayList<>();
	ArrayList<String> listPermissionsNeeded = new ArrayList<>();
	int dialogContent;
	int reqCode;

	AlertDialogUtil alertUtil = new AlertDialogUtil();

	public PermissionUtils(Context context) {
		this.context = context;
		this.currentActivity = (Activity) context;

		permissionResultCallback = (PermissionResultCallback) context;
	}

	/**
	 * Checks the API Level (Android version) & the permissions
	 *
	 * @param permissions   The permissions
	 * @param dialogContent the dialog of the permissions
	 * @param requestCode   The request code to call
	 */
	public void checkPermissions(ArrayList<String> permissions, int dialogContent, int requestCode) {
		this.permission_list = permissions;
		this.dialogContent = dialogContent;
		this.reqCode = requestCode;

		if (Build.VERSION.SDK_INT >= 23) {
			if (checkAndRequestPermissions(permissions, requestCode)) {
				permissionResultCallback.PermissionGranted(requestCode);
				AppLogger.info("All permissions granted, proceeding to callback");
			}
		} else {
			permissionResultCallback.PermissionGranted(requestCode);
			AppLogger.info("All permissions granted, proceeding to callback");
		}
	}

	/**
	 * Check and request the Permissions
	 *
	 * @param permissions The permissions
	 * @param requestCode
	 * @return
	 */
	public boolean checkAndRequestPermissions(ArrayList<String> permissions, int requestCode) {
		if (permissions.size() > 0) {
			listPermissionsNeeded = new ArrayList<>();
			for (int i = 0; i < permissions.size(); i++) {
				int hasPermission = ContextCompat.checkSelfPermission(currentActivity, permissions.get(i));
				if (hasPermission != PackageManager.PERMISSION_GRANTED) {
					listPermissionsNeeded.add(permissions.get(i));
				}
			}
			if (!listPermissionsNeeded.isEmpty()) {
				ActivityCompat.requestPermissions(currentActivity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), requestCode);
				return false;
			}
		}
		return true;
	}

	/**
	 * Runs on permission request result
	 *
	 * @param requestCode
	 * @param permissions
	 * @param grantResults
	 */
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case 1:
				if (grantResults.length > 0) {
					Map<String, Integer> perms = new HashMap<>();
					for (int i = 0; i < permissions.length; i++) {
						perms.put(permissions[i], grantResults[i]);
					}
					final ArrayList<String> pending_permissions = new ArrayList<>();
					for (int i = 0; i < listPermissionsNeeded.size(); i++) {
						if (perms.get(listPermissionsNeeded.get(i)) != PackageManager.PERMISSION_GRANTED) {
							if (ActivityCompat.shouldShowRequestPermissionRationale(currentActivity, listPermissionsNeeded.get(i)))
								pending_permissions.add(listPermissionsNeeded.get(i));
							else {
								AppLogger.info("Going to settings and waiting for user to enable permissions");
								permissionResultCallback.NeverAskAgain(reqCode);
								Toast.makeText(currentActivity, "Please go to settings and enable all permissions", Toast.LENGTH_LONG).show();
								Intent intent = new Intent();
								intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
								intent.setData(Uri.parse("package:" + currentActivity.getPackageName()));
								intent.addCategory(Intent.CATEGORY_DEFAULT);
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								currentActivity.startActivity(intent);
								return;
							}
						}
					}
					if (pending_permissions.size() > 0) {
						alertUtil.showMessageOKCancel(currentActivity, dialogContent,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										switch (which) {
											case DialogInterface.BUTTON_POSITIVE:
												checkPermissions(permission_list, dialogContent, reqCode);
												break;
											case DialogInterface.BUTTON_NEGATIVE:
												AppLogger.info("Permission not fully given");
												if (permission_list.size() == pending_permissions.size())
													permissionResultCallback.PermissionDenied(reqCode);
												else
													permissionResultCallback.PartialPermissionGranted(reqCode, pending_permissions);
												break;
										}
									}
								});
					} else {
						AppLogger.info("All permissions granted, proceeding to next step");
						permissionResultCallback.PermissionGranted(reqCode);
					}
				}
				break;
		}
	}
}