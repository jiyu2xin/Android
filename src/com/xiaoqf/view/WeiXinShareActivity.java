package com.xiaoqf.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xiaoqf.util.LogUtil;
import com.xiaoqf.wxapi.ShareWeiXinDialog;
import com.xiaoqf.app.R;

/**
 * @ClassName: SettingsActivity
 * @Description: 设置 界面
 * @version: 1.0
 * @author: wangbin
 * @Create: 2015-06-07
 */
public class WeiXinShareActivity extends BaseActivity {

	// 标题栏
	@ViewInject(R.id.iv_title_left)
	private ImageView backImageView;

	@ViewInject(R.id.tv_title)
	private TextView titleTextView;

	@ViewInject(R.id.activity_room_select_house_ll)
	private LinearLayout myLinearLayout;
	
	private String orderID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_weixin_share);
		ViewUtils.inject(this);

		titleTextView.setText("小Q购房Q房价");
		init();
	}

	private void init() {
	
		Intent intent = getIntent(); 
		if(intent!=null)
			orderID = intent.getStringExtra("WeiXinShareKey");
		LogUtil.log(TAG, "orderID:"+orderID);
		//ImageView mImageView =new ImageView(this);
		//mImageView.setBackgroundResource(R.drawable.ic_share_weixin_bargain2);
		//LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//params.gravity = Gravity.CENTER;// 重心
		//mImageView.setLayoutParams(params);
		
		LinearLayout listItemLayout = new LinearLayout(this);
		String inflater = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater vi = (LayoutInflater) this.getSystemService(inflater);
		vi.inflate(R.layout.share_weixin_item, listItemLayout, true); 
		Button mButton = (Button) listItemLayout.findViewById(R.id.activity_share_weixin_item_button);
		mButton.setOnClickListener(mButtonClickListener);
		myLinearLayout.addView(listItemLayout);
		//myLinearLayout.addView(mImageView);
	}

	OnClickListener mButtonClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			ShareWeiXinDialog share = new ShareWeiXinDialog();
			share.init(WeiXinShareActivity.this, orderID);
			share.showShareWXDialog();	
		}
	};
	
	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}
	
	@OnClick(R.id.iv_title_left)
	public void onWeiXinShareActivityBackClick(View v) {
		finish();
		super.onBackPressed();
	}
}
