package com.tc.emms.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
 
/**
 * 下载工具类
 * @author lu
 *
 */
public class DownloadTask{
     
    /**进度这里固定为100*/
    public static final int jd = 100;
 
    /**
     * 
     * @param urlStr http://f.txt
     * @param path /mnt/sdcard/f.txt
     * @param handler what=0失败1成功2进度：arg1(1~100)
     */
    public static void to(final String urlStr,final String path,final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                File f = new File(path);
                if (f.exists()) {
                    Log.d("", "file exists delete:" + f.exists());
                    boolean isCopy = FileUtils.copyFile(path, path + ".back");
                    Log.d("", "file copy exists:" + isCopy);
                    if(isCopy){
                    	f.delete();
                    }else{
                    	Log.d("", "file copy error:" + isCopy);
                    	handler.sendEmptyMessage(ConstantUtils.U_DOWN_COM);
                    }
                }
                InputStream is = null;
                OutputStream os = null;
                try {
                    // 构造URL   
                    URL url = new URL(urlStr);   
                    // 打开连接   
                    URLConnection con = url.openConnection();
                    //获得文件的长度
                    int contentLength = con.getContentLength();
                    int cd = 0;
                    if (contentLength>=jd) {
                        cd = contentLength/jd;//计算出文件长度的1/100用于进度
                    }
                    // 输入流   
                    is = con.getInputStream();  
                    // 1K的数据缓冲   
                    byte[] bs = new byte[1024];   
                    // 读取到的数据长度   
                    int len;   
                    // 输出的文件流   
                    os = new FileOutputStream(path);   
                    // 开始读取   
                    int i = 0;
                    int arg = 0;
                    while ((len = is.read(bs)) != -1) {   
                        os.write(bs, 0, len);   
                        i+=len;
                        if (i>=cd) {
                            Message msg = Message.obtain();
                            msg.arg1 = ++arg;
                            msg.what = ConstantUtils.U_DOWN_PROCESS;
                            handler.sendMessage(msg);
                            i=0;
                        }else{
                        	//Log.v("", "down seccess and last");
                        }
                    }  
                    // 完毕，关闭所有链接   
                    os.flush();
                    os.close();  
                    is.close();
                    handler.sendEmptyMessage(ConstantUtils.U_DOWN_COM);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0);
                    Message msg = Message.obtain();
                    msg.what = ConstantUtils.U_DOWN_ERR;
                    handler.sendMessage(msg);
                } 
            }
        }).start();
    }
     
}
