package com.tc.emms.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@SuppressLint("SimpleDateFormat")
public class DateUtils {

	public static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat s_ymd = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static final SimpleDateFormat s_ym = new SimpleDateFormat("yyyy-MM");
	public static final SimpleDateFormat s_md = new SimpleDateFormat("MM-dd");
	public static final SimpleDateFormat s_no = new SimpleDateFormat(
			"yyyyMMdd");
	public static final SimpleDateFormat s_SFM = new SimpleDateFormat(
			"HH:mm:ss");
	/**
	 * 系统当前时间
	 * 
	 * @return
	 */
	public static Date getCurrentDate() {
		return getCurrentCalendar().getTime();
	}

	public static Calendar getCurrentCalendar() {
		Calendar localCalendar = Calendar.getInstance(TimeZone
				.getTimeZone("Asia/Shanghai"));
		return localCalendar;
	}

	public static SimpleDateFormat getDateFormat() {
		SimpleDateFormat my_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TimeZone china = TimeZone.getTimeZone("Asia/Shanghai");
		my_format.setTimeZone(china);
		return my_format;
	}

	// 时间戳转字符串
	public static String getStrTime(String cc_time) {
		String re_StrTime = "";
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	// 字符串转时间戳
	public static String getLongTime(String user_time) {
		String long_time = null;
		Date d;
		try {
			d = sdf.parse(user_time);
			long l = d.getTime();
			String str = String.valueOf(l);
			long_time = str.substring(0, 10);
		} catch (ParseException e) {
		}
		return long_time;
	}

	/*** 获取当前时间 10位? ***/
	public static String getNowTime() {
		long currentTimeMillis = Long.parseLong(String
				.valueOf(System.currentTimeMillis()).toString()
				.substring(0, 10));
		return currentTimeMillis + "";
	}
	
	/*** 获取当前时间 2016-12-01 ***/
	public static String getNowTime_Y_M_D() {
		String re_StrTime = null;
		re_StrTime = s_ymd.format(new Date());
		return re_StrTime;
	}

	/*** 获取当前时间 20161201 ***/
	public static String getNowTime_YMD() {
		String re_StrTime = null;
		re_StrTime = s_no.format(new Date());
		return re_StrTime;
	}
	
	public static String getNowTime_SFM() {
		String re_StrTime = null;
		re_StrTime = s_SFM.format(new Date());
		return re_StrTime;
	}

	/*** 获取当前时间 2016-12 ***/
	public static String getNowTime_YM() {
		String re_StrTime = null;
		re_StrTime = s_ym.format(new Date());
		return re_StrTime;
	}

	/*** 获取当前时间 12-01 ***/
	public static String getNowTime_MD() {
		String re_StrTime = null;
		re_StrTime = s_md.format(new Date());
		return re_StrTime;
	}
	
	/** 
     * 获得指定日期的前一天 
     *  
     * @param specifiedDay 
     * @return 
     * @throws Exception 
     */  
    public static String getSpecifiedDayBefore() {
    	//可以用new Date().toLocalString()传递参数  
        Calendar c = Calendar.getInstance();  
        Date date = new Date();   //当前时间
        c.setTime(date);  
        int day = c.get(Calendar.DATE);  
        c.set(Calendar.DATE, day - 1);  
  
        String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c  
                .getTime());  
        return dayBefore;  
    }  
}
