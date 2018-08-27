package com.tc.emms.utils;

import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

@SuppressLint("NewApi") 
public class SharedUtils {

	/**
	 * 获取应用程序的SharedPreferences句柄，只是在本类使用
	 * 
	 * @param context
	 *            不能为null,如果为null，那么返回null
	 * @return
	 */
	private static SharedPreferences getAppSharedPreferences(Context context) {
		if (context == null) {
			return null;
		}
		return context.getSharedPreferences(ConstantUtils.IS_NOW_TYPEONLY, Context.MODE_PRIVATE);
	}

	/**
	 * 功能说明:用来获取当前是否是第一次运行本应用程序
	 * 
	 * @param context
	 * @return 2013-7-30
	 * @since v1.0
	 * @see #setGuideActivityHadRun(Context)
	 */
	public static boolean isFirstOpenApp(Context context) {
		if (context == null) {
			return false;
		}
		SharedPreferences share = getAppSharedPreferences(context);
		return share.getBoolean(ConstantUtils.IS_FRIST_OPEN_APP, true);
	}

	/**
	 * 功能说明:用来设置应用程序已经打开过�??
	 * 
	 * @param context
	 * @return 2013-7-30
	 * @since v1.0
	 * @see #isFirstOpenApp(Context)
	 */
	public static boolean setGuideActivityHadRun(Context context) {
		if (context == null) {
			return false;
		}
		try {
			SharedPreferences share = getAppSharedPreferences(context);
			Editor edi = share.edit();
			edi.putBoolean(ConstantUtils.IS_FRIST_OPEN_APP, false);
			edi.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 功能说明:设置值�??
	 * 
	 * @param context
	 * @param key
	 * @param value
	 * @return
	 * @date 2013-8-19
	 * @since v1.0
	 * @throws null
	 */
	public static boolean setValue(Context context, String key, String value) {
		if (context == null) {
			return false;
		}
		try {
			SharedPreferences share = getAppSharedPreferences(context);
			Editor edi = share.edit();
			edi.putString(key, value);
			edi.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 功能说明:保存设置�??关�?��??
	 * 
	 * @param context
	 * @param key
	 * @param value
	 * @return
	 * @date 2013-8-19
	 * @since v1.0
	 * @throws null
	 */
	public static boolean setBooleanValue(Context context, String key,
			boolean value) {
		if (context == null) {
			return false;
		}
		try {
			SharedPreferences share = getAppSharedPreferences(context);
			Editor edi = share.edit();
			edi.putBoolean(key, value);
			edi.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 功能说明:获取值�??
	 * 
	 * @param context
	 * @param key
	 * @return
	 * @date 2013-8-19
	 * @since v1.0
	 * @throws null
	 */
	public static boolean getBooleanValue(Context context, String key,
			boolean defaultValue) {
		if (context == null) {
			return defaultValue;
		}
		try {
			SharedPreferences share = getAppSharedPreferences(context);
			return share.getBoolean(key, defaultValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultValue;

	}

	/**
	 * 功能说明:获取值�??
	 * 
	 * @param context
	 * @param key
	 * @return
	 * @date 2013-8-19
	 * @since v1.0
	 * @throws null
	 */
	public static String getValue(Context context, String key) {
		if (context == null) {
			return "";
		}
		try {
			SharedPreferences share = getAppSharedPreferences(context);
			return share.getString(key, "");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}

	/**
	 * 功能说明:获取值�??
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 *            默认�??
	 * @return
	 * @throws null
	 */
	public static String getValueWithDefValue(Context context, String key,
			String defValue) {
		if (defValue == null) {
			defValue = "";
		}
		if (context == null) {
			return defValue;
		}
		try {
			SharedPreferences share = getAppSharedPreferences(context);
			return share.getString(key, defValue);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return defValue;

	}

	/**
	 * 功能说明:设置值�??
	 * 
	 * @param context
	 * @param key
	 * @param value
	 * @return
	 * @date 2013-8-19
	 * @since v1.0
	 * @throws null
	 */
	public static boolean setInt(Context context, String key, int value) {
		if (context == null) {
			return false;
		}
		try {
			SharedPreferences share = getAppSharedPreferences(context);
			Editor edi = share.edit();
			edi.putInt(key, value);
			edi.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 功能说明:获取值�??
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 *            默认�??
	 * @return
	 * @throws null
	 */
	public static int getInt(Context context, String key) {

		if (context == null) {
			return 0;
		}
		try {
			SharedPreferences share = getAppSharedPreferences(context);
			return share.getInt(key, 0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;

	}

	/***
	 * 
	 * @param context
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean setSetValues(Context context, String key,
			Set<String> value) {
		if (context == null) {
			return false;
		}
		try {
			SharedPreferences share = getAppSharedPreferences(context);
			Editor edi = share.edit();
			edi.putStringSet(key, value);
			edi.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 功能说明:获取值�??
	 * 
	 * @param context
	 * @param key
	 * @return
	 * @date 2013-8-19
	 * @since v1.0
	 * @throws null
	 */
	public static Set<String> getSetValue(Context context, String key) {
		if (context == null) {
			return null;
		}
		try {
			SharedPreferences share = getAppSharedPreferences(context);
			return share.getStringSet(key, null);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}
