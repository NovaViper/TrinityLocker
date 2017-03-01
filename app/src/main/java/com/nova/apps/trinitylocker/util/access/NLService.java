package com.nova.apps.trinitylocker.util.access;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import com.nova.apps.trinitylocker.util.AppLogger;

//TODO Work on Notificaiton Service Listener
public class NLService extends NotificationListenerService {

	Context context;

	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
	}

	public void onNotificationPosted(StatusBarNotification sbn) {
		String pack = sbn.getPackageName();

		String text = "";
		String title = "";
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Bundle extras = extras = sbn.getNotification().extras;
			text = extras.getCharSequence("android.text").toString();
			title = extras.getString("android.title");
		}

		AppLogger.info("Package", pack);
		AppLogger.info("Title", title);
		AppLogger.info("Text", text);
	}

	@Override
	public void onNotificationRemoved(StatusBarNotification sbn) {
		Log.i("Msg", "Notification Removed");
	}
}