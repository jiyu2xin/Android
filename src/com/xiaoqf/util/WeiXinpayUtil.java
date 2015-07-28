package com.xiaoqf.util;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.xiaoqf.beans.WeiXinPayment;

public class WeiXinpayUtil {
	
	// 生成签名参数
	public static PayReq genPayReq(WeiXinPayment wxPayment) {
		PayReq req = new PayReq();
		req.appId = wxPayment.getAppid();
		req.partnerId = wxPayment.getPartnerid();
		req.prepayId = wxPayment.getPrepayid();
		req.packageValue = wxPayment.getPackAge();
		req.nonceStr = wxPayment.getNoncestr();
		req.timeStamp = wxPayment.getTimestamp();
		req.sign = wxPayment.getSign();
		
		return req;
	}

}
