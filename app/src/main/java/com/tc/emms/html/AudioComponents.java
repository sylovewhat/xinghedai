package com.tc.emms.html;

import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tc.emms.R;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.widget.X5WebView;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;

/*
 * 语音合成类 
 */

public class AudioComponents {

	private static String TAG = "AudioComponents";
	/***
	 * 文字转语音播放声音
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
	static String isPush = "0";
	@SuppressWarnings("static-access")
	public static void playMusic(String jsonString, final String returnMethodName,
			final BaseActivity mActivity, final X5WebView mWebView,
			View headerView, View footerView, final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " playMusic seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		try {
			String musci = "";
			String rate = "";
			
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && keyString.equals("musci")) {
					musci = valueString;
					LogUtils.vLog(TAG, "--- playMusic musci:" + musci);
				}
				if (keyString != null && keyString.equals("rate")) {
					rate = valueString;
					LogUtils.vLog(TAG, "--- playMusic rate:" + rate);
				}
				if (keyString != null && keyString.equals("isPush")) {
					isPush = valueString;
					LogUtils.vLog(TAG, "--- playMusic isPush:" + isPush);
				}
			}
			if (!musci.equals("")) {
				/* 这是语言合成部分 */
				LogUtils.eLog(TAG, "playMusic start mTts：" + BaseActivity.mTts);
				
				BaseActivity.mTts.startSpeaking(musci, new SynthesizerListener() {
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
						LogUtils.vLog(TAG, "--- playMusic SpeechSynthesizer onCompleted");
						if(!returnMethodName.equals("")){
							LogUtils.vLog(TAG, "--- playMusic SpeechSynthesizer isPush :" + isPush);
							ConstantUtils.PushWebView.loadUrl("javascript:" + returnMethodName + "(" + new JSONObject() + ")");
						}
					}

					@Override
					public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

					}
				});
				
				LogUtils.eLog(TAG, "playMusic end mTts");

			} else {
				_obj = mActivity.getResources().getString(
						R.string.speak_tts_faile1);
				_what = ConstantUtils.HANDLER_FAILE;
			}

		} catch (JSONException e) {
			_obj = e.getMessage();
			_what = ConstantUtils.HANDLER_FAILE;
			throw new RuntimeException(e);
		}
		msg.obj = _obj;
		msg.what = _what;
		handler.sendMessage(msg);
	}
}
