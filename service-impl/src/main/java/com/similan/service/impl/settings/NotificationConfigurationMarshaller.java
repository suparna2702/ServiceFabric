package com.similan.service.impl.settings;

import org.springframework.stereotype.Component;

import com.similan.domain.entity.settings.CollaborationWorkspaceNotificationConfiguration;
import com.similan.domain.entity.settings.ContentWorkspaceNotificationConfiguration;
import com.similan.domain.entity.settings.NotificationConfiguration;
import com.similan.domain.entity.settings.PartnerProgramNotificationConfiguration;
import com.similan.domain.entity.settings.ProfileNotificationConfiguration;
import com.similan.service.api.settings.dto.CollaborationWorkspaceNotificationConfigurationDto;
import com.similan.service.api.settings.dto.ContentWorkspaceNotificationConfigurationDto;
import com.similan.service.api.settings.dto.PartnerProgramNotificationConfigurationDto;
import com.similan.service.api.settings.dto.ProfileNotificationConfigurationDto;
import com.similan.service.impl.Marshaller;

@Component
public class NotificationConfigurationMarshaller extends Marshaller {
  public ProfileNotificationConfigurationDto marshallProfileConfig(
      NotificationConfiguration config) {

    ProfileNotificationConfiguration profileNotification = config
        .getProfileNotification();
    ProfileNotificationConfigurationDto profileNotificationDto = new ProfileNotificationConfigurationDto();
    profileNotificationDto.setProfileUpdate(profileNotification
        .getProfileUpdate());

    return profileNotificationDto;
  }

  public ContentWorkspaceNotificationConfigurationDto marshallContentWorkspaceConfig(
      NotificationConfiguration config) {

    ContentWorkspaceNotificationConfiguration contentWorkspaceConfig = config
        .getContentWorkspaceNotification();

    ContentWorkspaceNotificationConfigurationDto contentWorkspaceConfigDto = new ContentWorkspaceNotificationConfigurationDto();
    contentWorkspaceConfigDto
        .setContentSpaceDocumentAnnotation(contentWorkspaceConfig
            .getContentSpaceDocumentAnnotation());
    contentWorkspaceConfigDto
        .setContentSpaceDocumentComment(contentWorkspaceConfig
            .getContentSpaceDocumentComment());
    contentWorkspaceConfigDto
        .setContentSpaceDocumentDownload(contentWorkspaceConfig
            .getContentSpaceDocumentDownload());
    contentWorkspaceConfigDto
        .setContentSpaceDocumentViewed(contentWorkspaceConfig
            .getContentSpaceDocumentViewed());
    contentWorkspaceConfigDto
        .setContentSpaceParticipantJoined(contentWorkspaceConfig
            .getContentSpaceParticipantJoined());
    contentWorkspaceConfigDto
        .setContentSpaceDocumentUpload(contentWorkspaceConfig
            .getContentSpaceDocumentUpload());
    contentWorkspaceConfigDto
        .setContentSpaceDocumentCheckIn(contentWorkspaceConfig
            .getContentSpaceDocumentCheckIn());
    contentWorkspaceConfigDto
        .setContentSpaceDocumentCheckOut(contentWorkspaceConfig
            .getContentSpaceDocumentCheckOut());

    return contentWorkspaceConfigDto;
  }

  public CollaborationWorkspaceNotificationConfigurationDto marshallCollabWorkspaceConfig(
      NotificationConfiguration config) {

    CollaborationWorkspaceNotificationConfiguration collabWorkspaceConfig = config
        .getCollabWorkspaceNotification();

    CollaborationWorkspaceNotificationConfigurationDto retDto = new CollaborationWorkspaceNotificationConfigurationDto();
    retDto.setCollabSpaceDocumentAnnotation(collabWorkspaceConfig
        .getCollabSpaceDocumentAnnotation());
    retDto.setCollabSpaceDocumentComment(collabWorkspaceConfig
        .getCollabSpaceDocumentComment());
    retDto.setCollabSpaceDocumentDownload(collabWorkspaceConfig
        .getCollabSpaceDocumentDownload());
    retDto.setCollabSpaceDocumentShare(collabWorkspaceConfig
        .getCollabSpaceDocumentShare());
    retDto.setCollabSpaceDocumentViewed(collabWorkspaceConfig
        .getCollabSpaceDocumentViewed());
    retDto.setCollabSpaceParticipantJoined(collabWorkspaceConfig
        .getCollabSpaceParticipantJoined());
    retDto.setCollabSpaceUpdated(collabWorkspaceConfig.getCollabSpaceUpdated());

    return retDto;
  }

  public PartnerProgramNotificationConfigurationDto marshallPartnerConfig(
      NotificationConfiguration config) {
    PartnerProgramNotificationConfiguration partnerConfig = config
        .getPartnerProgramNotification();
    PartnerProgramNotificationConfigurationDto retDto = new PartnerProgramNotificationConfigurationDto();
    retDto.setPartnerJoined(partnerConfig.getPartnerJoined());
    retDto.setPartnerProgramUpdate(partnerConfig.getPartnerProgramUpdate());

    return retDto;
  }

}
