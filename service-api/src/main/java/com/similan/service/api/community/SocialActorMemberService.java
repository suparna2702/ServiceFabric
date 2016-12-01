package com.similan.service.api.community;

import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.basic.MemberDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.community.dto.operation.MemberSignupDto;

public interface SocialActorMemberService {
  MemberDto signUp(MemberSignupDto signupDto);

  MemberDto verify(String processUuid);

  ActorDto viewMemberProfile(SocialActorKey requestor,
      SocialActorKey memberProfile);
}
