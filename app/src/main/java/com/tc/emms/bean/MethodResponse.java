package com.tc.emms.bean;

public class MethodResponse {
	public int state;
	public String message;
	public String data;

	public boolean isSuccess() {
		return (state == 1) ? true : false;
	}

}
