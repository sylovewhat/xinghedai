package com.tc.emms.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tc.emms.service.BusinessResolver.BusinessCallback;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.JsonUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.utils.NetUtils;
import com.tc.emms.utils.RSAUtils;
import com.tc.emms.utils.SharedUtils;

@SuppressWarnings("rawtypes")
public class RequestTask extends
		AsyncTask<BusinessRequest, Void, BusinessResult> {
	private static String TAG = "RequestTask";
	private BusinessCallback mCallback;

	private BusinessRequest request;

	/** 进度密?? */
	// private ProDialog mProDialog;

	private Context mContext;

	public RequestTask(BusinessCallback callback, Context context) {
		mCallback = callback;
		this.mContext = context;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected BusinessResult doInBackground(BusinessRequest... params) {
		request = params[0];
		BusinessResult result = new BusinessResult();
		String strParams = JsonUtils.paramsToJson(request);
		String resultJson = null;
		LogUtils.dLog(TAG, "json: " + request.paramsJSON);
		try {
			switch (request.requestType) {
			case BusinessRequest.REQUEST_TYPE_GET:
				if (request.params != null) {
					resultJson = BusinessResolver.getData(request.url,
							request.params);
				} else {
					resultJson = BusinessResolver.getData(request.url);
				}
				break;
			case BusinessRequest.REQUEST_TYPE_POST:
				/*取出post_key*/
				JSONObject object = JSON.parseObject(strParams);
				request.url = object.getString("post_url");
				String post_key = object.getString("post_key");
				if(post_key != null){
					strParams = post_key + "=" + strParams;
				}else{
					object.remove("post_url");
					object.remove("hud_type");
					Map<String, Object> mapValues = new HashMap<String, Object>();
					try {
						// 动态获取key值
						for (Map.Entry<String, Object> entry : object.entrySet()) {
							// 提取key
							String keyString = entry.getKey();
							// 提取value
							String valueString = entry.getValue().toString();
							if (keyString != null && !keyString.equals("")) {
								mapValues.put(keyString, valueString);
							} 
						}

					} catch (JSONException e) {
						throw new RuntimeException(e);
					}
					if (params != null && !mapValues.isEmpty()) {
						StringBuilder sb = new StringBuilder();

						for (Entry<String, Object> entry : mapValues.entrySet()) {
							if (entry.getValue() != null) {
								sb.append(entry.getKey()).append("=")
										.append(entry.getValue()).append("&");
							}
						}
						sb.deleteCharAt(sb.lastIndexOf("&"));
						strParams = sb.toString();
					}
				}
				resultJson = NetUtils.doPost(request.url, strParams);
				break;
			case BusinessRequest.REQUEST_MCHNT_POST_IMG:
				strParams = request.key + "=" + strParams;
				LogUtils.vLog(TAG, "strParams: " + strParams);
				String _post = strParams.replaceAll("\\+", "%2B");
				LogUtils.vLog(TAG, "_post: " + _post);
				resultJson = NetUtils.doPost(request.url, _post);
				break;
			case BusinessRequest.REQUEST_TYPE_POST_ASE:
				/****** 对请求体加密 *****/
				String business_no = SharedUtils.getValue(mContext,
						ConstantUtils.BUSINESS_NO);
				String str_key = SharedUtils.getValue(mContext,
						ConstantUtils.RSA_KEY);
				LogUtils.vLog(TAG, "url: " + request.url + strParams);
				strParams = request.key + "="
						+ RSAUtils.rsaEncode(strParams, business_no, str_key);

				resultJson = NetUtils.doPost(request.url, strParams);
				break;
			}
			if (!TextUtils.isEmpty(resultJson)) {
				LogUtils.iLog(TAG, "*************** 接口  return:" + resultJson);
				result.json_string = resultJson;
				switch (request.resultType) {
				case BusinessRequest.RESULT_TYPE_OBJECT:
					// 返回对象
					if (request.cls == null) {
						result.success = false;
						break;
					}
					result.returnObject = JSON.parseObject(resultJson,
							request.cls);
					result.success = true;
					break;
				case BusinessRequest.RESULT_TYPE_VOID:
					result.returnObject = null;
					result.success = true;
					break;
				case BusinessRequest.RESULT_TYPE_CUSTOM:
					// 自定义接密??
					if (request.cls == null) {
						result.success = false;
						break;
					}
					result.success = true;
					break;
				}
			} else {
				// 返回失败
				result.success = false;
			}

		} catch (BusinessException e) {
			result.exception = e;
			result.returnObject = null;
			result.success = false;
		} catch (Exception e) {
			e.printStackTrace();
			result.exception = new BusinessException(
					BusinessException.CODE_ILLEGAL_RETURN);
			result.returnObject = null;
			result.success = false;
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onPostExecute(BusinessResult result) {
		if (mCallback != null && !isCancelled()) {
			if (result.success) {
				mCallback.onSuccess(result.returnObject, result.json_string, request.RESULT_ACT);
			} else {
				if (result.exception != null) {
					mCallback.onError(result.exception, request.RESULT_ACT);
				} else {
					result.exception = new BusinessException(
							BusinessException.CODE_NO_DATA);
					mCallback.onError(result.exception, request.RESULT_ACT);
				}
			}
		}
	}

}
