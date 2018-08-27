package com.tc.emms.html;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.config.BaseApplictaion;
import com.tc.emms.model.TitleBar;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.FileUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.utils.SharedUtils;
import com.tc.emms.widget.X5WebView;

import java.util.Map;



/*
 * HTML更新控制功能库 
 */

public class HtmlUpdateClass {

	private static String TAG = "HtmlUpdateClass";
	/* 统一方法库回调 */
	private static void webViewReturn(final X5WebView mWebView,
			final String returnMethodName, final JSONObject jObject) {
		Log.d("sylove","进入统一回调方法==============================" +
				"+++++++++++++++++++++++++++："+returnMethodName);
		if (returnMethodName != null && !returnMethodName.equals("")) {
			LogUtils.vLog(TAG, "---统一方法库回调 jObject:" + jObject.toString());
			mWebView.post(new Runnable() {
				@Override
				public void run() {

					mWebView.loadUrl("javascript:" + returnMethodName + "(" + jObject + ")");
					Log.d("sylove","加载的json=======================================" +
							"+++++++++++++++++++++++++++："+jObject.get("url_html"));
//                    mWebView.loadUrl("https://www.baidu.com/");

				}
			});
		}
	}

	/**
	 * 新增加（统一回调方法）
	 * @param mWebView
	 * @param returnMethodName
	 * @param jsonString
	 */
	private static void webViewReturnNew(final X5WebView mWebView,
									  final String returnMethodName, final String jsonString,final BaseActivity mActivity) {
		try {
			JSONObject object = JSON.parseObject(jsonString);

			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue()
						.toString();

				if (keyString != null && valueString != null) {

					if (keyString.equals("url_html")) {
                         if(valueString!=null||!valueString.equals(""))
						 {
							 final String HTML_URL=valueString;
							 final String  u_path = FileUtils.getU_PATH(mActivity);
							 String _url = ConstantUtils.U_BASE + u_path + HTML_URL;

							 Log.d("sylove","加载路径================================"+_url);
							 if(ConstantUtils.IS_OPEN_DEBUG){
								 /*测试环境地址*/
								 final String local_downurl = SharedUtils.getValue(BaseApplictaion.getInstance(), ConstantUtils.UPDATE_LOCAL_MAIN);
								 com.tencent.mm.opensdk.utils.Log.d(TAG, "测试地址:" + local_downurl);
								 _url = local_downurl + HTML_URL;
							 }
							 String final_url = _url;
							 mWebView.post(new Runnable() {
								 @Override
								 public void run() {

									 mWebView.loadUrl(final_url);
								 }
							 });
						 }
					}


				}
			}
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}


	}


	/***
	 * 设置html立即更新 (新加的方法)
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
	public static void htmlUpdateNew(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " htmlUpdate seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		
		try {
			/*LoginActivity.initData();*/
			/* 统一方法库回调 */
			webViewReturnNew(mWebView, returnMethodName,jsonString,mActivity);

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
	 * 设置html立即更新
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
	public static void htmlUpdate(String jsonString,
								  String returnMethodName, final BaseActivity mActivity,
								  final X5WebView mWebView, View headerView, View footerView,
								  final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " htmlUpdate seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;

		try {
			/*LoginActivity.initData();*/
			/* 统一方法库回调 */
			webViewReturn(mWebView, returnMethodName, new JSONObject());

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
