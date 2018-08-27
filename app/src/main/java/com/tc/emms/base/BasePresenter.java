package com.tc.emms.base;

import com.tc.emms.config.BaseApplictaion;

import android.os.Handler;

/*
 * 接口请求信息基类
 */
public class BasePresenter {
	
	public static Handler getHandler() {
		return BaseApplictaion.getHandler();
	}

	public static void runInUI(Runnable runnable) {
		BaseApplictaion.getHandler().post(runnable);
	}
	
	public interface ILoadDataUIRunnadle {
		/**
		 * 返回true代表终止数据加载任务
		 * 
		 * @return
		 */
		public boolean onPreRun();

		/**
		 * 数据加载结束时的回调
		 * 
		 * @return
		 */
		public void onPostRun(Object... params);

		/**
		 * 数据加载失败时候的回调
		 * 
		 * @return
		 */
		public void onFailUI(Object... params);
	}

	public static class LoadDataUIRunnadle implements ILoadDataUIRunnadle {
		/**
		 * 返回false代表终止数据加载任务
		 * 
		 * @return
		 */
		@Override
		public boolean onPreRun() {
			return false;
		}

		/**
		 * 数据加载结束时的回调
		 * 
		 * @return
		 */
		@Override
		public void onPostRun(Object... params) {

		}

		/**
		 * 数据加载失败时候的回调
		 * 
		 * @return
		 */
		@Override
		public void onFailUI(Object... params) {

		}
	}

	public interface ILoadPostRunnable {
		public void run(boolean isSuccess);
	}
}
