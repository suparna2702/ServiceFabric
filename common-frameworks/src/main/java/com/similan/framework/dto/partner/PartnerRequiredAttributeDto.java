package com.similan.framework.dto.partner;

import java.io.Serializable;
import java.util.UUID;

import com.similan.domain.entity.partner.PartnerProgramRequiredAttributeType;

public class PartnerRequiredAttributeDto implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	private Long id = new Long(0);
	
	private PartnerProgramRequiredAttributeType nameType;
	
	private String uuId;
	
	private String answer;
	
	public PartnerRequiredAttributeDto(){
		id = Long.MIN_VALUE;
		uuId = UUID.randomUUID().toString();
	}
	
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}


	public String getUuId() {
		return uuId;
	}

	public void setUuId(String uuId) {
		this.uuId = uuId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PartnerProgramRequiredAttributeType getNameType() {
		return nameType;
	}

	public void setNameType(PartnerProgramRequiredAttributeType nameType) {
		this.nameType = nameType;
	}

	public String getName() {
		return nameType.toString();
	}

	public void setName(String name) {
		this.nameType = PartnerProgramRequiredAttributeType.valueOf(name);
	}
	
	public String toString(){
		return nameType.toString();
	}
}
