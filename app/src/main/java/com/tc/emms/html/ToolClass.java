package com.tc.emms.html;

import java.util.Map;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.utils.SharedUtils;
import com.tc.emms.widget.X5WebView;



/*
 * 工具类功能库 
 */

public class ToolClass {

	private static String TAG = "ToolClass";
	/* 统一方法库回调 */
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
	 * 复制文本信息
	 * 
	 * @param jsonString 传入字符串参数
	 * @param returnMethodName 返回方法
	 * @param mActivity 当前视图
	 * @param mWebView 对象
	 * @return 设备ID
	 */
	@SuppressWarnings("deprecation")
	public static void copyText(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " copyText seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		String textStr = "";
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && keyString.equals("textStr")) {
					textStr = valueString;
					LogUtils.vLog(TAG, "--- copyText textStr:" + textStr);
				}
			}
			if(!textStr.equals("")){
				// 从API11开始android推荐使用android.content.ClipboardManager
		        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
		        ClipboardManager cm = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
		        // 将文本内容放到系统剪贴板里。
		        cm.setText(textStr);
		        /* 统一方法库回调 */
				webViewReturn(mWebView, returnMethodName, new JSONObject());
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
	 * 启用/禁用返回键
	 * 
	 * @param jsonString 传入字符串参数
	 * @param returnMethodName 返回方法
	 * @param mActivity 当前视图
	 * @param mWebView 对象
	 * @return 设备ID
	 */
	public static void setBack(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " setBack seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		String isOpen = "";
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && keyString.equals("isOpen")) {
					isOpen = valueString;
					LogUtils.vLog(TAG, "---isOpen:" + isOpen);
				}
			}
			if(isOpen.equals("0")){
				// 禁用返回键
				SharedUtils.setBooleanValue(mActivity, ConstantUtils.IS_OPEN_BACK, true);
			}else{
				// 启用回键
				SharedUtils.setBooleanValue(mActivity, ConstantUtils.IS_OPEN_BACK, false);
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
