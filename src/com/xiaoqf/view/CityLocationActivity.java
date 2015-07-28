package com.xiaoqf.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xiaoqf.common.Consts;
import com.xiaoqf.customview.CityLocationAdapter;
import com.xiaoqf.util.CommonUtil;
import com.xiaoqf.util.EventHandler;
import com.xiaoqf.util.LogUtil;
import com.xiaoqf.app.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class CityLocationActivity extends BaseActivity {
	public static final String tag = "CityLocationActivity";

	protected static final int SHOW_NO_NETWORK_ACTIVITY = 1;

	@ViewInject(R.id.tv_city)
	private TextView mCityName;
	@ViewInject(R.id.city_location_gv)
	private GridView mGridView;
	@ViewInject(R.id.activity_city_location_tv_current_city)
	private TextView currentCity;

	private CityLocationAdapter cityLocationAdapter;
	private List<String> cityNameArrys = new ArrayList<String>();
	private List<String> cityCodeArrys = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city_location);
		ViewUtils.inject(this);
		init();
	}

	private void init() {
		// initLocation();
		Intent intent = getIntent();
		String cityName = intent.getStringExtra("CITY_NAME");
		mCityName.setText(cityName);
		cityNameArrys.clear();
		currentCity.setText(cityName);

		cityLocationAdapter = new CityLocationAdapter(this, cityNameArrys);
		mGridView.setAdapter(cityLocationAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = getIntent();
				Consts.City_Name = cityNameArrys.get(position);
				intent.putExtra("CITY_NAME", Consts.City_Name);
				intent.putExtra("CITY_CODE", cityCodeArrys.get(position));
				Consts.City_SelectedPosition = position;
				setResult(RESULT_OK,intent);
				finish();
			}
		});
		reLoadOpenedCity();
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		super.onBackPressed();
		finish();
	}

	public void reLoadOpenedCity() {

		// 获取已开通城市
		final HttpUtils http = new HttpUtils();

		// http.configCurrentHttpCacheExpiry(1000*10);
		http.send(HttpRequest.HttpMethod.POST,
				Consts.SERVER_URL_LOCATE_OPENEDCITY,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtil.log(TAG, "responseInfo.result:["
								+ responseInfo.result + "]");
						try {
							JSONArray jsonArray = new JSONArray(
									responseInfo.result);// 获取楼盘信息数据
							cityNameArrys.clear();
							for (int i = 0; i < jsonArray.length(); i++) {

								cityNameArrys.add(jsonArray.getJSONObject(i)
										.getString("cityName"));
								cityCodeArrys.add(jsonArray.getJSONObject(i)
										.getString("mapCode"));
							}
							if (!cityNameArrys.isEmpty())
								cityLocationAdapter.notifyDataSetChanged();
							// myTextView.startScroll(null, 0, totalFreeAmount,
							// null);

						} catch (Exception e) {
							LogUtil.logError(e);
							EventHandler.showToast(CityLocationActivity.this,
									"服务器忙，请稍后重试");
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						LogUtil.logError(error);
						handler.sendEmptyMessageDelayed(
								SHOW_NO_NETWORK_ACTIVITY, 1000);
						// EventHandler.showToast(HomeActivity.this,
						// "服务器忙，请稍后重试");
					}
				});

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case SHOW_NO_NETWORK_ACTIVITY: {
				if (!CommonUtil.checkNetWork(CityLocationActivity.this)) {
					CommonUtil.setNetworkMethod(CityLocationActivity.this);
				} else {
					Intent intent = new Intent(CityLocationActivity.this,
							ShowNoNetWorkActivity.class);
					intent.putExtra("TITLE_FLAG", "请选择楼盘所在城市");
					startActivity(intent);
					finish();
				}

			}
				break;

			default:
				break;
			}
		}

	};
	
	@OnClick(R.id.activity_city_location_tv_current_city)
	public void onCurrentCityItemClick(View v) {
		setResult(RESULT_CANCELED);
		finish();
	}
}
