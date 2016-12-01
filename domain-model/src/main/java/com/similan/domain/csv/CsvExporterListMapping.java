package com.similan.domain.csv;

import java.util.Date;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name = "CsvExporterListMapping")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "mappingType",
		"name" }))
public class CsvExporterListMapping {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String name;
	
	@Enumerated(EnumType.ORDINAL)
	@Column
	private CsvExporterListMappingType mappingType;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<CsvExporterListMappingAttribute> attributeMapping;
	
	@Column(name = "creationDate", nullable = true)
	private Date creationDate;

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

	public CsvExporterListMappingType getMappingType() {
		return mappingType;
	}

	public void setMappingType(CsvExporterListMappingType mappingType) {
		this.mappingType = mappingType;
	}

	public List<CsvExporterListMappingAttribute> getAttributeMapping() {
		return attributeMapping;
	}

	public void setAttributeMapping(
			List<CsvExporterListMappingAttribute> attributeMapping) {
		this.attributeMapping = attributeMapping;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
}
