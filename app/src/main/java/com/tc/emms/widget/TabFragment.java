package com.tc.emms.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tc.emms.R;

public class TabFragment extends Fragment {
	private String mTitle = "Default";

	public TabFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (getArguments() != null) {
			mTitle = getArguments().getString("title");
		}

		TextView textView = new TextView(getActivity());
		textView.setTextSize(13);
		textView.setBackgroundColor(getResources().getColor(R.color.main_backgroud));
		textView.setGravity(Gravity.CENTER);
		textView.setText(mTitle);
		return textView;
	}
}
