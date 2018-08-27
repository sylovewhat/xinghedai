package com.tc.emms.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tc.emms.R;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.bean.BaseResponse;
import com.tc.emms.service.BusinessException;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.ToastShow;

/**
 * @author 小本
 * @Time 2015年3月30日 下午4:55:56
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

	/** 分享到微信接口 **/
	private IWXAPI mWxApi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mWxApi = WXAPIFactory.createWXAPI(BaseActivity.mActivity,
				ConstantUtils.WEIXIN_ID, false);
		mWxApi.registerApp(ConstantUtils.WEIXIN_ID);
		mWxApi.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		mWxApi.handleIntent(intent, this);
	}

	/***
	 * 请求微信的相应码
	 * 
	 * @author YOLANDA
	 * @param arg0
	 */
	@Override
	public void onResp(BaseResp baseResp) {
		switch (baseResp.errCode) {
			case BaseResp.ErrCode.ERR_OK :
				ToastShow.showShort(
						BaseActivity.mActivity,
						BaseActivity.mActivity.getResources().getString(
								R.string.wx_share_return01));
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL :
				ToastShow.showShort(
						BaseActivity.mActivity,
						BaseActivity.mActivity.getResources().getString(
								R.string.wx_share_return02));
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED :
				ToastShow.showShort(
						BaseActivity.mActivity,
						BaseActivity.mActivity.getResources().getString(
								R.string.wx_share_return03));
				break;
			default :

				break;
		}
		// TODO 微信分享 成功之后调用接口
		this.finish();
	}

	/** 微信主动请求我们 **/
	@Override
	public void onReq(BaseReq baseResp) {

	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onSuccess(BaseResponse response, String jsonStr,  int act) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(BusinessException e, int act) {
		// TODO Auto-generated method stub

	}
}
