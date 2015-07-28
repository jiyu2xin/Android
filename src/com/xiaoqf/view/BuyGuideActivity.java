package com.xiaoqf.view;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xiaoqf.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BuyGuideActivity extends Activity {
	// 标题栏部件
	@ViewInject(R.id.iv_title_left)
	private ImageView backImageView;
	
	@ViewInject(R.id.tv_title)
	private TextView titleTextView;
	
	@ViewInject(R.id.tv_title_right)
	private TextView titleRightTextView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy_guide);
		ViewUtils.inject(this);
		
		titleTextView.setText("购买流程");
	}
	
	@OnClick(R.id.iv_title_left)
	private void onTitleBackClick(View v) {
		onBackPressed();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

}
