package com.xiaoqf.view;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xiaoqf.util.CommonUtil;
import com.xiaoqf.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowNoNetWorkActivity extends Activity{

	@ViewInject(R.id.iv_title_left)
	private ImageView imageView;
	
	@ViewInject(R.id.tv_title)
	private TextView titleTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_no_network);
		ViewUtils.inject(this);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		imageView.setVisibility(View.INVISIBLE);
		Intent intent = getIntent();
		titleTextView.setText(intent.getStringExtra("TITLE_FLAG"));
	}
	
	@OnClick(R.id.activity_reloading_button)
	public void onReloadingButtonClick(View v) {
		//onBackPressed();
		CommonUtil.openActivity(ShowNoNetWorkActivity.this, MainActivity.class);
		finish();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}
