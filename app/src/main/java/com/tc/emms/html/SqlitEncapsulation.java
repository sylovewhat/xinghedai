package com.tc.emms.html;

import java.util.HashMap;
import java.util.Map;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.db.DBHelper;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.widget.X5WebView;

/*
 * 数据库功能库方法 
 */

public class SqlitEncapsulation {
	private static String TAG = "SqlitEncapsulation";
	private static DBHelper dbHelper;

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

	private static void webViewReturn(final X5WebView mWebView,
			final String returnMethodName, final JSONArray jObject) {
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
	 * 创建表
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return handler
	 */
	public static void creadSqlTable(String jsonString,
			String returnMethodName, BaseActivity mActivity,
			X5WebView mWebView, View headerView, View footerView,
			Handler handler) {
		Message msg = new Message();
		String _obj = TAG + " creadSqlTable seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;

		String tabName = "";
		Map<String, String> mapValues = new HashMap<String, String>();
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && keyString.equals("sqlTableName")) {
					tabName = valueString;
					LogUtils.vLog(TAG, "--- add creadSqlTable name:" + valueString);
				} else {
					mapValues.put(keyString, valueString);
					LogUtils.vLog(TAG, "--- add creadSqlTable values:" + keyString);
				}
			}
			if (dbHelper == null) {
				dbHelper = new DBHelper(mActivity);
			}
			boolean seccess = dbHelper.creadSqlTable(tabName, mapValues);
			if (seccess) {
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
	 * 删除表
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return handler
	 */
	public static void deleteTable(String jsonString, String returnMethodName,
			BaseActivity mActivity, X5WebView mWebView, View headerView,
			View footerView, Handler handler) {
		Message msg = new Message();
		String _obj = TAG + " deleteTable seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;

		String tabName = "";
		Map<String, String> mapValues = new HashMap<String, String>();
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && keyString.equals("sqlTableName")) {
					tabName = valueString;
					LogUtils.vLog(TAG, "--- add deleteTable name:" + valueString);
				} else {
					mapValues.put(keyString, valueString);
					LogUtils.vLog(TAG, "--- add deleteTable values:" + keyString);
				}
			}
			if (dbHelper == null) {
				dbHelper = new DBHelper(mActivity);
			}
			boolean seccess = dbHelper.deleteTable(tabName, mapValues);
			if (seccess) {
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
	 * 插入数据
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return handler
	 */
	public static void saveCell(String jsonString, String returnMethodName,
			BaseActivity mActivity, X5WebView mWebView, View headerView,
			View footerView, Handler handler) {
		Message msg = new Message();
		String _obj = TAG + " saveCell seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;

		String tabName = "";
		Map<String, String> mapValues = new HashMap<String, String>();
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && keyString.equals("sqlTableName")) {
					tabName = valueString;
					LogUtils.vLog(TAG, "--- add saveCell name:" + valueString);
				} else {
					mapValues.put(keyString, valueString);
					LogUtils.vLog(TAG, "--- add saveCell values:" + keyString);
				}
			}
			if (dbHelper == null) {
				dbHelper = new DBHelper(mActivity);
			}
			boolean seccess = dbHelper.saveCell(tabName, mapValues);
			if (seccess) {
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
	 * 删除数据【单条】
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return handler
	 */
	public static void deleteCell(String jsonString, String returnMethodName,
			BaseActivity mActivity, X5WebView mWebView, View headerView,
			View footerView, Handler handler) {
		Message msg = new Message();
		String _obj = TAG + " deleteCell seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;

		String tabName = "";
		Map<String, String> mapValues = new HashMap<String, String>();
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && keyString.equals("sqlTableName")) {
					tabName = valueString;
					LogUtils.vLog(TAG, "--- add deleteCell name:" + valueString);
				} else {
					mapValues.put(keyString, valueString);
					LogUtils.vLog(TAG, "--- add deleteCell values:" + keyString);
				}
			}
			if (dbHelper == null) {
				dbHelper = new DBHelper(mActivity);
			}
			boolean seccess = dbHelper.deleteCell(tabName, mapValues);
			if (seccess) {
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
	 * 删除数据【所有数据】
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return handler
	 */
	public static void deleteAll(String jsonString, String returnMethodName,
			BaseActivity mActivity, X5WebView mWebView, View headerView,
			View footerView, Handler handler) {
		Message msg = new Message();
		String _obj = TAG + " deleteAll seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;

		String tabName = "";
		Map<String, String> mapValues = new HashMap<String, String>();
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && keyString.equals("sqlTableName")) {
					tabName = valueString;
					LogUtils.vLog(TAG, "--- add deleteAll name:" + valueString);
				} else {
					mapValues.put(keyString, valueString);
					LogUtils.vLog(TAG, "--- add deleteAll values:" + keyString);
				}
			}
			if (dbHelper == null) {
				dbHelper = new DBHelper(mActivity);
			}
			boolean seccess = dbHelper.deleteAll(tabName, mapValues);
			if (seccess) {
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
	 * 修改数据【单条】
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return handler
	 */
	public static void updataCell(String jsonString, String returnMethodName,
			BaseActivity mActivity, X5WebView mWebView, View headerView,
			View footerView, Handler handler) {
		Message msg = new Message();
		String _obj = TAG + " updataCell seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;

		String tabName = "";
		String sql = "";
		Map<String, String> mapValues = new HashMap<String, String>();
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && keyString.equals("sqlTableName")) {
					tabName = valueString;
					LogUtils.vLog(TAG, "--- add updataCell name:" + valueString);
				} else {
					if (keyString != null && keyString.equals("sql")) {
						sql = valueString;
						LogUtils.vLog(TAG, "--- add updataCell sql:" + valueString);
					} else {
						mapValues.put(keyString, valueString);
						LogUtils.vLog(TAG, "--- add updataCell values:" + keyString);
					}
				}
			}
			if (dbHelper == null) {
				dbHelper = new DBHelper(mActivity);
			}
			boolean seccess = dbHelper.updataCell(tabName, mapValues, sql);
			if (seccess) {
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
	 * 修改数据【所有数据】
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return handler
	 */
	public static void updateAll(String jsonString, String returnMethodName,
			BaseActivity mActivity, X5WebView mWebView, View headerView,
			View footerView, Handler handler) {
		Message msg = new Message();
		String _obj = TAG + " updateAll seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;

		String tabName = "";
		Map<String, String> mapValues = new HashMap<String, String>();
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && keyString.equals("sqlTableName")) {
					tabName = valueString;
					LogUtils.vLog(TAG, "--- add updateAll name:" + valueString);
				} else {
					mapValues.put(keyString, valueString);
					LogUtils.vLog(TAG, "--- add updateAll values:" + keyString);
				}
			}
			if (dbHelper == null) {
				dbHelper = new DBHelper(mActivity);
			}
			boolean seccess = dbHelper.updateAll(tabName, mapValues);
			if (seccess) {
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
	 * 查询数据【单条】
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return handler
	 */
	public static void selectCell(String jsonString, String returnMethodName,
			BaseActivity mActivity, X5WebView mWebView, View headerView,
			View footerView, Handler handler) {
		Message msg = new Message();
		String _obj = TAG + " selectCell seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;

		String tabName = "";
		String tabSql = "";
		Map<String, String> mapValues = new HashMap<String, String>();
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && keyString.equals("sqlTableName")) {
					tabName = valueString;
					LogUtils.vLog(TAG, "--- add selectCell name:" + valueString);
				} else if (keyString.equals("sql")) {
					tabSql = valueString;
				} else {
					mapValues.put(keyString, valueString);
					LogUtils.vLog(TAG, "--- add selectCell values:" + keyString);
				}
			}
			if (dbHelper == null) {
				dbHelper = new DBHelper(mActivity);
			}
			String jsonStr = dbHelper.selectCell(tabName, tabSql, mapValues);
			JSONArray _json = JSON.parseArray(jsonStr);
			if (_json != null) {
				/* 统一方法库回调 */
				webViewReturn(mWebView, returnMethodName, _json);
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
	 * 查询数据【所有数据】
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return handler
	 */
	public static void selectAll(String jsonString, String returnMethodName,
			BaseActivity mActivity, X5WebView mWebView, View headerView,
			View footerView, Handler handler) {
		Message msg = new Message();
		String _obj = TAG + " selectAll seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;

		String tabName = "";
		Map<String, String> mapValues = new HashMap<String, String>();
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && keyString.equals("sqlTableName")) {
					tabName = valueString;
					LogUtils.vLog(TAG, "--- add selectAll name:" + valueString);
				} else {
					mapValues.put(keyString, valueString);
					LogUtils.vLog(TAG, "--- add selectAll values:" + keyString);
				}
			}
			if (dbHelper == null) {
				dbHelper = new DBHelper(mActivity);
			}
			String jsonStr = dbHelper.selectAll(tabName, mapValues);
			JSONArray _json = JSON.parseArray(jsonStr);
			if (_json != null) {
				/* 统一方法库回调 */
				webViewReturn(mWebView, returnMethodName, _json);
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
	 * 自定义sql语句
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return handler
	 */
	public static void theCustomSql(String jsonString, String returnMethodName,
			BaseActivity mActivity, X5WebView mWebView, View headerView,
			View footerView, Handler handler) {
		Message msg = new Message();
		String _obj = TAG + " theCustomSql seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;

		String tabName = "";
		String sql = "";
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && keyString.equals("sqlTableName")) {
					tabName = valueString;
					LogUtils.vLog(TAG, "--- add theCustomSql name:" + valueString);
				} else {
					if (keyString != null && keyString.equals("sql")) {
						sql = valueString;
						LogUtils.vLog(TAG, "--- add theCustomSql sql:" + valueString);
					}
				}
			}
			if (dbHelper == null) {
				dbHelper = new DBHelper(mActivity);
			}
			String jsonStr = dbHelper.theCustomSql(tabName, sql);
			JSONArray _json = JSON.parseArray(jsonStr);
			if (_json != null) {
				/* 统一方法库回调 */
				webViewReturn(mWebView, returnMethodName, _json);
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
