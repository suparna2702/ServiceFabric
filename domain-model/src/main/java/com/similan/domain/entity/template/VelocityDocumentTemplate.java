package com.similan.domain.entity.template;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity(name = "VelocityDocumentTemplate")
public class VelocityDocumentTemplate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column
	private String name;
	
	@Column(length=10000)
	private String templateContent;
	
	@Column(length=1000)
	private String templateHeader;
	
	@JoinColumn
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<VelocityTemplateAttribute> attributes;
	
	@Column
	@Enumerated(EnumType.STRING)
	private VelocityTemplateType templateType;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTemplateHeader() {
		return templateHeader;
	}

	public void setTemplateHeader(String templateHeader) {
		this.templateHeader = templateHeader;
	}

	public String getTemplateContent() {
		return templateContent;
	}

	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}

	public List<VelocityTemplateAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<VelocityTemplateAttribute> attributes) {
		this.attributes = attributes;
	}

	public VelocityTemplateType getTemplateType() {
		return templateType;
	}

	public void setTemplateType(VelocityTemplateType templateType) {
		this.templateType = templateType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
