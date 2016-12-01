package com.similan.service.impl.settings;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.settings.CollaborationWorkspaceNotificationConfiguration;
import com.similan.domain.entity.settings.ContentWorkspaceNotificationConfiguration;
import com.similan.domain.entity.settings.NotificationConfiguration;
import com.similan.domain.entity.settings.PartnerProgramNotificationConfiguration;
import com.similan.domain.entity.settings.ProfileNotificationConfiguration;
import com.similan.domain.repository.settings.NotificationConfigurationRepository;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.settings.NotificationConfigurationService;
import com.similan.service.api.settings.dto.CollaborationWorkspaceNotificationConfigurationDto;
import com.similan.service.api.settings.dto.ContentWorkspaceNotificationConfigurationDto;
import com.similan.service.api.settings.dto.PartnerProgramNotificationConfigurationDto;
import com.similan.service.api.settings.dto.ProfileNotificationConfigurationDto;
import com.similan.service.impl.base.ServiceImpl;
import com.similan.service.impl.community.SocialActorMarshaller;

@Slf4j
@Service
public class NotificationConfigurationServiceImpl extends ServiceImpl implements
    NotificationConfigurationService {
  @Autowired
  private NotificationConfigurationRepository notificationConfigurationRepository;
  @Autowired
  private SocialActorMarshaller actorMarshaller;
  @Autowired
  private NotificationConfigurationMarshaller notificationConfigurationMarshaller;

  private NotificationConfiguration getConfig(SocialActorKey memberKey) {
    SocialActor member = actorMarshaller.unmarshallActorKey(
        memberKey, true);

    log.info("Getting notification config for member "
        + member.getDisplayName() + " id " + member.getId());

    NotificationConfiguration notificationConfig = notificationConfigurationRepository
        .findByMember(member);

    if (notificationConfig == null) {
      notificationConfig = notificationConfigurationRepository.create(member);
      notificationConfigurationRepository.save(notificationConfig);
    }

    return notificationConfig;
  }

  @Override
  @Transactional
  public ProfileNotificationConfigurationDto update(
      ProfileNotificationConfigurationDto configDto, SocialActorKey actorKey) {

    log.info("Updating profile notification config " + configDto
        + " for member " + actorKey);

    NotificationConfiguration notificationConfig = this.getConfig(actorKey);
    ProfileNotificationConfiguration partnerConfig = notificationConfig
        .getProfileNotification();
    partnerConfig.setProfileUpdate(configDto.getProfileUpdate());

    this.notificationConfigurationRepository.save(notificationConfig);

    ProfileNotificationConfigurationDto retDto = notificationConfigurationMarshaller
        .marshallProfileConfig(notificationConfig);
    return retDto;
  }

  @Override
  @Transactional
  public CollaborationWorkspaceNotificationConfigurationDto update(
      CollaborationWorkspaceNotificationConfigurationDto config,
      SocialActorKey actorKey) {

    log.info("Updating collab workspace notification config " + config
        + " for member " + actorKey);

    NotificationConfiguration notificationConfig = this.getConfig(actorKey);
    CollaborationWorkspaceNotificationConfiguration collabWorkspaceConfig = notificationConfig
        .getCollabWorkspaceNotification();
    collabWorkspaceConfig.setCollabSpaceDocumentAnnotation(config
        .getCollabSpaceDocumentAnnotation());
    collabWorkspaceConfig.setCollabSpaceDocumentComment(config
        .getCollabSpaceDocumentComment());
    collabWorkspaceConfig.setCollabSpaceDocumentDownload(config
        .getCollabSpaceDocumentDownload());
    collabWorkspaceConfig.setCollabSpaceDocumentShare(config
        .getCollabSpaceDocumentShare());
    collabWorkspaceConfig.setCollabSpaceDocumentViewed(config
        .getCollabSpaceDocumentViewed());
    collabWorkspaceConfig.setCollabSpaceParticipantJoined(config
        .getCollabSpaceParticipantJoined());
    collabWorkspaceConfig.setCollabSpaceUpdated(config.getCollabSpaceUpdated());

    this.notificationConfigurationRepository.save(notificationConfig);

    CollaborationWorkspaceNotificationConfigurationDto retConfig = notificationConfigurationMarshaller
        .marshallCollabWorkspaceConfig(notificationConfig);

    return retConfig;
  }

  @Override
  @Transactional
  public ContentWorkspaceNotificationConfigurationDto update(
      ContentWorkspaceNotificationConfigurationDto config,
      SocialActorKey actorKey) {

    log.info("Updating content workspace notification config " + config
        + " for member " + actorKey);

    NotificationConfiguration notificationConfig = this.getConfig(actorKey);
    ContentWorkspaceNotificationConfiguration contentWorkspaceConfig = notificationConfig
        .getContentWorkspaceNotification();

    contentWorkspaceConfig.setContentSpaceDocumentAnnotation(config
        .getContentSpaceDocumentAnnotation());
    contentWorkspaceConfig.setContentSpaceDocumentComment(config
        .getContentSpaceDocumentComment());
    contentWorkspaceConfig.setContentSpaceDocumentDownload(config
        .getContentSpaceDocumentDownload());
    contentWorkspaceConfig.setContentSpaceDocumentViewed(config
        .getContentSpaceDocumentViewed());
    contentWorkspaceConfig.setContentSpaceParticipantJoined(config
        .getContentSpaceParticipantJoined());
    contentWorkspaceConfig.setContentSpaceDocumentUpload(config
        .getContentSpaceDocumentUpload());
    contentWorkspaceConfig.setContentSpaceDocumentCheckIn(config
        .getContentSpaceDocumentCheckIn());
    contentWorkspaceConfig.setContentSpaceDocumentCheckOut(config
        .getContentSpaceDocumentCheckOut());

    this.notificationConfigurationRepository.save(notificationConfig);

    ContentWorkspaceNotificationConfigurationDto retDto = notificationConfigurationMarshaller
        .marshallContentWorkspaceConfig(notificationConfig);

    return retDto;
  }

  @Override
  @Transactional
  public PartnerProgramNotificationConfigurationDto update(
      PartnerProgramNotificationConfigurationDto config,
      SocialActorKey actorKey) {

    log.info("Updating partner notification config " + config + " for member "
        + actorKey);

    NotificationConfiguration notificationConfig = this.getConfig(actorKey);
    PartnerProgramNotificationConfiguration partnerConfig = notificationConfig
        .getPartnerProgramNotification();
    partnerConfig.setPartnerJoined(config.getPartnerJoined());
    partnerConfig.setPartnerProgramUpdate(config.getPartnerProgramUpdate());

    this.notificationConfigurationRepository.save(notificationConfig);

    PartnerProgramNotificationConfigurationDto retDto = notificationConfigurationMarshaller
        .marshallPartnerConfig(notificationConfig);

    return retDto;
  }

  @Override
  @Transactional
  public ProfileNotificationConfigurationDto getProfileConfig(
      SocialActorKey actorKey) {

    log.info("Getting profile notification config for member " + actorKey);

    NotificationConfiguration notificationConfig = this.getConfig(actorKey);

    ProfileNotificationConfigurationDto retDto = notificationConfigurationMarshaller
        .marshallProfileConfig(notificationConfig);

    return retDto;
  }

  @Override
  @Transactional
  public CollaborationWorkspaceNotificationConfigurationDto getCollabWorkspaceConfig(
      SocialActorKey actorKey) {

    log.info("Getting collab workspace notification config for member "
        + actorKey);

    NotificationConfiguration notificationConfig = this.getConfig(actorKey);

    CollaborationWorkspaceNotificationConfigurationDto retDto = notificationConfigurationMarshaller
        .marshallCollabWorkspaceConfig(notificationConfig);

    return retDto;
  }

  @Override
  @Transactional
  public ContentWorkspaceNotificationConfigurationDto getContentWorkspaceConfig(
      SocialActorKey actorKey) {

    log.info("Getting content workspace notification config for member "
        + actorKey);

    NotificationConfiguration notificationConfig = this.getConfig(actorKey);

    ContentWorkspaceNotificationConfigurationDto retDto = notificationConfigurationMarshaller
        .marshallContentWorkspaceConfig(notificationConfig);
    return retDto;
  }

  @Override
  @Transactional
  public PartnerProgramNotificationConfigurationDto getPartnerProgramConfig(
      SocialActorKey actorKey) {

    log.info("Getting partner program notification config for member "
        + actorKey);

    NotificationConfiguration notificationConfig = this.getConfig(actorKey);

    PartnerProgramNotificationConfigurationDto retDto = notificationConfigurationMarshaller
        .marshallPartnerConfig(notificationConfig);
    return retDto;
  }

}
