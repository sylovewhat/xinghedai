package com.tc.emms.utils;

import android.util.Log;

/**
 * 公用的常用工具类
 * 
 * @author xiaoben
 */
public class LogUtils {
	private static String TAG = "mainpayeelogs：";
	
	private static int LOGLEVEL = 6;
	private static int VERBOSE = 1;
	private static int DEBUG = 2;
	private static int INFO = 3;
	private static int WARN = 4;
	private static int ERROR = 5;

	public static void vLog(String tag, String msg) {
		if (LOGLEVEL > VERBOSE) {
			Log.v(tag, TAG + msg); 
		}
	}

	public static void dLog(String tag, String msg) {
		if (LOGLEVEL > DEBUG) {
			Log.d(tag, TAG + msg);
		}
	}

	public static void iLog(String tag, String msg) {
		if (LOGLEVEL > INFO) {
			Log.i(tag, TAG + msg);
		}
	}

	public static void wLog(String tag, String msg) {
		if (LOGLEVEL > WARN) {
			Log.w(tag, TAG + msg);
		}
	}

	public static void eLog(String tag, String msg) {
		if (LOGLEVEL > ERROR) {
			Log.e(tag, TAG + msg);
		}
	}
}
