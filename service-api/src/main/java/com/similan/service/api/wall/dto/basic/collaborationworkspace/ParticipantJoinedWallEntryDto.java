package com.similan.service.api.wall.dto.basic.collaborationworkspace;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

import lombok.ToString;

import com.similan.service.api.collaborationworkspace.dto.basic.CollaborationWorkspaceParticipationDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.api.wall.dto.key.WallEntryKey;

@ToString
public class ParticipantJoinedWallEntryDto extends
    CollaborationWorkspaceWallEntryDto {

  @XmlElement
  private CollaborationWorkspaceParticipationDto participation;

  protected ParticipantJoinedWallEntryDto() {
  }

  public ParticipantJoinedWallEntryDto(
      WallEntryKey<CollaborationWorkspaceKey> key, ActorDto initiator,
      Date date, CollaborationWorkspaceParticipationDto participation) {
    super(key, initiator, date);
    this.participation = participation;
  }

  public CollaborationWorkspaceParticipationDto getParticipation() {
    return participation;
  }

  @Override
  public WallEntryType getType() {
    return WallEntryType.COLLABORATION_WORKSPACE_PARTICIPANT_JOINED;
  }

}
