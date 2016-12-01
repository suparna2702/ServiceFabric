package com.similan.domain.entity.wall.network;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "MemberProfileUpdateWallEntry")
@DiscriminatorValue("MEMBER_PROFILE_UPDATED")
public class MemberProfileUpdateWallEntry extends SocialActorProfileWallEntry {
	
	protected MemberProfileUpdateWallEntry(){
		
	}
	
	public MemberProfileUpdateWallEntry(int number, Date date){
		super(WallEntryType.MEMBER_PROFILE_UPDATED, number, date);
		this.setShowWall(Boolean.TRUE);
	}
}
