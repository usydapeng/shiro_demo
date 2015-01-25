package com.dapeng.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "user_info")
@Entity
public class UserInfo implements Serializable {

	private static final long serialVersionUID = -1995613001970300707L;

	@Id
	@GeneratedValue
	private Long id;

	private Long slug;

	private String nickname;

	private int gender = 0;

	private String school;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSlug() {
		return slug;
	}

	public void setSlug(Long slug) {
		this.slug = slug;
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
