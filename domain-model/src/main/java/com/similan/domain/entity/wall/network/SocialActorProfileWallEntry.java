package com.similan.domain.entity.wall.network;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.similan.domain.entity.wall.WallEntry;
import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "SocialActorProfileWallEntry")
@DiscriminatorValue("SOCIAL_ACTOR_PROFILE_ENTRY")
public abstract class SocialActorProfileWallEntry extends WallEntry {
	
	public SocialActorProfileWallEntry(){
		
	}
	
	public SocialActorProfileWallEntry(WallEntryType entryType, int number, Date date){
		super(entryType, number, date);
	}

}
