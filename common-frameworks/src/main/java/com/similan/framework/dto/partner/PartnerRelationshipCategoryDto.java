package com.similan.framework.dto.partner;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.similan.domain.entity.partner.PartnerRelationshipCategoryType;

public class PartnerRelationshipCategoryDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id = Long.MIN_VALUE;
	
	private PartnerRelationshipCategoryType partnerRelationshipCategoryType = PartnerRelationshipCategoryType.Gold;
	
	private String customName = StringUtils.EMPTY;
	
	private String relationshipDetails;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PartnerRelationshipCategoryType getPartnerRelationshipCategoryType() {
		return partnerRelationshipCategoryType;
	}

	public void setPartnerRelationshipCategoryType(
			PartnerRelationshipCategoryType partnerRelationshipCategoryType) {
		this.partnerRelationshipCategoryType = partnerRelationshipCategoryType;
	}

	public String getCustomName() {
		return customName;
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	public String getRelationshipDetails() {
		return relationshipDetails;
	}

	public void setRelationshipDetails(String relationshipDetails) {
		this.relationshipDetails = relationshipDetails;
	}

	@Override
	public String toString() {
		return "PartnerRelationshipCategoryDto [id=" + id
				+ ", partnerRelationshipCategoryType="
				+ partnerRelationshipCategoryType + ", customName="
				+ customName + ", relationshipDetails=" + relationshipDetails
				+ "]";
	}
	
	
	
	

}
