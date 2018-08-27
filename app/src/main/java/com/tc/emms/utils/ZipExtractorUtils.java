package com.tc.emms.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tc.emms.base.BaseActivity;

public class ZipExtractorUtils extends AsyncTask<Void, Integer, Long> {
	private final String TAG = "ZipExtractorTask";
	private final File mInput;
	private final File mOutput;
	private int mProgress = 0;
	private final BaseActivity mContext;
	private boolean mReplaceAll;
	Handler down_handler;
	public ZipExtractorUtils(String in, String out, BaseActivity context, boolean replaceAll,
			Handler d_handler){
		super();
		down_handler = d_handler;
		mInput = new File(in);//压缩包资源
		mOutput = new File(out);

		if(!mOutput.exists()){
			if(!mOutput.mkdirs()){
				LogUtils.eLog(TAG, "Failed to make directories:"+mOutput.getAbsolutePath());
				Log.d("sylove","---------------创建文件夹失败-------------------"+mOutput.getAbsolutePath());
			}
		}

		mContext = context;
		mReplaceAll = replaceAll;
	}

    /**
     * onPreExecute执行完毕后，执行该方法，参数传到了这个方法中，执行完毕后必须返回一个值，还可以使用publishProgress(Progress...)发布进度到onProgressUpdate(Progress...)，便于更新进度
     * @param params
     * @return
     */
	@Override
	protected Long doInBackground(Void... params) {
		// TODO Auto-generated method stub

		return unzip();

	}


    /**
     * 当doInBackground(Params...)执行完毕后即执行该方法
     * @param result
     */
	@Override
	protected void onPostExecute(Long result) {

		/* 解压资源文件完成 */
		Message msg = new Message();
		msg.what = 4;
		down_handler.sendMessage(msg);
		Log.d("", "解压资源文件完成！");
		Log.d("sylove", "解压资源文件完成！!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		if(isCancelled())
			return;
	}

    /**
     * 在task被执行之后，立刻调用
     */
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub

	}
	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		/* 更新进度 values[0].intValue() */
		Message msg = new Message();
		msg.what = 3;
		msg.arg1 = values[0].intValue();

		down_handler.sendMessage(msg);
	}
	@SuppressWarnings("unchecked")
	private long unzip(){

		long extractedSize = 0L;
		Enumeration<ZipEntry> entries;
		ZipFile zip = null;
		try {
			zip = new ZipFile(mInput);
			long uncompressedSize = getOriginalSize(zip);
			publishProgress(0, (int) uncompressedSize);

			entries = (Enumeration<ZipEntry>) zip.entries();
			while(entries.hasMoreElements()){
				ZipEntry entry = entries.nextElement();
				if(entry.isDirectory()){
					continue;
				}
				File destination = new File(mOutput, entry.getName());
				if(!destination.getParentFile().exists()){
					LogUtils.eLog(TAG, "make="+destination.getParentFile().getAbsolutePath());
					Log.d("sylove","make创建"+destination.getParentFile().getAbsolutePath());
					destination.getParentFile().mkdirs();
				}
				if(destination.exists()&&mContext!=null&&!mReplaceAll){

				}
				ProgressReportingOutputStream outStream = new ProgressReportingOutputStream(destination);
				extractedSize+=copy(zip.getInputStream(entry),outStream);
				outStream.close();
			}
		} catch (ZipException e) {
			Log.d("sylove","解压异常捕捉1");
			e.printStackTrace();
		} catch (IOException e) {
			Log.d("sylove","输入输出流异常");
			e.printStackTrace();
		}finally{
			Log.d("sylove","最后");
			try {
				if(zip != null){
					zip.close();
				}
			} catch (IOException e) {
				Log.d("sylove","最后");
				e.printStackTrace();
			}
		}


		return extractedSize;
	}

	@SuppressWarnings("unchecked")
	private long getOriginalSize(ZipFile file){
		Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) file.entries();
		long originalSize = 0l;
		while(entries.hasMoreElements()){
			ZipEntry entry = entries.nextElement();
			if(entry.getSize()>=0){
				originalSize+=entry.getSize();
			}
		}
		return originalSize;
	}
	
	private int copy(InputStream input, OutputStream output){
		byte[] buffer = new byte[1024*8];
		BufferedInputStream in = new BufferedInputStream(input, 1024*8);
		BufferedOutputStream out  = new BufferedOutputStream(output, 1024*8);
		int count =0,n=0;
		try {
			while((n=in.read(buffer, 0, 1024*8))!=-1){
				out.write(buffer, 0, n);
				count+=n;
			}
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return count;
	}
	
	private final class ProgressReportingOutputStream extends FileOutputStream{

		public ProgressReportingOutputStream(File file)
				throws FileNotFoundException {
			super(file);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void write(byte[] buffer, int byteOffset, int byteCount)
				throws IOException {
			// TODO Auto-generated method stub
			super.write(buffer, byteOffset, byteCount);
		    mProgress += byteCount;
		    publishProgress(mProgress);
		}
		
	}
}
