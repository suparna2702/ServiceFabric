package com.similan.domain.entity.wall.network;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "MemberProfileViewWallEntry")
@DiscriminatorValue("MEMBER_PROFILE_VIEWED")
public class MemberProfileViewWallEntry extends SocialActorProfileWallEntry {
	
	protected MemberProfileViewWallEntry(){
		
	}
	
	public MemberProfileViewWallEntry(int number, Date date){
		super(WallEntryType.MEMBER_PROFILE_VIEWED, number, date);
		this.setShowWall(Boolean.FALSE);
	}
	
}
