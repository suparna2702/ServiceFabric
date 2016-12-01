package com.similan.domain.entity.partner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="PartnerRelationshipCategory")
public class PartnerRelationshipCategory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column
	private PartnerRelationshipCategoryType relationshipCategoryType;
	
	@Column(length=1000)
	private String customName;
	
	@Column(length=8000)
	private String relationshipDetails;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PartnerRelationshipCategoryType getRelationshipCategoryType() {
		return relationshipCategoryType;
	}

	public void setRelationshipCategoryType(
			PartnerRelationshipCategoryType relationshipCategoryType) {
		this.relationshipCategoryType = relationshipCategoryType;
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

}
