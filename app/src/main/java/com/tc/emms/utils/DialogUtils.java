package com.tc.emms.utils;

import android.app.Dialog;

import com.tc.emms.R;
import com.tc.emms.base.BaseActivity;

/*
 * Dialog 统一管理
 */

public class DialogUtils {

	/**
	 * 通用的动画对话框
	 * 
	 * @param context
	 */

	public static Dialog commonAnimDialog(BaseActivity context) {
		final Dialog dialog = new Dialog(context, R.style.Theme_Transparent);
		dialog.setContentView(R.layout.waitting_loading);
		return dialog;
	}
}
