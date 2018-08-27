package com.tc.emms.ui;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.fastjson.JSONObject;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tc.emms.R;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.bean.BaseResponse;
import com.tc.emms.camera.CameraActivity;
import com.tc.emms.crop.FileUtil;
import com.tc.emms.crop.RecognizeService;
import com.tc.emms.service.BusinessException;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.ImageUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.utils.SharedUtils;
import com.tc.emms.utils.SystemBarTintManager;
import com.tc.emms.utils.ToastShow;
import com.tc.emms.widget.X5WebView;

/*
 * 身份证、银行卡自动识别页面
 * 功能【身份证、银行卡自动识别】
 */
@SuppressLint("InlinedApi") 
public class IDCardActivity extends BaseActivity {
	
	private static String TAG = "IDCardActivity";
	
	/* 图片类返回使用 */
	public X5WebView ChooseImgWebView;
	public String returnMethodName;
	private static final int REQUEST_CODE_CAMERA = 102;
	private static final int REQUEST_CODE_BANKCARD = 110;
	private String ID_CARD_TYPE = "0";
	/* 返回json */
	private JSONObject jObject = new JSONObject();
	private ImageLoader imageLoader = ImageLoader.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			// Translucent status bar
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			int iv_resource = SharedUtils.getInt(this, ConstantUtils.APP_BACKGROUD_COLOR);
			LogUtils.vLog(TAG, "iv_resource：" + iv_resource);
//			if (iv_resource <= 0) {
//				iv_resource = R.color.main_blue;
//			}
			tintManager.setStatusBarTintColor(iv_resource);
		}
		innitView();
		ChooseImgWebView = ConstantUtils.ChooseImgWebView;
		returnMethodName = ConstantUtils.returnMethodName;
		ID_CARD_TYPE = getIntent().getStringExtra(ConstantUtils.ID_CARD_TYPE);
		LogUtils.vLog(TAG, "---IDCardActivity ChooseImgActivity ID_CARD_TYPE:"
				+ ID_CARD_TYPE);
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));

		Intent intent = new Intent(IDCardActivity.this, CameraActivity.class);
		intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtil
				.getSaveFile(getApplication()).getAbsolutePath());
		if (ID_CARD_TYPE.equals("0")) {
			/* 身份证正面 */
			intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
					CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
			startActivityForResult(intent, REQUEST_CODE_CAMERA);
		} else if (ID_CARD_TYPE.equals("1")) {
			/* 身份证反面 */
			intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
					CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
			startActivityForResult(intent, REQUEST_CODE_CAMERA);
		} else if (ID_CARD_TYPE.equals("2")) {
			/* 银行卡 */
			intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
					CameraActivity.CONTENT_TYPE_BANK_CARD);
			startActivityForResult(intent, REQUEST_CODE_BANKCARD);
		}
	}

	private void innitView() {
		setContentView(R.layout.translucent_layout);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtils.iLog(TAG, "IDCardActivity Choose Picture--: onActivityResult");
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			ToastShow.showShort(IDCardActivity.this,
					getString(R.string.pic_getfaile));
			finish();
			return;
		}
		if (requestCode == REQUEST_CODE_CAMERA) {
			if (data != null) {
				String contentType = data
						.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
				String filePath = FileUtil.getSaveFile(getApplicationContext())
						.getAbsolutePath();
				if (!TextUtils.isEmpty(contentType)) {
					if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT
							.equals(contentType)) {
						recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
					} else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK
							.equals(contentType)) {
						recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath);
					}
				} else {
					ToastShow.showShort(IDCardActivity.this, getResources()
							.getString(R.string.camera_rec_faile));
					finish();
				}
			} else {
				ToastShow.showShort(IDCardActivity.this, getResources()
						.getString(R.string.camera_rec_faile));
				finish();
			}
		}

		if (requestCode == REQUEST_CODE_BANKCARD) {
			RecognizeService.recBankCard(
					FileUtil.getSaveFile(getApplicationContext())
							.getAbsolutePath(),
					new RecognizeService.ServiceListener() {
						@Override
						public void onResult(String result) {
							LogUtils.iLog(TAG, "IDCardActivity recBank result--: "
									+ result.toString());
							ToastShow.showShort(
									IDCardActivity.this,
									getResources().getString(
											R.string.camera_rec_seccess));
							if (ID_CARD_TYPE.equals("2")) {
							}

							webViewReturn();
						}
					});
		}
	}

	private void recIDCard(String idCardSide, String filePath) {
		IDCardParams param = new IDCardParams();
		param.setImageFile(new File(filePath));
		// 设置身份证正反面
		param.setIdCardSide(idCardSide);
		// 设置方向检测
		param.setDetectDirection(true);
		// 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
		param.setImageQuality(20);

		OCR.getInstance().recognizeIDCard(param,
				new OnResultListener<IDCardResult>() {
					@Override
					public void onResult(IDCardResult result) {
						if (result != null) {
							LogUtils.iLog(TAG, "IDCardActivity recIDCard result--: "
									+ result.toString());
							ToastShow.showShort(
									IDCardActivity.this,
									getResources().getString(
											R.string.camera_rec_seccess));
							if (ID_CARD_TYPE.equals("0")) {
								jObject.put("idcard_man", result.getName()
										.getWords());
								jObject.put("idcard_no", result.getIdNumber()
										.getWords());
								jObject.put("addr", result.getAddress()
										.getWords());
								jObject.put("born", result.getBirthday()
										.getWords());
								jObject.put("peoples", result.getEthnic()
										.getWords());
								jObject.put("sex", result.getGender()
										.getWords());
								jObject.put("id_card_type_name", "身份证");

							} else if (ID_CARD_TYPE.equals("1")) {
								jObject.put("issuance", result
										.getIssueAuthority().getWords());
								jObject.put("idcard_b_date", result
										.getSignDate().getWords());
								jObject.put("idcard_e_date", result
										.getExpiryDate().getWords());
							}
							webViewReturn();
						}
					}

					@Override
					public void onError(OCRError error) {
						LogUtils.iLog(TAG, "IDCardActivity recIDCard onError--: "
								+ error.getMessage());
						ToastShow.showShort(IDCardActivity.this, getResources()
								.getString(R.string.camera_rec_faile));
						webViewReturn();
					}
				});
	}

	/* 统一方法库回调 */
	@SuppressLint("NewApi")
	private void webViewReturn() {
		LogUtils.eLog(TAG, "IDCardActivity webViewReturn");
		LogUtils.iLog(TAG, "IDCardActivity webViewReturn ConstantUtils.saveBitmap:"
				+ ConstantUtils.saveBitmap);
		if (ConstantUtils.saveBitmap == null) {
			ToastShow.showShort(this, getString(R.string.pic_getfaile));
		} else {
			Bitmap _bitMap = null;
			if (ConstantUtils.saveBitmap.isRecycled()) {
				_bitMap = imageLoader.loadImageSync("file://"
						+ ConstantUtils.savePath);
			} else {
				_bitMap = ConstantUtils.saveBitmap;
			}
			if (_bitMap == null) {
				ToastShow.showShort(this, getString(R.string.pic_getfaile));
			} else {
				LogUtils.vLog(TAG,
						"IDCardActivity uploadIMG Picture size: "
								+ _bitMap.getByteCount());
				final String b_img = ImageUtils.bitmapToBase64(
						_bitMap, 100);
				if (returnMethodName != null && !returnMethodName.equals("")) {

					jObject.put("image", b_img);
					LogUtils.vLog(TAG,
							"IDCardActivity---统一方法库回调 jObject:"
									+ jObject.toString());
					if (ChooseImgWebView != null) {

						ChooseImgWebView.loadUrl("javascript:"
								+ returnMethodName + "(" + jObject + ")");
					}
				}
			}

		}
		finish();
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
