package com.xiaoqf.view;

import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xiaoqf.beans.User;
import com.xiaoqf.common.Consts;
import com.xiaoqf.service.RegisterCodeTimerService;
import com.xiaoqf.util.CommonUtil;
import com.xiaoqf.util.EventHandler;
import com.xiaoqf.util.LogUtil;
import com.xiaoqf.util.PropertiesUtil;
import com.xiaoqf.util.RegisterCodeTimer;
import com.xiaoqf.app.R;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @ClassName: RegisterActivity
 * @Description: 注册 界面
 * @version: 1.0
 * @author: wangbin
 * @Create: 2015-07-15
 */
public class LoginFreeRegisterActivity extends BaseActivity {
	
	@ViewInject(R.id.iv_title_left)
	private ImageView backImageView;
	
	@ViewInject(R.id.tv_title)
	private TextView titleTextView;
	
	@ViewInject(R.id.activity_loginfreeregister_item_et_num)
	private EditText registernumEditText;
	
	// 验证码填入框
	@ViewInject(R.id.activity_loginfreeregister_item_et_code)
	private EditText registerSecurityCode ;
	
	@ViewInject(R.id.activity_loginfreeregister_item_tv_code)
	private TextView getCodeTv;
	
	@ViewInject(R.id.activity_loginfreeregister_item_rl_confirm)
	private RelativeLayout registerButtonRelativeLayout;
	
	private final static String TAG ="LoginFreeRegisterActivity";
	private static boolean isSignButtonClick = false;
	
	public ProgressDialog progressDialog =null;
	
	private Intent timerService;
	private static final int CODE_GET = 1;

	protected static final int USER_REGISTER_OK = 101;
	private DefaultHttpClient dh;
	
	// 定义一个广播接收器
    private BroadcastReceiver smsReceiver;
	private IntentFilter smsFilter;
	// 验证码内容
	private String verificationCode;
	
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 正在倒计时
			case RegisterCodeTimer.IN_RUNNING:
				getCodeTv.setTextColor(getResources().getColor(R.color.nomalGray));
				getCodeTv.setText(msg.obj.toString());				
				break;

			// 完成倒计时
			case RegisterCodeTimer.END_RUNNING:
				getCodeTv.setEnabled(true);
				getCodeTv.setTextColor(getResources().getColor(R.color.black));
				getCodeTv.setText(msg.obj.toString());
				break;
				
			// 获取验证码
			case CODE_GET:
				registerSecurityCode.setText(verificationCode);
				break;
			
			//注册成功 保存cookie
			case USER_REGISTER_OK:
				//CookieStore  cookieStore = getCookieStore(dh);	
				if(cookieStore==null)
					createPreferencesCookieStore();
				
				CookieStore cs = dh.getCookieStore();
				cookieStore.clear();
				List<Cookie> cookies = cs.getCookies();
				for (int i = 0; i < cookies.size(); i++) {
					
					LogUtil.log(TAG,"name："+cookies.get(i).getName());
					LogUtil.log(TAG,"value："+cookies.get(i).getValue());
					cookieStore.addCookie(cookies.get(i));
					
					//设置过期时间
					//cookieStore.clearExpired(cookies.get(i).getExpiryDate());
				}
				
