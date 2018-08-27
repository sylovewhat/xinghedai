package com.tc.emms.printer;

import java.io.IOException;
import java.util.List;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.tc.emms.model.ACCOUNT_INFO;
import com.tc.emms.model.PRINT_ACCOUNT_INFO;
import com.tc.emms.model.PRINT_ORDER_INFO;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.DateUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.utils.SharedUtils;

public class PrintHelper {
	
	private static String TAG = "PrintHelper";

	public static Handler printHandler;
	public static void printRlt(Context context, PRINT_ORDER_INFO payOrder) {
		
		String isAutoPrint = SharedUtils.getValue(context,
				PrintUtil.KEY_IS_AUTU_PRINT);
		if ("".equals(isAutoPrint) || "1".equals(isAutoPrint)) {
			printResult(context, payOrder, false);
		} 
	}

	/***
	 * 
	 * @param c
	 *            商家名称
	 * @param u
	 *            操作员 001
	 * @param p
	 *            支付账号
	 * @param p_t
	 *            交易类型
	 * @param pt
	 *            支付类型
	 * @param o
	 *            订单号
	 * @param pri
	 *            交易金额
	 * @param cd
	 *            持卡人签名
	 * 
	 *            收单机构---（）
	 */
	public static void printResult(Context context, PRINT_ORDER_INFO orderInfo) {
		printResult(context, orderInfo, true);
	}

	public static void printResult(Context context, PRINT_ORDER_INFO orderInfo,
			Handler mHandler) {
		printHandler = mHandler;
		printResult(context, orderInfo, true);
	}

