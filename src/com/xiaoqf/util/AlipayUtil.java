package com.xiaoqf.util;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import com.xiaoqf.beans.AliPayment;

public class AlipayUtil {

	//商户PID
	public static final String PARTNER = "2088211142665516";
	//商户收款账号
	public static final String SELLER = "yanghaibo@shishuo.com";
	//商户私钥，pkcs8格式
	public static final String RSA_PRIVATE =
		"MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAN1jldvMQBQnKXmrNWCs2LQ4KvbhwUhneCET/rLffkL4lY/ztyKAmFJkavcEV1qufyFIoWC0RfM00cYHomyMNN6bC0fxignttlGl8qFxtZs+xz2kmJ89nxklrMRFFr0IUXCUnb+xZlsNW41KohTnVeNt5VbwZDWOzJDDZAJPBQt9AgMBAAECgYAVIT8ZVlo7Ui2MNkIG8xhPwxierZdUAMQfbBggHGKho7Yqohk1is5+9qSa5OgteIAbviHg5/PRLZZj7zCaaZtWAjyksXfhTIvLmCttZFx+7LilzxYZUnHySk5LbjorWoHma/x+qHZx+XqZkIvyXviZ2ycc4iAspmSQpfwhDLfBhQJBAPgktSCF0P17GvYWMA5WckKSeYslU6jF62VFGIVPZ2NOh4OcIeErSJUKUmEPPLDRzozFSQaDPEb1opa2kOnyFM8CQQDkZgYanwTSEGXgpT//ZBnAWNeXeBuiI4NxZDPvBmutOjS+3mSEWO5gEJqtGmYm7H57VyQWHmos+4SEzZHGSMXzAkEAwhIzdtQxt43ocD66JvyFMHvOg30WCRGCIRoBK4IGoixw4AXkmW54vdfsxexc0w3ENWS20Fkjc3v5RVZT0CR9EQJBAJTl4lLWmojsAc7LTIaN1q/r+1kXB0TTzCxPPAA2+lmrWZmZWIRjibv89ymkzTXQ9cApFjCJUYTT0BfYAJH4/XsCQQCw6WnpwrHB0vr4SxTYO1YuYnsIZi0eXezFEjXEHsCqz0WaDnQesyZ5ScNDfQGrNKb4iSYtF4VUjLrg51TGhdpe";
	//支付宝公钥
	public static final String RSA_PUBLIC =
		"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	

	private static final String ALGORITHM = "RSA";

	private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	private static final String DEFAULT_CHARSET = "UTF-8";
	
	public static String sign(String content, String privateKey) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(DEFAULT_CHARSET));

			byte[] signed = signature.sign();

			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public static String getOrderInfo(AliPayment pay) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + pay.getOrderId() + "\"";//getOutTradeNo()

		// 商品名称
		orderInfo += "&subject=" + "\"" + pay.getSubject() + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + pay.getBody() + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + pay.getTotalFee() + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + pay.getUrl() + "\"";
//		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public static String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public static String sign(String content) {
		return sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public static String getSignType() {
		return "sign_type=\"RSA\"";
	}

}
