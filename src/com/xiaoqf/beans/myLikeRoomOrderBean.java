package com.xiaoqf.beans;

import java.io.Serializable;

public class myLikeRoomOrderBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7196140352919115614L;
	/** 楼盘名称 */
	private String projectName;
	/** 订单号 */
	private String orderNum;
	/** 订单id */
	private String orderID;
	/** 栋 */
	private String floorNUm;
	/** 房间编号 */
	private String roomNum;
	/** 房间id */
	private String roomID;
	/** 优惠金额 */
	private String orderUnAmount;
	/** 完成任务百分比 */
	private String taskComplementPercent;
	/** 原价 */
	private String originalPrice;
	/** 小Q价　*/
	private String orderMaxFreeAmount;
	/** 创建时间 */
	private String taskCreateTime;
	/** 订单有效天数 */
	private String orderValidDays;
	/** logo图片URL */
	private String logoImageUrl;
	/** 订单状态 */
	private String orderStatus;
	/** 订单流程 */
	private String orderHandleProcess;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getFloorNUm() {
		return floorNUm;
	}

	public void setFloorNUm(String floorNUm) {
		this.floorNUm = floorNUm;
	}

	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}

	public String getRoomID() {
		return roomID;
	}

	public void setRoomID(String roomID) {
		this.roomID = roomID;
	}

	public String getOrderUnAmount() {
		return orderUnAmount;
	}

	public void setOrderUnAmount(String orderUnAmount) {
		this.orderUnAmount = orderUnAmount;
	}

	public String getTaskComplementPercent() {
		return taskComplementPercent;
	}

	public void setTaskComplementPercent(String taskComplementPercent) {
		this.taskComplementPercent = taskComplementPercent;
	}

	public String getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}

	public String getTaskCreateTime() {
		return taskCreateTime;
	}

	public void setTaskCreateTime(String taskCreateTime) {
		this.taskCreateTime = taskCreateTime;
	}

	public String getOrderValidDays() {
		return orderValidDays;
	}

	public void setOrderValidDays(String orderValidDays) {
		this.orderValidDays = orderValidDays;
	}

	public String getLogoImageUrl() {
		return logoImageUrl;
	}

	public void setLogoImageUrl(String logoImageUrl) {
		this.logoImageUrl = logoImageUrl;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderHandleProcess() {
		return orderHandleProcess;
	}

	public void setOrderHandleProcess(String orderHandleProcess) {
		this.orderHandleProcess = orderHandleProcess;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getOrderMaxFreeAmount() {
		return orderMaxFreeAmount;
	}
	
	public void setOrderMaxFreeAmount(String orderMaxFreeAmount) {
		this.orderMaxFreeAmount = orderMaxFreeAmount;
	}
}
