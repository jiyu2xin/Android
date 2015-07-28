package com.xiaoqf.view;

import java.io.File;

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
import com.xiaoqf.beans.User;
import com.xiaoqf.common.Consts;
import com.xiaoqf.util.CommonUtil;
import com.xiaoqf.util.EventHandler;
import com.xiaoqf.util.ImageConvertUtil;
import com.xiaoqf.util.LogUtil;
import com.xiaoqf.util.PropertiesUtil;
import com.xiaoqf.wxapi.ShareWeiXinDialog;
import com.xiaoqf.app.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @ClassName: MineActivity
 * @Description: 我的 界面
 * @version: 1.0
 * @author: wangbin
 * @Create: 2015-05-11
 */

public class MineActivity extends BaseActivity {

	@ViewInject(R.id.activity_mint_item_bg_image)
	ImageView myImageView;
	private ProgressDialog pDialog;
	private String TAG = "MYLIKES_ACTIVITY";
	private static final int REQUEST_CODE = 1;
	private static final int REQUEST_LOGAIN_OK = 2;
	private static final int REQUEST_REGISTER_OK = 3;

	private static final String cashTag = "TAG_CASH";
	private static final String walletTag = "TAG_WALLET";
	private Context context;

	@ViewInject(R.id.activity_mine_item_load_button)
	private TextView loadingTextView;
	/**
	 * 退出按钮区域
	 */
	@ViewInject(R.id.activity_mine_item_settings)
	private RelativeLayout rlExitLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mine);
		ViewUtils.inject(this);
		init();
	}

	private void init() {
		// Uri uri = checkFileIsExit();
		// if(uri!=null)
		// myImageView.setImageURI(uri);
		context = this;
		if (Consts.userMobile != null) {
			loadingTextView.setText(Consts.userMobile);
			rlExitLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Uri uri = checkFileIsExit();
		// if(uri!=null)
		// myImageView.setImageURI(uri);
	}

	@OnClick(R.id.activity_mine_item_mylikes)
	// 看中的房按钮响应
	private void myLikesRoomsButton(View v) {
		Intent intent = new Intent(this, MyLikesActivity.class);
		Bundle bundle = new Bundle();
		bundle.putBoolean(TAG, true);
		intent.putExtras(bundle);
		startActivity(intent);
		// finish();
	}

	@OnClick(R.id.activity_mine_item_mypurpse)
	// 我的钱包按钮响应
	private void myPurpseButton(View v) {
		// Intent intent = new Intent(this,ShareWeiXinActivity.class);
		// Intent intent = new Intent(this,WalletRecordActivity.class);
		// intent.putExtra("key", cashTag);
		Intent intent = new Intent(this, BuyGuideActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.activity_mine_item_extension)
	// 红包
	private void extensionButton(View v) {
		// Intent intent = new Intent(this,WalletRecordActivity.class);
		Intent intent = new Intent(this, AboutActivity.class);
		// intent.putExtra("key", walletTag);
		startActivity(intent);
	}

	@OnClick(R.id.activity_mine_item_extension1)
	// 意见反馈
	private void onAdvicesFeedBackClick(View v) {
		// Intent intent = new Intent(this,WalletRecordActivity.class);
		Intent intent = new Intent(this, AdvicesFeedBackActivity.class);
		// intent.putExtra("key", walletTag);
		startActivity(intent);
	}

	@OnClick(R.id.activity_mine_item_settings)
	// 设置按钮响应
	private void settingsButton(View v) {
		// Intent intent = new Intent(this,SettingsActivity.class);
		// startActivity(intent);
		// 防止重复点击
		if (CommonUtil.isFastDoubleClick())
			return;

		pDialog = EventHandler.showProgress(this, "安全退出中……", false, false);
		// 调用服务端“退出”接口
		RequestParams params = new RequestParams();
		params.addBodyParameter("userMobile", Consts.userMobile);
		params.addBodyParameter("tokenId", Consts.tokenID);
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, Consts.SERVER_URL_USER_LOGOUT,
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
										getApplicationContext()).getProperty(
										errCode);
								EventHandler.showToast(MineActivity.this,
										errMsg);
								return;
							}
							if ("1".equals(jsonObject.getString("status"))) {
								closeDialog();
								cookieStore.clear();

								EventHandler.showToast(MineActivity.this,
										"成功退出登录！");
								loadingTextView.setText("登录/注册");
								rlExitLayout.setVisibility(View.GONE);
								// CommonUtil.openActivity(MineActivity.this,MainActivity.class);
								User user = User.getUser();
								Consts.tokenID = null;
								user.setuId(null);
								Consts.userMobile = null;
								user.setPhone(null);
								setUser(user);
								// finish();
							}
							closeDialog();
						} catch (Exception e) {
							LogUtil.logError(e);
							closeDialog();
							EventHandler.showToast(MineActivity.this,
									"服务器忙，请稍后重试");

						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						LogUtil.logError(error);
						closeDialog();
						EventHandler.showToast(MineActivity.this, "服务器忙，请稍后重试");

					}
				});
	}

	@OnClick(R.id.activity_mint_item_bg_image)
	// 改变头像 head sculpture
	private void onMyHeadSculptureClick(View v) {
		// Intent intent = new Intent(this,UserInfoActivity.class);
		// startActivityForResult(intent, REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == 0) {
			if (requestCode == REQUEST_CODE) {
				Uri uri = checkFileIsExit();
				if (uri != null) {
					Bitmap bitmap = ImageConvertUtil.decodeUriAsBitmap(this,
							uri);
					myImageView.setImageBitmap(bitmap);
					LogUtil.log(TAG,
							"activity result from  onMyHeadSculptureClick!");
				}

			}
			if (requestCode == REQUEST_LOGAIN_OK) {
				if (Consts.userMobile != null) {
					loadingTextView.setText(Consts.userMobile);
					rlExitLayout.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	private Uri checkFileIsExit() {
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/xiaoQfang", "user.png");
		if (file.exists()) {
			Uri imageUri = Uri.fromFile(file);
			return imageUri;
		}
		return null;

	}

	// 登陆或注册
	@OnClick(R.id.activity_mine_item_load_button)
	private void onLoadingOrRegisterClick(View v) {
		if (CommonUtil.isFastDoubleClick())
			return;

		if (Consts.tokenID == null) {
			Intent intent = new Intent(context, LoginFreeRegisterActivity.class);
			startActivityForResult(intent, REQUEST_LOGAIN_OK);
		} else
			EventHandler.showToast(this, "您已登录！");
	}

	@Override
	public void onBackPressed() {
		finish();
		// super.onBackPressed();
	}

	private void closeDialog() {
		if (null != pDialog) {
			pDialog.dismiss();
		}
	}
}
