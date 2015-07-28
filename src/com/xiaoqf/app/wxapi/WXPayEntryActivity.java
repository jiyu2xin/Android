package com.xiaoqf.app.wxapi;


import net.sourceforge.simcpux.Constants;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xiaoqf.common.Consts;
import com.xiaoqf.util.EventHandler;
import com.xiaoqf.view.BargainNewActivity;
import com.xiaoqf.view.BookPayedActivity;
import com.xiaoqf.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{

	/**resp.errCode==  0 ：表示支付成功*/
	private static final int PAY_SUCESS = 0;
	/**resp.errCode== -1 ：表示支付失败*/
	private static final int PAY_FAILURE = -1;
	/** resp.errCode== -2 ：表示取消支付 */
	private static final int PAY_CANCEL = -2;
	
	private static final int MSG_PAY_SUCESS = 3220;
	private static final int MSG_PAY_FAILED = 3221;
	private static final int MSG_PAY_CANCEL = 3222;
	
	private static final String TAG = "com.xiaoqf.app.wxapi.WXPayEntryActivity";
//	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
    private Handler mHandler = new Handler() {
    	@Override
    	public void handleMessage(android.os.Message msg) {
    		switch (msg.what) {
			case MSG_PAY_SUCESS:
				// 支付成功处理订单相关信息
				BargainNewActivity.isBooked = true;
				Intent intent = new Intent(getApplicationContext(), BookPayedActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("Order", Consts.book_orderInfo);
//				bundle.putSerializable("Project", Consts.project_wx);
//				bundle.putSerializable("House", Consts.house_wx);
//				bundle.putSerializable("Room", Consts.room_wx);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
				break;
				
			case MSG_PAY_FAILED:
				// 支付失败
				
			case MSG_PAY_CANCEL:
				finish();
				break;

			default:
				break;
			}
    	};
    };
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	/**
	 * 支付后回调
	 */
	@Override
	public void onResp(BaseResp resp) {
		String state="";
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			/*AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.app_tip);
			builder.setMessage(getString(R.string.pay_result_callback_msg, resp.errStr +";code=" + String.valueOf(resp.errCode)));
			builder.show();*/
			switch (resp.errCode) {
			case PAY_SUCESS:
				state = "成功";
				break;
			case PAY_FAILURE:
				state = "失败";
				break;
			case PAY_CANCEL:
				state = "取消";
				break;

			default:
				break;
			}
			EventHandler.showToast(this, "微信支付"+ state);
		}
		
		// 支付成功
		if (resp.errCode == PAY_SUCESS) {
			mHandler.sendEmptyMessage(MSG_PAY_SUCESS);
		}
		// 支付失败
		if (resp.errCode == PAY_FAILURE) {
			mHandler.sendEmptyMessage(MSG_PAY_FAILED);
		}
		// 支付取消
		if (resp.errCode == PAY_CANCEL) {
			mHandler.sendEmptyMessage(MSG_PAY_CANCEL);
		}
	}
}