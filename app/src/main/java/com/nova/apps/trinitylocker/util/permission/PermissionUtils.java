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

import com.nova.apps.trinitylocker.util.AppLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PermissionUtils {

	//The Context of the Activity
	Context context;
	//The Current Activity
	Activity current_activity;
	//The Permission Results callback
	PermissionResultCallback permissionResultCallback;

	ArrayList<String> permission_list = new ArrayList<>();
	ArrayList<String> listPermissionsNeeded = new ArrayList<>();
	int dialog_content;
	int req_code;

	public PermissionUtils(Context context) {
		this.context = context;
		this.current_activity = (Activity) context;

		permissionResultCallback = (PermissionResultCallback) context;
	}

	/**
	 * Check the API Level (Android version) & Permission
	 *
	 * @param permissions    The permissions
	 * @param dialog_content the dialog of the permissions
	 * @param request_code   The request code to call
	 */
	public void check_permission(ArrayList<String> permissions, int dialog_content, int request_code) {
		this.permission_list = permissions;
		this.dialog_content = dialog_content;
		this.req_code = request_code;

		if (Build.VERSION.SDK_INT >= 23) {
			if (checkAndRequestPermissions(permissions, request_code)) {
				permissionResultCallback.PermissionGranted(request_code);
				AppLogger.info("All permissions granted, proceeding to callback");
			}
		} else {
			permissionResultCallback.PermissionGranted(request_code);
			AppLogger.info("All permissions granted, proceeding to callback");
		}
	}

	/**
	 * Check and request the Permissions
	 *
	 * @param permissions  The permissions
	 * @param request_code
	 * @return
	 */
	public boolean checkAndRequestPermissions(ArrayList<String> permissions, int request_code) {
		if (permissions.size() > 0) {
			listPermissionsNeeded = new ArrayList<>();
			for (int i = 0; i < permissions.size(); i++) {
				int hasPermission = ContextCompat.checkSelfPermission(current_activity, permissions.get(i));
				if (hasPermission != PackageManager.PERMISSION_GRANTED) {
					listPermissionsNeeded.add(permissions.get(i));
				}
			}
			if (!listPermissionsNeeded.isEmpty()) {
				ActivityCompat.requestPermissions(current_activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), request_code);
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
							if (ActivityCompat.shouldShowRequestPermissionRationale(current_activity, listPermissionsNeeded.get(i)))
								pending_permissions.add(listPermissionsNeeded.get(i));
							else {
								AppLogger.info("Going to settings and waiting for user to enable permissions");
								permissionResultCallback.NeverAskAgain(req_code);
								Toast.makeText(current_activity, "Please go to settings and enable all permissions", Toast.LENGTH_LONG).show();
								Intent intent = new Intent();
								intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
								intent.setData(Uri.parse("package:" + current_activity.getPackageName()));
								intent.addCategory(Intent.CATEGORY_DEFAULT);
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								current_activity.startActivity(intent);
								return;
							}
						}
					}
					if (pending_permissions.size() > 0) {
						showMessageOKCancel(dialog_content,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {

										switch (which) {
											case DialogInterface.BUTTON_POSITIVE:
												check_permission(permission_list, dialog_content, req_code);
												break;
											case DialogInterface.BUTTON_NEGATIVE:
												AppLogger.info("Permission not fully given");
												if (permission_list.size() == pending_permissions.size())
													permissionResultCallback.PermissionDenied(req_code);
												else
													permissionResultCallback.PartialPermissionGranted(req_code, pending_permissions);
												break;
										}
									}
								});
					} else {
						AppLogger.info("All permissions granted, proceeding to next step");
						permissionResultCallback.PermissionGranted(req_code);
					}
				}
				break;
		}
	}

	/**
	 * Explain why the app needs permissions
	 *
	 * @param message
	 * @param okListener
	 */
	private void showMessageOKCancel(int message, DialogInterface.OnClickListener okListener) {
		new AlertDialog.Builder(current_activity)
				.setMessage(message)
				.setPositiveButton("Ok", okListener)
				.setNegativeButton("Cancel", okListener)
				.create()
				.show();
	}
}