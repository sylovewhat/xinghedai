package com.tc.emms.html;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.widget.X5WebView;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;

/*
 * 测试映射方法 
 */

public class WebConfigClass {
	
	private static String TAG = "WebConfigClass";
	/* 统一方法库回调 */
	@SuppressWarnings("unused")
	private static void webViewReturn(final X5WebView mWebView,
			final String returnMethodName, final JSONObject jObject) {
		if (returnMethodName != null && !returnMethodName.equals("")) {
			LogUtils.vLog(TAG, "---统一方法库回调 jObject:" + jObject.toString());
			mWebView.post(new Runnable() {
				@Override
				public void run() {
					mWebView.loadUrl("javascript:" + returnMethodName + "(" + jObject
							+ ")");
				}
			});
		}
	}

	/***
	 * 注册cookie
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return 设备ID
	 */
	public static void registerCookie(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {
		
		Message msg = new Message();
		String _obj = TAG + " registerCookie seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;


		LogUtils.eLog("", "onPageFinished registerCookie");
		LogUtils.eLog("", "onPageFinished registerCookie X5WebView: "
				+ mWebView.getUrl());
		ConstantUtils.LoginWebView = mWebView;
		
		
		msg.obj = _obj;
		msg.what = _what;
		handler.sendMessage(msg);
	}

	/***
	 * 清空缓存
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return 设备ID
	 */
	public static void clearWebCookie(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {
		LogUtils.eLog(TAG, "WebConfigClass clearWebCookie");
		CookieManager cookieManager = CookieManager.getInstance();
		final String CookieStr = cookieManager.getCookie(mWebView.getUrl());
		LogUtils.eLog(TAG, "WebConfigClass clearWebCookie CookieStr：" + CookieStr);
		// 清除cookie
		CookieSyncManager.createInstance(mActivity);
		CookieManager.getInstance().removeAllCookie();
		
		final String CookieStr2 = cookieManager.getCookie(mWebView.getUrl());
		LogUtils.eLog(TAG, "WebConfigClass clearWebCookie CookieStr2：" + CookieStr2);
	}

	/***
	 * 取消cookie
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return 设备ID
	 */
	public static void cancelCookie(String jsonString, String returnMethodName,
			final BaseActivity mActivity, final X5WebView mWebView,
			View headerView, View footerView, final Handler handler) {
		
	}
}
