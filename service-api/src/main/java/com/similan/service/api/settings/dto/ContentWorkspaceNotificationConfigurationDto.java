package com.similan.service.api.settings.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ContentWorkspaceNotificationConfigurationDto implements
    Serializable {

  private static final long serialVersionUID = 1L;
  
  private Boolean contentSpaceDocumentDownload = Boolean.TRUE;

  private Boolean contentSpaceDocumentComment = Boolean.TRUE;

  private Boolean contentSpaceDocumentAnnotation = Boolean.TRUE;
  
  private Boolean contentSpaceDocumentUpload = Boolean.TRUE;
  
  private Boolean contentSpaceDocumentCheckIn = Boolean.TRUE;
  
  private Boolean contentSpaceDocumentCheckOut = Boolean.TRUE;

  private Boolean contentSpaceDocumentViewed = Boolean.TRUE;

  private Boolean contentSpaceParticipantJoined = Boolean.TRUE;

}
