package com.dapeng.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "slug_info", uniqueConstraints = {@UniqueConstraint(columnNames = "slug")})
public class SlugInfo implements Serializable {

	private static final long serialVersionUID = 74586927583429629L;

	@Id
	@GeneratedValue
	private Long id;

	private Long slug;

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
}
