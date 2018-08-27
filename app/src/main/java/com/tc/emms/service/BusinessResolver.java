package com.tc.emms.service;

import java.util.Map;
import java.util.Map.Entry;

import com.tc.emms.utils.NetUtils;

/***
 * 业务接口调用辅助�??
 * 
 * @author tomenter
 * 
 */
public abstract class BusinessResolver {

	/**
	 * 数据请求是否成功
	 */
	public static final String PRAM_SUCCESS = "success";

	/**
	 * 数据请求返回消息 成功时为“�?? 失败�?? 错误信息
	 */
	public static final String PRAM_MSG = "message";

	/**
	 * 数据请求后返回的数组
	 */
	public static final String PRAM_DATA = "data";

	/**
	 * 数据请求后返回的数组
	 */
	public static final String PRAM_MAP = "map";

	/**
	 * 数据请求后返回的数组
	 */
	public static final String PRAM_RETV = "retValue";

	/**
	 * 解析json字符串，判断是否正确返回结果，正确则解析出data属�?�中的内容，否则抛出BusinessException异常
	 * 
	 * @param json
	 * @return
	 * @throws BusinessException
	 * 
	 *             /** 使用GET方式获取数据
	 * 
	 * @param url
	 * @return
	 * @throws BusinessException
	 */
	static String getData(String url) throws BusinessException {
		String json = null;
		try {
			json = NetUtils.doGetString(url);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(BusinessException.CODE_UNREACH_SERVER);
		}
		return json;
	}

	static String getDataLogin(String url) throws BusinessException {
		return null;

	}

	/**
	 * 使用GET方式获取数据
	 * 
	 * @param url
	 * @return
	 * @throws BusinessException
	 */
	static String getData(String url, Map<String, Object> params)
			throws BusinessException {
		if (params != null && !params.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			sb.append(url).append("?");

			for (Entry<String, Object> entry : params.entrySet()) {
				if (entry.getValue() != null) {
					sb.append(entry.getKey()).append("=")
							.append(entry.getValue()).append("&");
				}
			}
			sb.deleteCharAt(sb.lastIndexOf("&"));
			url = sb.toString();
			//System.out.println(url + "---");

		}
		return getData(url);

	}

	static String getDataLogin(String url, Map<String, Object> params)
			throws BusinessException {
		if (params != null && !params.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			sb.append(url).append("?");

			for (Entry<String, Object> entry : params.entrySet()) {
				if (entry.getValue() != null) {
					sb.append(entry.getKey()).append("=")
							.append(entry.getValue()).append("&");
				}
			}
			sb.deleteCharAt(sb.lastIndexOf("&"));
			url = sb.toString();

		}
		return getDataLogin(url);

	}

	/**
	 * 使用post方式获取数据
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws BusinessException
	 */
	static String postData(String url, Map<String, Object> params)
			throws BusinessException {
		String json = null;
		try {
			json = NetUtils.doPostString(url, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	/****
	 * 自定义json转换
	 * 
	 * @author tomenter
	 * 
	 * @param <T>
	 */
	interface JsonParser<T> {
		T parse(String json);
	}

	public interface BusinessCallback<T> {
		/**
		 * 成功时调�??
		 * 
		 * @param response
		 *            泛型，按照实际返回结果使用指定对象�?�如果不�??要返回对象则使用Void
		 */
		// void onSuccess(T t);
		void onSuccess(T response, String jsonStr, int act);

		/**
		 * 发生异常时调�??
		 * 
		 * @param e
		 */
		void onError(BusinessException e, int act);
	}
}
