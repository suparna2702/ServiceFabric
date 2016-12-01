package com.similan.domain.entity.wall.network;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "BusinessProfileViewWallEntry")
@DiscriminatorValue("BUSINESS_PROFILE_VIEWED")
public class BusinessProfileViewWallEntry extends SocialActorProfileWallEntry {
	
	protected BusinessProfileViewWallEntry(){
		
	}
	
	public BusinessProfileViewWallEntry(int number, Date date){
		super(WallEntryType.BUSINESS_PROFILE_VIEWED, number, date);
		this.setShowWall(Boolean.FALSE);
	}

}
