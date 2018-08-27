package com.tc.emms.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tc.emms.R;
import com.tc.emms.adapter.TabPagerAdapter;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.base.BaseFunction;
import com.tc.emms.bean.BaseResponse;
import com.tc.emms.config.BaseApplictaion;
import com.tc.emms.service.BusinessException;
import com.tc.emms.utils.ActivityManager;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.FileUtils;
import com.tc.emms.utils.HtmlUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.utils.SharedUtils;
import com.tc.emms.utils.StatusBarUtils;
import com.tc.emms.utils.SystemBarTintManager;
import com.tc.emms.widget.CustomViewPager;
import com.tc.emms.widget.RedTipTextView;
import com.tc.emms.widget.X5WebView;

@SuppressLint({"InflateParams", "NewApi", "HandlerLeak"})
public class MainActivity extends BaseActivity implements OnPageChangeListener {
	private static String TAG = "MainActivity";

	private BaseActivity mActivity;
	private static CustomViewPager mViewPager;
	private View headerView;//头部总体布局
	private LinearLayout m_header;//头部标题栏背景
	private ImageView iv_back;//左边图片
	private ImageView iv_right;//右边图片
	private TextView tv_title;//中间标题
	private View title_bar_view;//状态栏


	public View initFootView;
	public LinearLayout footerView;

	private LayoutInflater mInflater;
	private View tab_all;
	private List<View> views = new ArrayList<View>();
	private TabPagerAdapter adapter;
	private static int tab_index = 0;
	private static String u_path = "";
	private BaseFunction mBaseFunction;
	private ViewGroup mViewGroup;
	private X5WebView mX5WebView = null;
	/* 返回键退出 */
	private long exitTime = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StatusBarUtils.setCompat(this);
		StatusBarUtils.setStatusBarMode(this,false);

