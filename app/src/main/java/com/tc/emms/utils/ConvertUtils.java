package com.tc.emms.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

/**
 * 通用工具�?
 */
@SuppressLint("SimpleDateFormat")
public class ConvertUtils {
	
	//把String转化为float  
    public static float convertToFloat(String number, float defaultValue) {  
        if (TextUtils.isEmpty(number)) {  
            return defaultValue;  
        }  
        try {  
            return Float.parseFloat(number);  
        } catch (Exception e) {  
            return defaultValue;  
        }  
  
    }  
  
    //把String转化为double  
    public static double convertToDouble(String number, double defaultValue) {  
        if (TextUtils.isEmpty(number)) {  
            return defaultValue;  
        }  
        try {  
            return Double.parseDouble(number);  
        } catch (Exception e) {  
            return defaultValue;  
        }  
  
    }  
  
    //把String转化为int  
    public static int convertToInt(String number, int defaultValue) {  
        if (TextUtils.isEmpty(number)) {  
            return defaultValue;  
        }  
        try {  
            return Integer.parseInt(number);  
        } catch (Exception e) {  
            return defaultValue;  
        }  
    }  
    
    public static String recode(String str) {  
        String formart = "";  
  
        try {  
            boolean ISO = Charset.forName("ISO-8859-1").newEncoder()  
                    .canEncode(str);  
            if (ISO) {  
                formart = new String(str.getBytes("ISO-8859-1"), "GB2312");  
                Log.i("1234      ISO8859-1", formart);  
            } else {  
                formart = str;  
                Log.i("1234      stringExtra", str);  
            }  
        } catch (UnsupportedEncodingException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return formart;  
    }  
}
