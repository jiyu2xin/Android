package com.xiaoqf.service;

import com.xiaoqf.common.Consts;
import com.xiaoqf.util.RegisterCodeTimer;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

/**
 * 注册验证码计时服务
 */
public class RegisterCodeTimerService extends Service {
	
	private static final long TOTAL_TIME_MS = Consts.isDebugMode ? 6*1000 : 180*1000;
	private static final long TIME_INTERVAL_MS = 1 * 1000;
	private static Handler mHandler;
	private static RegisterCodeTimer mCodeTimer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		mCodeTimer = new RegisterCodeTimer(TOTAL_TIME_MS, TIME_INTERVAL_MS, mHandler);
		mCodeTimer.start();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 设置Handler
	 */
	public static void setHandler(Handler handler) {
		mHandler = handler;
	}

}
