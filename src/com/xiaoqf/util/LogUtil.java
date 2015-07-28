package com.xiaoqf.util;

import com.xiaoqf.common.Consts;

import android.util.Log;

/**
 * 日志工具
 */
public class LogUtil {
	private static boolean isDebug = Consts.isDebugMode;// 调试开关

	/**
	 * log方式打印界面启动日志
	 * 
	 * @param tag
	 */
	public static void logCreate(String tag) {
		if (isDebug) {
			Log.d(tag, "onCreate");
		}
	}

	/**
	 * log方式打印日志
	 * 
	 * @param tag
	 *            标签
	 * @param info
	 *            信息
	 */
	public static void log(String tag, String info) {
		if (isDebug) {
			Log.d(tag, info);
		}

	}

	/**
	 * system方式打印日志
	 * 
	 * @param tag
	 *            标签
	 * @param info
	 *            信息
	 */
	public static void system(String tag, String info) {
		if (isDebug) {
			System.out.println(tag + "->" + info);
		}
	}

	/**
	 * log方式打印异常日志
	 * 
	 * @param e
	 */
	public static void logError(Exception e) {
		if (isDebug) {
			e.printStackTrace();
		}
	}

}
