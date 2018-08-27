package com.tc.emms.html;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tc.emms.R;
import com.tc.emms.adapter.CameraListAdapter;
import com.tc.emms.adapter.PrintListAdapter;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.bean.BaseResponse;
import com.tc.emms.db.DBHelper;
import com.tc.emms.model.PRINT_ITEMS;
import com.tc.emms.printer.BluetoothUtil;
import com.tc.emms.printer.PrintUtil;
import com.tc.emms.service.BusinessException;
import com.tc.emms.service.BusinessResolver.BusinessCallback;
import com.tc.emms.service.PayeeBusines;
import com.tc.emms.ui.CaptureActivity;
import com.tc.emms.ui.InitWebActivity;
import com.tc.emms.ui.Test1Activity;
import com.tc.emms.ui.WebActivity;
import com.tc.emms.utils.AppUtils;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.DateUtils;
import com.tc.emms.utils.DeviceUtils;
import com.tc.emms.utils.DownloadTask;
import com.tc.emms.utils.FileUtils;
import com.tc.emms.utils.ImageUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.utils.MD5Utils;
import com.tc.emms.utils.NetUtils;
import com.tc.emms.utils.SharedUtils;
import com.tc.emms.utils.ToastShow;
import com.tc.emms.utils.ToastUtils;
import com.tc.emms.utils.ZipExtractorUtils;
import com.tc.emms.widget.ColorfulProgressBar;
import com.tc.emms.widget.X5WebView;

/*
 * 映射方法 
 */

@SuppressWarnings("deprecation")
@SuppressLint({"HandlerLeak", "InflateParams"})
public class FuctionClass {

	private static String TAG = "FuctionClass";
	/* 统一方法库回调 */
	private static void webViewReturn(final X5WebView mWebView,
			final String returnMethodName, final JSONObject jObject) {
		if (returnMethodName != null && !returnMethodName.equals("")) {
			LogUtils.vLog(TAG, "---统一方法库回调 jObject:" + jObject.toString());
			mWebView.post(new Runnable() {
				@Override
				public void run() {
					mWebView.loadUrl("javascript:" + returnMethodName + "("
							+ jObject + ")");
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
				Log.d("sylove", "*** 跳转到 :" + go_url);
				if (loadType.equals("0")) {
					Log.d("sylove", "打开 WebActivity:" + go_url);
					Intent intent = new Intent(mActivity, WebActivity.class);
					intent.putExtra(ConstantUtils.HTML_NAME, url_html);
					intent.putExtra(ConstantUtils.HTML_URL, go_url);
					mActivity.startActivity(intent);
				} else {
					/* 打开web url */
					/* 直接跳转 */
					Log.d("sylove", "打开 InitWebActivity:" + go_url);
					Intent intent = new Intent(mActivity, InitWebActivity.class);
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
	 * 本地缓存【保存】
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
	public static void localhostSave(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " localhostSave seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
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
				SharedUtils.setValue(mActivity, keyString, valueString);
			}
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

	/***
	 * 本地缓存【获取】
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
	public static void localhostGet(String jsonString, String returnMethodName,
			final BaseActivity mActivity, final X5WebView mWebView,
			View headerView, View footerView, final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " localhostGet seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		try {
			JSONObject jObject = new JSONObject();
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				LogUtils.vLog(TAG, "---localhostGet keyString:" + keyString);
				/* 默认取出String类型 */
				String _getValue = SharedUtils.getValue(mActivity, keyString);
				jObject.put(keyString, _getValue);
			}
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
	 * 网络请求加密封装
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
	public static void encrypting498(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " encrypting498 seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		try {

			JSONObject new_json = JSON.parseObject(jsonString);
			org.json.JSONObject o_json;
			o_json = new org.json.JSONObject(jsonString);
			String _sign = MD5Utils.getMD5(o_json, "0");
			new_json.put("sign", _sign);
			/* 统一方法库回调 */
			webViewReturn(mWebView, returnMethodName, new_json);

		} catch (JSONException e) {
			_obj = e.getMessage();
			_what = ConstantUtils.HANDLER_FAILE;
			throw new RuntimeException(e);
		} catch (org.json.JSONException e) {
			_obj = e.getMessage();
			_what = ConstantUtils.HANDLER_FAILE;
			e.printStackTrace();
		}
		msg.obj = _obj;
		msg.what = _what;
		handler.sendMessage(msg);
	}

	/***
	 * 网络请求加密封装
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
	public static void encryptingMD5(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " encrypting498 seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		try {

			JSONObject new_json = JSON.parseObject(jsonString);
			org.json.JSONObject o_json;
			o_json = new org.json.JSONObject(jsonString);
			String _sign = MD5Utils.getMD5(o_json, "1");
			new_json.put("sign", _sign);
			/* 统一方法库回调 */
			webViewReturn(mWebView, returnMethodName, new_json);

		} catch (JSONException e) {
			_obj = e.getMessage();
			_what = ConstantUtils.HANDLER_FAILE;
			throw new RuntimeException(e);
		} catch (org.json.JSONException e) {
			_obj = e.getMessage();
			_what = ConstantUtils.HANDLER_FAILE;
			e.printStackTrace();
		}
		msg.obj = _obj;
		msg.what = _what;
		handler.sendMessage(msg);
	}

	/***
	 * 网络请求
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
	public static void post_action(String jsonString,
			final String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " post_action seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		JSONObject new_json = JSON.parseObject(jsonString);
		String post_url = new_json.getString("post_url");
		String post_key = new_json.getString("post_key");
		LogUtils.vLog(TAG, "--- post_action url:" + post_url);
		LogUtils.vLog(TAG, "--- post_action key:" + post_key);
		String hud_type = new_json.getString("hud_type");
		LogUtils.vLog(TAG, TAG + " post_action hud_type:" + hud_type);
		if (hud_type == null || !"1".equals(hud_type)) {
			/* mActivity.showWaittingDialog(); */
			ToastUtils.getInstances().showWaittingDialog();
		}
		PayeeBusines.post_action(mActivity, new_json, post_url, post_key,
				new BusinessCallback<BaseResponse>() {

					@Override
					public void onSuccess(BaseResponse response,
							String jsonStr, int act) {
						if (response != null) {
							/* 统一更改成返回String格式json字符串 */
							JSONObject jObject = JSON.parseObject(jsonStr);
							/* 统一方法库回调 */
							webViewReturn(mWebView, returnMethodName, jObject);
						}
						ToastUtils.getInstances().dissWaittingDialog();
					}

					@Override
					public void onError(BusinessException e, int act) {
						ToastUtils.getInstances().dissWaittingDialog();
						/* ToastShow.showNetToast(mActivity); */
						String _msg = mActivity
								.getString(R.string.net_noselect);
						if (NetUtils.isConnectedNet(mActivity)) {
							_msg = mActivity.getString(R.string.loading_faile);
						}
						/* 返回错误结果 */
						JSONObject new_json = new JSONObject();
						new_json.put("f", "0");
						new_json.put("m", _msg);
						new_json.put("data", "");
						/* 统一方法库回调 */
						webViewReturn(mWebView, returnMethodName, new_json);
					}
				});
		msg.obj = _obj;
		msg.what = _what;
		handler.sendMessage(msg);
	}

