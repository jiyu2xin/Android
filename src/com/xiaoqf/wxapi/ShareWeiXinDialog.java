package com.xiaoqf.wxapi;

import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xiaoqf.common.AsyncImageLoader;
import com.xiaoqf.common.Consts;
import com.xiaoqf.util.EventHandler;
import com.xiaoqf.util.LogUtil;
import com.xiaoqf.util.PropertiesUtil;
import com.xiaoqf.view.BaseActivity;
import com.xiaoqf.app.R;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShareWeiXinDialog {
	protected static final String TAG = "ShareWeiXinDialog";
	// public ProgressDialog progressDialog = null;
	private final static int SHARE_WEIXIN = 1;
	private boolean isServerQuestOK = false;
	private IWXAPI wxApi;
	private Context context;
	private String Url;
	// private String imageUrl;
	private String title;
	private Bitmap bitmap = null;
	private String orderId = null;
	private int shareFlag = -1;
	private Dialog dlg;

	public void init(Context con, String orderId) {
		// 实例化
		context = con;
		this.orderId = orderId;
		wxApi = WXAPIFactory.createWXAPI(con, Consts.APP_ID);
		wxApi.registerApp(Consts.APP_ID);
		requestServerData();
	}

	private void share2Friends(View v) {
		// 在需要分享的地方添加代码：
		if (isServerQuestOK)
			wechatShare(0);// 分享到微信好友
		else
			setShareFlag(0);// 服务器数据未加载请求完成
	}

	private void share2FriendsCircle(View v) {
		// 在需要分享的地方添加代码：
		if (isServerQuestOK)
			wechatShare(1);// 分享到微信朋友圈
		else
			setShareFlag(1);// 服务器数据未加载请求完成
	}

	private void setShareFlag(int flag) {
		shareFlag = flag;
	}

	private boolean isShareFlagValid() {
		if ((shareFlag == 0) || (shareFlag == 1))
			return true;
		else
			return false;
	}

	/**
	 * 微信分享 （这里仅提供一个分享网页的示例，其它请参看官网示例代码）
	 * 
	 * @param flag
	 *            (0:分享到微信好友，1：分享到微信朋友圈)
	 */
	private void wechatShare(int flag) {

		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = Url;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = title;
		msg.description = "小Q购房，让买房更直接";

		// 图片资源
		Bitmap thumb = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ic_xiaoq_iv);
		msg.setThumbImage(thumb);
		// bitmap = dowmloadImage(imageUrl);
		// if(bitmap!=null)
		// msg.setThumbImage(bitmap);

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession
				: SendMessageToWX.Req.WXSceneTimeline;
		wxApi.sendReq(req);
	}

	private void requestServerData() {
		// progressDialog = ProgressDialog.show(context, "", "加载中...");
		// 调用服务端登陆接口
		RequestParams params = new RequestParams();
		params.addBodyParameter("orderId", orderId);
		params.addBodyParameter("tokenId", Consts.tokenID);
		LogUtil.log(TAG, "tokenId:" + Consts.tokenID + ",orderId" + orderId);

		HttpUtils http = new HttpUtils();
		DefaultHttpClient dc = new DefaultHttpClient();
		dc = (DefaultHttpClient) http.getHttpClient();
		CookieStore cs = dc.getCookieStore();
		cs.clear();
		List<Cookie> cookies = ((CookieStore) BaseActivity.cookieStore)
				.getCookies();
		for (Cookie cookie : cookies)
			cs.addCookie(cookie);

		if (cs != null) {
			http.configCookieStore(cs);
		}

		http.send(HttpRequest.HttpMethod.POST, Consts.SERVER_URL_WEIXIN_SHARE,
				params, new RequestCallBack<String>() {
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
										context.getApplicationContext())
										.getProperty(errCode);
								EventHandler.showToast(context, errMsg);
								// progressDialog.dismiss();
								isServerQuestOK = false;
								cancleDialog();
								return;
							}
							if ("1".equals(jsonObject.getString("status"))) {
								// progressDialog.dismiss();
								Url = jsonObject.getString("url");
								// imageUrl = jsonObject.getString("image");
								title = jsonObject.getString("title");
								isServerQuestOK = true;
								mHandler.sendEmptyMessage(SHARE_WEIXIN);
							}

							// setResult(RESULT_OK, intent);
						} catch (Exception e) {
							LogUtil.logError(e);
							// progressDialog.dismiss();
							isServerQuestOK = false;
							cancleDialog();
							EventHandler.showToast(context, "服务器忙，请稍后重试");

						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						LogUtil.logError(error);
						// progressDialog.dismiss();
						EventHandler.showToast(context, "服务器忙，请稍后重试");

					}
				});
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHARE_WEIXIN:
				if (isShareFlagValid())
					wechatShare(shareFlag);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	@SuppressLint("NewApi")
	public void showShareWXDialog() {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.activity_shareweixin, null);

		final ImageView image = (ImageView) layout
				.findViewById(R.id.dialog_share_item_iv_weixin);
		final ImageView image1 = (ImageView) layout
				.findViewById(R.id.dialog_share_item_iv_weixin1);
		TextView mCancleTextView = (TextView) layout
				.findViewById(R.id.dialog_share_item_iv_tv_cancle);

		mCancleTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				cancleDialog();
			}
		});
		// set a large value put it in bottom
		image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				share2Friends(v);
			}
		});

		image1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				share2FriendsCircle(v);
			}
		});

		dlg = new Dialog(context, R.style.AlertDialogCustom);
		Window dialogWindow = dlg.getWindow();
		// 设置位置
		dialogWindow.setGravity(Gravity.BOTTOM);
		// 设置dialog的宽高属性

		dlg.setContentView(layout);
		dialogWindow.setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		dlg.show();
		// new
		// AlertDialog.Builder(context,R.style.AlertDialogCustom).setView(layout).show();
	}

	private void cancleDialog() {
		if (dlg != null)
			dlg.dismiss();
	}

	private Bitmap dowmloadImage(String url) {

		if (!"".equals(url)) {
			try {
				AsyncImageLoader loader = new AsyncImageLoader(context);
				loader.setCache2File(true);
				loader.setCachedDir(context.getCacheDir().getAbsolutePath());
				loader.downloadImage(url, true,
						new AsyncImageLoader.ImageCallback() {
							@Override
							public void onImageLoaded(Bitmap bt, String imageUrl) {
								if (bt != null) {
									LogUtil.log("FloorAdapter", imageUrl);
									bitmap = bt;

								} else {
									// 下载失败，设置默认图片
									LogUtil.log("FloorAdapter", "下载失败!");
									bitmap = null;
								}
							}

						});
				// new ImageUtil(image).execute(landBean.getLogourl());
			} catch (Exception e) {
				LogUtil.logError(e);
				bitmap = null;
			}
		}
		return bitmap;
	}
}