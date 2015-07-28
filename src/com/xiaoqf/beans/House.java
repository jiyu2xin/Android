package com.xiaoqf.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @ClassName: House
 * @Description: 一栋楼的数据模型
 *
 */
public class House implements Serializable {

	/** 本栋建筑id */
	private String id;
	/** 栋名：一栋、A栋…… */
	private String name;
	/** 序号：1、2、3…… */
	private String inProjectNo;
	/** 本栋楼层 */
	private ArrayList<Floor> floors;
	/** 楼层数 */
	private int floorNum;
	/** 本栋一层房间最大数 */
	
	// 附属关系
	/** 开发商编号 */
	private String estateId;
	
	/**
	 * 构造函数
	 * @param floors
	 */
	public House(ArrayList<Floor> floors) {
		super();
		this.floors = floors;
	}

	public void add(Floor floor) {
		floors.add(floor);
	}
	
	public void clear() {
		floors.clear();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Floor> getFloors() {
		return floors;
	}

	public void setFloors(ArrayList<Floor> floors) {
		this.floors = floors;
	}

	public String getEstateId() {
		return estateId;
	}

	public void setEstateId(String estateId) {
		this.estateId = estateId;
	}

	public String getId() {
		return id;
	}

	public int getFloorNum() {
		return floors.size();
	}
	
	public String getInProjectNo() {
		return inProjectNo;
	}
	
	public void setInProjectNo(String inProjectNo) {
		this.inProjectNo = inProjectNo;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setFloorNum(int floorNum) {
		this.floorNum = floorNum;
	}
	
}
