package com.similan.domain.entity.settings;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class CollaborationWorkspaceNotificationConfiguration {

  @Column
  private Boolean collabSpaceDocumentShare = Boolean.TRUE;

  @Column
  private Boolean collabSpaceDocumentDownload = Boolean.TRUE;

  @Column
  private Boolean collabSpaceDocumentComment = Boolean.TRUE;

  @Column
  private Boolean collabSpaceDocumentAnnotation = Boolean.TRUE;

  @Column
  private Boolean collabSpaceDocumentViewed = Boolean.TRUE;

  @Column
  private Boolean collabSpaceParticipantJoined = Boolean.TRUE;
  
  @Column
  private Boolean collabSpaceUpdated = Boolean.TRUE;

}
