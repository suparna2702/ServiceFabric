package com.similan.service.impl.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.repository.community.SocialActorRepository;
import com.similan.service.api.community.SocialActorService;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.error.ActorErrorCode;
import com.similan.service.api.community.dto.error.ActorException;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.impl.base.ServiceImpl;

@Service
public class SocialActorServiceImpl extends ServiceImpl implements
    SocialActorService {
  @Autowired
  private SocialActorRepository socialActorRepository;
  @Autowired
  private SocialActorMarshaller actorMarshaller;

  @Override
  @Transactional(readOnly = true)
  public ActorDto getActor(SocialActorKey actorKey) {
    SocialActor actor = actorMarshaller
        .unmarshallActorKey(actorKey, true);
    return actorMarshaller
        .marshallActor(actor);
  }

  @Override
  @Transactional(readOnly = true)
  public SocialActorKey transitional_getKey(long id) {
    SocialActor actor = socialActorRepository.findOne(id);
    if (actor == null) {
      throw new ActorException(ActorErrorCode.ACTOR_NOT_FOUND, "Actor does not exist: " + id);
    }
    SocialActorKey actorKey = actorMarshaller
        .marshallActorKey(actor);
    return actorKey;
  }

  // Alan - temporarily needed to convert keys to an id to continue working with
  // existing member service.
  @Override
  @Transactional(readOnly = true)
  public Long transitional_getId(SocialActorKey key) {
    SocialActor actor = actorMarshaller
        .unmarshallActorKey(key, true);
    return actor.getId();
  }

}
