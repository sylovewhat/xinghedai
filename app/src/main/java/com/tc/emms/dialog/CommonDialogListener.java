package com.tc.emms.dialog;

/**
 * Created by 于德海 on 2016/9/8.
 *
 * @version ${VERSION}
 * @decpter
 */
public interface CommonDialogListener {
	void showWaittingDialog();
	void dissWaittingDialog();
    void show(String _title, String _summary);
    void cancel();
}
