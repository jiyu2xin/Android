package com.xiaoqf.beans;

import java.io.Serializable;

/**
 * 订单详情页
 * @author xiaoq
 *
 */
public class OrderInfo implements Serializable {
	
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 用户手机号码
	 */
	private String userMobile;
	/**
	 * 订单id
	 */
	private String orderId;
	/**
	 * 订单编号
	 */
	private String orderCode;
	/**
	 * 小Q价
	 */
	private String orderAmount;
	/**
	 * 原价
	 */
	private String orderOrininalAmout;
	/**
	 * 成功砍价金额
	 */
	private String orderCouponAmount;
	
	/**
	 * 最大优惠价
	 */
	private String maxDiscountAmount;
	/**
	 * 订单创建时间
	 */
	private String createTime;
	/**
	 * 订单状态
	 */
	private String orderStatus;
	/**
	 * 订单完成步骤
	 */
	private String orderComStep;
	/**
	 * 订单剩余时间
	 */
	private String orderSurplusTime;
	/**
	 * 最低价格
	 */
	private String roomLawAmount;
	/**
	 * 房间需要支付的金额
	 */
	private String roomChargeAmount;
	/**
	 * 楼盘名称
	 */
	private String projectName;
	/**
	 * 楼盘地址
	 */
	private String projectAddress;
	/**
	 * 房间面积
	 */
	private String roomSquare;
	/**
	 * 房间产权期限
	 */
	private String propertyRight;
	/**
	 * 房间类型（毛坯 精装）
	 */
	private String roomType;
	/**
	 * 房间户型
	 */
	private String roomLayout;
	/**
	 * 房间户型图片地址
	 */
	private String roomLayoutImage;
	/**
	 * 房间信息
	 */
	private String roomInfo;
	
	public OrderInfo() {
		super();
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
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getOrderOrininalAmout() {
		return orderOrininalAmout;
	}
	public void setOrderOrininalAmout(String orderOrininalAmout) {
		this.orderOrininalAmout = orderOrininalAmout;
	}
	public String getMaxDiscountAmount() {
		return maxDiscountAmount;
	}
	public void setMaxDiscountAmount(String maxDiscountAmount) {
		this.maxDiscountAmount = maxDiscountAmount;
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
	public String getOrderComStep() {
		return orderComStep;
	}
	public void setOrderComStep(String orderComStep) {
		this.orderComStep = orderComStep;
	}
	public String getOrderSurplusTime() {
		return orderSurplusTime;
	}
	public void setOrderSurplusTime(String orderSurplusTime) {
		this.orderSurplusTime = orderSurplusTime;
	}
	public String getRoomLawAmount() {
		return roomLawAmount;
	}
	public void setRoomLawAmount(String roomLawAmount) {
		this.roomLawAmount = roomLawAmount;
	}
	public String getRoomChargeAmount() {
		return roomChargeAmount;
	}
	public void setRoomChargeAmount(String roomChargeAmount) {
		this.roomChargeAmount = roomChargeAmount;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectAddress() {
		return projectAddress;
	}
	public void setProjectAddress(String projectAddress) {
		this.projectAddress = projectAddress;
	}
	public String getRoomSquare() {
		return roomSquare;
	}
	public void setRoomSquare(String roomSquare) {
		this.roomSquare = roomSquare;
	}
	public String getPropertyRight() {
		return propertyRight;
	}
	public void setPropertyRight(String propertyRight) {
		this.propertyRight = propertyRight;
	}
	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	public String getRoomLayout() {
		return roomLayout;
	}
	public void setRoomLayout(String roomLayout) {
		this.roomLayout = roomLayout;
	}
	public String getRoomLayoutImage() {
		return roomLayoutImage;
	}
	public void setRoomLayoutImage(String roomLayoutImage) {
		this.roomLayoutImage = roomLayoutImage;
	}
	public String getOrderCouponAmount() {
		return orderCouponAmount;
	}
	public void setOrderCouponAmount(String orderCouponAmount) {
		this.orderCouponAmount = orderCouponAmount;
	}
	public String getRoomInfo() {
		return roomInfo;
	}
	public void setRoomInfo(String roomInfo) {
		this.roomInfo = roomInfo;
	}
	
}
