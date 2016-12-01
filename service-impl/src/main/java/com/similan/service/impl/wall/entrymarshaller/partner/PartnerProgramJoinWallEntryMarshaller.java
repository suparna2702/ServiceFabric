package com.similan.service.impl.wall.entrymarshaller.partner;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.wall.partner.PartnerProgramJoinWallEntry;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.basic.partner.PartnerProgramJoinWallEntryDto;
import com.similan.service.api.wall.dto.key.WallEntryKey;
import com.similan.service.impl.community.SocialActorMarshaller;
import com.similan.service.impl.wall.WallEntryMarshaller;

@Component
public class PartnerProgramJoinWallEntryMarshaller extends
    WallEntryMarshaller<PartnerProgramJoinWallEntry, CollaborationWorkspaceKey> {
  @Autowired
  private SocialActorMarshaller actorMarshaller;

  @Override
  public WallEntryDto<CollaborationWorkspaceKey> marshall(
      WallEntryKey<CollaborationWorkspaceKey> entryKey, ActorDto initiatorDto,
      Date date, PartnerProgramJoinWallEntry entry) {

    SocialActor creator = entry.getPartnerProgram().getCreator();
    ActorDto initiator = null;
    if (creator != null) {
      initiator = actorMarshaller.marshallActor(creator);
    } else {
      initiator = actorMarshaller.marshallActor(entry.getPartnerProgram()
          .getProgramOwner());
    }

    ActorDto partner = actorMarshaller.marshallBusiness(entry.getPartnership()
        .getPartner());

    PartnerProgramJoinWallEntryDto wallEntry = new PartnerProgramJoinWallEntryDto(
        entryKey, initiator, entry.getDate(), entry.getPartnerProgram()
            .getName(), entry.getPartnerProgram().getId(), partner);

    return wallEntry;
  }

}
