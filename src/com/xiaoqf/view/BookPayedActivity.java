package com.xiaoqf.view;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xiaoqf.beans.Discount;
import com.xiaoqf.beans.House;
import com.xiaoqf.beans.Order;
import com.xiaoqf.beans.OrderInfo;
import com.xiaoqf.beans.Project;
import com.xiaoqf.beans.Room;
import com.xiaoqf.common.Consts;
import com.xiaoqf.customview.ContactDialog;
import com.xiaoqf.util.CommonUtil;
import com.xiaoqf.util.EventHandler;
import com.xiaoqf.wxapi.ShareWeiXinDialog;
import com.xiaoqf.app.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @ClassName: BookPayedActivity
 * @Description: 订金支付完成 界面
 * @version: 1.0
 * @author: tongdu
 * @Create: 2015-06-12
 */
public class BookPayedActivity extends BaseActivity {
	private static OrderInfo order;
//	private static Project project;
//	private static House house;
//	private static Room room;
	
	// 标题栏
	@ViewInject(R.id.iv_title_left)
	private ImageView backImageView;
	
	@ViewInject(R.id.tv_title)
	private TextView titleTextView;
	
	// 支付金额
	@ViewInject(R.id.activity_book_payed_tv_amount)
	private TextView bookAmount;
	
	// 订单号
	@ViewInject(R.id.activity_book_payed_tv_order_no)
	private TextView bookOrderNo;
	
//	// 创建日期
//	@ViewInject(R.id.activity_book_payed_tv_order_time)
//	private TextView bookOrderTime;
	
	// 微信Q价
	@ViewInject(R.id.activity_book_payed_button_wechat)
	private LinearLayout wechatPrice;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_book_payed);
		ViewUtils.inject(this);
		
		titleTextView.setText("订金支付完成");
		Intent intent = getIntent();
		if (null != intent.getExtras()) {
			Bundle data = intent.getExtras();
			if (null != data.getSerializable("Order")) {
				order = (OrderInfo)intent.getSerializableExtra("Order");
				if (!TextUtils.isEmpty(order.getOrderCode())) {
					bookOrderNo.setText(order.getOrderCode());
				}
//				if (!TextUtils.isEmpty(order.getCreateTime())) {
//					bookOrderTime.setText(order.getCreateTime());
//				}
			}
//			if (null != data.getSerializable("Project")) {
//				project = (Project) data.getSerializable("Project");
//			}
//			if (null != data.getSerializable("Room")) {
//				room = (Room) data.getSerializable("Room");
//			}
//			if (null != data.getSerializable("House")) {
//				house = (House) data.getSerializable("House");
//			}
		}
		
		bookAmount.setText(Consts.PRICE_CENT);
	}
	
	@OnClick(R.id.iv_title_left)
	public void onClickBack(View v) {
		onBackPressed();
	}
	
	@Override
	public void onBackPressed() {
//		Intent intent = new Intent(this, BargainActivity.class);
//		Bundle bundle = new Bundle();
//		bundle.putSerializable("BookedOrder", order);
//		bundle.putSerializable("Project", project);
//		bundle.putSerializable("House", house);
//		bundle.putSerializable("Room", room);
//		intent.putExtras(bundle);
//		startActivity(intent);
//		CommonUtil.openActivity(this, BargainActivity.class);
		finish();
	}
	
	// 邀请微信好友帮我Q价
	@OnClick(R.id.activity_book_payed_button_wechat)
	public void onWechatPrice(View v) {
		ShareWeiXinDialog share = new ShareWeiXinDialog();
		share.init(this, order.getOrderId());
		share.showShareWXDialog();	
	}
}
