package com.similan.service.impl.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.community.MemberState;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.process.signup.BasicMemberSignUpProcess;
import com.similan.domain.repository.community.SocialOrganizationRepository;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.domain.repository.process.BusinessProcessRepository;
import com.similan.service.api.community.SocialActorMemberService;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.basic.MemberDto;
import com.similan.service.api.community.dto.error.ActorErrorCode;
import com.similan.service.api.community.dto.error.ActorException;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.community.dto.operation.MemberSignupDto;
import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.impl.base.ServiceImpl;
import com.similan.service.internal.api.event.EventInternalService;
import com.similan.service.internal.api.event.io.network.SocialActorProfileWallEntryEvent;
import com.similan.service.internal.api.process.BusinessProcessInternalService;

@Service
public class SocialActorMemberServiceImpl extends ServiceImpl implements
    SocialActorMemberService {
  @Autowired
  private EventInternalService eventInternalService;
  @Autowired
  private SocialActorMarshaller actorMarshaller;
  @Autowired
  private BusinessProcessRepository businessProcessRepository;
  @Autowired
  private BusinessProcessInternalService businessProcessInternalService;
  @Autowired
  private SocialPersonRepository memberRepository;
  @Autowired
  private SocialOrganizationRepository businessRepository;

  @Override
  @Transactional
  public ActorDto viewMemberProfile(SocialActorKey requestorKey,
      SocialActorKey memberProfileKey) {
    SocialActor requestor = actorMarshaller.unmarshallActorKey(requestorKey,
        true);

    SocialActor memberProfile = actorMarshaller.unmarshallActorKey(
        memberProfileKey, true);

    ActorDto ret = actorMarshaller.marshallActor(memberProfile);

    this.eventInternalService.fire(new SocialActorProfileWallEntryEvent(
        memberProfile.getId(), requestor.getId(),
        WallEntryType.MEMBER_PROFILE_VIEWED));

    return ret;
  }

  @Transactional
  public MemberDto signUp(MemberSignupDto signupDto) {
    // check wheather there is a pending member workflow
    SocialPerson checkMember = memberRepository.findByEmailAndState(signupDto.getEmail(),
        MemberState.Pending);
    if (checkMember != null) {
      throw new ActorException(ActorErrorCode.PENDING_VERIFICATION,
          "A member signup initiative is pending with the email " + signupDto.getEmail()
              + " please check your email and complete the process ");
    }

    // also check wheather there is an existing member with the same email
    checkMember = this.memberRepository.findByEmail(signupDto.getEmail());
    if (checkMember != null) {
      throw new ActorException(ActorErrorCode.DUPLICATE_MEMBER_EMAIL,
          "A member with the email " + signupDto.getEmail()
              + " already exists in the community");
    }

    // check whether there is a business entity with the same email address
    // exists in community if so do not proceeed
    SocialOrganization business = businessRepository.findOrgByEmail(signupDto.getEmail());
    if (business != null) {
      throw new ActorException(ActorErrorCode.DUPLICATE_BUSINESS_EMAIL,
          "A business with the email " + signupDto.getEmail()
              + " already exists in the community");
    }

    BasicMemberSignUpProcess process = new BasicMemberSignUpProcess();
    process.setFirstName(signupDto.getFirstName());
    process.setLastName(signupDto.getLastName());
    process.setUsername(signupDto.getUsername());
    process.setPassword(signupDto.getPassword());
    process.setEmail(signupDto.getEmail());

    businessProcessInternalService.initiate(process);
    return actorMarshaller.marshallMember(process.getMember());
  }

  @Transactional
  public MemberDto verify(String processUuid) {
    BasicMemberSignUpProcess process = businessProcessRepository.findByUuid(
        BasicMemberSignUpProcess.class, processUuid);
    if (process == null) {
      throw new ActorException(ActorErrorCode.INVALID_SIGN_UP_PROCESS,
          "Invalid sign up process: " + processUuid);
    }
    businessProcessInternalService.resume(process);
    return actorMarshaller.marshallMember(process.getMember());
  }
}
