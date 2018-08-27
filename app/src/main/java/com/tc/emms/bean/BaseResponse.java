package com.tc.emms.bean;

public class BaseResponse {
	public String f;
	public String m;
	public String data;
	public String d;
	public boolean isSuccess() {
		return (f != null && f.equals("1")) ? true : false;
	}

}
