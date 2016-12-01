package com.similan.domain.repository.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.settings.CollaborationWorkspaceNotificationConfiguration;
import com.similan.domain.entity.settings.ContentWorkspaceNotificationConfiguration;
import com.similan.domain.entity.settings.NotificationConfiguration;
import com.similan.domain.entity.settings.PartnerProgramNotificationConfiguration;
import com.similan.domain.entity.settings.ProfileNotificationConfiguration;
import com.similan.domain.repository.settings.jpa.JpaNotificationConfigurationRepository;

@Repository
public class NotificationConfigurationRepository {
  @Autowired
  private JpaNotificationConfigurationRepository repository;

  public NotificationConfiguration findByMember(SocialActor member) {
    return repository.findByMember(member);
  }

  public NotificationConfiguration save(NotificationConfiguration config) {
    return repository.save(config);
  }

  public NotificationConfiguration create(SocialActor member) {

    NotificationConfiguration retConfig = new NotificationConfiguration();
    retConfig.setMember(member);

    ProfileNotificationConfiguration profileConfig = new ProfileNotificationConfiguration();
    retConfig.setProfileNotification(profileConfig);

    CollaborationWorkspaceNotificationConfiguration collabWorkspaceConfig = new CollaborationWorkspaceNotificationConfiguration();
    retConfig.setCollabWorkspaceNotification(collabWorkspaceConfig);

    ContentWorkspaceNotificationConfiguration contentWorkspaceConfig = new ContentWorkspaceNotificationConfiguration();
    retConfig.setContentWorkspaceNotification(contentWorkspaceConfig);

    PartnerProgramNotificationConfiguration partnerConfig = new PartnerProgramNotificationConfiguration();
    retConfig.setPartnerProgramNotification(partnerConfig);
    return retConfig;
  }

}
