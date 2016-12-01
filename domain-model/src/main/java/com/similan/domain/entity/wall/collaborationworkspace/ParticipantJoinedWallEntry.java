package com.similan.domain.entity.wall.collaborationworkspace;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "ParticipantJoinedWallEntry")
@DiscriminatorValue("COLLABORATION_WORKSPACE_PARTICIPANT_JOINED")
public class ParticipantJoinedWallEntry extends CollaborationWorkspaceWallEntry {

  public ParticipantJoinedWallEntry() {
  }

  public ParticipantJoinedWallEntry(int number, Date date) {
    super(WallEntryType.COLLABORATION_WORKSPACE_PARTICIPANT_JOINED, number,
        date);
    this.setShowWall(Boolean.TRUE);
  }
}
