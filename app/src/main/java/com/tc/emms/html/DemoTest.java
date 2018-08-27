package com.tc.emms.html;

import android.webkit.WebView;

import com.tc.emms.base.BaseActivity;



/*
 * 测试映射方法 
 */

public class DemoTest {

	/***
	 * 获取设备ID
	 * 
	 * @param jsonString 传入字符串参数
	 * @param returnMethodName 返回方法
	 * @param mActivity 当前视图
	 * @param mWebView 对象
	 * @return 设备ID
	 */
	public static String getDeviceID(String jsonString, String returnMethodName, BaseActivity mActivity, WebView mWebView) {
		String strResult = null;
		strResult = "AAA-" + jsonString + "-CCC";
		return strResult;
	}
	
	/***
	 * 数据库方法
	 * 
	 * @param jsonString 传入字符串参数
	 * @param returnMethodName 返回方法
	 * @param mActivity 当前视图
	 * @param mWebView 对象
	 * @return 设备ID
	 */
	public static String getDeviceID1(String jsonString, String returnMethodName, BaseActivity mActivity, WebView mWebView) {
		String strResult = null;
		strResult = "AAA-" + jsonString + "-CCC";
		
		return strResult;
	}
}
