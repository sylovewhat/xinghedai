package com.tc.emms.html;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.tc.emms.base.BaseActivity;
import com.tc.emms.ui.IDCardActivity;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.ToastShow;
import com.tc.emms.widget.X5WebView;

/*
 * 自动识别类
 */

public class CardOrcController {

	private static String TAG = "CardOrcController";

	/***
	 * 身份证正面拍照识别
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
	public static void idcardOCROnline(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " idcardOCROnline seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;

		if (!ConstantUtils.hasGotToken) {
			ToastShow.showShort(mActivity, "token还未成功获取");
			_obj = "token还未成功获取";
			_what = ConstantUtils.HANDLER_FAILE;
			return;
		}

		
		
		ConstantUtils.ChooseImgWebView = mWebView;
		ConstantUtils.returnMethodName = returnMethodName;
		/* 打开默认图片选择视图 */
		Intent intent = new Intent(mActivity, IDCardActivity.class);
		intent.putExtra(ConstantUtils.ID_CARD_TYPE, "0");
		mActivity.startActivity(intent);

		msg.obj = _obj;
		msg.what = _what;
		handler.sendMessage(msg);
	}

	/***
	 * 身份证反面拍照识别
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
	public static void idcardOCROnlineBack(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " idcardOCROnlineBack seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;

		if (!ConstantUtils.hasGotToken) {
			ToastShow.showShort(mActivity, "token还未成功获取");
			_obj = "token还未成功获取";
			_what = ConstantUtils.HANDLER_FAILE;
			return;
		}

		ConstantUtils.ChooseImgWebView = mWebView;
		ConstantUtils.returnMethodName = returnMethodName;
		/* 打开默认图片选择视图 */
		Intent intent = new Intent(mActivity, IDCardActivity.class);
		intent.putExtra(ConstantUtils.ID_CARD_TYPE, "1");
		mActivity.startActivity(intent);

		msg.obj = _obj;
		msg.what = _what;
		handler.sendMessage(msg);
	}

	/***
	 * 银行卡正面拍照识别
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
	public static void bankCardOCROnline(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " bankCardOCROnline seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;

		if (!ConstantUtils.hasGotToken) {
			ToastShow.showShort(mActivity, "token还未成功获取");
			_obj = "token还未成功获取";
			_what = ConstantUtils.HANDLER_FAILE;
			return;
		}

		ConstantUtils.ChooseImgWebView = mWebView;
		ConstantUtils.returnMethodName = returnMethodName;
		/* 打开默认图片选择视图 */
		Intent intent = new Intent(mActivity, IDCardActivity.class);
		intent.putExtra(ConstantUtils.ID_CARD_TYPE, "2");
		mActivity.startActivity(intent);

		msg.obj = _obj;
		msg.what = _what;
		handler.sendMessage(msg);
	}
}
