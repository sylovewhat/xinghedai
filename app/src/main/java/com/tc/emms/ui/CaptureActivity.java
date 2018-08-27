package com.tc.emms.ui;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tc.emms.R;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.bean.BaseResponse;
import com.tc.emms.service.BusinessException;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.ConvertUtils;
import com.tc.emms.utils.FileUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.utils.SharedUtils;
import com.tc.emms.utils.SystemBarTintManager;
import com.tc.emms.utils.ToastShow;
import com.tc.emms.utils.ToastUtils;
import com.tc.emms.zxing.CameraManager;
import com.tc.emms.zxing.CaptureActivityHandler;
import com.tc.emms.zxing.FinishListener;
import com.tc.emms.zxing.InactivityTimer;
import com.tc.emms.zxing.RGBLuminanceSource;
import com.tc.emms.zxing.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

/*
 * 二维码扫描、本地识别页面
 * 功能【订单查询、扫码进件】
 */
@SuppressLint({"NewApi", "InflateParams"})
public class CaptureActivity extends BaseActivity implements Callback {

	private static String TAG = "CaptureActivity";
	
	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;// surface有没有被绘制
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;// 完成扫描时是否震动提示
	private ImageView iv_back;
	private TextView tv_edit;
	/* 扫码页面传递类型 */
	private String scan_type = "0";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
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
		setContentView(R.layout.twodimcode);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		tv_edit = (TextView) findViewById(R.id.tv_edit);
		tv_edit.setOnClickListener(this);
		CameraManager.init(this, new Handler());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);// activity静止一段时间会自动关闭

		scan_type = getIntent().getStringExtra(ConstantUtils.CAPTURE_SCAN_TYPE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogUtils.vLog(TAG, "--- init onResume");
		initCamera();
	}

	@SuppressWarnings("deprecation")
	private void initCamera() {
		LogUtils.vLog(TAG, "--- init initCamera");
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = false;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	private void stopCamera() {
		LogUtils.vLog(TAG, "--- init stopCamera");
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onPause() {
		super.onPause();
		LogUtils.vLog(TAG, "--- init onPause");
		stopCamera();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
		LogUtils.vLog(TAG, "--- init onDestroy");
	}

	// 初始化照相机
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			displayFrameworkBugMessageAndExit();
			return;
		} catch (RuntimeException e) {
			displayFrameworkBugMessageAndExit();
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	private void displayFrameworkBugMessageAndExit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				R.style.Theme_Transparent);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage(getString(R.string.msg_camera_framework_bug));
		builder.setPositiveButton(R.string.btn_text_confirm,
				new FinishListener(this));
		builder.setOnCancelListener(new FinishListener(this));
		builder.show();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	// 扫描结果数据
	public void handleDecode(Result obj, Bitmap barcode) {
		/*layout_main.setBackgroundColor(getResources().getColor(
				R.color.main_blue));*/

		inactivityTimer.onActivity();
		viewfinderView.drawResultBitmap(barcode);// 画结果图片
		playBeepSoundAndVibrate();// 启动声音效果

		String str = obj.getText() + "";
		LogUtils.vLog(TAG, "scan result: " + str);
		gotoResult(str);
	}

	private void showCodeTip(String tip_string, final String url) {
		final Dialog dialog = new Dialog(CaptureActivity.this,
				R.style.Theme_Transparent);
		View v = getLayoutInflater().inflate(R.layout.dialog_normal_tip, null);
		dialog.setContentView(v);
		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		TextView tv_tip = (TextView) v.findViewById(R.id.tv_tip);
		tv_tip.setText(tip_string);
		v.findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				/*Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_url = Uri.parse(url);
				intent.setData(content_url);
				startActivity(intent);*/
				finish();
			}
		});
		v.findViewById(R.id.btn_cancel).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						viewfinderView.drawViewfinder();
						CameraManager.init(CaptureActivity.this, new Handler());
						SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
						SurfaceHolder surfaceHolder = surfaceView.getHolder();
						initCamera(surfaceHolder);
						if (handler != null) {
							handler.restartPreviewAndDecode();
						}
					}
				});
		dialog.show();
	}

	// 声音控制
	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	// 启动声音功能
	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		@Override
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_back :
				finish();
				break;
			case R.id.tv_edit :
				/* 相册选取图片 */
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				mActivity.startActivityForResult(intent,
						ConstantUtils.IMAGE_CODE);
				break;
			default :
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		ToastUtils.getInstances().showWaittingDialog();
		LogUtils.iLog(TAG, "CaptureActivity Choose Picture--: onActivityResult");
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			ToastShow.showShort(CaptureActivity.this,
					getString(R.string.pic_getfaile));
			ToastUtils.getInstances().dissWaittingDialog();
			return;
		}
		// String save_path = Utils.getUPLOAD_PATH(WebActivity.this);
		String save_path = FileUtils
				.getStoragePath(CaptureActivity.this, false)
				+ "/"
				+ ConstantUtils.UPLOAD_PATH;
		LogUtils.vLog(TAG, "CaptureActivity  init save_path:" + save_path);
		File file = new File(save_path);
		if (!file.exists()) {
			file.mkdirs();
		}
		switch (requestCode) {
			case ConstantUtils.CAPTURE_CODE :

				break;

			case ConstantUtils.IMAGE_CODE :
				final String path;
				if (data != null) {
					Uri uri = data.getData();
					if (!TextUtils.isEmpty(uri.getAuthority())) {
						Cursor cursor = getContentResolver().query(uri,
								new String[]{MediaColumns.DATA},
								null, null, null);
						if (null == cursor) {
							ToastShow.showShort(CaptureActivity.this,
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
					LogUtils.vLog(TAG, "CaptureActivity Local Picture--path: " + path);
					new Thread(new Runnable() {

						@Override
						public void run() {
							Result result = scanningImage(path);
							/*Result result = DecodeYUV.decodeBarcodeYUV(path);*/
							// String result = decode(photo_path);
							if (result == null) {
								Looper.prepare();
								ToastUtils.getInstances().dissWaittingDialog();
								ToastShow.showShort(CaptureActivity.this,
										"识别失败,图片格式有误");
								Looper.loop();
							} else {
								// 数据返回
								String recode = ConvertUtils.recode(result
										.toString());
								LogUtils.iLog(TAG, "CaptureActivity Decode result"
										+ result.toString());
								gotoResult(recode);
							}
						}
					}).start();
				} else {
					ToastShow.showShort(CaptureActivity.this,
							getString(R.string.pic_getfaile));
					ToastUtils.getInstances().dissWaittingDialog();
					return;
				}
				break;
		}
	}
	
	private void gotoResult(String _codeStr){
		ToastUtils.getInstances().dissWaittingDialog();
		if (scan_type.equals("1")) {
			/* 订单查询返回 */
			webViewReturn(_codeStr);
		} else {
			String tip_string = "";
			if (!_codeStr.contains(".498.net")
					|| !_codeStr.contains("?p=") ) {
				LogUtils.vLog(TAG, "gotoResult result: code_error_tip");
				
				tip_string = getString(R.string.code_error_tip);
				showCodeTip(tip_string, _codeStr);
			} else {
				tip_string = getString(R.string.code_seccess_tip);
				ToastShow.showShort(CaptureActivity.this,
						getString(R.string.code_seccess_tip));
				String s_url = _codeStr.replace("?p=", "/p/");
				s_url += "/flag/1.html";
				LogUtils.vLog(TAG, "gotoResult result: " + s_url);
				/* 直接跳转 */

				Intent intent = new Intent(CaptureActivity.this,
						InitWebActivity.class);
				intent.putExtra(ConstantUtils.HTML_NAME, "扫码进件");
				intent.putExtra(ConstantUtils.HTML_URL, s_url);
				intent.putExtra(ConstantUtils.IS_CHECK, "0");
				startActivity(intent);
				finish();

			}
		}
	}

	/* 统一方法库回调 */
	@SuppressLint("NewApi")
	private void webViewReturn(final String recode) {
		finish();
		LogUtils.eLog(TAG, "webViewReturn getScanCode recode: " + recode);
		if (ConstantUtils.returnMethodName != null
				&& !ConstantUtils.returnMethodName.equals("")) {
			JSONObject jObject = new JSONObject();
			jObject.put("result", recode);
			LogUtils.vLog(TAG, "---统一方法库回调 jObject:" + jObject.toString());
			if (ConstantUtils.ChooseImgWebView != null) {
				ConstantUtils.ChooseImgWebView.loadUrl("javascript:"
						+ ConstantUtils.returnMethodName + "(" + jObject + ")");
			}
		}

		
	}

	@Override
	public void onSuccess(BaseResponse response, String jsonStr,  int act) {

	}

	@Override
	public void onError(BusinessException e, int act) {

	}
	
	/**
	 * 扫描二维码图片的方法
	 * @param path
	 * @return
	 */
	Bitmap scanBitmap ;
	public Result scanningImage(String path) {
	    if(TextUtils.isEmpty(path)){
	        return null;
	    }
	    Hashtable<DecodeHintType, String> hints = new Hashtable<>();
	    hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码

	    BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true; // 先获取原大小
	    scanBitmap = BitmapFactory.decodeFile(path, options);
	    options.inJustDecodeBounds = false; // 获取新的大小
	    int sampleSize = (int) (options.outHeight / (float) 200);
	    if (sampleSize <= 0)
	        sampleSize = 1;
	    options.inSampleSize = sampleSize;
	    scanBitmap = BitmapFactory.decodeFile(path, options);
	    RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
	    BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
	    QRCodeReader reader = new QRCodeReader();
	    try {
	        return reader.decode(bitmap1, hints);
	    } catch (NotFoundException e) {
	        e.printStackTrace();
	    } catch (ChecksumException e) {
	        e.printStackTrace();
	    } catch (FormatException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

}