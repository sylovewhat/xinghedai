package com.tc.emms.widget;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tc.emms.R;
import com.tc.emms.adapter.AbstractWheelTextAdapter;
import com.tc.emms.wheelview.OnWheelChangedListener;
import com.tc.emms.wheelview.OnWheelScrollListener;
import com.tc.emms.wheelview.WheelView;

public class ChangeWheelPopwindow extends PopupWindow
		implements
			View.OnClickListener {

	private LinearLayout lin_wheel;
	private WheelView wvProvince;
	private WheelView wvCitys;
	private WheelView wvArea;
	private View lyChangeAddressChild;
	private TextView btnSure;
	private TextView btnCancel;

	private ArrayList<String> arrProvinces = new ArrayList<String>();
	private ArrayList<String> arrCitys = new ArrayList<String>();
	private ArrayList<String> arrAreas = new ArrayList<String>();
	private AddressTextAdapter provinceAdapter;
	private AddressTextAdapter cityAdapter;
	private AddressTextAdapter areaAdapter;

	private String strProvince = "广东";
	private String strCity = "深圳";
	private String strArea = "福田区";
	private String strJson = "";
	private OnAddressCListener onAddressCListener;

	private int maxsize = 14;
	private int minsize = 12;
	private String oneKey = "";
	private String twoKey = "";
	private String threeKey = "";

	private int oneSelect = 0;

	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	public ChangeWheelPopwindow(final Context context, JSONArray fristAry,
			final JSONArray secondAry, final JSONArray thirdAry, String oneKey,
			String twoKey, String threeKey, String cellNum) {
		super(context);
		this.oneKey = oneKey;
		this.twoKey = twoKey;
		this.threeKey = threeKey;
		View view = View.inflate(context,
				R.layout.edit_changeaddress_pop_layout, null);
		lin_wheel = (LinearLayout) view.findViewById(R.id.lin_wheel);
		LayoutInflater mInflater = LayoutInflater.from(context);
		wvProvince = (WheelView) mInflater.inflate(R.layout.wheel_item, null);
		wvCitys = (WheelView) mInflater.inflate(R.layout.wheel_item, null);
		wvArea = (WheelView) mInflater.inflate(R.layout.wheel_item, null);
		wvProvince.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));
		wvCitys.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));
		wvArea.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));

		int _length = Integer.parseInt(cellNum);
		switch (_length) {
			case 1 :
				lin_wheel.addView(wvProvince);
				break;
			case 2 :
				lin_wheel.addView(wvProvince);
				lin_wheel.addView(wvCitys);
				break;
			case 3 :
				lin_wheel.addView(wvProvince);
				lin_wheel.addView(wvCitys);
				lin_wheel.addView(wvArea);
				break;
			default :
		}
		
		lyChangeAddressChild = view
				.findViewById(R.id.ly_myinfo_changeaddress_child);
		btnSure = (TextView) view.findViewById(R.id.btn_myinfo_sure);
		btnCancel = (TextView) view.findViewById(R.id.btn_myinfo_cancel);

		// 设置SelectPicPopupWindow的View
		this.setContentView(view);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.anim_dialog);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);

		lyChangeAddressChild.setOnClickListener(this);
		btnSure.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		// initJsonData();
		// initDatas();

		initProvinces(fristAry);
		provinceAdapter = new AddressTextAdapter(context, arrProvinces,
				getProvinceItem(strProvince), maxsize, minsize);
		wvProvince.setVisibleItems(5);
		wvProvince.setViewAdapter(provinceAdapter);
		wvProvince.setCurrentItem(0);

		try {
			String secondStr = secondAry.get(0).toString();
			Log.i("", "init secondStr: " + secondStr);
			initCitys(secondStr);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cityAdapter = new AddressTextAdapter(context, arrCitys,
				getCityItem(strCity), maxsize, minsize);
		wvCitys.setVisibleItems(5);
		wvCitys.setViewAdapter(cityAdapter);
		wvCitys.setCurrentItem(0);

		String oneArray;
		try {
			oneArray = thirdAry.get(0).toString();
			JSONArray secondAryJson = new JSONArray(oneArray);
			String twoArray = secondAryJson.get(0).toString();
			initAreas(twoArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		areaAdapter = new AddressTextAdapter(context, arrAreas,
				getAreaItem(strArea), maxsize, minsize);
		wvArea.setVisibleItems(5);
		wvArea.setViewAdapter(areaAdapter);
		wvArea.setCurrentItem(0);

		wvProvince.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) provinceAdapter.getItemText(wheel
						.getCurrentItem());
				strProvince = currentText;
				setTextviewSize(currentText, provinceAdapter);
				Log.i("", "onChanged oldValue: " + oldValue);
				Log.i("", "onChanged newValue: " + newValue);
				oneSelect = newValue;
				try {
					String secondStr = secondAry.get(newValue).toString();
					Log.i("", "init secondStr: " + secondStr);
					initCitys(secondStr);

					cityAdapter = new AddressTextAdapter(context, arrCitys, 0,
							maxsize, minsize);
					wvCitys.setVisibleItems(5);
					wvCitys.setViewAdapter(cityAdapter);
					wvCitys.setCurrentItem(0);
					setTextviewSize("0", cityAdapter);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String oneArray;
				try {
					oneArray = thirdAry.get(newValue).toString();
					JSONArray secondAryJson = new JSONArray(oneArray);
					String twoArray = secondAryJson.get(0).toString();
					Log.i("", "init twoArray: " + twoArray);
					initAreas(twoArray);

					areaAdapter = new AddressTextAdapter(context, arrAreas, 0,
							maxsize, minsize);
					wvArea.setVisibleItems(5);
					wvArea.setViewAdapter(areaAdapter);
					wvArea.setCurrentItem(0);
					setTextviewSize("0", areaAdapter);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		wvProvince.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) provinceAdapter.getItemText(wheel
						.getCurrentItem());
				setTextviewSize(currentText, provinceAdapter);
			}
		});

		wvCitys.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) cityAdapter.getItemText(wheel
						.getCurrentItem());
				strCity = currentText;
				setTextviewSize(currentText, cityAdapter);

				// 根据市，地区联动
				String oneArray;
				try {
					oneArray = thirdAry.get(oneSelect).toString();
					JSONArray secondAryJson = new JSONArray(oneArray);
					String twoArray = secondAryJson.get(newValue).toString();
					initAreas(twoArray);

					areaAdapter = new AddressTextAdapter(context, arrAreas, 0,
							maxsize, minsize);
					wvArea.setVisibleItems(5);
					wvArea.setViewAdapter(areaAdapter);
					wvArea.setCurrentItem(0);
					setTextviewSize("0", areaAdapter);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		wvCitys.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) cityAdapter.getItemText(wheel
						.getCurrentItem());
				setTextviewSize(currentText, cityAdapter);
			}
		});

		wvArea.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) areaAdapter.getItemText(wheel
						.getCurrentItem());
				strArea = currentText;
				setTextviewSize(currentText, cityAdapter);
			}
		});

		wvArea.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) areaAdapter.getItemText(wheel
						.getCurrentItem());
				setTextviewSize(currentText, areaAdapter);
			}
		});

	}

	private class AddressTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected AddressTextAdapter(Context context, ArrayList<String> list,
				int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_picker, NO_RESOURCE, currentItem,
					maxsize, minsize);
			this.list = list;
			setItemTextResource(R.id.tempValue);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index) + "";
		}
	}

	/**
	 * 设置字体大小
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSize(String curriteItemText,
			AddressTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(14);
			} else {
				textvew.setTextSize(12);
			}
		}
	}

	public void setAddresskListener(OnAddressCListener onAddressCListener) {
		this.onAddressCListener = onAddressCListener;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnSure) {
			if (onAddressCListener != null) {
				onAddressCListener.onClick(strProvince, strCity, strArea,
						strJson);
			}
		} else if (v == btnCancel) {

		} else if (v == lyChangeAddressChild) {
			return;
		} else {
			// dismiss();
		}
		dismiss();
	}

	/**
	 * 回调接口
	 * 
	 * @author Administrator
	 * 
	 */
	public interface OnAddressCListener {
		public void onClick(String province, String city, String area,
				String strJson);
	}

	/**
	 * 初始化省会
	 */
	public void initProvinces(JSONArray fristAry) {
		for (int i = 0; i < fristAry.length(); i++) {
			JSONObject jsonP;
			try {
				jsonP = fristAry.getJSONObject(i);
				// 每个省的json对象
				String province = jsonP.getString(oneKey);// 省名字
				if (i == 0) {
					strProvince = province;
				}
				arrProvinces.add(province);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * 根据省会，生成该省会的所有城市
	 * 
	 * @param citys
	 */
	public void initCitys(String secondAry) {
		try {
			JSONArray secondAryJson = new JSONArray(secondAry);
			arrCitys.clear();
			for (int i = 0; i < secondAryJson.length(); i++) {
				JSONObject jsonP;
				try {
					jsonP = secondAryJson.getJSONObject(i);
					// 每个市的json对象
					String province = jsonP.getString(twoKey);// 市名字
					if (i == 0) {
						strJson = jsonP.toString();
						strCity = province;
					}
					arrCitys.add(province);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 根据城市，生成该城市的所有地区
	 * 
	 * @param areas
	 */
	public void initAreas(String thirdAry) {

		try {
			JSONArray thirdAryJson = new JSONArray(thirdAry);
			arrAreas.clear();
			for (int i = 0; i < thirdAryJson.length(); i++) {
				JSONObject jsonP;
				try {
					jsonP = thirdAryJson.getJSONObject(i);
					// 每个区的json对象
					String province = jsonP.getString(threeKey);// 区名字
					if (i == 0) {
						strJson = jsonP.toString();
						strArea = province;
					}
					arrAreas.add(province);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			if (thirdAryJson.length() == 0) {
				Log.i("", "init initAreas: " + thirdAryJson.length());
				strArea = "";
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 初始化地点
	 * 
	 * @param province
	 * @param city
	 */
	public void setAddress(String province, String city, String area) {
		if (province != null && province.length() > 0) {
			this.strProvince = province;
		}
		if (city != null && city.length() > 0) {
			this.strCity = city;
		}

		if (area != null && area.length() > 0) {
			this.strArea = area;
		}
	}

	/**
	 * 返回省会索引，没有就返回默认“广东”
	 * 
	 * @param province
	 * @return
	 */
	public int getProvinceItem(String province) {
		int size = arrProvinces.size();
		int provinceIndex = 0;
		boolean noprovince = true;
		for (int i = 0; i < size; i++) {
			if (province.equals(arrProvinces.get(i))) {
				noprovince = false;
				return provinceIndex;
			} else {
				provinceIndex++;
			}
		}
		if (noprovince) {
			strProvince = "广东";
			return 18;
		}
		return provinceIndex;
	}

	/**
	 * 得到城市索引，没有返回默认“深圳”
	 * 
	 * @param city
	 * @return
	 */
	public int getCityItem(String city) {
		int size = arrCitys.size();
		int cityIndex = 0;
		boolean nocity = true;
		for (int i = 0; i < size; i++) {
			System.out.println(arrCitys.get(i));
			if (city.equals(arrCitys.get(i))) {
				nocity = false;
				return cityIndex;
			} else {
				cityIndex++;
			}
		}
		if (nocity) {
			strCity = "深圳";
			return 2;
		}
		return cityIndex;
	}

	/**
	 * 得到地区索引，没有返回默认“福田区”
	 * 
	 * @param area
	 * @return
	 */
	public int getAreaItem(String area) {
		int size = arrAreas.size();
		int areaIndex = 0;
		boolean noarea = true;
		for (int i = 0; i < size; i++) {
			System.out.println(arrAreas.get(i));
			if (area.equals(arrAreas.get(i))) {
				noarea = false;
				return areaIndex;
			} else {
				areaIndex++;
			}
		}
		if (noarea) {
			strArea = "福田区";
			return 1;
		}
		return areaIndex;
	}

}
