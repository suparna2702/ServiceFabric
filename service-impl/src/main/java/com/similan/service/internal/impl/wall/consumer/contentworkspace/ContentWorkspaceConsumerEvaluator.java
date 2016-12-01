package com.similan.service.internal.impl.wall.consumer.contentworkspace;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.managementworkspace.ManagementWorkspace;
import com.similan.domain.entity.managementworkspace.ManagementWorkspaceParticipation;
import com.similan.domain.repository.managementworkspace.ManagementWorkspaceParticipationRepository;
import com.similan.service.internal.impl.wall.ConsumerEvaluator;

@Component
public class ContentWorkspaceConsumerEvaluator implements
    ConsumerEvaluator<ManagementWorkspace> {
  @Autowired
  private ManagementWorkspaceParticipationRepository managementWorkspaceParticipationRepository;

  @Override
  public Set<SocialActor> getConsumers(SocialActor initiator,
      ManagementWorkspace wallContainer) {

    Set<SocialActor> consumers = new HashSet<>();
    List<ManagementWorkspaceParticipation> participations = managementWorkspaceParticipationRepository
        .findByWorkspace(wallContainer);

    for (ManagementWorkspaceParticipation participation : participations) {
      if (participation.getParticipant() != initiator) {
        consumers.add(participation.getParticipant());
      }
    }
    return consumers;
  }

}
