package com.xiaoqf.view;

import java.util.ArrayList;
import java.util.List;

import com.xiaoqf.customview.GuidePagerAdapter;
import com.xiaoqf.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * @ClassName: GuideView
 * @Description: 向导 界面
 * @version: 1.0
 * @author: wangbin
 * @Create: 2015-05-07
 */
public class GuideView extends BaseActivity implements OnPageChangeListener {

	private final static String TAG = "GuideView->";
	private ViewPager vp;
	private GuidePagerAdapter vpAdapter;
	private List<View> views;

	// 底部小点图片
	private int oldPosition = 0;// 记录上一次点的位置
	private ArrayList<View> dots = new ArrayList<View>();

	private LayoutInflater inflater;
	private LinearLayout dot_point;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide);
		
		// 初始化页面
		initViews();

		// 初始化底部小点
		initDots();
	}

	private void initViews() {
		try {
			inflater = LayoutInflater.from(this);

			views = new ArrayList<View>();
			// 初始化引导图片列表
			views.add(inflater.inflate(R.layout.guide_one, null));
			views.add(inflater.inflate(R.layout.guide_two, null));
			views.add(inflater.inflate(R.layout.guide_three, null));
			views.add(inflater.inflate(R.layout.guide_four, null));

			// 初始化Adapter
			vpAdapter = new GuidePagerAdapter(views, this);

			vp = (ViewPager) findViewById(R.id.guide_viewpager_layout);
			vp.setAdapter(vpAdapter);
			// 绑定回调
			vp.setOnPageChangeListener(this);
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}

	private void initDots() {
		dot_point = (LinearLayout) findViewById(R.id.guide_point_layout);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				22, 22);
		layoutParams.setMargins(12, 3, 12, 3);
		for (int i = 0; i < views.size(); i++) {
			ImageView iv_image = (ImageView) inflater.inflate(
					R.layout.activity_item_dots, null);
			iv_image.setLayoutParams(layoutParams);
			if (i == 0) {
				iv_image.setBackgroundResource(R.drawable.point_select);
			}
			iv_image.setTag(views.get(i));
			dot_point.addView(iv_image);
			dots.add(iv_image);
		}
	}

	// 当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	// 当前页面被滑动时调用
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	// 当新的页面被选中时调用
	@Override
	public void onPageSelected(int position) {
		// 设置底部小点选中状态״̬
		// setCurrentDot(arg0);
		dots.get(oldPosition).setBackgroundResource(R.drawable.point_normal);
		dots.get(position).setBackgroundResource(R.drawable.point_select);

		oldPosition = position;
		
		
		if (position==views.size()-1) {
			//dot_point.setVisibility(View.GONE);
		}else{
			dot_point.setVisibility(View.VISIBLE);
		}
	}

	public void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		System.gc();
		System.runFinalization();
	}
	
}
