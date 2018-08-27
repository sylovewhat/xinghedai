package com.tc.emms.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.tc.emms.base.BaseActivity;

/**
 * 公用的常用工具类
 * 
 * @author xiaoben
 */
public class ImageUtils {

	/**
	 * 获取图片名称获取图片的资源id的方法
	 * 
	 * @param imageName
	 * @return
	 */
	public static int getResourceID(BaseActivity mActivity, String imageName) {
		Context ctx = mActivity.getBaseContext();
		int resId = mActivity.getResources().getIdentifier(imageName,
				"drawable", ctx.getPackageName());
		Log.v("", "根据图片名称获取图片的资源id的方法 getResourceID: " + resId);
		return resId;
	}

	/**
	 * bitmap转为base64
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String bitmapToBase64(Bitmap bitmap, int compression) {

		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, compression, baos);

				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * base64转为bitmap
	 * 
	 * @param base64Data
	 * @return
	 */
	public static Bitmap base64ToBitmap(String base64Data) {
		byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	public static boolean saveText(String text, String PATH) {

		if (text == null)
			return false;

		try {
			FileWriter fw = new FileWriter(PATH + "/aa.txt");
			fw.flush();
			fw.write(text);
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public static Bitmap getBitmapFromPath(String path) {

		if (!new File(path).exists()) {
			System.err.println("getBitmapFromPath: file not exists");
			return null;
		}
		FileInputStream fis = null;
		byte[] buf = new byte[1024 * 1024];// 1M
		Bitmap bitmap = null;
		try {

			fis = new FileInputStream(path);
			int len = fis.read(buf, 0, buf.length);
			bitmap = BitmapFactory.decodeByteArray(buf, 0, len);
			if (bitmap == null) {
				System.out.println("len= " + len);
				System.err
						.println("path: " + path + "  could not be decode!!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bitmap;
	}

	/**
	 * 保存bitmap到SD卡
	 * 
	 * @param bitmap
	 * @param imagename
	 */
	public static boolean saveBitmapToSDCard(Bitmap bitmap, String path) {
		boolean isOk = false;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path);
			if (fos != null) {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
				fos.close();
			}
			isOk = true;
			return isOk;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isOk;
	}
	
	//保存文件到指定路径
    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "dearxy";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
