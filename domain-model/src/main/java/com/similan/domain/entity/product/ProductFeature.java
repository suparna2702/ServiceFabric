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

@Entity(name="ProductFeature")
public class ProductFeature implements IDomainEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String domainId;
	
	@Column(nullable = false, unique = true)
	private String name;
	
	@Column(length=2000)
	private String description;
	
	@ManyToOne
	@JoinColumn
	private Product owner;

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public Product getOwner() {
		return owner;
	}

	public void setOwner(Product owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.PRODUCT_FEATURE;
	}

}
