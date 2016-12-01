package com.similan.domain.entity.wall.network;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "BusinessProfileUpdateWallEntry")
@DiscriminatorValue("BUSINESS_PROFILE_UPDATED")
public class BusinessProfileUpdateWallEntry extends SocialActorProfileWallEntry {
	
	protected BusinessProfileUpdateWallEntry(){
		
	}
	
	public BusinessProfileUpdateWallEntry(int number, Date date){
		super(WallEntryType.BUSINESS_PROFILE_UPDATED, number, date);	
		this.setShowWall(Boolean.TRUE);
	}
}
