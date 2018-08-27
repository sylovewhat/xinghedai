package com.tc.emms.service;

import java.util.Map;

import com.tc.emms.R;
import com.tc.emms.service.BusinessResolver.JsonParser;
import com.tc.emms.utils.ConstantUtils;

@SuppressWarnings("rawtypes")
public class BusinessRequest {

	/**
	 * 请求方式 GET
	 */
	public final static int REQUEST_TYPE_GET = 0;

	/**
	 * 请求方式 POST,params不能为空
	 */
	public final static int REQUEST_TYPE_POST = 1;

	/**
	 * 请求方式 POST,不加�? s={}
	 */
	public final static int REQUEST_TYPE_POST_S = 10001;

	/**
	 * 商户认证 请求方式 POST,不带s
	 */
	public final static int REQUEST_MCHNT_POST = 10002;
	
	/**
	 * 上传图片
	 */
	public final static int REQUEST_MCHNT_POST_IMG = 10003;

	/**** 请求方式POST，且加密 ***/
	public final static int REQUEST_TYPE_POST_ASE = 3;

	/**
	 * 返回格式 对象
	 */
	public final static int RESULT_TYPE_OBJECT = 0;

	/**
	 * 返回格式 列表
	 */
	public final static int RESULT_TYPE_LIST = 1;

	/**
	 * 返回格式，自定义解析器，parser不能为空
	 */
	public final static int RESULT_TYPE_CUSTOM = 2;

	/**
	 * 返回格式 无返回结�???
	 */
	public final static int RESULT_TYPE_VOID = 3;

	/**** 接口�??? ***/
	public int RESULT_ACT;

	/** * version版本 */
	public String VERSION = "1";

	/**
	 * 加密方式 加密方式(0-127) 0=原始数据 1=Gzip压缩 2=以登录时协商�???3DES密钥进行3DES加密然后base64
	 * 3=先进行Gzip压缩，然后以协商的随�???3DES密钥进行3DES加密 4=RSA公钥加密
	 * 
	 * ***/
	public int ENCRYPT_TYPE = 0;

	/*** 2=以登录时协商�???3DES密钥�??? */
	public final static int ENCRYPT_VALUES_2 = 2;

	/*** 1=Gzip压缩 */
	public final static int ENCRYPT_VALUES_1 = 1;

	/***** 请求方式 ***/
	public String method = "POST";

	public String url = ConstantUtils.UMAIN;
	
	public String key = ConstantUtils.UKEY;
	/*** FID请求时加密随机数 **/
	public String FID_RANDOM;

	/** 请求的JSON */
	public String paramsJSON;

	public Map<String, Object> params;

	/** 请求�???,是否显示进度�???,默认true表示显示 */
	public boolean isShowProDialog = true;

	/** 请求�???,是否进度条是否可以取�???,默认true表示可以取消 */
	public boolean isCancelProDialog = true;

	/** 进度条的内容R.string */
	public int proDialogMsgId = R.string.loading_value;

	/** 请求的键值对_对象 */
	public Object paramsObject;

	boolean isGetOrPost;

	public int requestType;

	public int resultType;

	public boolean isListResult;

	JsonParser parser;

	public Class cls;

	public BusinessRequest(boolean isGetOrPost, boolean isListResult) {
		this.isGetOrPost = isGetOrPost;
		this.isListResult = isListResult;
	}

	public BusinessRequest(int requestType, int resultType) {
		this.requestType = requestType;
		this.resultType = resultType;
	}

}
