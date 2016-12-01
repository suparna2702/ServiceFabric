package com.similan.service.api.collaborationworkspace.dto.operation;

import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.operation.OperationDto;

public class UpdateCollaborationWorkspaceDto extends OperationDto {

  @XmlElement
  String logo;

  @XmlElement
  String description;

  @XmlElement
  private boolean showParticipants = true;

  @XmlElement
  private boolean showActivities = true;

  public UpdateCollaborationWorkspaceDto(String logo, String description,
      boolean showParticipants, boolean showActivities) {
    this.logo = logo;
    this.description = description;
    this.showParticipants = showParticipants;
    this.showActivities = showActivities;
  }

  public String getLogo() {
    return logo;
  }

  public String getDescription() {
    return description;
  }

  public boolean isShowParticipants() {
    return showParticipants;
  }

  public boolean isShowActivities() {
    return showActivities;
  }

}
