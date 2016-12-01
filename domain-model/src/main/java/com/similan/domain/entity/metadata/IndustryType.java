package com.similan.domain.entity.metadata;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name="InductryType")
@XmlRootElement(name = "IndustryType")
public class IndustryType implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column
	private String insdustryName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlAttribute(name = "industryName")
	public String getInsdustryName() {
		return insdustryName;
	}

	public void setInsdustryName(String insdustryName) {
		this.insdustryName = insdustryName;
	}
	
}
