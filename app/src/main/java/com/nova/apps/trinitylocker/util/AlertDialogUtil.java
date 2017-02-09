package com.nova.apps.trinitylocker.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by novag on 2/4/2017.
 */

public class AlertDialogUtil {

	public void showMessageOKCancel(Activity currentActivity, int message, DialogInterface.OnClickListener okListener) {
		new AlertDialog.Builder(currentActivity)
				.setMessage(message)
				.setPositiveButton("Ok", okListener)
				.setNegativeButton("Cancel", okListener)
				.create()
				.show();
	}

	public void showMessageAgreeDisagreeWithTitle(Activity currentActivity, int title, int message, DialogInterface.OnClickListener okListener) {
		new AlertDialog.Builder(currentActivity)
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton("Agree", okListener)
				.setNegativeButton("Disagree", okListener)
				.create()
				.show();
	}
}
