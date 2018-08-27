package com.tc.emms.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tc.emms.R;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.dialog.CommonData;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.LogUtils;
import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

@SuppressLint({"ClickableViewAccessibility", "InflateParams", "InlinedApi"})
public class X5WebView extends WebView {

	private BaseActivity mActivity;
	private FrameLayout mFrameLayout;
	public X5WebView(BaseActivity context) {
		super(context);
		mActivity = context;
	}

	@SuppressLint("SetJavaScriptEnabled")
	public X5WebView(BaseActivity context, AttributeSet arg1) {
		super(context, arg1);
		mActivity = context;
		this.setWebViewClient(client);
		initWebViewSettings();
		/**
		 * Webview在安卓5.0之前默认允许其加载混合网络协议内容
		 * 在安卓5.0之后，默认不允许加载http与https混合内容，需要设置webview允许其加载混合网络协议内容
		 */
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			this.getSettings().setMixedContentMode(
					android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

		}
		this.getView().setClickable(true);
		this.setWebChromeClient(chromeClient);
		this.setBackgroundColor(getResources().getColor(R.color.main_backgroud));
		this.setHorizontalScrollBarEnabled(false);
		this.setVerticalScrollBarEnabled(false);
		IX5WebViewExtension ix5 = this.getX5WebViewExtension();
        if (null != ix5) {
            ix5.setScrollBarFadingEnabled(false);
        }
		/* 截取webView可视区域的截图 */
		/* this.setDrawingCacheEnabled(true); */
		ViewParent viewParent = getParent();
		mFrameLayout = (FrameLayout) viewParent;
		LogUtils.vLog("", "X5webview init mFrameLayout:" + mFrameLayout);
	}

	private WebViewClient client = new WebViewClient() {
		/**
		 * 防止加载网页时调起系统浏览器
		 */
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			LogUtils.iLog("", "X5webview shouldOverrideUrlLoading url:" + url);
			if (url.startsWith("tel:")) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
				mActivity.startActivity(intent);
				return true;
			} else {

				view.loadUrl(url);
			}
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			LogUtils.iLog("", "X5webview onPageStarted");
			/*ToastUtils.getInstances().showWaittingDialog();*/
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			LogUtils.iLog("", "X5webview onPageFinished");
			/*ToastUtils.getInstances().dissWaittingDialog();*/

			CookieManager cookieManager = CookieManager.getInstance();
			final String CookieStr = cookieManager.getCookie(url);
			if (CookieStr != null) {
				LogUtils.iLog("", "onPageFinished cookit: " + CookieStr);
				boolean isReturn = false;
				String[] cookies = CookieStr.split(";");
				/* 返回结果 */
				JSONObject new_json = new JSONObject();
				for (int i = 0; i < cookies.length; i++) {
					String item = cookies[i];
					int index = item.indexOf("=");
					String itemKey = item.substring(0, index).trim();
					LogUtils.iLog("", "onPageFinished-cookit itemKey: "
							+ itemKey);
					String itemValue = item.substring(index + 1).trim();
					LogUtils.iLog("", "onPageFinished-cookit itemValue: "
							+ itemValue);
					new_json.put(itemKey, itemValue);

					if (itemKey != null && itemKey.contains("merName")) {
						LogUtils.eLog("", "onPageFinished cookit openId ok: 返回");
						isReturn = true;
					}
				}
				if (isReturn) {
					ConstantUtils.CookieStr = new_json.toString();
					mActivity.finish();
				}

			} else {
				LogUtils.iLog("", "onPageFinished cookit: null" + CookieStr);
			}
		}

		/**
		 * 显示错误页面
		 */
		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {

			// view.loadUrl(url);
		};

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {

			// 不要使用super，否则有些手机访问不了，因为包含了一条 handler.cancel()
			// super.onReceivedSslError(view, handler, error);

			// 接受所有网站的证书，忽略SSL错误，执行访问网页
			handler.proceed();
		}
	};

	private WebChromeClient chromeClient = new WebChromeClient() {
		@Override
		public boolean onJsConfirm(WebView arg0, String arg1, String arg2,
				JsResult arg3) {
			return super.onJsConfirm(arg0, arg1, arg2, arg3);
		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				final JsResult result) {

			Log.i("", "X5webview onJsAlert message：" + message);
			/**
			 * 自定义的window alert
			 */
			/*ToastUtils.getInstances().showDialog("提示", message);*/
			Log.i("", "X5webview onJsAlert mActivity：" + mActivity);
			Log.i("", "X5webview onJsAlert CommonData.mNowContext：" + CommonData.mNowContext);
			if(CommonData.mNowContext != null){
				final Dialog dialog = new Dialog(CommonData.mNowContext,
						R.style.Theme_Transparent);
				View v = ((BaseActivity)CommonData.mNowContext).getLayoutInflater().inflate(
						R.layout.dialog_simple_tip, null);
				TextView tv_tip = (TextView) v.findViewById(R.id.tv_tip);
				tv_tip.setText(message);
				dialog.setContentView(v);
				dialog.getWindow().setGravity(Gravity.CENTER);
				dialog.getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
						android.view.ViewGroup.LayoutParams.MATCH_PARENT);
				v.findViewById(R.id.btn_ok).setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								result.confirm();
							}
						});
				dialog.show();
			}

			
			return true;
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
			/* 设置进度条 */
			if (newProgress >= 100) {

			} else {

			}
		}

		private View mCustomView;
		private CustomViewCallback mCustomViewCallback;
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			super.onShowCustomView(view, callback);
			if (mCustomView != null) {
				callback.onCustomViewHidden();
				return;
			}
			mCustomView = view;
			if(mFrameLayout == null){
				callback.onCustomViewHidden();
				LogUtils.vLog("", "X5webview onShowCustomView mFrameLayout: null");
				return;
			}
			mFrameLayout.addView(mCustomView);
			mCustomViewCallback = callback;
			setVisibility(View.GONE);
			mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}

		@Override
		public void onHideCustomView() {
			setVisibility(View.VISIBLE);
			if (mCustomView == null) {
				return;
			}
			mCustomView.setVisibility(View.GONE);
			if(mFrameLayout == null){
				LogUtils.vLog("", "X5webview onShowCustomView mFrameLayout: null");
				return;
			}
			mFrameLayout.removeView(mCustomView);
			mCustomViewCallback.onCustomViewHidden();
			mCustomView = null;
			mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			super.onHideCustomView();
		}
	};

	@SuppressLint("SetJavaScriptEnabled")
	private void initWebViewSettings() {
		WebSettings webSetting = this.getSettings();
		webSetting.setJavaScriptEnabled(true);
		webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
		webSetting.setAllowFileAccess(true);
		webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webSetting.setSupportZoom(true);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setUseWideViewPort(true);
		webSetting.setSupportMultipleWindows(true);
		webSetting.setLoadWithOverviewMode(true);
		webSetting.setAppCacheEnabled(true);
		// webSetting.setDatabaseEnabled(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setGeolocationEnabled(true);
		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
		// webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
		// webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
		webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
	}

	/* 统一的加载url页面 */
	public void loadLocalUrl(BaseActivity mActivity, String _url) {
		this.mActivity = mActivity;

	}
	
	public void loadLocalUrl(BaseActivity mActivity) {
		this.mActivity = mActivity;

	}

	/* 判断是否加载 */
	boolean is_loaded = false;
	public void setis_loaded(boolean type) {
		is_loaded = type;
	}

	public boolean getis_loaded() {
		return is_loaded;
	}
}
