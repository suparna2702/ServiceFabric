package com.similan.domain.repository.partner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.partner.PartnerRelationshipCategory;
import com.similan.domain.entity.partner.PartnerRelationshipCategoryType;
import com.similan.domain.repository.partner.jpa.JpaPartnerRelationshipCategoryRepository;

@Repository
public class PartnerRelationshipCategoryRepository {
  @Autowired
  private JpaPartnerRelationshipCategoryRepository repository;
	
	public PartnerRelationshipCategory save(PartnerRelationshipCategory category) {
    return repository.save(category);
  }
	
	public List<PartnerRelationshipCategory> findAll() {
    return repository.findAll();
  }
	
	public PartnerRelationshipCategory create(PartnerRelationshipCategoryType relationshipCategoryType, 
			String relationshipDetails){
		PartnerRelationshipCategory category = new PartnerRelationshipCategory();
		category.setRelationshipCategoryType(relationshipCategoryType);
		category.setRelationshipDetails(relationshipDetails);
		
		return category;
	}

}
