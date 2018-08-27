package com.tc.emms.html;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.model.ACCOUNT_INFO;
import com.tc.emms.model.PRINT_ACCOUNT_INFO;
import com.tc.emms.model.PRINT_ORDER_INFO;
import com.tc.emms.printer.PrintHelper;
import com.tc.emms.printer.PrintUtil;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.utils.SharedUtils;
import com.tc.emms.utils.ToastShow;
import com.tc.emms.utils.ToastUtils;
import com.tc.emms.widget.X5WebView;

/*
 * 打印机公共类
 */

@SuppressLint("HandlerLeak")
public class PosPrintClass {

	private static String TAG = "PosPrintClass";
	
	/***
	 * 打印[推送打印]
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return 设备ID
	 */
	private static boolean isPrintEnd = true;
	private static boolean printCountEnd = false;
	/* 缓存需要打印的内容 */
	private static List<String> allPrint = new ArrayList<String>();
	public static boolean startPrintPush(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {
		boolean printResult = false;
		LogUtils.iLog(TAG, "startPrintPush jsonString:" + jsonString);
		try {
			JSONObject object = JSON.parseObject(jsonString);
			String memo = object.getString("memo");
			if(memo != null && !memo.equals("")){
				LogUtils.iLog(TAG, "onNotification startPrintPush isPrintEnd:" + isPrintEnd);
				if (isPrintEnd) {
					isPrintEnd = false;
					memo = memo.replace("<br>", "\r\n");
					LogUtils.iLog(TAG, "startPrintPush _text:" + memo);
					/* 自动打印 */
					print_memo(mActivity, memo);
				} else {
					LogUtils.eLog(TAG, "onNotification startPrintPush自动打印！正在打印中,添加：" + memo);
					allPrint.add(memo);
				}
			}

		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return printResult;
	}
	
	private static void print_memo(BaseActivity context, String memo) {
		LogUtils.iLog(TAG, "onNotification print_memo memo:" + memo);
		String isAutoPrint = SharedUtils.getValue(context,
				PrintUtil.KEY_IS_AUTU_PRINT);
		LogUtils.iLog(TAG, "onNotification print_memo isAutoPrint:" + isAutoPrint);
		if ("".equals(isAutoPrint) || "1".equals(isAutoPrint)) {
			int count = SharedUtils.getInt(context, PrintUtil.KEY_PRINT_COUNT);
			for (int i = 0; i < count; i++) {
				String _title_text = "客户联";
				if (i == 0) {
					_title_text = "存根联";
				}
				String prnText = " \r\n----------云POS签购单-----------\r\n \r\n"
						+ memo + " \r\n \r\n收银员签名:\r\n \r\n \r\n \r\n\r\n"
						+ " 客户签名:\r\n \r\n \r\n \r\n\r\n" + " \r\n------------"
						+ _title_text + "------------\r\n \r\n";
				try {
					PrintHelper.printPOS(context, prnText, print_handler);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (i == (count - 1)) {
					try {
						LogUtils.iLog(TAG, "onNotification 打印最后一联 休眠3秒:"
								+ printCountEnd);
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					printCountEnd = true;
					LogUtils.iLog(TAG, "onNotification 打印最后一联:" + printCountEnd);
				} else {
					printCountEnd = false;
					LogUtils.iLog(TAG, "onNotification 打印第" + (i + 1) + "联");
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
	}

	/***
	 * 打印[订单详情]
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return 设备ID
	 */
	public static boolean startPrintPay(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {
		boolean printResult = false;
		LogUtils.iLog(TAG, "startPrintPay jsonString:" + jsonString);
		PRINT_ORDER_INFO mPRINT_ORDER_INFO = JSON.parseObject(jsonString,
				PRINT_ORDER_INFO.class);
		if (mPRINT_ORDER_INFO != null) {
			LogUtils.iLog(TAG, "startPrintPay mPRINT_ORDER_INFO:"
					+ mPRINT_ORDER_INFO.toString());
			
			String isAutoPrint = SharedUtils.getValue(mActivity,
					PrintUtil.KEY_IS_AUTU_PRINT);
			LogUtils.iLog(TAG, "startPrintPay isAutoPrint:" + isAutoPrint);
			if ("".equals(isAutoPrint) || "1".equals(isAutoPrint)) {
				PrintHelper.printResult(mActivity, mPRINT_ORDER_INFO,
						print_handler);
			}
		}
		return printResult;
	}

	/***
	 * 结算打印
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return 设备ID
	 */
	public static boolean startPrintAccount(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		boolean printResult = false;

		String listStr = "";
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && keyString.equals("listStr")) {
					listStr = valueString;
				}
			}

		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		if (!listStr.equals("")) {
			LogUtils.vLog(TAG, "startPrintAccount listStr:" + listStr);
			ACCOUNT_INFO PI = JSON.parseObject(jsonString, ACCOUNT_INFO.class);
			List<PRINT_ACCOUNT_INFO> list = new ArrayList<PRINT_ACCOUNT_INFO>();
			list = JSON.parseArray(listStr, PRINT_ACCOUNT_INFO.class);

			int count = SharedUtils
					.getInt(mActivity, PrintUtil.KEY_PRINT_COUNT);
			for (int i = 0; i < count; i++) {
				try {
					PrintHelper
							.printQianTui(mActivity, PI, list, print_handler);
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (i < count - 1) {
					try {
						Thread.sleep(3500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					printResult = true;
				}
			}
		}
		return printResult;
	}

	static Handler print_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 更改页面
			switch (msg.what) {
				case 0 :
					LogUtils.iLog(TAG, "startPrint 打印失败");
					String _msg = "打印失败: " + (String) msg.obj;
					/* 全局弹窗显示通知 */
					ToastUtils.getInstances().showDialog("提示", _msg);
					break;
				case 1 :
					LogUtils.eLog(TAG, "startPrint 打印成功");
					if (printCountEnd) {
						printCountEnd = false;
						LogUtils.iLog(TAG, "onNotification 最后一联打印完成:" + printCountEnd);
						isPrintEnd = true;
						if (allPrint.size() > 0) {
							/* 自动打印 */
							print_memo(BaseActivity.mActivity, allPrint.get(0));
							allPrint.remove(0);
						}else {
							ToastShow.showShort(BaseActivity.mActivity, "打印成功");
						}
					}
					break;
				default :
					break;
			}
		}
	};

}
