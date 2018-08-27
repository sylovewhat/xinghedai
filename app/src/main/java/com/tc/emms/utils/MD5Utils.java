package com.tc.emms.utils;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.tc.emms.base.BaseActivity;

import android.util.Log;

public class MD5Utils {
	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	public static String getMD5(JSONObject J, String md5) {
		String md5_string = "";
		try {
			
			List<String> list = new ArrayList<String>();
			Log.d("", "J: " + J.length());
			Iterator<?> iterator = J.keys();
			// Ӧ�õ�����Iterator ��ȡ���е�keyֵ
			while (iterator.hasNext()) {
				// ����ÿ��key
				String _key = (String) iterator.next();
				list.add(_key);
			}
			Collections.sort(list);
			String as = "=";
			String us = "&";
			String strInfo = "";
			for (int i = 0; i < list.size(); i++) {
				String new_key = list.get(i);
				String _value = J.getString(new_key);
				String keyS = "";
				if (i == list.size() - 1) {
					keyS = new_key + as + _value;
				} else {
					keyS = new_key + as + _value + us;
				}
				strInfo += keyS;
			}
			Log.i("", "before strInfo: " + strInfo);
			if(md5.equals("1")){
				String md5Key = SharedUtils.getValue(BaseActivity.mActivity, ConstantUtils.MD5KEY);
				if(md5Key.equals("")){
					md5Key = ConstantUtils.MD5KEY;
				}
				strInfo += "&key=" + md5Key;
			}
			Log.i("", "add key: " + strInfo);
			md5_string = MD5Utils.MD5(strInfo);
			Log.i("", "after strInfo: " + md5_string);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return md5_string;
	}
}