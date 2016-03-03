package com.lll.app.utils;

import java.util.Stack;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
/**
 * 管理Activity
 * @author liulongling
 *
 */
public class AppManager {
	
	private static Stack<Activity> activityStack;

	private AppManager() {}
	
	/**
	 * 单例
	 */
	public static AppManager getAppManager() {
		return AppManagerHolder.sInstance;
	}
	
	/**
	 * 静态内部类
	 * @author liulongling
	 *
	 */
	private static class AppManagerHolder{
		private static final AppManager sInstance = new AppManager();
	}
	
	public int activitySize() {
		return activityStack.size();
	}
	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity() {
		Activity activity = null;
		if(activityStack!=null){
			try {
				activity = activityStack.lastElement();
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		return activity;
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		if (activity != null) {
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}


	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 退出应用程序
	 */
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {
		}
	}
}
