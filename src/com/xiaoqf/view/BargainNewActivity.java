package com.xiaoqf.view;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.alipay.sdk.app.PayTask;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xiaoqf.app.R;
import com.xiaoqf.beans.AliPayment;
import com.xiaoqf.beans.House;
import com.xiaoqf.beans.Order;
import com.xiaoqf.beans.OrderInfo;
import com.xiaoqf.beans.Project;
import com.xiaoqf.beans.Room;
import com.xiaoqf.beans.WeiXinPayment;
import com.xiaoqf.beans.myLikeRoomOrderBean;
import com.xiaoqf.common.Consts;
import com.xiaoqf.customview.BaseEffects;
import com.xiaoqf.customview.Effectstype;
import com.xiaoqf.customview.MyScrollView;
import com.xiaoqf.customview.MyScrollView.OnBorderListener;
import com.xiaoqf.customview.PaymentDialog;
import com.xiaoqf.util.AdvancedCountdownTimer;
import com.xiaoqf.util.AlipayUtil;
import com.xiaoqf.util.CommonUtil;
import com.xiaoqf.util.EventHandler;
import com.xiaoqf.util.LogUtil;
import com.xiaoqf.util.PayResult;
import com.xiaoqf.util.PropertiesUtil;
import com.xiaoqf.util.WeiXinpayUtil;
import com.xiaoqf.wxapi.ShareWeiXinDialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class BargainNewActivity extends BaseActivity {

	private static final int ORDER_NEW = 341;
	private static final int ORDER_DETAIL = 344;

	private static final int GET_IMAGE_SUCCESS = 111;
	private static final int SDK_PAY_FLAG = 112;

	/**
	 * 判断时间支付时间是否结束
	 */
	private boolean isTimeUp = false;

	/**
	 * 显示标题栏名字
	 */
	@ViewInject(R.id.tv_title)
	private TextView titleTextView;
	/**
	 * 显示订单编号
	 */
	@ViewInject(R.id.tv_order_number)
	private TextView tvOrderNum;
	/**
	 * 显示小Q购房价
	 */
	@ViewInject(R.id.tv_pay_xiaoq)
	private TextView tvPayXiaoQ;
	/**
	 * 显示原购房价
	 */
	@ViewInject(R.id.tv_pay_original)
	private TextView tvPayOriginal;
	/**
	 * 显示小Q砍价优惠
	 */
	@ViewInject(R.id.tv_cut_money)
	private TextView tvCutMoney;
	/**
	 * 显示小Q最高砍价优惠额
	 */
	@ViewInject(R.id.tv_max_cut)
	private TextView tvMaxCut;
	/**
	 * 显示最低购买价格
	 */
	@ViewInject(R.id.tv_min_pay)
	private TextView tvMinPay;
	/**
	 * 按钮邀请砍价
	 */
	@ViewInject(R.id.b_invite)
	private Button bCut;
	/**
	 * 显示楼盘名称
	 */
	@ViewInject(R.id.tv_build_name)
	private TextView tvBuildName;
	/**
	 * 显示楼盘地址
	 */
	@ViewInject(R.id.tv_build_address)
	private TextView tvBuildAddress;
	/**
	 * 显示房间面积
	 */
	@ViewInject(R.id.tv_build_area)
	private TextView tvRoomArea;
	/**
	 * 显示房间编号
	 */
	@ViewInject(R.id.tv_build_room)
	private TextView tvRoomNum;
	/**
	 * 显示房产期限
	 */
	@ViewInject(R.id.tv_build_term)
	private TextView tvRoomTerm;
	/**
	 * 显示房间类型（毛坯 精装 豪装等）
	 */
	@ViewInject(R.id.tv_build_type)
	private TextView tvRoomType;

	/**
	 * 显示房型图
	 */
	@ViewInject(R.id.iv_room_image)
	private ImageView ivRoomImage;
	/**
	 * 是否阅读协议
	 */
	@ViewInject(R.id.iv_room_image)
	private ImageView ivIsCheckRead;
	/**
	 * 购买定金金额
	 */
	@ViewInject(R.id.tv_deposit)
	private TextView tvPayDeposit;
	/**
	 * 支付定金
	 */
	@ViewInject(R.id.rl_pay)
	private RelativeLayout rlPay;
	/**
	 * 支付完定金显示
	 */
	@ViewInject(R.id.activity_bargain_items_after_book)
	private RelativeLayout rlPayed;
	/**
	 * 显示剩余有效支付时间
	 */
	@ViewInject(R.id.tv_left_time)
	private TextView tvLeftTime;
	/**
	 * 显示房间户型
	 */
	@ViewInject(R.id.tv_build_layout)
	private TextView tvRoomLayout;
	/**
	 * 点击跳转到协议界面
	 */
	@ViewInject(R.id.activity_loginfreeregister_item_tv_comfirm)
	private TextView tvAgreement;
	/**
	 * 是否阅读协议区域
	 */
	@ViewInject(R.id.llRead)
	private LinearLayout llRead;
	/**
	 * 加载信息提示框
	 */
	private static ProgressDialog pDialog;
	/**
	 * 订单详情的滚动部分
	 */
	@ViewInject(R.id.sv_order_info)
	private MyScrollView svOrderInfo;
	/**
	 * 定金以及支付按钮区域
	 */
	@ViewInject(R.id.ll_pay_layout)
	private LinearLayout llPayLayout;
	/**
	 * 是否已提交定金
	 */
	public static boolean isBooked;
	/** 选中的房间 */
	private Room room;
	/** 该房所在楼栋 */
	private House house;
	/** 选中房的楼盘信息 */
	private Project project;
	/** 当前订单 */
	private Order order;
	/** 看中的订单 **/
	private myLikeRoomOrderBean myLikeRoomOrder;
	/** 楼盘Id */
	private String projectId;
	/** 房间Id */
	private String roomId;
	/**
	 * 订单id
	 */
	protected String orderId;
	/**
	 * 拼接房间信息
	 */
	private StringBuilder sb;
	/**
	 * 最新获取到的订单详情
	 */
	private OrderInfo orderInfo;

	/**
	 * 头像bitmap
	 */
	private Bitmap headBitmap;

	/**
	 * 支付宝充值支付信息
	 * 
	 */
	private AliPayment aliPayment;

	// 微信充值支付信息
	private WeiXinPayment wxPayment;
	private IWXAPI msgApi;
	PayReq req;

	private MyCount myCount;

	private boolean isSignButtonClick = false;

	private Effectstype type = null;
	/**
	 * 用于支付按钮区域是否显示
	 */
	private boolean isPayLayoutVisible = true;

	private int mDuration = 700 * 1;

	// private int lastY = 0;
	// private int touchEventId = -9983761;

	private Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// View scroller = (View) msg.obj;
			//
			// if (msg.what == touchEventId) {
			// if (lastY == scroller.getScrollY()) {
			// System.out.println("==================我停止了"+scroller.getScrollY()+","+lastY);
			// // 停止了，此处你的操作业务
			// if (scroller.getScrollY()==0) {
			// // type = Effectstype.Shake;
			// // start(type);
			// llPayLayout.setVisibility(View.GONE);
			// isPayLayoutVisible = false;
			// }
			//
			// } else {
			// myHandler.sendMessageDelayed(
			// myHandler.obtainMessage(touchEventId, scroller), 1);
			// lastY = scroller.getScrollY();
			// }
			// };

			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();
				String memo = payResult.getMemo();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					EventHandler.showToast(BargainNewActivity.this, "支付成功");
					// 更新订单详情相关组件
					// getOrderDetail(orderId);
					isBooked = true;
					// rlPay.setVisibility(View.GONE);
					// rlPayed.setVisibility(View.VISIBLE);
					// llRead.setVisibility(View.GONE);
					Intent intent = new Intent(getApplicationContext(),
							BookPayedActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("Order", orderInfo);
					// bundle.putSerializable("Project", project);
					// bundle.putSerializable("House", house);
					// bundle.putSerializable("Room", room);
					intent.putExtras(bundle);
					startActivity(intent);

				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						EventHandler.showToast(BargainNewActivity.this,
								"支付结果确认中");

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						EventHandler.showToast(BargainNewActivity.this, "支付失败");
						isBooked = false;
					}
				}
				break;
			}
			case ORDER_DETAIL:
				orderId = orderInfo.getOrderId();
				// if ((myLikeRoomOrder == null) && (project != null)
				// && (house != null) && (room != null)) {
				// sb = new StringBuilder().append(project.getName())
				// .append('(').append(house.getName())
				// .append(room.getName()).append(')');
				// titleTextView.setText(sb);
				// }
				setUIData();
				closeDialog();
				break;
			case ORDER_NEW:
				orderId = orderInfo.getOrderId();
				sb = new StringBuilder().append(project.getName()).append('(')
						.append(house.getName()).append(room.getName())
						.append(')');
				titleTextView.setText(sb);
				setUIData();
				closeDialog();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bargain_new);
		ViewUtils.inject(this);
		Consts.book_orderInfo = null;
		isBooked = false;
		Intent intent = getIntent();
		if (null != intent.getExtras()) {
			Bundle data = intent.getExtras();
			if (null != data.getSerializable("Project")) {
				project = (Project) data.getSerializable("Project");
				projectId = project.getId();
			}
			if (null != data.getSerializable("Room")) {
				room = (Room) data.getSerializable("Room");
				roomId = room.getId();
			}
			if (null != data.getSerializable("House")) {
				house = (House) data.getSerializable("House");
			}
			if (null != data.getSerializable("MyLikeRoomOrder")) {
				myLikeRoomOrder = (myLikeRoomOrderBean) data
						.getSerializable("MyLikeRoomOrder");
				orderId = myLikeRoomOrder.getOrderID();
				if ("1".equals(myLikeRoomOrder.getOrderHandleProcess())) {
					isBooked = false;
				} else
					isBooked = true;
			}
			if (null != data.getSerializable("BookedOrder")) {
				order = (Order) data.getSerializable("BookedOrder");
				orderId = order.getId();
				isBooked = true;
			}
		}
		init();

	}

	private void init() {
		if (myLikeRoomOrder != null) {
			getOrderDetail(orderId);
			String titleName = myLikeRoomOrder.getProjectName();
			titleTextView.setText(titleName);
		} else if (null != order) {
			getOrderDetail(order.getId());
			sb = new StringBuilder().append(project.getName()).append('(')
					.append(house.getName()).append(room.getName()).append(')');
			titleTextView.setText(sb);
		} else {
			getLowestPrice();
		}
	}

	/**
	 * 获取底价，并生成新订单
	 */
	private void getLowestPrice() {
		// 调用获取底价接口
		RequestParams params = new RequestParams();
		params.addBodyParameter("projectId", projectId);
		params.addBodyParameter("roomId", roomId);
		params.addBodyParameter("userMobile", Consts.userMobile);
		params.addBodyParameter("tokenId", Consts.tokenID);

		getOrder(Consts.SERVER_URL_PRICELOWEST, params, ORDER_NEW);
	}

	/**
	 * 获取当前订单详情
	 */
	private void getOrderDetail(String orderid) {
		// 调用获取底价接口
		RequestParams params = new RequestParams();
		params.addBodyParameter("orderId", orderid);
		params.addBodyParameter("tokenId", Consts.tokenID);

		getOrder(Consts.SERVER_URL_ORDER_DETIAL, params, ORDER_DETAIL);
	}

	/**
	 * 
	 * 调用订单相关接口
	 * 
	 * @param url
	 *            服务器接口地址
	 * @param params
	 * @param what
	 *            msg.what
	 */
	private void getOrder(String url, RequestParams params, final int what) {
		pDialog = EventHandler.showProgress(this, "正在更新信息……", false, false);
		HttpUtils http = new HttpUtils();
		CookieStore cookieStore = getCookieStore((DefaultHttpClient) http
				.getHttpClient());

		if (cookieStore != null) {
			http.configCookieStore(cookieStore);
		}
		http.send(HttpRequest.HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtil.log(TAG, "responseInfo.result:["
								+ responseInfo.result + "]");
						String info = responseInfo.result;
						try {
							JSONObject jsonObject = new JSONObject(info);
							if (!"1".equals(jsonObject.getString("status"))) {
								closeDialog();
								String errCode = jsonObject
										.getString("errorCode");
								String errMsg = PropertiesUtil.getProperties(
										getApplicationContext()).getProperty(
										errCode);
								EventHandler.showToast(BargainNewActivity.this,
										errMsg);
								if (isUserNeedLogin(errCode)) {
									skip2LoginActicity(BargainNewActivity.this);
								}
								return;
							}

							JSONObject jOrder = new JSONObject(jsonObject
									.getString("order"));
							// order = new Order();
							orderInfo = new OrderInfo();
							orderInfo.setUserId(jOrder.getString("userId"));
							orderInfo.setUserMobile(jOrder
									.getString("userMobile"));
							orderInfo.setOrderCode(jOrder
									.getString("orderCode"));
							orderInfo.setOrderAmount(jOrder
									.getString("orderAmount"));
							orderInfo.setOrderCouponAmount(jOrder
									.getString("orderCouponAmount"));
							orderInfo.setOrderOrininalAmout(jOrder
									.getString("orderOriginalAmount"));
							orderInfo.setMaxDiscountAmount(jOrder
									.getString("maxDiscountAmount"));
							orderInfo.setCreateTime(jOrder
									.getString("createTime"));
							orderInfo.setOrderStatus(jOrder
									.getString("orderStatus"));
							orderInfo.setOrderComStep(jOrder
									.getString("orderComStep"));
							orderInfo.setOrderSurplusTime(jOrder
									.getString("orderSurplusTime"));
							orderInfo.setRoomLawAmount(jOrder
									.getString("roomLowAmount"));
							orderInfo.setRoomChargeAmount(jOrder
									.getString("roomChargeAmount"));
							orderInfo.setProjectName(jOrder
									.getString("projectName"));
							orderInfo.setProjectAddress(jOrder
									.getString("projectAddress"));
							orderInfo.setOrderId(jOrder.getString("orderId"));
							orderInfo.setRoomSquare(jOrder
									.getString("roomSquare"));
							orderInfo.setPropertyRight(jOrder
									.getString("propertyRight"));
							String type = jOrder.getString("roomType");
							if (type.equals("0")) {
								orderInfo.setRoomType("毛坯");
							} else if (type.equals("1")) {
								orderInfo.setRoomType("精装");
							}
							orderInfo.setRoomLayout(jOrder
									.getString("roomLayout"));
							orderInfo.setRoomLayoutImage(jOrder
									.getString("roomLayoutImage"));
							orderInfo.setRoomInfo(jOrder.getString("roomInfo"));
							myHandler.sendEmptyMessage(what);
						} catch (JSONException e) {
							closeDialog();
							LogUtil.logError(e);
							EventHandler.showToast(BargainNewActivity.this,
									"服务器忙，请稍后重试");
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						closeDialog();
						LogUtil.logError(error);
						EventHandler.showToast(BargainNewActivity.this,
								"服务器忙，请稍后重试");
					}
				});
	}

	/**
	 * 填充界面中UI数据
	 */
	@SuppressLint("SimpleDateFormat")
	private void setUIData() {
		tvOrderNum.setText("订单编号:" + orderInfo.getOrderCode());
		tvPayXiaoQ.setText(orderInfo.getOrderAmount());
		tvPayOriginal.getPaint().setAntiAlias(true);// 抗锯齿
		tvPayOriginal.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 中划线
		tvPayOriginal.getPaint().setFlags(
				Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
		tvPayOriginal.setText("原价:" + orderInfo.getOrderOrininalAmout());
		tvCutMoney.setText(orderInfo.getOrderCouponAmount());
		tvMaxCut.setText(orderInfo.getMaxDiscountAmount());
		tvBuildName.setText(orderInfo.getProjectName());
		tvBuildAddress.setText(orderInfo.getProjectAddress());
		tvRoomNum.setText(orderInfo.getRoomInfo());
		tvRoomArea.setText(orderInfo.getRoomSquare() + "平");
		tvRoomTerm.setText(orderInfo.getPropertyRight() + "年");
		tvRoomType.setText(orderInfo.getRoomType());
		tvRoomLayout.setText(orderInfo.getRoomLayout());
		setRoomLayoutIcon();
		tvPayDeposit.setText(orderInfo.getRoomChargeAmount());
		long ms = Long.parseLong(orderInfo.getOrderSurplusTime());
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");// 初始化Formatter的转换格式。
		String hms = formatter.format(ms);
		tvLeftTime.setText(hms);
		myCount = new MyCount(ms, 1000);
		myCount.start();
		String minPeiceTips = "小Q提醒:最低购买价" + orderInfo.getRoomLawAmount()
				+ "元，赶紧邀请朋友去砍价吧！";
		tvMinPay.setText(minPeiceTips);
		// Spannable span = new SpannableString(tvMinPay.getText());
		// span.setSpan(new AbsoluteSizeSpan(58), 11, 16,
		// Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		// span.setSpan(new ForegroundColorSpan(Color.RED), 11, 16,
		// Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		// span.setSpan(new BackgroundColorSpan(Color.YELLOW), 11, 16,
		// Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		// textView3.setText(span);
		if (isPayLayoutVisible) {

		}
		svOrderInfo.setOnBorderListener(new OnBorderListener() {

			@Override
			public void onTop() {
				// TODO Auto-generated method stub
				llPayLayout.setVisibility(View.GONE);
				Animation animation = AnimationUtils.loadAnimation(
						BargainNewActivity.this,
						R.anim.push_bottom_out);
				llPayLayout.startAnimation(animation);
				llPayLayout.setVisibility(View.GONE);
				isPayLayoutVisible = false;

			}

			@Override
			public void onBottom() {
				// TODO Auto-generated method stub

			}
		});
		svOrderInfo.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View view, MotionEvent e) {
				switch (e.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// lastY = view.getScrollY();
					break;
				case MotionEvent.ACTION_MOVE:
					int scrollY = view.getScrollY();
					// int height = view.getHeight();
					// int scrollViewMeasuredHeight = svOrderInfo.getChildAt(0)
					// .getMeasuredHeight();
					if (scrollY == 0) {
						// System.out.println("滑动到了顶端 view.getScrollY()="
						// + scrollY);
						if (isPayLayoutVisible) {
							llPayLayout.setVisibility(View.GONE);
						}
						isPayLayoutVisible = false;
					} else {
						if (!isPayLayoutVisible) {
							// if(type==null){
							// type = Effectstype.SlideBottom;
							// // }
							// start(type);
							llPayLayout.setVisibility(View.VISIBLE);
							Animation animation = AnimationUtils.loadAnimation(
									BargainNewActivity.this,
									R.anim.push_bottom_in);
							llPayLayout.startAnimation(animation);
							
							// llPayLayout.seta
							isPayLayoutVisible = true;
						}
					}
					break;
				case MotionEvent.ACTION_UP:
					// myHandler.sendMessageDelayed(
					// myHandler.obtainMessage(touchEventId, view), 5);

				default:
					break;

				}
				return false;
			}
		});
	}

	private void start(Effectstype type) {
		BaseEffects animator = type.getAnimator();
		if (mDuration != -1) {
			animator.setDuration(Math.abs(mDuration));
		}
		animator.start(llPayLayout);
	}

	/**
	 * 通过获取到用户头像连接地址显示头像
	 */
	private void setRoomLayoutIcon() {
		if (!orderInfo.getRoomLayoutImage().equals("")
				&& orderInfo.getRoomLayoutImage() != null) {
			Thread thread = new Thread() {

				public void run() {

					try {
						headBitmap = BitmapFactory.decodeStream(new URL(
								orderInfo.getRoomLayoutImage()).openStream());
						myHandler.sendEmptyMessage(GET_IMAGE_SUCCESS);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			thread.start();
		} else {
			ivRoomImage.setImageResource(R.drawable.image_room_model);
		}
	}

	/**
	 * 关闭提示框
	 */
	private static void closeDialog() {
		if (null != pDialog) {
			pDialog.dismiss();
		}
	}

	/**
	 * 邀好友砍价
	 * 
	 */
	@OnClick(R.id.b_invite)
	public void onBargainByWechatClick(View v) {
		onWeiXinShareClick(v);
	}

	/**
	 * 微信分享
	 * 
	 */
	@OnClick(R.id.activity_bargain_free_weixin)
	public void onWeiXinShareClick(View v) {
		ShareWeiXinDialog share = new ShareWeiXinDialog();
		share.init(this, orderId);
		share.showShareWXDialog();
	}

	/**
	 * 用户充值 弹出选择支付方式
	 * 
	 * @param v
	 */
	@OnClick(R.id.rl_pay)
	public void onDiscountDepositClick(View v) {
		// 支付方式选择框
		final PaymentDialog payDialog = PaymentDialog.getInstance(this);

		payDialog.onAlipayRlClick(new OnClickListener() {

			@Override
			public void onClick(View v) {
				payDialog.dismiss();
				alipay(v);
				// EventHandler.showToast(BargainActivity.this, "调用支付宝支付订金");

			}
		});

		payDialog.onWeixinRlClick(new OnClickListener() {

			@Override
			public void onClick(View v) {
				payDialog.dismiss();
				weixinPay(v);
				// EventHandler.showToast(BargainActivity.this, "调用微信支付订金");

			}
		});

		Window dialogWin = payDialog.getWindow();
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		// 去除默认黑色背景
		dialogWin.setBackgroundDrawable(new BitmapDrawable());
		// 水平占满
		dialogWin.getDecorView().setPadding(0, 0, 0, 0);
		dialogWin.setGravity(Gravity.BOTTOM);
		WindowManager.LayoutParams lp = dialogWin.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		dialogWin.setAttributes(lp);
		payDialog.setCanceledOnTouchOutside(true);
		// payDialog.setCancelable(true);
		// payDialog.setCancelable(true);
		payDialog.show();
	}

	/**
	 * 调用支付宝支付订金
	 * 
	 * @param v
	 */
	public void alipay(View v) {
		if (null == orderInfo.getRoomChargeAmount()) {
			EventHandler.showToast(BargainNewActivity.this, "服务器忙，请稍后重试");
			return;
		}

		// 调用用户充值接口
		RequestParams params = new RequestParams();
		params.addBodyParameter("tokenId", Consts.tokenID);
		params.addBodyParameter("orderId", orderInfo.getOrderId());
		// params.addBodyParameter("orderId", orderId);
		params.addBodyParameter("payType",
				String.valueOf(AliPayment.OPERATE_RECHARGE));

		HttpUtils http = new HttpUtils();
		CookieStore cookieStore = getCookieStore((DefaultHttpClient) http
				.getHttpClient());

		if (cookieStore != null) {
			http.configCookieStore(cookieStore);
		}

		http.send(HttpRequest.HttpMethod.POST,
				Consts.SERVER_URL_RECHARGE_BY_ALIPAY, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtil.log(TAG, "responseInfo.result:["
								+ responseInfo.result + "]");
						String info = responseInfo.result;

						try {
							JSONObject jsonObject = new JSONObject(info);
							if (!"1".equals(jsonObject.getString("status"))) {
								String errCode = jsonObject
										.getString("errorCode");
								String errMsg = PropertiesUtil.getProperties(
										getApplicationContext()).getProperty(
										errCode);
								EventHandler.showToast(BargainNewActivity.this,
										errMsg);

								if (isUserNeedLogin(errCode)) {
									skip2LoginActicity(BargainNewActivity.this);
								}
								return;
							}

							aliPayment = new AliPayment();
							aliPayment.setService(jsonObject
									.getString("service"));
							aliPayment.setPantner(jsonObject
									.getString("pantner"));
							aliPayment.setEncode(jsonObject
									.getString("_input_charset"));
							aliPayment.setEncrypttype(jsonObject
									.getString("sign_type"));
							aliPayment.setOrderId(jsonObject
									.getString("outTradeNo"));
							aliPayment.setSubject(jsonObject
									.getString("subject"));
							aliPayment.setType(jsonObject
									.getString("payment_type"));
							aliPayment.setSellerId(jsonObject
									.getString("seller_id"));
							aliPayment.setBody(jsonObject.getString("body"));
							aliPayment.setKey(jsonObject.getString("key"));

							String fee = Consts.isDebugMode ? Consts.PRICE_CENT
									: jsonObject.getString("total_fee");
							aliPayment.setTotalFee(fee);

							String url = jsonObject.getString("notify_url");
							if (!url.contains("http")) {
								url = Consts.SERVER_URL + url;
							}
							aliPayment.setUrl(url);

							// 调用支付宝充值
							String orderInfo = AlipayUtil
									.getOrderInfo(aliPayment);
							pay(orderInfo);

						} catch (JSONException e) {
							LogUtil.logError(e);
							EventHandler.showToast(BargainNewActivity.this,
									"服务器忙，请稍后重试");
						}

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						LogUtil.logError(error);
						EventHandler.showToast(BargainNewActivity.this,
								"服务器忙，请稍后重试");
					}

				});

	}

	/**
	 * 调用支付宝SDK
	 */
	private void pay(String orderInfo) {
		// 对订单做RSA 签名
		String sign = AlipayUtil.sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ AlipayUtil.getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(BargainNewActivity.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				myHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();

	}

	// 调用微信支付订金
	public void weixinPay(View v) {
		if (null == orderInfo.getRoomChargeAmount()) {
			EventHandler.showToast(BargainNewActivity.this, "服务器忙，请稍后重试");
			return;
		}

		// 调用用户充值接口
		RequestParams params = new RequestParams();
		params.addBodyParameter("tokenId", Consts.tokenID);
		params.addBodyParameter("orderId", orderInfo.getOrderId());
		params.addBodyParameter("payType",
				String.valueOf(WeiXinPayment.OPERATE_RECHARGE));
		params.addBodyParameter("spbill_create_ip",
				CommonUtil.getLocalIpAddress());

		HttpUtils http = new HttpUtils();
		CookieStore cookieStore = getCookieStore((DefaultHttpClient) http
				.getHttpClient());

		if (cookieStore != null) {
			http.configCookieStore(cookieStore);
		}

		http.send(HttpRequest.HttpMethod.POST,
				Consts.SERVER_URL_RECHARGE_BY_WXPAY, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtil.log(TAG, "responseInfo.result:["
								+ responseInfo.result + "]");
						String info = responseInfo.result;

						try {
							JSONObject jsonObject = new JSONObject(info);
							if (!"1".equals(jsonObject.getString("status"))) {
								String errCode = jsonObject
										.getString("errorCode");
								String errMsg = PropertiesUtil.getProperties(
										getApplicationContext()).getProperty(
										errCode);
								EventHandler.showToast(BargainNewActivity.this,
										errMsg);

								if (isUserNeedLogin(errCode)) {
									skip2LoginActicity(BargainNewActivity.this);
								}
								return;
							}

							wxPayment = new WeiXinPayment();
							wxPayment.setAppid(jsonObject.getString("appid"));
							wxPayment.setPartnerid(jsonObject
									.getString("partnerid"));
							wxPayment.setPrepayid(jsonObject
									.getString("prepayid"));
							wxPayment.setPackAge(jsonObject
									.getString("packAge"));
							wxPayment.setNoncestr(jsonObject
									.getString("noncestr"));
							wxPayment.setTimestamp(jsonObject
									.getString("timestamp"));
							wxPayment.setSign(jsonObject.getString("sign"));

							Consts.book_orderInfo = orderInfo;
							Consts.project_wx = project;
							Consts.house_wx = house;
							Consts.room_wx = room;
							// 调用微信充值
							msgApi = WXAPIFactory.createWXAPI(
									BargainNewActivity.this, null);
							req = WeiXinpayUtil.genPayReq(wxPayment);
							msgApi.registerApp(wxPayment.getAppid());
							msgApi.sendReq(req);

						} catch (JSONException e) {
							LogUtil.logError(e);
							EventHandler.showToast(BargainNewActivity.this,
									"服务器忙，请稍后重试");
						}

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						LogUtil.logError(error);
						EventHandler.showToast(BargainNewActivity.this,
								"服务器忙，请稍后重试");
					}

				});

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isBooked) {
			rlPay.setVisibility(View.GONE);
			rlPayed.setVisibility(View.VISIBLE);
			llRead.setVisibility(View.GONE);
		} else {
			rlPay.setVisibility(View.VISIBLE);
			rlPayed.setVisibility(View.GONE);
			llRead.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 跳转到协议详情界面
	 * 
	 * @param v
	 */
	@OnClick(R.id.activity_loginfreeregister_item_tv_comfirm)
	public void onSignInfoClick(View v) {
		CommonUtil.openActivity(this, AgreementActivity.class);
	}

	@SuppressLint("NewApi")
	@OnClick(R.id.activity_loginfreeregister_item_iv_comfirm)
	public void onSignImageViewOnClick(View v) {
		if (isSignButtonClick) {
			isSignButtonClick = false;
			v.setBackground(getResources().getDrawable(R.drawable.sign_comfirm));
			if (!isTimeUp) {
				rlPay.setPressed(false);
				rlPay.setClickable(true);
			}

		} else {
			isSignButtonClick = true;
			v.setBackground(getResources().getDrawable(
					R.drawable.sign_discomfirm));
			if (!isTimeUp) {
				rlPay.setPressed(true);
				rlPay.setClickable(false);
			}
		}
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

	/**
	 * 实现定时计时功能
	 * 
	 * @author xiaoq
	 * 
	 */
	class MyCount extends AdvancedCountdownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			rlPay.setPressed(true);
			rlPay.setClickable(false);
			tvLeftTime.setText(0 + ":" + 0 + ":" + 0);
			isTimeUp = true;
		}

		// 更新剩余时间
		@Override
		public void onTick(long millisUntilFinished, int percent) {
			long myhour = (millisUntilFinished / 1000) / 3600;
			long myminute = ((millisUntilFinished / 1000) - myhour * 3600) / 60;
			long mysecond = millisUntilFinished / 1000 - myhour * 3600
					- myminute * 60;
			tvLeftTime.setText(myhour + ":" + myminute + ":" + mysecond);

		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		myCount.cancel();
	}

}
