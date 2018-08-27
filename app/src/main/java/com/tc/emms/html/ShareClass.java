package com.tc.emms.html;

import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tc.emms.R;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.ImageUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.utils.ToastShow;
import com.tc.emms.widget.X5WebView;



/*
 * 分享功能库
 */

public class ShareClass {

	private static String TAG = "ShareClass";
	/* 统一方法库回调 */
	@SuppressWarnings("unused")
	private static void webViewReturn(final X5WebView mWebView,
			final String returnMethodName, final JSONObject jObject) {
		if (returnMethodName != null && !returnMethodName.equals("")) {
			LogUtils.vLog(TAG, "---统一方法库回调 jObject:" + jObject.toString());
			mWebView.post(new Runnable() {
				@Override
				public void run() {
					mWebView.loadUrl("javascript:" + returnMethodName + "(" + jObject
							+ ")");
				}
			});
		}
	}
	
	/***
	 * 微信分享
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
	public static void share_weixin(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " share_weixin seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;

		String shareType = "0";//0 = 好友列表 1 = 朋友圈 2 = 收藏
		// 标题
		String title = "";
		// 分享描述
		String description = "";
		// 图片
		String imageName = "";
		// 分享链接
		String url = "";
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && keyString.equals("shareType")) {
					shareType = valueString;
					LogUtils.vLog(TAG, "--- shareType:" + shareType);
				}
				if (keyString != null && keyString.equals("title")) {
					title = valueString;
					LogUtils.vLog(TAG, "--- title:" + title);
				}
				if (keyString != null && keyString.equals("description")) {
					description = valueString;
					LogUtils.vLog(TAG, "--- description:" + description);
				}
				if (keyString != null && keyString.equals("imageName")) {
					imageName = valueString;
					LogUtils.vLog(TAG, "--- loadType:" + imageName);
				}
				if (keyString != null && keyString.equals("url")) {
					url = valueString;
					LogUtils.vLog(TAG, "--- loadType:" + url);
				}
			}
			IWXAPI api = mActivity.getShareApi();
			if(isCanShare(mActivity, api)){
				shareWebPage(shareType, mActivity, api, url, title, description, imageName);
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
	
	/*微信分享 WEBPAGE 统一方法*/
	private static void shareWebPage(String shareType, BaseActivity mActivity, IWXAPI api, String url, String title, String description, String imageName){
		LogUtils.vLog(TAG, "---微信分享 shareWebPage start:" + url);
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = url;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = title;
		msg.description = description;
		int iv_resource = ImageUtils.getResourceID(
				mActivity, imageName);
		if (iv_resource > 0) {
			LogUtils.vLog(TAG, "--- 微信分享 iv_resource:" + iv_resource);
		} else {
			LogUtils.vLog(TAG, "---微信分享 iv_resource null: use normal");
			iv_resource = R.drawable.ic_launcher;
		}
		Bitmap bmp = BitmapFactory.decodeResource(mActivity.getResources(), iv_resource);
		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, ConstantUtils.THUMB_SIZE, ConstantUtils.THUMB_SIZE, true);
		bmp.recycle();
		msg.thumbData = ImageUtils.bmpToByteArray(thumbBmp, true);
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
		if (shareType.equals("0")) {
			/* 好友列表分享 */
			req.scene = SendMessageToWX.Req.WXSceneSession;
		} else {
			if (shareType.equals("1")) {
				/* 朋友圈分享 */
				req.scene = SendMessageToWX.Req.WXSceneTimeline;
			} else {
				/* 收藏  */
				req.scene = SendMessageToWX.Req.WXSceneFavorite;
			}
		}
		api.sendReq(req);
	}
	
	private static String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	
	private static boolean isCanShare(BaseActivity mActivity, IWXAPI api){
		if(!api.isWXAppInstalled()){
			ToastShow.showShort(mActivity, mActivity.getResources().getString(R.string.wx_share_error01));
			return false;
		}
		if(!api.isWXAppSupportAPI()){
			ToastShow.showShort(mActivity, mActivity.getResources().getString(R.string.wx_share_error02));
			return false;
		}
		return true;
	}
}
