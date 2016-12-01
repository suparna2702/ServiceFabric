package com.similan.domain.entity.settings;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

import com.similan.domain.entity.community.SocialActor;

@Getter
@Setter
@Entity(name = "NotificationConfiguration")
public class NotificationConfiguration {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;
  
  @ManyToOne
  @JoinColumn
  private SocialActor member;

  @Embedded
  private ProfileNotificationConfiguration profileNotification;

  @Embedded
  private CollaborationWorkspaceNotificationConfiguration collabWorkspaceNotification;

  @Embedded
  private ContentWorkspaceNotificationConfiguration contentWorkspaceNotification;

  @Embedded
  private PartnerProgramNotificationConfiguration partnerProgramNotification;

}
