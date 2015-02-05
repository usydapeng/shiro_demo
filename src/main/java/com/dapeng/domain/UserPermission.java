package com.dapeng.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_permission")
public class UserPermission extends BaseEntity {

	private static final long serialVersionUID = -8242001086734050872L;

	private Long userId;

	private String permission;

	private boolean enabled;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
