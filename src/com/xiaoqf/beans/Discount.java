package com.xiaoqf.beans;

import java.io.Serializable;

/**
 * 
 * @ClassName: Discount
 * @Description: 优惠信息的数据模型
 *
 */
public class Discount implements Serializable {
	/** 红包优惠 */
	public static final int DISCOUNT_COUPON = 1;
	/** 充值优惠 */
	public static final int DISCOUNT_DEPOSIT = 2;
	/** 微信优惠 */
	public static final int DISCOUNT_WECHAT = 3;
	
	/** 优惠名称 */
	private String name;
	/** 优惠类型（1：红包、2：充值、3：微信） */
	private String type;
	/** 优惠状态 */
	private String status;
	/** 最高优惠金额 */
	private String maxAmount;
	/** 完成优惠金额 */
	private String orderDiscAmount;
	/** 未完成优惠金额 */
	private String orderUnDiscAmount;

	// 父属性
	/** 订单号 */
	private String orderId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(String maxAmount) {
		this.maxAmount = maxAmount;
	}

	public String getOrderDiscAmount() {
		return orderDiscAmount;
	}

	public void setOrderDiscAmount(String orderDiscAmount) {
		this.orderDiscAmount = orderDiscAmount;
	}

	public String getOrderUnDiscAmount() {
		return orderUnDiscAmount;
	}

	public void setOrderUnDiscAmount(String orderUnDiscAmount) {
		this.orderUnDiscAmount = orderUnDiscAmount;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}
