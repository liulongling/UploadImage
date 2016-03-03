package com.lll.app.utils;

import java.util.Stack;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
/**
 * ����Activity
 * @author liulongling
 *
 */
public class AppManager {
	
	private static Stack<Activity> activityStack;

	private AppManager() {}
	
	/**
	 * ����
	 */
	public static AppManager getAppManager() {
		return AppManagerHolder.sInstance;
	}
	
	/**
	 * ��̬�ڲ���
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
	 * ���Activity����ջ
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * ��ȡ��ǰActivity����ջ�����һ��ѹ��ģ�
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
	 * ������ǰActivity����ջ�����һ��ѹ��ģ�
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		if (activity != null) {
			activity.finish();
			activity = null;
		}
	}

	/**
	 * ����ָ����Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}


	/**
	 * ����ָ��������Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * ��������Activity
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
	 * �˳�Ӧ�ó���
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
