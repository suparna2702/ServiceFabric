package com.similan.service.internal.impl.event.processor.partner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.partner.Partnership;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.partner.PartnerProgramWallEntry;
import com.similan.domain.repository.partner.PartnershipRepository;
import com.similan.service.internal.api.event.io.partner.PartnerProgramJoinEvent;

@Component
public class PartnerProgramJoinEventProcessor extends
    PartnerProgramEventProcessor<PartnerProgramJoinEvent> {
  @Autowired
  private PartnershipRepository partnershipRepository;

  @Override
  protected PartnerProgramWallEntry createWallEntry(
      PartnerProgramJoinEvent event) {
    Partnership partnership = partnershipRepository.findOne(event
        .getPartnershipId());

    Wall wall = getWall(partnership.getPartnerProgram()
        .getPartnerProgramWorkspace());
    PartnerProgramWallEntry wallEntry = this.getWallEntryRepository()
        .createPartnerJoinWallEntry(wall, partnership);

    return wallEntry;
  }

}
