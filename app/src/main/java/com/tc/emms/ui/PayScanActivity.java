package com.tc.emms.ui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.tc.emms.R;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.bean.BaseResponse;
import com.tc.emms.db.DBHelper;
import com.tc.emms.model.CODE_INFO;
import com.tc.emms.model.PAYORDER_INFO;
import com.tc.emms.model.TONGDAO_INFO;
import com.tc.emms.service.BusinessException;
import com.tc.emms.service.PayeeBusines;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.DateUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.utils.OrderUtils;
import com.tc.emms.utils.OtherUtils;
import com.tc.emms.utils.QrCodeImgUtils;
import com.tc.emms.utils.SharedUtils;
import com.tc.emms.utils.SystemBarTintManager;
import com.tc.emms.utils.ToastShow;
import com.tc.emms.utils.ToastUtils;
import com.tc.emms.zxing.CameraManager;
import com.tc.emms.zxing.FinishListener;
import com.tc.emms.zxing.InactivityTimer;
import com.tc.emms.zxing.PayScanActivityHandler;
import com.tc.emms.zxing.QRUtils;
import com.tc.emms.zxing.RGBLuminanceSource;
import com.tc.emms.zxing.ViewfinderView;

@SuppressLint({"NewApi", "HandlerLeak", "InflateParams", "DefaultLocale"})
public class PayScanActivity extends BaseActivity
		implements
			SurfaceHolder.Callback {
	private BaseActivity mActivity;
	private TextView body_title;
	private ImageView body_title_left_but;
	private FrameLayout fatherLayout;
	private static final String TAG = PayScanActivity.class.getSimpleName();
	private PayScanActivityHandler handler;
	private ViewfinderView viewfinderView;
	private Result lastResult;
	private boolean hasSurface;
	private IntentSource source;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	static final int PARSE_BARCODE_SUC = 3035;
	static final int PARSE_BARCODE_FAIL = 3036;
	protected String photoPath;
	public ProgressDialog mProgress;
	private TextView tv_price;
	private TextView status_view;
	private String price;
	private SurfaceView surfaceView;
	private boolean isScanPage = true;
	private int payType = 0;
	private int scanType = 0;
	private String db_pay_type = "0";
	private String business_no;
	private String tn;
	private String i;
	private LinearLayout scan_layout;
	private LinearLayout pay_layout;
	private LinearLayout card_layout;
	private List<TONGDAO_INFO> scanList = new ArrayList<TONGDAO_INFO>();
	private List<TONGDAO_INFO> payList = new ArrayList<TONGDAO_INFO>();
	private CODE_INFO mCODE_INFO;
	private PAYORDER_INFO mPAYORDER_INFO;
	private String orderid;
	private int code_width = 0;
	private int code_height = 0;
	private TextView iv_scancode;
	private TextView iv_paycode;
	private TextView iv_code;
	private ImageView image_code;
	private Bitmap code_bitmap;
	private TextView i_tip;
	/* 弹出对话框 */
	private TextView t_message;
	private Button b_ok;
	private Button b_cancel;
	private AlertDialog dialog;
	private AlertDialog.Builder builder;
	private String T_message = "";
	private String isPaySeccess = "0";
	private Dialog shareDialog;

	/* 数据库链接 */
	private DBHelper dbHelper;
	/*
	 * 接口请求标识 0 : 扫码支付接口1 : 二维码支付接口2 : 一码付接口 3 : 支付状态查询 5 : 刷卡接口完成流水保存
	 */
	private int post_action_rsa_index = 0;

	enum IntentSource {
		ZXING_LINK, NONE
	}

	String scan_code = "";

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			// Translucent status bar
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			int iv_resource = SharedUtils.getInt(this, ConstantUtils.APP_BACKGROUD_COLOR);
			/*if(iv_resource <= 0){
				iv_resource = R.color.main_blue;
			}*/
			tintManager.setStatusBarTintColor(iv_resource);
			//tintManager.setStatusBarTintResource(iv_resource);// 通知栏所需颜色
		}
		mActivity = this;
		setContentView(R.layout.capture_activity);

		dbHelper = new DBHelper(mActivity);
		ConstantUtils.IS_WEIXIN_ID = false;
		fatherLayout = (FrameLayout) findViewById(R.id.fatherLayout);
		body_title = (TextView) findViewById(R.id.body_title);
		body_title.setText(getResources().getString(R.string.main_pay));
		body_title_left_but = (ImageView) findViewById(R.id.body_title_left_but);
		body_title_left_but.setOnClickListener(this);
		hasSurface = false;
		CameraManager.init(PayScanActivity.this, update_ui_handler);
		iv_scancode = (TextView) findViewById(R.id.iv_scancode);
		iv_paycode = (TextView) findViewById(R.id.iv_paycode);
		iv_code = (TextView) findViewById(R.id.iv_code);
		iv_scancode.setSelected(true);
		iv_scancode.setOnClickListener(this);
		iv_paycode.setOnClickListener(this);
		iv_code.setOnClickListener(this);
		tv_price = (TextView) findViewById(R.id.tv_price);
		status_view = (TextView) findViewById(R.id.status_view);
		status_view.setOnClickListener(this);
		image_code = (ImageView) findViewById(R.id.image_code);
		image_code.setOnLongClickListener(onLongClick);
		i_tip = (TextView) findViewById(R.id.i_tip);

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		inactivityTimer = new InactivityTimer(this);// activity静止一段时间会自动关闭
		scan_layout = (LinearLayout) findViewById(R.id.scan_layout);
		pay_layout = (LinearLayout) findViewById(R.id.pay_layout);
		card_layout = (LinearLayout) findViewById(R.id.card_layout);
		price = getIntent().getStringExtra("price");
		if (!TextUtils.isEmpty(price)) {
			tv_price.setText(price);
		}

		initData();

		boolean is_pos = ConstantUtils.IS_POS_CARD;
		if (!is_pos) {
			card_layout.setVisibility(View.GONE);
			iv_code.setVisibility(View.GONE);
		}
		CameraManager.get().requestAutoFocus(autofocus_handler, 0);
	}

	/* 创建一个handler，内部完成处理消息方法 */
	Handler autofocus_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

		}
	};

	OnLongClickListener onLongClick = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View view) {

			/* 取出用户信息 */
			final String memberName = SharedUtils.getValue(mActivity,
					ConstantUtils.MEMBER_NAME);
			LogUtils.eLog(TAG, "PayScanActivity 取出用户信息 memberName: " + memberName);
			shareDialog = new Dialog(mActivity, R.style.Theme_Transparent);
			View v = mActivity.getLayoutInflater().inflate(
					R.layout.dialog_bottom_tip, null);
			TextView tv_tip = (TextView) v.findViewById(R.id.tv_tip);
			tv_tip.setText(getString(R.string.share_tip));
			
			LinearLayout ly = (LinearLayout) v.findViewById(R.id.content_lin);
			View v_top = mActivity.getLayoutInflater().inflate(
					R.layout.dialog_top_item, null);
			Button btn_top = (Button) v_top.findViewById(R.id.btn_top);

			View v_center = mActivity.getLayoutInflater().inflate(
					R.layout.dialog_center_item, null);
			Button btn_center = (Button) v_center.findViewById(R.id.btn_center);
			
			View v_bottom = mActivity.getLayoutInflater().inflate(
					R.layout.dialog_bottom_item, null);
			Button btn_bottom = (Button) v_bottom.findViewById(R.id.btn_bottom);
			ly.addView(v_top);
			ly.addView(v_center);
			ly.addView(v_bottom);
			btn_top.setText(getString(R.string.img_share_save));
			btn_center.setText(getString(R.string.img_share_weixin));
			btn_bottom.setText(getString(R.string.btn_text_cancel));
			btn_top.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO 保存图片到本地
					QrCodeImgUtils.createImg(mActivity, code_bitmap,
							ui_handler, memberName, price, 0);

				}
			});
			btn_center.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO 分享微信好友
					/* 取出用户信息 */
					QrCodeImgUtils.createImg(mActivity, code_bitmap,
							ui_handler, memberName, price, 1);
				}
			});

			btn_bottom.setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							shareDialog.dismiss();
						}
					});
			shareDialog.setContentView(v);
			shareDialog.setCancelable(false);
			shareDialog.getWindow().setWindowAnimations(R.style.anim_dialog);
			shareDialog.getWindow().setGravity(Gravity.BOTTOM);
			shareDialog.getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			shareDialog.show();
			return false;
		}
	};

	/* 创建一个handler，内部完成处理消息方法 */
	Handler ui_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 更改页面
			switch (msg.what) {
				case 0 :
					ToastShow.showShort(mActivity,
							getString(R.string.save_seccess));

					break;
				case 1 :
					/* 分享微信 */
					
					break;
				default :
					break;
			}
			shareDialog.dismiss();
		}
	};

	/* 创建一个handler，内部完成处理消息方法 */
	Handler update_ui_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 更改页面
			// ViewGroup.LayoutParams s_lay = image_code.getLayoutParams();
			ViewGroup.LayoutParams params = image_code.getLayoutParams();
			code_width = msg.arg1 - 60;
			code_height = msg.arg2 - 60;
			params.width = code_width;
			params.height = code_height;
			// surfaceView.setLayoutParams(s_lay);
			image_code.setLayoutParams(params);
			ConstantUtils.CODE_WIDTH = code_width;
			ConstantUtils.CODE_HEIGHT = code_height;
			// Toast.makeText(getApplicationContext(), "w:" + code_width +
			// " ; h" + code_height, Toast.LENGTH_LONG).show();
			LogUtils.dLog(TAG, "update_ui_handler code_width: " + code_width);
			LogUtils.dLog(TAG, "update_ui_handler code_height: " + code_height);
		}
	};

	private void initData() {
		String sType = SharedUtils
				.getValue(mActivity, ConstantUtils.SCAN_INDEX);
		if (sType != null && !sType.equals("")) {
			scanType = Integer.parseInt(sType);
			Log.e("initData", "取出保存的扫码通道q:scanType：" + scanType);
		}
		String pType = SharedUtils.getValue(mActivity, ConstantUtils.PAY_INDEX);
		if (pType != null && !pType.equals("")) {
			payType = Integer.parseInt(pType);
			Log.e("initData", "取出保存的  二维码通道q:payType：" + payType);
		}
		/* 默认不保存之前记录 */
		scanType = 0;
		payType = 0;
		/* 取出config信息 */
		business_no = SharedUtils
				.getValue(mActivity, ConstantUtils.BUSINESS_NO);
		tn = SharedUtils.getValue(mActivity, ConstantUtils.TN);
		i = SharedUtils.getValue(mActivity, ConstantUtils.I_ID);
		/* 取出结算表信息 */
		String tongArray = dbHelper.selectAll(ConstantUtils.TABLE_NAME_TONGDAO,
				null);
		/* 添加默认‘智能扫码’ */
		TONGDAO_INFO T_Add = new TONGDAO_INFO();
		T_Add.q_n = getString(R.string.pay_znsm);
		T_Add.t_1 = "1";
		T_Add.t_3 = "0";
		scanList.add(T_Add);
		/* 添加到View */
		View view0 = LayoutInflater.from(this).inflate(R.layout.tongdao_item,
				scan_layout, false);
		TextView txt0 = (TextView) view0.findViewById(R.id.text_item);
		txt0.setText(getString(R.string.pay_znsm));
		txt0.setTag(0);
		txt0.setOnClickListener(mScanClick);
		if (0 == scanType) {
			txt0.setSelected(true);
		}
		scan_layout.addView(view0);

		/* 添加默认‘一码付’ */
		TONGDAO_INFO T_Add01 = new TONGDAO_INFO();
		T_Add01.q_n = getString(R.string.pay_ymf);
		T_Add01.t_1 = "0";
		T_Add01.t_3 = "1";
		payList.add(T_Add01);
		/* 添加到View */
		View view1 = LayoutInflater.from(this).inflate(R.layout.tongdao_item,
				pay_layout, false);
		TextView txt1 = (TextView) view1.findViewById(R.id.text_item);
		txt1.setText(getString(R.string.pay_ymf));
		txt1.setTag(0);
		txt1.setOnClickListener(mPayClick);
		if (0 == payType) {
			txt1.setSelected(true);
		}
		pay_layout.addView(view1);
		LogUtils.eLog(TAG, "取出保存的扫码通道   tongArray：" + tongArray);
		List<TONGDAO_INFO> tongList = JSON.parseArray(tongArray,
				TONGDAO_INFO.class);
		for (int i = 0; i < tongList.size(); i++) {
			TONGDAO_INFO TI = new TONGDAO_INFO();
			String q_index = tongList.get(i).q;
			TI.q = q_index;
			TI.q_cn = tongList.get(i).q_cn;
			TI.q_n = tongList.get(i).q_n;
			TI.t_1 = tongList.get(i).t_1;
			TI.t_3 = tongList.get(i).t_3;
			TI.business_no = tongList.get(i).business_no;
			if (tongList.get(i).t_1.equals("1")) {
				scanList.add(TI);
				/* 添加到View */
				View view = LayoutInflater.from(this).inflate(
						R.layout.tongdao_item, scan_layout, false);
				TextView txt = (TextView) view.findViewById(R.id.text_item);
				txt.setText(tongList.get(i).q_n);
				txt.setTag(q_index);
				txt.setOnClickListener(mScanClick);
				if (scanType != 0 && q_index.equals(scanType + "")) {
					txt.setSelected(true);
					LogUtils.eLog(TAG, "取出保存的 scanType：" + scanType);
				}
				scan_layout.addView(view);
			}
			if (tongList.get(i).t_3.equals("1")) {
				payList.add(TI);
				/* 添加到View */
				View view = LayoutInflater.from(this).inflate(
						R.layout.tongdao_item, pay_layout, false);
				TextView txt = (TextView) view.findViewById(R.id.text_item);
				txt.setText(tongList.get(i).q_n);
				txt.setTag(q_index);
				txt.setOnClickListener(mPayClick);
				if (payType != 0 && q_index.equals(payType + "")) {
					txt.setSelected(true);
					LogUtils.eLog(TAG, "取出保存的 payType：" + payType);
				}
				pay_layout.addView(view);
			}
		}
	}

	private OnClickListener mScanClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			for (int i = 0; i < scanList.size(); i++) {
				scan_layout.getChildAt(i).setSelected(false);
			}
			int now_click = Integer.parseInt(v.getTag() + "");
			scanType = now_click;
			LogUtils.dLog(TAG, "scan_layout select q: " + scanType);
			LogUtils.dLog(TAG,
					"scan_layout select name: "
							+ OtherUtils.getPayType(scanType + ""));
			v.setSelected(true);
		}
	};

	private OnClickListener mPayClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			for (int i = 0; i < payList.size(); i++) {
				pay_layout.getChildAt(i).setSelected(false);
			}
			int now_click = Integer.parseInt(v.getTag() + "");
			payType = now_click;
			LogUtils.dLog(TAG, "pay_layout select index: " + payType);
			LogUtils.dLog(TAG,
					"pay_layout select name: "
							+ OtherUtils.getPayType(payType + ""));
			v.setSelected(true);
			getCode();
		}
	};

	public String parsLocalPic(String path) {
		String parseOk = null;
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF8");

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // 获取新的大小
		// 缩放比0
		int be = (int) (options.outHeight / (float) 200);
		if (be <= 0)
			be = 1;
		options.inSampleSize = be;
		bitmap = BitmapFactory.decodeFile(path, options);
		RGBLuminanceSource source = new RGBLuminanceSource(bitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader2 = new QRCodeReader();
		Result result;
		try {
			result = reader2.decode(bitmap1, hints);
			android.util.Log.i("steven", "result:" + result);
			parseOk = result.getText();

		} catch (NotFoundException e) {
			parseOk = null;
		} catch (ChecksumException e) {
			parseOk = null;
		} catch (FormatException e) {
			parseOk = null;
		}
		return parseOk;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		LogUtils.vLog(TAG, "ConstantUtils.IS_WEIXIN_ID: " + ConstantUtils.IS_WEIXIN_ID);
		if (!ConstantUtils.IS_WEIXIN_ID) {
			LogUtils.vLog(TAG, "PayScanActivity onResume db_pay_type: " + db_pay_type);
			if (db_pay_type.equals("0")) {
				/* 扫码页面 */
				handler = null;
				lastResult = null;
				resetStatusView();
				surfaceView = (SurfaceView) findViewById(R.id.preview_view);
				SurfaceHolder surfaceHolder = surfaceView.getHolder();
				if (hasSurface) {
					initCamera(surfaceHolder);
				} else {
					surfaceHolder.addCallback(this);
					surfaceHolder
							.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
				}
				source = IntentSource.NONE;
				decodeFormats = null;
			} else {
				if (db_pay_type.equals("1")) {
					/* 刷卡页面 */
					view_scard();
				} else {
					if (db_pay_type.equals("3")) {
						/* 二维码页面 */
						view_paycode();
					}
				}
			}

		} else {
			LogUtils.eLog(TAG, "-----微信分享图片--onResume");
			iv_scancode.setSelected(false);
			iv_paycode.setSelected(true);
			iv_code.setSelected(false);
			status_view.setVisibility(View.VISIBLE);
			viewfinderView.setVisibility(View.GONE);
			surfaceView.setVisibility(View.GONE);
			image_code.setVisibility(View.VISIBLE);
			scan_layout.setVisibility(View.GONE);
			pay_layout.setVisibility(View.VISIBLE);
			card_layout.setVisibility(View.GONE);
			i_tip.setText(getString(R.string.home_qr_memo));
			isScanPage = false;
			db_pay_type = "3";
			closeCamera();
		}
	}

	@Override
	protected void onPause() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		if (mProgress != null) {
			mProgress.dismiss();
		}
		SharedUtils
				.setValue(mActivity, ConstantUtils.SCAN_INDEX, scanType + "");
		SharedUtils.setValue(mActivity, ConstantUtils.PAY_INDEX, payType + "");
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK :
				if ((source == IntentSource.NONE || source == IntentSource.ZXING_LINK)
						&& lastResult != null) {
					restartPreviewAfterDelay(0L);
					return true;
				}
				break;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 这里初始化界面，调用初始化相机
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			LogUtils.eLog(TAG,
					"*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	/**
	 * 处理扫描结果
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result rawResult, Bitmap barcode) {
		inactivityTimer.onActivity();
		lastResult = rawResult;
		String resultString = rawResult.getText();
		if (resultString.equals("")) {
			LogUtils.iLog(TAG, "---onActivityResult Scan failed!: null");
		} else {
			LogUtils.iLog(TAG, "---onActivityResult Scan seccess!: " + resultString);
			ToastShow.showShort(mActivity, getString(R.string.scan_seccess));
			orderid = scanType + DateUtils.getNowTime()
					+ OtherUtils.getSeverCount();
			CODE_INFO mCODE_INFO = new CODE_INFO();
			mCODE_INFO.o = orderid;
			/* 保存到流水数据库 */
			saveFlows(mCODE_INFO);
			post_action_rsa_index = 0;
			JSONObject jsonStr = new JSONObject();
			jsonStr.put("a", "payScan");
			jsonStr.put("i", i + "");
			jsonStr.put("j", price);
			jsonStr.put("c", resultString);
			jsonStr.put("q", scanType + "");
			jsonStr.put("t", "1");
			jsonStr.put("z", "1");
			jsonStr.put("o", orderid);
			jsonStr.put("tn", tn);
			jsonStr.put("post_url", ConstantUtils.SERVER_ADDRESSTES);
			jsonStr.put("post_key", ConstantUtils.URSAKEY);
			PayeeBusines.post_action_rsa(mActivity, jsonStr, this);
		}
	}

	private void showConfirm(String isPayState) {
		builder = new AlertDialog.Builder(mActivity, R.style.Theme_Transparent);
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_normal_tip, null);
		builder.setView(view);
		t_message = (TextView) view.findViewById(R.id.tv_tip);
		t_message.setText(T_message);
		b_ok = (Button) view.findViewById(R.id.btn_ok);
		b_cancel = (Button) view.findViewById(R.id.btn_cancel);
		b_ok.setOnClickListener(this);
		b_cancel.setOnClickListener(this);
		b_cancel.setVisibility(View.GONE);
		if (isPayState.equals("0")) {
			/* 支付失败 */
			b_ok.setText(getString(R.string.dig_next));
			b_cancel.setText(getString(R.string.dig_goback));
		} else {
			if (isPayState.equals("1")) {
				/* 支付成功 */
				b_ok.setText(getString(R.string.dig_next));
				b_cancel.setText(getString(R.string.dig_goback));
			} else {
				if (isPayState.equals("2")) {
					/* 支付等待【间联】 */
					b_ok.setText(getString(R.string.home_qr_status));
					b_cancel.setText(getString(R.string.dig_goback));
				}
			}
		}
		dialog = builder.create();
		dialog.show();
	}

	// 初始化照相机，CaptureActivityHandler解码
	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		try {
			CameraManager.get().openDriver(surfaceHolder);
			if (handler == null) {
				handler = new PayScanActivityHandler(this, decodeFormats,
						characterSet);
			}
		} catch (IOException ioe) {
			LogUtils.iLog(TAG, ioe.toString());
			displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			LogUtils.iLog(TAG, "Unexpected error initializing camera" + e);
			displayFrameworkBugMessageAndExit();
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

	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
		}
		resetStatusView();
	}

	private void resetStatusView() {
		viewfinderView.setVisibility(View.VISIBLE);
		lastResult = null;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

	void restartCamera() {
		LogUtils.dLog(TAG, "hasSurface " + hasSurface);

		viewfinderView.setVisibility(View.VISIBLE);

		surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		initCamera(surfaceHolder);

	}

	void closeCamera() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}

		// 关闭摄像头
		CameraManager.get().closeDriver();
	}

	/****** 获取二维码 ****/
	public void getCode() {
		Log.v("-------------- ------------", "getCode payType:" + payType);
		orderid = payType + DateUtils.getNowTime() + OtherUtils.getSeverCount();
		LogUtils.eLog(TAG, "getCode orderid: " + orderid);
		if (payType == 0) {
			ToastUtils.getInstances().showWaittingDialog();
			post_action_rsa_index = 2;
			/**** 一码付 ****/
			LogUtils.eLog(TAG, "getCode 一码付 start");
			orderid = OrderUtils.makeOrderNo();
			LogUtils.eLog(TAG, "getCode ymf_o: " + orderid);
			String member_id = SharedUtils.getValue(mActivity,
					ConstantUtils.MEMBER_ID);
			LogUtils.eLog(TAG, "getCode member_id: " + member_id);
			String md5_key = SharedUtils.getValue(mActivity, ConstantUtils.MD5);
			LogUtils.eLog(TAG, "getCode md5_key: " + md5_key);
			String md5_asc = OrderUtils.getOrderNo(md5_key, orderid, business_no,
					member_id, i, price, mActivity);
			String md5_encode = "";
			try {
				md5_encode = URLEncoder.encode(md5_asc, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				md5_encode = md5_asc;
			}
			LogUtils.eLog(TAG, "getCode end md5_encode: " + md5_encode);
			/* 显示在页面 */
			try {
				LogUtils.dLog(TAG, "getCode framing rect PAYYMF 开始生成二维码： " + md5_encode);
				LogUtils.dLog(TAG, "getCode framing rect PAYYMF 二维码宽高： " + code_width);
				code_bitmap = QRUtils.createQRCode(ConstantUtils.U_PAYYMF
						+ md5_encode, ConstantUtils.CODE_WIDTH);
				LogUtils.iLog(TAG, "bitmap： " + code_bitmap.getWidth());
				image_code.setImageBitmap(code_bitmap);
				ViewGroup.LayoutParams params = image_code.getLayoutParams();
				LogUtils.iLog(TAG, "getCode params 二维码宽高： " + params.width);
				LogUtils.iLog(TAG, "getCode params 二维码宽高： " + params.height);
				LogUtils.iLog(TAG, "getCode image_code： " + image_code.getVisibility());
				/* 保存到流水数据库 */
				mCODE_INFO = new CODE_INFO();
				mCODE_INFO.o = orderid;
				saveFlows(mCODE_INFO);

			} catch (Exception error) {
				error.printStackTrace();
			}
			ToastUtils.getInstances().dissWaittingDialog();
		} else {
			if (payType == 9) {
				/* 网银支付 */
				/* 判断刷卡类型 */
				int app_type = ConstantUtils.APP_VERSION_TYPE;
				if (app_type == 6) {
					/* 新大陆 */
					Log.v("-------------- ------------", "pos_card 新大陆 刷卡接口");

				} else {
					Log.v("-------------- ------------", "民生 刷卡接口");

				}

			} else {
				post_action_rsa_index = 1;
				JSONObject jsonStr = new JSONObject();
				jsonStr.put("a", "payCode");
				jsonStr.put("i", i + "");
				jsonStr.put("j", price);
				jsonStr.put("q", payType + "");
				jsonStr.put("t", "3");
				jsonStr.put("z", "1");
				jsonStr.put("o", orderid);
				jsonStr.put("tn", tn);
				jsonStr.put("post_url", ConstantUtils.SERVER_ADDRESSTES);
				jsonStr.put("post_key", ConstantUtils.URSAKEY);
				PayeeBusines.post_action_rsa(mActivity, jsonStr, this);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bundle bundle = data.getExtras();
		if (requestCode == 1 && bundle != null) {
			switch (resultCode) {
			// 支付成功
				case Activity.RESULT_OK :

					break;
				// 支付取消
				case Activity.RESULT_CANCELED :

				default :
					// TODO:
					break;
			}
		}
	}

	@Override
	public void onSuccess(BaseResponse response, String sonStr, int act) {
		LogUtils.iLog(TAG, "response.f: " + response.f);
		LogUtils.iLog(TAG, "act: " + act);
		if (response != null) {
			if (response.m != null) {
				T_message = response.m;
			}
			switch (post_action_rsa_index) {
				case 0 :
					/* 扫码支付接口 */
					if ("1".equals(response.f)) {
						/* 弹出支付完成对话框 */
						/* 支付成功 */
						isPaySeccess = "1";
						mPAYORDER_INFO = JSON.parseObject(response.d,
								PAYORDER_INFO.class);
						paySeccess(mPAYORDER_INFO);

					} else {
						if ("2".equals(response.f)) {
							/* 弹出支付完成对话框 */
							/* 间联 */
							isPaySeccess = "2";

						} else {
							/* 支付失败 */
							isPaySeccess = "0";
						}
					}
					showConfirm(isPaySeccess);
					break;
				case 1 :
					/* 二维码支付接口 */
					LogUtils.iLog(TAG,
							"PAYCODE onSuccess： "
									+ JSON.parseObject(response.d));
					if (response.d != null && "2".equals(response.f)) {
						mCODE_INFO = JSON.parseObject(response.d,
								CODE_INFO.class);
						/* 显示在页面 */
						try {
							LogUtils.dLog(TAG, "framing rect 开始生成二维码： " + mCODE_INFO.qr);
							LogUtils.dLog(TAG, "framing rect 二维码宽高： " + code_width);
							// Bitmap bitmap = QRUtils.encodeToQR(mCODE_INFO.qr,
							// code_width);
							code_bitmap = QRUtils.createQRCode(mCODE_INFO.qr,
									ConstantUtils.CODE_WIDTH);
							image_code.setImageBitmap(code_bitmap);
							/* 保存到流水数据库 */
							saveFlows(mCODE_INFO);

						} catch (Exception e) {
							e.printStackTrace();
						}
						ToastUtils.getInstances().dissWaittingDialog();
					} else {
						ToastShow.showShort(mActivity, response.m);
						ToastUtils.getInstances().dissWaittingDialog();
					}
					break;
				case 2 :
					/* 一码付接口 */
					LogUtils.iLog(TAG,
							"PAYYMF onSuccess： " + JSON.parseObject(response.d));
					if (response.d != null && "2".equals(response.f)) {
						mCODE_INFO = JSON.parseObject(response.d,
								CODE_INFO.class);
						/* 显示在页面 */
						try {
							LogUtils.dLog(TAG, "framing rect PAYYMF 开始生成二维码： "
									+ mCODE_INFO.qr);
							LogUtils.dLog(TAG, "framing rect PAYYMF 二维码宽高： "
									+ code_width);
							code_bitmap = QRUtils.createQRCode(mCODE_INFO.qr,
									ConstantUtils.CODE_WIDTH);
							LogUtils.iLog(TAG, "bitmap： " + code_bitmap.getWidth());
							image_code.setImageBitmap(code_bitmap);
							ViewGroup.LayoutParams params = image_code
									.getLayoutParams();
							LogUtils.iLog(TAG, "params 二维码宽高： " + params.width);
							LogUtils.iLog(TAG, "params 二维码宽高： " + params.height);
							LogUtils.iLog(TAG,
									"image_code： " + image_code.getVisibility());
							fatherLayout.bringChildToFront(image_code);
							fatherLayout.updateViewLayout(image_code,
									image_code.getLayoutParams());
							/* 保存到流水数据库 */
							saveFlows(mCODE_INFO);

						} catch (Exception e) {
							e.printStackTrace();
						}
						ToastUtils.getInstances().dissWaittingDialog();
					} else {
						ToastShow.showShort(mActivity, response.m);
						ToastUtils.getInstances().dissWaittingDialog();
					}
					break;
				case 3 :
					/* 支付状态查询 */
					ToastUtils.getInstances().dissWaittingDialog();
					ToastShow.showShort(mActivity, response.m);

					/* 查询支付状态 */
					if ("1".equals(response.f)) {
						/* 支付成功 */
						mPAYORDER_INFO = JSON.parseObject(response.d,
								PAYORDER_INFO.class);
						paySeccess(mPAYORDER_INFO);
						finish();

					} else {
						/* 等待支付 */

					}
					break;
				case 5 :
					/* 刷卡接口完成流水保存 */
					/* 刷卡接口返回参数 保存服务器 */
					ToastUtils.getInstances().dissWaittingDialog();
					if ("1".equals(response.f)) {
						isPaySeccess = "1";
						showConfirm(isPaySeccess);
					} else {
						/* 交易成功，流水保存失败 */
						ToastShow.showShort(mActivity, "交易成功，流水记录保存失败。错误信息："
								+ response.m);
						/* 记录到本地 */
						CODE_INFO mCODE_INFO = new CODE_INFO();
						mCODE_INFO.o = orderid;
						/* 保存到流水数据库 */
						saveFlows(mCODE_INFO);
					}
					break;
				default :
					break;
			}
		}
	}

	private void saveFlows(CODE_INFO mCODE_INFO) {
		orderid = mCODE_INFO.o;
		/* 插入数据库 */
		Map<String, String> mapValues = new HashMap<String, String>();
		mapValues.put("i", i);
		mapValues.put("business_no", business_no);
		mapValues.put("pay_type", db_pay_type);
		if (isScanPage) {
			/** 扫码支付 */
			mapValues.put("trade_type", scanType + "");
			LogUtils.iLog(TAG, "扫码支付   保存流水数据库信息： " + scanType);
		} else {
			/** 二维码支付 */
			LogUtils.iLog(TAG, "二维码支付   保存流水数据库信息： " + payType);
			mapValues.put("trade_type", payType + "");
		}
		mapValues.put("pay_flag", "0");
		mapValues.put("total_money", price);
		mapValues.put("real_money", "0");
		mapValues.put("tuikuan_money", "0");
		mapValues.put("hexiao_money", "0");
		mapValues.put("u", "");
		mapValues.put("out_trade_no", mCODE_INFO.o);
		mapValues.put("trade_no", "");
		mapValues.put("go", "");
		mapValues.put("dt", DateUtils.getNowTime());
		boolean isSaveOK = dbHelper.saveCell(ConstantUtils.TABLE_NAME_FLOWS,
				mapValues);
		if (!isSaveOK) {
			LogUtils.iLog(TAG, "dbHelper.saveCell saveFlows: " + isSaveOK);
		}
	}

	private void updateFlows(PAYORDER_INFO mPAYORDER_INFO) {
		/* 更新流水数据库信息 */
		Map<String, String> mapValues = new HashMap<String, String>();
		mapValues.put("pay_flag", "1");
		mapValues.put("total_money", price);
		mapValues.put("real_money", price);
		mapValues.put("tuikuan_money", "0");
		mapValues.put("hexiao_money", "0");
		mapValues.put("u", mPAYORDER_INFO.u);
		mapValues.put("hexiao_money", "0");
		/*mapValues.put("out_trade_no", mPAYORDER_INFO.oo);*/
		mapValues.put("trade_no", mPAYORDER_INFO.o);
		mapValues.put("go", mPAYORDER_INFO.go);
		mapValues.put("dt", DateUtils.getNowTime());
		boolean isSaveOK = dbHelper.updataCell(ConstantUtils.TABLE_NAME_FLOWS,
				mapValues, "where out_trade_no = '" + orderid + "'");
		if (!isSaveOK) {
			LogUtils.iLog(TAG, "dbHelper.saveCell updateFlows: " + isSaveOK);
		}
	}

	private void paySeccess(PAYORDER_INFO mPAYORDER_INFO) {
		/* 更改数据库状态 */
		updateFlows(mPAYORDER_INFO);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.body_title_left_but :
				finish();
				break;
			case R.id.iv_scancode :
				iv_scancode.setSelected(true);
				iv_paycode.setSelected(false);
				iv_code.setSelected(false);
				status_view.setVisibility(View.INVISIBLE);
				viewfinderView.setVisibility(View.VISIBLE);
				surfaceView.setVisibility(View.VISIBLE);
				image_code.setVisibility(View.GONE);
				scan_layout.setVisibility(View.VISIBLE);
				pay_layout.setVisibility(View.GONE);
				card_layout.setVisibility(View.GONE);
				i_tip.setText(getString(R.string.home_sm_memo));
				isScanPage = true;
				db_pay_type = "0";
				/* 重新初始化 */
				restartCamera();
				break;
			case R.id.iv_paycode :
				view_paycode();
				payType = 0;
				/* 请求二维码接口 */
				getCode();
				break;
			case R.id.iv_code :
				LogUtils.vLog(TAG, "iv_code click------ view_scard");
				view_scard();
				payType = 9;
				LogUtils.vLog(TAG, "iv_code click------ getCode");
				/* 请求刷卡接口 */
				getCode();
				break;

			case R.id.status_view :
				if (payType == 9) {
					/* 刷卡支付重试 */
					/* 请求刷卡接口 */
					getCode();
				} else {
					post_action_rsa_index = 3;
					ToastUtils.getInstances().showWaittingDialog();
					/* 获取支付状态 */
					JSONObject jsonStr = new JSONObject();
					jsonStr.put("a", "payChk");
					jsonStr.put("o", orderid);
					jsonStr.put("tn", tn);
					jsonStr.put("post_url", ConstantUtils.SERVER_ADDRESSTES);
					jsonStr.put("post_key", ConstantUtils.URSAKEY);
					PayeeBusines.post_action_rsa(mActivity, jsonStr, this);
				}
				break;
			case R.id.btn_cancel :
				LogUtils.vLog(TAG, "222222222222222222iv_code isPaySeccess------ getCode");
				dialog.dismiss();
				LogUtils.vLog(TAG, "222222222222222222iv_code isPaySeccess:" + isPaySeccess);
				if (isPaySeccess.equals("0")) {
					/* 支付失败 */
					finish();
				} else {
					if (isPaySeccess.equals("1")) {
						/* 支付成功 */
						/* 返回主页 */
						Intent intent = new Intent(this, MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					} else {
						if (isPaySeccess.equals("2")) {
							/* 支付等待【间联】 */
							finish();
						}
					}
				}
				break;
			case R.id.btn_ok :
				if (isPaySeccess.equals("0")) {
					/* 支付失败 */
					/* 重新扫描 */
					/* 重新初始化 */
					viewfinderView.drawViewfinder();
					CameraManager.init(mActivity, update_ui_handler);
					SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
					SurfaceHolder surfaceHolder = surfaceView.getHolder();
					initCamera(surfaceHolder);
					if (handler != null) {
						handler.restartPreviewAndDecode();
					}
					
				} else {
					if (isPaySeccess.equals("1")) {
						/* 支付成功 */
						/* 继续收款 */
						finish();
					} else {
						if (isPaySeccess.equals("2")) {
							/* 支付等待【间联】 */
							ToastUtils.getInstances().showWaittingDialog();
							/* 获取支付状态 */
							JSONObject jsonStr = new JSONObject();
							jsonStr.put("a", "payChk");
							jsonStr.put("o", orderid);
							jsonStr.put("tn", tn);
							jsonStr.put("post_url",
									ConstantUtils.SERVER_ADDRESSTES);
							jsonStr.put("post_key", ConstantUtils.URSAKEY);
							PayeeBusines.post_action_rsa(mActivity, jsonStr,
									this);
						}
					}
				}
				dialog.dismiss();
				break;
			default :
				break;
		}
	}

	private void view_paycode() {
		iv_scancode.setSelected(false);
		iv_paycode.setSelected(true);
		iv_code.setSelected(false);
		status_view.setVisibility(View.VISIBLE);
		status_view.setText(mActivity.getString(R.string.home_qr_status));
		viewfinderView.setVisibility(View.GONE);
		surfaceView.setVisibility(View.GONE);
		image_code.setVisibility(View.VISIBLE);
		scan_layout.setVisibility(View.GONE);
		pay_layout.setVisibility(View.VISIBLE);
		card_layout.setVisibility(View.GONE);
		i_tip.setText(getString(R.string.home_qr_memo));
		isScanPage = false;
		db_pay_type = "3";
		closeCamera();
	}

	private void view_scard() {
		iv_scancode.setSelected(false);
		iv_paycode.setSelected(false);
		iv_code.setSelected(true);
		status_view.setVisibility(View.INVISIBLE);
		viewfinderView.setVisibility(View.GONE);
		surfaceView.setVisibility(View.GONE);
		image_code.setVisibility(View.INVISIBLE);
		scan_layout.setVisibility(View.GONE);
		pay_layout.setVisibility(View.GONE);
		card_layout.setVisibility(View.VISIBLE);
		i_tip.setText(getString(R.string.home_scard_memo));
		isScanPage = false;
		db_pay_type = "1";
		closeCamera();
	}

	@Override
	public void onError(BusinessException e, int act) {

	}
}
