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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @ClassName: PasswordFindActivity
 * @Description: 忘记密码 界面
 * @version: 1.0
 * @author: tongdu
 * @Create: 2015-05-07
 */
public class PasswordFindActivity extends BaseActivity {
	
	protected static final String TAG = "PasswordFindActivity";

	public ProgressDialog progressDialog = null;
	private Context mContext;
	
	@ViewInject(R.id.iv_title_left)
	private ImageView backImageView;
	
	@ViewInject(R.id.tv_title)
	private TextView titleTextView;
	
	@ViewInject(R.id.activity_password_find_item_et_num)
	private EditText phoneNumEditView;
	
	@ViewInject(R.id.activity_password_find_item_et_code)
	private EditText SecurityCodeEditView;
	
	@ViewInject(R.id.activity_password_find_item_et_new)
	private EditText newPasswordEditView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_password_find);
		ViewUtils.inject(this);
		mContext = this;
		titleTextView.setText("忘记密码");
	}
	
	//获取验证码按钮响应
	@OnClick(R.id.activity_password_find_item_tv_code)
	private void onGetRegisterCodeClick(View v) {
		//防止重复点击
		if(CommonUtil.isFastDoubleClick())
			return;
		String phoneNum = phoneNumEditView.getText().toString();
		
		if("".equals(phoneNum)||!phoneNum.matches("^[1-9]\\d{10}$")) {
			EventHandler.showToast(this, "您输入的电话号码格式不正确或为空！");//^(13[0-9]|15[0|1|3|6|7|8|9]|18[0-9]|14[7])\\d{8}$
			return;
		}else{
			LogUtil.log(TAG, phoneNum);
		}
		
		progressDialog = EventHandler.showProgress(this, "加载中...",false,false);
		RequestParams params = new RequestParams();
		params.addBodyParameter("userMobile", phoneNum);
		params.addBodyParameter("operFlag", "RESET");
		
		HttpUtils https = new HttpUtils();
		https.send(HttpRequest.HttpMethod.POST, Consts.SERVER_URL_USER_GET_SECURITYCODE, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LogUtil.log(TAG,"responseInfo.result:[" + responseInfo.result + "]");
				try {
					JSONObject jsonObject = new JSONObject(responseInfo.result);
					if ("0".equals(jsonObject.getString("status"))) {
						closeProgressDialog();
						String errCode = jsonObject.getString("errorCode");
						String errMsg = PropertiesUtil.getProperties(getApplicationContext()).getProperty(errCode);
						EventHandler.showToast(PasswordFindActivity.this, errMsg);
						return;
					}
					
					if ("1".equals(jsonObject.getString("status"))) {
						//progressDialog.dismiss();
						closeProgressDialog();
						EventHandler.showToast(PasswordFindActivity.this, "验证码发送成功！");
						LogUtil.log(TAG, "验证码发送成功！");
						//return;
					}
					//JSONObject datasJsonObject = new JSONObject(jsonObject.getString("value"));
					
					
				} catch (Exception e) {
					LogUtil.logError(e);
					closeProgressDialog();
					EventHandler.showToast(PasswordFindActivity.this, "服务器很忙，请稍后重试");
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				LogUtil.logError(error);
				closeProgressDialog();
				EventHandler.showToast(PasswordFindActivity.this, "服务器忙，请稍后重试");

			}
		});
	}
	
	//确认
	@OnClick(R.id.activity_password_find_item_rl_confirm)
	public void onComfirmClick(View v) {

		// 防止重复点击
		if (CommonUtil.isFastDoubleClick())
			return;

		final String phoneNum = phoneNumEditView.getText().toString();
		if ("".equals(phoneNum)||!phoneNum.matches("^[1-9]\\d{10}$")) {
			EventHandler.showToast(PasswordFindActivity.this, "请输入手机号");
			return;
		}
		String securityCode = SecurityCodeEditView.getText().toString();
		if ("".equals(securityCode)) {
			EventHandler.showToast(this, "请输入验证码!");
			return;
		}
		String newPassword = newPasswordEditView.getText().toString();
		if ("".equals(newPassword)) {
			EventHandler.showToast(this, "请输入密码!");
			return;
		}

		progressDialog = ProgressDialog.show(PasswordFindActivity.this, "", "加载中...");
		// 调用服务端登陆接口
		RequestParams params = new RequestParams();
		params.addBodyParameter("userMobile", phoneNum);
		params.addBodyParameter("mobileVerCode", securityCode);
		params.addBodyParameter("newPassword", newPassword);
		
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, Consts.SERVER_URL_FIND_PASSWORD, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LogUtil.log(TAG,"responseInfo.result:[" + responseInfo.result + "]");
				try {
					JSONObject jsonObject = new JSONObject(responseInfo.result);
					if ("0".equals(jsonObject.getString("status"))) {
						String errCode = jsonObject.getString("errorCode");
						String errMsg = PropertiesUtil.getProperties(getApplicationContext()).getProperty(errCode);
						EventHandler.showToast(PasswordFindActivity.this, errMsg);
						closeProgressDialog();
						return;
					}
					if ("1".equals(jsonObject.getString("status"))) {
						EventHandler.showToast(PasswordFindActivity.this,  "重置密成功！");
						closeProgressDialog();
						Consts.tokenID = jsonObject.getString("tokenId");
						User user = User.getUser();
						user.setuId(Consts.tokenID);
						Consts.userMobile=phoneNum;
						user.setPhone(phoneNum);
						setUser(user);
						Intent intent = new Intent(mContext,MainActivity.class);
						startActivity(intent);
						finish();
					}
					
					//setResult(RESULT_OK, intent);		
				} catch (Exception e) {
					LogUtil.logError(e);
					closeProgressDialog();
					EventHandler.showToast(PasswordFindActivity.this, "服务器忙，请稍后重试");

				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				LogUtil.logError(error);
				closeProgressDialog();
				EventHandler.showToast(PasswordFindActivity.this, "服务器忙，请稍后重试");

			}
		});
	}
	
	@OnClick(R.id.iv_title_left)
	public void onPasswordBackClick(View v) {
		//CommonUtil.openActivity(this, LoginActivity.class);
		//finish();
		onBackPressed();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		//CommonUtil.openActivity(this, LoginActivity.class);
		finish();
	}
	
	private void closeProgressDialog() {
		if (null != progressDialog) {
			progressDialog.dismiss();
		}
	}	
}
