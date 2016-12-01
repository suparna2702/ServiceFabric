package com.similan.service.internal.api.event.io.network;

import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.internal.api.event.io.Event;

public class SocialActorProfileWallEntryEvent extends Event {

	private static final long serialVersionUID = 1L;
	
	private Long socialActorProfileOwnerId;
	
	private Long socialActorInitiatorId;
	
	private WallEntryType entryType;
	
	public SocialActorProfileWallEntryEvent(Long socialActorProfileOwnerId,
			Long socialActorInitiatorId, WallEntryType entryType){
		
		this.socialActorProfileOwnerId = socialActorProfileOwnerId;
		this.socialActorInitiatorId = socialActorInitiatorId;
		this.entryType = entryType;
	}

	public Long getSocialActorProfileOwnerId() {
		return socialActorProfileOwnerId;
	}

	public Long getSocialActorInitiatorId() {
		return socialActorInitiatorId;
	}

	public WallEntryType getEntryType() {
		return entryType;
	}
	
	

}
