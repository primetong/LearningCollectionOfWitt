package com.evguard.tools;

import android.util.Log;

public class LogEx {

	public static void i(String Tag, String Message) {
		Log.i(Tag, Message);

	}

	public static void i(String tag, String msg, Throwable tr) {
		Log.i(tag, msg, tr);
	}

	public static void e(String tag, String msg) {
		Log.e(tag, msg);
	}

	public static void e(String tag, String msg, Throwable tr) {
		Log.e(tag, msg, tr);
	}

	public static void d(String tag, String msg) {
		Log.d(tag, msg);
	}

	public static void d(String tag, String msg, Throwable tr) {
		Log.d(tag, msg, tr);
	}

	public static void w(String tag, String msg) {
		Log.w(tag, tag);
	}

	public static void w(String tag, String msg, Throwable tr) {
		Log.w(tag, msg, tr);
	}
}
