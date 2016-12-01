package com.similan.service.impl.wall.entrymarshaller.network;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.wall.IWallEntrySubject;
import com.similan.domain.entity.wall.network.SocialActorProfileWallEntry;
import com.similan.service.api.base.dto.basic.IKeyHolderDto;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.api.wall.dto.basic.network.BusinessProfileUpdateWallEntryDto;
import com.similan.service.api.wall.dto.basic.network.BusinessProfileViewWallEntryDto;
import com.similan.service.api.wall.dto.basic.network.MemberProfileUpdateWallEntryDto;
import com.similan.service.api.wall.dto.basic.network.MemberProfileViewWallEntryDto;
import com.similan.service.api.wall.dto.basic.network.SocialActorProfileWallEntryDto;
import com.similan.service.api.wall.dto.key.IWallEntrySubjectKey;
import com.similan.service.api.wall.dto.key.WallEntryKey;
import com.similan.service.impl.common.GenericReferenceMarshaller;
import com.similan.service.impl.community.SocialActorMarshaller;
import com.similan.service.impl.wall.WallEntryMarshaller;

@Component
public class SocialActorProfileWallEntryMarshaller extends
                             WallEntryMarshaller<SocialActorProfileWallEntry, SocialActorKey>{
  @Autowired
  private GenericReferenceMarshaller genericReferenceMarshaller;
  @Autowired
  private SocialActorMarshaller actorMarshaller;

	@Override
	public WallEntryDto<SocialActorKey> marshall(
			WallEntryKey<SocialActorKey> entryKey,
			ActorDto initiatorDto, Date date,
			SocialActorProfileWallEntry entry) {
		
		WallEntryType entryType = entry.getType();
		GenericReference<IWallEntrySubject> subject = entry.getSubject();
		IKeyHolderDto<SocialActorKey> socialActorKey = genericReferenceMarshaller
	              .marshall(subject, IWallEntrySubjectKey.class);
		
		SocialActor socialActor = actorMarshaller
				.unmarshallActorKey(socialActorKey.getKey(), true);
		
		ActorDto profileOwnerActor = actorMarshaller
				.marshallActor(socialActor);
		SocialActorProfileWallEntryDto wallEntry = null;
		
		switch(entryType){
		  
		   case MEMBER_PROFILE_VIEWED:{
			   wallEntry = new MemberProfileViewWallEntryDto(profileOwnerActor,
					   entryKey, initiatorDto, entry.getDate());
			   break;
		   }
		  
		   case MEMBER_PROFILE_UPDATED:{
			   wallEntry = new MemberProfileUpdateWallEntryDto(profileOwnerActor,
					   entryKey, initiatorDto, entry.getDate());
			   break;
		   }
		  
		   case BUSINESS_PROFILE_VIEWED:{
			   wallEntry = new BusinessProfileViewWallEntryDto(profileOwnerActor,
					   entryKey, initiatorDto, entry.getDate());
			   break;
		   }
		  
		   case BUSINESS_PROFILE_UPDATED:{
			   wallEntry = new BusinessProfileUpdateWallEntryDto(profileOwnerActor,
					   entryKey, initiatorDto, entry.getDate());
			   break;
		   }
		   
		   default: break;
		}
		
		return wallEntry;
	}

}
