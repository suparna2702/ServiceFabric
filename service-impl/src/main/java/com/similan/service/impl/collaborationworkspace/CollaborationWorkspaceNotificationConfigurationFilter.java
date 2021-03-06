package com.similan.service.impl.collaborationworkspace;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspaceParticipation;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.settings.NotificationConfigurationService;
import com.similan.service.api.settings.dto.CollaborationWorkspaceNotificationConfigurationDto;
import com.similan.service.api.wall.dto.basic.WallEntryType;

@Slf4j
@Component
public class CollaborationWorkspaceNotificationConfigurationFilter {

  @Autowired
  private NotificationConfigurationService notificationConfigurationService;

  public List<SocialActor> getParticipants(CollaborationWorkspace workspace,
      SocialActor initiator, WallEntryType entryType) {

    List<SocialActor> participants = new ArrayList<SocialActor>();
    SocialActor creator = workspace.getCreator();

    /**
     * This is the case where 1) Workspace activity is configured false 2)
     * Activity is generated by non-owner
     */
    if (workspace.getConfig().getShowActivity() != true
        && initiator.getId().equals(creator.getId()) != true) {

      log.info("Since show activity flag is turned off by owner we will "
          + "not send email notification as well");

      if (creator != null) {

        addSocialActorBasedOnPreference(creator, participants, entryType,
            workspace.getOwner());
      }
      return participants;
    }

    List<CollaborationWorkspaceParticipation> participations = workspace
        .getParticipations();

    for (CollaborationWorkspaceParticipation participation : participations) {
      SocialActor participant = participation.getParticipant();
      if (participant instanceof SocialOrganization) {
        continue;
      }

      addSocialActorBasedOnPreference(participation.getParticipant(),
          participants, entryType, workspace.getOwner());

    }

    log.info("Number of participants " + participants.size());
    return participants;
  }

  private void addSocialActorBasedOnPreference(SocialActor participant,
      List<SocialActor> participants, WallEntryType entryType, SocialActor owner) {

    SocialActorKey participantKey = new SocialActorKey(participant.getName());
    SocialActorKey businessKey = new SocialActorKey(owner.getName());

    CollaborationWorkspaceNotificationConfigurationDto config = this.notificationConfigurationService
        .getCollabWorkspaceConfig(participantKey);

    CollaborationWorkspaceNotificationConfigurationDto businessWideConfig = this.notificationConfigurationService
        .getCollabWorkspaceConfig(businessKey);

    switch (entryType) {
    case COLLABORATION_WORKSPACE_DOCUMENT_VIEWED: {
      if (businessWideConfig.getCollabSpaceDocumentViewed() != null
          && businessWideConfig.getCollabSpaceDocumentViewed() != false) {
        if (config.getCollabSpaceDocumentViewed() != null
            && config.getCollabSpaceDocumentViewed() != false) {
          participants.add(participant);
        }
      }

      break;
    }
    case COLLABORATION_WORKSPACE_DOCUMENT_SHARED: {
      if (businessWideConfig.getCollabSpaceDocumentShare() != null
          && businessWideConfig.getCollabSpaceDocumentShare() != false) {
        if (config.getCollabSpaceDocumentShare() != null
            && config.getCollabSpaceDocumentShare() != false) {
          participants.add(participant);
        }
      }

      break;
    }
    case COLLABORATION_WORKSPACE_DOCUMENT_COMMENT: {
      if (businessWideConfig.getCollabSpaceDocumentComment() != null
          && businessWideConfig.getCollabSpaceDocumentComment() != false) {
        if (config.getCollabSpaceDocumentComment() != null
            && config.getCollabSpaceDocumentComment() != false) {
          participants.add(participant);
        }
      }

      break;
    }
    case COLLABORATION_WORKSPACE_DOCUMENT_UPDATED: {
      if (businessWideConfig.getCollabSpaceDocumentUpdated() != null
          && businessWideConfig.getCollabSpaceDocumentUpdated() != false) {
        if (config.getCollabSpaceDocumentUpdated() != null
            && config.getCollabSpaceDocumentUpdated() != false) {

          log.info("update config " + config.getCollabSpaceUpdated());
          participants.add(participant);
        }
      }

      break;
    }
    default: {
      log.info("deafult adding perticipant ");
      participants.add(participant);
      break;
    }

    }
  }
}
