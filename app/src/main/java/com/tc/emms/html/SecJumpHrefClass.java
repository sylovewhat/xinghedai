package com.tc.emms.html;

import java.util.Map;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.ui.CaptureActivity;
import com.tc.emms.ui.InitWebActivity;
import com.tc.emms.ui.WebActivity;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.widget.X5WebView;



/*
 * 统一跳转封装
 */

public class SecJumpHrefClass {

	private static String TAG = "SecJumpHrefClass";
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
	 * 跳转
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
	public static void sec_jump_href(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " sec_jump_href seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;

		// 网页地址或html名字
		String url_html = "";
		// 网页参数
		String url_data = "";
		// 页面高度,有导航栏传45，没有导航栏传0
		String viewHeigh = "";
		// 加载方式：0，为html加载方式；1，为url加载方式
		String loadType = "";
		String url_title = "";
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && keyString.equals("url_html")) {
					url_html = valueString;
					LogUtils.vLog(TAG, "--- url_html:" + url_html);
				}
				if (keyString != null && keyString.equals("url_data")) {
					url_data = valueString;
					LogUtils.vLog(TAG, "--- url_data:" + url_data);
				}
				if (keyString != null && keyString.equals("viewHeigh")) {
					viewHeigh = valueString;
					LogUtils.vLog(TAG, "--- viewHeigh:" + viewHeigh);
				}
				if (keyString != null && keyString.equals("loadType")) {
					loadType = valueString;
					LogUtils.vLog(TAG, "--- loadType:" + loadType);
				}
				if (keyString != null && keyString.equals("url_title")) {
					url_title = valueString;
					LogUtils.vLog(TAG, "--- url_title:" + url_title);
				}
			}
			if (!loadType.equals("")) {
				/* 跳转html */
				String go_url = "";
				if (!url_data.equals("") && !url_data.equals("no")) {
					go_url = url_html + url_data;

				} else {
					go_url = url_html;
				}
				LogUtils.eLog(TAG, "*** 跳转到 :" + go_url);
				if (loadType.equals("0")) {
					
					Intent intent = new Intent(mActivity, WebActivity.class);
					intent.putExtra(ConstantUtils.HTML_NAME, url_html);
					intent.putExtra(ConstantUtils.HTML_URL, go_url);
					mActivity.startActivity(intent);
				} else {
					/* 打开web url */
					/* 直接跳转 */

					Intent intent = new Intent(mActivity,
							InitWebActivity.class);
					intent.putExtra(ConstantUtils.HTML_URL, go_url);
					intent.putExtra(ConstantUtils.HTML_NAME, url_title);
					intent.putExtra(ConstantUtils.IS_CHECK, "0");
					mActivity.startActivity(intent);
				}
			}

		} catch (JSONException e) {
			_obj = e.getMessage();
			_what = ConstantUtils.HANDLER_FAILE;
			throw new RuntimeException(e);
		}
		msg.obj = _obj;
		msg.what = _what;
		handler.sendMessage(msg);
	}
	
	/***
	 * 扫码页面
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
	public static void getScanCode(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " getScanCode seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		
		ConstantUtils.ChooseImgWebView = mWebView;
		ConstantUtils.returnMethodName = returnMethodName;
		
		String scanType = "";
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && keyString.equals("scanType")) {
					scanType = valueString;
					LogUtils.vLog(TAG, "--- getScanCode scanType:" + scanType);
				}
				
			}
			if (!scanType.equals("")) {
				/* 直接跳转 */
				Intent intent = new Intent(mActivity, CaptureActivity.class);
				intent.putExtra(ConstantUtils.CAPTURE_SCAN_TYPE, scanType);
				mActivity.startActivity(intent);
			}

		} catch (JSONException e) {
			_obj = e.getMessage();
			_what = ConstantUtils.HANDLER_FAILE;
			throw new RuntimeException(e);
		}
		
		
		msg.obj = _obj;
		msg.what = _what;
		handler.sendMessage(msg);
	}
}
