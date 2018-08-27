package com.tc.emms.html;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.utils.RSAUtils;
import com.tc.emms.utils.SharedUtils;
import com.tc.emms.widget.X5WebView;



/*
 * 参数加密封装
 */

public class EncryptionClass {

	private static String TAG = "EncryptionClass";
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
	 * 网络请求加密封装【登录】
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
	public static void loginBusinessNum(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " loginBusinessNum seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		
		try {
			
			/****** 对请求体加密 *****/
			String business_no = SharedUtils.getValue(mActivity,
					ConstantUtils.BUSINESS_NO);
			LogUtils.vLog(TAG, "loginBusinessNum 加密前 business_no: " + business_no);
			String str_key = SharedUtils.getValue(mActivity,
					ConstantUtils.RSA_KEY);
			LogUtils.vLog(TAG, "loginBusinessNum 加密前 str_key: " + str_key);
			LogUtils.vLog(TAG, "loginBusinessNum 加密前: " + jsonString);
			String _sign = RSAUtils.rsaEncode(jsonString, business_no, str_key);
			LogUtils.vLog(TAG, "loginBusinessNum 加密后: " + _sign);
			JSONObject new_json = new JSONObject();
			new_json.put("s", _sign);
			/* 统一方法库回调 */
			webViewReturn(mWebView, returnMethodName, new_json);

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
