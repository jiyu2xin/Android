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
import com.lidroid.xutils.view.annotation.event.OnTouch;
import com.xiaoqf.beans.Floor;
import com.xiaoqf.beans.House;
import com.xiaoqf.beans.Project;
import com.xiaoqf.beans.Room;
import com.xiaoqf.beans.User;
import com.xiaoqf.common.Consts;
import com.xiaoqf.customview.RoomSelectorView;
import com.xiaoqf.util.CommonUtil;
import com.xiaoqf.util.EventHandler;
import com.xiaoqf.util.LogUtil;
import com.xiaoqf.util.PropertiesUtil;
import com.xiaoqf.app.R;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @ClassName: RoomSelectActivity
 * @Description: 选房 界面
 * @version: 1.0
 * @author: tongdu
 * @Create: 2015-05-06
 */
public class RoomSelectActivity extends BaseActivity {
	protected static final String TAG = "com.xiaoqf.app.RoomSelectActivity";
	protected static final int DOWN_ROOMS = 333;
	protected static final int DOWN_HOUSES = 332;
	protected static final int ROOM_PRICE = DOWN_ROOMS + 1;
	private String projectId;
	private String tokenId;
	/** 本栋第一层，在返回本界面时，作为偏移值 */
	private static int firstLayer = 1;

	// 最大列数
	private int MaxColNum;
	// 最大行数
	private int MaxFloorNum;
	private Project project;
	// 楼盘名称
	private String projectName;
	// 该楼盘栋数
	private int housesNum;
	// 选中的栋名
//	private String houseNameSel;
	// 选中的栋号
	private int houseNo;
	// 选中的按钮
	private Button houseBtnSel;
	// 上次选中的按钮
	private Button lastBtn;
	// 选中的房名
	private String roomNameSel;
	private Room roomSel;
	private ProgressDialog pDialog;
	
	// 本楼盘
	private House[] houses;
	private Button[] buttons;
	// 当前选中的栋
	private House house = new House(new ArrayList<Floor>());
//	private ArrayList<Floor> house = new ArrayList<Floor>();
	// 当前栋中的销售信息
	private ArrayList<ArrayList<Integer>> sellStates = new ArrayList<ArrayList<Integer>>();
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DOWN_HOUSES:
				if (houses == null) return;
//				setRoomsInfo(houses[0]);
				closeDialog();		
				setHouseBtnAndRooms(houseBtnSel);
				if (null != buttons[houseNo]) {
					buttons[houseNo].setPressed(true);
				}
				break;

			case DOWN_ROOMS:
				// 设置楼层高度
				LayoutParams lp = mRSView.getLayoutParams();
				// RoomSelectView高度
				int roomWidth = (MaxColNum > 4) ?
					RoomSelectActivity.this.getResources().getDimensionPixelSize(R.dimen.room_init_width)
					: (Consts.devWidth / MaxColNum);
				int roomHeight = (int) (roomWidth * RoomSelectorView.getRatiohw());
				lp.height = MaxFloorNum * roomHeight;
				mRSView.setLayoutParams(lp);
				mRSView.init(MaxColNum, MaxFloorNum, house.getFloors(), sellStates, null, 1);
				closeDialog();
				if (null != buttons[houseNo]) {
					buttons[houseNo].setPressed(true);
				}
				break;
				
			case ROOM_PRICE:
				roomPriceNameTextView.setText(houses[houseNo].getName() + roomNameSel +"售价：");
				roomPriceTextView.setText(CommonUtil.formatStr(roomSel.getPrice(), 4)+"元");
				break;
				
