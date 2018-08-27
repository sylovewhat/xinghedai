package com.tc.emms.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tc.emms.R;
import com.tc.emms.base.BaseActivity;

public class QrCodeImgUtils {

	public static Bitmap mBitmap;
	// 画布
	public static Canvas mCanvas;
	// 画笔－－画图片
	public static Paint mPicturePaint = new Paint();
	// 画笔－－写字
	public static Paint mTextPaint = new Paint();
	// 画笔－－写运动数据
	public static Paint mDataPaint = new Paint();

	public static ImageView mImageView;
	public static Bitmap mCodeBitmap;
	private static IWXAPI api;

	public static void createImg(BaseActivity mActivity, Bitmap mCodeBitmap,
			Handler ui_handler, String account, String money, int _index) {
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(mActivity, ConstantUtils.WEIXIN_ID, true);
		api.registerApp(ConstantUtils.WEIXIN_ID);
		Bitmap bm_bg = BitmapFactory.decodeResource(mActivity.getResources(),
				R.drawable.bg_code);
		// 得到图片的宽、高
		int width_bg = bm_bg.getWidth();
		int height_bg = bm_bg.getHeight();

		// 创建一个你需要尺寸的Bitmap
		mBitmap = Bitmap.createBitmap(width_bg, height_bg, Config.ARGB_8888);
		// 用这个Bitmap生成一个Canvas,然后canvas就会把内容绘制到上面这个bitmap中
		mCanvas = new Canvas(mBitmap);

		// 绘制背景图片
		mCanvas.drawBitmap(bm_bg, 0.0f, 0.0f, mPicturePaint);

		// 绘制图片

		// 得到图片的宽、高
		int width_head = mCodeBitmap.getWidth();
		int height_head = mCodeBitmap.getHeight();
		// 绘制文字
		mTextPaint.setColor(Color.BLACK);// 白色画笔
		mTextPaint.setTextSize(18f);
		mTextPaint.setAntiAlias(true);
		mTextPaint.setFakeBoldText(true);// 设置字体大小
		float title_width = mTextPaint
				.measureText(account, 0, account.length());
		float x1 = (width_bg - title_width) / 2;
		Log.e("", "account: " + account);
		mCanvas.drawText(account, x1, (height_bg - height_head) / 2, mTextPaint);// 绘制文字
		// 绘制图片－－保证其在水平方向居中
		mCanvas.drawBitmap(mCodeBitmap, (width_bg - width_head) / 2,
				(height_bg - height_head) / 2, mPicturePaint);

		// 绘制文字
		mDataPaint.setColor(Color.RED);// 红色画笔
		mDataPaint.setTextSize(15.0f);// 设置字体大小
		mDataPaint.setAntiAlias(true);
		mDataPaint.setFakeBoldText(true);

		String distanceTextString = "向我付款：";
		String distanceDataString = money;
		String distanceScalString = "元";

		float distanceTextString_width = mTextPaint.measureText(
				distanceTextString, 0, distanceTextString.length());

		float distanceDataString_width = mDataPaint.measureText(
				distanceDataString, 0, distanceDataString.length());
		float distanceScalString_width = mTextPaint.measureText(
				distanceScalString, 0, distanceScalString.length());
		float x = (width_bg - distanceTextString_width
				- distanceDataString_width - distanceScalString_width) / 2;

		mCanvas.drawText(distanceTextString, x, (height_bg - height_head) / 2
				+ height_head, mTextPaint);// 绘制文字
		mCanvas.drawText(distanceDataString, x + distanceTextString_width,
				(height_bg - height_head) / 2 + height_head, mDataPaint);// 绘制文字

		mCanvas.drawText(distanceScalString, x + distanceTextString_width
				+ distanceDataString_width, (height_bg - height_head) / 2
				+ height_head, mTextPaint);// 绘制文字

		// 保存绘图为本地图片
		mCanvas.save(Canvas.ALL_SAVE_FLAG);
		mCanvas.restore();
		String save_path = FileUtils.getStoragePath(mActivity, false)
				+ "/" + ConstantUtils.SHARE_PATH;
		Log.e("", "QrCodeImgUtils 生成二维码分享图片 save_path:" + save_path);
		String name = DateUtils.getNowTime() + ".png";
		File file = new File(save_path);
		if (!file.exists()) {
			file.mkdirs();
		}
		// 保存到payee/share/根目录下
		File fileName = new File(save_path + name);
		Log.i("", "QrCodeImgUtils 生成二维码分享图片 :" + Environment.getExternalStorageDirectory().getPath());
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileName);
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			saveImageToGallery(mActivity, mBitmap);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Message msg = ui_handler.obtainMessage();
		if (_index == 0) {
			// 保存
			msg.what = 0;
		} else {
			// 分享
			msg.what = 1;
			WXImageObject imgObj = new WXImageObject(mBitmap);
			WXMediaMessage wx_msg = new WXMediaMessage();
			wx_msg.mediaObject = imgObj;
			Bitmap thumbBmp = Bitmap
					.createScaledBitmap(mBitmap, 100, 100, true);
			mBitmap.recycle();
			wx_msg.thumbData = bmpToByteArray(thumbBmp, true); // 设置缩略图

			SendMessageToWX.Req req = new SendMessageToWX.Req();
			req.transaction = String.valueOf(System.currentTimeMillis());
			req.message = wx_msg;
			req.scene = SendMessageToWX.Req.WXSceneSession;
			api.sendReq(req);

			ConstantUtils.IS_WEIXIN_ID = true;
			Log.e("", "-----微信分享图片--");
		}
		ui_handler.sendMessage(msg);
	}

	public static void saveImageToGallery(Context context, Bitmap bmp) {
		// 首先保存图片
		File appDir = new File(Environment.getExternalStorageDirectory(), "");
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = System.currentTimeMillis() + ".jpg";
		File file = new File(appDir, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 其次把文件插入到系统图库
		try {
			MediaStore.Images.Media.insertImage(context.getContentResolver(),
					file.getAbsolutePath(), fileName, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Uri path = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		Log.d("", "path: " + path.getPath());
		// 最后通知图库更新
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
				path));
	}

	/**
	 * 微信分享
	 * 
	 */
	public static byte[] bmpToByteArray(final Bitmap bmp,
			final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 60, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
