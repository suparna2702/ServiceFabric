package com.similan.service.api.collaborationworkspace.dto.operation;

import javax.xml.bind.annotation.XmlAttribute;

import com.similan.service.api.base.dto.operation.OperationDto;

public class NewCollaborationWorkspaceDto extends OperationDto {
  @XmlAttribute
  private String description;

  @XmlAttribute
  private Boolean partnerWorkspace = Boolean.FALSE;

  public NewCollaborationWorkspaceDto(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public Boolean getPartnerWorkspace() {
    return partnerWorkspace;
  }

  public void setPartnerWorkspace(Boolean partnerWorkspace) {
    this.partnerWorkspace = partnerWorkspace;
  }

}
