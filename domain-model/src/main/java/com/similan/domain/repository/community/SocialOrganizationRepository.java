package com.similan.domain.repository.community;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.OrganizationSearchPreference;
import com.similan.domain.entity.community.OrganizationType;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialContact;
import com.similan.domain.entity.community.SocialEmployee;
import com.similan.domain.entity.community.SocialMemberVisibilityType;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.repository.community.jpa.JpaSocialOrganizationRepository;
import com.similan.service.api.community.dto.basic.SocialEmployeeType;

@Repository
public class SocialOrganizationRepository {
  @Autowired
  private JpaSocialOrganizationRepository repository;

	public List<SocialOrganization> findAll() {
    return repository.findAll();
  }
	
	public List<SocialOrganization> findByName(String name) {
    return repository.findByName(name);
  }

	public SocialOrganization findOne(Long id) {
    return repository.findOne(id);
  }
	
	public Long getTotalBusinessCount() {
    return repository.getTotalBusinessCount();
  }

	public SocialOrganization save(SocialOrganization org) {
    return repository.save(org);
  }
	
	public SocialOrganization findByBusinessNameAndPrimaryEmail(
      String name, String email){
	  return repository.findByBusinessNameAndPrimaryEmail(name, email);
	}
	
	public SocialOrganization create(OrganizationType organizationType, String name) {
		SocialOrganization organization = new SocialOrganization();
		organization.setName(name);
		organization.setOrganizationType(organizationType);
		organization.setVisibilityType(SocialMemberVisibilityType.VisiblePublic);
		organization.setCreationDate(new Date());
		return organization;
	}
	
	public SocialEmployee createEmployee(SocialOrganization organization,
			SocialPerson person, SocialEmployeeType employeeType) {

		SocialEmployee employee = new SocialEmployee();
		employee.setContactFrom(organization);
		employee.setContactTo(person);
		employee.setEmployeeType(employeeType);
		employee.setCreated(new Date());

		return employee;
	}
	
	public SocialContact createContact(SocialOrganization owner, SocialActor contact) {
		SocialContact socialContact = new SocialContact();
		
		socialContact.setContactFrom((SocialActor)owner);
		socialContact.setContactTo(contact);
		socialContact.setCreated(new Date());

		return socialContact;
	}

	public OrganizationSearchPreference createSearchPreference() {
		OrganizationSearchPreference searchPreference = new OrganizationSearchPreference();
		return searchPreference;
	}

	public Long getOrganizationCountByType(OrganizationType type) {
    return repository.getOrganizationCountByType(type);
  }
	public SocialOrganization findOrgByName(String businessName) {
    return repository.findOrgByName(businessName);
  }
	public SocialOrganization findOrgByEmail(String inviteeEmail) {
    return repository.findOrgByEmail(inviteeEmail);
  }
	public List<SocialOrganization> findAllOrgByVisibilityType(SocialMemberVisibilityType visibility) {
    return repository.findAllOrgByVisibilityType(visibility);
  }
	
  public SocialOrganization findOne(String name) {
    return repository.findOne(name);
  }
  
  public SocialOrganization findByBusinessName(String businessName){
    return this.repository.findByBusinessName(businessName);
  }

}
