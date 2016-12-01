package com.similan.service.marshallers;

import com.similan.domain.entity.community.SocialPerson;
import com.similan.framework.dto.member.MemberInfoDto;

public class MemberMarshaller {
  
  public MemberInfoDto marshallMember(SocialPerson person){
    MemberInfoDto retMember = new MemberInfoDto();
    retMember.setId(person.getId());
    retMember.setFirstName(person.getFirstName());
    retMember.setLastName(person.getLastName());
    retMember.setImagePath(person.getPhotoLocation());
    retMember.setEmail(person.getPrimaryEmail());
    
    return retMember;
  }

}
