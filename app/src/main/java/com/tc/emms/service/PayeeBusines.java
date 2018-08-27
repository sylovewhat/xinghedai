package com.tc.emms.service;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tc.emms.R;
import com.tc.emms.bean.BaseResponse;
import com.tc.emms.service.BusinessResolver.BusinessCallback;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.LogUtils;

@SuppressLint({ "InlinedApi", "NewApi" })
public class PayeeBusines {
	
	private static String TAG = "PayeeBusines";

	/* 获取当前系统的android版本号 */
	static int currentapiVersion = android.os.Build.VERSION.SDK_INT;
	static int accVersion = 11;

	/***
	 * H5原生交互统一请求数据接口
	 * 
	 * @param callback
	 * @return
	 */
	@SuppressLint("NewApi")
	public static RequestTask post_action(Context context, JSONObject str_json, String post_url, String post_key,
			BusinessCallback<BaseResponse> callback) {
		final RequestTask task = new RequestTask(callback, context);
		BusinessRequest request = new BusinessRequest(
				BusinessRequest.REQUEST_TYPE_POST,
				BusinessRequest.RESULT_TYPE_OBJECT);
		request.proDialogMsgId = R.string.loading_value;
		request.RESULT_ACT = 0;
		request.cls = BaseResponse.class;
		request.paramsJSON = str_json.toString();
		/*request.url = ConstantUtils.UMAIN;*/
		request.url = post_url;
		request.key = post_key;

		if (currentapiVersion >= accVersion) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request);
		} else {
			task.execute(request);
		}
		return task;
	}

	/***
	 * H5原生交互统一请求数据接口[上传图片]
	 * 
	 * @param callback
	 * @return
	 */
	@SuppressLint("NewApi")
	public static RequestTask post_action(Context context, String str_json,
			BusinessCallback<BaseResponse> callback) {
		final RequestTask task = new RequestTask(callback, context);
		BusinessRequest request = new BusinessRequest(
				BusinessRequest.REQUEST_MCHNT_POST_IMG,
				BusinessRequest.RESULT_TYPE_OBJECT);
		request.proDialogMsgId = R.string.loading_value;
		request.RESULT_ACT = 0;
		request.cls = BaseResponse.class;
		request.paramsJSON = str_json;
		request.url = ConstantUtils.UMAIN;
		LogUtils.dLog(TAG, "上传图片 str_json: " + str_json);
		if (currentapiVersion >= accVersion) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request);
		} else {
			task.execute(request);
		}
		return task;
	}

	/* 上传文件至Server的方法 */
	public static void uploadFile(String actionUrl, File uploadFile,
			Handler mHandler) {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		try {
			URL url = new URL(actionUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* 允许Input、Output，不使用Cache */
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* 设置传送的method=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			/* 设置DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; "
					+ "name=\"file1\";filename=\"" + "test.jpg" + "\"" + end);
			ds.writeBytes(end);
			/* 取得文件的FileInputStream */
			FileInputStream fStream = new FileInputStream(uploadFile);
			/* 设置每次写入1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			/* 从文件读取数据至缓冲区 */
			while ((length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
			fStream.close();
			ds.flush();
			/* 取得Response内容 */
			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			/* 将Response显示于Dialog */
			LogUtils.vLog(TAG, "上传成功: " + b.toString().trim());
			Message message = mHandler.obtainMessage();
			message.what = 111;
			mHandler.sendMessage(message);
			/* 关闭DataOutputStream */
			ds.close();
		} catch (Exception e) {
			LogUtils.vLog(TAG, "上传失败: " + e);
			Message message = mHandler.obtainMessage();
			message.what = 222;
			mHandler.sendMessage(message);
		}
	}

	/***
	 *
	 */
	public static RequestTask sendSMS(Context context, String qm, String tm,
			String te, String pr, BusinessCallback<BaseResponse> callback) {
		final RequestTask task = new RequestTask(callback, context);
		BusinessRequest request = new BusinessRequest(
				BusinessRequest.REQUEST_TYPE_POST_S,
				BusinessRequest.RESULT_TYPE_OBJECT);
		request.proDialogMsgId = R.string.loading_value;
		JSONObject str_json = new JSONObject();
		str_json.put("a", "send");
		str_json.put("qm", qm);
		str_json.put("tm", tm);
		str_json.put("te", te);
		LogUtils.iLog(TAG, "sendSMS pr:" + JSON.parseObject(pr));
		str_json.put("pr", JSON.parseObject(pr));

		request.RESULT_ACT = ConstantUtils.ACT_GET_MSM;
		request.cls = BaseResponse.class;
		request.paramsJSON = str_json.toString();
		request.url = ConstantUtils.USMS;

		if (currentapiVersion >= accVersion) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request);
		} else {
			task.execute(request);
		}
		return task;
	}
	
	
	public static RequestTask post_action_rsa(Context context, JSONObject str_json,
			BusinessCallback<BaseResponse> callback) {
		final RequestTask task = new RequestTask(callback, context);
		BusinessRequest request = new BusinessRequest(
				BusinessRequest.REQUEST_TYPE_POST_ASE,
				BusinessRequest.RESULT_TYPE_OBJECT);
		request.proDialogMsgId = R.string.loading_value;
		request.RESULT_ACT = 1;
		request.cls = BaseResponse.class;
		request.paramsJSON = str_json.toString();
		request.url = ConstantUtils.SERVER_ADDRESSTES;
		request.key = "s";

		if (currentapiVersion >= accVersion) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request);
		} else {
			task.execute(request);
		}
		return task;
	}
}
