package com.xiaoqf.view;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.xiaoqf.adapter.ImagePaperAdapter;
import com.xiaoqf.beans.Project;
import com.xiaoqf.common.Consts;
import com.xiaoqf.customview.FloorAdapter;
import com.xiaoqf.util.CommonUtil;
import com.xiaoqf.util.EventHandler;
import com.xiaoqf.util.LogUtil;
import com.xiaoqf.util.PropertiesUtil;
import com.xiaoqf.app.R;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

/**
 * @ClassName: HomeActivity
 * @Description: 首界面
 * @version: 1.0
 * @author: wangbin
 * @Create: 2015-05-07
 */
public class HomeActivity extends BaseActivity {
	private GridView gridView;
	//private MagicTextView myTextView;
	private Context context;
	public ProgressDialog progressDialog = null;
	private final static String TAG = "HomeActivity";
	protected static final int SCHEDULE_EXCECUTOR_FLAG = 100;
	protected static final int SHOW_NO_NETWORK_ACTIVITY = 404;
	protected static final int REQUEST_OK = 1001;
	
	private double totalFreeAmount = 0;
	private TextView mCityName;
	private FloorAdapter adapter;
	private List<Project> adapterList = new ArrayList<Project>();// 适配器List
	private boolean isUserNeedLogin = false;
	private Thread mThread;

/**  广告位相关start ***/
	private LayoutInflater inflater;
	
	private ViewPager mviewPager;
	
	/**用于小圆点图片*/
	private List<ImageView> dotViewList;
	
	/**用于存放轮播效果图片*/
	private List<ImageView> list;
	
	LinearLayout dotLayout;
	
	private int currentItem  = 0;//当前页面	
	boolean isAutoPlay = true;//是否自动轮播
	private ScheduledExecutorService scheduledExecutorService;
/**   广告位相关 end **/
	
	/*** 定位相关 ***/
	private LocationClient mLocationClient;
	/** 默认长沙 */
	private String cityCode = "158";
	public BDLocationListener myListener = new cityLocationListener();
	
