package com.similan.domain.entity.community;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.search.annotations.DocumentId;

@Entity
public class OrganizationSearchPreference {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "memberId")
	@DocumentId
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column
	private OrganizationSearchPreferenceType searchPreferenceType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OrganizationSearchPreferenceType getSearchPreferenceType() {
		return searchPreferenceType;
	}

	public void setSearchPreferenceType(
			OrganizationSearchPreferenceType searchPreferenceType) {
		this.searchPreferenceType = searchPreferenceType;
	}
}
