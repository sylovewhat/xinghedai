package com.tc.emms.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.storage.StorageManager;
import android.util.Log;

import com.tc.emms.base.BaseActivity;

/**
 * 通用工具�?
 */
@SuppressLint("SimpleDateFormat")
public class FileUtils {

	public static String getU_PATH(BaseActivity mActivity) {
		String u_path = SharedUtils.getValue(mActivity, "FILE")
				+ ConstantUtils.FILE_PATH + ConstantUtils.HTML_PATH;
		return u_path;
	}

	public static String getUPLOAD_PATH(BaseActivity mActivity) {
		String u_path = SharedUtils.getValue(mActivity, "FILE")
				+ ConstantUtils.FILE_PATH + ConstantUtils.UPLOAD_PATH;
		return u_path;
	}

	public static String getAPP_PATH(BaseActivity mActivity) {
		String u_path = SharedUtils.getValue(mActivity, "FILE") + "files";
		return u_path;
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	@SuppressWarnings("resource")
	public static boolean copyFile(String oldPath, String newPath) {
		boolean isCopy = false;
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				Log.v("", "img size: " + bytesum);
				inStream.close();
				isCopy = true;
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}
		return isCopy;
	}

	/**
	 * 复制整个文件夹内容
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 */
	public static void copyFolder(String oldPath, String newPath) {

		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			e.printStackTrace();

		}
	}

	@SuppressWarnings("static-access")
	public static String getStoragePath(BaseActivity mContext,
			boolean is_removale) {

		StorageManager mStorageManager = (StorageManager) mContext
				.getSystemService(Context.STORAGE_SERVICE);
		Class<?> storageVolumeClazz = null;
		try {
			storageVolumeClazz = Class
					.forName("android.os.storage.StorageVolume");
			Method getVolumeList = mStorageManager.getClass().getMethod(
					"getVolumeList");
			Method getPath = storageVolumeClazz.getMethod("getPath");
			Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
			Object result = getVolumeList.invoke(mStorageManager);
			final int length = Array.getLength(result);
			for (int i = 0; i < length; i++) {
				Object storageVolumeElement = Array.get(result, i);
				String path = (String) getPath.invoke(storageVolumeElement);
				boolean removable = (Boolean) isRemovable
						.invoke(storageVolumeElement);
				if (is_removale == removable) {
					return path;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*百度文字识别*/
	public static File getSaveFile(Context context) {
        File file = new File(context.getFilesDir(), "pic.jpg");
        return file;
    }
}
