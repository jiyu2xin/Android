package com.xiaoqf.beans;

import java.io.Serializable;

/**
 * 
 * @ClassName: Room
 * @Description: 房间的数据模型
 *
 */
public class Room implements Serializable {
	/** 不存在 */
	public static final int None = -1;
	/** 在售 */
	public static final int Normal = 0;
	/** 已售 */
	public static final int Sold = 1;
	/** 预售 */
	public static final int Presale = 2;
	/** 选定 */
	public static final int Checked= 3;

	/** 房间名 */
	private String name;
	/** 房间编号 */
	private String id;
	/** 销售状态 */
	private String sellState;
	/** 售价（选中时，获取） */
	private String price;
	
	// 附属关系
	/** 开发商编号 */
	private String estateId;
	/** 楼栋编号 */
	private String buildingId;
	/** 楼层编号，对应于行号 */
	private String floorNum;
	/** 所在层编号，对应于列号 */
	private String inFloorNum;
	/** 所在单元 */
	private String unit;

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSellState() {
		return sellState;
	}

	public void setSellState(String sellState) {
		this.sellState = sellState;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getEstateId() {
		return estateId;
	}

	public void setEstateId(String estateId) {
		this.estateId = estateId;
	}

	public String getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}

	public String getFloorNum() {
		return floorNum;
	}

	public void setFloorNum(String floorNum) {
		this.floorNum = floorNum;
	}

	public void setInFloorNum(String inFloorNum) {
		this.inFloorNum = inFloorNum;
	}

	public String getInFloorNum() {
		return inFloorNum;
	}
	
}
