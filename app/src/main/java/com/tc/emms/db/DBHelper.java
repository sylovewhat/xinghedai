package com.tc.emms.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.tc.emms.utils.ConstantUtils;
import com.tc.emms.utils.LogUtils;

/**
 * 数据库处理类 Created by xiaoben on 2017/10/20.
 */

public class DBHelper extends OrmLiteSqliteOpenHelper {
	private static String TAG = "DBHelper";

	public final static String DATABASE_NAME = ConstantUtils.DB_NAME; //数据库名字
	private static final int DATABASE_VERSION = 1;// 数据库版本
	private Context context;
	private boolean seccess = true;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
		if (this.context == null) {

		}
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		// creadSqlTable(db, TABLE_NAME);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int arg2, int arg3) {
		// 1.删除原来的表
		// 2.调用onCreate重新创建数据库
	}

	public static SQLiteDatabase openDatabase() {
		// 获得dictionary.db文件的绝对路径
		String databaseFilename = ConstantUtils.STORE_PATH + "databases/"
				+ DATABASE_NAME;
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(
				databaseFilename, null);
		return database;
	}

	/***
	 * 创建表
	 * 
	 * @param tableName
	 *            表名
	 * @param mapValues
	 *            表字段字符串
	 * @return handler
	 */
	public boolean creadSqlTable(String tableName, Map<String, String> mapValues) {
		seccess = true;
		LogUtils.vLog(TAG, "--- DBHelper creadSqlTable name:" + tableName);
		// 实例化一个容器，用来拼接sql语句
		StringBuffer sBuffer = new StringBuffer();
		// sql语句，第一个字段为_ID 主键自增，这是通用的，所以直接写死
		sBuffer.append("create table if not exists " + tableName + " "
				+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,");
		// 得到实体类中所有的公有属性
		for (Map.Entry<String, String> entry : mapValues.entrySet()) {
			sBuffer.append(entry.getKey() + " TEXT,");
		}
		// 将最后的逗号删除
		sBuffer.deleteCharAt(sBuffer.length() - 1);
		// 替换成); 表明sql语句结束
		sBuffer.append(");");
		LogUtils.vLog(TAG, "--- db creadSqlTable execSQL:" + sBuffer.toString());
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(sBuffer.toString());
		db.close();
		return seccess;
	}

	/***
	 * 删除表
	 * 
	 * @param tableName
	 *            表名
	 * @param mapValues
	 *            表字段字符串
	 * @return handler
	 */
	public boolean deleteTable(String tableName, Map<String, String> mapValues) {
		seccess = true;
		LogUtils.vLog(TAG, "--- DBHelper deleteTable name:" + tableName);
		// 实例化一个容器，用来拼接sql语句
		StringBuffer sBuffer = new StringBuffer();
		// sql语句，第一个字段为_ID 主键自增，这是通用的，所以直接写死
		sBuffer.append("drop table if exists " + tableName);
		LogUtils.vLog(TAG, "--- db deleteTable execSQL:" + sBuffer.toString());
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(sBuffer.toString());
		db.close();
		return seccess;
	}

	/***
	 * 插入表数据
	 * 
	 * @param tableName
	 *            表名
	 * @param mapValues
	 *            表字段字符串
	 * @return handler
	 */
	public boolean saveCell(String tableName, Map<String, String> mapValues) {
		seccess = true;
		LogUtils.vLog(TAG, "--- DBHelper saveCell name:" + tableName);
		// 实例化一个容器，用来拼接sql语句
		StringBuffer sBuffer = new StringBuffer();
		// sql语句，第一个字段为_ID 主键自增，这是通用的，所以直接写死
		sBuffer.append("insert into " + tableName + " ");
		StringBuffer _keys = new StringBuffer();
		StringBuffer _values = new StringBuffer();
		// 得到实体类中所有的公有属性
		for (Map.Entry<String, String> entry : mapValues.entrySet()) {
			// 提取key
			String keyString = entry.getKey() + ",";
			// 提取value
			String valueString = "'" + entry.getValue().toString() + "',";
			_keys.append(keyString);
			_values.append(valueString);
		}
		// 将最后的逗号删除
		_keys = _keys.deleteCharAt(_keys.length() - 1);
		_values = _values.deleteCharAt(_values.length() - 1);
		sBuffer.append("(" + _keys + ")");
		sBuffer.append(" values ");
		sBuffer.append("(" + _values + ")");
		LogUtils.vLog(TAG, "--- db saveCell execSQL:" + sBuffer.toString());
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(sBuffer.toString());
		db.close();
		return seccess;
	}

	/***
	 * 删除表数据
	 * 
	 * @param tableName
	 *            表名
	 * @param mapValues
	 *            表字段字符串
	 * @return handler
	 */
	public boolean deleteCell(String tableName, Map<String, String> mapValues) {
		seccess = true;
		LogUtils.vLog(TAG, "--- DBHelper deleteCell name:" + tableName);
		// 实例化一个容器，用来拼接sql语句
		StringBuffer sBuffer = new StringBuffer();
		// sql语句，第一个字段为_ID 主键自增，这是通用的，所以直接写死
		sBuffer.append("delete from " + tableName + " where");
		StringBuffer _where = new StringBuffer();
		// 得到实体类中所有的公有属性
		for (Map.Entry<String, String> entry : mapValues.entrySet()) {
			// 提取key
			String keyString = " " + entry.getKey() + " = ";
			// 提取value
			String valueString = "'" + entry.getValue().toString() + "'";
			_where.append(keyString + valueString + " and");
		}
		// 将最后的and删除
		_where = _where.delete(_where.length() - 3, _where.length());
		sBuffer.append(_where);
		LogUtils.vLog(TAG, "--- db deleteCell execSQL:" + sBuffer.toString());
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(sBuffer.toString());
		db.close();
		return seccess;
	}

	/***
	 * 删除表数据【删除所有数据】
	 * 
	 * @param tableName
	 *            表名
	 * @param mapValues
	 *            表字段字符串
	 * @return handler
	 */
	public boolean deleteAll(String tableName, Map<String, String> mapValues) {
		seccess = true;
		LogUtils.vLog(TAG, "--- DBHelper deleteAll name:" + tableName);
		// 实例化一个容器，用来拼接sql语句
		StringBuffer sBuffer = new StringBuffer();
		// sql语句，第一个字段为_ID 主键自增，这是通用的，所以直接写死
		sBuffer.append("delete from " + tableName);
		LogUtils.vLog(TAG, "--- db deleteAll execSQL:" + sBuffer.toString());
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(sBuffer.toString());
		db.close();
		return seccess;
	}

	/***
	 * 修改表数据
	 * 
	 * @param tableName
	 *            表名
	 * @param mapValues
	 *            表字段字符串
	 * @return handler
	 */
	public boolean updataCell(String tableName, Map<String, String> mapValues,
			String _sql) {
		seccess = true;
		LogUtils.vLog(TAG, "--- DBHelper updataCell name:" + tableName);
		// 实例化一个容器，用来拼接sql语句
		StringBuffer sBuffer = new StringBuffer();
		// sql语句，第一个字段为_ID 主键自增，这是通用的，所以直接写死
		sBuffer.append("update " + tableName + " set");
		StringBuffer _set = new StringBuffer();
		// 得到实体类中所有的公有属性
		for (Map.Entry<String, String> entry : mapValues.entrySet()) {
			// 提取key
			String keyString = " " + entry.getKey() + " = ";
			// 提取value
			String valueString = "'" + entry.getValue().toString() + "'";
			_set.append(keyString + valueString + " ,");
		}
		// 将最后的,删除
		_set = _set.deleteCharAt(_set.length() - 1);
		sBuffer.append(_set);
		sBuffer.append(" " + _sql);
		LogUtils.vLog(TAG, "--- db updataCell execSQL:" + sBuffer.toString());
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(sBuffer.toString());
		db.close();
		return seccess;
	}

	/***
	 * 修改表数据【所有数据】
	 * 
	 * @param tableName
	 *            表名
	 * @param mapValues
	 *            表字段字符串
	 * @return handler
	 */
	public boolean updateAll(String tableName, Map<String, String> mapValues) {
		seccess = true;
		LogUtils.vLog(TAG, "--- DBHelper updateAll name:" + tableName);
		// 实例化一个容器，用来拼接sql语句
		StringBuffer sBuffer = new StringBuffer();
		// sql语句，第一个字段为_ID 主键自增，这是通用的，所以直接写死
		sBuffer.append("update " + tableName + " set");
		StringBuffer _set = new StringBuffer();
		// 得到实体类中所有的公有属性
		for (Map.Entry<String, String> entry : mapValues.entrySet()) {
			// 提取key
			String keyString = " " + entry.getKey() + " = ";
			// 提取value
			String valueString = "'" + entry.getValue().toString() + "'";
			_set.append(keyString + valueString + " ,");
		}
		// 将最后的,删除
		_set = _set.deleteCharAt(_set.length() - 1);
		sBuffer.append(_set);
		LogUtils.vLog(TAG, "--- db updateAll execSQL:" + sBuffer.toString());
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(sBuffer.toString());
		db.close();
		return seccess;
	}

	/***
	 * 查询表数据
	 * 
	 * @param tableName
	 *            表名
	 * @param mapValues
	 *            表字段字符串
	 * @return handler
	 */
	public String selectCell(String tableName, String tabSql,
			Map<String, String> mapValues) {
		/* 查询得到的总条数 */
		int selectCount = 0;
		String jsonStr = "[";
		LogUtils.vLog(TAG, "--- DBHelper selectCell name:" + tableName);
		// 实例化一个容器，用来拼接sql语句
		StringBuffer sBuffer = new StringBuffer();
		// sql语句，第一个字段为_ID 主键自增，这是通用的，所以直接写死
		sBuffer.append("select * from " + tableName + " where");
		StringBuffer _where = new StringBuffer();
		// 得到实体类中所有的公有属性
		for (Map.Entry<String, String> entry : mapValues.entrySet()) {
			// 提取key
			String keyString = " " + entry.getKey() + " = ";
			// 提取value
			String valueString = "'" + entry.getValue().toString() + "'";
			_where.append(keyString + valueString + " and");
		}
		// 将最后的and删除
		_where = _where.delete(_where.length() - 3, _where.length());
		_where.append(" " + tabSql);
		sBuffer.append(_where);
		LogUtils.vLog(TAG, "--- db selectCell execSQL:" + sBuffer.toString());
		SQLiteDatabase db = this.getWritableDatabase();
		/* 获取表中所有字段 */
		/*Map<String, String> tableValues = new HashMap<String, String>();*/
		List<String> tableValues = new ArrayList<String>();
		Cursor cursor = db.rawQuery("pragma table_info(" + tableName + ")",
				null);
		while (cursor.moveToNext()) {
			String[] columnNames = cursor.getColumnNames();
			for (String name : columnNames) {
				if (name != null && name.equals("name")) {

					// 提取value
					String value = cursor
							.getString(cursor.getColumnIndex(name));
					LogUtils.vLog(TAG, "获取表中所有字段 db name：" + name + ",value：" + value);
					tableValues.add(value);
				}
			}
		}
		try {
			LogUtils.eLog(TAG, " start add result tableValues:" + tableValues.size());
			/* 查询表中指定数据 */
			Cursor result = db.rawQuery(sBuffer.toString(), null);
			selectCount = result.getCount();
			LogUtils.eLog(TAG, " add result count:" + selectCount);
			while (result.moveToNext()) {
				JSONObject return_Json = new JSONObject();
				for (int i = 0; i < tableValues.size(); i++) {
					// 提取value
					String valueString = tableValues.get(i).toString();
					int nameColumnIndex = result.getColumnIndex(valueString);
					String strValue = result.getString(nameColumnIndex);
					LogUtils.vLog(TAG, " *** 查询表中指定数据 db key：" + valueString
							+ "; value：" + strValue);
					return_Json.put(valueString, strValue);
				}
				LogUtils.iLog(TAG, " add json:" + return_Json.toString());
				jsonStr += return_Json.toString() + ",";
				LogUtils.iLog(TAG, " add result ok:" + jsonStr);
			}
			result.close();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cursor.close();
		db.close();
		if (selectCount > 0) {
			// 将最后的逗号删除
			jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
		}
		jsonStr += "]";
		LogUtils.vLog(TAG, "selectAll  jsonStr" + jsonStr);
		return jsonStr;
	}

	/***
	 * 查询表数据【查询所有数据】
	 * 
	 * @param tableName
	 *            表名
	 * @param mapValues
	 *            表字段字符串
	 * @return handler
	 */
	public String selectAll(String tableName, Map<String, String> mapValues) {
		/* 查询得到的总条数 */
		int selectCount = 0;
		String jsonStr = "[";
		JSONObject return_Json = new JSONObject();
		LogUtils.vLog(TAG, "--- DBHelper selectAll name:" + tableName);
		// 实例化一个容器，用来拼接sql语句
		StringBuffer sBuffer = new StringBuffer();
		// sql语句，第一个字段为_ID 主键自增，这是通用的，所以直接写死
		sBuffer.append("select * from " + tableName);
		LogUtils.vLog(TAG, "--- db selectAll execSQL:" + sBuffer.toString());
		SQLiteDatabase db = this.getWritableDatabase();
		/* 获取表中所有字段 */
		List<String> tableValues = new ArrayList<String>();
		Cursor cursor = db.rawQuery("pragma table_info(" + tableName + ")",
				null);
		while (cursor.moveToNext()) {
			String[] columnNames = cursor.getColumnNames();
			for (String name : columnNames) {
				if (name != null && name.equals("name")) {

					// 提取value
					String value = cursor
							.getString(cursor.getColumnIndex(name));
					LogUtils.vLog(TAG, "获取表中所有字段 db name " + ",value：" + value);
					tableValues.add(value);
				}
			}
		}

		try {
			/* 查询表中指定数据 */
			Cursor result = db.rawQuery(sBuffer.toString(), null);
			selectCount = result.getCount();
			LogUtils.eLog(TAG, " add result count:" + selectCount);
			while (result.moveToNext()) {
				for (int i = 0; i < tableValues.size(); i++) {
					// 提取value
					String valueString = tableValues.get(i).toString();
					int nameColumnIndex = result.getColumnIndex(valueString);
					String strValue = result.getString(nameColumnIndex);
					LogUtils.vLog(TAG, " *** 查询表中指定数据 db key：" + valueString
							+ "; value：" + strValue);
					return_Json.put(valueString, strValue);
				}
				LogUtils.iLog(TAG, " add json:" + return_Json.toString());
				jsonStr += return_Json.toString() + ",";
				LogUtils.iLog(TAG, " add result ok:" + jsonStr);
			}
			result.close();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cursor.close();
		db.close();
		if (selectCount > 0) {
			// 将最后的逗号删除
			jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
		}
		jsonStr += "]";
		LogUtils.vLog(TAG, "selectAll  jsonStr" + jsonStr);
		return jsonStr;
	}

	/***
	 * 自定义sql查询
	 * 
	 * @param tableName
	 *            表名
	 * @param mapValues
	 *            表字段字符串
	 * @return handler
	 */
	public String theCustomSql(String tableName, String _sql) {
		/* 查询得到的总条数 */
		int selectCount = 0;
		String jsonStr = "[";
		JSONObject return_Json = new JSONObject();
		LogUtils.vLog(TAG, "--- DBHelper theCustomSql name:" + tableName);
		LogUtils.vLog(TAG, "--- db theCustomSql execSQL:" + _sql.toString());
		SQLiteDatabase db = this.getWritableDatabase();
		/* 获取表中所有字段 */
		Map<String, String> tableValues = new HashMap<String, String>();
		Cursor cursor = db.rawQuery("pragma table_info(" + tableName + ")",
				null);
		while (cursor.moveToNext()) {
			String[] columnNames = cursor.getColumnNames();
			for (String name : columnNames) {
				if (name != null && name.equals("name")) {

					// 提取value
					String value = cursor
							.getString(cursor.getColumnIndex(name));
					LogUtils.vLog(TAG, "获取表中所有字段 db name " + ",value：" + value);
					tableValues.put(value, value);
				}
			}
		}

		try {
			/* 查询表中指定数据 */
			Cursor result = db.rawQuery(_sql.toString(), null);
			selectCount = result.getCount();
			LogUtils.eLog(TAG, " add result count:" + selectCount);
			while (result.moveToNext()) {
				for (Map.Entry<String, String> entry : tableValues.entrySet()) {
					// 提取value
					String valueString = entry.getValue().toString();
					int nameColumnIndex = result.getColumnIndex(valueString);
					String strValue = result.getString(nameColumnIndex);
					LogUtils.vLog(TAG, " *** 查询表中指定数据 db key：" + valueString
							+ "; value：" + strValue);
					return_Json.put(valueString, strValue);
				}
				LogUtils.iLog(TAG, " add json:" + return_Json.toString());
				jsonStr += return_Json.toString() + ",";
				LogUtils.iLog(TAG, " add result ok:" + jsonStr);
			}
			result.close();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cursor.close();
		db.close();
		if (selectCount > 0) {
			// 将最后的逗号删除
			jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
		}
		jsonStr += "]";
		LogUtils.vLog(TAG, "selectAll  jsonStr" + jsonStr);
		return jsonStr;
	}
}
