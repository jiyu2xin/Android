package com.xiaoqf.view;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import com.lidroid.xutils.util.PreferencesCookieStore;
import com.xiaoqf.beans.User;
import com.xiaoqf.util.CommonUtil;
import com.xiaoqf.util.LogUtil;
import com.xiaoqf.util.ViewManagerUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

/**
 * @ClassName: BaseActivity
 * @Description: 用于封装通用的共性, 其他activity继承于此类
 * @version: 1.0
 * @Create: 2015-6-4上午9:57:45
 * @tag
 */
public class BaseActivity extends Activity {
	private ViewManagerUtil manager = ViewManagerUtil.getInstance();
	
	protected final String TAG = getClass().getSimpleName();
	//public ProgressDialog progressDialog = null;

	/**
	 * @Title: getUser
	 * @Description: 获取登录User实体
	 * @return
	 * @author 
	 * @date 2015-6-4上午9:57:45
	 */
	public User getUser() {
		SharedPreferences preferences = getSharedPreferences("USER",
				Activity.MODE_PRIVATE);
		if (!"".equals(preferences.getString("uid", ""))) {
			User user = User.getUser();
			user.setuId(preferences.getString("uid", ""));
			//user.setUserName(preferences.getString("username", ""));
			//user.setsId(preferences.getString("sid", ""));
			//user.setAvatar(preferences.getString("avatar", ""));
			user.setPhone(preferences.getString("phone", ""));
			return user;
		}
		return null;
	}

	/**
	 * @Title: setUser
	 * @Description: 设置缓存中USER实体
	 * @param user
	 * @author 
	 * @date 2015-6-4上午9:57:45
	 */
	public void setUser(User user) {
		/** start 缓存系统登录用户信息 **********************************/
		Editor editor = getSharedPreferences("USER", Activity.MODE_PRIVATE)
				.edit(); // 获取编辑器
		editor.putString("uid", user.getuId());
		//editor.putString("username", user.getUserName());
		// editor.putString("sid", DesUtil.encode(user.getsId())); // 使用DES加密SID
		//DES des = new DES();
		//editor.putString("sid", des.encrypt(user.getsId())); // 使用DES加密SID
		editor.putString("phone", user.getPhone());
		//editor.putString("avatar", user.getAvatar());
		editor.commit();// 提交修改
		/** end 缓存系统登录用户信息 **********************************/

		/** start 缓存最后一次登录用户的登录名 **********************************/
		//getSharedPreferences("LAST_USERNAME", Activity.MODE_PRIVATE).edit()
		//		.putString("last_username", user.getUserName()).commit();
		/** end 缓存最后一次登录用户的登录名 **********************************/
	}
	
	public void setAvatar(User user) {
		// 获取编辑器
		Editor editor = getSharedPreferences("USER", Activity.MODE_PRIVATE).edit();
		editor.putString("avatar", user.getAvatar());
		editor.commit();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		manager.pushActivity(this);
	}

	protected void onResume() {
		super.onResume();
		//MobclickAgent.onResume(this);
	}

	protected void onPause() {
		super.onPause();
		//MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		manager.popActivity(this);
	}

	public void exitAll() {
		manager.exit();
	}
	
	//添加cookie
	 public static PreferencesCookieStore cookieStore=null;
	 
	 public void createPreferencesCookieStore() {
		 if(cookieStore==null) {
			 cookieStore =new PreferencesCookieStore(this.getApplicationContext());
		 }
	 }
	 
	 public CookieStore getCookieStore(DefaultHttpClient defaultHttpClient) {
		 if(cookieStore==null) {
			 cookieStore =new PreferencesCookieStore(this.getApplicationContext());
		 }
		 CookieStore cs = defaultHttpClient.getCookieStore();
		 cs.clear();
		 List<Cookie> cookies = ((CookieStore)cookieStore).getCookies();
		 for(Cookie cookie : cookies)
			 cs.addCookie(cookie);
		 
		 return cs;
	 }
	 
	 public boolean isUserNeedLogin(String errcode) {
		 if("N404".equals(errcode)) {
			 return true;
		 }
		 return false;
	 }
	 
	 public void skip2LoginActicity(Activity activity) {
		 
		 LogUtil.log(TAG, "Acticity:"+activity.getClass().getName());
		 String activityName = activity.getClass().getName();
		 if(activityName.equals("com.xiaoqf.app.MyLikesActivity")) {
			Intent intent = new Intent(activity,LoginFreeRegisterActivity.class);
			intent.putExtra("MYLIKES_ACTIVITY", "1");
			startActivity(intent);
			finish();
		 }
		 else {
			 CommonUtil.openActivity(activity, LoginFreeRegisterActivity.class);
			 finish();
		 }
	 }
}
