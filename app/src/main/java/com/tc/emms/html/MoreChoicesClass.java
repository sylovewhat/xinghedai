package com.tc.emms.html;

import java.util.Map;

import org.json.JSONArray;

import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.widget.ChangeWheelPopwindow;
import com.tc.emms.widget.X5WebView;

/*
 * 统一弹窗方法
 */

public class MoreChoicesClass {

	private static String TAG = "MoreChoicesClass";
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
	 * 联动选择器
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
	static String oneKey = "";
	static String twoKey = "";
	static String threeKey = "";
	public static void makeChoiceView(String jsonString,
			final String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " makeChoiceView seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;

		String cellNum = "";
		String fristAry = "";
		String secondAry = "";
		String thirdAry = "";
		JSONArray fristAryJson;
		JSONArray secondAryJson;
		JSONArray thirdAryJson;
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				LogUtils.iLog(TAG, "key: " + keyString + "; value:" + valueString);
				if (keyString != null && keyString.equals("fristAry")) {
					fristAry = valueString;
					LogUtils.iLog(TAG, "fristAry: " + fristAry);
				}
				if (keyString != null && keyString.equals("secondAry")) {
					secondAry = valueString;
					LogUtils.iLog(TAG, "secondAry: " + secondAry);
				}
				if (keyString != null && keyString.equals("thirdAry")) {
					thirdAry = valueString;
					LogUtils.iLog(TAG, "thirdAry: " + thirdAry);
				}

				if (keyString != null && keyString.equals("oneKey")) {
					oneKey = valueString;
					LogUtils.iLog(TAG, "oneKey: " + oneKey);
				}
				if (keyString != null && keyString.equals("twoKey")) {
					twoKey = valueString;
					LogUtils.iLog(TAG, "twoKey: " + twoKey);
				}
				if (keyString != null && keyString.equals("threeKey")) {
					threeKey = valueString;
					LogUtils.iLog(TAG, "threeKey: " + threeKey);
				}
				if (keyString != null && keyString.equals("cellNum")) {
					cellNum = valueString;
					LogUtils.iLog(TAG, "cellNum: " + cellNum);
				}
			}
			fristAryJson = new JSONArray(fristAry);
			secondAryJson = new JSONArray(secondAry);
			thirdAryJson = new JSONArray(thirdAry);
			ChangeWheelPopwindow mChangeAddressPopwindow = new ChangeWheelPopwindow(
					mActivity, fristAryJson, secondAryJson, thirdAryJson,
					oneKey, twoKey, threeKey, cellNum);
			// mChangeAddressPopwindow.setAddress("广东", "深圳", "福田区");
			mChangeAddressPopwindow.showAtLocation(mWebView, Gravity.BOTTOM, 0,
					0);
			mChangeAddressPopwindow
					.setAddresskListener(new ChangeWheelPopwindow.OnAddressCListener() {

						@Override
						public void onClick(String province, String city,
								String area, String strJson) {
							// TODO Auto-generated method stub
							LogUtils.iLog(TAG, "makeChoiceView onClick strJson: " + strJson);
							LogUtils.iLog(TAG, "makeChoiceView onClick: " + province
									+ "-" + city + "-" + area);
							JSONObject jObject = JSON.parseObject(strJson);
							/* 统一方法库回调 */
							webViewReturn(mWebView, returnMethodName, jObject);
						}
					});

		} catch (JSONException e) {
			_obj = e.getMessage();
			_what = ConstantUtils.HANDLER_FAILE;
			throw new RuntimeException(e);
		} catch (org.json.JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		msg.obj = _obj;
		msg.what = _what;
		handler.sendMessage(msg);
	}
}
