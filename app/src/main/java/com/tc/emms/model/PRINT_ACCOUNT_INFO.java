package com.tc.emms.model;

/****
 * 结算对应通道金额
 * 
 * @author tomenter
 * 
 */

public class PRINT_ACCOUNT_INFO{
	
	/** 通道名称 ***/
	public String tongdao_name;
	
	/** 通道类型 ***/
	public String trade_type;
	
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
}