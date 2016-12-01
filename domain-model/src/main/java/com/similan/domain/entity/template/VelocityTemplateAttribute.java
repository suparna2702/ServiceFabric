package com.similan.domain.entity.template;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "VelocityTemplateAttribute")
public class VelocityTemplateAttribute {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column
	private String name;
	
	@Column
	private String value;
	
	@Column
	@Enumerated(EnumType.STRING)
	private VelocityTemplateAttributeSourceType sourceType;

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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public VelocityTemplateAttributeSourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(VelocityTemplateAttributeSourceType sourceType) {
		this.sourceType = sourceType;
	}
	
}
