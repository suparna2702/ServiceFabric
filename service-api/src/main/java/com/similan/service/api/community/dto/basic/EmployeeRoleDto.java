package com.similan.service.api.community.dto.basic;

import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

public class EmployeeRoleDto extends
                KeyHolderDto<SocialActorKey> {
  
  protected EmployeeRoleDto(){
    
  }
  
  public EmployeeRoleDto(SocialActorKey actorKey){
    super(actorKey);
  }

  @XmlElement
  ActorDto employee;

  @XmlElement
  private Boolean businessAdmin = Boolean.FALSE;

  @XmlElement
  private Boolean contentSpaceAdmin = Boolean.FALSE;

  @XmlElement
  private Boolean collabWorkspaceAdmin = Boolean.FALSE;

  @XmlElement
  private Boolean partnerProgramAdmin = Boolean.FALSE;
  
  public ActorDto getEmployee() {
    return employee;
  }

  public void setEmployee(ActorDto employee) {
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

}
