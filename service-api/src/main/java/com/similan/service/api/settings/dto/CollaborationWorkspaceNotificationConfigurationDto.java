package com.similan.service.api.settings.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CollaborationWorkspaceNotificationConfigurationDto implements
    Serializable {

  private static final long serialVersionUID = 1L;
  
  private Boolean collabSpaceDocumentShare = Boolean.TRUE;

  private Boolean collabSpaceDocumentDownload = Boolean.FALSE;

  private Boolean collabSpaceDocumentComment = Boolean.FALSE;

  private Boolean collabSpaceDocumentAnnotation = Boolean.FALSE;

  private Boolean collabSpaceDocumentViewed = Boolean.FALSE;

  private Boolean collabSpaceParticipantJoined = Boolean.TRUE;
  
  private Boolean collabSpaceDocumentUpdated = Boolean.FALSE;
  
  private Boolean collabSpaceUpdated = Boolean.TRUE;

}
