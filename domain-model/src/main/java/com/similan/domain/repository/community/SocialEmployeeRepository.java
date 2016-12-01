package com.similan.domain.repository.community;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.EmployeeRole;
import com.similan.domain.entity.community.SocialEmployee;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.repository.community.jpa.JpaSocialEmployeeRepository;
import com.similan.service.api.community.dto.basic.SocialEmployeeType;

@Repository
public class SocialEmployeeRepository {
  @Autowired
  private JpaSocialEmployeeRepository repository;
	
	public List<SocialEmployee> findAll() {
    return repository.findAll();
  }
	
	public SocialEmployee findOne(Long id) {
    return repository.findOne(id);
  }
	
	public SocialEmployee save(SocialEmployee employement) {
    return repository.save(employement);
  }
	
	public void delete(SocialOrganization organization, SocialPerson employee) {
    repository.delete(organization, employee);
  }
	
	public List<SocialEmployee> findByContactTo(SocialPerson actor){
	  return repository.findByContactTo(actor);
	}
	
	public List<SocialEmployee> findByContactFrom(SocialOrganization actor) {
    return repository.findByContactFrom(actor);
  }
	
	public List<SocialEmployee> findByContactFromAndRolesIn(SocialOrganization business,
      Set<EmployeeRole> roles){
	  return repository.findByContactFromAndRolesIn(business, roles);
	}
	
	public SocialEmployee create(SocialEmployeeType emplyeeType,
		      SocialOrganization org, SocialPerson person, Set<EmployeeRole> roles){
	  
		SocialEmployee employee = new SocialEmployee();
		employee.setEmployeeType(emplyeeType);
		employee.setContactFrom(org);
		employee.setCreated(new Date());
		employee.setContactTo(person);
		employee.setRoles(roles);
		employee.setVisible(true);
		
		//also make sure to set persons employer
		person.setEmployer(employee);
		return employee;
	}

  public SocialEmployee findByContactToAndContactFromAndRolesIn(
      SocialPerson employee, SocialOrganization business,
      Set<EmployeeRole> roles) {
    return repository.findByContactToAndContactFromAndRolesIn(employee, business, roles);
  }
  
  public SocialEmployee findByContactToAndContactFrom(
      SocialPerson employee, SocialOrganization business) {
    return repository.findByContactToAndContactFrom(employee, business);
  }
}
