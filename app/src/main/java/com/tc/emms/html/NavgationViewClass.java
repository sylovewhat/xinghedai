package com.tc.emms.html;

import java.util.ArrayList;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tc.emms.R;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.model.TitleBar;
import com.tc.emms.ui.MainActivity;
import com.tc.emms.ui.WebActivity;
import com.tc.emms.utils.ActivityManager;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.ImageUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.utils.SharedUtils;

import com.tc.emms.utils.StatusBarUtils;
import com.tc.emms.widget.RedTipTextView;
import com.tc.emms.widget.X5WebView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * 导航栏功能库方法 
 */

public class NavgationViewClass {
	private static String TAG = "NavgationViewClass";
	/* 统一方法库回调 */
	private static void webViewReturn(final X5WebView mWebView,
			final String returnMethodName, final JSONObject jObject) {
		if (returnMethodName != null && !returnMethodName.equals("")) {
			LogUtils.vLog(TAG, "---统一方法库回调 jObject:" + jObject.toString());
			mWebView.post(new Runnable() {
				@Override
				public void run() {
					//加载网页中的方法
					mWebView.loadUrl("javascript:" + returnMethodName + "(" + jObject
							+ ")");
				}
			});
		}
	}
	
	/***
	 * 状态栏颜色设置
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return 设备ID
	 */
	public static void setStatusBarBackgroundColor(String jsonString,
												   String returnMethodName, final BaseActivity mActivity,
												   X5WebView mWebView, View headerView, View footerView,
												   Handler handler) {
		Message msg = new Message();
		String _obj = TAG + " setStatusBarBackgroundColor seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		String color = "";//状态栏颜色
		String full_screen = "";//是否全屏显示
		int color_values=R.color.main_blue;
//		int iv_resource = R.color.main_blue;
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				final String keyString = entry.getKey();
				// 提取value
				final String valueString = entry.getValue().toString();
				if (keyString != null && valueString != null) {
					if (keyString.equals("color")) {
						color=valueString;
						if (color.equals("")) {
							 /* 设置成默认主题颜色 */
							color_values = R.color.main_blue;
			            } else if (color.equals("clearcolor")) {
				            /* 设置成透明颜色 */
							color_values = R.color.transparent;
			            } else {
							color_values = Color.parseColor(color);
			            }
						SharedUtils.setInt(mActivity, ConstantUtils.APP_BACKGROUD_COLOR, color_values);
					}else if(keyString.equals("full_screen"))
                    {
                    	full_screen=valueString;
                    	Log.d("sylove","H5传递过来的值1"+full_screen);
						SharedUtils.setInt(mActivity, ConstantUtils.APP_fULL_SCREEN,Integer.parseInt(full_screen ));
						Log.d("sylove","H5传递过来的值2"+Integer.parseInt(full_screen ));
                    }






//						//状态栏颜色变化
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mActivity.runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        String colorvalue = "";//状态栏颜色值
//                                        String full_screen = "";//是否全屏显示
//                                        if (keyString.equals("color")) {
//                                                colorvalue=valueString;
//                                        }else if(keyString.equals("full_screen"))
//                                        {
//                                            full_screen=valueString;
//                                        }


//                                        StatusBarUtils.setCompat(mActivity);
//                                        if(full_screen.equals("yes")){
//                                            StatusBarUtils.setStatusBarMode(mActivity,false);
//                                        }else
//                                        {
//                                            StatusBarUtils.setStatusBarMode(mActivity,true);
//											if (colorvalue .equals("") )
//											{
//												StatusBarUtils.setColor(mActivity, R.color.main_blue);
//											}else if(colorvalue .equals("clearcolor"))
//											{
//												StatusBarUtils.setColor(mActivity, R.color.transparent);
//											}else
//											{
//												StatusBarUtils.setColor(mActivity,Color.parseColor(colorvalue));
//											}
//                                        }


//                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                            //是否执行全屏
//                                            if(full_screen.equals("yes"))
//                                            {
//                                            	//SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                                                mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                                                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                                                Log.d("sylove","我执行了全屏操作");
//
//                                            }else
//                                            {
//                                                mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                                                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                                                Log.d("sylove","我没有执行了全屏操作");
//                                            }
//                                            //创建系统状态栏对象
//                                            SystemBarTintManager tintManager = new SystemBarTintManager(mActivity);
//                                            //激活系统状态栏对象
//                                            tintManager.setStatusBarTintEnabled(true);
//                                            if (colorvalue .equals("") ) {
//                                                tintManager.setStatusBarTintColor(R.color.main_blue);
//                                            } else if (colorvalue .equals("clearcolor") ) {
//                                                tintManager.setStatusBarTintColor(R.color.transparent);
//                                            } else {
//                                                tintManager.setStatusBarTintColor( Color.parseColor(colorvalue));
//                                            }
//
//                                        }
//                                    }
//                                });
//                            }
//                        }).start();













				} else {
					_obj = "key or value is null";
					_what = ConstantUtils.HANDLER_FAILE;
				}
			}

//			if (color.equals("")) {
//				/* 设置成默认主题颜色 */
//				iv_resource = R.color.main_blue;
//			} else if (color.equals("clearcolor")) {
//				/* 设置成透明颜色 */
//				iv_resource = R.color.transparent;
//			} else {
//				//iv_resource = ImageUtils.getResourceID(mActivity, color);
//				iv_resource = Color.parseColor(color);
//			}
//			SharedUtils.setInt(mActivity, ConstantUtils.APP_BACKGROUD_COLOR, iv_resource);


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
	 * 头部导航栏显示／隐藏(新增加)
	 *
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return 设备ID
	 */
	public static void NewnavgationViewBarLoad(String jsonString,
											String returnMethodName, final BaseActivity mActivity,
											X5WebView mWebView, final View headerView, View footerView,
											Handler handler) {
		Message msg = new Message();
		String _obj = TAG + " navgationViewBarLoad seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				final String valueString = entry.getValue().toString();
				if (keyString != null && valueString != null) {
					if (keyString.equals("showType")) {
							if (valueString.equals("0")) {
								SharedUtils.setInt(mActivity, ConstantUtils.IS_TITLE_BAR,Integer.parseInt("0"));
							}else{
								SharedUtils.setInt(mActivity, ConstantUtils.IS_TITLE_BAR,Integer.parseInt("1"));
							}
						} else {
							_obj = "headerView is null";
							_what = ConstantUtils.HANDLER_FAILE;
						}


				} else {
					_obj = "key or value is null";
					_what = ConstantUtils.HANDLER_FAILE;
				}
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
	 * 头部导航栏显示／隐藏
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return 设备ID
	 */
	public static void navgationViewBarLoad(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			X5WebView mWebView, final View headerView, View footerView,
			Handler handler) {
		Message msg = new Message();
		String _obj = TAG + " navgationViewBarLoad seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				final String valueString = entry.getValue().toString();
				if (keyString != null && valueString != null) {
					if (keyString.equals("showType")) {
						if (headerView != null) {


//							if (valueString.equals("0")) {
//								new Thread(new Runnable() {
//									@Override
//									public void run() {
//										mActivity.runOnUiThread(new Runnable() {
//											@Override
//											public void run() {
//												View view = (View)headerView.getParent();
//												view.setVisibility(View.VISIBLE);
//												headerView.setVisibility(View.VISIBLE);
//											}
//										});
//									}
//								}).start();
//								LogUtils.vLog(TAG,
//										"--- navgationViewBarLoad 头部导航栏显示");
//							} else {
////								Log.d("sylove","隐藏头部导航栏000000000000000000000000000000000000000000000000");
////								headerView.setVisibility(View.GONE);
////								LogUtils.vLog(TAG,
////										"--- navgationViewBarLoad 头部导航栏隐藏");
//
//							}


							new Thread(new Runnable() {
								@Override
								public void run() {
									mActivity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											if (valueString.equals("0")) {
												View view = (View)headerView.getParent();
												view.setVisibility(View.VISIBLE);
												headerView.setVisibility(View.VISIBLE);
												LogUtils.vLog(TAG, "--- navgationViewBarLoad 头部导航栏显示");
											} else {
												headerView.setVisibility(View.GONE);
												LogUtils.vLog(TAG, "--- navgationViewBarLoad 头部导航栏隐藏");
											}

										}
									});
								}
							}).start();

						} else {
							_obj = "headerView is null";
							_what = ConstantUtils.HANDLER_FAILE;
						}
					}

				} else {
					_obj = "key or value is null";
					_what = ConstantUtils.HANDLER_FAILE;
				}
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
	 * 头部导航栏颜色变化
	 *
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return 设备ID
	 */
	public static void navColorChange(final String jsonString,
										 String returnMethodName, final BaseActivity mActivity,
										 final X5WebView mWebView, final View headerView, View footerView,
										 final Handler handler) {
		Message msg = new Message();
		String _obj = TAG + " navgationViewBarLoad seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				final String valueString = entry.getValue().toString();
				if (keyString != null && valueString != null) {

					if (keyString.equals("alpha")) {
						Log.d("sylove","进行第一页状态栏的操作"+valueString);

						if (headerView != null) {

								new Thread(new Runnable() {
									@Override
									public void run() {
										mActivity.runOnUiThread(new Runnable() {
											@Override
											public void run() {
												if (valueString.equals("0")) {
													headerView.setAlpha(0);
													headerView.setVisibility(View.VISIBLE);
												}else if(valueString.equals("1"))
												{
													headerView.setAlpha(1);
													headerView.setVisibility(View.GONE);
												}
											}
										});
									}
								}).start();
						} else {
							_obj = "headerView is null";
							_what = ConstantUtils.HANDLER_FAILE;
						}
					}

				} else {
					_obj = "key or value is null";
					_what = ConstantUtils.HANDLER_FAILE;
				}
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
	 * 头部导航栏值预设
	 *
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return 设备ID
	 */
	public static void makeNavgationPresetValue(final String jsonString,
										 String returnMethodName, final BaseActivity mActivity,
										 final X5WebView mWebView, final View headerView, View footerView,
										 final Handler handler) {
		Message msg = new Message();
		String _obj = TAG + " makeNavgationView seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		if (headerView == null) {
			_obj = "headerView is null";
			_what = ConstantUtils.HANDLER_FAILE;
			msg.obj = _obj;
			msg.what = _what;
			handler.sendMessage(msg);
			return;
		}
		if (ConstantUtils.heardListText == null) {
			ConstantUtils.heardListText = new ArrayList<TitleBar>();//底部页面按钮集合
		}
		try {
            JSONObject object = JSON.parseObject(jsonString);
            //创建TitleBar对象，用于预设值
            TitleBar mTitleBar=new TitleBar();
            for (Map.Entry<String, Object> entry : object.entrySet()) {
                // 提取key
                String keyString = entry.getKey();
                // 提取value
                String valueString = entry.getValue()
                        .toString();

                if (keyString != null && valueString != null) {

                    //是否隐藏
                    if (keyString.equals("is_hide")) {
                        mTitleBar.setMis_hide(valueString);
                    }
                    //背景预设
                    if (keyString.equals("backgroudColor")) {
                        mTitleBar.setmBackgroudColore(valueString);
                    }
                    //左边标题
                    if (keyString.equals("leftViewTitle"))
                    {
                        mTitleBar.setMleftViewTitle(valueString);
                    }
                    //左边图片
                    if (keyString.equals("leftViewImg")){
                        mTitleBar.setMleftViewImg(valueString);
                    }
                    //左边标题颜色
                    if (keyString.equals("leftTitleColor")) {
                        mTitleBar.setMleftTitleColor(valueString);
                    }

                    //中间图片
                    if (keyString.equals("centerViewImg")) {
                        mTitleBar.setMcenterViewImg(valueString);
                    }
                    //中间标题
                    if (keyString.equals("centerTitle")) {
                        mTitleBar.setmCenterTitle(valueString);
                    }
                    //中间标题颜色
                    if (keyString.equals("centerTitleColor")) {
                        mTitleBar.setMcenterTitleColor(valueString);
                    }
                    //右边图片
                    if (keyString.equals("rightViewImg")) {
                        mTitleBar.setMrightViewImg(valueString);
                    }
                    //右边标题
                    if (keyString.equals("rightViewTitle")) {
                        Log.d("sylove","右边值预设==============================="+valueString);
                        mTitleBar.setMrightViewTitle(valueString);
                    }
                    //右边标题颜色
                    if (keyString.equals("rightTitleColor")) {
                        mTitleBar.setMrightTitleColor(valueString);
                    }
					//显示的界面名字（这个要用来做判断用）
					if (keyString.equals("activityName")) {
						mTitleBar.setActivityName(valueString);
					}
					//是否全屏
					if (keyString.equals("is_ShowStatusBar")) {
						mTitleBar.setIs_ShowStatusBar(valueString);
					}

                }
            }
            ConstantUtils.heardListText.add(mTitleBar);

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
	 * 头部导航栏设置
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return 设备ID
	 */
	public static void makeNavgationView(final String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, final View headerView, View footerView,
			final Handler handler) {
		Message msg = new Message();
		String _obj = TAG + " makeNavgationView seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		if (headerView == null) {
			_obj = "headerView is null";
			_what = ConstantUtils.HANDLER_FAILE;
			msg.obj = _obj;
			msg.what = _what;
			handler.sendMessage(msg);
			return;
		}
		try {
			new Thread() {
				@Override
				public void run() {
					handler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							ImageView iv_back = (ImageView) headerView
									.findViewById(R.id.iv_back);
							TextView tv_left = (TextView) headerView
									.findViewById(R.id.tv_left);
							ImageView img_title = (ImageView) headerView
									.findViewById(R.id.img_title);
							TextView title_text = (TextView) headerView
									.findViewById(R.id.tv_title);
							ImageView iv_right = (ImageView) headerView
									.findViewById(R.id.iv_right);
							TextView tv_right = (TextView) headerView
									.findViewById(R.id.tv_right);

							iv_back.setVisibility(View.INVISIBLE);
							tv_left.setVisibility(View.INVISIBLE);
							img_title.setVisibility(View.GONE);
							iv_right.setVisibility(View.INVISIBLE);
							tv_right.setVisibility(View.INVISIBLE);
							LogUtils.eLog(TAG, "makeNavgationView iv_back:" + iv_back.getVisibility());
							JSONObject object = JSON.parseObject(jsonString);
							// 动态获取key值
							for (Map.Entry<String, Object> entry : object
									.entrySet()) {
								// 提取key
								String keyString = entry.getKey();
								// 提取value
								String valueString = entry.getValue()
										.toString();
								if (keyString != null && valueString != null) {
									/* 设置导航栏 */
									if (keyString.equals("backgroudColor")) {
										if(!valueString.equals("none")){
											LogUtils.eLog(TAG, "导航栏背景颜色1:" + valueString);
											/* 背景颜色 */
											LogUtils.eLog(TAG, "导航栏背景颜色2:" + Color
													.parseColor(valueString));
											View m_header =  (View) headerView
													.findViewById(R.id.m_header);
											m_header.setBackgroundColor(Color
													.parseColor(valueString));

											headerView.setBackgroundColor(Color
													.parseColor(valueString));
										}



//										if(valueString.equals("1")) {
////											/* 背景颜色 */
//											LogUtils.eLog(TAG, "导航栏背景颜色透明:" + valueString);
////											LogUtils.eLog(TAG, "导航栏背景颜色透明:" + Color
////													.parseColor(valueString));
//											View m_header =  (View) headerView
//													.findViewById(R.id.m_header);
//											m_header.getBackground().setAlpha(0);
//										}else {
//											View m_header =  (View) headerView
//													.findViewById(R.id.m_header);
//											m_header.setBackgroundColor(Color
//													.parseColor(valueString));
//
//											headerView.setBackgroundColor(Color
//													.parseColor(valueString));
//										}
										
									}
									if (keyString.equals("leftViewImg")) {
										/* 左边显示的图片 */
										LogUtils.eLog(TAG, "makeNavgationView iv_back leftViewImg:" + iv_back.getVisibility());
										if (!valueString.equals("")) {
											int iv_resource = ImageUtils
													.getResourceID(mActivity,
															valueString);
											if (iv_resource > 0) {
												iv_back.setVisibility(View.VISIBLE);
												iv_back.setImageResource(iv_resource);
												iv_back.setOnClickListener(new OnClickListener() {

													@Override
													public void onClick(View v) {
														// TODO leftBtnClick
														LogUtils.eLog(TAG,
																"左边图片 点击 leftBtnClick");
														mWebView.loadUrl("javascript:leftBtnClick()");
													}
												});
											} else {
												iv_back.setVisibility(View.INVISIBLE);
											}
										} else {
											iv_back.setVisibility(View.INVISIBLE);
										}
									}

									if (keyString.equals("leftViewTitle")) {
										/* 左边标题 */

										tv_left.setText(valueString);
										tv_left.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												// TODO title右边文字点击事件
												LogUtils.eLog(TAG,
														"左边文字  点击 leftBtnClick");
												mWebView.loadUrl("javascript:leftBtnClick()");
											}
										});
										tv_left.setVisibility(View.VISIBLE);
										iv_back.setVisibility(View.GONE);
									}
									if (keyString.equals("leftTitleColor")) {
										/* 左边标题文字颜色，默认白色 */
										tv_left.setTextColor(Color
												.parseColor(valueString));
									}
									if (keyString.equals("centerViewImg")) {
										/* 中间设置显示的图片 */

										if (!keyString.equals("")) {
											int iv_resource = ImageUtils
													.getResourceID(mActivity,
															valueString);
											if (iv_resource > 0) {
												img_title
														.setVisibility(View.VISIBLE);
												img_title
														.setImageResource(iv_resource);
											} else {
												img_title
														.setVisibility(View.GONE);
											}
										} else {
											img_title.setVisibility(View.GONE);
										}
									}
									if (keyString.equals("centerTitle")) {
										/* 中间标题 */
										LogUtils.eLog(TAG, "centerTitle:"
												+ valueString);
										title_text.setText(valueString);
									}
									if (keyString.equals("centerTitleColor")) {
										/* 中间标题文字颜色，默认白色 */
										title_text.setTextColor(Color
												.parseColor(valueString));
									}
									if (keyString.equals("rightViewImg")) {
										/* 右边设置显示的图片 */
										if (!keyString.equals("")) {
											int iv_resource = ImageUtils
													.getResourceID(mActivity,
															valueString);
											if (iv_resource > 0) {
												iv_right.setVisibility(View.VISIBLE);
												tv_right.setVisibility(View.GONE);
												iv_right.setImageResource(iv_resource);
												iv_right.setOnClickListener(new OnClickListener() {

													@Override
													public void onClick(View v) {
														// TODO title右边文字点击事件
														LogUtils.eLog(TAG,
																"右边图片  点击 rightBtnClick");
														mWebView.loadUrl("javascript:rightBtnClick()");
													}
												});
											} else {
												iv_right.setVisibility(View.GONE);
											}
										} else {
											iv_right.setVisibility(View.GONE);
										}
									}
									if (keyString.equals("rightViewTitle")) {
										LogUtils.eLog(TAG, "rightViewTitle:"
												+ valueString);
										/* 右边标题 */
										tv_right.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												// TODO title右边文字点击事件
												LogUtils.eLog(TAG,
														"右边文字  点击 rightBtnClick");
												mWebView.loadUrl("javascript:rightBtnClick()");
											}
										});
										tv_right.setText(valueString);
										tv_right.setVisibility(View.VISIBLE);
										iv_right.setVisibility(View.GONE);
									}
									if (keyString.equals("rightTitleColor")) {
										/* 右边标题文字颜色 */
										tv_right.setTextColor(Color
												.parseColor(valueString));
									}

								}
							}




						}
					});
				}
			}.start();

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
	 * 底部导航栏显示／隐藏
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return 设备ID
	 */
	public static void tabBarLoad(String jsonString, String returnMethodName,
			final BaseActivity mActivity, X5WebView mWebView, View headerView,
			View footerView,final Handler handler) {
		LogUtils.vLog(TAG, "--- tabBarLoad 底部导航栏控制");
		
		Message msg = new Message();
		String _obj = TAG + " tabBarLoad seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		try {
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				final String valueString = entry.getValue().toString();
				if (keyString != null && valueString != null) {
					if (keyString.equals("showType")) {
						new Thread() {
						     public void run() {
						      handler.post(new Runnable() {

						       @Override
						       public void run() {
						    	   MainActivity footView=(MainActivity) mActivity;
									if (footView.footerView != null) {
										if (valueString.equals("0")) {
											LogUtils.vLog(TAG, "--- tabBarLoad 底部导航栏显示");
											footView.initFootView.setVisibility(View.VISIBLE);
										} else {
											LogUtils.vLog(TAG, "--- tabBarLoad 底部导航栏隐藏");
											footView.initFootView.setVisibility(View.VISIBLE);
										}
									} else {
										LogUtils.vLog(TAG, "--- tabBarLoad is null");
									}
						       }
						      });
						     }
						    };
						
					}

				} else {
					_obj = "key or value is null";
					_what = ConstantUtils.HANDLER_FAILE;
				}
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
	 * 显示第几栏【首页】页
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return 设备ID
	 */
	public static void selectTabBar(String jsonString, String returnMethodName,
			BaseActivity mActivity, X5WebView mWebView, View headerView,
			View footerView, Handler handler) {
		Message msg = new Message();
		String _obj = TAG + " selectTabBar seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		try {
			JSONObject object = JSON.parseObject(jsonString);
			String selectNum = "";
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && valueString != null) {
					if (keyString.equals("selectNum")) {
						selectNum = valueString;
					}

				} else {
					_obj = "key or value is null";
					_what = ConstantUtils.HANDLER_FAILE;
				}
			}
			if (selectNum != null && !selectNum.equals("")) {
				LogUtils.iLog(TAG, "tiptip selectTabBar selectNum: "
						+ selectNum);
				MainActivity.setTabIndex(Integer.parseInt(selectNum));
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
	 * 底导航栏设置（这边实际上是做了底部导航栏值的预设）
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return 设备ID
	 */
	static String r_url_html = "";//html名字 比如index.html
	static String r_url_data = "";//html加载参数  没有的话写no
	static String r_webLoadType = "";//加载方式：0，为html加载方式；1，为url加载方式
	static String r_title = "";//选项卡标题
	static String r_image = "";//选项卡图标
	static String r_selectedImage = "";//选中后图标
	/* 新增 */
	static String r_selectedLabelColor = "";//底部文字选中颜色
	static String r_navLoadType = "";//头部导航栏是否可见
	@SuppressLint({"CutPasteId", "InflateParams"})
	public static void setTabBar(String jsonString, String returnMethodName,
			final BaseActivity mActivity, final X5WebView mWebView,
			View headerView, final View footerView, final Handler handler) {
		Message msg = new Message();
		String _obj = TAG + " setTabBar seccess.";//设置成功
		int _what = ConstantUtils.HANDLER_SECCESS;  //handle回调参数
		if (ConstantUtils.footListText == null) {
			ConstantUtils.footListText = new ArrayList<View>();//底部页面按钮集合
			ConstantUtils.WebViews = new ArrayList<X5WebView>();//集合webview对象
		}
		try {
			String _url_html = "";
			String _url_data = "";
			String _webLoadType = "";
			String _title = "";
			String _image = "";
			String _selectedImage = "";
			String _index = "";
			/* 新增 */
			String _selectedLabelColor = "";
			String _navLoadType="";//是否显示头部导航栏  0，有导航栏；1，无导航栏；2，置顶到电池栏
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && valueString != null) {
					/* 设置导航栏 跳转页面*/
					if (keyString.equals("url_html")) {
						_url_html = valueString;
						r_url_html = valueString;
					}
					/* 设置导航栏 跳转页面参数*/
					if (keyString.equals("url_data")) {
						_url_data = valueString;
						r_url_data = valueString;
					}
					/* 设置导航栏 页面加载类型*/
					if (keyString.equals("webLoadType")) {
						_webLoadType = valueString;
						r_webLoadType = valueString;
					}
					/* 设置导航栏 底部标题*/
					if (keyString.equals("title")) {
						_title = valueString;
						r_title = valueString;
					}
					/* 设置导航栏 底部图标*/
					if (keyString.equals("image")) {
						_image = valueString;
						r_image = valueString;
					}
					/* 设置导航栏 选择后的图标*/
					if (keyString.equals("selectedImage")) {
						_selectedImage = valueString;
						r_selectedImage = valueString;
					}
					/* 设置导航栏 底部选项卡位置*/
					if (keyString.equals("index")) {
						_index = valueString;
					}
					/* 设置导航栏 选择后的文字颜色*/
					if (keyString.equals("selectedLabelColor")) {
						_selectedLabelColor = valueString;
						r_selectedLabelColor = valueString;
					}

					/* 设置头部导航栏是否可以见*/
					if (keyString.equals("navLoadType")) {
						_navLoadType = valueString;
						r_navLoadType = valueString;
					}

				} else {
					_obj = "key or value is null";
					_what = ConstantUtils.HANDLER_FAILE;
				}
			}




			/* 判断是否替换 */
			/* 不替换 */
			if (_index.equals("")) {
				//ConstantUtils.footListText.size() 最先的时候这个是0，是后来一个个加载装填上来的
				/* **************** 新增 **************** */
				Log.d("sylove","新增"+ConstantUtils.footListText.size());


				LayoutInflater inflater = LayoutInflater.from(mActivity);
				//创建底部视图对象
				final View view = inflater.inflate(R.layout.main_footer_item,
						null);
				RedTipTextView footView = (RedTipTextView) view
						.findViewById(R.id.btn_text);
				//设置跳转页面
				if (_url_html != "") {
					footView.setUrlHtml(_url_html);
				}
				//设置页面参数
				if (_url_data != "" && _url_data != "no") {
					footView.setUrlData(_url_data);
				}
				//设置加载类型
				if (_webLoadType != "") {
					footView.setWebLoadType(_webLoadType);
				}
				//设置标题
				if (_title != "") {
					footView.setTitleText(_title);
				}
				//设置图标
				if (_image != "" && _selectedImage != "") {
					LogUtils.iLog(TAG, "setTabBar setTextDrawable _image: "
							+ _image);
					LogUtils.iLog(TAG,
							"setTabBar setTextDrawable _selectedImage: "
									+ _selectedImage);
					footView.setTextDrawable(mActivity, _image, _selectedImage);
				}
				//设置选中后的文字颜色
				if (_selectedLabelColor != "") {
					footView.setTextColor("", _selectedLabelColor);
				}

				//设置头部导航栏的是否可见
				if (_navLoadType != "") {
					footView.set_navLoadType(_navLoadType);
				}


				//将视图添加到集合中去
				ConstantUtils.footListText.add(view);
			} else {

				/* **************** 替换 **************** */
				final int _size = Integer.parseInt(_index);
				LogUtils.iLog(TAG, "setTabBar view 直接替换: " + _size);
				final View _view = ConstantUtils.footListText.get(_size);
				final RedTipTextView footView = (RedTipTextView) _view
						.findViewById(R.id.btn_text);

				if (ConstantUtils.footListText.size() > _size) {
					new Thread() {
						@Override
						public void run() {
							handler.post(new Runnable() {

								@Override
								public void run() {

									if (r_url_html != "") {
										footView.setUrlHtml(r_url_html);
									}
									if (r_url_data != "" && r_url_data != "no") {
										footView.setUrlData(r_url_data);
									}
									if (r_webLoadType != "") {
										footView.setWebLoadType(r_webLoadType);
									}
									if (r_title != "") {
										footView.setTitleText(r_title);
									}
									if (r_image != "" && r_selectedImage != "") {
										LogUtils.iLog(TAG,
												"setTabBar setTextDrawable _image: "
														+ r_image);
										LogUtils.iLog(TAG,
												"setTabBar setTextDrawable _selectedImage: "
														+ r_selectedImage);
										footView.setTextDrawable(mActivity,
												r_image, r_selectedImage);
									}
									if (r_selectedLabelColor != "") {
										footView.setTextColor("",
												r_selectedLabelColor);
									}
									LogUtils.iLog(TAG, "Thread setTabBar 替换完成 ");
									ConstantUtils.footListText
											.set(_size, _view);
									for (int i = 0; i < ConstantUtils.footListText
											.size(); i++) {
										View _view1 = ConstantUtils.footListText
												.get(i);
										RedTipTextView footView1 = (RedTipTextView) _view1
												.findViewById(R.id.btn_text);
										LogUtils.iLog(TAG,
												"Thread setTabBar 替换完成  footView："
														+ footView1.getText()
																.toString());
									}
								}
							});
						}
					}.start();
				}
			}

			LogUtils.iLog(TAG, "setTabBar mActivity.footListText size: "
					+ ConstantUtils.footListText.size());
			LogUtils.iLog(TAG, "setTabBar mActivity.WebViews size: "
					+ ConstantUtils.WebViews.size());
			if (returnMethodName != null && !returnMethodName.equals("")) {

				/* 统一方法库回调   调用网页中的回调方法*/
				webViewReturn(mWebView, returnMethodName, new JSONObject());
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
	 * 返回
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return 设备ID
	 */
	public static void back(String jsonString, String returnMethodName,
			final BaseActivity mActivity, final X5WebView mWebView,
			View headerView, View footerView, final Handler handler) {
            //执行返回的动作一定要和webview所在的线程处于同一线程，否则会出现返回按钮无效的bug
		    mActivity.runOnUiThread(new Runnable() {
			@Override
			   public void run() {
		             if (mWebView != null && mWebView.canGoBack()) {
		             	 mWebView.goBack();// 返回前一个页面
		             } else {
			             mActivity.finish();
						 mActivity.getActivityManager().endActivity(mActivity);
			             Log.d("sylove","结束当前界面AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		             }

			        }
		       });
		Message msg = new Message();
		msg.obj = TAG + " back seccess.";
		msg.what = ConstantUtils.HANDLER_SECCESS;
		handler.sendMessage(msg);
	}
	
	/***
	 * 退出
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return 设备ID
	 */
	public static void outToApp(String jsonString, String returnMethodName,
			final BaseActivity mActivity, final X5WebView mWebView,
			View headerView, View footerView, final Handler handler) {

		mActivity.finish();
		System.exit(0);
	}

	/* 进入底部导航栏,跳转到首页 */
	public static void goToTabBar(String jsonString, String returnMethodName,
			final BaseActivity mActivity, final X5WebView mWebView,
			View headerView, View footerView, final Handler handler) {

		Intent intent = new Intent(mActivity, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mActivity.startActivity(intent);
		mActivity.finish();

		Message msg = new Message();
		msg.obj = TAG + " goToTabBar seccess.【跳转】";
		msg.what = ConstantUtils.HANDLER_SECCESS;
		handler.sendMessage(msg);
	}

	/***
	 * 退出当前账号
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return 设备ID
	 */
	public static void outToLogin(String jsonString, String returnMethodName,
			final BaseActivity mActivity, final X5WebView mWebView,
			View headerView, View footerView, final Handler handler) {

		SharedUtils.setBooleanValue(mActivity, ConstantUtils.IS_SAVE_LOGIN,
				false);
		SharedUtils.setValue(mActivity, ConstantUtils.USERPASSWORD, "");
		SharedUtils.setBooleanValue(mActivity, ConstantUtils.MAIN_SHOW_INDEX,
				true);

		ConstantUtils.footListText = new ArrayList<View>();
		ConstantUtils.WebViews = new ArrayList<X5WebView>();

		Intent intent = new Intent(mActivity, WebActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(ConstantUtils.HTML_NAME, ConstantUtils.LOGIN_HTML);
		intent.putExtra(ConstantUtils.HTML_URL, ConstantUtils.LOGIN_HTML);
		mActivity.startActivity(intent);
		mActivity.overridePendingTransition(R.anim.slide_left_in,
				R.anim.slide_right_out);
		mActivity.finish();
	}
	
	/***
	 * 退出系统
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return 设备ID
	 */
	public static void systemExit(String jsonString, String returnMethodName,
			final BaseActivity mActivity, final X5WebView mWebView,
			View headerView, View footerView, final Handler handler) {
		mActivity.finish();
		System.exit(0);
	}

	/***
	 * 轮播点击控制
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return 设备ID
	 */
	public static void preventParentTouchEvent(String jsonString,
			String returnMethodName, final BaseActivity mActivity,
			final X5WebView mWebView, View headerView, View footerView,
			final Handler handler) {
		/* 设置轮播焦点 */
		/*mWebView.preventParentTouchEvent();*/
	}


	/***
	 * 返回指定页面
	 * 
	 * @param jsonString
	 *            传入字符串参数
	 * @param returnMethodName
	 *            返回方法
	 * @param mActivity
	 *            当前视图
	 * @param mWebView
	 *            对象
	 * @return 设备ID
	 */
	public static void backMain(String jsonString, String returnMethodName,
			final BaseActivity mActivity, final X5WebView mWebView,
			View headerView, View footerView, final Handler handler) {
		Message msg = new Message();
		String _obj = TAG + " backMain seccess.";
		int _what = ConstantUtils.HANDLER_SECCESS;
		/* String className = AppUtils.getPackage() + ".ui." + valueString; */
		/* 返回指定页面 */
		try {
			String backIndex = "";//大于0的话回退到最初的级数 ，小小于0的话，指定回退几级，具体的级数看下面的backNum
			String backNum = "";//回退的级数
			JSONObject object = JSON.parseObject(jsonString);
			// 动态获取key值
			for (Map.Entry<String, Object> entry : object.entrySet()) {
				// 提取key
				String keyString = entry.getKey();
				// 提取value
				String valueString = entry.getValue().toString();
				if (keyString != null && valueString != null) {

					if (keyString.equals("backIndex")) {
						backIndex = valueString;
						LogUtils.eLog(TAG, "backIndex:" + backIndex);
					}
					if (keyString.equals("backNum")) {
						backNum = valueString;
						LogUtils.eLog(TAG, "backNum:" + backNum);
					}

				} else {
					_obj = "key or value is null";
					_what = ConstantUtils.HANDLER_FAILE;
				}
			}

			if (backIndex.equals("1")) {
				Log.d("sylove","回退到首页+++++++++========================================");
				Intent intent = new Intent(mActivity, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				mActivity.startActivity(intent);

			} else {
				if (backIndex.equals("2")) {
					LogUtils.iLog(TAG, "backMain backIndex 回退到登录: " + backIndex);
					Intent intent = new Intent(mActivity, WebActivity.class);
					intent.putExtra(ConstantUtils.HTML_NAME, ConstantUtils.LOGIN_HTML);
					intent.putExtra(ConstantUtils.HTML_URL, ConstantUtils.LOGIN_HTML);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					mActivity.startActivity(intent);

				} else {
					if (!backNum.equals("")) {
						int back = Integer.parseInt(backNum);
						LogUtils.iLog(TAG, "backMain back: " + back);
						ActivityManager aManager = mActivity.getActivityManager();
						boolean isSeccess = aManager.finishActivityExceptIndex(back);
						LogUtils.iLog(TAG,
								"backMain finishActivityExceptIndex isSeccess: "
										+ isSeccess);
						Log.d("sylove","finish 结束掉某一层界面成功"+isSeccess);
						if (isSeccess) {

						} else {
							_obj = "error: backNum大小越界";
							_what = ConstantUtils.HANDLER_FAILE;
						}
					}
				}
				
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
}
