package com.tc.emms.receiver;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.utils.ToastShow;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

@SuppressLint("SimpleDateFormat") 
public class MessageReceiver extends XGPushBaseReceiver {
	private Intent intent = new Intent("com.tc.emms.activity.UPDATE_LISTVIEW");
	public static final String LogTag = "TPushReceiver";

	private void show(Context context, String text) {
		/*ToastShow.showShort(context, text);*/
	}

	// 通知展示
	@Override
	public void onNotifactionShowedResult(Context context,
			XGPushShowedResult notifiShowedRlt) {
		if (context == null || notifiShowedRlt == null) {
			return;
		}
		context.sendBroadcast(intent);
		LogUtils.iLog("", "onNotifactionShowedResult 通知内容 :" + notifiShowedRlt.toString());
		
		LogUtils.iLog("", "onNotifactionShowedResult 通知  title:" + notifiShowedRlt.getTitle());
		LogUtils.iLog("", "onNotifactionShowedResult 通知内容 Content:" + notifiShowedRlt.getContent());
		LogUtils.iLog("", "onNotifactionShowedResult 通知内容 CustomContent:" + notifiShowedRlt.getCustomContent());
		if(ConstantUtils.PushWebView != null){
			LogUtils.iLog("", "onNotifactionShowedResult WebView:" + ConstantUtils.PushWebView);
			JSONObject jsonStr = new JSONObject();
			try {
				jsonStr.put("title", notifiShowedRlt.getTitle());
				jsonStr.put("Content", notifiShowedRlt.getContent());
				jsonStr.put("CustomContent", notifiShowedRlt.getCustomContent());
				LogUtils.iLog("", "onNotifactionShowedResult return jsonStr:" + jsonStr.toString());
				ConstantUtils.PushWebView.loadUrl("javascript:backMessageReceived("
						+ jsonStr + ")");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public void onUnregisterResult(Context context, int errorCode) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "反注册成功";
		} else {
			text = "反注册失败" + errorCode;
		}
		Log.d(LogTag, text);
		show(context, text);

	}

	@Override
	public void onSetTagResult(Context context, int errorCode, String tagName) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "\"" + tagName + "\"设置成功";
		} else {
			text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
		}
		Log.d(LogTag, text);
		show(context, text);

	}

	@Override
	public void onDeleteTagResult(Context context, int errorCode, String tagName) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "\"" + tagName + "\"删除成功";
		} else {
			text = "\"" + tagName + "\"删除失败,错误码：" + errorCode;
		}
		Log.d(LogTag, text);
		show(context, text);

	}

	// 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击
	@Override
	public void onNotifactionClickedResult(Context context,
			XGPushClickedResult message) {
		if (context == null || message == null) {
			return;
		}
		String text = "";
		if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
			// 通知在通知栏被点击啦。。。。。
			// APP自己处理点击的相关动作
			// 这个动作可以在activity的onResume也能监听，请看第3点相关内容
			text = "通知被打开 :" + message;
		} else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
			// 通知被清除啦。。。。
			// APP自己处理通知被清除后的相关动作
			text = "通知被清除 :" + message;
		}
		//Toast.makeText(context, "广播接收到通知被点击:" + message.toString(),
		//		Toast.LENGTH_SHORT).show();
		// 获取自定义key-value
		String customContent = message.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			try {
				JSONObject obj = new JSONObject(customContent);
				// key1为前台配置的key
				if (!obj.isNull("key")) {
					String value = obj.getString("key");
					Log.d(LogTag, "get custom value:" + value);
				}
				// ...
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// APP自主处理的过程。。。
		Log.d(LogTag, text);
		show(context, text);
	}

	@Override
	public void onRegisterResult(Context context, int errorCode,
			XGPushRegisterResult message) {
		// TODO Auto-generated method stub
		if (context == null || message == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = message + "注册成功";
			// 在这里拿token
			String token = message.getToken();
			LogUtils.dLog("TPush", "onRegisterResult 设备token为：" + token);
			/*ToastShow.showShort(context, "推送注册成功 设备token为：" + token);*/
			/*ToastUtils.getInstances().showDialog("提示", "注册成功 设备token为：" + token);*/
		} else {
			text = message + "注册失败，错误码：" + errorCode;
			LogUtils.dLog("TPush", "onRegisterResult text：" + text);
			ToastShow.showLong(context, "推送注册失败，错误码：" + errorCode);
			/*ToastUtils.getInstances().showDialog("提示", text);*/
		}
		Log.d(LogTag, text);
	}

	// 消息透传
	@Override
	public void onTextMessage(Context context, XGPushTextMessage message) {
		// TODO Auto-generated method stub
		String text = "收到消息:" + message.toString();
		// 获取自定义key-value
		String customContent = message.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			try {
				JSONObject obj = new JSONObject(customContent);
				// key1为前台配置的key
				if (!obj.isNull("key")) {
					String value = obj.getString("key");
					Log.d(LogTag, "get custom value:" + value);
				}
				// ...
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// APP自主处理消息的过程...
		Log.d(LogTag, text);
		show(context, text);
	}

}
