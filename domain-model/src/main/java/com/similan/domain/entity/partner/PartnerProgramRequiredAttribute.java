package com.similan.domain.entity.partner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PartnerProgramRequiredAttribute {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "partnerProgramAttrType")
	private PartnerProgramRequiredAttributeType attributeType;
	
	@Column
	@Enumerated(EnumType.STRING)
	private PartnerPreQualificationQuestionRequired requiredAttribute = PartnerPreQualificationQuestionRequired.Required;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PartnerProgramRequiredAttributeType getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(PartnerProgramRequiredAttributeType attributeType) {
		this.attributeType = attributeType;
	}

	public Boolean getRequired() {
		if(requiredAttribute.equals(PartnerPreQualificationQuestionRequired.Required) == true){
			return true;
		}
		else {
			return false;
		}
	}

	public void setRequired(Boolean required) {
		if(required == true){
			requiredAttribute =  PartnerPreQualificationQuestionRequired.Required;
		}
		else {
			requiredAttribute =  PartnerPreQualificationQuestionRequired.NotRequired;
		}
	}

	public PartnerPreQualificationQuestionRequired getRequiredAttribute() {
		return requiredAttribute;
	}

	public void setRequiredAttribute(PartnerPreQualificationQuestionRequired requiredAttribute) {
		this.requiredAttribute = requiredAttribute;
	}
	
}
