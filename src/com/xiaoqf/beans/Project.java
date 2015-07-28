package com.xiaoqf.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @ClassName: Project
 * @Description: 楼盘对象(开发商的工程)的数据模型
 *
 */
public class Project implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5172109633708465667L;
	/** 楼盘编号 */
	private String id;
	/** 开发商编号 */
	private String estateId;
	/** 楼盘名称 */
	private String name;
	/** 楼盘图片 */
	private String imgUrl;
	/** 该楼盘栋数 */
	private String housesNum;
	/** 该楼盘的楼栋 */
	private ArrayList<House> project;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEstateId() {
		return estateId;
	}
	public void setEstateId(String estateId) {
		this.estateId = estateId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getHousesNum() {
		return housesNum;
	}
	public void setHousesNum(String housesNum) {
		this.housesNum = housesNum;
	}
	public ArrayList<House> getProject() {
		return project;
	}
	public void setProject(ArrayList<House> project) {
		this.project = project;
	}

}
