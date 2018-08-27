package com.tc.emms.model;


/**
 * 订单/流水bean
 */

public class PAYORDER_INFO {

	/** 订单号 ***/
	public String o;
	
	/** 二维码订单号 ***/
	public String oo;

	/** 订单钱。0.01 ***/
	public String j = "0";

	/** * 1. 条码支付 2. 声波支付 3. 二维码支付 4. 线上支付 ***/
	public String t;

	/** 格式为: 2015-06-06 20:18:37 ***/
	public String dt;

	/** * 1. 支付宝 2. 微信支付 3. 百付宝 4. 易付宝, 5 其它 ***/
	public String q;

	/** 订单支付成功后返回的支付者账号 ***/
	public String u;

	/** 核销金额 ***/
	public String hj;

	/** 核销类型 ***/
	public int ht;
	
	/** 退款金额 ***/
	public String tj;

	/** 实收金额 ***/
	public String sj;
	
	/** 官方订单号 ***/
	public String go;
}