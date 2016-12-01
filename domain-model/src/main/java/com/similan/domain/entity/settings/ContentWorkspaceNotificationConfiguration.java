package com.similan.domain.entity.settings;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class ContentWorkspaceNotificationConfiguration {

  @Column
  private Boolean contentSpaceDocumentDownload = Boolean.TRUE;

  @Column
  private Boolean contentSpaceDocumentComment = Boolean.TRUE;

  @Column
  private Boolean contentSpaceDocumentAnnotation = Boolean.TRUE;

  @Column
  private Boolean contentSpaceDocumentViewed = Boolean.TRUE;

  @Column
  private Boolean contentSpaceParticipantJoined = Boolean.TRUE;

  @Column
  private Boolean contentSpaceDocumentUpload = Boolean.TRUE;

  @Column
  private Boolean contentSpaceDocumentCheckIn = Boolean.TRUE;

  @Column
  private Boolean contentSpaceDocumentCheckOut = Boolean.TRUE;

}
