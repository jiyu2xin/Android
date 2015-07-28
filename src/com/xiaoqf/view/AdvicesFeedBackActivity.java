package com.xiaoqf.view;

import org.json.JSONArray;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xiaoqf.common.Consts;
import com.xiaoqf.customview.ContactDialog;
import com.xiaoqf.util.CommonUtil;
import com.xiaoqf.util.EventHandler;
import com.xiaoqf.util.LogUtil;
import com.xiaoqf.app.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @ClassName: AboutActivity
 * @Description: 关于 界面
 * @version: 1.0
 * @author: tongdu
 * @Create: 2015-06-08
 */
public class AdvicesFeedBackActivity extends BaseActivity {
	// 标题栏
	@ViewInject(R.id.iv_title_left)
	private ImageView backImageView;
	
	@ViewInject(R.id.tv_title)
	private TextView titleTextView;
	
	@ViewInject(R.id.activity_advice_feedback_et)
	private EditText editView;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_advice_feedback);
		ViewUtils.inject(this);
		
		titleTextView.setText("意见反馈");
	}
	
	@OnClick(R.id.iv_title_left)
	public void onClickBack(View v) {
		onBackPressed();
	}
	
	@Override
	public void onBackPressed() {
		//CommonUtil.openActivity(this, SettingsActivity.class);
		super.onBackPressed();
		finish();
	}
	
	@OnClick(R.id.activity_advice_feedback_tv)
	public void onFeedBackClick(View v) {
		String text = editView.getText().toString();
		if(text==null)
			return;
		RequestParams params = new RequestParams();
		params.addBodyParameter("context", text);
		
		if(Consts.userMobile!=null)
			params.addBodyParameter("userMobile", Consts.userMobile);//测试用户18673218888
		
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST,
				Consts.SERVER_URL_FEED_BACK,params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtil.log(TAG, "responseInfo.result:["
								+ responseInfo.result + "]");
						try {
							EventHandler.showToast(AdvicesFeedBackActivity.this,
									"提交成功，谢谢分享！");
							finish();

						} catch (Exception e) {
							LogUtil.logError(e);
							EventHandler.showToast(AdvicesFeedBackActivity.this,
									"服务器忙，请稍后重试");
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						LogUtil.logError(error);
						EventHandler.showToast(AdvicesFeedBackActivity.this,
						 "服务器忙，提交失败！");
					}
				});
	}
	
	
}