		mActivity = this;
		setContentView(R.layout.activity_main);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		u_path = FileUtils.getU_PATH(mActivity);//界面路径
		initView();
		LogUtils.vLog(TAG, "*****************************MainActivity onCreate");
        mActivity.getActivityManager().finishAllActivity();

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		mActivity.getActivityManager().finishAllActivity();
	}




	@SuppressLint("HandlerLeak")
	@SuppressWarnings("deprecation")
	private void initView() {
		mViewPager = (CustomViewPager) findViewById(R.id.viewPager);
		headerView = findViewById(R.id.main_header_view);
		m_header= (LinearLayout) headerView.findViewById(R.id.m_header);
		iv_back= (ImageView) headerView.findViewById(R.id.iv_back);
		iv_right= (ImageView) headerView.findViewById(R.id.iv_right);
		tv_title= (TextView) headerView.findViewById(R.id.tv_title);
		title_bar_view= (View)headerView.findViewById(R.id.title_bar_view);

//		headerView.setVisibility(View.GONE);

		//headerView.setVisibility(View.VISIBLE);
		initFootView = findViewById(R.id.main_footer_view);
			footerView = (LinearLayout) initFootView
					.findViewById(R.id.footer_layout);
			mInflater = LayoutInflater.from(mActivity);

		/* 添加底部导航栏 **/
		if (ConstantUtils.footListText != null
				&& ConstantUtils.footListText.size() > 0) {
			LogUtils.eLog(TAG, "main footerView size:"
					+ ConstantUtils.footListText.size());
			for (int i = 0; i < ConstantUtils.footListText.size(); i++) {
				View _view = ConstantUtils.footListText.get(i);

				_view.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,
						1.0f));
				_view.setTag(i);
				_view.setOnClickListener(this);
				RedTipTextView footView = (RedTipTextView) _view
						.findViewById(R.id.btn_text);
				Log.d("sylove","main footerView add footView text:"+ footView.getText().toString());
				Log.d("sylove","main footerView add footView img:"+ footView.getCompoundDrawables());

				footerView.addView(_view);
				LogUtils.eLog(TAG, "main footerView add:" + _view.getTag());
				tab_all = mInflater.inflate(R.layout.tabmain_view, null);
				views.add(tab_all);
				mViewGroup = (ViewGroup) tab_all
						.findViewById(R.id.webView_frame);
				mX5WebView = new X5WebView(this, null);
				mBaseFunction = new BaseFunction(this, mX5WebView, headerView,
						initFootView);
				mX5WebView.addJavascriptInterface(mBaseFunction,
						ConstantUtils.HTML_KEY);
				ConstantUtils.WebViews.add(mX5WebView);
				LayoutParams layout = new FrameLayout.LayoutParams(
						LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT);
				mViewGroup.addView(mX5WebView, layout);

			}
			adapter = new TabPagerAdapter(views);
			mViewPager.setAdapter(adapter);
			mViewPager.setOnPageChangeListener(this);
			//设置viewpager不可滑动
			mViewPager.setOnViewPagerScrollAction(new CustomViewPager.setOnViewPagerScrollAction() {
				@Override
				public boolean unAbleScroll() {
					return true;
				}
			});
		} else {
			LogUtils.vLog(TAG, "main mActivity.footListText: null or size = 0");
		}
	}


	@SuppressLint("HandlerLeak")
	private void htmlUpdate(final RedTipTextView footView) {
		String _htmlName = footView.getUrlHtml();
		Log.v("isHtmlUpdate", "--- isHtmlUpdate WebViews:"
				+ ConstantUtils.WebViews.size());
		mX5WebView = ConstantUtils.WebViews.get(tab_index);
		Log.v("isHtmlUpdate", "--- isHtmlUpdate _htmlName:" + _htmlName);

		/* 查看当前html是否需要下载更新 */
		HtmlUtils.isHtmlUpdate(mActivity, _htmlName, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				String _path = ConstantUtils.U_BASE + u_path + footView.getUrlHtml();
				if(ConstantUtils.IS_OPEN_DEBUG){
					/* 测试环境地址 */
					final String local_downurl = SharedUtils.getValue(
							BaseApplictaion.getInstance(),
							ConstantUtils.UPDATE_LOCAL_MAIN);
					Log.d("", "498test down local:" + local_downurl);
					_path = local_downurl + footView.getUrlHtml();
				}
				
				LogUtils.vLog(TAG, "main mX5WebView.open:" + _path);
				switch (msg.what) {
					case ConstantUtils.ACT_HTML_COM :
						mX5WebView.loadUrl(_path);
						footView.setis_loaded(true);
						break;
					case ConstantUtils.ACT_HTML_FAILE :

						LogUtils.iLog(TAG, "下载失败。MainActivity打开默认页面: " + _path);
						mX5WebView.loadUrl(_path);
						footView.setis_loaded(true);
						break;
					default :
						break;
				}
			}
		});
	}

	//选中某张视图
	@Override
	public void onPageSelected(int position) {
		LogUtils.eLog(TAG, "OnPageChangeListener position:" + position);
		tab_index = position;
		showTitleBar(position+1);
		showTabView();
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	public static void setTabIndex(int _index) {
		LogUtils.iLog(TAG, "setTabIndex: " + _index);
		tab_index = _index;
	}

	public static int getTabIndex() {
		LogUtils.iLog(TAG, "getTabIndex: " + tab_index);
		return tab_index;
	}

	/**
	 * 显示标题栏
	 */
	private void showTitleBar(int i){
		if (ConstantUtils.heardListText != null && ConstantUtils.heardListText.size() > 0) {

			if(ConstantUtils.heardListText.get(i).getIs_ShowStatusBar().equals("0"))
			{
				title_bar_view.setVisibility(View.GONE);
				headerView.setVisibility(View.GONE);
				Log.d("sylove","隐藏头部标题栏==============");

			}else
			{
				Log.d("sylove","显示头部标题栏==============");
				headerView.setVisibility(View.VISIBLE);
				title_bar_view.setVisibility(View.VISIBLE);
				int color_value = Color.parseColor(ConstantUtils.heardListText.get(0).getmBackgroudColore());
				title_bar_view.setBackgroundColor(color_value);
			}

			//如果背景颜色不为空就设置背景颜色
			if(!ConstantUtils.heardListText.get(i).getmBackgroudColore().equals(""))
			{
				int mBackgroundColor=Color.parseColor(ConstantUtils.heardListText.get(i).getmBackgroudColore());
				m_header.setBackgroundColor(mBackgroundColor);
				Log.d("sylove","设置头部标题栏背景颜色——————");
			}

			//如果中间标题不为空就设置中间标题
			if(!ConstantUtils.heardListText.get(i).getmCenterTitle().equals(""))
			{
				tv_title.setText(ConstantUtils.heardListText.get(i).getmCenterTitle());
				Log.d("sylove","设置头部标题栏中间标题++++++++++++");
			}

			//如果左边图标为none就不显示左边图标
			if(ConstantUtils.heardListText.get(i).getMleftViewImg().equals("none"))
			{
				iv_back.setVisibility(View.GONE);
				Log.d("sylove","设置左边图标不可见++++++++++++");
			}

			//如果左右边图标为none就不显示右边边图标
			if(ConstantUtils.heardListText.get(i).getMrightViewImg().equals("none"))
			{
				iv_right.setVisibility(View.GONE);
				Log.d("sylove","设置右边图标不可见++++++++++++");
			}

		}
    }


	//显示视图
	private void showTabView() {
		LogUtils.iLog(TAG, "showTabView tab_index: " + tab_index);
		mViewPager.setCurrentItem(tab_index);

		if (ConstantUtils.footListText != null
				&& ConstantUtils.footListText.size() > 0) {
			LogUtils.vLog(TAG, "showTabView all.size:"
					+ ConstantUtils.footListText.size());
			for (int i = 0; i < ConstantUtils.footListText.size(); i++) {
				View _view = ConstantUtils.footListText.get(i);
				RedTipTextView footView = (RedTipTextView) _view
						.findViewById(R.id.btn_text);
				if (tab_index == i) {
//					String navLoadType=footView.get_navLoadType();
//					Log.d("sylove","传递的值：处理是否显示头部导航栏的=============================================="+navLoadType);
//
//					StatusBarUtils.setCompat(this);
//					if(navLoadType.equals("0"))
//                    {
//						StatusBarUtils.setStatusBarMode(this,true);
//						int color_value = Color.parseColor(ConstantUtils.heardListText.get(0).getmBackgroudColore());
//						StatusBarUtils.setColor(MainActivity.this, color_value);
//                        headerView.setVisibility(View.VISIBLE);
//                    }else if(navLoadType.equals("1"))
//                    {
//						StatusBarUtils.setStatusBarMode(this,true);
////						StatusBarUtils.setColor(this, Color.RED);
//                        headerView.setVisibility(View.GONE);
//
//                    }else if(navLoadType.equals("2"))
//                    {
//						StatusBarUtils.setStatusBarMode(this,false);
//                        headerView.setVisibility(View.GONE);
//                    }


					LogUtils.iLog(TAG, "showTabView footView true text: "
							+ footView.getText().toString());
					footView.setSelected(true);
					String _path = ConstantUtils.U_BASE + u_path
							+ footView.getUrlHtml();
					LogUtils.vLog(TAG, "showTabView WebView.open:" + _path);
					X5WebView mX5WebView = ConstantUtils.WebViews.get(i);
					LogUtils.vLog(TAG, "showTabView WebView.open old path:"
							+ mX5WebView.getUrl());
					LogUtils.vLog(TAG, "showTabView WebView.open is_loaded:"
							+ footView.getis_loaded());
					if (footView.getis_loaded()) {
						mX5WebView.loadUrl("javascript:viewWillAppear()");
					} else {
						footView.setis_loaded(true);
						htmlUpdate(footView);
					}
					ConstantUtils.PushWebView=mX5WebView;
				} else {
					LogUtils.iLog(TAG, "showTabView footView false text: "
							+ footView.getText().toString());
					footView.setSelected(false);
				}
			}
		} else {
			LogUtils.vLog(TAG,
					"showTabView mActivity.footListText: null or size = 0");
		}
	}

	@Override
	public void onClick(View v) {
		tab_index = (int) v.getTag();
		LogUtils.iLog(TAG, "onClick tab_index:" + tab_index);
		mViewPager.setCurrentItem(tab_index);
	}

	/* 消息通知hander,用来刷新图标 */
	public static Handler notice_ui_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 更改页面
			switch (msg.what) {
				case ConstantUtils.ACT_NOTICE_NORMAIL :
					LogUtils.iLog(TAG, "onNotification 通知到达。当前页面 tab_index:"
							+ tab_index);
					LogUtils.iLog(TAG, "onNotification 通知到达。开始判断当前页面 ");

					/* 刷新通知图标 */
					/* set_notice_icon(true); */
					break;
				default :
					break;
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtils.iLog(TAG, "退出");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				exitTime = System.currentTimeMillis();
				mX5WebView.loadUrl("javascript:andBackbtn()");
			} else {
				LogUtils.iLog(TAG, "双击退出2");
				mX5WebView.loadUrl("javascript:andExitbtn()");
			}
			return true;
			
		}
		return super.onKeyDown(keyCode, event);
	}
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		boolean isCanBack = SharedUtils.getBooleanValue(this, ConstantUtils.IS_OPEN_BACK, true);
//		LogUtils.vLog(TAG, "*****************************onKeyDown2："+isCanBack);
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if ((System.currentTimeMillis() - exitTime) > 2000) {
//				ToastShow.showShort(mActivity,
//						getString(R.string.main_again_logout));
//				exitTime = System.currentTimeMillis();
//			} else {
//				finish();
//				System.exit(0);
//			}
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}

	@Override
	public void onSuccess(BaseResponse response, String jsonStr, int act) {

	}

	@Override
	public void onError(BusinessException e, int act) {

	}

	@Override
	protected void onResume() {
		super.onResume();
		
		if (ConstantUtils.LoginWebView != null) {
			LogUtils.vLog(TAG, "---onPageFinished 回调 LoginWebView:"
					+ ConstantUtils.CookieStr);
			ConstantUtils.LoginWebView.post(new Runnable() {
				@Override
				public void run() {
					JSONObject object = JSON
							.parseObject(ConstantUtils.CookieStr);
					ConstantUtils.LoginWebView
							.loadUrl("javascript:backCookieDic(" + object + ")");
					
					ConstantUtils.LoginWebView = null;
				}
			});
		} else {
			LogUtils.iLog(TAG, "onPageFinished ConstantUtils.LoginWebView: "
					+ ConstantUtils.LoginWebView);
			boolean isIndex = SharedUtils.getBooleanValue(mActivity,
					ConstantUtils.MAIN_SHOW_INDEX, false);
			if (isIndex) {
				/* 退出后显示首页 */
				tab_index = 0;
				SharedUtils.setBooleanValue(mActivity,
						ConstantUtils.MAIN_SHOW_INDEX, false);
			}
			LogUtils.iLog(TAG, "tiptip main onResume: " + tab_index);
			showTabView();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
