package com.similan.service.internal.impl.event.processor.partner;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.partner.PartnerProgramWallEntry;
import com.similan.domain.repository.community.SocialOrganizationRepository;
import com.similan.domain.repository.partner.PartnerProgramDefinitionRepository;
import com.similan.domain.repository.wall.WallEntryRepository;
import com.similan.service.internal.api.event.io.partner.PartnerProgramEvent;
import com.similan.service.internal.api.wall.WallInternalService;
import com.similan.service.internal.impl.event.EventPreProcessor;

public abstract class PartnerProgramEventProcessor<T extends PartnerProgramEvent>
    implements EventPreProcessor<T> {
  @Autowired
  private WallInternalService wallInternalService;
  @Autowired
  private SocialOrganizationRepository socialOrganizationRepository;
  @Autowired
  private PartnerProgramDefinitionRepository partnerProgramDefinitionRepository;
  @Autowired
  @Getter
  private WallEntryRepository wallEntryRepository;

  public WallInternalService getWallInternalService() {
    return wallInternalService;
  }

  protected abstract PartnerProgramWallEntry createWallEntry(T event);

  protected Wall getWall(CollaborationWorkspace partnerWorkspace) {
    return wallInternalService.get(partnerWorkspace);
  }

  @Override
  @Transactional
  public void preProcess(T event) {
    PartnerProgramWallEntry entry = createWallEntry(event);
    wallInternalService.post(entry);
  }

}
