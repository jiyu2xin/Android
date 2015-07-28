package com.xiaoqf.wxapi;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.xiaoqf.common.Consts;
import com.xiaoqf.util.EventHandler;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
	
	// IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
    	api = WXAPIFactory.createWXAPI(this, Consts.APP_ID, false);
        api.handleIntent(getIntent(), this);
        super.onCreate(savedInstanceState);
    }

	/*@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
        api.handleIntent(intent, this);
	}*/

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			//goToGetMsg();		
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			//goToShowMsg((ShowMessageFromWX.Req) req);
			break;
		default:
			break;
		}
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		String result = null;
		
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = "ok";
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "用户取消";
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "用户拒绝";
			break;
		default:
			result = "";
			break;
		}
		
		EventHandler.showToast(this, result);
	}
	
}