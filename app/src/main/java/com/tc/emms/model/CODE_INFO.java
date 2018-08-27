package com.tc.emms.model;


/***
 * 
 * @author tomenter
 * 
 */
public class CODE_INFO {
	// 订单号
	public String o;
	// 支付通道
	public String q;
	// 服务端返回的支付方式(1.条形码 2.声波 3.二维码,4.线上支付)
	public String t;
	// 二维码字符串
	public String qr;

}
