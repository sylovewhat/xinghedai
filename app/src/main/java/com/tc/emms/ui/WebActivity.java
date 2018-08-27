package com.tc.emms.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.tc.emms.R;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.base.BaseFunction;
import com.tc.emms.bean.BaseResponse;
import com.tc.emms.config.BaseApplictaion;
import com.tc.emms.service.BusinessException;
import com.tc.emms.utils.ActivityManager;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.FileUtils;
import com.tc.emms.utils.HtmlUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.utils.SharedUtils;
import com.tc.emms.utils.StatusBarUtils;
import com.tc.emms.utils.SystemBarTintManager;
import com.tc.emms.utils.ToastShow;
import com.tc.emms.widget.X5WebView;
import com.tencent.mm.opensdk.utils.Log;

/*
 * 本地html页面
 * 功能【统一显示本地html页面】
 */
@SuppressLint({"HandlerLeak", "InlinedApi"})
public class WebActivity extends BaseActivity {
	
	private static String TAG = "WebActivity";

	private BaseActivity mActivity;
	private ViewGroup webView_frame;
	private BaseFunction mFunctionUtil;
	private String u_path = "";
	private View m_header;
	private String HTML_NAME = "";
	private String HTML_URL = "";
	/* 返回键退出 */
	private long exitTime = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = this;
		mActivity.getActivityManager().pushActivity(WebActivity.this);
		initView();
		initData();
		LogUtils.vLog(TAG, "*****************************WebActivity onCreate");
	}

	/**
	 * 显示标题栏（是否全屏）
	 */
	private void showTitleBar(){
		StatusBarUtils.setCompat(this);
		if (ConstantUtils.heardListText != null && ConstantUtils.heardListText.size() > 0) {

		    if(ConstantUtils.heardListText.get(0).getIs_ShowStatusBar().equals("0"))
		    {
			     StatusBarUtils.setStatusBarMode(this,false);
		    }else{
			     int color_value = Color.parseColor(ConstantUtils.heardListText.get(0).getmBackgroudColore());
			     StatusBarUtils.setStatusBarMode(this,true);
			     StatusBarUtils.setColor(WebActivity.this, color_value);
		}
		}
	}



	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);


	}


	@SuppressWarnings("deprecation")
	private void initView() {
		setContentView(R.layout.main_web_init);
		showTitleBar();
		m_header = findViewById(R.id.m_header);
		webView_frame = (ViewGroup) findViewById(R.id.webView_frame);
		m_webview = new X5WebView(mActivity, null);
		webView_frame.addView(m_webview, new FrameLayout.LayoutParams(
				LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		mFunctionUtil = new BaseFunction(mActivity, m_webview, m_header, null);
		m_webview.addJavascriptInterface(mFunctionUtil, ConstantUtils.HTML_KEY);
	}

	private void initData() {
		u_path = FileUtils.getU_PATH(mActivity);
		LogUtils.iLog(TAG, "innitData u_path: " + u_path);
		HTML_NAME = getIntent().getStringExtra(ConstantUtils.HTML_NAME);
		HTML_URL = getIntent().getStringExtra(ConstantUtils.HTML_URL);
		/* 查看当前html是否需要下载更新 */
		HtmlUtils.isHtmlUpdate(mActivity, HTML_NAME, update_ui_handler);
	}

	Handler update_ui_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			m_webview.setis_loaded(true);
			String _url = ConstantUtils.U_BASE + u_path + HTML_URL;
			if(ConstantUtils.IS_OPEN_DEBUG){
				/*测试环境地址*/
		    	final String local_downurl = SharedUtils.getValue(BaseApplictaion.getInstance(), ConstantUtils.UPDATE_LOCAL_MAIN);
		    	Log.d(TAG, "测试地址:" + local_downurl);
		    	_url = local_downurl + HTML_URL;
			}
			
			// 更改页面
			switch (msg.what) {
				case ConstantUtils.ACT_HTML_COM :
					
					LogUtils.iLog(TAG, "完成。WebActivity打开页面: " + _url);
					m_webview.loadUrl(_url);
					break;
				case ConstantUtils.ACT_HTML_FAILE :
					LogUtils.iLog(TAG, "下载失败。MainActivity打开默认页面: " + _url);
					m_webview.loadUrl(_url);
					break;
				default :
					break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_ok :

				break;
			default :
				break;
		}
	}

	public void login() {

	}

	@Override
	public void onSuccess(BaseResponse response, String jsonStr,  int act) {
		if (response != null && response.m != null) {

		}
	}

	@Override
	public void onError(BusinessException e, int act) {

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtils.iLog(TAG, "退出");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				exitTime = System.currentTimeMillis();
				m_webview.loadUrl("javascript:andBackbtn()");
			} else {
				LogUtils.iLog(TAG, "双击退出");
				m_webview.loadUrl("javascript:andExitbtn()");
			}
			return true;
			
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("sylove","重新回来==============================onResume");
		LogUtils.vLog(TAG, "*****************************WebActivity onResume1");
//		if(m_webview != null && m_webview.getis_loaded()){
//			LogUtils.vLog(TAG, "*****************************WebActivity onResume is_loaded:" + m_webview.getis_loaded());
//			m_webview.loadUrl("javascript:viewWillAppear()");
//		}
		if(m_webview != null ){
			m_webview.loadUrl("javascript:viewWillAppear()");
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
