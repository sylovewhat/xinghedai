package com.tc.emms.widget;

import com.tc.emms.R;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.utils.ImageUtils;
import com.tc.emms.utils.LogUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * textView上的小红点
 * 
 * @author Visual
 * 
 */
public class RedTipTextView extends TextView {
	public static final int RED_TIP_INVISIBLE = 4;
	public static final int RED_TIP_VISIBLE = 0;
	public static final int RED_TIP_GONE = 8;
	private int tipVisibility = 0;

	public RedTipTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(null);
	}

	public RedTipTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(attrs);
	}

	public RedTipTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(attrs);
	}

	public void init(AttributeSet attrs) {
		if (attrs != null) {
			TypedArray array = getContext().obtainStyledAttributes(attrs,
					R.styleable.RedTipTextView);
			tipVisibility = array.getInt(
					R.styleable.RedTipTextView_redTipsVisibility, 0);
			array.recycle();
		}
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (tipVisibility == 0) {
			int width = getWidth();
			int paddingRight = getPaddingRight();
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			paint.setAntiAlias(true);
			paint.setDither(true);
			paint.setStyle(Style.FILL_AND_STROKE);
			canvas.drawCircle(width - getPaddingRight() / 2, paddingRight / 2,
					paddingRight / 2, paint);
		}
	}

	/* 设置小红点是否显示 */
	@Override
	public void setVisibility(int visibility) {
		tipVisibility = visibility;
		invalidate();
	}

	/* 新增方法 */
	public String _index = "";
	public String _url_html = "";
	public String _url_data = "";
	public String _webLoadType = "";
	public String _title = "";
	public String _image = "";
	public String _selectedImage = "";
	public String _navLoadType = "";//这边是设置头部导航栏是否可见的

	/* 是否已加载过 */
	public boolean is_loaded = false;

	public void setTitleText(String text) {
		this.setText(text);
	}

	/* 颜色设置 */
	public void setTextColor(String color, String color_on) {
		int _color = getResources().getColor(R.color.main_font_black_and_gray);
		int _color_on = getResources().getColor(R.color.main_title_backgroud);
		LogUtils.iLog("", "setTabBar setTextColor color: " + color);
		LogUtils.iLog("", "setTabBar setTextColor color_on: " + color_on);
		if(!color.equals("")){
			_color = Color.parseColor(color);
		}
		if(!color_on.equals("")){
			_color_on = Color.parseColor(color_on);
		}
		
		int[] colors = new int[] { _color_on, _color_on, _color };
        int[][] states = new int[3][];
        states[0] = new int[] { android.R.attr.state_selected};
        states[1] = new int[] { android.R.attr.state_pressed};
        states[2] = new int[] {};
        ColorStateList colorList = new ColorStateList(states, colors);
		this.setTextColor(colorList);
	}

	/* 图标设置 */
	public void setTextDrawable(BaseActivity mActivity, String image,
			String image_click) {
		/* 通知图标设置 */
		int iv_resource = ImageUtils.getResourceID(mActivity, image);
		int iv_resource_click = ImageUtils
				.getResourceID(mActivity, image_click);
		LogUtils.iLog("", "setTextDrawable iv_resource: " + iv_resource);
		LogUtils.iLog("", "setTextDrawable iv_resource_click: " + iv_resource_click);
		if (iv_resource > 0 && iv_resource_click > 0) {
			Drawable notice_img = addStateDrawable(mActivity, iv_resource,
					iv_resource_click, iv_resource_click);
			//这边改变Textview大小
//			notice_img.setBounds(0, 0, notice_img.getMinimumWidth(),
//					notice_img.getMinimumHeight());

			notice_img.setBounds(0, 0, 80,
					80);
			/* 设置按钮状态 */
			this.setCompoundDrawables(null, notice_img, null, null);

		}
	}

	private StateListDrawable addStateDrawable(Context context, int idNormal,
			int idPressed, int idFocused) {
		StateListDrawable sd = new StateListDrawable();
		Drawable normal = idNormal == -1 ? null : context.getResources()
				.getDrawable(idNormal);
		Drawable pressed = idPressed == -1 ? null : context.getResources()
				.getDrawable(idPressed);
		Drawable focus = idFocused == -1 ? null : context.getResources()
				.getDrawable(idFocused);
		// 注意该处的顺序，只要有一个状态与之相配，背景就会被换掉
		// 所以不要把大范围放在前面了，如果sd.addState(new[]{},normal)放在第一个的话，就没有什么效果了
		sd.addState(new int[]{android.R.attr.state_selected}, focus);
		sd.addState(new int[]{android.R.attr.state_focused}, focus);
		sd.addState(new int[]{android.R.attr.state_pressed}, pressed);
		sd.addState(new int[]{}, normal);
		return sd;
	}

	/* 设置html */
	public void setUrlHtml(String html) {
		_url_html = html;
	}

	public String getUrlHtml() {
		return _url_html;
	}

	public void setUrlData(String data) {
		_url_data = data;
	}

	public String getUrlData() {
		return _url_data;
	}

	public void setWebLoadType(String type) {
		_webLoadType = type;
	}

	public String getWebLoadType() {
		return _webLoadType;
	}

	public void setis_loaded(boolean type) {
		is_loaded = type;
	}

	public boolean getis_loaded() {
		return is_loaded;
	}


	public String get_navLoadType() {
		return _navLoadType;
	}

	public void set_navLoadType(String _navLoadType) {
		this._navLoadType = _navLoadType;
	}
}
