package com.similan.service.internal.impl.event.processor.network;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.network.BusinessProfileUpdateWallEntry;
import com.similan.domain.entity.wall.network.BusinessProfileViewWallEntry;
import com.similan.domain.entity.wall.network.MemberProfileUpdateWallEntry;
import com.similan.domain.entity.wall.network.MemberProfileViewWallEntry;
import com.similan.domain.repository.community.SocialActorRepository;
import com.similan.domain.repository.wall.WallEntryRepository;
import com.similan.service.internal.api.event.io.network.SocialActorProfileWallEntryEvent;
import com.similan.service.internal.api.wall.WallInternalService;
import com.similan.service.internal.impl.event.EventPreProcessor;

@Component
public class SocialActorProfileWallEntryEventProcessor 
         implements EventPreProcessor<SocialActorProfileWallEntryEvent>{
  @Autowired
  private WallInternalService wallInternalService;
  @Autowired
  private WallEntryRepository wallEntryRepository;
  @Autowired
  private SocialActorRepository socialActorRepository;

	@Override
	public void preProcess(SocialActorProfileWallEntryEvent event) {
		
		SocialActor socialActorProfileOwner = this.socialActorRepository
				.findOne(event.getSocialActorProfileOwnerId());
		
		SocialActor socialActorInitiator = this.socialActorRepository
				.findOne(event.getSocialActorInitiatorId());
		
		Wall wall = this.wallInternalService.get(socialActorInitiator);
				
		switch(event.getEntryType()){
		
		  case MEMBER_PROFILE_VIEWED:{
			  MemberProfileViewWallEntry entry = this.wallEntryRepository
					  .createMemberProfileViewWallEntry(wall, 
					  socialActorInitiator, socialActorProfileOwner);
			  this.wallInternalService.post(entry);
			  break;
		  }
		           
		  case MEMBER_PROFILE_UPDATED:{
			  MemberProfileUpdateWallEntry entry = this.wallEntryRepository
					  .createMemberProfileUpdateWallEntry(wall, 
					  socialActorInitiator, socialActorProfileOwner);
			  this.wallInternalService.post(entry);
			  break;
		  }
		  
		  case BUSINESS_PROFILE_VIEWED:{
			  BusinessProfileViewWallEntry entry = this.wallEntryRepository
					  .createBusinessProfileViewWallEntry(wall, 
					  socialActorInitiator, socialActorProfileOwner);
			  this.wallInternalService.post(entry);
			  break;
		  }
		  
		  case BUSINESS_PROFILE_UPDATED:{
			  BusinessProfileUpdateWallEntry entry = this.wallEntryRepository
					  .createBusinessProfileUpdateWallEntry(wall, 
					  socialActorInitiator, socialActorProfileOwner);
			  this.wallInternalService.post(entry);
			  break;
			  
		  }
		  
		  default: break;
		}
	}
}
