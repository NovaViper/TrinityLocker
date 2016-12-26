package com.nova.apps.trinitylocker.util;

import android.util.Log;

/***
 * FOR DEVELOPER REFERENCE ONLY
 * <p>
 * V    Verbose (Reports all possibly useless logs, default level)
 * D    Debug (Reports all reasonable debug logs)
 * I    Info (Reports expected logs for regular usage)
 * W    Warn (Reports possible issues that are not yet errors)
 * E    Error (Reports issues that have caused errors)
 * F    Fatal (Reports issues that are fatal to runtime and will often result in rebooting)
 * WTF  WTF (Reports a condition that should never happen)
 */

public final class AppLogger {
	private static final String TAG = "Trinity Locker";
	private static final String EMPTY = "";
	private static boolean disableLogging;

	/**
	 * Used to disables logs for VERBOSE, DEBUG, INFO, WARN levels
	 * Is called once and preferably from the class that extends 'Application'
	 * This is useful when you might want to disable log printing before publishing app to the PlayStore
	 * This will disable printing log for VERBOSE, DEBUG, INFO, WARN level
	 *
	 * @param value Setting value to true will disable printing logs (Default false)
	 */
	public static void disableLogging(boolean value) {
		AppLogger.disableLogging = value;
	}


	public static void verbose(String format, Object... args) {
		if(disableLogging){
			return;
		}

		Log.v(TAG, format(format, args));
	}


	public static void verbose(String msg, Throwable e) {
		if(disableLogging){
			return;
		}

		Log.v(TAG, msg, e);
	}

	public static void verbose(String format, Throwable e, Object... args) {
		if(disableLogging){
			return;
		}

		Log.v(TAG, format(format, args), e);
	}

	public static void debug(String format, Object... args) {
		if(disableLogging){
			return;
		}

		Log.d(TAG, format(format, args));
	}

	public static void debug(String msg, Throwable e) {
		if(disableLogging){
			return;
		}

		Log.d(TAG, msg, e);
	}


	public static void debug(String format, Throwable e, Object... args) {
		if(disableLogging){
			return;
		}

		Log.d(TAG, format(format, args), e);
	}

	public static void warn(String format, Object... args) {
		if(disableLogging){
			return;
		}

		Log.w(TAG, format(format, args));
	}

	public static void warn(String msg, Throwable e) {
		if(disableLogging){
			return;
		}

		Log.w(TAG, msg, e);
	}


	public static void warn(String format, Throwable e, Object... args) {
		if(disableLogging){
			return;
		}

		Log.w(TAG, format(format, args), e);
	}

	public static void info(String format, Object... args) {
		if(disableLogging){
			return;
		}

		Log.i(TAG, format(format, args));
	}

	public static void info(String msg, Throwable e) {
		if(disableLogging){
			return;
		}

		Log.i(TAG, msg, e);
	}

	public static void info(String format, Throwable e, Object... args) {
		if(disableLogging){
			return;
		}

		Log.i(TAG, format(format, args), e);
	}

	public static void error(String format, Object... args) {
		Log.e(TAG, format(format, args));
	}

	public static void error(String msg, Throwable e) {
		Log.e(TAG, msg, e);
	}

	public static void error(String format, Throwable e, Object... args) {
		Log.e(TAG, format(format, args), e);
	}

	public void wtf(String message, Object... args) {
		Log.wtf(TAG, format(message, args));
	}

	@Deprecated
	public void wtf(Throwable e, String message, Object... args) {
		Log.wtf(TAG, format(message, args), e);
	}

	private static String format(String format, Object... args) {
		try {
			return String.format(format == null ? EMPTY : format, args);
		} catch (RuntimeException e) {
			AppLogger.warn(TAG, "Format Error. Reason=%s, Format=%s", e.getMessage(), format);
			return String.format(EMPTY, format);
		}
	}
}