package com.similan.service.api.collaborationworkspace.dto.basic;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceParticipationKey;

public class CollaborationWorkspaceParticipationDto extends
    KeyHolderDto<CollaborationWorkspaceParticipationKey> {

  @XmlAttribute
  private Date joinDate;

  protected CollaborationWorkspaceParticipationDto() {
  }

  public CollaborationWorkspaceParticipationDto(
      CollaborationWorkspaceParticipationKey key, Date joinDate) {
    super(key);
    this.joinDate = joinDate;
  }

  public Date getJoinDate() {
    return joinDate;
  }
}
