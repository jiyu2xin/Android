package com.xiaoqf.view;

import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
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
 * @ClassName: PasswordEditActivity
 * @Description: 修改密码 界面
 * @version: 1.0
 * @author: tongdu
 * @Create: 2015-05-07
 */
public class PasswordEditActivity extends BaseActivity {
	
	protected static final String TAG = "PasswordEditActivity";
	public ProgressDialog progressDialog = null;
	private Context mcontext;
	
	@ViewInject(R.id.iv_title_left)
	private ImageView backImageView;
	
	@ViewInject(R.id.tv_title)
	private TextView titleTextView;
	
	@ViewInject(R.id.activity_password_edit_item_et_num)
	private EditText comfirmNumEditText;
	
	@ViewInject(R.id.activity_password_edit_item_et_old)
	private EditText oldPassworEditText;
	
	@ViewInject(R.id.activity_password_edit_item_et_new)
	private EditText newPasswordEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_password_edit);
		ViewUtils.inject(this);
		init();
		titleTextView.setText("修改密码");
	}

	void init() {
		mcontext = this;
	}
	
	// 返回按钮
	@OnClick(R.id.iv_title_left)
	private void onBackClick(View v) {
		onBackPressed();
	}
	
	@OnClick(R.id.activity_password_edit_item_rl_confirm)
	private void onModifyPasswordClick(View v) {
		// 防止重复点击
		if (CommonUtil.isFastDoubleClick())
			return;
			
		String oldpassword = oldPassworEditText.getText().toString();
		if ("".equals(oldpassword)) {
			EventHandler.showToast(this, "请输入密码");
			return;
		} else if(!oldpassword.matches("^\\d{6}$")) {
			EventHandler.showToast(this, "请输入六位数字密码的旧密码!");
			return;
		}
		
		String newpassword = newPasswordEditText.getText().toString();
		if ("".equals(newpassword)) {
			EventHandler.showToast(this, "请输入新密码");
			return;
		}else if(!newpassword.matches("^\\d{6}$")) {
			EventHandler.showToast(this, "请输入六位数字密码的新密码!");
			return;
		}
		
		String comfirmNum = comfirmNumEditText.getText().toString();
		if (!newpassword.equals(comfirmNum)) {
			EventHandler.showToast(PasswordEditActivity.this, "您输入的新密码和确认密码不符或为空！");
			return;
		}
		progressDialog = EventHandler.showProgress(this, "加载中...",false,false);
		// 调用服务端登陆接口
		RequestParams params = new RequestParams();
		params.addBodyParameter("userMobile", Consts.userMobile);
		params.addBodyParameter("password", oldpassword);
		params.addBodyParameter("newPassword", newpassword);
		params.addBodyParameter("tokenId", Consts.tokenID);
		
		HttpUtils http = new HttpUtils();
		CookieStore cookieStore = getCookieStore((DefaultHttpClient)http.getHttpClient());
		
		if(cookieStore!=null) {
			http.configCookieStore(cookieStore);
		}
		
		http.send(HttpRequest.HttpMethod.POST, Consts.SERVER_URL_MODIFY_PASSWORD, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LogUtil.log(TAG,"responseInfo.result:[" + responseInfo.result + "]");
				try {
					JSONObject jsonObject = new JSONObject(responseInfo.result);
					if ("0".equals(jsonObject.getString("status"))) {
						String errCode = jsonObject.getString("errorCode");
						String errMsg = PropertiesUtil.getProperties(getApplicationContext()).getProperty(errCode);
						EventHandler.showToast(PasswordEditActivity.this, errMsg);
						closeProgressDialog();
						if(isUserNeedLogin(errCode)) {
							skip2LoginActicity(PasswordEditActivity.this);
						}
						return;
					}
					if ("1".equals(jsonObject.getString("status"))) {
						closeProgressDialog();
						EventHandler.showToast(PasswordEditActivity.this,  "修改成功！");
						Intent intent = new Intent(mcontext,MainActivity.class);
						startActivity(intent);
						finish();
					}
					
					//setResult(RESULT_OK, intent);		
				} catch (Exception e) {
					LogUtil.logError(e);
					closeProgressDialog();
					EventHandler.showToast(PasswordEditActivity.this, "服务器忙，请稍后重试");

				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				LogUtil.logError(error);
				closeProgressDialog();
				EventHandler.showToast(PasswordEditActivity.this, "服务器忙，请稍后重试");

			}
		});
	}
	
	private void closeProgressDialog() {
		if (null != progressDialog) {
			progressDialog.dismiss();
		}
	}
	
	@Override
	public void onBackPressed() {
		//CommonUtil.openActivity(this, SettingsActivity.class);
		super.onBackPressed();
		finish();
	}
}
