package com.tc.emms.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.tc.emms.base.BaseActivity;

/**
 * 网络操作工具�??
 * 
 * @author wangjie
 */
public class NetUtils {

	/**
	 * 无连�??
	 */
	public static final int STATE_CONNECT_NONE = 0;

	/**
	 * WIFI连接
	 */
	public static final int STATE_CONNECT_WIFI = 1;

	/**
	 * 移动网络 2G/3G
	 */
	public static final int STATE_CONNECT_MOBILE = 2;

	private static final int TIMEOUT = 8000;

	public static final String ENCODE_UTF_8 = "UTF-8";

	/**
	 * 判断网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context) {
		final ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
			return false;
		}
		return true;
	}

	/**
	 * 判断网络是否连接[new]
	 * 
	 * @param context
	 * @return
	 */
	@SuppressWarnings({ "static-access"})
	public static boolean isConnectedNet(BaseActivity mActivity) {
		ConnectivityManager con = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnectedOrConnecting();
		boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.isConnectedOrConnecting();
		if (wifi | internet) {
			// 执行相关操作
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取当前网络连接状�??
	 * 
	 * @param context
	 * @return 常量 STATE_CONNECT_NONE：无连接�?? STATE_CONNECT_WIFI：WIFI连接,
	 *         STATE_CONNECT_MOBILE：移动网�?? 2G/3G
	 */
	public static int getNetConnectState(Context context) {
		final ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (networkInfo != null && networkInfo.isConnected()) {
			return STATE_CONNECT_WIFI;
		}
		networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (networkInfo != null && networkInfo.isConnected()) {
			return STATE_CONNECT_MOBILE;
		}
		return STATE_CONNECT_NONE;
	}

	/**
	 * 使用GET连接指定的url网址，返回结果转为String
	 * 
	 * @param urlString
	 * @return
	 */
	public static String doGetString(String urlString) {
		InputStream is = null;
		disableConnectionReuseIfNecessary();
		HttpURLConnection connection = null;
		try {

			URL url = new URL(urlString);
			connection = (HttpURLConnection) url.openConnection();
			connection.setReadTimeout(TIMEOUT);
			connection.setConnectTimeout(TIMEOUT);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("accept", "*/*");
			connection.setDoInput(true);
			connection.connect();

			final int statusCode = connection.getResponseCode();
			if (statusCode != 200) {
				return null;
			}

			is = connection.getInputStream();
			return StreamUtils.convertToString(is);

		} catch (Exception e) {
			// System.out.print("===================================" +
			// e.getMessage());
		} finally {

			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}

	/**
	 * 向指定url发�?�POST请求，返回结果转为String
	 * 
	 * @param urlString
	 * @param params
	 *            post请求参数
	 * @return
	 */
	public static String doPostString(String urlString,
			Map<String, Object> params) {

		return doPost(urlString, mapToParams(params));
	}

	/**
	 * 将Map转换成JSON字符�??
	 * 
	 * @param params
	 * @return 实例:key1=value1&key2=value2
	 */
	public static String mapToParams(Map<String, Object> params) {
		StringBuilder paramsStr = new StringBuilder();
		if (params != null && params.size() > 0) {
			try {
				for (String key : params.keySet()) {

					paramsStr.append(URLEncoder.encode(key, ENCODE_UTF_8))
							.append("=").append(params.get(key)).append("&");

				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				paramsStr.toString();
			}
			paramsStr.deleteCharAt(paramsStr.length() - 1);
		}
		return paramsStr.toString();
	}

	/**
	 * 向指定url发�?�POST请求，返回结果转为String
	 * 
	 * @param urlString
	 * @param params
	 *            post请求参数
	 * @return
	 */
	public static String doPost(String urlString, String params) {
		if (TextUtils.isEmpty(urlString) || TextUtils.isEmpty(params)) {
			return null;
		}
		InputStream is = null;
		disableConnectionReuseIfNecessary();
		BufferedReader reader = null;
		Log.i("====== 请求连接 ======", "doPost: " + urlString + params);
		StringBuilder result = new StringBuilder();
		HttpURLConnection connection = null;
		OutputStream out = null;
		try {
			URL url = new URL(urlString);
			connection = (HttpURLConnection) url.openConnection();
			connection.setReadTimeout(TIMEOUT);
			connection.setConnectTimeout(TIMEOUT);
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.connect();
			out = connection.getOutputStream();
			out.write(params.getBytes(ENCODE_UTF_8));
			out.flush();
			out.close();

			int statusCode = connection.getResponseCode();
			if (statusCode != 200) {
				return null;
			}

			is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, ENCODE_UTF_8));
			String buff;
			while ((buff = reader.readLine()) != null) {
				result.append(buff);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// LogUtils.e(TAG, "doPost()=请求异常=====" + e.toString());
			return null;
		} finally {

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				connection.disconnect();
			}
		}
		// LogUtils.v(TAG, "doPost()=响应报文=====" + result.toString());
		return result.toString();

	}
	
	private static void disableConnectionReuseIfNecessary() {
		System.setProperty("http.keepAlive", "false");
	}

	private NetUtils() {
	}
}
