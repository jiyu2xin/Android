package com.xiaoqf.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
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
import com.xiaoqf.adapter.walletRecordListAdapter;
import com.xiaoqf.beans.walletRecordBean;
import com.xiaoqf.common.Consts;
import com.xiaoqf.util.CommonUtil;
import com.xiaoqf.util.EventHandler;
import com.xiaoqf.util.LogUtil;
import com.xiaoqf.util.PropertiesUtil;
import com.xiaoqf.app.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class WalletRecordActivity extends BaseActivity {

	public ProgressDialog progressDialog = null;
	private final static String TAG = "WalletRecordActivity";
	private final static int SHOW_NO_INFRO = 1;
	private String requestType =null;
	
	@ViewInject(R.id.iv_title_left)
	private ImageView imageView;

	@ViewInject(R.id.tv_title)
	private TextView titleTextView;

	@ViewInject(R.id.activity_wallet_record_list)
	private ListView myListView;

	@ViewInject(R.id.activity_mylikes_item_scrollView)
	private ScrollView myScrollView;

	@ViewInject(R.id.activity_mylikes_item_linearlayout)
	private LinearLayout myLinearLayout;
	
	@ViewInject(R.id.activity_no_wallet_item_linearlayout)
	private LinearLayout myTextLinearLayout; 
	
	@ViewInject(R.id.activity_no_wallet_item_linearlayout1)
	private LinearLayout myTextLinearLayout1;  
	
	
	@ViewInject(R.id.activty_no_wallet_image)
	private ImageView myNoWalletImageView; 
	
	@ViewInject(R.id.activty_no_wallet_text1) 
	private TextView textNoWallet;

	private walletRecordListAdapter walletRecordListAdapter;
	private List<walletRecordBean> adapterList = new ArrayList<walletRecordBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallet_record);
		ViewUtils.inject(this);
		init();
	}

	private void init() {
	
		Intent intent = this.getIntent();
		String tag =null;
		if(intent!=null)
			tag =(String) intent.getCharSequenceExtra("key");
		
		if("TAG_CASH".equals(tag)) {
			requestType = "0";	
			titleTextView.setText("现金记录");
		}else if("TAG_WALLET".equals(tag)) {
			requestType = "2";	
			titleTextView.setText("红包记录");
		}

		//titleTextView.setTextColor(0xff808080);
		
		Consts.showHightSize =0;
		adapterList.clear();
		// myListView.setOnItemClickListener(myItemOnClick);
		walletRecordListAdapter = new walletRecordListAdapter(this,
				R.layout.order_item, adapterList,requestType);
		walletRecordListAdapter.setNotifyOnChange(false);
		myListView.setDivider(null);
		// myListView.setFooterDividersEnabled(false);
		// myListView.setHeaderDividersEnabled(false);
		myListView.setAdapter(walletRecordListAdapter);
		//myListView.setOnScrollListener(myScrollListener);
		//myScrollView.setOnTouchListener(myScrollTouchListener);

		progressDialog = EventHandler.showProgress(this, "加载中...",false,false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				loadOrderListInfo();
			}
		}).start();
	}

	// 获取红包记录列表
	private void loadOrderListInfo() {
		// 获取红包的信息
		RequestParams params = new RequestParams();
		params.addBodyParameter("tokenId", Consts.tokenID);
		params.addBodyParameter("userMobile", Consts.userMobile);// 测试用户18673218888
		params.addBodyParameter("detailType", requestType);// 2 红包   0 现金
		params.addBodyParameter("searchDate", CommonUtil.getCurrentMonth());// 红包

		HttpUtils http = new HttpUtils();
		CookieStore cookieStore = getCookieStore((DefaultHttpClient)http.getHttpClient());
		
		if(cookieStore!=null) {
			http.configCookieStore(cookieStore);
		}
		http.send(HttpRequest.HttpMethod.POST,
				Consts.SERVER_URL_TRANSACTION_RECORD, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtil.log(TAG, "responseInfo.result:["
								+ responseInfo.result + "]");
						
						try {
							JSONObject jsonObject = new JSONObject(
									responseInfo.result);
							if ("0".equals(jsonObject.getString("status"))) {
								String errCode = jsonObject
										.getString("errorCode");
								String errMsg = PropertiesUtil.getProperties(
										getApplicationContext()).getProperty(
										errCode);
								closeProgressDialog();
								
								EventHandler.showToast(
										WalletRecordActivity.this, errMsg);
								
								if(isUserNeedLogin(errCode)) {
									skip2LoginActicity(WalletRecordActivity.this);
								}
								return;
							}
							adapterList.clear();
							walletRecordListAdapter.clear();
							Consts.showHightSize =0;
							getOrderDetailData(jsonObject.getString("detailNow"));
							getOrderDetailData(jsonObject.getString("detailBefore"));
							getOrderDetailData(jsonObject.getString("detailFeb"));
							

							closeProgressDialog();
							if (!adapterList.isEmpty()) {
								walletRecordListAdapter.notifyDataSetChanged();
							} else
								mHandler.sendEmptyMessage(SHOW_NO_INFRO);

						} catch (Exception e) {
							LogUtil.logError(e);
							closeProgressDialog();
							
							if (!adapterList.isEmpty()) {
								walletRecordListAdapter.notifyDataSetChanged();
							} else
								mHandler.sendEmptyMessage(SHOW_NO_INFRO);
							// mHandler.sendEmptyMessage(SHOW_NO_INFRO);
							// EventHandler.showToast(WalletRecordActivity.this,
							// "服务器忙，请稍后重试");
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						LogUtil.logError(error);
						closeProgressDialog();
						EventHandler.showToast(WalletRecordActivity.this,
								"服务器忙，请稍后重试");
					}
				});
	}

	@OnClick(R.id.iv_title_left)
	public void onQcallBackClick(View v) {
		super.onBackPressed();
	}

	private void getOrderDetailData(String copyFrom) {
		try {
			JSONObject jsonData = new JSONObject(copyFrom);// jsonObject.getString("detailNow")
			String year = jsonData.getString("year");

			String month = jsonData.getString("month");
			String size = jsonData.getString("size");
			Consts.showHightSize += Integer.parseInt(size)*210+65;
			
			JSONArray jsonArray = new JSONArray(
					jsonData.getString("detailList"));// 红包记录详情

			for (int i = 0; i < jsonArray.length(); i++) {
				// Estate estate = new Estate();
				walletRecordBean record = new walletRecordBean();

				// 交易金额
				record.setTransactionAmount(jsonArray.getJSONObject(i)
						.getString("detailAmt"));
				// 交易时间
				record.setTransactionCreateTime(jsonArray.getJSONObject(i)
						.getString("detailTime"));
				record.setTransactionYear(year);
				record.setTransactionMonth(month);
				record.setTransactionCounts(size);
				adapterList.add(record);
			}

		} catch (JSONException e) {
			// e.printStackTrace();
			closeProgressDialog();
			if (!adapterList.isEmpty()) {
				walletRecordListAdapter.notifyDataSetChanged();
			} else
				mHandler.sendEmptyMessage(SHOW_NO_INFRO);
		}
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_NO_INFRO:	
				myLinearLayout.setVisibility(View.VISIBLE);
				if("0".equals(requestType)) {
					myTextLinearLayout.setVisibility(View.VISIBLE);
					myNoWalletImageView.setImageResource(R.drawable.ic_no_cash);
					myTextLinearLayout1.setVisibility(View.INVISIBLE);
					textNoWallet.setText("还没有现金记录");
					
				}else if("2".equals(requestType)) {
					requestType = "2";
				}
				
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	@OnClick(R.id.activity_wallet_item_select)
	public void onSelectFoorButtonClick(View v) {
		Intent intent = new Intent(WalletRecordActivity.this,
				MainActivity.class);
		startActivity(intent);
		finish();
	}

	OnScrollListener myScrollListener = new AbsListView.OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView arg0, int arg1) {
			LogUtil.log(TAG, "lastVisible:");
		}
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			int lastVisible = myListView.getLastVisiblePosition();
			LogUtil.log(TAG, "lastVisible:" + lastVisible + "totalItemCount:"+ totalItemCount + "lastindex:" + firstVisibleItem);

		}
	};

	private int index = 0;  
	OnTouchListener myScrollTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				LogUtil.log(TAG,"down");
				break;
			case MotionEvent.ACTION_MOVE:
				index++;
				
				break;
			default:
				break;
			}
			if (event.getAction() == MotionEvent.ACTION_UP && index > 0) {
				index = 0;
				View view = ((ScrollView) v).getChildAt(0);
				if (view.getMeasuredHeight() <= v.getScrollY() + v.getHeight()) {
					// 加载数据代码
					LogUtil.log(TAG,"到底了！");
				}
			}
			return false;
		}
	};
	
	private void closeProgressDialog() {
		if (null != progressDialog) {
			progressDialog.dismiss();
		}
	}	
}
