package com.tc.emms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.tc.emms.R;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.db.DBHelper;
import com.tc.emms.printer.PrintHelper;
import com.tc.emms.printer.PrintUtil;
import com.tc.emms.ui.MainActivity;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.utils.SharedUtils;
import com.tc.emms.utils.ToastUtils;

@SuppressLint("HandlerLeak")
public class MyMessageReceiver extends MessageReceiver {
	private String TAG = "MyMessageReceiver";

	private boolean isSpeakEnd = true;
	/* 缓存需要播报的语音 */
	private List<String> allSpeak = new ArrayList<String>();
	private boolean isPrintEnd = true;
	private boolean printCountEnd = false;
	/* 缓存需要打印的内容 */
	private List<String> allPrint = new ArrayList<String>();

	@Override
	protected void onNotificationOpened(Context paramContext,
			String paramString1, String paramString2, String paramString3) {
		LogUtils.iLog(TAG, "onNotification Opened");
		LogUtils.iLog(TAG, "onNotification title:" + paramString1 + " ; summary:"
				+ paramString2 + " ; key:" + paramString3);
	}

	@Override
	protected void onNotificationRemoved(Context paramContext,
			String paramString) {
		LogUtils.iLog(TAG, "onNotificationRemoved");
	}

	@Override
	protected void onMessage(Context paramContext,
			CPushMessage paramCPushMessage) {
		LogUtils.iLog(TAG, "onMessage");
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected void onNotification(Context context, final String title,
			final String summary, final Map<String, String> extraMap) {
		LogUtils.iLog(TAG, TAG);
		LogUtils.iLog(TAG, "onNotification title:" + title);
		LogUtils.iLog(TAG, "onNotification summary:" + summary);
		LogUtils.iLog(TAG,
				"onNotification extraMap:" + extraMap.toString());

		/* 全局弹窗显示通知 */
		/*ToastUtils.getInstances().showDialog(title, summary);*/
		
		
		if(ConstantUtils.PushWebView != null){
			LogUtils.iLog(TAG, "onNotification WebView:" + ConstantUtils.PushWebView);
			JSONObject jsonStr = new JSONObject();
			try {
				jsonStr.put("title", title);
				jsonStr.put("summary", summary);
				for (Map.Entry entry : extraMap.entrySet()) {
					String key = entry.getKey().toString();
					String value = entry.getValue().toString();
					jsonStr.put(key, value);
				}
				/*jsonStr.put("key", _key);*/
				LogUtils.iLog(TAG, "onNotification return jsonStr:" + jsonStr.toString());
				ConstantUtils.PushWebView.loadUrl("javascript:backMessageReceived("
						+ jsonStr + ")");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			
			/*       使用原生存储            */
			LogUtils.iLog(TAG, "onNotification 使用原生存储: WebView == null");
			/*ToastShow.showShort(context, "使用原生存储");*/
			
			String m_name = context.getResources().getString(
					R.string.main_tip_notice);
			String member_id = SharedUtils.getValue(context,
					ConstantUtils.MEMBER_ID);
			LogUtils.eLog(TAG, "onNotification begin save DB...");
			for (Map.Entry entry : extraMap.entrySet()) {
				Object _value;
				Object key = entry.getKey();
				LogUtils.iLog(TAG, "onNotification key:" + key);
				if (key != null && key.equals("key")) {
					_value = entry.getValue();
					LogUtils.iLog(TAG, "onNotification _value:" + _value);
					if (_value != null && !_value.equals("")) {
						JSONObject jsonObj;
						try {
							jsonObj = new JSONObject(_value.toString());
							LogUtils.iLog(TAG, "onNotification jsonObj:"
									+ jsonObj);
							// 通知标题
							LogUtils.iLog(TAG, "onNotification title:" + title);
							// 通知内容
							String sub_title = jsonObj.getString("sub_title");
							// 通知额外参数
							String o = jsonObj.getString("o");
							String dt = jsonObj.getString("dt");
							String memo = jsonObj.getString("memo");
							// String news_flag = jsonObj.getString("news_flag");
							String news_type = jsonObj.getString("news_type");
							/* 保存到数据库 */
							/* 插入数据库 */
							Map<String, String> mapValues = new HashMap<String, String>();
							mapValues.put("dt", dt);
							mapValues.put("news_flag", "0");
							mapValues.put("sub_title", sub_title);
							mapValues.put("title", title);
							mapValues.put("summary", summary);
							mapValues.put("news_type", news_type);
							mapValues.put("o", o);
							mapValues.put("memo", memo);
							mapValues.put("member_id", member_id);
							DBHelper dbHelper = new DBHelper(context);
							boolean isSaveOK = dbHelper.saveCell(
									ConstantUtils.TABLE_NAME_NEWS, mapValues);
							if (!isSaveOK) {
								LogUtils.iLog(TAG, "onNotification seccess save DB...: "
										+ isSaveOK);
							}

							SharedUtils.setValue(context,
									ConstantUtils.IS_MAIN_REFRESH, "1");
							SharedUtils.setValue(context,
									ConstantUtils.IS_SJ_REFRESH, "1");
							SharedUtils.setValue(context,
									ConstantUtils.IS_TZ_REFRESH, "1");
							SharedUtils.setValue(context,
									ConstantUtils.IS_ME_REFRESH, "1");
							Handler mHandler = MainActivity.notice_ui_handler;
							if (mHandler != null) {
								Message msg = mHandler.obtainMessage();
								msg.what = ConstantUtils.ACT_NOTICE_NORMAIL;
								mHandler.sendMessage(msg);
							} else {
								LogUtils.iLog(TAG, "null mHandler:" + null);
							}

							/* 这是语言合成部分 */
							startSpeak(m_name + title);

							/* 自动打印 */
							LogUtils.iLog(TAG, "memo:" + memo);
							startPrint(context, memo);
						} catch (JSONException e) {
							e.printStackTrace();
							LogUtils.iLog(TAG,
									"onNotification JSONException:error");
						}
					}
				}
			}
		}
	}

	@Override
	protected void onNotificationReceivedInApp(Context paramContext,
			String paramString1, String paramString2,
			Map<String, String> paramMap, int paramInt, String paramString3,
			String paramString4) {
		LogUtils.iLog(TAG, "onNotificationReceivedInApp");
	}

	@Override
	protected void onNotificationClickedWithNoAction(Context paramContext,
			String paramString1, String paramString2, String paramString3) {
		LogUtils.iLog(TAG, "onNotificationClickedWithNoAction");
	}

	/* 语音播报 */
	private void startSpeak(String _text) {
		if (isSpeakEnd) {
			isSpeakEnd = false;
			int code = BaseActivity.mTts.startSpeaking(_text, mTtsListener);
			LogUtils.eLog(TAG, "语言合成！开始播报 code：" + code);
			if (code != ErrorCode.SUCCESS) {
				if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
					/* 未安装则跳转到提示安装页面 */
					// mInstaller.install();
				} else {
					/* "语音合成失败,错误码: " + code */
				}
			}
		} else {
			LogUtils.eLog(TAG, "语言合成！正在播报 中,添加：" + _text);
			allSpeak.add(_text);
		}
	}

	/* 打印 */
	private void startPrint(Context context, String _text) {
		LogUtils.iLog(TAG, "onNotification startPrint isPrintEnd:" + isPrintEnd);
		if (isPrintEnd) {
			isPrintEnd = false;
			_text = _text.replace("<br>", "\r\n");
			LogUtils.iLog(TAG, "_text:" + _text);
			/* 自动打印 */
			print_memo(context, _text);
		} else {
			LogUtils.eLog(TAG, "onNotification 自动打印！正在打印中,添加：" + _text);
			allPrint.add(_text);
		}
	}

	private void print_memo(Context context, String memo) {
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

	Handler print_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 更改页面
			switch (msg.what) {
				case 0 :
					LogUtils.iLog(TAG, "onNotification 打印失败");
					String _msg = "打印失败: " + (String) msg.obj;
					/* 全局弹窗显示通知 */
					ToastUtils.getInstances().showDialog("提示", _msg);
					break;
				case 1 :

					// ToastView.showToastShort("打印成功");
					LogUtils.eLog(TAG, "onNotification 打印成功");
					if (printCountEnd) {
						printCountEnd = false;
						LogUtils.iLog(TAG, "onNotification 最后一联打印完成:" + printCountEnd);
						isPrintEnd = true;
						if (allPrint.size() > 0) {
							startPrint(BaseActivity.mActivity, allPrint.get(0));
							allPrint.remove(0);
						}
					}

					break;
				default :
					break;
			}
		}
	};

	/**
	 * 合成回调监听。
	 */
	private SynthesizerListener mTtsListener = new SynthesizerListener() {
		@Override
		public void onSpeakBegin() {
			/* 开始播放 */
		}

		@Override
		public void onSpeakPaused() {
			/* 暂停播放 */
		}

		@Override
		public void onSpeakResumed() {
			/* 继续播放 */
		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos,
				String info) {
			/* 合成进度 percent */
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
			/* 播放进度 percent */
		}

		@Override
		public void onCompleted(SpeechError error) {
			if (error == null) {
				/* 播放完成 */
			} else if (error != null) {
				/* 播放失败 */
			}
			isSpeakEnd = true;
			if (allSpeak.size() > 0) {
				startSpeak(allSpeak.get(0));
				allSpeak.remove(0);
			}
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

		}
	};
}
