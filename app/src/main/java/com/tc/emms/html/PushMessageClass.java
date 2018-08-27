package com.tc.emms.html;

import java.util.Map;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tc.emms.R;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.dialog.CommonData;
import com.tc.emms.ui.MainActivity;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.utils.SharedUtils;
import com.tc.emms.utils.ToastShow;
import com.tc.emms.widget.X5WebView;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

/*
 * 推送接口类 
 */

public class PushMessageClass {

	private static String TAG = "PushMessageClass";
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
	 * 获取推送注册设备ID
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
	public static void registerPush(String jsonString, String returnMethodName,
			final BaseActivity mActivity, final X5WebView mWebView,
			View headerView, View footerView, final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " registerPush seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		try {
			JSONObject jObject = new JSONObject();
			/* 默认取出String类型 */
			String _getpushId = SharedUtils.getValue(mActivity,
					ConstantUtils.ALI_DEVICE_ID);
			if (_getpushId.equals("")) {
				_getpushId = "0";
			}
			jObject.put("pushId", _getpushId);
			
			ConstantUtils.PushWebView = mWebView;
			/* 统一方法库回调 */
			webViewReturn(mWebView, returnMethodName, jObject);

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
	 * 回调当前顶层页面，执行刷新操作
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
	public static void pushRefreshLoadView(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " pushRefreshLoadView seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		
		LogUtils.iLog(TAG, "start pushRefreshLoadView");
		/*SharedUtils.setValue(mActivity, ConstantUtils.IS_MAIN_REFRESH, "1");
		SharedUtils.setValue(mActivity, ConstantUtils.IS_SJ_REFRESH, "1");
		SharedUtils.setValue(mActivity, ConstantUtils.IS_TZ_REFRESH, "1");
		SharedUtils.setValue(mActivity, ConstantUtils.IS_ME_REFRESH, "1");*/
		
		LogUtils.iLog(TAG, "return pushRefreshLoadView 刷新当前页面");
		int tab_index = MainActivity.getTabIndex();
		LogUtils.iLog(TAG, "pushRefreshLoadView ConstantUtils.WebViews.size():" + ConstantUtils.WebViews.size());
		for (int i = 0; i < ConstantUtils.WebViews.size(); i++) {
			if(tab_index == i){
				/*回调当前页面刷新*/
				LogUtils.iLog(TAG, "return pushRefreshLoadView tab_index:" + tab_index);
				LogUtils.iLog(TAG, "return pushRefreshLoadView");
				final int nowIndex = i;
				/* 统一方法库回调 */
				ConstantUtils.WebViews.get(i).post(new Runnable() {
					@Override
					public void run() {
						LogUtils.iLog(TAG, "return pushRefreshLoadView nowIndex:" + nowIndex);
						ConstantUtils.WebViews.get(nowIndex).loadUrl("javascript:viewWillAppear()");
					}
				});
			}
		}
		msg.obj = _obj;
		msg.what = _what;
		handler.sendMessage(msg);
	}
	
	/***
	 * 绑定推送账号
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
	public static void countPushAccount(String jsonString, String returnMethodName,
			final BaseActivity mActivity, final X5WebView mWebView,
			View headerView, View footerView, final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " countPushAccount seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		String getKey = "";
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				/* 默认存入String类型 */
				LogUtils.vLog(TAG, "---localhostSave setKey:" + keyString);
				LogUtils.vLog(TAG, "---localhostSave setValue:" + valueString);
				if(keyString != null && keyString.equals("bingKey")){
					getKey = valueString;
				}
			}
			if(!getKey.equals("")){
				final String toastKey = getKey;
				//注意在3.2.2 版本信鸽对账号绑定和解绑接口进行了升级具体详情请参考API文档。
				XGPushManager.bindAccount(mActivity, getKey, new XGIOperateCallback() {
					
					@Override
					public void onSuccess(Object arg0, int arg1) {
						// TODO Auto-generated method stub
						//ToastShow.showShort(mActivity, "绑定推送账号成功！账号： " + toastKey);
						
						Log.i("", "XGPushManager bindAccount CommonData.mNowContext：" + CommonData.mNowContext);
						if(CommonData.mNowContext != null){
							final Dialog dialog = new Dialog(CommonData.mNowContext,
									R.style.Theme_Transparent);
							View v = ((BaseActivity)CommonData.mNowContext).getLayoutInflater().inflate(
									R.layout.dialog_simple_tip, null);
							TextView tv_tip = (TextView) v.findViewById(R.id.tv_tip);
							tv_tip.setText("绑定推送账号成功！账号： " + toastKey);
							dialog.setContentView(v);
							dialog.getWindow().setGravity(Gravity.CENTER);
							dialog.getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
									android.view.ViewGroup.LayoutParams.MATCH_PARENT);
							v.findViewById(R.id.btn_ok).setOnClickListener(
									new OnClickListener() {
										@Override
										public void onClick(View v) {
											dialog.dismiss();
										}
									});
							//dialog.show();
						}
					}
					
					@Override
					public void onFail(Object data, int errCode, String msg) {
						// TODO Auto-generated method stub
						/*ToastShow.showShort(mActivity, "绑定推送账号失败，错误：" + arg2);*/
						
						Log.i("", "XGPushManager bindAccount CommonData.mNowContext：" + CommonData.mNowContext);
						if(CommonData.mNowContext != null){
							final Dialog dialog = new Dialog(CommonData.mNowContext,
									R.style.Theme_Transparent);
							View v = ((BaseActivity)CommonData.mNowContext).getLayoutInflater().inflate(
									R.layout.dialog_simple_tip, null);
							TextView tv_tip = (TextView) v.findViewById(R.id.tv_tip);
							tv_tip.setText("绑定推送账号失败，错误：" + data + ";errCode：" + errCode + ";msg：" + msg);
							dialog.setContentView(v);
							dialog.getWindow().setGravity(Gravity.CENTER);
							dialog.getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
									android.view.ViewGroup.LayoutParams.MATCH_PARENT);
							v.findViewById(R.id.btn_ok).setOnClickListener(
									new OnClickListener() {
										@Override
										public void onClick(View v) {
											dialog.dismiss();
										}
									});
							//dialog.show();
						}
					}
				});
			}else{
				ToastShow.showShort(mActivity, "Key值错误，请重试");
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
	 * 解除绑定推送账号
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
	public static void unCountPushAccount(String jsonString, String returnMethodName,
			final BaseActivity mActivity, final X5WebView mWebView,
			View headerView, View footerView, final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " unCountPushAccount seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		String getKey = "";
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				/* 默认存入String类型 */
				LogUtils.vLog(TAG, "---localhostSave setKey:" + keyString);
				LogUtils.vLog(TAG, "---localhostSave setValue:" + valueString);
				if(keyString != null && keyString.equals("bingKey")){
					getKey = valueString;
				}
			}
			if(!getKey.equals("")){
				//注意在3.2.2 版本信鸽对账号绑定和解绑接口进行了升级具体详情请参考API文档。
				/*XGPushManager.unregisterPush(mActivity);*/
				XGPushManager.delAccount(mActivity, getKey, new XGIOperateCallback() {
					
					@Override
					public void onSuccess(Object arg0, int arg1) {
						// TODO Auto-generated method stub
						LogUtils.vLog(TAG, "---localhostSave delAccount: onSuccess");
					}
					
					@Override
					public void onFail(Object arg0, int arg1, String arg2) {
						// TODO Auto-generated method stub
						
					}
				});
			}else{
				ToastShow.showShort(mActivity, "Key值错误，请重试");
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