				LogUtil.log(TAG,"cookies"+cookieStore.getCookies()+"size:"+cookies.size());
				Intent intent = new Intent(LoginFreeRegisterActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
				break;
				
			default:
				break;
			}
		};
	};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_loginfreeregister);
		ViewUtils.inject(this);
		
		//获取本机号码
		String phoneNum = getNativePhoneNum();
		if(!"".equals(phoneNum))
			registernumEditText.setText(phoneNum);
		
		titleTextView.setText("登录");
		timerService = new Intent(this, RegisterCodeTimerService.class);
		RegisterCodeTimerService.setHandler(mHandler);

		smsFilter = new IntentFilter();// 创建意图过滤器
		smsFilter.addAction("android.provider.Telephony.SMS_RECEIVED");// 创建意图动作
		smsFilter.setPriority(Integer.MAX_VALUE);// 设置等级最大
		smsReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Object[] objs = (Object[]) intent.getExtras().get("pdus");// 获取收 到的消息
				for (Object obj : objs) {
					byte[] pdu = (byte[]) obj;
					SmsMessage sms = SmsMessage.createFromPdu(pdu);
					// 获取手机短信的内容
					String message = sms.getMessageBody();
					LogUtil.log(TAG, "message     " + message);
					// 获取手机号码
					String from = sms.getOriginatingAddress();
					EventHandler.showToast(getApplicationContext(), "from: " + from);
					LogUtil.log(TAG, "from     " + from);
					// 判断号码是否为空
					if (!TextUtils.isEmpty(from)) {
						String code = CommonUtil.getVerificationCode(message);
						if (!TextUtils.isEmpty(code)) {
							verificationCode = code;
							mHandler.sendEmptyMessage(CODE_GET);
						}
					}
				}
			}
		};
		// 注册广播监听器
		registerReceiver(smsReceiver, smsFilter);
	}

	
	@OnClick(R.id.activity_loginfreeregister_item_tv_code)
	//获取验证码按钮响应
	public void onGetRegisterCodeClick(View v) {
		//防止重复点击
		if(CommonUtil.isFastDoubleClick())
			return;
		String phoneNum = registernumEditText.getText().toString().trim();
		
		if("".equals(phoneNum)||!phoneNum.matches("^[1-9]\\d{10}$")) {
			EventHandler.showToast(this, "您输入的电话号码格式不正确或为空！");//^(13[0-9]|15[0|1|3|6|7|8|9]|18[0-9]|14[7])\\d{8}$
			return;
		}else{
			LogUtil.log(TAG, phoneNum);
		}

		progressDialog = EventHandler.showProgress(this, "加载中...",false,false);
		RequestParams params = new RequestParams();
		params.addBodyParameter("userMobile", phoneNum);
		params.addBodyParameter("operFlag", "LOGIN");
		
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
//						EventHandler.showToast(LoginFreeRegisterActivity.this, errCode);
						return;
					}
					
					if ("1".equals(jsonObject.getString("status"))) {
						closeProgressDialog();
						EventHandler.showToast(LoginFreeRegisterActivity.this, "验证码发送成功！");
						LogUtil.log(TAG, "验证码发送成功！");
						
						getCodeTv.setEnabled(false);
						getCodeTv.setTextColor(getResources().getColor(R.color.nomalGray));
						startService(timerService);
						//return;
					}
					//JSONObject datasJsonObject = new JSONObject(jsonObject.getString("value"));
					
				} catch (Exception e) {
					LogUtil.logError(e);
					closeProgressDialog();
					EventHandler.showToast(LoginFreeRegisterActivity.this, "服务器很忙，请稍后重试");
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				LogUtil.logError(error);
				closeProgressDialog();
				EventHandler.showToast(LoginFreeRegisterActivity.this, "服务器忙，请稍后重试");

			}
		});
	}
	
	@OnClick(R.id.activity_loginfreeregister_item_rl_confirm)
	//注册按钮响应
	public void onResgisterButtonClick(View v) {
		if(CommonUtil.isFastDoubleClick())
			return;
		
		if(isSignButtonClick) {
			//EventHandler.showToast(this, "请选择");
			return;
		}
		//电话号码
		final String phoneNum = registernumEditText.getText().toString();
		
		if("".equals(phoneNum)||!phoneNum.matches("^(13[0-9]|15[0|1|3|6|7|8|9]|18[0-9]|14[7])\\d{8}$")) {
			EventHandler.showToast(this, "您输入的电话号码格式不正确或为空！");
			return;
		}else{
			LogUtil.log(TAG, phoneNum);
		}
		
		//验证码    验证码为4~8位数字
		String securityCode = registerSecurityCode.getText().toString();

		if("".equals(phoneNum)||!securityCode.matches("^\\d{4,8}$")) {
			EventHandler.showToast(this, "您输入的验证码格式不正确或为空！");
			return;
		}else{
			LogUtil.log(TAG, securityCode);
		}
		
/*		//设置密码 密码以字母开头，允许字符、数字、下划线  5-8位
		String password = registerPassword.getText().toString();
		
		if("".equals(phoneNum)||!password.matches("^\\d{6}$")) {//^[a-zA-Z]\\w{5,8}$
			EventHandler.showToast(this, "您输入的密码格式不正确,请输入六位数字！");
			return;
		}else{
			LogUtil.log(TAG, password);
		}*/
		
		//推荐人电话号码
//		final String refPhoneNum = registerRefePhoneNum.getText().toString();
	
		
		progressDialog = ProgressDialog.show(this, "", "加载中...");
		RequestParams params = new RequestParams();
		params.addBodyParameter("userMobile", phoneNum);
//		params.addBodyParameter("password", password);
		params.addBodyParameter("mobileVerCode", securityCode);
		//如果推荐人不为空 提交
//		if(!"".equals(refPhoneNum))
//			params.addBodyParameter("recommendMobile", refPhoneNum);
		
		final HttpUtils https = new HttpUtils();
		https.send(HttpMethod.POST, Consts.SERVER_URL_USER_LOGIN, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				
				LogUtil.logError(error);
				LogUtil.log(TAG, msg);
				closeProgressDialog();
				EventHandler.showToast(LoginFreeRegisterActivity.this, "服务器忙，请稍后重试");
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LogUtil.log(TAG, "responseInfo.result:[" + responseInfo.result + "]");
				try {
					JSONObject jsonObject = new JSONObject(responseInfo.result);
					if("0".equals(jsonObject.getString("status"))) {
						closeProgressDialog();
						String errCode = jsonObject.getString("errorCode");
						String errMsg = PropertiesUtil.getProperties(getApplicationContext()).getProperty(errCode);
						EventHandler.showToast(LoginFreeRegisterActivity.this, errMsg);
						return;
					}
					if("1".equals(jsonObject.getString("status"))) {
						Consts.tokenID = jsonObject.getString("tokenId");
						closeProgressDialog();
						dh = (DefaultHttpClient) https.getHttpClient();
						mHandler.sendEmptyMessage(USER_REGISTER_OK);
						EventHandler.showToast(LoginFreeRegisterActivity.this, "恭喜，登录成功！");
						User user = User.getUser();
						user.setuId(Consts.tokenID);
						Consts.userMobile=phoneNum;
						user.setPhone(phoneNum);
						setUser(user);
					}
					//JSONObject jsonDatas = new JSONObject(jsonObject.getString("value"));
					//Consts.tokenID = jsonDatas.getString("tokenId");
					
					closeProgressDialog();
				} catch (JSONException e) {
					LogUtil.logError(e);
					closeProgressDialog();
					EventHandler.showToast(LoginFreeRegisterActivity.this, "服务器很忙，请稍后重试");
				}
				
			}
			
		});
	}
	
	@SuppressLint("NewApi") @OnClick(R.id.activity_loginfreeregister_item_iv_comfirm)
	public void onSignImageViewOnClick(View v) {
		if(isSignButtonClick) {
			isSignButtonClick = false;
			v.setBackground(getResources().getDrawable(R.drawable.sign_comfirm));
			registerButtonRelativeLayout.setPressed(false);
			registerButtonRelativeLayout.setClickable(true);
		}else {
			isSignButtonClick = true;
			v.setBackground(getResources().getDrawable(R.drawable.sign_discomfirm));
			registerButtonRelativeLayout.setPressed(true);
			registerButtonRelativeLayout.setClickable(false);
		}
	}
	
	@OnClick(R.id.activity_loginfreeregister_item_tv_comfirm)
	public void onSignInfoClick(View v) {
		CommonUtil.openActivity(this, AgreementActivity.class);
	}
	
	@OnClick(R.id.iv_title_left)
	public void onRegisterBackClick(View v) {
		//CommonUtil.openActivity(this, LoginActivity.class);
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
	
	@Override
	protected void onDestroy() {
		stopService(timerService);
		// 解除自动输入的广播
		unregisterReceiver(smsReceiver);
		super.onDestroy();
	}
	
	private String getNativePhoneNum() {
		//获取手机管理
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		//返回手机号码    获取手机号码和SIM有关 非100%成功
		String temp = tm.getLine1Number();
		String phoneNum =null;
		if((temp!=null)&&(temp.length()>=11))
			phoneNum = temp.substring(temp.length()-11);
		
		return phoneNum;
	}
}
