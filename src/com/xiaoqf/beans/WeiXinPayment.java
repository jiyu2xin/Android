package com.xiaoqf.beans;

/**
 * 
 * @ClassName: WeiXinPayment
 * @Description: 微信支付信息的数据模型
 *
 */
public class WeiXinPayment {
	/** 充值操作 */
	public static final int OPERATE_RECHARGE = 0;
	/** 支付操作 */
	public static final int OPERATE_PAY = 1;
	
	/** 微信APPID */
	private String appid;
	/** 商户ID */
	private String partnerid;
	/** 微信生成的预会话标识 */
	private String prepayid; 
	/** 暂填写固定值Sign=WXPay */
	private String packAge; 
	/** 随机字符串 */
	private String noncestr;
	/** 时间戳 */
    private String timestamp;
	/** 签名 */
	private String sign;
	
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getPartnerid() {
		return partnerid;
	}
	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}
	public String getPrepayid() {
		return prepayid;
	}
	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}
	public String getPackAge() {
		return packAge;
	}
	public void setPackAge(String packAge) {
		this.packAge = packAge;
	}
	public String getNoncestr() {
		return noncestr;
	}
	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}

}
