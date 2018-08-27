package com.tc.emms.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.tc.emms.base.BaseActivity;
import com.tc.emms.config.BaseApplictaion;

/**
 * 通用工具�?
 */
@SuppressLint("SimpleDateFormat")
public class AppUtils {

	/**
	 * 获取包名
	 * 
	 * @return 当前应用的版本号
	 */
	public static String getPackage() {
		try {
			String pkName = BaseApplictaion.getInstance().getPackageName();
			return pkName;
		} catch (Exception e) {
			e.printStackTrace();
			return ConstantUtils.APP_PACKAGE;
		}
	}

	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 */
	public static String getVersion(BaseActivity mActivity) {
		try {
			PackageManager manager = mActivity.getPackageManager();
			PackageInfo info = manager.getPackageInfo(
					mActivity.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "1.0";
		}
	}

	/**
	 * get App versionCode
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionCode(BaseActivity mActivity) {
		PackageManager packageManager = mActivity.getPackageManager();
		PackageInfo packageInfo;
		String versionCode = "";
		try {
			packageInfo = packageManager.getPackageInfo(
					mActivity.getPackageName(), 0);
			versionCode = packageInfo.versionCode + "";
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 获取软件名称
	 * 
	 * @return 当前应用的软件名称
	 */
	public static String getApplicationName(BaseActivity mActivity) {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = mActivity.getApplicationContext()
					.getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(
					mActivity.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager
				.getApplicationLabel(applicationInfo);
		return applicationName;
	}

	/**
	 * 获取当前正在运行的Activity名称
	 * 
	 * @return 当前正在运行的Activity名称
	 */
	public static String getActivityName(Context context) {
		String contextString = context.toString();
		return contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));
	}
}