	private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            switch(msg.what) {
            case SCHEDULE_EXCECUTOR_FLAG:
            	mviewPager.setCurrentItem(currentItem); 
            	break;
            case SHOW_NO_NETWORK_ACTIVITY:
            {
            	if(!CommonUtil.checkNetWork(HomeActivity.this)) {
            		CommonUtil.setNetworkMethod(HomeActivity.this);
            	}else {
                	Intent intent = new Intent(HomeActivity.this,ShowNoNetWorkActivity.class);
                	intent.putExtra("TITLE_FLAG","小Q购房");
                	startActivity(intent);
                	finish();
            	}

            }break;
            
            default:
            	break;
            }
        }
        
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		progressDialog = EventHandler.showProgress(HomeActivity.this, "加载中...",false,true);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		initView();
		context = this;
		initPageView();
	}



	private void initView() {
		//开启定位
		if (!Consts.isDebugMode) {
			initLocation();
		}
		
		mCityName = (TextView) findViewById(R.id.tv_city);
		mCityName.setOnClickListener(myCityLocationListener);
		gridView = (GridView) findViewById(R.id.floorview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new FloorAdapter(this, R.layout.floor_item, adapterList);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				LogUtil.log(TAG, adapterList.get(position).getName());
				if(isUserNeedLogin) {
					skip2LoginActicity(HomeActivity.this);
					isUserNeedLogin = false;
				}else {
					Intent intent = new Intent(context,RoomSelectActivity.class);
					//Intent intent = new Intent(context,RoomSelectWebViewActivity.class);//WebView实现
					Bundle bundle = new Bundle();
					bundle.putSerializable("Project", adapterList.get(position));
					intent.putExtras(bundle);
					startActivity(intent);
					//finish();
				}
			}
		});
		//myTextView =(MagicTextView) findViewById(R.id.infor2);
		if (Consts.isDebugMode) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					loadPropertyInfo();
				}
			}).start();
			mCityName.setText(Consts.City_Name);
		} else {
			mThread =new Thread(new Runnable() {
				@Override
				public void run() {
					loadPropertyInfo();
				}
			});
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	//	myTextView.startScroll(null, 0, 2345, null);
	}

	@Override
	protected void onResume() {
		super.onResume();
		//myTextView.startScroll(null, 0, totalFreeAmount, null);
	}

	private void loadPropertyInfo() {
		
		String version =null;
		try {
			version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//获取楼盘的信息
		RequestParams params = new RequestParams();
		params.addBodyParameter("deviceType", "android");
		params.addBodyParameter("rateType", "3");
		params.addBodyParameter("citiyCode",cityCode);
		//params.addBodyParameter("Cookie", "00000");
		
		final HttpUtils http = new HttpUtils();
		
		CookieStore cookieStore = getCookieStore((DefaultHttpClient)http.getHttpClient());
		
		if(cookieStore!=null) {
			http.configCookieStore(cookieStore);
		}
			
		//http.configCurrentHttpCacheExpiry(1000*10);
		http.send(HttpRequest.HttpMethod.POST, Consts.SERVER_URL_PROPERTY_INFO,params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LogUtil.log(TAG,"responseInfo.result:[" + responseInfo.result + "]");
				try {
					JSONObject jsonObject = new JSONObject(responseInfo.result);
					if ("0".equals(jsonObject.getString("status"))) {
						String errCode = jsonObject.getString("errorCode");
						String errMsg = PropertiesUtil.getProperties(getApplicationContext()).getProperty(errCode);
						closeProgressDialog();
						EventHandler.showToast(HomeActivity.this, errMsg);
						if(isUserNeedLogin(errCode)) {
							isUserNeedLogin = true;
						}
						return;
					}
					totalFreeAmount = Double.parseDouble( jsonObject.getString("sumCouponAmount"));
					
					JSONArray jsonArray = new JSONArray(
							jsonObject.getString("projectList"));// 获取楼盘信息数据
					adapterList.clear();
					for(int i=0;i<jsonArray.length();i++) {
						//Estate estate = new Estate();
						Project project = new Project();
						//设置楼盘ID  jsonArray.length()
						project.setId(jsonArray
								.getJSONObject(i)
								.getString("projectId"));
						 //设置开发商ID
						/*
						estate.setEstateId(jsonArray
								.getJSONObject(i)
								.getString("estateId"));*/
						//设置楼盘名称
						project.setName(jsonArray
								.getJSONObject(i)
								.getString("projectName"));
						//图片URL
						project.setImgUrl(jsonArray
								.getJSONObject(i)
								.getString("projectIcon"));
	
						adapterList.add(project);
					}
					if(!adapterList.isEmpty())
						adapter.notifyDataSetChanged();
					closeProgressDialog();
					//myTextView.startScroll(null, 0, totalFreeAmount, null);
					
				} catch (Exception e) {
					LogUtil.logError(e);
					closeProgressDialog();
					EventHandler.showToast(HomeActivity.this, "服务器忙，请稍后重试");
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				LogUtil.logError(error);
				closeProgressDialog();
				handler.sendEmptyMessageDelayed(SHOW_NO_NETWORK_ACTIVITY, 1000);
				//EventHandler.showToast(HomeActivity.this, "服务器忙，请稍后重试");
			}
		});
	}
	/** 广告位相关*/
	@SuppressLint("NewApi") private void initPageView() {
		inflater = LayoutInflater.from(HomeActivity.this);
		mviewPager = (ViewPager) findViewById(R.id.myviewPager);
		dotLayout = (LinearLayout)findViewById(R.id.dotLayout);
		dotLayout.removeAllViews();
		dotViewList = new ArrayList<ImageView>();
    	list = new ArrayList<ImageView>();
    	
		
		for (int i = 0; i < 4; i++) {
			ImageView dotView = new ImageView(HomeActivity.this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			
        	params.leftMargin = 15;//设置小圆点的外边距
			params.rightMargin = 15;
			
			params.height = 20;//设置小圆点的大小
			params.width = 20;
			
			if(i == 0){
				//dotView.setBackgroundResource(R.drawable.point_pressed);
			}else{
				
				//dotView.setBackgroundResource(R.drawable.point_unpressed);
			}
			dotLayout.addView(dotView, params);
			
			dotViewList.add(dotView);
			//上面是动态添加了四个小圆点
		}
		
		ImageView img1 = (ImageView) inflater.inflate(R.layout.scroll_vew_item, null);
		ImageView img2 = (ImageView) inflater.inflate(R.layout.scroll_vew_item, null);
		ImageView img3 = (ImageView) inflater.inflate(R.layout.scroll_vew_item, null);
		ImageView img4 = (ImageView) inflater.inflate(R.layout.scroll_vew_item, null);

		img1.setBackgroundResource(R.drawable.ic_home_activty_bg);
		img2.setBackgroundResource(R.drawable.ic_home_activty_bg);
		img3.setBackgroundResource(R.drawable.ic_home_activty_bg);
		img4.setBackgroundResource(R.drawable.ic_home_activty_bg);
		
		list.add(img1);
		list.add(img2);
		list.add(img3);
		list.add(img4);
		
		ImagePaperAdapter adapter = new ImagePaperAdapter((ArrayList)list);
		
		mviewPager.setAdapter(adapter);
		mviewPager.setCurrentItem(0);
		mviewPager.setOnPageChangeListener(new MyPageChangeListener());

        if(isAutoPlay){
        	startPlay();
        }
	}
	
	 /**
     * 开始轮播图切换
     */
    private void startPlay(){
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4, TimeUnit.SECONDS);
       //根据他的参数说明，第一个参数是执行的任务，第二个参数是第一次执行的间隔，第三个参数是执行任务的周期；
    }
    
    /**
     *执行轮播图切换任务
     *
     */
    private class SlideShowTask implements Runnable{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            synchronized (mviewPager) {
                currentItem = (currentItem+1)%list.size();
                handler.sendEmptyMessage(SCHEDULE_EXCECUTOR_FLAG);
            }
        }
    }
 
  /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     * 
     */
    private class MyPageChangeListener implements OnPageChangeListener{

        boolean isAutoPlay = false;
        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
            case 1:// 手势滑动，空闲中
                isAutoPlay = false;
                break;
            case 2:// 界面切换中
                isAutoPlay = true;
                break;
            case 0:// 滑动结束，即切换完毕或者加载完毕
                // 当前为最后一张，此时从右向左滑，则切换到第一张
                if (mviewPager.getCurrentItem() == mviewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                    mviewPager.setCurrentItem(0);
                }
                // 当前为第一张，此时从左向右滑，则切换到最后一张
                else if (mviewPager.getCurrentItem() == 0 && !isAutoPlay) {
                    mviewPager.setCurrentItem(mviewPager.getAdapter().getCount() - 1);
                }
                break;
        }       
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageSelected(int pos) {
            // TODO Auto-generated method stub
            //这里面动态改变小圆点的被背景，来实现效果
            currentItem = pos;
            for(int i=0;i < dotViewList.size();i++){
                if(i == pos){
                    ((View)dotViewList.get(pos)).setBackgroundResource(R.drawable.point1_select);
                }else {
                    ((View)dotViewList.get(i)).setBackgroundResource(R.drawable.point1_normal);
                }
            }
        }   
    }
	
    /*
     * 定位城市
     */
    private OnClickListener myCityLocationListener = new OnClickListener() {		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(HomeActivity.this,CityLocationActivity.class);
			intent.putExtra("CITY_NAME", Consts.City_Name);
			startActivityForResult(intent, REQUEST_OK);
			overridePendingTransition(R.anim.in_from_top, R.anim.out_from_bottom);
			//finish();
		}
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtil.log(TAG, "resultCode :"+resultCode);
		if(resultCode==RESULT_OK) {
			if(requestCode==REQUEST_OK) {
				Bundle bundle = data.getExtras();
				mCityName.setText(bundle.getString("CITY_NAME"));				
				cityCode = bundle.getString("CITY_CODE");
				LogUtil.log(TAG, "mycityName :"+bundle.getString("CITY_NAME"));
				loadPropertyInfo();
			}
		}
	};
	
	private void closeProgressDialog() {
		if (null != progressDialog) {
			progressDialog.dismiss();
		}
	}	
	
	/*
	 * 初始化定位
	 */
	private void initLocation(){
		mLocationClient = new LocationClient(getApplicationContext());
		mLocationClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Battery_Saving);//设置定位模式  Hight_Accuracy  Battery_Saving
		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
		option.setOpenGps(true);// 打开GPS
		option.setScanSpan(3000);//设置发起定位请求的间隔时间为3000ms
		option.setIsNeedAddress(true);//设置是否需要地址信息，默认为无地址
		mLocationClient.setLocOption(option);
		mLocationClient.start();// 开启定位SDK
		mLocationClient.requestLocation();// 开始请求位置
	}
	
	/**
	 * 地图定位(监听器)
	 */
	public class cityLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location != null) {
				//StringBuffer sb = new StringBuffer(128);// 接受服务返回的缓冲区
				//sb.append(location.getCity());// 获得城市
				LogUtil.log(TAG, "City:"+location.getCity());
				Consts.City_Name = location.getCity();
				mCityName.setText(Consts.City_Name);
				cityCode = location.getCityCode();
				mLocationClient.stop();
				mThread.start();
			}
		}

		public void onReceivePoi(BDLocation arg0) {

		}
	}
	
	/**
	 * 停止，减少资源消耗
	 */
	public void stopListener() {
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.stop();// 关闭定位SDK
			mLocationClient = null;
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		stopListener();
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		LogUtil.log(TAG, "onBackPressed!");
		closeProgressDialog();
		super.onBackPressed();
	}
}
