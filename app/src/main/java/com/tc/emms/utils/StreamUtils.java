package com.tc.emms.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 公用的常用工具类
 * 
 * @author xiaoben
 */
public class StreamUtils {

	/**
	 * 从InputStream中读取数据并转为字符串类�?
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String convertToString(InputStream is) throws IOException {
		BufferedReader reader = null;
		StringBuilder builder = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
			String buff;
			while ((buff = reader.readLine()) != null) {
				builder.append(buff);
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return builder.toString();
	}
}
