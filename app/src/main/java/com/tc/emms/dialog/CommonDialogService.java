package com.tc.emms.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.tc.emms.R;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.utils.ToastShow;
import com.tc.emms.utils.ToastUtils;

/**
 * Created by 于德海 on 2016/9/8.
 * 
 * @version ${VERSION}
 * @decpter
 */
@SuppressLint("NewApi")
public class CommonDialogService extends Service
		implements
			CommonDialogListener {

	private static String TAG = "CommonDialogService";
	private static Dialog dialog;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		ToastUtils.getInstances().setListener(this);// 绑定

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtils.vLog(TAG, "CommonDialogService onDestroy: " + dialog);
		if (dialog != null) {
			dialog.cancel();
			dialog.dismiss();
			dialog = null;
		}
	}

	@SuppressLint("InflateParams")
	private void showDialog(String _title, String _summary) {
		LogUtils.vLog(TAG, "showDialog dialog: " + dialog);
		LogUtils.vLog(TAG, "showDialog CommonData.mNowContext: "
				+ CommonData.mNowContext);
		if (dialog == null && CommonData.mNowContext != null) {

			/**
			 * 自定义的window alert
			 */
			dialog = new Dialog(CommonData.mNowContext,
					R.style.Theme_Transparent);
			View v = LayoutInflater.from(this).inflate(
					R.layout.dialog_simple_tip, null);
			TextView tv_tip_title = (TextView) v
					.findViewById(R.id.tv_tip_title);
			tv_tip_title.setText(_title);

			TextView tv_tip = (TextView) v.findViewById(R.id.tv_tip);
			tv_tip.setText(_summary);
			dialog.setContentView(v);
			dialog.setCancelable(false);
			dialog.getWindow().setGravity(Gravity.CENTER);
			dialog.getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT);
			v.findViewById(R.id.btn_ok).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							cancel();
						}
					});
			dialog.show();

		} else {
			ToastShow.showShort(CommonData.applicationContext, "有误");
		}
	}

	@Override
	@SuppressLint("InflateParams")
	public void showWaittingDialog() {
		LogUtils.vLog(TAG, "showWattingDialog dialog: " + dialog);
		LogUtils.vLog(TAG, "showWattingDialog CommonData.mNowContext: "
				+ CommonData.mNowContext);
		if (dialog == null && CommonData.mNowContext != null) {
			dialog = new Dialog(CommonData.mNowContext,
					R.style.Theme_Transparent);
			View v = LayoutInflater.from(this).inflate(
					R.layout.waitting_loading, null);
			dialog.setContentView(v);
			dialog.setCanceledOnTouchOutside(false);
			if (!dialog.isShowing()) {
				LogUtils.eLog(TAG,
						"dialog == null showWaittingDialog 已存在 waittingDialog,没有显示。开启显示");
				dialog.show();
			} else {
				LogUtils.eLog(TAG,
						"dialog == null showWaittingDialog 已存在 waittingDialog,已显示。跳过");
			}
		} else {
			LogUtils.eLog(TAG, "showWaittingDialog 已存在 waittingDialog");
			if (!dialog.isShowing()) {
				LogUtils.eLog(TAG,
						"showWaittingDialog 已存在 waittingDialog,没有显示。开启显示");
				dialog.show();
			} else {
				LogUtils.eLog(TAG,
						"showWaittingDialog 已存在 waittingDialog,已显示。跳过");
			}
		}
	}

	@Override
	public void show(String _title, String _summary) {
		showDialog(_title, _summary);
	}

	@Override
	public void cancel() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	@Override
	public void dissWaittingDialog() {
		// TODO Auto-generated method stub
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}
}
