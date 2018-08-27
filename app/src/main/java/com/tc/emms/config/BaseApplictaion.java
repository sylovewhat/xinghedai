package com.tc.emms.config;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.tc.emms.bean.BaseResponse;
import com.tc.emms.dialog.CommonData;
import com.tc.emms.dialog.CommonDialogService;
import com.tc.emms.service.BusinessException;
import com.tc.emms.service.BusinessResolver.BusinessCallback;
import com.tc.emms.service.PayeeBusines;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.utils.SharedUtils;
import com.tc.emms.utils.ToastShow;
import com.tc.emms.utils.ToastUtils;
import com.iflytek.cloud.SpeechUtility;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.smtt.sdk.QbSdk;

@SuppressLint("NewApi")
public class BaseApplictaion extends Application
		implements
			Application.ActivityLifecycleCallbacks {

	private static BaseApplictaion instance;
	private static Handler mHandler;
	private static final String TAG = "BaseApplictaion Init";
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		initCloudChannel(this);
		initTBS();
		SpeechUtility.createUtility(this, ConstantUtils.XUNFEI_ID);

		this.registerActivityLifecycleCallbacks(this);// 注册
		CommonData.applicationContext = this;
		DisplayMetrics metric = new DisplayMetrics();
		WindowManager mWindowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		mWindowManager.getDefaultDisplay().getMetrics(metric);
		CommonData.ScreenWidth = metric.widthPixels; // 屏幕宽度（像素）
		Intent dialogservice = new Intent(this, CommonDialogService.class);
		startService(dialogservice);

		XGPushConfig.enableDebug(this, true);
		initXgPush();
	}

	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
		if (activity.getParent() != null) {
			CommonData.mNowContext = activity.getParent();
		} else {
			CommonData.mNowContext = activity;
		}
	}

	@Override
	public void onActivityStarted(Activity activity) {
		if (activity.getParent() != null) {
			CommonData.mNowContext = activity.getParent();
		} else {
			CommonData.mNowContext = activity;
		}
	}

	@Override
	public void onActivityResumed(Activity activity) {
		if (activity.getParent() != null) {
			CommonData.mNowContext = activity.getParent();
		} else {
			CommonData.mNowContext = activity;
		}
	}

	@Override
	public void onActivityPaused(Activity activity) {
		ToastUtils.getInstances().cancel();
	}

	@Override
	public void onActivityStopped(Activity activity) {

	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

	}

	@Override
	public void onActivityDestroyed(Activity activity) {

	}

	/**
	 * 初始化云推送通道
	 * 
	 * @param applicationContext
	 */
	private void initCloudChannel(final Context applicationContext) {
		PushServiceFactory.init(applicationContext);
		final CloudPushService pushService = PushServiceFactory
				.getCloudPushService();
		pushService.register(applicationContext, new CommonCallback() {
			@Override
			public void onSuccess(String response) {
				LogUtils.dLog(TAG, "init cloudchannel success");
				final String deviceId = pushService.getDeviceId();
				LogUtils.eLog(TAG, "onNotification cloudchannel devicedId: "
						+ deviceId);
				SharedUtils.setValue(applicationContext,
						ConstantUtils.ALI_DEVICE_ID, deviceId);

				LogUtils.dLog(TAG, "init cloudchannel start binding。。。");
				String MEMBER_ID = SharedUtils.getValue(applicationContext,
						ConstantUtils.MEMBER_ID);
				String S_ID = SharedUtils.getValue(applicationContext,
						ConstantUtils.S_ID);
				String I_ID = SharedUtils.getValue(applicationContext,
						ConstantUtils.I_ID);

				JSONObject jObject = new JSONObject();
				jObject.put("a", "tuisong");
				jObject.put("c", deviceId);
				jObject.put("ct", ConstantUtils.PUSH_DEVICE_TYPE);
				jObject.put("mi", MEMBER_ID);
				jObject.put("si", S_ID);
				jObject.put("i", I_ID);
				jObject.put("state", "0");
				/* 软件版本 */
				jObject.put("v", ConstantUtils.APP_VERSION_TYPE);

				PayeeBusines.post_action_rsa(applicationContext, jObject,
						new BusinessCallback<BaseResponse>() {

							@Override
							public void onSuccess(BaseResponse response,
									String jsonStr, int act) {
								LogUtils.eLog(TAG,
										"init cloudchannel success -- 绑定服务器成功");
								SharedUtils.setValue(applicationContext,
										ConstantUtils.REGIST_SERVER_PUSHID,
										deviceId);
							}

							@Override
							public void onError(BusinessException e, int act) {
								LogUtils.dLog(TAG,
										"init cloudchannel failed -- 绑定服务器失败");
							}
						});
				/* 绑定服务器推送 */
				/*
				 * String jsonString = "{\"a\":\"tuisong\",\"c\":\"" + deviceId
				 * + "\",\"ct\":\"" + ConstantUtils.PUSH_DEVICE_TYPE +
				 * "\",\"mi\":\"" + MEMBER_ID + ",\"si\":\"" + S_ID +
				 * "\",\"v\":\"" + ConstantUtils.APP_VERSION_TYPE +
				 * "\",\"i\":\"" + I_ID + "\",\"state\":\"0\",\"post_url\":\"" +
				 * ConstantUtils.SERVER_ADDRESSTES + "\",\"post_key\":\"s\"}";
				 */

			}
			@Override
			public void onFailed(String errorCode, String errorMessage) {
				LogUtils.dLog(TAG, "init cloudchannel failed -- errorcode:"
						+ errorCode + " -- errorMessage:" + errorMessage);
			}
		});
	}
	/**
	 * 初始化TBS浏览服务X5内核
	 */
	private void initTBS() {
		// 搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
		QbSdk.setDownloadWithoutWifi(true);// 非wifi条件下允许下载X5内核
		QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

			@Override
			public void onViewInitFinished(boolean arg0) {
				// x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
				LogUtils.dLog(TAG, "init QbSdk onViewInitFinished is " + arg0);
			}

			@Override
			public void onCoreInitFinished() {
			}
		};
		// x5内核初始化接口
		QbSdk.initX5Environment(getApplicationContext(), cb);
	}

	private void initXgPush() {
		XGPushManager.registerPush(this, new XGIOperateCallback() {
			@Override
			public void onSuccess(Object data, int flag) {
				// token在设备卸载重装的时候有可能会变
				LogUtils.dLog("TPush", "TPush 注册成功，设备token为：" + data);
				
				Log.i("", "XGPushManager bindAccount CommonData.mNowContext：" + CommonData.mNowContext);
				/*if(CommonData.mNowContext != null){
					final Dialog dialog = new Dialog(CommonData.mNowContext,
							R.style.Theme_Transparent);
					View v = ((BaseActivity)CommonData.mNowContext).getLayoutInflater().inflate(
							R.layout.dialog_simple_tip, null);
					TextView tv_tip = (TextView) v.findViewById(R.id.tv_tip);
					tv_tip.setText("推送注册成功，设备token为：" + data);
					dialog.setContentView(v);
					dialog.getWindow().setGravity(Gravity.CENTER);
					dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT);
					v.findViewById(R.id.btn_ok).setOnClickListener(
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
								}
							});
					dialog.show();
				}*/
			}
			@Override
			public void onFail(Object data, int errCode, String msg) {
				LogUtils.dLog("TPush", "TPush 注册失败，错误码：" + errCode + ",错误信息：" + msg);
				
				ToastShow.showShort(instance, "注册失败，错误码：" + errCode + ",错误信息：" + msg);
			}
		});
	}

	public static BaseApplictaion getInstance() {
		return instance;
	}

	public static Handler getHandler() {
		if (mHandler == null) {
			mHandler = new Handler(Looper.getMainLooper());
		}
		return mHandler;
	}
}
