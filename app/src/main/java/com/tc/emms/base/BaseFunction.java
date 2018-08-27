package com.tc.emms.base;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.utils.SharedUtils;
import com.tc.emms.utils.ToastShow;
import com.tc.emms.widget.X5WebView;

/**
 * html执行事件 统一管理 by xiaoben
 */
@SuppressLint({ "SetJavaScriptEnabled", "InflateParams" })
public class BaseFunction {
	
	private static String TAG = "BaseFunction";

	private X5WebView webView;
	private BaseActivity mActivity;
	/* 用来自定义的页面header */
	public View header_view;
	/* 用来自定义的页面footer */
	public View footer_view;

	public BaseFunction(BaseActivity mContext, X5WebView myWebView,
			View _headerView, View _footerView) {
		super();
		this.mActivity = mContext;
		this.webView = myWebView;
		header_view = _headerView;
		footer_view = _footerView;
	}

	/***
	 * html统一调用原生功能接口
	 * 
	 * @param pkgName
	 *            类名
	 * @param methodName
	 *            方法名
	 * @param jsonString
	 *            字符串参数
	 * @param returnMethodName
	 *            回调方法
	 */
	@JavascriptInterface
	public void unifyClassMethod(final String pkgName, final String methodName,
			final String jsonString, final String returnMethodName) {
		/*if("pushRefreshLoadView".equals(methodName)){
			ToastUtils.getInstances().showDialog("提示", "start pushRefreshLoadView");
		}*/
		
		LogUtils.eLog(TAG, "***************** start getMethodInfo *****************");
		mActivity.getMethodInfo(ConstantUtils.HTML_CLASS_BEGIN
				+ pkgName, methodName, jsonString, returnMethodName,
				mActivity, webView, header_view, footer_view, handler);
		
		
		/*webView.post(new Runnable() {
			@Override
			public void run() {
				LogUtils.eLog(TAG, "***************** start webView run *****************");
				mActivity.getMethodInfo(ConstantUtils.HTML_CLASS_BEGIN
						+ pkgName, methodName, jsonString, returnMethodName,
						mActivity, webView, header_view, footer_view, handler);
			}
		});*/
	}

	/**
	 * 方法库执行 回调信息
	 */
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ConstantUtils.HANDLER_SECCESS:
				// TODO 成功执行方法库
				LogUtils.vLog(TAG, " ********* 方法库执行 回调信息 :" + msg.obj);
				break;
			case ConstantUtils.HANDLER_FAILE:
				// TODO 失败
				String logToast = SharedUtils.getValue(mActivity, ConstantUtils.LOG_SWICH);
				if(logToast.equals("1")){
					ToastShow.showShort(mActivity, msg.obj.toString());
				}
				break;
			}
		}
	};
}
