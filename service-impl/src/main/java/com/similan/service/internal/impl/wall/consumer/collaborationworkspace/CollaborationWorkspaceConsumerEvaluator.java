package com.similan.service.internal.impl.wall.consumer.collaborationworkspace;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspaceParticipation;
import com.similan.domain.entity.community.SocialActor;
import com.similan.service.internal.impl.wall.ConsumerEvaluator;

@Component
public class CollaborationWorkspaceConsumerEvaluator implements
    ConsumerEvaluator<CollaborationWorkspace> {

  @Override
  public Set<SocialActor> getConsumers(SocialActor initiator,
      CollaborationWorkspace wallContainer) {
    Set<SocialActor> consumers = new HashSet<>();

    // we do not want to distribute it to participants in this case
    //only initiator and creator will see that
    if (wallContainer.getConfig().getShowActivity() != true) {
      consumers.add(wallContainer.getCreator());
      consumers.add(initiator);
      return consumers;
    }

    List<CollaborationWorkspaceParticipation> participations = wallContainer
        .getParticipations();
    for (CollaborationWorkspaceParticipation participation : participations) {
      if (participation.getParticipant() != initiator) {
        consumers.add(participation.getParticipant());
      }
    }
    return consumers;
  }

}