	/***
	 * 提示框【显示】
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
	public static void progressHUDshow(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " progressHUDshow seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		String showType = "0";
		String showTitle = "";
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && keyString.equals("showType")) {
					showType = valueString;
					LogUtils.vLog(TAG, "--- progressHUDshow name:" + showType);
				}
				if (keyString != null && keyString.equals("showTitle")) {
					showTitle = valueString;
					LogUtils.vLog(TAG, "--- progressHUDshow name:" + showTitle);
				}
			}
			if (showType.equals("0")) {
				/* TOAST弹窗 */
				ToastShow.showShort(mActivity, showTitle);
			} else {
				/* 加载弹窗 */
				ToastUtils.getInstances().showWaittingDialog();
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
	 * 提示框【隐藏】
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
	public static void progressHUDend(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		ToastUtils.getInstances().dissWaittingDialog();

		msg.obj = TAG + " progressHUDend seccess.";
		msg.what = ConstantUtils.HANDLER_SECCESS;
		handler.sendMessage(msg);
	}

	/***
	 * 弹窗选择
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
	/* 弹出对话框 */
	private static TextView t_title;
	private static TextView t_message;
	private static Button b_ok;
	private static Button b_cancel;
	private static Dialog dialog;
	private static ColorfulProgressBar seekbar;
	private static String is_must_app = "0";
	private static String new_version = "";
	private static String update_des = "";
	private static int update_index = 0;
	private static String app_save_path;
	private static BaseActivity activity;
	public static void alertSheetShow(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " AlertSheetShow seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		String alertType = "0";
		String alertTitle = "";
		String alertChoice = "";

		/* 文本输入框 */
		String addFieldNum = "0";
		ArrayList<String> pList = new ArrayList<String>();
		ArrayList<String> fList = new ArrayList<String>();
		ArrayList<String> tList = new ArrayList<String>();
		activity = mActivity;
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && keyString.equals("alertType")) {
					alertType = valueString;
					LogUtils.vLog(TAG, "--- AlertSheetShow alertType:"
							+ alertType);
				}
				if (keyString != null && keyString.equals("alertTitle")) {
					alertTitle = valueString;
					LogUtils.vLog(TAG, "--- AlertSheetShow alertTitle:"
							+ alertTitle);
				}
				if (keyString != null && keyString.equals("alertChoice")) {
					alertChoice = valueString;
					LogUtils.vLog(TAG, "--- AlertSheetShow alertChoice:"
							+ alertChoice);
				}
				/* 动态添加输入框 */
				if (keyString != null && keyString.equals("addFieldNum")) {
					addFieldNum = valueString;
					LogUtils.vLog(TAG, "--- AlertSheetShow addFieldNum:"
							+ addFieldNum);
				}
				if (keyString != null && keyString.contains("placeholder")) {
					pList.add(valueString);
					LogUtils.vLog(TAG, "--- AlertSheetShow placeholder ++:"
							+ valueString);
				}
				if (keyString != null && keyString.contains("fieldLoadStr")) {
					fList.add(valueString);
					LogUtils.vLog(TAG, "--- AlertSheetShow fieldLoadStr ++:"
							+ valueString);
				}
				if (keyString != null && keyString.contains("fieldType")) {
					tList.add(valueString);
					LogUtils.vLog(TAG, "--- AlertSheetShow fieldType ++:"
							+ valueString);
				}
			}
			if (!alertChoice.equals("")) {
				String[] temp = alertChoice.split("/&");
				if (temp.length <= 0) {
					msg.obj = "参数错误：alertChoice";
					msg.what = ConstantUtils.HANDLER_FAILE;
					handler.sendMessage(msg);
				} else {
					ArrayList<String> list_items = new ArrayList<String>();
					for (int i = 0; i < temp.length; i++) {
						list_items.add(temp[i]);
					}
					if (alertType.equals("0")) {
						/* 中间弹窗 */
						showCenterDialog(mActivity, mWebView, alertTitle,
								returnMethodName, list_items, addFieldNum,
								pList, fList, tList);
					} else if (alertType.equals("1")) {
						/* 底部弹窗 */
						showBottomDialog(mActivity, mWebView, alertTitle,
								returnMethodName, list_items, addFieldNum,
								pList, fList, tList);
					} else if (alertType.equals("2")) {
						/* 更新弹窗 */
						LogUtils.vLog(TAG, "--- AlertSheetShow App更新弹窗:"
								+ alertType);
						new_version = SharedUtils.getValue(mActivity,
								ConstantUtils.VERSION_NO);
						update_des = SharedUtils.getValue(mActivity,
								ConstantUtils.UPDATE_DES);
						is_must_app = SharedUtils.getValue(mActivity,
								ConstantUtils.UPDATE_FLAG);
						LogUtils.vLog(TAG,
								"--- AlertSheetShow App更新弹窗 new_version:"
										+ new_version);
						LogUtils.vLog(TAG,
								"--- AlertSheetShow App更新弹窗 update_des:"
										+ update_des);
						LogUtils.vLog(TAG,
								"--- AlertSheetShow App更新弹窗 is_must_app:"
										+ is_must_app);
						showUpdateDialog();
					} else if (alertType.equals("3")) {
						/* 打印机弹窗 */
						LogUtils.vLog(TAG, "--- AlertSheetShow 打印机弹窗:"
								+ alertType);
						showPrintDialog(mActivity);
					} else if (alertType.equals("4")) {
						/* 摄像头选择弹窗 */
						LogUtils.vLog(TAG, "--- AlertSheetShow 摄像头选择弹窗:"
								+ alertType);
						showCameraDialog(mActivity);
					}
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
	 * MD5封装加密
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
	public static void getParamWithJsonString(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " getParamWithJsonString seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		try {
			org.json.JSONObject o_json;
			o_json = new org.json.JSONObject(jsonString);
			String _sign = MD5Utils.getMD5(o_json, "0");
			/* 返回加密后结果 */
			JSONObject new_json = new JSONObject();
			new_json.put("str_md", _sign);
			/* 统一方法库回调 */
			webViewReturn(mWebView, returnMethodName, new_json);

		} catch (JSONException e) {
			_obj = e.getMessage();
			_what = ConstantUtils.HANDLER_FAILE;
			throw new RuntimeException(e);
		} catch (org.json.JSONException e) {
			_obj = e.getMessage();
			_what = ConstantUtils.HANDLER_FAILE;
			e.printStackTrace();
		}
		msg.obj = _obj;
		msg.what = _what;
		handler.sendMessage(msg);
	}

	/***
	 * 获取当前手机信息
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
	public static void getPhoneInfo(String jsonString, String returnMethodName,
			final BaseActivity mActivity, final X5WebView mWebView,
			View headerView, View footerView, final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " getPhoneInfo seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		try {
			/* 返回结果 */
			JSONObject new_json = new JSONObject();
			String userPhoneName = DeviceUtils.getPhoneName();
			new_json.put("userPhoneName", userPhoneName);
			String deviceName = DeviceUtils.getDeviceId(mActivity);
			new_json.put("deviceName", deviceName);
			String deviceId = DeviceUtils.getDeviceId(mActivity);
			new_json.put("deviceId", deviceId);
			String phoneVersion = DeviceUtils.getBuildVersion();
			new_json.put("phoneVersion", phoneVersion);
			String phoneModel = DeviceUtils.getPhoneModel();
			new_json.put("phoneModel", phoneModel);
			new_json.put("app_type", ConstantUtils.APP_TYPE);
			new_json.put("app_version", AppUtils.getVersion(mActivity));
			new_json.put("appCurVersionNum", AppUtils.getVersionCode(mActivity));
			String version_path = SharedUtils.getValue(mActivity,
					ConstantUtils.SERVICE_APP_VERSION_PATH_KEY);
			if (version_path.equals("")) {
				LogUtils.eLog("", "当前软件升级路径 normal version_path:"
						+ ConstantUtils.SERVICE_APP_VERSION_PATH);
				version_path = ConstantUtils.SERVICE_APP_VERSION_PATH + "";
			}
			new_json.put("appVersionType", version_path);
			new_json.put("app_name", AppUtils.getApplicationName(mActivity));
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

	/* 弹出选择 【底部】 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("InflateParams")
	public static void showBottomDialog(BaseActivity mActivity,
			final X5WebView mWebView, final String alertTitle,
			final String returnMethodName, ArrayList<String> list_items,
			final String addFieldNum, final ArrayList<String> pList,
			final ArrayList<String> fList, final ArrayList<String> tList) {

		/* 动态获取输入框的值 */
		final List<Map<String, Object>> inputList = new ArrayList<Map<String, Object>>();

		dialog = new Dialog(mActivity, R.style.Theme_Transparent);
		View v = mActivity.getLayoutInflater().inflate(
				R.layout.dialog_bottom_tip, null);
		LinearLayout ly = (LinearLayout) v.findViewById(R.id.content_lin);
		View v_top = mActivity.getLayoutInflater().inflate(
				R.layout.dialog_top_item, null);
		Button btn_top = (Button) v_top.findViewById(R.id.btn_top);
		View v_bottom = mActivity.getLayoutInflater().inflate(
				R.layout.dialog_bottom_item, null);
		Button btn_bottom = (Button) v_bottom.findViewById(R.id.btn_bottom);
		View v_center = mActivity.getLayoutInflater().inflate(
				R.layout.dialog_center_item, null);
		Button btn_center = (Button) v_center.findViewById(R.id.btn_center);
		TextView tv_tip = (TextView) v.findViewById(R.id.tv_tip);
		tv_tip.setText(alertTitle);

		/* 动态添加选项 */
		switch (list_items.size()) {
			case 1 :
				ly.addView(v_center);
				btn_center.setText(list_items.get(0));
				btn_center.setTag(0);
				btn_center.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						String _tag = v.getTag() + "";
						LogUtils.iLog(TAG, "btn_center onClick 点击跳转: " + _tag);
						/* 返回加密后结果 */
						JSONObject new_json = new JSONObject();
						new_json.put("alertChoice", _tag);
						/* 统一方法库回调 */
						webViewReturn(mWebView, returnMethodName, new_json);
					}
				});
				break;
			case 2 :
				ly.addView(v_top);
				ly.addView(v_bottom);
				btn_top.setText(list_items.get(0));
				btn_top.setTag(0);
				btn_bottom.setText(list_items.get(1));
				btn_bottom.setTag(1);
				btn_top.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						String _tag = v.getTag() + "";
						LogUtils.iLog(TAG, "btn_top onClick 点击跳转: " + _tag);
						/* 返回加密后结果 */
						JSONObject new_json = new JSONObject();
						new_json.put("alertChoice", _tag);
						/* 统一方法库回调 */
						webViewReturn(mWebView, returnMethodName, new_json);
					}
				});
				btn_bottom.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						String _tag = v.getTag() + "";
						LogUtils.iLog(TAG, "btn_bottom onClick 点击跳转: " + _tag);
						/* 返回加密后结果 */
						JSONObject new_json = new JSONObject();
						new_json.put("alertChoice", _tag);
						/* 统一方法库回调 */
						webViewReturn(mWebView, returnMethodName, new_json);
					}
				});
				break;
			default :
				for (int i = 0; i < list_items.size(); i++) {
					if (i == 0) {
						/* 添加第一项 */
						ly.addView(v_top);
						btn_top.setText(list_items.get(0));
						btn_top.setTag(0);
						btn_top.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								String _tag = v.getTag() + "";
								LogUtils.iLog(TAG, "btn_top onClick 点击跳转: "
										+ _tag);
								/* 返回加密后结果 */
								JSONObject new_json = new JSONObject();
								new_json.put("alertChoice", _tag);
								/* 统一方法库回调 */
								webViewReturn(mWebView, returnMethodName,
										new_json);
							}
						});
					} else {
						if (i == list_items.size() - 1) {
							/* 添加最后一项 */
							ly.addView(v_bottom);
							btn_bottom
									.setText(list_items.get(list_items.size() - 1));
							btn_bottom.setTag(i);
							btn_bottom
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											dialog.dismiss();
											String _tag = v.getTag() + "";
											LogUtils.iLog(TAG,
													"btn_bottom onClick 点击跳转: "
															+ _tag);
											/* 返回加密后结果 */
											JSONObject new_json = new JSONObject();
											new_json.put("alertChoice", _tag);
											/* 统一方法库回调 */
											webViewReturn(mWebView,
													returnMethodName, new_json);
										}
									});
						} else {
							ly.addView(v_center);
							btn_center.setText(list_items.get(i));
							btn_center.setTag(i);
							btn_center
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											dialog.dismiss();
											String _tag = v.getTag() + "";
											LogUtils.iLog(TAG,
													"btn_center onClick 点击跳转: "
															+ _tag);
											/* 返回加密后结果 */
											JSONObject new_json = new JSONObject();
											new_json.put("alertChoice", _tag);
											/* 统一方法库回调 */
											webViewReturn(mWebView,
													returnMethodName, new_json);
										}
									});
						}
					}
				}
				break;
		}
		/* 文本输入框 */
		LinearLayout cen = (LinearLayout) v.findViewById(R.id.content_cen);
		if (addFieldNum != null && !addFieldNum.equals("0")) {
			int count = Integer.parseInt(addFieldNum);
			if (count == pList.size() && count == fList.size()) {
				for (int j = 0; j < count; j++) {
					Map<String, Object> map = new HashMap<String, Object>();
					View v_input = mActivity.getLayoutInflater().inflate(
							R.layout.dialog_input_item, null);
					EditText et_value = (EditText) v_input
							.findViewById(R.id.et_value);
					int _type = InputType.TYPE_NULL;
					switch (Integer.parseInt(tList.get(j))) {
						case 0 :
							LogUtils.iLog(TAG, "showCenterDialog 文本");
							_type = InputType.TYPE_CLASS_TEXT;
							break;
						case 1 :
							LogUtils.iLog(TAG, "showCenterDialog 金额数字");
							_type = (InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
							break;
						case 2 :
							LogUtils.iLog(TAG, "showCenterDialog 密码");
							_type = (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
							break;
						case 3 :
							LogUtils.iLog(TAG, "showCenterDialog ASCII");
							_type = InputType.TYPE_CLASS_TEXT;
							final String FILTER_ASCII = "\\A\\p{ASCII}*\\z";
							et_value.setFilters(new InputFilter[]{new InputFilter() {
								@Override
								public CharSequence filter(CharSequence source,
										int start, int end, Spanned dest,
										int dstart, int dend) {
									if (!(source + "").matches(FILTER_ASCII)) {
										return "";
									}

									return null;
								}
							}});
							break;
						case 4 :
							LogUtils.iLog(TAG, "showCenterDialog 电话");
							_type = InputType.TYPE_CLASS_PHONE;
							break;
						case 5 :
							LogUtils.iLog(TAG, "showCenterDialog 电子邮件");
							_type = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
							break;
						case 6 :
							LogUtils.iLog(TAG, "showCenterDialog url字符");
							_type = InputType.TYPE_TEXT_VARIATION_URI;
							break;
						default :
							break;
					}
					et_value.setInputType(_type);
					map.put("value", et_value);
					inputList.add(map);

					if ("".equals(fList.get(j))) {
						et_value.setHint(pList.get(j));
					} else {
						et_value.setText(fList.get(j));
					}
					cen.addView(v_input);
				}
			} else {
				LogUtils.eLog(TAG,
						"showCenterDialog dialog: show faile, input size");
			}
		}

		dialog.setContentView(v);
		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow().setWindowAnimations(R.style.anim_dialog);
		dialog.getWindow().setGravity(Gravity.BOTTOM);
		dialog.getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		dialog.show();
	}

	/* 弹出选择 【中间】 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("InflateParams")
	public static void showCenterDialog(BaseActivity mActivity,
			final X5WebView mWebView, final String alertTitle,
			final String returnMethodName, ArrayList<String> list_items,
			final String addFieldNum, final ArrayList<String> pList,
			final ArrayList<String> fList, final ArrayList<String> tList) {

		/* 动态获取输入框的值 */
		final List<Map<String, Object>> inputList = new ArrayList<Map<String, Object>>();

		final Dialog dialog = new Dialog(mActivity, R.style.Theme_Transparent);
		View v = mActivity.getLayoutInflater().inflate(
				R.layout.dialog_custom_tip, null);
		dialog.setContentView(v);
		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		TextView tv_tip = (TextView) v.findViewById(R.id.tv_tip);
		tv_tip.setText(alertTitle);

		LinearLayout ly = (LinearLayout) v.findViewById(R.id.content_lin);
		View v_top = mActivity.getLayoutInflater().inflate(
				R.layout.dialog_top_item, null);
		Button btn_top = (Button) v_top.findViewById(R.id.btn_top);
		View v_bottom = mActivity.getLayoutInflater().inflate(
				R.layout.dialog_bottom_item, null);
		Button btn_bottom = (Button) v_bottom.findViewById(R.id.btn_bottom);
		View v_center = mActivity.getLayoutInflater().inflate(
				R.layout.dialog_center_item, null);
		Button btn_center = (Button) v_center.findViewById(R.id.btn_center);
		/* 动态添加选项 */
		switch (list_items.size()) {
			case 1 :
				ly.addView(v_center);
				btn_center.setText(list_items.get(0));
				btn_center.setTag(0);
				btn_center.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						String _tag = v.getTag() + "";
						LogUtils.iLog(TAG, "btn_center onClick 点击跳转: " + _tag);
						/* 返回加密后结果 */
						JSONObject new_json = new JSONObject();
						new_json.put("alertChoice", _tag);
						for (int i = 0; i < inputList.size(); i++) {
							String value = ((EditText) inputList.get(i).get(
									"value")).getText().toString();
							new_json.put("fieldValue" + i, value);
						}
						/* 统一方法库回调 */
						webViewReturn(mWebView, returnMethodName, new_json);
					}
				});
				break;
			case 2 :
				ly.addView(v_top);
				ly.addView(v_bottom);
				btn_top.setText(list_items.get(0));
				btn_top.setTag(0);
				btn_bottom.setText(list_items.get(1));
				btn_bottom.setTag(1);
				btn_top.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						String _tag = v.getTag() + "";
						LogUtils.iLog(TAG, "btn_top onClick 点击跳转: " + _tag);
						/* 返回加密后结果 */
						JSONObject new_json = new JSONObject();
						new_json.put("alertChoice", _tag);
						for (int i = 0; i < inputList.size(); i++) {
							String value = ((EditText) inputList.get(i).get(
									"value")).getText().toString();
							new_json.put("fieldValue" + i, value);
						}
						/* 统一方法库回调 */
						webViewReturn(mWebView, returnMethodName, new_json);
					}
				});
				btn_bottom.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						String _tag = v.getTag() + "";
						LogUtils.iLog(TAG, "btn_bottom onClick 点击跳转: " + _tag);
						/* 返回加密后结果 */
						JSONObject new_json = new JSONObject();
						new_json.put("alertChoice", _tag);
						for (int i = 0; i < inputList.size(); i++) {
							String value = ((EditText) inputList.get(i).get(
									"value")).getText().toString();
							new_json.put("fieldValue" + i, value);
						}
						/* 统一方法库回调 */
						webViewReturn(mWebView, returnMethodName, new_json);
					}
				});
				break;
			default :
				for (int i = 0; i < list_items.size(); i++) {
					if (i == 0) {
						/* 添加第一项 */
						ly.addView(v_top);
						btn_top.setText(list_items.get(0));
						btn_top.setTag(0);
						btn_top.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								String _tag = v.getTag() + "";
								LogUtils.iLog(TAG, "btn_top onClick 点击跳转: "
										+ _tag);
								/* 返回加密后结果 */
								JSONObject new_json = new JSONObject();
								new_json.put("alertChoice", _tag);
								for (int i = 0; i < inputList.size(); i++) {
									String value = ((EditText) inputList.get(i)
											.get("value")).getText().toString();
									new_json.put("fieldValue" + i, value);
								}
								/* 统一方法库回调 */
								webViewReturn(mWebView, returnMethodName,
										new_json);
							}
						});
					} else {
						if (i == list_items.size() - 1) {
							/* 添加最后一项 */
							ly.addView(v_bottom);
							btn_bottom
									.setText(list_items.get(list_items.size() - 1));
							btn_bottom.setTag(i);
							btn_bottom
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											dialog.dismiss();
											String _tag = v.getTag() + "";
											LogUtils.iLog(TAG,
													"btn_bottom onClick 点击跳转: "
															+ _tag);
											/* 返回加密后结果 */
											JSONObject new_json = new JSONObject();
											new_json.put("alertChoice", _tag);
											for (int i = 0; i < inputList
													.size(); i++) {
												String value = ((EditText) inputList
														.get(i).get("value"))
														.getText().toString();
												new_json.put("fieldValue" + i,
														value);
											}
											/* 统一方法库回调 */
											webViewReturn(mWebView,
													returnMethodName, new_json);
										}
									});
						} else {
							ly.addView(v_center);
							btn_center.setText(list_items.get(i));
							btn_center.setTag(i);
							btn_center
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											dialog.dismiss();
											String _tag = v.getTag() + "";
											LogUtils.iLog(TAG,
													"btn_center onClick 点击跳转: "
															+ _tag);
											/* 返回加密后结果 */
											JSONObject new_json = new JSONObject();
											new_json.put("alertChoice", _tag);
											for (int i = 0; i < inputList
													.size(); i++) {
												String value = ((EditText) inputList
														.get(i).get("value"))
														.getText().toString();
												new_json.put("fieldValue" + i,
														value);
											}
											/* 统一方法库回调 */
											webViewReturn(mWebView,
													returnMethodName, new_json);
										}
									});
						}
					}
				}
				break;
		}

		/* 文本输入框 */
		LinearLayout cen = (LinearLayout) v.findViewById(R.id.content_cen);
		if (addFieldNum != null && !addFieldNum.equals("0")) {
			int count = Integer.parseInt(addFieldNum);
			if (count == pList.size() && count == fList.size()
					&& count == tList.size()) {
				for (int j = 0; j < count; j++) {
					Map<String, Object> map = new HashMap<String, Object>();
					View v_input = mActivity.getLayoutInflater().inflate(
							R.layout.dialog_input_item, null);
					EditText et_value = (EditText) v_input
							.findViewById(R.id.et_value);
					int _type = InputType.TYPE_NULL;
					switch (Integer.parseInt(tList.get(j))) {
						case 0 :
							LogUtils.iLog(TAG, "showCenterDialog 文本");
							_type = InputType.TYPE_CLASS_TEXT;
							break;
						case 1 :
							LogUtils.iLog(TAG, "showCenterDialog 金额数字");
							_type = (InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
							break;
						case 2 :
							LogUtils.iLog(TAG, "showCenterDialog 密码");
							_type = (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
							break;
						case 3 :
							LogUtils.iLog(TAG, "showCenterDialog ASCII");
							_type = InputType.TYPE_CLASS_TEXT;
							final String FILTER_ASCII = "\\A\\p{ASCII}*\\z";
							et_value.setFilters(new InputFilter[]{new InputFilter() {
								@Override
								public CharSequence filter(CharSequence source,
										int start, int end, Spanned dest,
										int dstart, int dend) {
									if (!(source + "").matches(FILTER_ASCII)) {
										return "";
									}

									return null;
								}
							}});
							break;
						case 4 :
							LogUtils.iLog(TAG, "showCenterDialog 电话");
							_type = InputType.TYPE_CLASS_PHONE;
							break;
						case 5 :
							LogUtils.iLog(TAG, "showCenterDialog 电子邮件");
							_type = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
							break;
						case 6 :
							LogUtils.iLog(TAG, "showCenterDialog url字符");
							_type = InputType.TYPE_TEXT_VARIATION_URI;
							break;
						default :
							break;
					}
					et_value.setInputType(_type);
					map.put("value", et_value);
					inputList.add(map);

					if ("".equals(fList.get(j))) {
						et_value.setHint(pList.get(j));
					} else {
						et_value.setText(fList.get(j));
					}
					cen.addView(v_input);
				}
			} else {
				LogUtils.eLog(TAG,
						"showCenterDialog dialog: show faile, input size");
			}
		}
		dialog.show();
	}
	/***
	 * json数组排序
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
	public static void dictComparator(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " dictComparator seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		try {
			Map<String, String> mapValues = new HashMap<String, String>();
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				mapValues.put(keyString, valueString);
			}
			// 这里将map.entrySet()转换成list
			List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(
					mapValues.entrySet());
			// 然后通过比较器来实现排序
			Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
				// 升序排序
				@Override
				public int compare(Entry<String, String> o1,
						Entry<String, String> o2) {
					return o1.getValue().compareTo(o2.getValue());
				}

			});
			JSONObject jObject = new JSONObject();
			for (Map.Entry<String, String> mapping : list) {
				String _key = mapping.getKey();
				String _value = mapping.getValue();
				jObject.put(_key, _value);
				LogUtils.iLog(TAG, "key: " + _key + "; value:" + _value);
			}
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
	public static void getScanCode(String jsonString, String returnMethodName,
			final BaseActivity mActivity, final X5WebView mWebView,
			View headerView, View footerView, final Handler handler) {

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

	/***
	 * 保存图片到本地存储
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return base64 image
	 */
	public static void saveimageTosandboxWithimage(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " saveimageTosandboxWithimage seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		String imageBase64 = "";
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && keyString.equals("imageBase64")) {
					imageBase64 = valueString;
					LogUtils.vLog(TAG,
							"--- saveimageTosandboxWithimage imageBase64:"
									+ imageBase64);
				}
			}
			if (!imageBase64.equals("")) {
				Bitmap mBitmap = ImageUtils.base64ToBitmap(imageBase64);
				if (mBitmap != null) {
					String save_path = FileUtils.getStoragePath(mActivity,
							false) + "/" + ConstantUtils.UPLOAD_PATH;
					LogUtils.vLog(TAG, "saveimageTosandboxWithimage save_path:"
							+ save_path);
					String name = DateUtils.getNowTime() + ".jpg";
					File file = new File(save_path);
					if (!file.exists()) {
						file.mkdirs();
					}
					String _path = save_path + name;
					LogUtils.vLog(TAG, "saveimageTosandboxWithimage--path: "
							+ _path);
					boolean isOK = ImageUtils
							.saveBitmapToSDCard(mBitmap, _path);
					if (isOK) {
						JSONObject jObject = new JSONObject();
						jObject.put("localPaths", _path);
						/* 统一方法库回调 */
						webViewReturn(mWebView, returnMethodName, jObject);

					} else {
						_obj = mActivity.getResources().getString(
								R.string.save_base_img_faile3);
						_what = ConstantUtils.HANDLER_FAILE;
					}

				} else {
					_obj = mActivity.getResources().getString(
							R.string.save_base_img_faile2);
					_what = ConstantUtils.HANDLER_FAILE;
				}
			} else {
				_obj = mActivity.getResources().getString(
						R.string.save_base_img_faile1);
				_what = ConstantUtils.HANDLER_FAILE;
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

	/* ================================= 更新弹窗 ========================= */
	private static void showUpdateDialog() {
		dialog = new Dialog(activity, R.style.Theme_Transparent);
		LayoutInflater inflater = activity.getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_update_tip, null);
		dialog.setContentView(view);
		t_title = (TextView) view.findViewById(R.id.title);
		t_message = (TextView) view.findViewById(R.id.message);
		t_message.setText(update_des);
		b_ok = (Button) view.findViewById(R.id.b_ok);
		b_cancel = (Button) view.findViewById(R.id.b_cancel);
		if (is_must_app.equals("1")) {
			dialog.setCancelable(false);
		}
		seekbar = (ColorfulProgressBar) view.findViewById(R.id.seekbar);
		seekbar.setMax(100);
		t_title.setText(activity.getString(R.string.app_update_tip) + ":"
				+ new_version);
		b_ok.setText(activity.getString(R.string.app_update_ok));
		b_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/* 新版本下载 */
				update_index = 0;
				ToastShow.showShort(activity,
						activity.getString(R.string.server_down_now));
				String version_path = SharedUtils.getValue(activity,
						ConstantUtils.SERVICE_APP_VERSION_PATH_KEY);
				if (version_path.equals("")) {
					LogUtils.eLog("", "当前软件升级路径 normal version_path:"
							+ ConstantUtils.SERVICE_APP_VERSION_PATH);
					version_path = ConstantUtils.SERVICE_APP_VERSION_PATH + "";
				}
				String downurl = ConstantUtils.U_DOWN + version_path + "/"
						+ ConstantUtils.U_SOFT_NAME;
				LogUtils.dLog(TAG, "app downurl:" + downurl);
				seekbar.setVisibility(View.VISIBLE);
				t_message.setVisibility(View.GONE);
				t_title.setText(activity.getString(R.string.tip_down_now));
				b_cancel.setVisibility(View.GONE);
				b_ok.setVisibility(View.GONE);
				dialog.setCancelable(false);
				// /传入参数：Context对象，下载地址, 文件保存路径；
				app_save_path = FileUtils.getAPP_PATH(activity);
				File f = new File(app_save_path);
				if (!f.exists()) {
					f.mkdirs();
				}
				DownloadTask.to(downurl, app_save_path + "/"
						+ ConstantUtils.U_SOFT_NAME, down_handler);
			}
		});
		b_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (is_must_app.equals("1")) {
					activity.finish();
				} else {
					/* 不更新 */
					dialog.dismiss();
				}
			}
		});
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	/* 下载通知 */
	static Handler down_handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			final int _process = msg.arg1;
			// 更改页面
			switch (msg.what) {
				case 3 :
					new Thread(new Runnable() {
						@Override
						public void run() {
							activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									/* 正在解压 */
									t_message.setText(activity
											.getString(R.string.server_unzip_now)
											+ msg.arg1);
								}
							});
						}
					}).start();

					break;
				case 4 :
					new Thread(new Runnable() {
						@Override
						public void run() {
							activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {

									/* 解压完成 */
									dialog.dismiss();
									ToastShow.showShort(
											activity,
											activity.getString(R.string.tip_update_seccess));
								}
							});
						}
					}).start();
					/* updateDB(); */
					break;
				case ConstantUtils.U_DOWN_PROCESS :
					new Thread(new Runnable() {
						@Override
						public void run() {
							activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									LogUtils.eLog(TAG, "refresh 下载进度："
											+ _process);
									if (update_index == 0) {
										seekbar.setProgress(_process);
									} else {
										t_message.setText(activity
												.getString(R.string.rar_update_info)
												+ _process + "%");
									}
								}
							});
						}
					}).start();

					break;
				case ConstantUtils.U_DOWN_COM :
					new Thread(new Runnable() {
						@Override
						public void run() {
							activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {

									if (update_index == 0) {
										dialog.dismiss();
										LogUtils.dLog(TAG, "下载成功 install:"
												+ app_save_path + "/"
												+ ConstantUtils.U_SOFT_NAME);
										/* updateDB(); */
										openFile();
									} else {
										// 解压文件
										LogUtils.dLog(TAG, "下载完成，正在解压！");
										String mPath = FileUtils
												.getU_PATH(activity);
										LogUtils.iLog(TAG, "解压目录： " + mPath
												+ ConstantUtils.U_RES_NAME);
										ZipExtractorUtils task = new ZipExtractorUtils(
												mPath
														+ ConstantUtils.U_RES_NAME,
												mPath, activity, true,
												down_handler);
										task.execute();
									}
								}
							});
						}
					}).start();

					break;
				case ConstantUtils.U_DOWN_ERR :
					new Thread(new Runnable() {
						@Override
						public void run() {
							activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {

									// 下载失败
									dialog.dismiss();
									ToastShow.showShort(activity, activity
											.getString(R.string.tip_down_faie));
								}
							});
						}
					}).start();
					break;
				default :
					break;
			}
		}
	};

	@SuppressWarnings("unused")
	private void updateDB() {
		// 实例化一个容器，用来拼接sql语句
		StringBuffer sBuffer = new StringBuffer();
		// sql语句，第一个字段为_ID 主键自增，这是通用的，所以直接写死
		sBuffer.append("update " + ConstantUtils.HTML_UPDATE_TABLE_NAME
				+ " set " + ConstantUtils.HTML_UPDATE_KEY + "=0" + " where "
				+ ConstantUtils.HTML_UPDATE_NAME + "='"
				+ ConstantUtils.U_SOFT_NAME + "'");
		LogUtils.vLog(TAG,
				"--- db changedIsUpdate execSQLStr:" + sBuffer.toString());
		/* 查询表中指定数据 */
		SQLiteDatabase db = DBHelper.openDatabase();
		/* 获取表中所有字段 */
		db.execSQL(sBuffer.toString());
		db.close();
	}

	// install apk
	private static void openFile() {
		// 安装应用
		File file_dir = new File(app_save_path);
		File file = new File(app_save_path + "/" + ConstantUtils.U_SOFT_NAME);
		LogUtils.eLog(TAG, "OpenFile" + file.getName());
		String chmodCmd = "chmod 777 " + file.getAbsolutePath();
		String cho_dir = "chmod 777 " + file_dir.getAbsolutePath();
		try {
			Runtime.getRuntime().exec(cho_dir);
			Runtime.getRuntime().exec(chmodCmd);
		} catch (Exception e) {
			LogUtils.eLog(TAG, "error open");
		}
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		activity.startActivity(intent);
	}

	/***
	 * 获取设备唯一标识
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
	public static void getUUID(String jsonString, String returnMethodName,
			final BaseActivity mActivity, final X5WebView mWebView,
			View headerView, View footerView, final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " getUUID seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		try {
			JSONObject jObject = new JSONObject();
			String _uuid = DeviceUtils.getDeviceId(mActivity);
			jObject.put("uuid", _uuid);
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

	/* ================================= 打印机设置弹窗 ========================= */
	private static List<PRINT_ITEMS> userList;
	private static PrintListAdapter pAdapter;
	private static CheckBox cb_connect;
	private static Spinner spinner;
	private static void showPrintDialog(BaseActivity mActivity) {
		BluetoothAdapter btAdapter = BluetoothUtil.getBTAdapter();
		if (btAdapter != null) {
			if (!btAdapter.isEnabled()) {
				showPrintError(mActivity);
			} else {
				userList = new ArrayList<PRINT_ITEMS>();
				// 获取所有已经绑定的蓝牙设备
				Set<BluetoothDevice> devices = btAdapter.getBondedDevices();
				if (devices.size() > 0) {
					String save_pname = SharedUtils.getValue(mActivity,
							PrintUtil.KEY_PRINT_NAME);
					String save_padd = SharedUtils.getValue(mActivity,
							PrintUtil.KEY_PRINT_ADD);
					for (BluetoothDevice bluetoothDevice : devices) {
						PRINT_ITEMS PI = new PRINT_ITEMS();
						PI.p_add = bluetoothDevice.getAddress();
						PI.p_name = bluetoothDevice.getName();
						if (save_pname.equals(bluetoothDevice.getName())
								&& save_padd.equals(bluetoothDevice
										.getAddress())) {
							PI.isCheck = true;
						} else {
							PI.isCheck = false;
						}
						userList.add(PI);
					}
				}
				showPrintList(mActivity);
			}
		} else {
			ToastShow.showShort(mActivity,
					mActivity.getString(R.string.print_bt_notsuppest));
		}
	}

	private static void showPrintError(BaseActivity mActivity) {
		final Dialog dialog = new Dialog(mActivity, R.style.Theme_Transparent);
		View v = mActivity.getLayoutInflater().inflate(
				R.layout.dialog_simple_tip, null);
		TextView tv_tip = (TextView) v.findViewById(R.id.tv_tip);
		tv_tip.setText(mActivity.getString(R.string.print_bt_notopen));
		dialog.setContentView(v);
		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		v.findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private static void showPrintList(final BaseActivity mActivity) {
		final Dialog dialog = new Dialog(mActivity, R.style.Theme_Transparent);
		View v = mActivity.getLayoutInflater().inflate(
				R.layout.dialog_printlist_tip, null);
		TextView tv_tip = (TextView) v.findViewById(R.id.tv_tip_title);
		tv_tip.setText(mActivity.getString(R.string.print_bt_list));
		ListView print_list = (ListView) v.findViewById(R.id.print_list);
		pAdapter = new PrintListAdapter(mActivity, userList);
		print_list.setAdapter(pAdapter);
		print_list
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						// 重置，确保最多只有一项被选中
						for (int i = 0; i < userList.size(); i++) {
							if (i == position) {
								userList.get(i).isCheck = true;
								SharedUtils.setValue(mActivity,
										PrintUtil.KEY_PRINT_NAME,
										userList.get(i).p_name);
								SharedUtils.setValue(mActivity,
										PrintUtil.KEY_PRINT_ADD,
										userList.get(i).p_add);
							} else {
								userList.get(i).isCheck = false;
							}

						}
						pAdapter.notifyDataSetChanged();
					}
				});
		int sel_page = SharedUtils.getInt(mActivity, PrintUtil.KEY_PRINT_COUNT);
		String isAutoPrint = SharedUtils.getValue(mActivity,
				PrintUtil.KEY_IS_AUTU_PRINT);
		cb_connect = (CheckBox) v.findViewById(R.id.cb_connect);
		if ("".equals(isAutoPrint) || "1".equals(isAutoPrint)) {
			cb_connect.setChecked(true);
		} else {
			cb_connect.setChecked(false);
		}

		spinner = (Spinner) v.findViewById(R.id.spinner1);
		spinner.setSelection((sel_page - 1));
		spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				SharedUtils.setInt(mActivity, PrintUtil.KEY_PRINT_COUNT,
						(arg2 + 1));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		dialog.setContentView(v);
		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		v.findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				String _saveAutoPrint = "0";
				if (cb_connect.isChecked()) {
					_saveAutoPrint = "1";
				}
				SharedUtils.setValue(mActivity, PrintUtil.KEY_IS_AUTU_PRINT,
						_saveAutoPrint);
			}
		});
		dialog.show();
	}

	/* ================================= 摄像头设置弹窗 ========================= */
	private static List<PRINT_ITEMS> cameraList;
	private static CameraListAdapter cAdapter;
	private static Camera.CameraInfo cameraInfo = null;
	private static String save_camera;
	private static void showCameraDialog(BaseActivity mActivity) {
		cameraList = new ArrayList<PRINT_ITEMS>();
		save_camera = SharedUtils.getValue(mActivity, PrintUtil.KEY_IS_CAMERA);
		int cameracount = Camera.getNumberOfCameras();
		cameraInfo = new Camera.CameraInfo();
		for (int i = 0; i < cameracount; i++) {
			Camera.getCameraInfo(i, cameraInfo);
			PRINT_ITEMS PI = new PRINT_ITEMS();
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {

				PI.p_name = "前置摄像头";
				if (save_camera.equals("前置摄像头")) {
					PI.isCheck = true;
					PI.p_add = "已选择√";
				} else {
					PI.isCheck = false;
				}
			} else if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
				PI.p_name = "后置摄像头";
				if (save_camera.equals("后置摄像头")) {
					PI.isCheck = true;
					PI.p_add = "已选择√";
				} else {
					PI.isCheck = false;
				}
			}
			cameraList.add(PI);
		}
		showCameraList(mActivity);
	}

	private static void showCameraList(final BaseActivity mActivity) {
		final Dialog dialog = new Dialog(mActivity, R.style.Theme_Transparent);
		View v = mActivity.getLayoutInflater().inflate(
				R.layout.dialog_cameralist_tip, null);
		TextView tv_tip = (TextView) v.findViewById(R.id.tv_tip_title);
		tv_tip.setText(mActivity.getString(R.string.camera_bt_list));
		ListView camera_list = (ListView) v.findViewById(R.id.camera_list);
		cAdapter = new CameraListAdapter(mActivity, cameraList);
		camera_list.setAdapter(cAdapter);
		camera_list
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						// 重置，确保最多只有一项被选中
						save_camera = cameraList.get(position).p_name + "";
						SharedUtils.setValue(mActivity,
								PrintUtil.KEY_IS_CAMERA, save_camera);
						dialog.dismiss();
					}
				});

		dialog.setContentView(v);
		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		dialog.show();
	}
	
	/***
	 * 在app外打开链接
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
	public static void openUrl(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " openUrl seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		String openUrl = "";
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && keyString.equals("openUrl")) {
					openUrl = valueString;
					LogUtils.vLog(TAG, "--- openUrl name:" + openUrl);
				}
			}
			if (!openUrl.equals("")) {
				/* 打开url */
				Intent intent = new Intent();        
		        intent.setAction("android.intent.action.VIEW");    
		        Uri content_url = Uri.parse(openUrl);   
		        intent.setData(content_url);  
		        mActivity.startActivity(intent);
				
			}else{
				ToastShow.showShort(mActivity, "链接不能为空");
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
