package com.xiaoqf.beans;

import java.io.Serializable;

import android.text.TextUtils;

/**
 * @ClassName: User
 * @Description: 用户登录信息实体
 * @version: 1.0
 * @author: 
 * @Create: 2014-7-19
 */
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private String uId; // 用户编号
	private String userName; // 会员名
	private String sId; // session编号
	private String phone;	// 手机号
	private String avatar; // 头像地址

	private volatile static User user;
	
	private User() {}
	
	public static User getUser() {
		if (user == null) {
			synchronized (User.class) {
				if (user == null) {
					user = new User();
				}
			}
		}
		return user;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		if (!TextUtils.isEmpty(avatar)) {
			avatar = avatar.replace("\\", "");
		}
		this.avatar = avatar;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getsId() {
		sId = sId.replace("+", "@");
		return sId;
	}

	public void setsId(String sId) {
		this.sId = sId;
	}

}
