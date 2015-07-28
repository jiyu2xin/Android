package com.xiaoqf.common;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

public class XiaoQApplication extends Application {
	
	public static String keyWord = "";
	public static int versionCode = 1;
	public static String apkDownloadUrl = "";
	
	//百度相关
	//public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public Vibrator mVibrator;
	
	@Override
	public void onCreate() {
		super.onCreate();
		//SDKInitializer.initialize(getApplicationContext());
		initBaiduLocation();
	}
	
	private void initBaiduLocation() {
		//mLocationClient = new LocationClient(this.getApplicationContext());
		mGeofenceClient = new GeofenceClient(getApplicationContext());
		mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
	}
}
