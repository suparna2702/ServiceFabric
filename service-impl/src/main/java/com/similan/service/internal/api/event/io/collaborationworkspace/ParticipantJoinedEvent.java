package com.similan.service.internal.api.event.io.collaborationworkspace;

public class ParticipantJoinedEvent extends CollaborationWorkspaceEvent {

  private static final long serialVersionUID = 1L;

  private long participationId;

  public ParticipantJoinedEvent(long participationId) {
    this.participationId = participationId;
  }

  public long getParticipationId() {
    return participationId;
  }

}
