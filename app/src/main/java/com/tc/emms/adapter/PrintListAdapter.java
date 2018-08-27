package com.tc.emms.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tc.emms.R;
import com.tc.emms.model.PRINT_ITEMS;

@SuppressLint("InflateParams") 
public class PrintListAdapter extends BaseAdapter {

	private Context context;
	private List<PRINT_ITEMS> userList;

	public PrintListAdapter(Context context, List<PRINT_ITEMS> userList) {
		this.context = context;
		this.userList = userList;
	}

	@Override
	public int getCount() {
		return userList.size();
	}

	@Override
	public Object getItem(int position) {
		return userList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.print_bt_item, null);
			holder = new ViewHolder();
			holder.select_list_item = (LinearLayout) convertView
					.findViewById(R.id.select_list_item);
			holder.userName = (TextView) convertView
					.findViewById(R.id.print_name);
			holder.userAdd = (TextView) convertView
					.findViewById(R.id.print_add);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final RadioButton radio = (RadioButton) convertView
				.findViewById(R.id.radio_btn);
		holder.rdBtn = radio;

		holder.userName.setText(userList.get(position).p_name);
		holder.userAdd.setText(userList.get(position).p_add);
		holder.rdBtn.setChecked(userList.get(position).isCheck);

		return convertView;
	}

	static class ViewHolder {
		LinearLayout select_list_item;
		TextView userName;
		TextView userAdd;
		RadioButton rdBtn;
	}

}