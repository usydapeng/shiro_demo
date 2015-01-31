package com.dapeng.service;

import java.io.Serializable;
import java.util.List;

public class SimpleUserInfo implements Serializable {

	private static final long serialVersionUID = -6314706101680391390L;

	private Long userId;

	private Long slug;

	private String username;

	private String password;

	private String nickname;

	private String school;

	private int userRole;

	private List<String> userPermissionList;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getSlug() {
		return slug;
	}

	public void setSlug(Long slug) {
		this.slug = slug;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public int getUserRole() {
		return userRole;
	}

	public void setUserRole(int userRole) {
		this.userRole = userRole;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getUserPermissionList() {
		return userPermissionList;
	}

	public void setUserPermissionList(List<String> userPermissionList) {
		this.userPermissionList = userPermissionList;
	}
}
