package com.tc.emms.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.tc.emms.base.BaseActivity;
import com.tc.emms.db.DBHelper;

public class HtmlUtils {
	private static BaseActivity mActivity;
	public static Handler ui_handler;
	public static String h5Name;

	/**
	 *
	 * @param activity   承载界面
	 * @param Name       html名字 默认是default
	 * @param _handler   handler
	 */
	public static void isHtmlUpdate(final BaseActivity activity,
			final String Name, final Handler _handler) {
		ui_handler = _handler;
		h5Name = Name;
		mActivity = activity;
		
		if(ConstantUtils.IS_OPEN_DEBUG){
			/* 不需要更新 */
			Message msgT = ui_handler.obtainMessage();
			msgT.what = ConstantUtils.ACT_HTML_COM;
			msgT.obj = h5Name;
			ui_handler.sendMessage(msgT);
		}else{
			boolean isUpdate = false;
			int indexValue = 0;
			// 实例化一个容器，用来拼接sql语句
			StringBuffer sBuffer = new StringBuffer();
			// sql语句，第一个字段为_ID 主键自增，这是通用的，所以直接写死
			sBuffer.append("select * from " + ConstantUtils.HTML_UPDATE_TABLE_NAME
					+ " where " + ConstantUtils.HTML_UPDATE_NAME + "='" + h5Name + "'");
			Log.v("isHtmlUpdate",
					"--- db isHtmlUpdate execSQLStr:" + sBuffer.toString());
			Log.d("sylove","数据库执行语句"+sBuffer.toString());
			/* 查询表中指定数据 */
			SQLiteDatabase db = DBHelper.openDatabase();
			/* 获取表中所有字段 */
			List<String> tableValues = new ArrayList<String>();

			//pragma table_info 查看表的基本信息
			Cursor cursor = db.rawQuery("pragma table_info("
					+ ConstantUtils.HTML_UPDATE_TABLE_NAME + ")", null);
			Log.d("sylove","表信息"+cursor.moveToNext());

			while (cursor.moveToNext()) {
				Log.d("sylove","执行循环"+cursor);
				String[] columnNames = cursor.getColumnNames();
				for (String name : columnNames) {
					Log.d("sylove","字段信息"+cursor);
					if (name != null && name.equals("name")) {
						// 提取value
						String value = cursor
								.getString(cursor.getColumnIndex(name));
						Log.v("isHtmlUpdate", "获取表中所有字段 db execSQLStr name:" + name
								+ ",value==" + value);
						Log.d("sylove","获取表中所有字段 "+ name + ",value==" + value);
						tableValues.add(value);
					}
				}
			}
			Log.d("sylove","获取表中所有字段集合"+tableValues);
			try {
				LogUtils.eLog("", " start add result tableValues:" + tableValues.size());
				/* 查询表中指定数据 */
				Cursor result = db.rawQuery(sBuffer.toString(), null);
				result.moveToFirst();
				while (!result.isAfterLast()) {
					indexValue++;
					for (int i = 0; i < tableValues.size(); i++) {
						// 提取value
						String valueString = tableValues.get(i).toString();
						int nameColumnIndex = result.getColumnIndex(valueString);
						String strValue = result.getString(nameColumnIndex);
						Log.v("isHtmlUpdate", "查询表中指定数据 db execSQLStr key:"
								+ valueString + "; value" + strValue);
						if (valueString.equals(ConstantUtils.HTML_UPDATE_KEY)) {
							Log.v("isHtmlUpdate", "查询表中是否需要更新 execSQLStr  value："
									+ strValue);
							// if (strValue.equals("1")) {
							if (strValue.equals("0")) {
								isUpdate = true;
							} else {
								isUpdate = false;
							}
						}
					}
					result.moveToNext();
				}
				result.close();

				if (indexValue == 0) {
					isUpdate = true;
					Log.v("isHtmlUpdate",
							"selectCell execSQLStr isHtmlUpdate 没有查到html数据 , 判断本地文件是否存在"
									+ indexValue);
					String u_path = SharedUtils.getValue(mActivity, "FILE")
							+ ConstantUtils.FILE_PATH + ConstantUtils.HTML_PATH;
					File filename = new File(u_path + h5Name);
					if(!filename.exists()){
						/*文件不存在*/
						Log.v("isHtmlUpdate", "execSQLStr 文件不存在   新增：" + h5Name + "需要下载更新...");
						insertHtml();
					}else{
						/*文件存在，直接显示*/
						Log.v("isHtmlUpdate", "execSQLStr 文件存在，直接显示：" + h5Name);
						isUpdate = false;
					}
				}

				Log.v("isHtmlUpdate", "selectCell execSQLStr isHtmlUpdate 是否需要更新："
						+ isUpdate);
				if (isUpdate) {
					/* 需要更新 */
					String dUrl = ConstantUtils.NORMAIL_DOWNURL;
					String downUrl = SharedUtils.getValue(mActivity, ConstantUtils.SETH5UPDATEURL);
					if(!downUrl.equals("")){
						dUrl = downUrl;
					}
					/* 下载更新 */
					String downurl = dUrl + h5Name;
					Log.d("", "html downurl:" + downurl);
					String u_path = SharedUtils.getValue(mActivity, "FILE")
							+ ConstantUtils.FILE_PATH + ConstantUtils.HTML_PATH;
					// /传入参数：Context对象，下载地址, 文件保存路径；
					DownloadTask.to(downurl, u_path + h5Name, down_handler);
				} else {
					/* 不需要更新 */
					Message msg2 = ui_handler.obtainMessage();
					msg2.what = ConstantUtils.ACT_HTML_COM;
					msg2.obj = h5Name;
					ui_handler.sendMessage(msg2);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cursor.close();
			db.close();
		}

		

	}

	/* 更新UI */
	static Handler down_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case ConstantUtils.U_DOWN_PROCESS :
					int _process = msg.arg1;
					Log.e("", "html 下载进度：" + _process);
					break;
				case ConstantUtils.U_DOWN_COM :
					/* 更改数据库 isUpadte */
					changedIsUpdate();

					Message msg2 = ui_handler.obtainMessage();
					msg2.what = ConstantUtils.ACT_HTML_COM;
					msg2.obj = h5Name;
					ui_handler.sendMessage(msg2);
					break;
				case ConstantUtils.U_DOWN_ERR :
					// 下载失败
					Log.e("", "html 下载失败, 默认显示");
					String u_path = SharedUtils.getValue(mActivity, "FILE")
							+ ConstantUtils.FILE_PATH + ConstantUtils.HTML_PATH
							+ h5Name;
					Log.e("", "html 下载失败, 重新还原备份文件");
					boolean isCopy = FileUtils.copyFile(u_path + ".back",
							u_path);
					if (isCopy) {
						File f = new File(u_path + ".back");
						if (f.exists()) {
							f.delete();
						}
					} else {
						Log.d("", "下载失败, 重新还原备份文件 失败file copy error:" + isCopy);
					}
					/* 更改数据库 isUpadte */
					changedIsUpdate();
					Message msg3 = ui_handler.obtainMessage();
					msg3.what = ConstantUtils.ACT_HTML_FAILE;
					msg3.obj = h5Name;
					ui_handler.sendMessage(msg3);
					break;
				default :
					break;
			}
		}
	};

	private static void changedIsUpdate() {
		// 实例化一个容器，用来拼接sql语句
		StringBuffer sBuffer = new StringBuffer();
		// sql语句，第一个字段为_ID 主键自增，这是通用的，所以直接写死
		sBuffer.append("update " + ConstantUtils.HTML_UPDATE_TABLE_NAME
				+ " set " + ConstantUtils.HTML_UPDATE_KEY + "=1" + " where "
				+ ConstantUtils.HTML_UPDATE_NAME + "='" + h5Name + "'");
		Log.v("isHtmlUpdate",
				"--- db changedIsUpdate execSQLStr:" + sBuffer.toString());
		/* 查询表中指定数据 */
		SQLiteDatabase db = DBHelper.openDatabase();
		/* 获取表中所有字段 */
		db.execSQL(sBuffer.toString());
		db.close();
	}

	private static void insertHtml() {
		// 实例化一个容器，用来拼接sql语句
		StringBuffer sBuffer = new StringBuffer();
		// sql语句，第一个字段为_ID 主键自增，这是通用的，所以直接写死
		sBuffer.append("insert into " + ConstantUtils.HTML_UPDATE_TABLE_NAME
				+ " (" + ConstantUtils.HTML_UPDATE_KEY + ","
				+ ConstantUtils.HTML_UPDATE_NAME + ","
				+ ConstantUtils.HTML_UPDATE_VERSIONNO + ")" + " values (1,'"
				+ h5Name + "','" + AppUtils.getVersion(mActivity) + "')");
		Log.v("isHtmlUpdate", "--- db insert execSQLStr:" + sBuffer.toString());
		/* 查询表中指定数据 */
		SQLiteDatabase db = DBHelper.openDatabase();
		/* 获取表中所有字段 */
		db.execSQL(sBuffer.toString());
		db.close();
	}
}
