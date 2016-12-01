package com.similan.service.api.settings;

import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.settings.dto.CollaborationWorkspaceNotificationConfigurationDto;
import com.similan.service.api.settings.dto.ContentWorkspaceNotificationConfigurationDto;
import com.similan.service.api.settings.dto.PartnerProgramNotificationConfigurationDto;
import com.similan.service.api.settings.dto.ProfileNotificationConfigurationDto;

public interface NotificationConfigurationService {

  public ProfileNotificationConfigurationDto update(
      ProfileNotificationConfigurationDto configDto, SocialActorKey memberKey);

  public CollaborationWorkspaceNotificationConfigurationDto update(
      CollaborationWorkspaceNotificationConfigurationDto config,
      SocialActorKey memberKey);

  public ContentWorkspaceNotificationConfigurationDto update(
      ContentWorkspaceNotificationConfigurationDto config,
      SocialActorKey memberKey);

  public PartnerProgramNotificationConfigurationDto update(
      PartnerProgramNotificationConfigurationDto config,
      SocialActorKey memberKey);

  public ProfileNotificationConfigurationDto getProfileConfig(
      SocialActorKey memberKey);

  public CollaborationWorkspaceNotificationConfigurationDto getCollabWorkspaceConfig(
      SocialActorKey memberKey);

  public ContentWorkspaceNotificationConfigurationDto getContentWorkspaceConfig(
      SocialActorKey memberKey);

  public PartnerProgramNotificationConfigurationDto getPartnerProgramConfig(
      SocialActorKey memberKey);

}
