package com.xiaoqf.view;

import com.xiaoqf.app.R;

import android.os.Bundle;

public class MapActivity extends BaseActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
		//SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_map);
		//获取地图控件引用  
		//mMapView = (MapView) findViewById(R.id.bmapView);
	}

}