			default:
				closeDialog();
				break;
			}
		};
	};
	//楼盘图
	@ViewInject(R.id.activity_roomselect_iv)
	private ImageView projectBgImageView ;
	private String url;
	
	// 标题栏部件
	@ViewInject(R.id.iv_title_left)
	private ImageView backImageView;
	
	@ViewInject(R.id.tv_title)
	private TextView titleTextView;
	
	@ViewInject(R.id.tv_title_right)
	private TextView titleRightTextView;
	
	// 栋选择
	@ViewInject(R.id.activity_room_select_house_ll)
	private LinearLayout houseSelectorLinearLayout;
	
	@ViewInject(R.id.activity_room_house_bottom_1)
	private Button house1Button;
	
	// 房间选择界面
	@ViewInject(R.id.activity_house_rsview)
	private RoomSelectorView mRSView;
	
	// 选房信息
	@ViewInject(R.id.activity_house_select_tv_room_price_name)
	private TextView roomPriceNameTextView;
	
	@ViewInject(R.id.activity_house_select_tv_room_price)
	private TextView roomPriceTextView;
	
	// 获取底价
	@ViewInject(R.id.activity_room_select_get_price_rl_button)
	private RelativeLayout buttonGetPrice;
	
	// 获取底价LinearLayout
	@ViewInject(R.id.activity_house_select_ll_house)
	private LinearLayout roomPriceLinearlayout;
	
	// 未点击的提示信息
	@ViewInject(R.id.activity_house_select_layout_none)
	private LinearLayout infroLinearlayout;
	
	@ViewInject(R.id.activity_room_select_flow_guide)
	private TextView flowguideTv;
	
	@OnClick(R.id.activity_room_select_get_price_rl_button)
	public void onGetPriceClick(View v) {
		if (null == roomSel) {
			EventHandler.showToast(this, "请先选房。");
			return;
		}
	
		Intent intent = new Intent(this, BargainNewActivity.class);
//		Intent intent = new Intent(this, BargainActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("Room", roomSel);
		bundle.putSerializable("Project", project);
		bundle.putSerializable("House", houses[houseNo]);
//		intent.putExtra("projectId", projectId);
//		intent.putExtra("roomId", roomSel.getId());
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	@OnTouch(R.id.activity_room_house_bottom_1)
	public boolean house1ButtonTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			//clearRoomInfo();
			roomSel = null;
			houseBtnSel = (Button) v;
			setHouseBtnAndRooms(houseBtnSel);
			lastBtn = houseBtnSel;
		}
		return true;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_room_select);
		ViewUtils.inject(this);
		//clearRoomInfo();
		// Project
		tokenId = Consts.tokenID;
		Intent intent = getIntent();
		if (null != intent.getExtras()) {
			Bundle data = intent.getExtras();
			if (null != data.getSerializable("Project")) {
				project = (Project)intent.getSerializableExtra("Project");
				if (!TextUtils.isEmpty(project.getId())) {
					projectId = project.getId();
				}
				if (!TextUtils.isEmpty(project.getName())) {
					projectName = project.getName();
				}
			}
		}
		
		titleTextView.setText(projectName);
		lastBtn = house1Button;
		Consts.devWidth = CommonUtil.getScreenWidth(this);
