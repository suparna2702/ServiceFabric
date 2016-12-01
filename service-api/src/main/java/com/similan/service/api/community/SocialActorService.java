package com.similan.service.api.community;

import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

public interface SocialActorService {
  
   ActorDto getActor(SocialActorKey actorKey);

   SocialActorKey transitional_getKey(long id);

  // Alan - temporarily needed to convert keys to an id to continue working with
  // existing member service.
  Long transitional_getId(SocialActorKey key);

}
