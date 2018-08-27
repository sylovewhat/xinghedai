package com.tc.emms.base;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechSynthesizer;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tc.emms.R;
import com.tc.emms.bean.BaseResponse;
import com.tc.emms.service.BusinessResolver.BusinessCallback;
import com.tc.emms.utils.ActivityManager;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.utils.SharedUtils;
import com.tc.emms.utils.ToastShow;
import com.tc.emms.widget.X5WebView;

/*
 * xiaoben
 * 2013-8-12 xiaoben7610@outlook.com
 */
public abstract class BaseActivity extends Activity
		implements
			OnClickListener,
			BusinessCallback<BaseResponse> {
	private static final String TAG = "BaseActivity Init";
	public X5WebView m_webview = null;
	public static BaseActivity mActivity;
	private ActivityManager activityManager = null; // activity管理类
	// 语音合成对象
	public static SpeechSynthesizer mTts;
	/* 注册微信分享 */
	private IWXAPI api;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ConstantUtils.PushWebView=m_webview;
		mActivity = this;
		if (activityManager == null) {
			activityManager = ActivityManager.getInstance(); // 获得实例
		}
		/* 语音合成对象 */
		mTts = SpeechSynthesizer.createSynthesizer(mActivity, mTtsInitListener);

		/* 注册微信分享 */
		LogUtils.dLog(TAG, "init weixin share.");
		api = WXAPIFactory.createWXAPI(this, ConstantUtils.WEIXIN_ID, true);
		api.registerApp(ConstantUtils.WEIXIN_ID);
		if (ConstantUtils.REGIST_OCR) {
			/* 百度文字识别 */
			initAccessTokenWithAkSk();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public ActivityManager getActivityManager() {
		return activityManager;
	}

	/**
	 * 传入全类名获得对应类中所有方法名和参数名
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void getMethodInfo(String pkgName, String methodName,
			String jsonString, String returnMethodName, BaseActivity activity,
			X5WebView webView, View headerView, View footerView,
			Handler mHandler) {
		LogUtils.eLog(TAG, ConstantUtils.HTML_LOG + " 类名称:" + pkgName);
		LogUtils.eLog(TAG, ConstantUtils.HTML_LOG + " 方法名称:" + methodName);
		LogUtils.eLog(TAG, ConstantUtils.HTML_LOG + " Json字符串:" + jsonString);
		boolean isSeccess = true;
		String _obj = " getMethodInfo seccess.";
		try {
			/* 获取类名称 */
			Class ServiceManager = Class.forName(pkgName);
			/* 获取方法名为showName，参数为String , String , BaseActivity , WebView 类型的方法 */
			Method method = ServiceManager.getDeclaredMethod(methodName,
					new Class[]{String.class, String.class, BaseActivity.class,
							X5WebView.class, View.class, View.class,
							Handler.class});
			/* 实例化对象 */
			Object methodobject1 = ServiceManager.newInstance();
			/* 若调用私有方法，必须抑制java对权限的检查 */
			// method.setAccessible(true);
			/* 使用invoke调用方法，并且获取方法的返回值，传入参数:json,返回方法名,activity,webview */
			method.invoke(methodobject1, jsonString, returnMethodName,
					activity, webView, headerView, footerView, mHandler);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			LogUtils.eLog(TAG, "NoSuchMethodException 没有找到方法:" + e.getMessage());
			isSeccess = false;
			_obj = activity.getResources().getString(R.string.invoke_error01)
					+ ":" + methodName;

		} catch (SecurityException e) {
			e.printStackTrace();
			LogUtils.eLog(TAG, "SecurityException 没有权限:" + e.getMessage());
			isSeccess = false;
			_obj = activity.getResources().getString(R.string.invoke_error02);

		} catch (IllegalAccessException e) {
			e.printStackTrace();
			LogUtils.eLog(TAG,
					"IllegalAccessException 没有权限调用私有方法:" + e.getMessage());
			isSeccess = false;
			_obj = activity.getResources().getString(R.string.invoke_error03);

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			LogUtils.eLog(TAG,
					"IllegalArgumentException 参数错误，数据异常:" + e.getMessage());
			isSeccess = false;
			_obj = activity.getResources().getString(R.string.invoke_error04);

		} catch (InvocationTargetException e) {
			e.printStackTrace();
			LogUtils.eLog(
					TAG,
					"InvocationTargetException 被调用方法内部未被捕获的异常:"
							+ e.getMessage());
			isSeccess = false;
			_obj = activity.getResources().getString(R.string.invoke_error05);

		} catch (Exception e) {
			LogUtils.eLog(TAG, "Exception :" + e.getMessage());
			isSeccess = false;
			_obj = activity.getResources().getString(R.string.invoke_error)
					+ ":" + pkgName;

		}
		/* 方法执行失败 */
		if (!isSeccess) {
			Message msg = new Message();
			msg.obj = _obj;
			msg.what = ConstantUtils.HANDLER_FAILE;
			mHandler.sendMessage(msg);
		}
	}

	/* 是否需要更新HTML */
	public void isHtmlUpdate() {

	}

	/* 微信分享获取 */
	public IWXAPI getShareApi() {
		return api;
	}

	/*public void showWaittingDialog() {
		ToastUtils.getInstances().showWaittingDialog();
	}

	public void dissWaittingDialog() {
		ToastUtils.getInstances().dissWaittingDialog();
	}*/

	/**
	 * 初始化监听。
	 */
	private InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			LogUtils.dLog(TAG, "语音合成初始化 InitListener init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				ToastShow.showShort(mActivity, "语音合成初始化失败,错误码：" + code);
				SharedUtils.setValue(mActivity, ConstantUtils.MSC_SPEAK, "0");
			} else {
				// 初始化成功，之后可以调用startSpeaking方法
				// 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
				// 正确的做法是将onCreate中的startSpeaking调用移至这里
				LogUtils.dLog(TAG, "语音合成初始化 InitListener init() SUCCESS");
				SharedUtils.setValue(mActivity, ConstantUtils.MSC_SPEAK, "1");
			}
		}
	};

	private void initAccessTokenWithAkSk() {
		OCR.getInstance().initAccessTokenWithAkSk(
				new OnResultListener<AccessToken>() {
					@Override
					public void onResult(AccessToken result) {
						String token = result.getAccessToken();
						ConstantUtils.hasGotToken = true;
						LogUtils.dLog(TAG,
								"initAccessTokenWithAkSk init() SUCCESS:"
										+ token);
					}

					@Override
					public void onError(OCRError error) {
						error.printStackTrace();
						LogUtils.dLog(TAG, "OCR SK方式获取token失败 init() Faile:"
								+ error.getMessage());
					}
				}, getApplicationContext(), ConstantUtils.API_KEY,
				ConstantUtils.SECRET_KEY);
	}
}
