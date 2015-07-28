package com.xiaoqf.view;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
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
import com.xiaoqf.beans.AliPayment;
import com.xiaoqf.beans.Discount;
import com.xiaoqf.beans.House;
import com.xiaoqf.beans.Order;
import com.xiaoqf.beans.Project;
import com.xiaoqf.beans.Room;
import com.xiaoqf.beans.WeiXinPayment;
import com.xiaoqf.beans.myLikeRoomOrderBean;
import com.xiaoqf.common.Consts;
import com.xiaoqf.customview.MagicTextView;
import com.xiaoqf.customview.PaymentDialog;
import com.xiaoqf.util.AlipayUtil;
import com.xiaoqf.util.CommonUtil;
import com.xiaoqf.util.EventHandler;
import com.xiaoqf.util.LogUtil;
import com.xiaoqf.util.PayResult;
import com.xiaoqf.util.PropertiesUtil;
import com.xiaoqf.util.WeiXinpayUtil;
import com.xiaoqf.wxapi.ShareWeiXinDialog;
import com.xiaoqf.app.R;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BargainActivity extends BaseActivity {
	private static final int ORDER_NEW = 341;
	private static final int ORDER_DETAIL = 344;

	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;

	private static final String TAG = "com.xiaoqf.app.BargainActivity";
	
	/** 是否已交订金 */
	private static boolean isBooked = false;
	private static ProgressDialog pDialog;


	// 手机注册红包
	private Discount discountCoupon;
	// 存款充值优惠
	private Discount discountDeposit;
	// 朋友帮砍价优惠
	private Discount discountFriend;
	// 支付宝充值支付信息
	private AliPayment aliPayment;
	// 微信充值支付信息
	private WeiXinPayment wxPayment;
	private IWXAPI msgApi;
	PayReq req;
	private StringBuilder sb;
	
	/** 选中的房间 */
	private Room room;
	/** 该房所在楼栋 */
	private House house;
	/** 选中房的楼盘信息 */
	private Project project;
	/** 当前订单 */
	private Order order;
	/** 看中的订单**/
	private myLikeRoomOrderBean myLikeRoomOrder;
	
	/** 楼盘Id */
	private String projectId;
	/** 房间Id */
	private String roomId;
	protected String orderId;
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();
				
				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					EventHandler.showToast(BargainActivity.this, "支付成功");
					// 更新订单详情相关组件
					getOrderDetail(orderId);

					Intent intent = new Intent(getApplicationContext(), BookPayedActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("Order", order);
					bundle.putSerializable("Project", project);
					bundle.putSerializable("House", house);
					bundle.putSerializable("Room", room);
					intent.putExtras(bundle);
					startActivity(intent);
					
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						EventHandler.showToast(BargainActivity.this, "支付结果确认中");

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						EventHandler.showToast(BargainActivity.this, "支付失败");
						isBooked = false;
					}
				}
				break;
			}
			case ORDER_NEW:
				orderId = order.getId();
				sb = new StringBuilder()
					.append(project.getName()).append('(')
					.append(house.getName()).append(room.getName()).append(')');
				titleTextView.setText(sb);
				
				//maxDisAmountInfoTextView.setText(
				//	project.getName() +"最高可优惠"+ order.getMaxDiscountAmount());
				originalAmountTextView.setText(
					CommonUtil.formatStr(order.getOriginalAmount(), 4));
				originalAmountTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG |Paint.ANTI_ALIAS_FLAG);//文字中间加划线
				amountTextView.setText(CommonUtil.formatStr(order.getAmount(), 4));
				myMagicText.setText(order.getUnAmount());
				myMagicText.startScroll(null, 0, Double.valueOf(order.getUnAmount()), "");
				setBargainBodyValue();
				closeDialog();
				
				break;
			case ORDER_DETAIL:
				orderId = order.getId();
				if((myLikeRoomOrder==null)&&(project!=null)&&(house!=null)&&(room!=null)) {
					sb = new StringBuilder()
						.append(project.getName()).append('(')
						.append(house.getName()).append(room.getName()).append(')');
					titleTextView.setText(sb);
				}
				
				//maxDisAmountInfoTextView.setText(
				//		myLikeRoomOrder.getProjectName() +"最高可优惠"+ order.getMaxDiscountAmount());
				originalAmountTextView.setText(CommonUtil.formatStr(order.getOriginalAmount(),4));
				originalAmountTextView.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG |Paint.ANTI_ALIAS_FLAG);//文字中间加划线
				amountTextView.setText(CommonUtil.formatStr(order.getAmount(),4));
				myMagicText.setText(order.getUnAmount());
				myMagicText.startScroll(null, 0, Double.valueOf(order.getUnAmount()), "");
				setBargainBodyValue();
				closeDialog();
				break;

			default:
				break;
			}
		}
	};
	
	// 界面相关组件
	@ViewInject(R.id.tv_title)
	private TextView titleTextView;
	
	// 订单优惠余额
	@ViewInject(R.id.activity_bargain_item_free_value)
	private MagicTextView myMagicText;

	// 楼盘最高优惠价格
	//@ViewInject(R.id.activity_bargain_item_free_value_max)
	//private TextView maxDisAmountInfoTextView;
	
	// 市场原价
	@ViewInject(R.id.activity_bargain_item_percent_value)
	private TextView originalAmountTextView;
	
	// 最终优惠价
	@ViewInject(R.id.activity_bargain_item_fianlvalue)
	private TextView amountTextView;  
	
	//获得红包Item
	@ViewInject(R.id.activity_bargain_wallet_text)
	private TextView walletTextView;  
	
	//已支付的钱
	@ViewInject(R.id.activity_bargain_wallet_text_gray)
	private TextView walletValueTextView; 
	
	//获得存优惠
	@ViewInject(R.id.activity_bargain_free_text)
	private TextView bargainFreeTextView;  
	
	//已存优惠
	@ViewInject(R.id.activity_bargain_free_text_gray)
	private TextView bargainFreeValueTextView; 
	
	//朋友砍价
	@ViewInject(R.id.activity_bargain_free_weixin_text)
	private TextView weixinBargainTextView;  
	
	//朋友砍价优惠
	@ViewInject(R.id.activity_bargain_free_weixin_text_gray)
	private TextView weixinBargainValueTextView; 
	
	// 支付订金按钮
	@ViewInject(R.id.activity_bargain_item_button_book)
	private RelativeLayout bookRelativeLayout;
	
	// 订金支付后续按钮
	@ViewInject(R.id.activity_bargain_items_after_book)
	private RelativeLayout afterbookRelativeLayout;
	
	// 砍价按钮
	@ViewInject(R.id.activity_bargain_item_button_wechat)
	private TextView wechatTextView;
	
	// 付全款按钮
	@ViewInject(R.id.activity_bargain_item_button_payall)
	private TextView payallTextView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bargain);
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
				myLikeRoomOrder = (myLikeRoomOrderBean) data.getSerializable("MyLikeRoomOrder");
				orderId = myLikeRoomOrder.getOrderID();
				if("1".equals(myLikeRoomOrder.getOrderHandleProcess())) {
					isBooked = false;
				}
				else
					isBooked = true;
			}
			if (null != data.getSerializable("BookedOrder")) {
				order = (Order) data.getSerializable("BookedOrder");
				orderId = order.getId();
				isBooked = true;
			}
