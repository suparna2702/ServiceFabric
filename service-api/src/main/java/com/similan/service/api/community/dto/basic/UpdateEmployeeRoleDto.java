package com.similan.service.api.community.dto.basic;

import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.operation.OperationDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

public class UpdateEmployeeRoleDto extends OperationDto {

  @XmlElement
  SocialActorKey employee;
  
  @XmlElement
  SocialActorKey employer;

  @XmlElement
  private Boolean businessAdmin = Boolean.FALSE;

  @XmlElement
  private Boolean contentSpaceAdmin = Boolean.FALSE;

  @XmlElement
  private Boolean collabWorkspaceAdmin = Boolean.FALSE;

  @XmlElement
  private Boolean partnerProgramAdmin = Boolean.FALSE;

  public SocialActorKey getEmployee() {
    return employee;
  }

  public void setEmployee(SocialActorKey employee) {
    this.employee = employee;
  }

  public Boolean getBusinessAdmin() {
    return businessAdmin;
  }

  public void setBusinessAdmin(Boolean businessAdmin) {
    this.businessAdmin = businessAdmin;
  }

  public Boolean getContentSpaceAdmin() {
    return contentSpaceAdmin;
  }

  public void setContentSpaceAdmin(Boolean contentSpaceAdmin) {
    this.contentSpaceAdmin = contentSpaceAdmin;
  }

  public Boolean getCollabWorkspaceAdmin() {
    return collabWorkspaceAdmin;
  }

  public void setCollabWorkspaceAdmin(Boolean collabWorkspaceAdmin) {
    this.collabWorkspaceAdmin = collabWorkspaceAdmin;
  }

  public Boolean getPartnerProgramAdmin() {
    return partnerProgramAdmin;
  }

  public void setPartnerProgramAdmin(Boolean partnerProgramAdmin) {
    this.partnerProgramAdmin = partnerProgramAdmin;
  }

  public SocialActorKey getEmployer() {
    return employer;
  }

  public void setEmployer(SocialActorKey employer) {
    this.employer = employer;
  }
  
  

}
