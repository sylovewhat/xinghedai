package com.tc.emms.utils;

import android.content.Context;
import android.util.Log;

public class OrderUtils {

	// private static String md5_key = "150bf1caa5f5a1385bee63037217e711";
	// 商户号
	// private static String e = "1001100016";
	// member_id
	// private static String m = "600016";
	// 操作员ID,7位,不足前补0
	// private static String i = "0000000";
	// 金额,8位,单位分,不足前补0
	// private static String j = "00000001";
	// 固定值
	private static String ek = "8941053627";

	/**
	 * 生成订单号
	 */
	public static String getOrderNo(String md5_key, String o, String e,
			String member_id, String i, String j, Context context) {
		Log.e("", "end md5_key: " + md5_key);
		Log.e("", "end e: " + e);
		Log.e("", "end member_id: " + member_id);
		if (i.length() < 7) {
			String n_i = i;
			for (int k = 0; k < 7; k++) {
				Log.d("", "n_i: " + n_i);
				if (n_i.length() < 7) {
					n_i = "0" + n_i;
				}
			}
			i = n_i;
			Log.e("", "end i: " + i);
		}
		j = Integer.parseInt((int) (Double.parseDouble(j) * 100) + "") + "";
		if (j.length() < 8) {
			String n_j = j;
			for (int k = 0; k < 8; k++) {
				Log.d("", "n_j: " + n_j);
				if (n_j.length() < 8) {
					n_j = "0" + n_j;
				}
			}
			j = n_j;
			Log.e("", "end j: " + j);
		}
		String o_no = "";
		// String o = makeOrderNo();
		Log.e("", "o: " + o);
		String md5_asc = makeMd5Asc(md5_key, member_id);
		Log.e("", "md5_asc: " + md5_asc);
		String end_str = enMixStr(e, o, i, j, ek, md5_asc);
		Log.e("", "end_str: " + end_str);
		o_no = end_str;
		Log.e("", "end lenght: " + o_no.length());
		return o_no;
	}

	public static String makeOrderNo() {
		String r_o = "";
		String _time = DateUtils.getNowTime();
		Log.v("", "getCode _time: " + _time);
		String _data = DateUtils.getNowTime_YMD();
		Log.v("", "getCode _data: " + _data);//2017-12-27
		String s1 = (char) (Integer.parseInt(_data.substring(2, 4)) + 65) + "";
		Log.v("", "getCode s1: " + s1);
		String s2 = (char) (Integer.parseInt(_data.substring(4, 6)) + 65) + "";
		Log.v("", "getCode s2: " + s2);
		String s3 = (char) (Integer.parseInt(_data.substring(6, 8)) + 65) + "";
		Log.v("", "getCode s3: " + s3);
		String s4 = _time.substring(5, 10);
		Log.v("", "getCode s4: " + s4);
		String strRand = "";
		for (int i = 0; i < 5; i++) {
			strRand += String.valueOf((int) (Math.random() * 10));
		}
		Log.v("", "getCode strRand: " + strRand);
		r_o = s1 + s2 + s3 + s4 + strRand;
		return r_o;
	}

	private static String makeMd5Asc(String md5_key, String m_id) {
		int sum = 0;
		for (int i = 0; i < md5_key.length(); i++) {
			int md5_value = StringToAscii(md5_key.substring(i, (i + 1)));
			sum += md5_value;
		}
		return (sum + Integer.parseInt(m_id)) + "";
	}

	private static String enMixStr(String e, String o, String i, String j,
			String ek, String md5_asc) {
		String end_str = "";
		// 加入操作员7,金额8,
		String str = i + j;
		Log.v("", "str: " + str);
		// 用自己补齐位数 如'1234'用自己补齐到10位为'1234123412'
		int old_length = md5_asc.length();
		Log.v("", "old_length: " + old_length);
		if (old_length < 13) {
			for (int k = 0; k < 13; k++) {
				md5_asc += md5_asc;
			}
		}
		String md5 = md5_asc.substring(0, 13);
		Log.v("", "md5: " + md5);
		Log.v("", "new new_length: " + md5.length());
		// 订单号打乱
		String o1 = "";
		for (int a = 0; a < 13; a++) {
			int k = Integer.parseInt(md5.substring(a, (a + 1)));
			if (a < 3) {
				o1 += (char) ((StringToAscii(o.substring(a, (a + 1)))) + k);
			} else {
				o1 += (char) ((Integer.parseInt((o.substring(a, (a + 1))))) + k + 65);
			}
		}
		Log.v("", "o1: " + o1);
		// 加入订单号
		for (int a = 0; a < 13; a++) {
			int k = Integer.parseInt(md5.substring(a, (a + 1)));
			String v = o1.substring(a, (a + 1));
			if (k == 0) {
				// 插入到第一位
				str = v + str;
			} else {
				if (k == str.length()) {
					// 插入到最后一位
					str += v;
				} else {
					// 插入到中间位置
					String s1 = str.substring(0, k);
					String s2 = str.substring(k, str.length());
					str = s1 + v + s2;
				}
			}
		}
		int _length = str.length();
		Log.v("", "_length: " + _length);
		// 倒置
		String str1 = new StringBuffer(str).reverse().toString();
		Log.v("", "old str1: " + str1);
		// 加入商户号
		for (int a = 0; a < 10; a++) {
			// 商户号10位
			int k = Integer.parseInt(ek.substring(a, (a + 1)));
			String s1 = str1.substring(0, k);
			String s2 = str1.substring(k, str1.length());
			str1 = s1 + e.substring(a, (a + 1)) + s2;
		}
		Log.v("", "new str1: " + str1);
		// 分半打乱插入,后半段插入到前半段
		_length = str1.length();
		String s1 = str1.substring(0, _length / 2);
		String s2 = str1.substring(_length / 2, _length);
		for (int a = 0; a < _length / 2; a++) {
			end_str += s1.substring(a, (a + 1)) + s2.substring(a, (a + 1));
		}
		return end_str;
	}

	/* 字符串转换为ASCII码 */
	public static int StringToAscii(String s) {
		int as_code = 0;
		char[] chars = s.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			as_code = chars[i];
		}
		return as_code;
	}
}
