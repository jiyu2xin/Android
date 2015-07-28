package com.xiaoqf.view;


import com.xiaoqf.util.CommonUtil;
import com.xiaoqf.app.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * @ClassName: MainActivity
 * @Description: 主界面
 * @version: 1.0
 * @author: wangbin
 * @Create: 2015-05-07
 */
@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

	private final static int TAB1 = 1;
	private final static int TAB2 = 2;
	private final static int TAB3 = 3;
	private final static int TAB4 = 4;

	private static final String TAG = "MainActivity";

	private TabHost tabHost;
	public static String serverVersionName = "";
	private RadioButton rb0, rb1, rb2, rb3;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initTab();

	}

	private void initTab() {
		
		//检查系统版本是否需要更新
		CommonUtil.getServerVersionName(this, TAG);
		
		tabHost = this.getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this, HomeActivity.class);
		spec = tabHost.newTabSpec(TAB1 + "").setIndicator(TAB1 + "")
				.setContent(intent);
		tabHost.addTab(spec);

		//添加Tabhost
		Bundle data = new Bundle();
		data.putString("enterType", "tabhost");
		intent = new Intent();
		intent.putExtras(data);
		intent.setClass(this, MyLikesActivity.class);
		spec = tabHost.newTabSpec(TAB2 + "").setIndicator(TAB2 + "")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, QCallActivity.class);
		spec = tabHost.newTabSpec(TAB3 + "").setIndicator(TAB3 + "")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, MineActivity.class);
		spec = tabHost.newTabSpec(TAB4 + "").setIndicator(TAB4 + "")
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0); // 设置当前页

		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.main_tab_group);
		rb0 = (RadioButton) findViewById(R.id.main_tab_home);
		rb1 = (RadioButton) findViewById(R.id.main_tab_like);
		rb2 = (RadioButton) findViewById(R.id.main_tab_call);
		rb3 = (RadioButton) findViewById(R.id.main_tab_mine);


		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.main_tab_home://主页面
					tabHost.setCurrentTabByTag(TAB1 + "");

					rb0.setTextColor(0xffff5a00);
					rb1.setTextColor(0xff838B83);
					rb2.setTextColor(0xff838B83);
					rb3.setTextColor(0xff838B83);
					break;
				case R.id.main_tab_like:// 看中的房
					tabHost.setCurrentTabByTag(TAB2 + "");
					rb1.setTextColor(0xffff5a00);
					rb0.setTextColor(0xff838B83);
					rb2.setTextColor(0xff838B83);
					rb3.setTextColor(0xff838B83);
					break;
				case R.id.main_tab_call:// 小Q电话
					tabHost.setCurrentTabByTag(TAB3 + "");
					rb2.setTextColor(0xffff5a00);
					rb1.setTextColor(0xff838B83);
					rb0.setTextColor(0xff838B83);
					rb3.setTextColor(0xff838B83);
					break;
				case R.id.main_tab_mine://我的
					tabHost.setCurrentTabByTag(TAB4 + "");
					rb3.setTextColor(0xffff5a00);
					rb1.setTextColor(0xff838B83);
					rb2.setTextColor(0xff838B83);
					rb0.setTextColor(0xff838B83);
					break;
				default:
					break;
				}
			}
		});

	}



}

