package com.similan.domain.entity.community;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "ProductCatalogueInfo")
public class ProductCatalogueInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column
	private String catalogueName;
	
	@Column
	private String supplierName;
	
	@Column
	private String catalogueCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCatalogueName() {
		return catalogueName;
	}

	public void setCatalogueName(String catalogueName) {
		this.catalogueName = catalogueName;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getCatalogueCode() {
		return catalogueCode;
	}

	public void setCatalogueCode(String catalogueCode) {
		this.catalogueCode = catalogueCode;
	}
}
