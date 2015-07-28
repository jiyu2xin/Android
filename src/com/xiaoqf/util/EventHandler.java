package com.xiaoqf.util;

import java.lang.reflect.Field;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.widget.Toast;

/**
 * @ClassName:EventHandler
 * @Description:事件工具
 * @date:2015-5-5 16:55
 */
public class EventHandler extends Handler {
	private static EventHandler instance;

	private EventHandler() {

	}

	public synchronized static EventHandler getInstance() {
		if (instance == null) {
			instance = new EventHandler();
		}
		return instance;
	}

	/**
	 * 显示提示信息
	 * 
	 * @param ctx
	 *            环境
	 * @param msg
	 *            信息
	 */
	public static void showToast(Context ctx, String msg) {
		Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 显示提示信息
	 * 
	 * @param ctx
	 *            环境
	 * @param msg
	 *            信息
	 * @param duration
	 *            显示时长
	 */
	public static void showToast(Context ctx, String msg,int duration){
		Toast.makeText(ctx, msg, duration).show();
	}

	/**
	 * 显示进度条
	 * 
	 * @param context
	 *            环境
	 * @param title
	 *            标题
	 * @param msg
	 *            信息
	 * @param cancelable
	 *            可撤销
	 * @param indeterminate
	 *            确定性
	 * @return
	 */
	public static ProgressDialog showProgress(Context context, String msg,
			boolean cancelable, boolean indeterminate) {
		ProgressDialog pDialog = new ProgressDialog(context);
		pDialog.setMax(100);
		pDialog.setMessage(msg);
		pDialog.setCancelable(cancelable);
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setIndeterminate(indeterminate);
		pDialog.show();
		return pDialog;
	}

	public static void allowDialogDismissed(DialogInterface dialog, boolean allowed ) {
		try {
			Class<?> c = dialog.getClass().getSuperclass();  //AlertDialog
			if( dialog instanceof ProgressDialog ) c = c.getSuperclass();
			
			Field field = c.getDeclaredField("mShowing");
			field.setAccessible(true);
			// 将mShowing变量设为false，表示对话框已关闭
			field.set(dialog, allowed);
			// dialog.dismiss();
		} catch (Exception e) {
			LogUtil.logError(e);
		}
	}

}