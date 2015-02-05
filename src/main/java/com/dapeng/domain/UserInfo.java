package com.dapeng.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "user_info")
@Entity
public class UserInfo extends BaseEntity {

	private static final long serialVersionUID = 2537245848408684926L;

	private Long userId;

	private String nickname;

	private int gender = 0;

	private String school;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public enum Gender {
		MALE(1),

		FEMALE(2),

		OTHERS(3);

		private final int id;

		Gender(final int id){
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}
}
