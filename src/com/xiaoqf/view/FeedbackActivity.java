package com.xiaoqf.view;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xiaoqf.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @ClassName: FeedbackActivity
 * @Description: 意见反馈 界面
 * @version: 1.0
 * @author: tongdu
 * @Create: 2015-06-04
 */
public class FeedbackActivity extends BaseActivity {
	// 标题栏
	@ViewInject(R.id.iv_title_left)
	private ImageView backImageView;
	
	@ViewInject(R.id.tv_title)
	private TextView titleTextView;
	
	@ViewInject(R.id.activity_agreement_webview)
	private WebView agreementWebView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_feedback);
		ViewUtils.inject(this);
		
		titleTextView.setText("意见反馈");
	}
	
	@OnClick(R.id.iv_title_left)
	public void onClickBack(View v) {
		super.onBackPressed();
	}
	
}
