package com.similan.service.internal.impl.process.signup;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Delegate;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import com.similan.domain.entity.community.Account;
import com.similan.domain.entity.community.MemberJoinIntent;
import com.similan.domain.entity.community.MemberState;
import com.similan.domain.entity.community.SocialMemberVisibilityType;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.process.signup.BasicMemberSignUpProcess;
import com.similan.domain.repository.community.SocialOrganizationRepository;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.framework.configuration.PlatformCommonSettings;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BasicMemberSignUpActions extends
    BaseSignUpActions<BasicMemberSignUpProcess> {
  @Autowired
  PlatformCommonSettings platformCommonSettings;
  @Autowired
  SocialOrganizationRepository organizationRepository;
  @Autowired
  SocialPersonRepository personRepository;

  @Delegate
  protected BasicMemberSignUpProcess process() {
    return getProcess();
  }

  public boolean validateInput() {
    /**
     * 1. Get all the necessary parameters from the context 2. Create a member
     * 3. Generate the Validation
     */

    /* for the time being create a basic member */
    Account memberAccount = new Account();
    memberAccount.setUserName(getUsername());
    memberAccount.setPassword(getPassword());

    String name = UUID.randomUUID().toString();
    SocialPerson member = personRepository.create(name);
    member.setMemberAccount(memberAccount);
    member.setFirstName(getFirstName());
    member.setLastName(getLastName());
    member.setState(MemberState.Pending);
    member.setJoinMethod(MemberJoinIntent.JoinBySignup);
    member.setPrimaryEmail(getEmail());
    member.setStartDate(new Date());

    this.personRepository.save(member);
    String processInstanceId = getUuid();

    StringBuilder urlBuilder = new StringBuilder();
    String hostUrl = platformCommonSettings.getPortalApplcationUrl().getValue();
    System.out.println("Host URL " + hostUrl);

    urlBuilder.append(hostUrl).append("member/memberSignupComplete.xhtml?mid=")
        .append(member.getId()).append("&pid=").append(processInstanceId);
    String memberValidationUrl = urlBuilder.toString();

    /* Put everything in the context */
    setValidationUrl(memberValidationUrl);

    log.info("Member validation Url " + memberValidationUrl);

    /* save the validation */
    setMember(member);

    return true;
  }

  public boolean validateCompletion() {
    SocialPerson member = getMember();
    member.setState(MemberState.Active);
    member.setVisibilityType(SocialMemberVisibilityType.VisiblePublic);
    this.personRepository.save(member);
    return true;
  }

  public void sendValidationEmail() {
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("firstName", getFirstName());
    parameters.put("lastName", getLastName());
    parameters.put("validationUrl", getValidationUrl());
    sendEmail("memberSignupWelcomeEmail.vm", getEmail(), parameters);
  }
}
