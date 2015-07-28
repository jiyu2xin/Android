package com.xiaoqf.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
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
import com.xiaoqf.adapter.RoomOrderListAdapter;
import com.xiaoqf.beans.User;
import com.xiaoqf.beans.myLikeRoomOrderBean;
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
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @ClassName: MyLikesActivity
 * @Description: 我看中的房 页面
 * @version: 1.0
 * @author: wangbin
 * @Create: 2015-05-07
 */
public class MyLikesActivity extends BaseActivity {
	private Boolean isTabActivity =false; 
	public ProgressDialog progressDialog = null;
	private final static String TAG = "MYLIKES_ACTIVITY";
	private final static int SHOW_NO_INFRO = 1;
	private final static int SHOW_ROOM_INFRO = 2;
	protected static final int SHOW_NO_NETWORK_ACTIVITY = 404;
	
	@ViewInject(R.id.iv_title_left)
	private ImageView imageView;
	
	@ViewInject(R.id.tv_title)
	private TextView titleTextView;
	
	@ViewInject(R.id.list)
	private ListView myListView;
	
	private RoomOrderListAdapter roomListAdapter;
	private List<myLikeRoomOrderBean> adapterList = new ArrayList<myLikeRoomOrderBean>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mylikes);
		ViewUtils.inject(this);
		init();
	}
	
	private void init() {
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null)
			isTabActivity = bundle.getBoolean(TAG, false);

		if(!isTabActivity)
			imageView.setVisibility(View.INVISIBLE);
		else
			imageView.setVisibility(View.VISIBLE);
		titleTextView.setText("看中的房");
		//titleTextView.setTextColor(0xff808080);
		
		myListView.setDivider(null);
		myListView.setOnItemClickListener(myItemOnClick);
		roomListAdapter = new RoomOrderListAdapter(this,R.layout.room_item_mylikes,adapterList);
		myListView.setAdapter(roomListAdapter);
	}
	
	@OnClick(R.id.iv_title_left)
	public void onQcallBackClick(View v) {
		super.onBackPressed();
		finish();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		progressDialog = EventHandler.showProgress(this, "加载中...",false,false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				loadOrderListInfo();
			}
		}).start();
	}

	//获取订单列表
	private void loadOrderListInfo() {
		//获取楼盘的信息
				RequestParams params = new RequestParams();
				params.addBodyParameter("tokenId", Consts.tokenID);
				params.addBodyParameter("userMobile", Consts.userMobile);//测试用户18673218888
				//params.addBodyParameter("deviceType", "android");
				//params.addBodyParameter("rateType", "3");
				LogUtil.log(TAG, "tokenId:"+Consts.tokenID+"userMobile:"+Consts.userMobile);
				HttpUtils http = new HttpUtils();
				CookieStore cookieStore = getCookieStore((DefaultHttpClient)http.getHttpClient());
				
				if(cookieStore!=null) {
					http.configCookieStore(cookieStore);
				}
				
				http.send(HttpRequest.HttpMethod.POST, Consts.SERVER_URL_ROOM_ORDER_INFO,params, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtil.log(TAG,"responseInfo.result:[" + responseInfo.result + "]");
						try {
							JSONObject jsonObject = new JSONObject(responseInfo.result);
							if ("0".equals(jsonObject.getString("status"))) {
								String errCode = jsonObject.getString("errorCode");
								String errMsg = PropertiesUtil.getProperties(getApplicationContext()).getProperty(errCode);
								closeProgressDialog();
								EventHandler.showToast(MyLikesActivity.this, errMsg);
								if(isUserNeedLogin(errCode)) {
									skip2LoginActicity(MyLikesActivity.this);
								}
								return;
							}
							
							JSONArray jsonArray = new JSONArray(
									jsonObject.getString("orderList"));// 获取楼盘信息数据
								
							adapterList.clear();
							for(int i=0;i<jsonArray.length();i++) {
								//Estate estate = new Estate();
								myLikeRoomOrderBean roomOrder = new myLikeRoomOrderBean();

								 //设置开发商ID
								/*
								estate.setEstateId(jsonArray
										.getJSONObject(i)
										.getString("estateId"));*/
								//设置楼盘名称
								roomOrder.setProjectName(jsonArray
										.getJSONObject(i)
										.getString("projectName"));
	
								//创建时间
								roomOrder.setTaskCreateTime(jsonArray
										.getJSONObject(i)
										.getString("createTime"));
								//完成进度
								roomOrder.setTaskComplementPercent(jsonArray
										.getJSONObject(i)
										.getString("orderComStep"));
								//最高优惠金额
								roomOrder.setOrderUnAmount(jsonArray
										.getJSONObject(i)
										.getString("maxDiscountAmount"));
								//图片URL
								roomOrder.setLogoImageUrl(jsonArray
										.getJSONObject(i)
										.getString("projectIcon"));
								//订单id
								roomOrder.setOrderID(jsonArray
										.getJSONObject(i)
										.getString("orderId"));
								//订单状态
								roomOrder.setOrderStatus(jsonArray
										.getJSONObject(i)
										.getString("orderStatus"));
								//原价
								roomOrder.setOriginalPrice(jsonArray
										.getJSONObject(i)
										.getString("orderOriginalAmount"));	
								//小Q价
								roomOrder.setOrderMaxFreeAmount(jsonArray
										.getJSONObject(i)
										.getString("orderAmount"));	
								//订单流程
								roomOrder.setOrderHandleProcess(jsonArray
										.getJSONObject(i)
										.getString("orderComStep"));
/*								//订单有效天数
								roomOrder.setOrderValidDays(jsonArray
										.getJSONObject(i)
										.getString("orderSurplusTime"));*/
			
								adapterList.add(roomOrder);
							}
							if(!adapterList.isEmpty()) {
								//mHandler.sendEmptyMessage(SHOW_ROOM_INFRO);
								roomListAdapter.notifyDataSetChanged();	
							}
							closeProgressDialog();
							
						} catch (Exception e) {
							LogUtil.logError(e);
							closeProgressDialog();
							mHandler.sendEmptyMessage(SHOW_NO_INFRO);
							//EventHandler.showToast(MyLikesActivity.this, "服务器忙，请稍后重试");
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						LogUtil.logError(error);
						closeProgressDialog();
						//EventHandler.showToast(MyLikesActivity.this, "服务器忙，请稍后重试");
						mHandler.sendEmptyMessageDelayed(SHOW_NO_NETWORK_ACTIVITY, 1000);
					}
				});		
	}
	
	//响应item点击事件
	OnItemClickListener myItemOnClick = new AdapterView.OnItemClickListener() {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			Bundle bundle = new Bundle();
			bundle.putSerializable("MyLikeRoomOrder", adapterList.get(position));
			handleOrderByStatus(adapterList.get(position).getOrderStatus(),
					adapterList.get(position).getOrderID(),bundle);
		}
	};
	
	private void handleOrderByStatus(String status,String id,Bundle bundle) {
		switch(Integer.parseInt(status)) {
			case 0 :
			case 1 :
			case 9 : 
			{
				Intent intent = new Intent(MyLikesActivity.this,BargainNewActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
				//finish();
			}break;
			case -1:
			{
				EventHandler.showToast(MyLikesActivity.this, "订单已作废");
			}break;
			default :
				break;
		}
	}
	
	@OnClick(R.id.activity_mylikes_item_floor_select)
	public void onSelectFoorButtonClick(View v) {
		Intent intent = new Intent(MyLikesActivity.this,MainActivity.class);
		startActivity(intent);
		finish();
	}
	
	private void skip2LoginAcitivty() {
		Intent intent = new Intent(MyLikesActivity.this,LoginFreeRegisterActivity.class);
		intent.putExtra(TAG, "1");
		startActivity(intent);
		finish();
	}
	
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {	
			switch (msg.what) {
			case SHOW_NO_INFRO:
				//myLinearLayout.setVisibility(View.VISIBLE);
	
				LinearLayout listItemLayout = new LinearLayout(MyLikesActivity.this);
				LayoutInflater vi = (LayoutInflater) MyLikesActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				vi.inflate(R.layout.activity_mylikes_none, listItemLayout, true);
				TextView myTextView = (TextView) listItemLayout.findViewById(R.id.activity_mylikes_item_floor_select);
				TextView titleTextView = (TextView) listItemLayout.findViewById(R.id.tv_title);
				titleTextView.setText("看中的房");
				//titleTextView.setTextColor(0xff808080);
				
				myTextView.setOnClickListener(myTextListener);
				setContentView(listItemLayout);
				break;
			case SHOW_ROOM_INFRO:
				
				break;
			case SHOW_NO_NETWORK_ACTIVITY:
            {
            	Intent intent = new Intent(MyLikesActivity.this,ShowNoNetWorkActivity.class);
            	intent.putExtra("TITLE_FLAG","小Q购房");
            	startActivity(intent);
            	finish();
            }break;
            
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	OnClickListener myTextListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			CommonUtil.openActivity(MyLikesActivity.this, MainActivity.class);
		}
	};
	
	private void closeProgressDialog() {
		if (null != progressDialog) {
			progressDialog.dismiss();
		}
	}
}
