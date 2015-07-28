package com.xiaoqf.util;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.app.Activity;

/**
 * @ClassName:ViewManagerUtil
 * @Description:界面管理工具
 * @date:2015-5-5 15:57
 */
public class ViewManagerUtil {
	// 用于记录activity栈，方便退出程序（这里为了不影响系统回收activity，所以用软引用）
	private final HashMap<String, SoftReference<Activity>> activityStack = new HashMap<String, SoftReference<Activity>>();
	private static ViewManagerUtil instance;

	private ViewManagerUtil() {
	}

	public static ViewManagerUtil getInstance() {
		if (instance == null) {
			instance = new ViewManagerUtil();
		}
		return instance;
	}

	/**
	 * 退出栈顶Activity
	 * 
	 * @param activity
	 */
	public void popActivity(Activity activity) {
		activityStack.remove(activity.toString());
	}

	/**
	 * 将当前Activity加入栈中
	 * 
	 * @param activity
	 */
	public void pushActivity(Activity activity) {
		activityStack.put(activity.toString(), new SoftReference<Activity>(
				activity));
	}

	/**
	 * 退出栈中所有Activity
	 */
	public void exit() {
		try {
			for (Iterator<Entry<String, SoftReference<Activity>>> iterator = activityStack
					.entrySet().iterator(); iterator.hasNext();) {
				SoftReference<Activity> activityReference = iterator.next()
						.getValue();
				Activity activity = activityReference.get();
				if (activity != null) {
					activity.finish();
				}
			}
			activityStack.clear();
		} catch (Exception e) {
			LogUtil.logError(e);
		} finally {
			System.exit(0);
		}

	}
}