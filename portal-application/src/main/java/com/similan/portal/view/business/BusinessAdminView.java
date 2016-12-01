package com.similan.portal.view.business;

import java.util.List;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;
import com.similan.service.api.community.dto.basic.EmployeeRoleDto;
import com.similan.service.api.community.dto.basic.UpdateEmployeeRoleDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

@Scope("view")
@Component("businessAdminView")
@Slf4j
public class BusinessAdminView extends BaseView {

  private static final long serialVersionUID = 1L;

  @Autowired(required = true)
  private MemberInfoDto member = null;

  @Autowired
  private OrganizationDetailInfoDto orgInfo;

  private List<EmployeeRoleDto> employeeRoleList;

  @PostConstruct
  public void init() {
    log.info("Initializing the employee role admin view");
    employeeRoleList = this.employeeRoleAdminService.getAllRoles(this
        .getOrgKey(orgInfo));
  }

  public List<EmployeeRoleDto> getEmployeeRoleList() {
    return employeeRoleList;
  }

  public void updateEmployeeRoleProfile(SocialActorKey employee,
      boolean contentSpaceAdmin, boolean collabWorkspaceAdmin,
      boolean partnerProgramAdmin) {
    
    UpdateEmployeeRoleDto updateRole = new UpdateEmployeeRoleDto();
    updateRole.setCollabWorkspaceAdmin(collabWorkspaceAdmin);
    updateRole.setContentSpaceAdmin(contentSpaceAdmin);
    updateRole.setEmployer(this.getOrgKey(orgInfo));
    updateRole.setEmployee(employee);
    updateRole.setPartnerProgramAdmin(partnerProgramAdmin);
    
    log.info("Updating member rights " + updateRole.toString());
    
    this.employeeRoleAdminService.updateRole(updateRole);
  }

}
