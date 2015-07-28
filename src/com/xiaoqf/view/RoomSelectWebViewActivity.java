package com.xiaoqf.view;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xiaoqf.util.CommonUtil;
import com.xiaoqf.app.R;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @ClassName: AgreementActivity
 * @Description: 用户协议 界面
 * @version: 1.0
 * @author: tongdu
 * @Create: 2015-05-29
 */
public class RoomSelectWebViewActivity extends BaseActivity {
	private String url = "http://120.24.227.76:8080/project/item.do?projectId=";//"http://xiaoqf.com/agreement.html";//"file:///android_asset/agreement.htm";
	
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
		setContentView(R.layout.activity_agreement);
		ViewUtils.inject(this);
		
		titleTextView.setText("用户协议");
		
		agreementWebView.loadUrl(url);
		agreementWebView.setWebViewClient(new WebViewClient(){

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
			
		});
	}
	
	@OnClick(R.id.iv_title_left)
	public void onClickBack(View v) {
		//super.onBackPressed();
		onBackPressed();
	}
	
	@Override
	public void onBackPressed() {
		//CommonUtil.openActivity(this, SettingsActivity.class);
		super.onBackPressed();
		finish();
	}
}
