package com.similan.service.internal.impl.event.processor.collaborationworkspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspaceParticipation;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.collaborationworkspace.CollaborationWorkspaceWallEntry;
import com.similan.domain.repository.collaborationworkspace.CollaborationWorkspaceParticipationRepository;
import com.similan.service.internal.api.event.io.collaborationworkspace.ParticipantJoinedEvent;

@Component
public class ParticipantJoinedEventProcessor extends
    CollaborationWorkspaceEventProcessor<ParticipantJoinedEvent> {
  @Autowired
  private CollaborationWorkspaceParticipationRepository participationRepository;

  @Override
  protected CollaborationWorkspaceWallEntry createWallEntry(
      ParticipantJoinedEvent event) {
    long participationId = event.getParticipationId();
    CollaborationWorkspaceParticipation participation = participationRepository
        .findOne(participationId);
    CollaborationWorkspace workspace = participation.getWorkspace();
    Wall wall = getWall(workspace);
    return getCollaborationWorkspaceWallEntryRepository()
        .createMemberJoinedEntry(wall, participation);
  }

}
