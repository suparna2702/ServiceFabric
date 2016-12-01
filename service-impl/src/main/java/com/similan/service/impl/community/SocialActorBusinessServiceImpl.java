package com.similan.service.impl.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.community.SocialActor;
import com.similan.service.api.community.SocialActorBusinessService;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.impl.base.ServiceImpl;
import com.similan.service.internal.api.event.EventInternalService;
import com.similan.service.internal.api.event.io.network.SocialActorProfileWallEntryEvent;

@Service
public class SocialActorBusinessServiceImpl extends ServiceImpl implements
    SocialActorBusinessService {
  @Autowired
  private EventInternalService eventInternalService;
  @Autowired
  private SocialActorMarshaller actorMarshaller;

  @Override
  @Transactional
  public ActorDto viewBusinessProfile(
      SocialActorKey requestorKey, SocialActorKey businessProfileKey) {
    SocialActor requestor = actorMarshaller
        .unmarshallActorKey(requestorKey, true);

    SocialActor businessProfile = actorMarshaller
        .unmarshallActorKey(businessProfileKey, true);

    ActorDto ret = actorMarshaller
        .marshallActor(businessProfile);

    this.eventInternalService.fire(new SocialActorProfileWallEntryEvent(
        businessProfile.getId(), requestor.getId(),
        WallEntryType.BUSINESS_PROFILE_VIEWED));

    return ret;
  }

}
