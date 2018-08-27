package com.tc.emms.ui;

import java.text.DecimalFormat;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tc.emms.R;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.bean.BaseResponse;
import com.tc.emms.service.BusinessException;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.utils.SharedUtils;
import com.tc.emms.utils.SystemBarTintManager;
import com.tc.emms.utils.ToastShow;

/***
 * 付款机算器界面
 * 
 * @author tomenter
 * 
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class PayAmountActivity extends BaseActivity{
	
	private static String TAG = "PayAmountActivity";

	private TextView tv_title;
	private ImageView iv_back;
	private TextView tv_price;
	private TextView tv_result;
	// 声明一些控件
	private TextView tv_0 = null;
	private TextView tv_1 = null;
	private TextView tv_2 = null;
	private TextView tv_3 = null;
	private TextView tv_4 = null;
	private TextView tv_5 = null;
	private TextView tv_6 = null;
	private TextView tv_7 = null;
	private TextView tv_8 = null;
	private TextView tv_9 = null;
	private TextView tv_point = null;
	private TextView tv_c = null;
	private TextView tv_add = null;
	private TextView tv_sub = null;
	private TextView tv_end = null;
	private ImageView ivBack = null;
	// 声明两个参数。接收tv_Result前后的值
	double num1 = 0, num2 = 0;
	// 计算结果
	double Result = 0;
	// 判断是否显示“=”按钮
	boolean isClickEqu = false;
	// 判断操作类型
	int op = 0;
	// 当前输入的字符串
	String now_input;
	String now_result;
	private DecimalFormat df = new DecimalFormat("0.00");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			// Translucent status bar
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			int iv_resource = SharedUtils.getInt(this, ConstantUtils.APP_BACKGROUD_COLOR);
			/*if(iv_resource <= 0){
				iv_resource = R.color.main_blue;
			}*/
			tintManager.setStatusBarTintColor(iv_resource);
			//tintManager.setStatusBarTintResource(iv_resource);// 通知栏所需颜色
		}
		innitView();
	}

	private void innitView() {
		setContentView(R.layout.activity_paee_cycle);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getResources().getString(R.string.main_pay));
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setVisibility(View.VISIBLE);
		iv_back.setImageResource(R.drawable.goback);
		iv_back.setOnClickListener(this);
		tv_price = (TextView) findViewById(R.id.tv_price);
		tv_result = (TextView) findViewById(R.id.tv_result);
		tv_0 = (TextView) findViewById(R.id.tv_0);
		tv_1 = (TextView) findViewById(R.id.tv_1);
		tv_2 = (TextView) findViewById(R.id.tv_2);
		tv_3 = (TextView) findViewById(R.id.tv_3);
		tv_4 = (TextView) findViewById(R.id.tv_4);
		tv_5 = (TextView) findViewById(R.id.tv_5);
		tv_6 = (TextView) findViewById(R.id.tv_6);
		tv_7 = (TextView) findViewById(R.id.tv_7);
		tv_8 = (TextView) findViewById(R.id.tv_8);
		tv_9 = (TextView) findViewById(R.id.tv_9);
		tv_point = (TextView) findViewById(R.id.tv_point);
		tv_c = (TextView) findViewById(R.id.tv_c);
		tv_add = (TextView) findViewById(R.id.tv_add);
		tv_sub = (TextView) findViewById(R.id.tv_sub);
		tv_end = (TextView) findViewById(R.id.tv_end);
		ivBack = (ImageView) findViewById(R.id.ivBack);

		tv_0.setOnClickListener(this);
		tv_1.setOnClickListener(this);
		tv_2.setOnClickListener(this);
		tv_3.setOnClickListener(this);
		tv_4.setOnClickListener(this);
		tv_5.setOnClickListener(this);
		tv_6.setOnClickListener(this);
		tv_7.setOnClickListener(this);
		tv_8.setOnClickListener(this);
		tv_9.setOnClickListener(this);

		tv_add.setOnClickListener(this);
		tv_sub.setOnClickListener(this);
		tv_c.setOnClickListener(this);
		tv_end.setOnClickListener(this);
		tv_point.setOnClickListener(this);

		ivBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_0:
			input("0");
			break;
		case R.id.tv_1:
			input("1");
			break;
		case R.id.tv_2:
			input("2");
			break;
		case R.id.tv_3:
			input("3");
			break;
		case R.id.tv_4:
			input("4");
			break;
		case R.id.tv_5:
			input("5");
			break;
		case R.id.tv_6:
			input("6");
			break;
		case R.id.tv_7:
			input("7");
			break;
		case R.id.tv_8:
			input("8");
			break;
		case R.id.tv_9:
			input("9");
			break;
		case R.id.tv_point:
			now_input = tv_price.getText().toString();
			if (now_input.equals("")) {
				input("0.");
			} else {
				boolean isContainedDot = now_input.contains(".");
				if (!isContainedDot) {
					input(".");
				}
			}
			break;
		case R.id.tv_add:
			now_input = tv_price.getText().toString();
			if (!now_input.equals("")) {
				now_result = tv_result.getText().toString();
				if (now_result.contains("-")) {
					/* 先计算之前的加法 */
					double n_end = num1 - Double.valueOf(now_input);
					num1 = n_end;
				} else {
					num1 += Double.valueOf(now_input);
				}
				/* 更改tv_end为等号 */
				isClickEqu = true;
				tv_end.setText("=");
				tv_price.setText("");
				op = 1;
				input_result(df.format(num1) + "+");
			}
			break;
		case R.id.tv_sub:
			now_input = tv_price.getText().toString();
			if (!now_input.equals("")) {
				now_result = tv_result.getText().toString();
				if (now_result.contains("+")) {
					/* 先计算之前的加法 */
					num1 += Double.valueOf(now_input);
				} else {
					if (!now_result.equals("")) {
						double n_end = num1 - Double.valueOf(now_input);
						num1 = n_end;
					} else {
						num1 += Double.valueOf(now_input);
					}
				}
				/* 更改tv_end为等号 */
				isClickEqu = true;
				tv_end.setText("=");
				tv_price.setText("");
				op = 2;
				input_result(df.format(num1) + "-");
			}
			break;
		case R.id.tv_c:
			tv_price.setText("");
			tv_result.setText("");
			num1 = 0;
			num2 = 0;
			Result = 0;
			break;
		case R.id.tv_end:

			if (isClickEqu) {
				String myStringEqu = tv_price.getText().toString();
				if (!myStringEqu.equals("")) {
					tv_result.setText("");
					num2 = Double.valueOf(myStringEqu);
					tv_price.setText("");
					switch (op) {
					case 1:
						Result = num1 + num2;
						break;
					case 2:
						Result = num1 - num2;
						break;
					default:
						Result = 0;
						break;
					}
					num1 = 0;
					num2 = 0;
					tv_price.setText(String.valueOf(df.format(Result)));
					Result = 0;
					isClickEqu = false;
					/* 更改tv_end为收款 */
					tv_end.setText(getResources().getString(R.string.main_pay));
				}

			} else {

				String myStringEqu = tv_price.getText().toString();
				if (myStringEqu.equals("")) {
					ToastShow.showShort(PayAmountActivity.this, getString(R.string.cycle_tip));
				} else {
					double price = Double.valueOf(myStringEqu);
					if (price >= 0.01) {
						
						/* 进入收款页面 */
						Intent intent = new Intent(this,
								PayScanActivity.class);
						intent.putExtra("price", myStringEqu);
						startActivity(intent);

					} else {
						ToastShow.showShort(PayAmountActivity.this, getString(R.string.cycle_tip));
					}
				}

			}
			break;
		case R.id.ivBack:
			String myStr = tv_price.getText().toString();
			try {
				tv_price.setText(myStr.substring(0, myStr.length() - 1));
			} catch (Exception e) {
				tv_price.setText("");
			}
			break;
		default:
			break;
		}
	}

	private void input(String i_number) {
		String myString = tv_price.getText().toString();
		if (myString.contains(".")) {
			/* 输入小数 */
			LogUtils.iLog(TAG, "input : " + myString);
			if (myString.length() - myString.indexOf(".") > 2) {
				LogUtils.iLog(TAG, "length > 2 : " + myString.length());
			} else {
				myString += i_number;
				tv_price.setText(myString);
			}
		} else {
			if (!i_number.equals("0") && myString.contains("0")) {
				if (Long.parseLong(myString) == 0) {
					if (i_number.equals(".")) {
						myString = "0";
					} else {
						myString = "";
					}
				} else {

				}

			}
			myString += i_number;
			tv_price.setText(myString);
		}
	}

	private void input_result(String i_result) {
		tv_result.setText(i_result);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onSuccess(BaseResponse response, String jsonStr, int act) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(BusinessException e, int act) {
		// TODO Auto-generated method stub
		
	}
}