//			if (!TextUtils.isEmpty(data.getString("projectId"))) {
//				projectId = data.getString("projectId");
//			}
//			if (!TextUtils.isEmpty(data.getString("roomId"))) {
//				roomId = data.getString("roomId");
//			}
		}
		
		init();
	}

	private void init() {
		//titleTextView.setText("小Q购房楼盘名");
		//titleTextView.setTextColor(0xff808080);
		//bargainTextView.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG |Paint.ANTI_ALIAS_FLAG);//文字中间加划线
		//bargainValueTextView.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG |Paint.ANTI_ALIAS_FLAG);//文字中间加划线
		if(myLikeRoomOrder!=null) {
			getOrderDetail(orderId);
			String titleName = 	myLikeRoomOrder.getProjectName()+"("+
					myLikeRoomOrder.getFloorNUm()+"栋"+
					myLikeRoomOrder.getRoomNum()+"房"+")";
			titleTextView.setText(titleName);
		} else if(null != order) {
			getOrderDetail(order.getId());
			sb = new StringBuilder()
				.append(project.getName()).append('(')
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
	 * @param url 服务器接口地址
	 * @param params
	 * @param what msg.what
	 */
	private void getOrder(String url, RequestParams params, final int what) {
		pDialog = EventHandler.showProgress(this, "正在更新信息……", false, false);
		HttpUtils http = new HttpUtils();
		CookieStore cookieStore = getCookieStore((DefaultHttpClient)http.getHttpClient());
		
		if(cookieStore!=null) {
			http.configCookieStore(cookieStore);
		}
		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LogUtil.log(TAG,"responseInfo.result:[" + responseInfo.result + "]");
				String info = responseInfo.result;
				try {
					JSONObject jsonObject = new JSONObject(info);
					if (!"1".equals(jsonObject.getString("status"))) {
						closeDialog();
						String errCode = jsonObject.getString("errorCode");
						String errMsg = PropertiesUtil.getProperties(getApplicationContext()).getProperty(errCode);
						EventHandler.showToast(BargainActivity.this, errMsg);
						if(isUserNeedLogin(errCode)) {
							skip2LoginActicity(BargainActivity.this);
						}
						return;
					}
					
					JSONObject jOrder = new JSONObject(jsonObject.getString("order"));
					order = new Order();
					order.setUnAmount(jOrder.getString("orderUnAmount"));
					order.setMaxDiscountAmount(jOrder.getString("maxDiscountAmount"));
					order.setUserId(jOrder.getString("userId"));
					order.setUserMobile(jOrder.getString("userMobile"));
					order.setCode(jOrder.getString("orderCode"));
					order.setAmount(jOrder.getString("orderAmount"));
					order.setCouponAmount(jOrder.getString("orderCouponAmount"));
					order.setOriginalAmount(jOrder.getString("orderOriginalAmount"));
					order.setCreateTime(jOrder.getString("createTime"));
					order.setOrderStatus(jOrder.getString("orderStatus"));
					// 充值金额
					order.setRoomChargeAmount(jOrder.getString("roomChargeAmount"));
					
//					orderId = Consts.isDebugMode ? Consts.orderId : jOrder.getString("orderId");
					orderId = jOrder.getString("orderId");
					order.setId(jOrder.getString("orderId"));
					
					JSONArray jDiscountList = new JSONArray(jOrder.getString("discountList"));
					int discountNum = jDiscountList.length();
					for (int idx = 0; idx < discountNum; idx++) {
						JSONObject jDiscount = jDiscountList.getJSONObject(idx);
						Discount aDiscount = new Discount();
						aDiscount.setOrderId(orderId);
						aDiscount.setName(jDiscount.getString("discName"));
						aDiscount.setStatus(jDiscount.getString("discStatus"));
						aDiscount.setOrderDiscAmount(jDiscount.getString("orderDiscAmount"));
						aDiscount.setOrderUnDiscAmount(jDiscount.getString("orderUnDiscAmount"));
						aDiscount.setMaxAmount(jDiscount.getString("maxDiscAmount"));
						aDiscount.setType(jDiscount.getString("discType"));
						
						switch (Integer.valueOf(jDiscount.getString("discType"))) {
						case Discount.DISCOUNT_COUPON:
							discountCoupon = aDiscount;
							break;
						case Discount.DISCOUNT_DEPOSIT:
							discountDeposit = aDiscount;
							if (Math.abs(Double.valueOf(aDiscount.getMaxAmount()) -
											Double.valueOf(aDiscount.getOrderDiscAmount())) < 0.01D) {
							/*if ( Math.abs(Double.valueOf(order.getRoomChargeAmount())) < 0.01D							
								|| Math.abs(Double.valueOf(aDiscount.getMaxAmount()) -
								Double.valueOf(aDiscount.getOrderDiscAmount())) < 0.01D) {*/
								isBooked = true;
							}
							break;
						case Discount.DISCOUNT_WECHAT:
							discountFriend = aDiscount;
							break;

						default:
							break;
						}
					}
					
					ArrayList<Discount> discounts = new ArrayList<Discount>();
					discounts.add(discountCoupon);
					discounts.add(discountDeposit);
					discounts.add(discountFriend);
					order.setDiscountList(discounts);
					
					mHandler.sendEmptyMessage(what);
				} catch (JSONException e) {
					closeDialog();
					LogUtil.logError(e);
					EventHandler.showToast(BargainActivity.this, "服务器忙，请稍后重试");
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				closeDialog();
				LogUtil.logError(error);
				EventHandler.showToast(BargainActivity.this, "服务器忙，请稍后重试");
			}
		});
	}
	
	/**
	 * 用户充值
	 * @param v
	 */
	@OnClick(R.id.activity_bargain_free)
	public void onDiscountDepositClick(View v) {
		// 支付方式选择框
		final PaymentDialog payDialog = PaymentDialog.getInstance(this);
		
		payDialog.onAlipayRlClick(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				payDialog.dismiss();
				alipay(v);
//				EventHandler.showToast(BargainActivity.this, "调用支付宝支付订金");
				
			}
		});
		
		payDialog.onWeixinRlClick(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				payDialog.dismiss();
				weixinPay(v);
//				EventHandler.showToast(BargainActivity.this, "调用微信支付订金");
				
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
//		payDialog.setCancelable(true);
		//payDialog.setCancelable(true);
		payDialog.show();
	}
	
	// 调用支付宝支付订金
	public void alipay(View v) {
		if (null == discountDeposit) {
			EventHandler.showToast(BargainActivity.this, "服务器忙，请稍后重试");
			return;
		}
		
		// 调用用户充值接口
		RequestParams params = new RequestParams();
		params.addBodyParameter("tokenId", Consts.tokenID);
		params.addBodyParameter("orderId", order.getId());
//		params.addBodyParameter("orderId", orderId);
		params.addBodyParameter("payType", String.valueOf(AliPayment.OPERATE_RECHARGE));

		HttpUtils http = new HttpUtils();
		CookieStore cookieStore = getCookieStore((DefaultHttpClient)http.getHttpClient());
		
		if(cookieStore!=null) {
			http.configCookieStore(cookieStore);
		}
		
		http.send(HttpRequest.HttpMethod.POST, Consts.SERVER_URL_RECHARGE_BY_ALIPAY, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LogUtil.log(TAG,"responseInfo.result:[" + responseInfo.result + "]");
				String info = responseInfo.result;
				
				try {
					JSONObject jsonObject = new JSONObject(info);
					if (!"1".equals(jsonObject.getString("status"))) {
						String errCode = jsonObject.getString("errorCode");
						String errMsg = PropertiesUtil.getProperties(getApplicationContext()).getProperty(errCode);
						EventHandler.showToast(BargainActivity.this, errMsg);
						
						if(isUserNeedLogin(errCode)) {
							skip2LoginActicity(BargainActivity.this);
						}
						return;
					}
					
					aliPayment = new AliPayment();
					aliPayment.setService(jsonObject.getString("service"));
					aliPayment.setPantner(jsonObject.getString("pantner"));
					aliPayment.setEncode(jsonObject.getString("_input_charset"));
					aliPayment.setEncrypttype(jsonObject.getString("sign_type"));
					aliPayment.setOrderId(jsonObject.getString("outTradeNo"));
					aliPayment.setSubject(jsonObject.getString("subject"));
					aliPayment.setType(jsonObject.getString("payment_type"));
					aliPayment.setSellerId(jsonObject.getString("seller_id"));
					aliPayment.setBody(jsonObject.getString("body"));
					aliPayment.setKey(jsonObject.getString("key"));
					
					String fee = Consts.isDebugMode ? Consts.PRICE_CENT : jsonObject.getString("total_fee");
					aliPayment.setTotalFee(fee);
					
					String url = jsonObject.getString("notify_url");
					if (!url.contains("http")) {
						url = Consts.SERVER_URL + url;
					}
					aliPayment.setUrl(url);
					
					// 调用支付宝充值
					String orderInfo = AlipayUtil.getOrderInfo(aliPayment);
					pay(orderInfo);
					
				} catch (JSONException e) {
					LogUtil.logError(e);
					EventHandler.showToast(BargainActivity.this, "服务器忙，请稍后重试");
				}
				
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				LogUtil.logError(error);
				EventHandler.showToast(BargainActivity.this, "服务器忙，请稍后重试");
			}
			
		});

	}
	
	// 调用微信支付订金
	public void weixinPay(View v) {
		if (null == discountDeposit) {
			EventHandler.showToast(BargainActivity.this, "服务器忙，请稍后重试");
			return;
		}
		
		// 调用用户充值接口
		RequestParams params = new RequestParams();
		params.addBodyParameter("tokenId", Consts.tokenID);
		params.addBodyParameter("orderId", order.getId());
		params.addBodyParameter("payType", String.valueOf(WeiXinPayment.OPERATE_RECHARGE));
		params.addBodyParameter("spbill_create_ip", CommonUtil.getLocalIpAddress());

		HttpUtils http = new HttpUtils();
		CookieStore cookieStore = getCookieStore((DefaultHttpClient)http.getHttpClient());
		
		if(cookieStore!=null) {
			http.configCookieStore(cookieStore);
		}
		
		http.send(HttpRequest.HttpMethod.POST, Consts.SERVER_URL_RECHARGE_BY_WXPAY, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LogUtil.log(TAG,"responseInfo.result:[" + responseInfo.result + "]");
				String info = responseInfo.result;
				
				try {
					JSONObject jsonObject = new JSONObject(info);
					if (!"1".equals(jsonObject.getString("status"))) {
						String errCode = jsonObject.getString("errorCode");
						String errMsg = PropertiesUtil.getProperties(getApplicationContext()).getProperty(errCode);
						EventHandler.showToast(BargainActivity.this, errMsg);
						
						if(isUserNeedLogin(errCode)) {
							skip2LoginActicity(BargainActivity.this);
						}
						return;
					}
					
					wxPayment = new WeiXinPayment();
					wxPayment.setAppid(jsonObject.getString("appid"));
					wxPayment.setPartnerid(jsonObject.getString("partnerid"));
					wxPayment.setPrepayid(jsonObject.getString("prepayid"));
					wxPayment.setPackAge(jsonObject.getString("packAge"));
					wxPayment.setNoncestr(jsonObject.getString("noncestr"));
					wxPayment.setTimestamp(jsonObject.getString("timestamp"));
					wxPayment.setSign(jsonObject.getString("sign"));
					
					
					Consts.book_order = order;
					Consts.project_wx = project;
					Consts.house_wx = house;
					Consts.room_wx = room;
					// 调用微信充值
					msgApi = WXAPIFactory.createWXAPI(BargainActivity.this, null);
					req = WeiXinpayUtil.genPayReq(wxPayment);
					msgApi.registerApp(wxPayment.getAppid());
					msgApi.sendReq(req);
					
				} catch (JSONException e) {
					LogUtil.logError(e);
					EventHandler.showToast(BargainActivity.this, "服务器忙，请稍后重试");
				}
				
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				LogUtil.logError(error);
				EventHandler.showToast(BargainActivity.this, "服务器忙，请稍后重试");
			}
			
		});

	}
	
	@OnClick(R.id.iv_title_left)
	public void onBargainBackClick(View v) {
		onBackPressed();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (null != order) {
			myMagicText.startScroll(null, 10000, Double.valueOf(order.getUnAmount()), "");
		}
		
		if (isBooked) {
			bookRelativeLayout.setVisibility(View.GONE);
			afterbookRelativeLayout.setVisibility(View.VISIBLE);
		} else {
			bookRelativeLayout.setVisibility(View.VISIBLE);
			afterbookRelativeLayout.setVisibility(View.GONE);
		}
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
				PayTask alipay = new PayTask(BargainActivity.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
		
	}
	
	//微信分享
	@OnClick(R.id.activity_bargain_free_weixin)
	public void onWeiXinShareClick(View v) {
/*		Intent intent = new Intent(this,WeiXinShareActivity.class);
		intent.putExtra("WeiXinShareKey", orderId);
		LogUtil.log(TAG, "orderID:"+orderId);
		startActivity(intent);*/
		ShareWeiXinDialog share = new ShareWeiXinDialog();
		share.init(this, orderId);
		share.showShareWXDialog();	
		//finish();
	}
	
	// 支付订金
	@OnClick(R.id.activity_bargain_item_button_book)
	public void onBookClick(View v) {
		onDiscountDepositClick(v);
	}
	
	// 邀好友砍价
	@OnClick(R.id.activity_bargain_item_button_wechat)
	public void onBargainByWechatClick(View v) {
		onWeiXinShareClick(v);
	}
	
	// 付全款
	@OnClick(R.id.activity_bargain_item_button_payall)
	public void onPayallClick(View v) {
		// TODO:付全款操作
		Intent intent = new Intent(this,FullPayConfirmActivity.class);
		startActivity(intent);
		finish();
	}
	
	private void setBargainBodyValue() {
		walletTextView.setText(order.getDiscountList().get(Discount.DISCOUNT_COUPON-1).getName());
		String walletValue =order.getDiscountList().get(Discount.DISCOUNT_COUPON-1).getOrderDiscAmount();
		if(walletValue!=null)
			walletValueTextView.setText("- "+CommonUtil.formatStr(walletValue,4));
		
		bargainFreeTextView.setText(order.getDiscountList().get(Discount.DISCOUNT_DEPOSIT-1).getName());
		walletValue =order.getDiscountList().get(Discount.DISCOUNT_DEPOSIT-1).getOrderDiscAmount();
		if(walletValue!=null)
			bargainFreeValueTextView.setText("- "+CommonUtil.formatStr(walletValue,4));
		
		weixinBargainTextView.setText(order.getDiscountList().get(Discount.DISCOUNT_WECHAT-1).getName());
		walletValue =order.getDiscountList().get(Discount.DISCOUNT_WECHAT-1).getOrderDiscAmount();
		if(walletValue!=null)
			weixinBargainValueTextView.setText("- "+CommonUtil.formatStr(walletValue,4));	
	}

	private static void closeDialog() {
		if (null != pDialog) {
			pDialog.dismiss();
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
