package com.tc.emms.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.tc.emms.R;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.base.BaseFunction;
import com.tc.emms.bean.BaseResponse;
import com.tc.emms.service.BusinessException;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.FileUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.utils.SharedUtils;
import com.tc.emms.utils.StatusBarUtils;
import com.tc.emms.utils.ToastShow;
import com.tc.emms.widget.X5WebView;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
/*
 * 显示第三方网页链接页面
 * 功能【显示第三方网页、扫码进件】
 */
@SuppressLint({"SetJavaScriptEnabled", "HandlerLeak", "InflateParams",
		"InlinedApi", "NewApi"})
public class InitWebActivity extends BaseActivity implements OnClickListener {
	private static String TAG = "InitWebActivity";

	private String POSITION = "";
	private String TITLE = "";
	private String u_path = "";
	private ValueCallback<Uri> mUploadMessage;
	public ValueCallback<Uri[]> uploadMessage;
	public static final int REQUEST_SELECT_FILE = 100;
	private final static int FILECHOOSER_RESULTCODE = 2;
	private TextView tv_title;
	private ImageView iv_back;
	private BaseFunction mFunctionUtil;
	private ViewGroup webView_frame;
	private View m_header;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//			//获取状态栏颜色
//			int status_bar_color = SharedUtils.getInt(this, ConstantUtils.APP_BACKGROUD_COLOR);
//		    //获取状态栏颜色
//		    int full_screen_value = SharedUtils.getInt(this, ConstantUtils.APP_fULL_SCREEN);
//		    Log.d("sylove","全屏变化值"+full_screen_value);
//		    StatusBarUtils.setCompat(this);
//		    StatusBarUtils.setStatusBarMode(this,true);
//		    if(full_screen_value==1)
//			{
//				Log.d("sylove","全屏");
//				//full_screen_value==1表示全屏
//				StatusBarUtils.setStatusBarMode(this,false);
//			}else{
//				Log.d("sylove","非全屏");
//				StatusBarUtils.setStatusBarMode(this,true);
//				StatusBarUtils.setColor(this, status_bar_color);
//			}
		innitView();
		innitData();
		LogUtils.vLog(TAG, "InitWebActivity onCreate ");
	}

	@SuppressWarnings("deprecation")
	private void innitView() {
		setContentView(R.layout.main_web_init);
		m_header = findViewById(R.id.m_header);

		tv_title = (TextView) findViewById(R.id.tv_title);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setImageDrawable(getResources().getDrawable(R.drawable.goback));
		iv_back.setVisibility(View.VISIBLE);
		iv_back.setOnClickListener(this);
		LogUtils.vLog(TAG, "InitWebActivity setVisibility ");
		webView_frame = (ViewGroup) findViewById(R.id.webView_frame);
		m_webview = new X5WebView(mActivity, null);
		webView_frame.addView(m_webview, new FrameLayout.LayoutParams(
				LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
        //BaseFunction类就是从这里开始引用开始的
		mFunctionUtil = new BaseFunction(mActivity, m_webview, m_header, null);
		//将对象mFunctionUtil封装后给html里面调用，我们在html里面看到的APPJS   实际上就是ConstantUtils.HTML_KEY这个的值
		m_webview.addJavascriptInterface(mFunctionUtil, ConstantUtils.HTML_KEY);
	}

	private void innitData() {
		u_path = FileUtils.getU_PATH(InitWebActivity.this);
		LogUtils.iLog(TAG, "innitData: " + u_path);
		POSITION = getIntent().getStringExtra(ConstantUtils.HTML_URL);
		TITLE = getIntent().getStringExtra(ConstantUtils.HTML_NAME);
		if (TITLE != null && !TITLE.equals("")) {
			tv_title.setText(TITLE);
		} else {
			/* 设置默认标题 */
			tv_title.setText("网页链接");
		}

		String is_check = getIntent().getStringExtra(ConstantUtils.IS_CHECK);
		if (is_check != null && is_check.equals("0")) {
			LogUtils.iLog(TAG, "扫码打开页面: " + POSITION);
			m_webview.loadUrl(POSITION);
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_back :
				finish();
				break;
			default :
				break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean isCanBack = SharedUtils.getBooleanValue(this, ConstantUtils.IS_OPEN_BACK, true);
		LogUtils.vLog(TAG, "*****************************onKeyDown1："+isCanBack);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			boolean isCanBack = SharedUtils.getBooleanValue(this, ConstantUtils.IS_OPEN_BACK, true);
			if (isCanBack) {
				return true;
			} else {
				finish();
			}
			
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onSuccess(BaseResponse response, String jsonStr, int act) {

	}

	@Override
	public void onError(BusinessException e, int act) {

	}

	@SuppressLint("NewApi")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			if (requestCode == REQUEST_SELECT_FILE) {
				if (uploadMessage == null)
					return;
				uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams
						.parseResult(resultCode, intent));
				uploadMessage = null;
			}
		} else if (requestCode == FILECHOOSER_RESULTCODE) {
			if (null == mUploadMessage)
				return;
			// Use MainActivity.RESULT_OK if you're implementing WebView inside
			// Fragment
			// Use RESULT_OK only if you're implementing WebView inside an
			// Activity
			Uri result = intent == null || resultCode != Activity.RESULT_OK
					? null
					: intent.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
		} else
			ToastShow.showShort(getBaseContext(), "Failed to Upload Image");
	}
}
