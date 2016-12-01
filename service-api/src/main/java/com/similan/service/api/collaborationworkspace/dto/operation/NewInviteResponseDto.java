package com.similan.service.api.collaborationworkspace.dto.operation;

import javax.xml.bind.annotation.XmlAttribute;

import com.similan.service.api.base.dto.operation.OperationDto;

public class NewInviteResponseDto extends OperationDto {

  @XmlAttribute
  private Boolean accepted;

  public NewInviteResponseDto() {
  }

  public NewInviteResponseDto(Boolean accepted) {
    super();
    this.accepted = accepted;
  }

  public Boolean getAccepted() {
    return accepted;
  }

}
