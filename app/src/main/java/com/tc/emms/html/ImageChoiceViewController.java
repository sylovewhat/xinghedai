package com.tc.emms.html;

import java.io.File;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tc.emms.R;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.ui.ChooseImgActivity;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.ConvertUtils;
import com.tc.emms.utils.DateUtils;
import com.tc.emms.utils.DownloadTask;
import com.tc.emms.utils.FileUtils;
import com.tc.emms.utils.ImageUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.utils.ScreenShotUtils;
import com.tc.emms.utils.SharedUtils;
import com.tc.emms.utils.ToastShow;
import com.tc.emms.utils.ZipExtractorUtils;
import com.tc.emms.widget.X5WebView;

public class ImageChoiceViewController {

	private static String TAG = "ImageChoiceViewController";

	/* 统一方法库回调 */
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
	 * 获取图片
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return base64 image
	 */
	public static void choose_picture(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " choose_picture seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		String image_type = "0";
		String compression = "1";
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && keyString.equals("getImageType")) {
					image_type = valueString;
					LogUtils.vLog(TAG, "--- choose_picture type:" + image_type);
				}
				if (keyString != null && keyString.equals("compression")) {
					compression = valueString;
					LogUtils.vLog(TAG, "--- choose_picture compression:" + compression);
				}
			}
			ConstantUtils.ChooseImgWebView = mWebView;
			ConstantUtils.returnMethodName = returnMethodName;
			double fStr = 100 * ConvertUtils.convertToDouble(compression, 1.0);
			LogUtils.vLog(TAG, "---Picture choose_picture fStr:" + fStr);
			int iStr = ConvertUtils.convertToInt(fStr + TAG, 100);
			LogUtils.vLog(TAG, "---Picture choose_picture iStr:" + iStr);
			ConstantUtils.compression = iStr;
			/* 打开默认图片选择视图 */
			Intent intent = new Intent(mActivity, ChooseImgActivity.class);
			intent.putExtra(ConstantUtils.CHOOSE_IMG_TYPE, image_type);
			mActivity.startActivity(intent);

			// ChoosImgDialog(mActivity, image_type);

		} catch (JSONException e) {
			_obj = e.getMessage();
			_what = ConstantUtils.HANDLER_FAILE;
			throw new RuntimeException(e);
		}
		msg.obj = _obj;
		msg.what = _what;
		handler.sendMessage(msg);
	}

	/***
	 * 保存图片到相册
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return base64 image
	 */
	public static void loadImageFinished(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " loadImageFinished seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		String imageBase64 = "";
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && keyString.equals("imageBase64")) {
					imageBase64 = valueString;
					LogUtils.vLog(TAG, "--- loadImageFinished imageBase64:"
							+ imageBase64);
				}
			}
			if (!imageBase64.equals("")) {
				Bitmap mBitmap = ImageUtils.base64ToBitmap(imageBase64);
				if (mBitmap != null) {
					String save_path = FileUtils.getStoragePath(mActivity,
							false) + "/" + ConstantUtils.UPLOAD_PATH;
					LogUtils.vLog(TAG, "WebActivity Picture save_path:" + save_path);
					String name = DateUtils.getNowTime() + ".jpg";
					File file = new File(save_path);
					if (!file.exists()) {
						file.mkdirs();
					}
					String _path = save_path + "/" + name;
					LogUtils.vLog(TAG, "Camera Picture--path: " + _path);
					boolean isOK = ImageUtils.saveImageToGallery(mActivity,
							mBitmap);
					if (isOK) {
						/* 统一方法库回调 */

					} else {
						_obj = mActivity.getResources().getString(
								R.string.save_base_img_faile3);
						_what = ConstantUtils.HANDLER_FAILE;
					}

				} else {
					_obj = mActivity.getResources().getString(
							R.string.save_base_img_faile2);
					_what = ConstantUtils.HANDLER_FAILE;
				}
			} else {
				_obj = mActivity.getResources().getString(
						R.string.save_base_img_faile1);
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

	/***
	 * 截屏
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return base64 image
	 */
	public static void glToUIImage(String jsonString,
			final String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " glToUIImage seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		LogUtils.vLog(TAG, "glToUIImage start");
		ScreenShotUtils screenshot = new ScreenShotUtils(mActivity);
		screenshot.takeScreenshot(mActivity.getWindow().getDecorView(),
				new Runnable() {
					@Override
					public void run() {
						LogUtils.vLog(TAG, "glToUIImage Runnable run ok");
						LogUtils.vLog(TAG, "glToUIImage Runnable run save path:"
								+ ConstantUtils.savePath);
						if (ConstantUtils.saveBitmap != null) {
							final String b_img = ImageUtils.bitmapToBase64(
									ConstantUtils.saveBitmap, 90);
							JSONObject jObject = new JSONObject();
							jObject.put("imageBase64", b_img);
							LogUtils.vLog(TAG,
									"---统一方法库回调 jObject:" + jObject.toString());
							webViewReturn(mWebView, returnMethodName, jObject);
						}
					}
				}, true, true);

		msg.obj = _obj;
		msg.what = _what;
		handler.sendMessage(msg);
	}

	/***
	 * 下载图片、文件
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return base64 image
	 */
	@SuppressLint("InflateParams")
	public static void downFile(String jsonString,
			final String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " downFile seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		LogUtils.vLog(TAG, "downFile start");
		String post_url = "";
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && keyString.equals("post_url")) {
					post_url = valueString;
					LogUtils.vLog(TAG, "--- downFile post_url:" + post_url);
				}
			}
			if (!post_url.equals("")) {
				/* 下载更新 */
				LogUtils.dLog(TAG, "html downurl:" + post_url);
				
				String file_name = ConstantUtils.U_RES_NAME;
				LogUtils.eLog(TAG, "当前查询 file_name: " + file_name);
				ToastShow.showShort(mActivity, mActivity.getString(R.string.rar_update_info));

				String u_path = SharedUtils.getValue(mActivity, "FILE")
						+ ConstantUtils.FILE_PATH + ConstantUtils.HTML_PATH;
				LogUtils.dLog(TAG, "开始下载:" + post_url);
				LogUtils.dLog(TAG, "保存路径:" + u_path);
				// /传入参数：Context对象，下载地址, 文件保存路径；
				DownloadTask.to(post_url, u_path + ConstantUtils.U_RES_NAME,
						new Handler() {
							@Override
							public void handleMessage(Message msg) {
								switch (msg.what) {
									case 3 :
										/* 正在解压 */
										
										/*ToastShow.showShort(mActivity, mActivity.getString(R.string.rar_update_info));*/
										break;
									case 4 :
										/* 解压完成 */
										ToastShow.showShort(
												mActivity,
												mActivity
														.getString(R.string.tip_update_seccess));

										/* 更新数据库 */
										// 实例化一个容器，用来拼接sql语句
										/*StringBuffer sBuffer = new StringBuffer();
										// sql语句，第一个字段为_ID 主键自增，这是通用的，所以直接写死
										sBuffer.append("update "
												+ ConstantUtils.HTML_UPDATE_TABLE_NAME
												+ " set "
												+ ConstantUtils.HTML_UPDATE_KEY
												+ "=0"
												+ " where "
												+ ConstantUtils.HTML_UPDATE_NAME
												+ "='"
												+ ConstantUtils.U_RES_NAME
												+ "'");
										Log.v("isHtmlUpdate",
												"--- db changedIsUpdate execSQLStr:"
														+ sBuffer.toString());
										 查询表中指定数据 
										SQLiteDatabase db = DBHelper
												.openDatabase();
										 获取表中所有字段 
										db.execSQL(sBuffer.toString());
										db.close();*/
										
										JSONObject jObject = new JSONObject();
										LogUtils.vLog(TAG,
												"---统一方法库回调 jObject:" + jObject.toString());
										webViewReturn(mWebView, returnMethodName, jObject);
										break;
									case ConstantUtils.U_DOWN_PROCESS :
										int _process = msg.arg1;
										LogUtils.eLog(TAG, "refresh 下载进度：" + _process);

										break;
									case ConstantUtils.U_DOWN_COM :
										// 解压文件
										LogUtils.dLog(TAG, "下载完成，正在解压！");
										String mPath = FileUtils
												.getU_PATH(mActivity);
										LogUtils.iLog(TAG, "解压目录： " + mPath + "img.zip");
										ZipExtractorUtils task = new ZipExtractorUtils(
												mPath + "img.zip", mPath,
												mActivity, true, this);
										task.execute();

										break;
									case ConstantUtils.U_DOWN_ERR :
										// 下载失败
										ToastShow.showShort(
												mActivity,
												mActivity
														.getString(R.string.tip_down_faie));
										break;
									default :
										break;
								}
							}
						});
			} else {
				_obj = "更新错误：路径为空";
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
	
	/***
	 * 保存文件
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return base64 image
	 */
	public static void writeInfoFile(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {

		Message msg = new Message();
		String _obj = TAG + " choose_picture seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		LogUtils.dLog(TAG, "no do writeInfoFile: ok");
		msg.obj = _obj;
		msg.what = _what;
		handler.sendMessage(msg);
	}
}
