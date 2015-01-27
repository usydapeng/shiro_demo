package com.dapeng.core.shiro.authc;

import java.io.Serializable;

public class EnhanceUser implements Serializable {

	private static final long serialVersionUID = -7336389088639221141L;

	private Long userId;

	private Long userSlug;

	private String username;

	public EnhanceUser(Long userId, Long userSlug, String username){
		this.userId = userId;
		this.userSlug = userSlug;
		this.username = username;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserSlug() {
		return userSlug;
	}

	public void setUserSlug(Long userSlug) {
		this.userSlug = userSlug;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
