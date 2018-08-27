package com.tc.emms.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.tc.emms.R;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.bean.BaseResponse;
import com.tc.emms.html.SqlitEncapsulation;
import com.tc.emms.service.BusinessException;
import com.tc.emms.utils.AppUtils;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.FileUtils;
import com.tc.emms.utils.ImageUtils;
import com.tc.emms.utils.LogUtils;
import com.tc.emms.utils.SharedUtils;
import com.tc.emms.utils.ToastShow;
import com.tc.emms.utils.ZipExtractorUtils;

/*
 * 启动页面
 * 功能【第三方api key初始化】
 */
@SuppressLint("HandlerLeak") public class StartActivity extends BaseActivity {
	private static String TAG = "StartActivity";
	
	private String FILE_PATH;
	private String HTML_PATH;
	private Bitmap bitmap;

	private String now_version = "";
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
//		ImageView startView = (ImageView) View.inflate(this, R.layout.splash, null);
//		startView.setImageDrawable(getResources().getDrawable(R.drawable.lunch_app_0));
//		setContentView(startView);
		mActivity = this;
		StartAnimat();//启动基座
		LogUtils.iLog(TAG, "StartActivity onCreate finish" );
		
	}

	private void StartAnimat() {
		if(ConstantUtils.IS_OPEN_DEBUG){
			LogUtils.iLog(TAG, "基座调整");
			/*进入基座调试模式*/
//			String u_path = FileUtils.getU_PATH(mActivity);
//			String _url = ConstantUtils.U_BASE + u_path + "init_setting.html";
			String _url = "file:///android_asset/PayeeHtml/init_setting.html";
			LogUtils.iLog(TAG, "基座地址1："+ _url);
			Intent intent = new Intent(StartActivity.this, InitWebActivity.class);
			intent.putExtra(ConstantUtils.HTML_URL, _url);
			intent.putExtra(ConstantUtils.HTML_NAME, "配置");
			intent.putExtra(ConstantUtils.IS_CHECK, "0");
			startActivity(intent);
			finish();
		}else{
			now_version = SharedUtils.getValue(StartActivity.this,
					ConstantUtils.IS_NEW_VERSION);
			if (now_version != null && !now_version.equals("")) {
				String app_version = AppUtils.getVersion(StartActivity.this);
				LogUtils.iLog(TAG, "StartActivity now_version: " + now_version);
				LogUtils.iLog(TAG, "StartActivity app_version: " + app_version);
				//版本有改，更新资源
				if (!now_version.equals(app_version)) {
					/* 删除之前的html文件 */
					loadWelActivity();
				} 
				
			} else {
				/* 删除之前的html文件 */
				loadWelActivity();

			}

			// 登录
//			Intent intent = new Intent(StartActivity.this, WebActivity.class);
//			intent.putExtra(ConstantUtils.HTML_NAME, ConstantUtils.FIRST_HTML);
//			intent.putExtra(ConstantUtils.HTML_URL, ConstantUtils.FIRST_HTML);
//			startActivity(intent);
//			finish();
		}
	}




	private void loadWelActivity() {
		SharedUtils.setValue(StartActivity.this, "FILE",
				ConstantUtils.STORE_PATH);
		//获取路径
		String mPath = SharedUtils.getValue(StartActivity.this, "FILE");
		FILE_PATH = mPath + ConstantUtils.FILE_PATH;

		//html路径
		HTML_PATH = FILE_PATH + ConstantUtils.HTML_PATH;

		File dirFirstFile = new File(FILE_PATH);// 新建一级主目录

		if (!dirFirstFile.exists()) {// 判断文件夹目录是否存在
			dirFirstFile.mkdirs();// 如果不存在则创建
			LogUtils.dLog(TAG, "创建: " + FILE_PATH);

		}


		File htmlFile = new File(HTML_PATH);// 新建一级主目录
		if (!htmlFile.exists()) {// 判断文件夹目录是否存在
			htmlFile.mkdirs();// 如果不存在则创建
			LogUtils.dLog(TAG, "创建: " + HTML_PATH);

		}

		int fileSize = ConstantUtils.SD_FILES.length;
		LogUtils.dLog(TAG, "合并创建: " + fileSize);
		for (int i = 0; i < fileSize; i++) {
			String file_path = HTML_PATH + ConstantUtils.SD_FILES[i];
			// 新建二级主目录
			File allFile = new File(file_path);
			// 判断文件夹目录是否存在
			if (!allFile.exists()) {
				// 如果不存在则创建
				allFile.mkdirs();
				LogUtils.dLog(TAG, "合并创建: " + file_path);

			}
		}

		String sd_path = FileUtils.getStoragePath(mActivity, false) + "/"
				+ ConstantUtils.SD_FILE_PATH;
		LogUtils.eLog(TAG, "查看本地存储路径 sd_path:" + sd_path);
		File sdFile = new File(sd_path);
		if (!sdFile.exists()) {// 判断文件夹目录是否存在
			sdFile.mkdirs();// 如果不存在则创建
			LogUtils.eLog(TAG, "创建: " + sd_path);
		}
		try {
			copyBigDataToSD(FILE_PATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//拷贝assets下的资源文件到SD卡中"
	private void copyBigDataToSD(String mPath) throws IOException {
		LogUtils.dLog(TAG, "拷贝assets下的资源文件到SD卡中");
		InputStream myInput;
		OutputStream myOutput = new FileOutputStream(mPath
				+ ConstantUtils.U_RES_ZIP);


		myInput = this.getAssets().open(ConstantUtils.U_RES_ZIP);//这边就是拷贝assets下的PayeeHtml.zip

		byte[] buffer = new byte[1024];
		int length = myInput.read(buffer);
		while (length > 0) {
			myOutput.write(buffer, 0, length);//写入数据
			length = myInput.read(buffer);

		}
		LogUtils.dLog(TAG, "开始解压资源文件包到sd卡11..");
		myOutput.flush();
		myInput.close();
		myOutput.close();

		LogUtils.dLog(TAG, "WelcomeActivity 01.解压资源文件包到sd卡");

		ZipExtractorUtils task1 = new ZipExtractorUtils(FILE_PATH
				+ ConstantUtils.U_RES_ZIP, FILE_PATH, StartActivity.this,
				true, down_handler);
		task1.execute();


		
	}

	Handler down_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 更改页面
			switch (msg.what) {
				case 3 :
					LogUtils.dLog(TAG, "解压。。。" + msg.arg1);
					Log.d("sylove","解压中-------------------------1111----------------------");
					break;

				case 4 :
					/* 添加默认图片 */
					LogUtils.iLog(TAG, "解压完成。。。添加默认图片");
					Log.d("sylove","解压完成-------------------------1111----------------------");
					bitmap = BitmapFactory.decodeResource(getResources(),
							R.drawable.nomarl);
					String noname = HTML_PATH + ConstantUtils.U_IMG
							+ ConstantUtils.NORMAL;
					FileOutputStream noout = null;
					try {
						noout = new FileOutputStream(noname);
						bitmap.compress(Bitmap.CompressFormat.PNG, 100, noout);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							noout.flush();
						} catch (IOException e) {
							e.printStackTrace();
						}
						try {
							noout.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						noout = null;
					}
					
					LogUtils.dLog(TAG, "WelcomeActivity 02.创建数据库");
					Log.d("sylove","创建数据库===============================================");
					String jsonString = "{\"sqlTableName\":\""
							+ ConstantUtils.HTML_UPDATE_TABLE_NAME + "\",\""
							+ ConstantUtils.HTML_UPDATE_NAME + "\":\"\",\""
							+ ConstantUtils.HTML_UPDATE_VERSIONNO + "\":\"\",\""
							+ ConstantUtils.HTML_UPDATE_KEY + "\":\"\",\""
							+ ConstantUtils.HTML_UPDATE_TIME + "\":\"\",\""
							+ ConstantUtils.HTML_UPDATE_DES + "\":\"\",\""
							+ ConstantUtils.HTML_UPDATE_VERNO + "\":\"\",\""
							+ ConstantUtils.HTML_UPDATE_NOTE + "\":\"\"}";
					SqlitEncapsulation.creadSqlTable(jsonString, "", StartActivity.this, null, null,
							null, handler);
					 //登录
			Intent intent = new Intent(StartActivity.this, WebActivity.class);
			intent.putExtra(ConstantUtils.HTML_NAME, ConstantUtils.FIRST_HTML);
			intent.putExtra(ConstantUtils.HTML_URL, ConstantUtils.FIRST_HTML);
			startActivity(intent);
			finish();
					break;
				default :
					Log.d("sylove","我是默认的==============================================");
					break;
			}
		}
	};
	
	/**
	 * 方法库执行 回调信息
	 */
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case ConstantUtils.HANDLER_SECCESS :
					// TODO 成功执行方法库
					LogUtils.vLog(TAG, " ********* 方法库执行 回调信息 :" + msg.obj);
					break;
				case ConstantUtils.HANDLER_FAILE :
					// TODO 失败
					ToastShow.showShort(StartActivity.this,
							msg.obj.toString());
					break;
			}
			
		}
	};

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onSuccess(BaseResponse response, String jsonStr, int act) {
		
	}

	@Override
	public void onError(BusinessException e, int act) {
		
	}

	
}