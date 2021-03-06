package com.dapeng.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "user_account")
public class UserAccount extends AbstractEntity {

	private static final long serialVersionUID = -6682307067052631200L;

	private Long slug;

	@Column(unique = true)
	private String username;

	@Column(name = "passwd")
	private String password;

	private int userRole = 1;

	private boolean enabled = false;

	private boolean confirmed = false;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getUserRole() {
		return userRole;
	}

	public void setUserRole(int userRole) {
		this.userRole = userRole;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public Long getSlug() {
		return slug;
	}

	public void setSlug(Long slug) {
		this.slug = slug;
	}

	//没有持久化，没有在数据库中存在列
	@Transient
	public static boolean isInGroup(Integer userRole, Integer group){
		if(userRole != null){
			return 0 != (userRole & group);
		}
		return false;
	}

	public enum Role{

		ROLE_GUEST(1),

		ROLE_USER(1 << 1),

		ROLE_ADMIN(1 << 3),

		ROLE_EDITOR(1 << 4),

		ROLE_TEACHER(1 << 5);

		private final int id;

		Role(final int id){
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}

}
