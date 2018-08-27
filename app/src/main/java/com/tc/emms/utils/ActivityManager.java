package com.tc.emms.utils;

import java.util.Stack;

import android.app.Activity;
import android.util.Log;

/**
 * activity管理类
 * 
 */
public class ActivityManager {
	private static String TAG = "ActivityManager";

	/**
	 * 接收activity的Stack
	 */
	public static Stack<Activity> activityStack = null;
	private static ActivityManager activityManager = new ActivityManager();

	private ActivityManager() {
	}

	/**
	 * 单实例
	 * 
	 * @return
	 */
	public static ActivityManager getInstance() {
		return activityManager;
	}

	public static Stack<Activity> getStack() {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		return activityStack;
	}

	/**
	 * 将activity移出栈
	 * 
	 * @param activity
	 */
	public void popActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
		}
	}

	/**
	 * 结束指定activity
	 * 
	 * @param activity
	 */
	public void endActivity(Activity activity) {
		if (activity != null) {
			activity.finish();
			Log.d("sylove","结束当前层=========================");
			activityStack.remove(activity);
			activity = null;
		}
	}

	/**
	 * 获得当前的activity(即最上层)
	 * 
	 * @return
	 */
	public Activity currentActivity() {
		Activity activity = null;
		if (!activityStack.empty())
			activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 将activity推入栈内
	 * 
	 * @param activity
	 */
	public void pushActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 弹出除cls外的所有activity
	 * 
	 * @param cls
	 */
	public void popAllActivityExceptOne(Class<? extends Activity> cls) {
		while (true) {
			Activity activity = currentActivity();
			if (activity == null) {
				break;
			}
			if (activity.getClass().equals(cls)) {
				break;
			}
			popActivity(activity);
		}
	}

	/**
	 * 结束除cls之外的所有activity,执行结果都会清空Stack
	 * 
	 * @param cls
	 */
	public void finishAllActivityExceptOne(Class<? extends Activity> cls) {
		while (!activityStack.empty()) {
			Activity activity = currentActivity();
			if (activity.getClass().equals(cls)) {
				popActivity(activity);
			} else {
				endActivity(activity);
			}
		}
	}

	/**
	 * 结束指定位数activity,执行结果都会清空Stack
	 * @param cls
	 */
	public boolean finishActivityExceptIndex(int endIndex) {
		boolean isOk = true;
		int activitySize = 0;

		if (activityStack != null) {
			Log.d("sylove","回退栈大小========================="+activityStack.size());
			activitySize = activityStack.size();
		}

		if(activitySize > endIndex){
			LogUtils.vLog(TAG, "finishActivityExceptIndex endIndex: " + endIndex);
			LogUtils.vLog(TAG, "finishActivityExceptIndex activitySize: " + activitySize);

			Log.d("sylove","回退层数========================="+endIndex);
			Log.d("sylove","回退栈具体层数========================="+activitySize);
			int nowBackIndex = 0;
//			while (!activityStack.empty()) {
//				nowBackIndex++;
//				Activity activity = currentActivity();
//				if (nowBackIndex <= endIndex) {
//					endActivity(activity);
//				}
//			}

			for(int i=0;i<endIndex;i++)
			{
				nowBackIndex++;
				Activity activity = currentActivity();
				if (nowBackIndex <= endIndex) {
					endActivity(activity);
				}
			}


		}else{
			isOk = false;
			LogUtils.vLog(TAG, "finishActivityExceptIndex error: backNum大小越界");
			Log.d("sylove","大小越界了=======================================================");
		}
		return isOk;
	}

	/**
	 * 结束所有activity
	 */
	public void finishAllActivity() {
		while (!activityStack.empty()) {
			Activity activity = currentActivity();
			endActivity(activity);
		}
	}
}
