package com.tc.emms.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tc.emms.R;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.bean.BaseResponse;
import com.tc.emms.service.BusinessException;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.DateUtils;
import com.tc.emms.utils.DeviceUtils;
import com.tc.emms.utils.FileUtils;
import com.tc.emms.utils.ImageUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.utils.ToastShow;
import com.tc.emms.utils.ToastUtils;
import com.tc.emms.widget.X5WebView;

/*
 * 图片拍照、选择页面【透明弹窗】
 * 功能【图片拍照、选择】
 */
public class ChooseImgActivity extends BaseActivity {
	
	private static String TAG = "ChooseImgActivity";

	private String image_type = "0";
	/* 弹出选择 */
	private Dialog dialog;
	private ArrayList<String> list_items;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private Bitmap _bitMap = null;
	private Bitmap _thu_bitMap = null;
	/* 图片类返回使用 */
	public X5WebView ChooseImgWebView;
	public String returnMethodName;
	public int compression;
	public String save_path;
	public String save_name = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		innitView();
		ChooseImgWebView = ConstantUtils.ChooseImgWebView;
		returnMethodName = ConstantUtils.returnMethodName;
		compression = ConstantUtils.compression;
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));

		save_path = FileUtils.getStoragePath(ChooseImgActivity.this, false)
				+ "/" + ConstantUtils.UPLOAD_PATH;
		LogUtils.vLog(TAG, "WebActivity init save_path:" + save_path);
		File file = new File(save_path);
		if (!file.exists()) {
			file.mkdirs();
		}
		save_name = DateUtils.getNowTime() + ".jpg";

		image_type = getIntent().getStringExtra(ConstantUtils.CHOOSE_IMG_TYPE);
		LogUtils.vLog(TAG, "--- ChooseImgActivity image_type:" + image_type);
		ChoosImgDialog(this, image_type);
	}

	private void innitView() {
		setContentView(R.layout.translucent_layout);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("InflateParams")
	private void ChoosImgDialog(final BaseActivity mActivity, String image_type) {
		list_items = new ArrayList<String>();
		dialog = new Dialog(mActivity, R.style.Theme_Transparent);
		View v = mActivity.getLayoutInflater().inflate(
				R.layout.dialog_popupwindow, null);
		LinearLayout ly = (LinearLayout) v.findViewById(R.id.content_lin);
		TextView tv_tip = (TextView) v.findViewById(R.id.tv_tip);
		tv_tip.setText("选择图片获取方式");
		
		View v_top = mActivity.getLayoutInflater().inflate(
				R.layout.dialog_top_item, null);
		Button btn_top = (Button) v_top.findViewById(R.id.btn_top);
		View v_bottom = mActivity.getLayoutInflater().inflate(
				R.layout.dialog_bottom_item, null);
		Button btn_bottom = (Button) v_bottom.findViewById(R.id.btn_bottom);
		btn_bottom.setTextColor(getResources().getColor(R.color.main_blue));
		if (image_type.equals("0")) {
			list_items.add(mActivity.getString(R.string.pic_camera));
			ly.addView(v_top);
			btn_top.setText(list_items.get(0));
			btn_top.setTag("1");

		} else {
			if (image_type.equals("1")) {
				list_items.add(mActivity.getString(R.string.pic_local));
				ly.addView(v_top);
				btn_top.setText(list_items.get(0));
				btn_top.setTag("1");

			} else {

				list_items.add(mActivity.getString(R.string.pic_camera));
				list_items.add(mActivity.getString(R.string.pic_local));
				ly.addView(v_top);
				ly.addView(v_bottom);
				btn_top.setText(list_items.get(0));
				btn_top.setTag("1");
				btn_bottom.setText(list_items.get(1));
				btn_bottom.setTag("2");
			}
		}
		btn_top.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/* 拍照 */
				if (DeviceUtils.isSDCardAvailable()) {
					try {
						Intent intent = new Intent(
								android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						File f = new File(save_path, save_name);
						Uri u = Uri.fromFile(f);
						intent.putExtra(ImageColumns.ORIENTATION, 0);
						intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
						mActivity.startActivityForResult(intent,
								ConstantUtils.CAPTURE_CODE);
					} catch (ActivityNotFoundException e) {
						// TODO Auto-generated catch block
						ToastShow.showShort(mActivity, R.string.no_sdsave);
					}
				} else {
					ToastShow.showShort(mActivity, R.string.no_sdcard);
				}
			}
		});
		btn_bottom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/* 本地照片 */

				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				mActivity.startActivityForResult(intent,
						ConstantUtils.IMAGE_CODE);
			}
		});

		dialog.setContentView(v);
		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow().setWindowAnimations(R.style.anim_dialog);
		dialog.getWindow().setGravity(Gravity.BOTTOM);
		dialog.getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		v.findViewById(R.id.btn_cancel).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						finish();
					}
				});
		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtils.iLog(TAG, "Choose Picture--: onActivityResult");
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			ToastShow.showShort(ChooseImgActivity.this,
					getString(R.string.pic_getfaile));
			return;
		}
		ToastUtils.getInstances().showWaittingDialog();
		switch (requestCode) {
			case ConstantUtils.CAPTURE_CODE :

				String _path = save_path + save_name;
				LogUtils.vLog(TAG, "Camera Picture--path: " + _path);
				webViewReturn(_path);
				break;

			case ConstantUtils.IMAGE_CODE :
				String path;
				if (data != null) {
					Uri uri = data.getData();
					if (!TextUtils.isEmpty(uri.getAuthority())) {
						Cursor cursor = getContentResolver().query(uri,
								new String[]{MediaColumns.DATA},
								null, null, null);
						if (null == cursor) {
							ToastShow.showShort(ChooseImgActivity.this,
									getString(R.string.pic_getfaile));
							ToastUtils.getInstances().dissWaittingDialog();
							return;
						}
						cursor.moveToFirst();
						path = cursor.getString(cursor
								.getColumnIndex(MediaColumns.DATA));
						cursor.close();
					} else {
						path = uri.getPath();
					}
					LogUtils.vLog(TAG, "Local Picture--path: " + path);
					if (path == null) {
						errorBack();
						return;
					} else {
						String u_path = save_path + save_name;
						boolean isCopy = FileUtils.copyFile(path, u_path);
						if (isCopy) {
							webViewReturn(u_path);
						} else {
							errorBack();
						}
					}

				} else {
					errorBack();
					return;
				}
				break;
		}
	}

	private void errorBack() {
		LogUtils.eLog(TAG, "webViewReturn Picture errorBack: path null");
		ToastShow.showShort(ChooseImgActivity.this,
				getString(R.string.pic_getfaile));
		ToastUtils.getInstances().dissWaittingDialog();
		finish();
	}

	/* 统一方法库回调 */
	@SuppressLint("NewApi")
	private void webViewReturn(final String local_path) {
		LogUtils.eLog(TAG, "webViewReturn Picture local_path: " + local_path);
		/*判断文件是否存在*/
		File file = new File(local_path);
		if (!file.exists()) {
			errorBack();
		}else{

			_bitMap = imageLoader.loadImageSync("file://" + local_path);
			if (_bitMap == null) {
				ToastShow.showShort(ChooseImgActivity.this,
						getString(R.string.pic_getfaile));
			} else {
				String thu_local_path = local_path.replace(".jpg", "_thu.jpg");
				LogUtils.vLog(TAG, "uploadIMG Picture thu_local_path: " + thu_local_path);
				File file2 = new File(thu_local_path);
				compressBitmapToFile(_bitMap, file2);
				
				//NativeUtil.compressBitmap(_bitMap, thu_local_path);
				_thu_bitMap = imageLoader.loadImageSync("file://" + thu_local_path);
				
				LogUtils.vLog(TAG, "uploadIMG Picture thu size: " + _thu_bitMap.getByteCount());
				final String b_img = ImageUtils
						.bitmapToBase64(_thu_bitMap, compression);
				if (returnMethodName != null && !returnMethodName.equals("")) {
					JSONObject jObject = new JSONObject();
					jObject.put("image", b_img);
					LogUtils.vLog(TAG, "---统一方法库回调 jObject:" + jObject.toString());
					if (ChooseImgWebView != null) {

						ChooseImgWebView.loadUrl("javascript:" + returnMethodName
								+ "(" + jObject + ")");
					}
				}
			}
			dialog.dismiss();
			finish();
		}
	}
	
	public static void compressBitmapToFile(Bitmap bmp, File file){
	    // 尺寸压缩倍数,值越大，图片尺寸越小
	    int ratio = 2;
	    // 压缩Bitmap到对应尺寸
	    Bitmap result = Bitmap.createBitmap(bmp.getWidth() / ratio, bmp.getHeight() / ratio, Config.ARGB_8888);
	    Canvas canvas = new Canvas(result);
	    Rect rect = new Rect(0, 0, bmp.getWidth() / ratio, bmp.getHeight() / ratio);
	    canvas.drawBitmap(bmp, null, rect, null);
	    
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    // 把压缩后的数据存放到baos中
	    result.compress(Bitmap.CompressFormat.JPEG, 80 ,baos);
	    try {  
	        FileOutputStream fos = new FileOutputStream(file);  
	        fos.write(baos.toByteArray());  
	        fos.flush();  
	        fos.close();  
	    } catch (Exception e) {  
	        e.printStackTrace();  
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
	public void onSuccess(BaseResponse response, String jsonStr,  int act) {

	}

	@Override
	public void onError(BusinessException e, int act) {

	}

	@Override
	public void onClick(View v) {

	}
}
