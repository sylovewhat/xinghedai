package com.tc.emms.model;


/**
 * 订单/流水bean
 */

public class PRINT_ORDER_INFO{

	/** 订单号 ***/
	public String order_no;
	
	/** 订单钱。0.01 ***/
	public String real_money;
	
	/** 订单钱。0.01 ***/
	public String refund_money;

	/** 订单钱。0.01 ***/
	public String trade_money;

	/** * 1. 条码支付 2. 声波支付 3. 二维码支付 4. 线上支付 ***/
	public String pay_type;

	/** 格式为: 2015-06-06 20:18:37 ***/
	public String c_time;

	/** * 1. 支付宝 2. 微信支付 3. 百付宝 4. 易付宝, 5 其它 ***/
	public String trade_type;

	/** 订单支付成功后返回的支付者账号 ***/
	public String pay_account;

	/** 操作员号 ***/
	public String cashier_id;
	
	/** 操作员名 ***/
	public String cashier_name;
	
	/** 商户名称 ***/
	public String member_name;

}