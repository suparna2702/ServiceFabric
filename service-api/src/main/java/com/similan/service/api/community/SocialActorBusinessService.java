package com.similan.service.api.community;

import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

public interface SocialActorBusinessService {
	
	public ActorDto viewBusinessProfile(SocialActorKey viewer, 
			SocialActorKey businessProfile);

}
