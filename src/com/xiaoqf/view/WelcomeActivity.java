package com.xiaoqf.view;
import com.xiaoqf.beans.User;
import com.xiaoqf.common.Consts;
import com.xiaoqf.common.XiaoQApplication;
import com.xiaoqf.util.CommonUtil;
import com.xiaoqf.app.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

public class WelcomeActivity extends BaseActivity {

	private String versionName = "";
	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;

	private static final String SHAREDPREFERENCES_NAME = "first_pref";

	private static final int SPLASH_TIME = 3 * 1000;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		init();
	}

	private void init() {
		// 调试开发模式，直接跳转到指定界面
		if (Consts.isDebugMode) {
			switch (Consts.DEBUGMODE) {
			case Consts.DEBUGMODE_WB:
				openActivity(LoginFreeRegisterActivity.class);
				return;

			case Consts.DEBUGMODE_TD:
//				openActivity(RegisterActivity.class);
//				openActivity(LoginActivity.class);
//				openActivity(TestActivity.class);
//				openActivity(RoomSelectActivity.class);
//				openActivity(BookPayedActivity.class);
				openActivity(BargainNewActivity.class);
//				openActivity(PayActivity.class);
//				openActivity(AgreementActivity.class);
//				openActivity(SettingsActivity.class);
//				openActivity(RegisterActivity.class);
				finish();
				return;

			default:
				break;
			}
		}
		versionName = CommonUtil.getAppVersionName(this);
		
		// 读取SharedPreferences中需要的数据
		// 使用SharedPreferences来记录程序的使用次数
		SharedPreferences preferences = getSharedPreferences(
				SHAREDPREFERENCES_NAME, MODE_PRIVATE);

		// 取得版本号的值，如果没有该值，说明还未写入，用1作为默认值ֵ
		int oldVersionCode = preferences.getInt("versionCode", 1);

		//判断上次是否已登录
		User user =getUser();
		if(user!=null) {
			Consts.tokenID = user.getuId();
			Consts.userMobile = user.getPhone();
		}
		
		// 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
		if (XiaoQApplication.versionCode == oldVersionCode) {
			// 使用Handler的postDelayed方法，2秒后执行跳转到MainActivity
			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_TIME);
		} else {
			mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_TIME);
		}
	}


	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {	
			switch (msg.what) {
			case GO_HOME:
				goMain();
				break;
			case GO_GUIDE:
				goMain();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};



	private void goMain() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private void goGuide() {
		Intent intent = new Intent(this, GuideView.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 从欢迎界面跳转到指定界面(开发调试时，跳转到指定界面)
	 * 
	 * @param dstActivityClz
	 *            要打开的界面
	 */
	private void openActivity(Class<? extends Activity> dstActivityClz) {
		CommonUtil.openActivity(this, dstActivityClz);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
