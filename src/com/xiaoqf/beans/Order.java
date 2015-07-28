package com.xiaoqf.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @ClassName: Order
 * @Description: 订单(优惠)信息的数据模型
 *
 */
public class Order implements Serializable {
	/** 待确认 */
	public static final int ORDER_STATE_CONFIRMING = 0;
	/** 预定 */
	public static final int ORDER_STATE_BOOK = 1;
	/** 确认 */
	public static final int ORDER_STATE_CONFIRMED = 9;
	/** 作废 */
	public static final int ORDER_STATE_OUT = -1;
	
	/** 订单ID */
	private String id;
	/** 订单主键 */
	private String code;
	/** 订单最终价格 */
	private String amount;
	/** 完成优惠金额 */
	private String couponAmount;
	/** 市场价格 */
	private String originalAmount;
	/** 还可优惠金额 */
	private String unAmount;
	/** 最高优惠金额 */
	private String maxDiscountAmount;
	/** 创建时间 */
	private String createTime;
	/** 订单状态 */
	private String orderStatus;
	/** 房间需要充值的金额（订金金额） */
	private String roomChargeAmount;
	/** 优惠列表 */
	private ArrayList<Discount> discountList;

	// 相关属性
	/** 用户ID */
	private String userId;
	/** 用户手机号 */
	private String userMobile;
	public String getUnAmount() {
		return unAmount;
	}
	public void setUnAmount(String unAmount) {
		this.unAmount = unAmount;
	}
	public String getMaxDiscountAmount() {
		return maxDiscountAmount;
	}
	public void setMaxDiscountAmount(String maxDiscountAmount) {
		this.maxDiscountAmount = maxDiscountAmount;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCouponAmount() {
		return couponAmount;
	}
	public void setCouponAmount(String couponAmount) {
		this.couponAmount = couponAmount;
	}
	public String getOriginalAmount() {
		return originalAmount;
	}
	public void setOriginalAmount(String originalAmount) {
		this.originalAmount = originalAmount;
	}
	public ArrayList<Discount> getDiscountList() {
		return discountList;
	}
	public void setDiscountList(ArrayList<Discount> discountList) {
		this.discountList = discountList;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserMobile() {
		return userMobile;
	}
	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getRoomChargeAmount() {
		return roomChargeAmount;
	}
	public void setRoomChargeAmount(String roomChargeAmount) {
		this.roomChargeAmount = roomChargeAmount;
	}
	
}
