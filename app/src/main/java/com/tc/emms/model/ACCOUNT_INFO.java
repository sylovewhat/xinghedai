package com.tc.emms.model;


/****
 * 结算，统计类
 * 
 * @author tomenter
 * 
 */

public class ACCOUNT_INFO{
	
	/** 实收总额 ***/
	public String real_money;

	/** 交易总额 ***/
	public String trade_money;
	
	/** 交易笔数 ***/
	public String trade_num;

	/** 退款总额 ***/
	public String refund_money;

	/** 核销总额 ***/
	public String member_hexiao_money;

	/** 结算开始时间 ***/
	public String b_date;
	
	/** 结算结束时间 ***/
	public String e_date;
	
	/** 操作员 ***/
	public String operate_code;
}