//		house1Button.setPressed(true);
		init();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setHouseBtnAndRooms(lastBtn);
	}
	
	private void init() {
		houseBtnSel = house1Button;
		setHousesInfo();
		
		mRSView.setOnRoomClickListener(new OnRoomClickListener() {
			
			@Override
			public boolean onSelected(int inFloorNum, int floorNum) {
				
				
				// 更新选中的房价
				roomSel = house.getFloors().get(floorNum).getRoom(inFloorNum);
				roomNameSel = roomSel.getName();
				mHandler.sendEmptyMessage(ROOM_PRICE);
//				String hint =  "您选择了第"+(floorNum+1)+"层" + " 第" + (inFloorNum+1) +"列";
//				EventHandler.showToast(RoomSelectActivity.this, hint);
				return false;
			}
			
			@Override
			public boolean onDeSelected(int inFloorNum, int floorNum) {
//				String hint =  "您取消了第"+(floorNum+1)+"层" + " 第" + (inFloorNum+1) +"列";
//				EventHandler.showToast(RoomSelectActivity.this, hint);
				//infroLinearlayout.setVisibility(View.VISIBLE);
				//roomPriceLinearlayout.setVisibility(View.INVISIBLE);
				return false;
			}

			@Override
			public void onSoldClick() {
				String hint = "此房已售出！";
				EventHandler.showToast(RoomSelectActivity.this, hint);
			}
		});
	}

	/**
	 * 楼盘栋信息设置
	 */
	@SuppressLint("NewApi")
	private void setHousesInfo() {
		pDialog = EventHandler.showProgress(this, "正在更新信息……", false, false);
		// 调用楼栋列表接口
		RequestParams params = new RequestParams();
		params.addBodyParameter("projectId", projectId);
		params.addBodyParameter("tokenId", tokenId);
		
		HttpUtils http = new HttpUtils();
		CookieStore cookieStore = getCookieStore((DefaultHttpClient)http.getHttpClient());
		
		if(cookieStore!=null) {
			http.configCookieStore(cookieStore);
		}
		http.send(HttpRequest.HttpMethod.POST, Consts.SERVER_URL_HOUSELIST, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				closeDialog();
				LogUtil.logError(error);
				EventHandler.showToast(RoomSelectActivity.this, "服务器忙，请稍后重试");
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LogUtil.log(TAG,"responseInfo.result:[" + responseInfo.result + "]");
				String info = responseInfo.result;
//				String info = Consts.responseInfo_result_housesList;
				try {
					JSONObject jsonObject = new JSONObject(info);
					if (!"1".equals(jsonObject.getString("status"))) {
						closeDialog();
						String errCode = jsonObject.getString("errorCode");
						String errMsg = PropertiesUtil.getProperties(getApplicationContext()).getProperty(errCode);
						EventHandler.showToast(RoomSelectActivity.this, errMsg);
						
						if(isUserNeedLogin(errCode)) {
							skip2LoginActicity(RoomSelectActivity.this);
						}
						return;
					}
					url = jsonObject.getString("projectIcon");
					CommonUtil.loadImageFromUrl(RoomSelectActivity.this, projectBgImageView, url);
					
					JSONArray listsHouses = new JSONArray(jsonObject.getString("houseList"));
					housesNum = listsHouses.length();
					houses = new House[housesNum];
					// 处理栋
					for (int tmpHouse = 0; tmpHouse < housesNum	; tmpHouse++) {
						House aHouse = new House(new ArrayList<Floor>());
						JSONObject jHouse = listsHouses.getJSONObject(tmpHouse);
						aHouse.setId(jHouse.getString("houseId"));
						aHouse.setInProjectNo(jHouse.getString("houseNo"));
						aHouse.setName(jHouse.getString("houseName"));
						houses[tmpHouse] = aHouse;
					}
					
					// 添加楼栋对应的选择按钮
					if (housesNum >= 1) {
						buttons = new Button[housesNum];
							
						buttons[0] = house1Button;
						house1Button.setText(houses[0].getName());
						
						LayoutParams lp = house1Button.getLayoutParams();
						for (int tmpButton = 1; tmpButton < housesNum; tmpButton++) {
							Button tmpBtn = new Button(RoomSelectActivity.this);
							buttons[tmpButton] = tmpBtn;
							tmpBtn.setLayoutParams(lp);
							tmpBtn.setBackground(getResources().getDrawable(R.drawable.view_house_button_bg));
							tmpBtn.setText(houses[tmpButton].getName());
							tmpBtn.setTextSize(20);
							tmpBtn.setTextColor(getResources().getColorStateList(R.drawable.button_house_font));
							tmpBtn.setOnTouchListener(new OnTouchListener() {
								
								@Override
								public boolean onTouch(View v, MotionEvent event) {
									if (event.getAction() == MotionEvent.ACTION_UP) {
										//clearRoomInfo();
										roomSel = null;
										houseBtnSel = (Button) v;
										setHouseBtnAndRooms(houseBtnSel);
										lastBtn = houseBtnSel;
									}
									return true;
								}
							});
							
							houseSelectorLinearLayout.addView(tmpBtn);
						}
					}
					mHandler.sendEmptyMessage(DOWN_HOUSES);
					
				} catch (JSONException e) {
					closeDialog();
					LogUtil.logError(e);
					EventHandler.showToast(RoomSelectActivity.this, "服务器忙，请稍后重试");
				}
			}
		});
		
	}
	
	/**
	 * 点击楼栋按钮时，设置该楼栋房间信息
	 * @param btnSelected
	 */
	private void setHouseBtnAndRooms(Button btnSelected) {
//		if (btnSelected == lastBtn) return;
		if (buttons == null) return;
		
		for (int idx = 0; idx < buttons.length; idx++) {
			Button tmpBtn = buttons[idx];
			if (btnSelected != buttons[idx]) {
				tmpBtn.setPressed(false);
			} else {
				tmpBtn.setPressed(true);
				
				// 刷新该楼盘的房间信息
				setRoomsInfo(houses[idx]);
				houseNo =  idx;
			}
		}
	}

	/**
	 * 房间信息设置
	 */
	private void setRoomsInfo(House aHouse) {
		pDialog = EventHandler.showProgress(this, "正在更新信息……", false, false);
		// 调用房间列表接口
		RequestParams params = new RequestParams();
		params.addBodyParameter("projectId", projectId);
		params.addBodyParameter("tokenId", tokenId);
		params.addBodyParameter("houseId", aHouse.getId());
		
		HttpUtils http = new HttpUtils();
		CookieStore cookieStore = getCookieStore((DefaultHttpClient)http.getHttpClient());
		
		if(cookieStore!=null) {
			http.configCookieStore(cookieStore);
		}
		
		http.send(HttpRequest.HttpMethod.POST, Consts.SERVER_URL_ROOMLIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException error, String msg) {
				closeDialog();
				LogUtil.logError(error);
				EventHandler.showToast(RoomSelectActivity.this, "服务器忙，请稍后重试");
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LogUtil.log(TAG,"responseInfo.result:[" + responseInfo.result + "]");
				String info = responseInfo.result;
//				String info = Consts.responseInfo_result_roomsList;
				try {
					JSONObject jsonObject =  new JSONObject(info);
					if (!"1".equals(jsonObject.getString("status"))) {
						closeDialog();
						String errCode = jsonObject.getString("errorCode");
						String errMsg = PropertiesUtil.getProperties(getApplicationContext()).getProperty(errCode);
						EventHandler.showToast(RoomSelectActivity.this, errMsg);
						
						if(isUserNeedLogin(errCode)) {
							skip2LoginActicity(RoomSelectActivity.this);
						}
						return;
					}
					
					house.clear();
					sellStates.clear();
					
					JSONArray listsFloor = new JSONArray(jsonObject.getString("list"));
					MaxFloorNum = listsFloor.length();
					// 处理楼层(高层→底层)
					for (int tmpFloor = MaxFloorNum - 1; tmpFloor > -1; tmpFloor--) {
//					for (int tmpFloor = 0; tmpFloor < MaxFloorNum; tmpFloor++) {
						Floor aFloor = new Floor();
						ArrayList<Room> roomsList = new ArrayList<Room>();
						ArrayList<Integer> mStates = new ArrayList<Integer>();
						aFloor.setFloor(roomsList);
						
						JSONObject objectListRoom = listsFloor.getJSONObject(tmpFloor);
						String floorNum = objectListRoom.getString("roomLayer");
						if (tmpFloor == 0) {
							firstLayer = Integer.valueOf(floorNum);
						}
						aFloor.setFloorNum(floorNum);
						JSONArray listRoom = new JSONArray(objectListRoom.getString("roomList"));
						MaxColNum = listRoom.length();
						// 处理楼层内的房间
						for (int tmpCol = 0; tmpCol < MaxColNum; tmpCol++) {
							Room room = new Room();
							JSONObject jRoom = listRoom.getJSONObject(tmpCol);
							room.setId(jRoom.getString("roomId"));
							room.setName(jRoom.getString("roomNo"));
							room.setSellState(jRoom.getString("roomStatus"));
							room.setPrice(jRoom.getString("roomAmount"));
							room.setFloorNum(jRoom.getString("roomLayer"));
							room.setUnit(jRoom.getString("roomUnit"));
							room.setInFloorNum(String.valueOf(tmpCol));
							roomsList.add(room);
							Integer roomState = Integer.valueOf(jRoom.getString("roomStatus"));
							if (null != roomSel
								// 偏移值1为该楼层最底层
								&& tmpFloor == Integer.valueOf(roomSel.getFloorNum()) - firstLayer
								&& tmpCol == Integer.valueOf(roomSel.getInFloorNum())) {
								roomState = Room.Checked;
							}
							/*if (null != roomSel
									&& tmpFloor == Integer.valueOf(roomSel.getFloorNum()) - 1
									&& tmpCol == Integer.valueOf(roomSel.getInFloorNum())) {
								roomState = Room.Checked;
							}*/
							mStates.add(roomState);
						}			
						
						house.add(aFloor);
						sellStates.add(mStates);

					}
					mHandler.sendEmptyMessage(DOWN_ROOMS);
					houses[houseNo].setFloors(house.getFloors());
					
				} catch (JSONException e) {
					closeDialog();
					LogUtil.logError(e);
					EventHandler.showToast(RoomSelectActivity.this, "服务器忙，请稍后重试");
				}
				
			}
			
		});

	}
	
	private void closeDialog() {
		if (null != pDialog) {
			pDialog.dismiss();
		}
	}
	private void clearRoomInfo() {
		roomPriceLinearlayout.setVisibility(View.GONE);
		buttonGetPrice.setVisibility(View.GONE);
		//infroLinearlayout.setVisibility(View.VISIBLE);
	}
	/**
	 * @Title: houselBackImageViewClick
	 * @Description: 点击“返回”事件
	 * @param v
	 */
	@OnClick(R.id.iv_title_left)
	public void houselBackImageViewClick(View v) {
		super.onBackPressed();
	}
	
	@OnClick(R.id.activity_room_select_flow_guide)
	public void onFlowGuideClick(View v) {
		CommonUtil.openActivity(this, BuyGuideActivity.class);
	}
}