	public static void printResult(Context context, PRINT_ORDER_INFO orderInfo,
			Boolean showTips) {

		String p_type = orderInfo.trade_type.equalsIgnoreCase("1")
				? "支付宝"
				: (orderInfo.trade_type.equalsIgnoreCase("2")
						? "微信"
						: (orderInfo.trade_type.equalsIgnoreCase("3")
								? "百度百付宝"
								: (orderInfo.trade_type.equalsIgnoreCase("4")
										? "苏宁易付宝"
										: (orderInfo.trade_type
												.equalsIgnoreCase("6")
												? "京东钱包"
												: (orderInfo.trade_type
														.equalsIgnoreCase("7")
														? "QQ钱包"
														: (orderInfo.trade_type
																.equalsIgnoreCase("8")
																? "翼支付"
																: (orderInfo.trade_type
																		.equalsIgnoreCase("9")
																		? "银联（储蓄卡）"
																		: (orderInfo.trade_type
																				.equalsIgnoreCase("10")
																				? "银联（信用卡）"
																				: "智能扫码"))))))));
		String paytype = "";
		if (!orderInfo.trade_type.equalsIgnoreCase("10")) {
			paytype = (orderInfo.pay_type.equalsIgnoreCase("1")
					? "条码"
					: (orderInfo.pay_type.equalsIgnoreCase("2")
							? "声波"
							: (orderInfo.pay_type.equalsIgnoreCase("3")
									? "二维码"
									: (orderInfo.pay_type.equalsIgnoreCase("4")
											? "线上"
											: (orderInfo.pay_type
													.equalsIgnoreCase("5")
													? "一码付"
													: "")))))
					+ "支付";
		}

		String mechanism = orderInfo.trade_type.equalsIgnoreCase("1")
				? "支付宝（杭州）有限公司"
				: (orderInfo.trade_type.equalsIgnoreCase("2")
						? "深圳财付通科技有限公司"
						: (orderInfo.trade_type.equalsIgnoreCase("3")
								? "北京百付宝科技有限公司"
								: (orderInfo.trade_type.equalsIgnoreCase("4")
										? "苏宁易付宝科技有限公司"
										: (orderInfo.trade_type
												.equalsIgnoreCase("5")
												? "支付宝（杭州）有限公司"
												: (orderInfo.trade_type
														.equalsIgnoreCase("6")
														? "北京京东世纪贸易有限公司"
														: (orderInfo.trade_type
																.equalsIgnoreCase("7")
																? "深圳财付通科技有限公司"
																: (orderInfo.trade_type
																		.equalsIgnoreCase("8")
																		? "天翼电子商务有限公司"
																		: (orderInfo.trade_type
																				.equalsIgnoreCase("9")
																				? "网银支付"
																				: (orderInfo.trade_type
																						.equalsIgnoreCase("10")
																						? "网银支付"
																						: "智能扫码")))))))));
		int _flag = 1;
		double sj = Double.valueOf(orderInfo.real_money) * 100;
		double tj = Double.valueOf(orderInfo.refund_money) * 100;
		LogUtils.vLog(TAG, "sj:" + sj);
		LogUtils.vLog(TAG, "tj:" + tj);
		if (sj == 0) {
			_flag = 2;
			LogUtils.vLog(TAG, "已退款");
		} else {
			if (tj == 0) {
				_flag = 1;
				LogUtils.vLog(TAG, "支付成功");
			} else {
				_flag = 0;
				LogUtils.vLog(TAG, "部分退款");
			}
		}
		String payState = _flag == 0 ? "部分退款" : (_flag == 1
				? "支付成功"
				: (_flag == 2 ? "已退款" : ""));
		String busName = orderInfo.member_name + "";
		String cashier_name = orderInfo.cashier_name + "";
		LogUtils.vLog(TAG, "cashier_name json:" + orderInfo.cashier_name);
		if (orderInfo.pay_account == null) {
			orderInfo.pay_account = "";
		}
		String t = DateUtils.getStrTime(orderInfo.c_time);
		String prnText = " \r\n----------云POS签购单-----------\r\n \r\n"
				+ "商家名称: "
				+ busName
				+ "\r\n \r\n"
				+ "操作员名: "
				+ cashier_name
				+ "\r\n"
				+ "支付状态: "
				+ payState
				+ "\r\n"
				+ "支付账号: "
				+ orderInfo.pay_account
				+ "\r\n"
				+ "交易类型: "
				+ p_type
				+ paytype
				+ "\r\n"
				+ "收款机构: "
				+ mechanism
				+ "\r\n"
				+ "交易单号: "
				+ orderInfo.order_no
				+ "\r\n"
				+ "交易时间: "
				+ t
				+ "\r\n"
				+ "交易金额: "
				+ "RMB"
				+ orderInfo.trade_money
				+ "\r\n \r\n"
				+ "收银员签名:\r\n \r\n \r\n \r\n \r\n"
				+ " 客户签名:\r\n \r\n \r\n \r\n\r\n"
				+ " \r\n---------重打印---------\r\n \r\n";
		int count = SharedUtils.getInt(context, PrintUtil.KEY_PRINT_COUNT);
		for (int i = 0; i < count; i++) {
			if (i == 0) {
				prnText = prnText.replace("重打印", "存根联-重打印");
			} else {
				prnText = prnText.replace("存根联", "客户联");
			}
			try {
				printPOS(context, prnText, printHandler);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (i < count - 1) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void printQianTui(Context context, ACCOUNT_INFO PI,
			List<PRINT_ACCOUNT_INFO> list, Handler mHandler) {
		printHandler = mHandler;
		// UserInfo userInfo = LocalUserData.getInstance().getUserInfo();
		String busName = SharedUtils.getValue(context,
				ConstantUtils.MEMBER_NAME);
		String busNo = SharedUtils.getValue(context, ConstantUtils.BUSINESS_NO);
		String mchid = SharedUtils.getValue(context, ConstantUtils.TN);
		LogUtils.iLog(TAG, "print_account PI.operate_code:" + PI.operate_code);

		String otherText = "";
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {

				PRINT_ACCOUNT_INFO PAI = list.get(i);
				otherText += "交易类型:" + PAI.tongdao_name + "\r\n" + "实收总额:"
						+ PAI.real_money + "\r\n" + "交易总额:" + PAI.trade_money
						+ "\r\n" + "交易笔数:" + PAI.trade_num + "\r\n" + "退款总额:"
						+ PAI.refund_money + "\r\n" + "核销总额:"
						+ PI.member_hexiao_money
						+ "\r\n--------------------------------\r\n";
			}
		}

		String prnText = "\r\n----------云POS结算单----------\r\n \r\n" + "门店名称:"
				+ busName + "\r\n" + "商户号:" + busNo + "\r\n" + "终端号:" + mchid
				+ "\r\n" + "操作员:" + PI.operate_code + "\r\n" + "起始时间:"
				+ PI.b_date + "\r\n" + "结束时间:" + PI.e_date + "\r\n"
				+ "--------------------------------\r\n" + "结算总额:" + "RMB"
				+ PI.trade_money
				+ "\r\n \r\n--------------------------------\r\n" + "实收总额:"
				+ PI.real_money + "\r\n" + "交易总额:" + PI.trade_money + "\r\n"
				+ "交易笔数:" + PI.trade_num + "\r\n" + "退款总额:" + PI.refund_money
				+ "\r\n" + "核销总额:" + PI.member_hexiao_money
				+ "\r\n \r\n-----------结算明细------------\r\n \r\n"

				+ otherText + "\r\n" + " \r\n"
				+ "操作员签名:\r\n \r\n \r\n \r\n \r\n ";

		// 获取系统打印管理对象
		try {
			printPOS(context, prnText, printHandler);
		} catch (Exception e) {
		}
	}

	@SuppressLint("NewApi")
	public static void printPOS(Context context, String prtMsg, Handler mHandler) {
		printHandler = mHandler;
		// 1: Get BluetoothAdapter
		Message msg = printHandler.obtainMessage();
		BluetoothAdapter btAdapter = BluetoothUtil.getBTAdapter();
		if (btAdapter == null) {
			if (printHandler != null) {
				msg.what = 0;
				msg.obj = "请开启蓝牙";
				printHandler.sendMessage(msg);
			} else {
				Toast.makeText(context, "请开启蓝牙！", Toast.LENGTH_LONG).show();
			}
		} else {
			LogUtils.iLog(TAG, "第二步");
			// 2: Get Sunmi's InnerPrinter BluetoothDevice
			String add_print = SharedUtils.getValue(context,
					PrintUtil.KEY_PRINT_ADD);
			BluetoothDevice device = BluetoothUtil.getDevice(btAdapter,
					add_print);
			if (device == null) {
				if (printHandler != null) {
					msg.what = 0;
					msg.obj = "请确认打印机是否已连接";
					printHandler.sendMessage(msg);
				} else {
					Toast.makeText(context, "请确认打印机是否已连接！", Toast.LENGTH_LONG)
							.show();
				}
			} else {
				LogUtils.iLog(TAG, "第三步");
				// 3: Generate a order data
				byte[] data = ESCUtil.generateMockData(prtMsg);
				// 4: Using InnerPrinter print data
				BluetoothSocket socket = null;
				try {
					socket = BluetoothUtil.getSocket(device);
					LogUtils.eLog(TAG, "PPPPPP 蓝牙状态:" + socket.isConnected());
					BluetoothUtil.sendData(data, socket);
					if (printHandler != null) {
						msg.what = 1;
						msg.obj = "打印成功";
						printHandler.sendMessage(msg);
					} else {
						Toast.makeText(context, "打印成功！", Toast.LENGTH_LONG)
								.show();
					}
				} catch (IOException e) {
					if (socket != null) {
						try {
							socket.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					if (printHandler != null) {
						msg.what = 0;
						msg.obj = e.getMessage();
						printHandler.sendMessage(msg);
					} else {
						Toast.makeText(context, "打印失败！" + e.getMessage(),
								Toast.LENGTH_LONG).show();
					}
				}
			}
		}
	}
}
