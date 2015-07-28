package com.xiaoqf.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @ClassName: Floor
 * @Description: 楼层的数据模型
 *
 */
public class Floor implements Serializable {

	/** 本层房间数 */
	private int roomNum;
	/** 本层编号，对应于行号 */
	private String floorNum;
	/** 房间列表 */
	private ArrayList<Room> floor;
	
	// 附属关系
	/** 开发商编号 */
	private String estateId;
	/** 楼栋编号 */
	private String buildingId;

	public void setFloorNum(String floorNum) {
		this.floorNum = floorNum;
	}

	public int getRoomNum() {
		return floor.size();
	}

	public String getFloorNum() {
		return floorNum;
	}

	public void setFloor(ArrayList<Room> floor) {
		this.floor = floor;
	}

	public ArrayList<Room> getFloor() {
		return floor;
	}

	public String getEstateId() {
		return estateId;
	}

	public String getBuildingId() {
		return buildingId;
	}

	public Room getRoom(int roomNum) {
		if (roomNum > this.floor.size() || roomNum < 0) {
			return new Room();
		}
		return this.floor.get(roomNum);
	}

}
