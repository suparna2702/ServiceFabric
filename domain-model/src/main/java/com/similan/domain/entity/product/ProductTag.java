package com.similan.domain.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.similan.domain.entity.common.IDomainEntity;
import com.similan.service.api.base.dto.key.EntityType;

@Entity(name="ProductTag")
public class ProductTag implements IDomainEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column(nullable = false, unique = true)
	protected String name;
	
	@ManyToOne
	@JoinColumn
	protected Product product;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.PRODUCT_TAG;
	}

}
