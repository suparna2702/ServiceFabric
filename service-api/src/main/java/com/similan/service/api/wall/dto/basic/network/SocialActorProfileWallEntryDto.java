package com.similan.service.api.wall.dto.basic.network;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.key.WallEntryKey;

public abstract class SocialActorProfileWallEntryDto 
                       extends WallEntryDto<SocialActorKey>{
	
	@XmlElement
	private ActorDto profileOwnerActor;
	
	public SocialActorProfileWallEntryDto(ActorDto profileOwnerActor,
			WallEntryKey<SocialActorKey> key, ActorDto initiator, Date date){
		super(key, initiator, date);
		this.profileOwnerActor = profileOwnerActor;
	}

	public ActorDto getProfileOwnerActor() {
		return profileOwnerActor;
	}


}
