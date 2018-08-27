package com.tc.emms.adapter;

import java.util.List;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.tc.emms.R;
import com.tc.emms.base.BaseActivity;

public class WelViewAdapter extends PagerAdapter {

	private List<View> views;
	private BaseActivity activity;

	public WelViewAdapter(List<View> views, BaseActivity act) {
		this.views = views;
		this.activity = act;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(views.get(arg1));
	}

	@Override
	public void finishUpdate(View arg0) {
		// TO DO Auto-generated method stub

	}

	@Override
	public int getCount() {
		if (views != null) {
			return views.size();
		}

		return 0;
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {

		((ViewPager) arg0).addView(views.get(arg1), 0);

		if (arg1 == views.size() - 1) {
			TextView login = (TextView) arg0.findViewById(R.id.iv_Indicator);

			login.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					goToBusinessActivity();
				}
			});
		}

		return views.get(arg1);

	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// TO DO Auto-generated method stub

	}

	@Override
	public Parcelable saveState() {
		// TO DO Auto-generated method stub
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
		// TO DO Auto-generated method stub

	}

	private void goToBusinessActivity() {
		// 查看是否登录
//		Intent intent = new Intent(this.activity, LoginActivity.class);
//		this.activity.startActivity(intent);
//		this.activity.finish();
	}

}
