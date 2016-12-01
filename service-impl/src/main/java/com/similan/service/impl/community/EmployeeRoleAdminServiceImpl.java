package com.similan.service.impl.community;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.community.EmployeeRole;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialEmployee;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.repository.community.SocialEmployeeRepository;
import com.similan.service.api.community.EmployeeRoleAdminService;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.basic.EmployeeRoleDto;
import com.similan.service.api.community.dto.basic.UpdateEmployeeRoleDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.impl.base.ServiceImpl;

@Service
public class EmployeeRoleAdminServiceImpl extends ServiceImpl implements
    EmployeeRoleAdminService {
  @Autowired
  private SocialEmployeeRepository socialEmployeeRepository;
  @Autowired
  private SocialActorMarshaller actorMarshaller;
  
  @Override
  @Transactional
  public List<EmployeeRoleDto> getAllRoles(SocialActorKey businessKey) {
    SocialActor employerActor = actorMarshaller
        .unmarshallActorKey(businessKey, true);

    List<SocialEmployee> employeeList = this.socialEmployeeRepository
        .findByContactFrom((SocialOrganization) employerActor);
    List<EmployeeRoleDto> retList = new ArrayList<EmployeeRoleDto>();

    for (SocialEmployee emp : employeeList) {
      SocialActor employee = emp.getContactTo();
      ActorDto employeeDto = actorMarshaller.marshallActor(employee);
      EmployeeRoleDto empRoleDto = new EmployeeRoleDto(employeeDto.getKey());
      empRoleDto.setEmployee(employeeDto);

      if (hasRole(emp.getRoles(), EmployeeRole.COLLABORATION_WORKSPACE_ADMIN)) {
        empRoleDto.setCollabWorkspaceAdmin(true);
      }

      if (hasRole(emp.getRoles(), EmployeeRole.MANAGEMENT_WORKSPACE_ADMIN)) {
        empRoleDto.setContentSpaceAdmin(true);
      }

      if (hasRole(emp.getRoles(), EmployeeRole.PARTNER_PROGRAM_ADMIN)) {
        empRoleDto.setPartnerProgramAdmin(true);
      }

      if (hasRole(emp.getRoles(), EmployeeRole.BUSINESS_ADMIN)) {
        empRoleDto.setBusinessAdmin(true);
      }

      retList.add(empRoleDto);

    }

    return retList;
  }

  @Override
  @Transactional
  public void updateRole(UpdateEmployeeRoleDto role) {
    SocialActor employee = actorMarshaller
        .unmarshallActorKey(role.getEmployee(), true);
    SocialActor employer = actorMarshaller
        .unmarshallActorKey(role.getEmployer(), true);
    SocialEmployee employment = this.socialEmployeeRepository
        .findByContactToAndContactFrom((SocialPerson) employee,
            (SocialOrganization) employer);
    Set<EmployeeRole> roles = employment.getRoles();

    // check each one and modify
    if (role.getCollabWorkspaceAdmin()) {
      // check whether role is there
      if (hasRole(roles, EmployeeRole.COLLABORATION_WORKSPACE_ADMIN) != true) {
        employment.getRoles().add(EmployeeRole.COLLABORATION_WORKSPACE_ADMIN);
      }
    } else {
      if (hasRole(roles, EmployeeRole.COLLABORATION_WORKSPACE_ADMIN)) {
        employment.getRoles()
            .remove(EmployeeRole.COLLABORATION_WORKSPACE_ADMIN);
      }
    }

    if (role.getContentSpaceAdmin()) {
      // check whether role is there
      if (hasRole(roles, EmployeeRole.MANAGEMENT_WORKSPACE_ADMIN) != true) {
        employment.getRoles().add(EmployeeRole.MANAGEMENT_WORKSPACE_ADMIN);
      }
    } else {
      if (hasRole(roles, EmployeeRole.MANAGEMENT_WORKSPACE_ADMIN)) {
        employment.getRoles().remove(EmployeeRole.MANAGEMENT_WORKSPACE_ADMIN);
      }
    }

    if (role.getPartnerProgramAdmin()) {
      // check whether role is there
      if (hasRole(roles, EmployeeRole.PARTNER_PROGRAM_ADMIN) != true) {
        employment.getRoles().add(EmployeeRole.PARTNER_PROGRAM_ADMIN);
      }
    } else {
      if (hasRole(roles, EmployeeRole.PARTNER_PROGRAM_ADMIN)) {
        employment.getRoles().remove(EmployeeRole.PARTNER_PROGRAM_ADMIN);
      }
    }

    this.socialEmployeeRepository.save(employment);
  }

  private Boolean hasRole(Set<EmployeeRole> roles, EmployeeRole roleType) {
    if (roles != null) {
      for (EmployeeRole role : roles) {
        if (role.equals(roleType)) {
          return true;
        }
      }
    }

    return false;
  }

}
