package com.xiaoqf.beans;

/**
 * 
 * @ClassName: Payment
 * @Description: 支付宝支付信息的数据模型。
 * 暂用来描述支付宝支付状态
 *
 */
public class AliPayment {
	/** 充值操作 */
	public static final int OPERATE_RECHARGE = 0;
	/** 支付操作 */
	public static final int OPERATE_PAY = 1;
	/** 支付宝支付 */
	public static final int TYPE_ALIPAY = 1;
	
	/** 接口名称 */
	private String service;
	/** 合作者身份ID:支付宝商户PID */
	private String pantner;
	/** 参数编码字符集_input_charset */
	private String encode;
	/** 加密类型sign_type:RSA */
	private String encrypttype;
	/** 服务器异步通知页面路径:notify_url */
	private String url;
	/** 商户网站唯一订单号:支付宝的outTradeNo*/
	private String orderId;
	/** 商品名称 */
	private String subject;
	/** 支付类型:payment_type 1表示支付宝 */
	private String type;
	/** 卖家支付宝账号:seller_id */
	private String sellerId;
	/** 支付宝总金额:total_fee */
	private String totalFee;
	/** 支付宝商品描述 */
	private String body;
	/** 支付宝私钥 */
	private String key;
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getPantner() {
		return pantner;
	}
	public void setPantner(String pantner) {
		this.pantner = pantner;
	}
	public String getEncode() {
		return encode;
	}
	public void setEncode(String encode) {
		this.encode = encode;
	}
	public String getEncrypttype() {
		return encrypttype;
	}
	public void setEncrypttype(String encrypttype) {
		this.encrypttype = encrypttype;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public String getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

}
