package com.dapeng.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "product_info")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductInfo extends AbstractEntity {

	private static final long serialVersionUID = -8714079346394547497L;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
