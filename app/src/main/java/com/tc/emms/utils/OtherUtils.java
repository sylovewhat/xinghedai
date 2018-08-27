package com.tc.emms.utils;

import android.annotation.SuppressLint;

/**
 * 通用工具
 */
@SuppressLint("SimpleDateFormat")
public class OtherUtils {

	/* 支付状态 */
	public static String getPayStatic(int index) {
		String s_static = "";
		switch (index) {
		case 0:
			s_static = "未支付";
			break;
		case 1:
			s_static = "已支付";
			break;
		case 2:
			s_static = "部分退款";
			break;
		case 3:
			s_static = "全额退款";
			break;
		default:
			s_static = "未支付";
			break;
		}
		return s_static;
	}

	/** * 1. 条码支付 2. 声波支付 3. 二维码支付 4. 线上支付 ***/
	public static String getPayJob(int index) {
		String s_static = "";
		switch (index) {
		case 1:
			s_static = "条码支付";
			break;
		case 2:
			s_static = "声波支付";
			break;
		case 3:
			s_static = "二维码支付";
			break;
		default:
			s_static = "线上支付";
			break;
		}
		return s_static;
	}

	/* 支付方式[显示用] */
	public static String getPayType(String index) {
		String s_static = "";
		if (index.equals("c")) {
			s_static = "银行间连";
		}
		if (index.equals("0")) {
			s_static = "智能扫码";
		}
		if (index.equals("1")) {
			s_static = "支付宝";
		}
		if (index.equals("2")) {
			s_static = "微信支付";
		}
		if (index.equals("3")) {
			s_static = "百度百付宝";
		}
		if (index.equals("4")) {
			s_static = "苏宁易付宝";
		}
		if (index.equals("5")) {
			s_static = "";
		}
		if (index.equals("6")) {
			s_static = "京东钱包";
		}
		if (index.equals("7")) {
			s_static = "QQ钱包";
		}
		if (index.equals("8")) {
			s_static = "翼支付";
		}
		if (index.equals("9")) {
			s_static = "银联";
		}
		return s_static;
	}

	/* 支付方式 */
	public static String getPayTypeInt(String index) {
		String s_static = "0";
		if (index.equals("银行间连")) {
			s_static = "c";
		}
		if (index.equals("智能扫码")) {
			s_static = "0";
		}
		if (index.equals("支付宝")) {
			s_static = "1";
		}
		if (index.equals("微信支付")) {
			s_static = "2";
		}
		if (index.equals("百度百付宝")) {
			s_static = "3";
		}
		if (index.equals("苏宁易付宝")) {
			s_static = "4";
		}
		if (index.equals("")) {
			s_static = "5";
		}
		if (index.equals("京东钱包")) {
			s_static = "6";
		}
		if (index.equals("QQ钱包")) {
			s_static = "7";
		}
		if (index.equals("翼支付")) {
			s_static = "8";
		}
		if (index.equals("银联")) {
			s_static = "9";
		}
		return s_static;
	}
	
	public static String getSeverCount() {
		String strRand = "";
		for (int i = 0; i < 7; i++) {
			strRand += String.valueOf((int) (Math.random() * 10));
		}
		return strRand + "";
	}
	
	public static String getEightCount() {
		String strRand = "";
		for (int i = 0; i < 8; i++) {
			strRand += String.valueOf((int) (Math.random() * 10));
		}
		return strRand + "";
	}
}
