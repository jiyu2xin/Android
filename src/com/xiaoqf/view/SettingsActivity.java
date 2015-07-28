package com.xiaoqf.view;

import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xiaoqf.beans.User;
import com.xiaoqf.common.Consts;
import com.xiaoqf.util.CommonUtil;
import com.xiaoqf.util.EventHandler;
import com.xiaoqf.util.LogUtil;
import com.xiaoqf.util.PropertiesUtil;
import com.xiaoqf.app.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @ClassName: SettingsActivity
 * @Description: 设置 界面
 * @version: 1.0
 * @author: tongdu
 * @Create: 2015-05-07
 */
public class SettingsActivity extends BaseActivity {
	private ProgressDialog pDialog;
	
	// 标题栏
	@ViewInject(R.id.iv_title_left)
	private ImageView backImageView;
	
	@ViewInject(R.id.tv_title)
	private TextView titleTextView; 
	
	@ViewInject(R.id.activity_config_button_exit)
	private RelativeLayout relativeLayout;
	// 当前版本
	@ViewInject(R.id.activity_config_item_version_tv_name)
	private TextView versionNameTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_settings);
		ViewUtils.inject(this);
		
		titleTextView.setText("设置");
		String version =null;
		try {
			version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		versionNameTextView.setText(version);
		if(Consts.userMobile==null)
			relativeLayout.setVisibility(View.INVISIBLE);
	}
	
	// 返回
	@OnClick(R.id.iv_title_left)
	private void onBackClick(View v) {
		onBackPressed();
	}
	
	// 修改登录密码
	@OnClick(R.id.activity_config_item_password_change)
	private void onModifyPasswordClick(View v) {
		if(Consts.userMobile==null) {
			CommonUtil.openActivity(this, LoginFreeRegisterActivity.class);
			//finish();
		}else {
			CommonUtil.openActivity(this, PasswordEditActivity.class);
			//finish();
		}
	}
	
	// 用户协议
	@OnClick(R.id.activity_config_item_agreement)
	private void onAgreementClick(View v) {
		CommonUtil.openActivity(this, AgreementActivity.class);
		//finish();
	}
	
	// 给我们评分
	// 推荐给朋友
	// 意见反馈
/*	@OnClick(R.id.activity_config_item_feedback)
	private void onFeedbackClick(View v) {
		CommonUtil.openActivity(this, FeedbackActivity.class);
		finish();
	}*/
	
	// 关于小Q购房
	@OnClick(R.id.activity_config_item_about)
	private void onAboutClick(View v) {
		CommonUtil.openActivity(this, AboutActivity.class);
		finish();
	}
	
	// 安全退出
	@OnClick(R.id.activity_config_button_exit)
	private void onExitClick(View v) {
		// 防止重复点击
		if (CommonUtil.isFastDoubleClick()) return;
		
		pDialog = EventHandler.showProgress(this, "安全退出中……", false, false);
		// 调用服务端“退出”接口
		RequestParams params = new RequestParams();
		params.addBodyParameter("userMobile", Consts.userMobile);
		params.addBodyParameter("tokenId", Consts.tokenID);
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, Consts.SERVER_URL_USER_LOGOUT, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LogUtil.log(TAG,"responseInfo.result:[" + responseInfo.result + "]");
				try {
					JSONObject jsonObject = new JSONObject(responseInfo.result);
					if ("0".equals(jsonObject.getString("status"))) {
						String errCode = jsonObject.getString("errorCode");
						String errMsg = PropertiesUtil.getProperties(getApplicationContext()).getProperty(errCode);
						EventHandler.showToast(SettingsActivity.this, errMsg);
						return;
					}
					if ("1".equals(jsonObject.getString("status"))) {
						closeDialog();
						cookieStore.clear();
						
						EventHandler.showToast(SettingsActivity.this,  "成功退出登录！");
						CommonUtil.openActivity(SettingsActivity.this,MainActivity.class);
						User user = User.getUser();
						Consts.tokenID=null;
						user.setuId(null);
						Consts.userMobile=null;
						user.setPhone(null);
						setUser(user);
						finish();
					}
					closeDialog();
				} catch (Exception e) {
					LogUtil.logError(e);
					closeDialog();
					EventHandler.showToast(SettingsActivity.this, "服务器忙，请稍后重试");

				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				LogUtil.logError(error);
				closeDialog();
				EventHandler.showToast(SettingsActivity.this, "服务器忙，请稍后重试");

			}
		});
	}
	
	private void closeDialog() {
		if (null != pDialog) {
			pDialog.dismiss();
		}
	}
	
	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}
}
