package com.similan.domain.repository.partner.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.partner.PartnerRelationshipCategory;

public interface JpaPartnerRelationshipCategoryRepository extends
		JpaRepository<PartnerRelationshipCategory, Long> {

}
