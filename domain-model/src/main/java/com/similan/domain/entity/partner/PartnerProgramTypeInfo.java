package com.similan.domain.entity.partner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="PartnerProgramTypeInfo")
public class PartnerProgramTypeInfo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column
	private PartnerProgramType programType;
	
	@Column
	private String customProgramName;
	
	@Column(length=8000)
	private String programDetails;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomProgramName() {
		return customProgramName;
	}

	public void setCustomProgramName(String customProgramName) {
		this.customProgramName = customProgramName;
	}

	public PartnerProgramType getProgramType() {
		return programType;
	}

	public void setProgramType(PartnerProgramType programType) {
		this.programType = programType;
	}

	public String getProgramDetails() {
		return programDetails;
	}

	public void setProgramDetails(String programDetails) {
		this.programDetails = programDetails;
	}

}
