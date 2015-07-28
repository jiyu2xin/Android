package com.xiaoqf.beans;

import java.util.ArrayList;

/**
 * 
 * @ClassName: Estate
 * @Description: 开发商对象的数据模型
 *
 */
public class Estate {
	/** 开发商名称 */
	private String name;
	/** 开发商编号 */
	private String estateId;
	/** 开发商图片 */	
	private String imageUrl;
	/** 楼盘编号*/
	private String propertyId;
	/** 开发商的楼盘列表 */
	private ArrayList<Project> projects;

	public Estate() {
		// super();
	}
	
	public Estate(String name, String imageUri) {
		this.name = name;
		this.imageUrl = imageUri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEstateId() {
		return estateId;
	}

	public void setEstateId(String estateId) {
		this.estateId = estateId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUri) {
		this.imageUrl = imageUri;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}



}
