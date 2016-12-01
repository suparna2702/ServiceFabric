package com.similan.process.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.Account;
import com.similan.domain.entity.community.MemberJoinIntent;
import com.similan.domain.entity.community.MemberState;
import com.similan.domain.entity.community.MemberTransientStateAttributeType;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialEmployee;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.entity.util.WorkflowTransientStateAttribute;
import com.similan.domain.entity.util.WorkflowTransientStateType;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.domain.repository.util.WorkflowTransientStateRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.workflow.ProcessContextParameterConstants;

@Slf4j
@Component
public class ValidateMemberInviteInputAction implements ActivityBehaviour {
  private static final long serialVersionUID = 1L;
  @Autowired
  private SocialPersonRepository memberRepository;
  @Autowired
  private WorkflowTransientStateRepository memberTransientStateRepository;
  @Autowired
  private PlatformCommonSettings platformCommonSettings;

  public void execute(ActivityExecution execution) throws Exception {
    /**
     * 1. Get all the necessary parameters from the context 2. Create a member
     * 3. Generate the Validation
     */

    String inviteeFirstName = (String) execution
        .getVariable(ProcessContextParameterConstants.INVITEE_FIRSTNAME);
    String inviteeLastName = (String) execution
        .getVariable(ProcessContextParameterConstants.INVITEE_LASTNAME);
    String inviteeEmail = (String) execution
        .getVariable(ProcessContextParameterConstants.INVITEE_EMAIL);
    String memberInviteType = (String) execution
        .getVariable(ProcessContextParameterConstants.MEMBER_INVITE_TYPE);
    String inviteePassword = (String) execution
        .getVariable(ProcessContextParameterConstants.INVITEE_PASSWORD);
    String inviteeCompany = (String) execution
        .getVariable(ProcessContextParameterConstants.BUSINESS_NAME);
    Long inviterId = (Long) execution
        .getVariable(ProcessContextParameterConstants.INVITER_ID);

    SocialPerson inviter = memberRepository.findOne(inviterId);
    SocialPerson invitee = getExistingMember(inviteeEmail);

    if (invitee != null) {
      handleInvitationToExistingMember(execution, inviter, invitee,
          memberInviteType);
    } else {
      handleInvitationToNewMember(execution, inviteeFirstName, inviteeLastName,
          inviteeEmail, memberInviteType, inviteePassword, inviteeCompany,
          inviterId);
    }

    /*
     * If it is from a member who is associated with Business then provide
     * business logo
     */
    SocialEmployee employee = inviter.getEmployer();
    if (employee != null) {
      SocialActor employer = employee.getContactFrom();
      String businessLogo = employer.getImage();

      if (!StringUtils.isBlank(businessLogo)) {
        log.info("Branding email with logo " + businessLogo);
        String logoUrl = platformCommonSettings.getPortalApplcationUrl()
            .getValue() + "spring/assets" + businessLogo;
        execution.setVariable(ProcessContextParameterConstants.BUSINESS_LOGO,
            logoUrl);
      }
    }
  }

  private SocialPerson getExistingMember(String inviteeEmail) {
    return memberRepository.findByEmail(inviteeEmail);
  }

  private void handleInvitationToNewMember(ActivityExecution execution,
      String inviteeFirstName, String inviteeLastName, String inviteeEmail,
      String memberInviteType, String inviteePassword, String inviteeCompany,
      Long inviterId) {
    /* for the time being create a basic member */
    log.info("Ivitee info first name: " + inviteeFirstName + " last name "
        + inviteeLastName + " email: " + inviteeEmail + " password "
        + inviteePassword);

    Account accnt = new Account();
    accnt.setPassword(inviteePassword);

    String name = UUID.randomUUID().toString();
    SocialPerson member = memberRepository.create(name);
    member.setFirstName(inviteeFirstName);
    member.setLastName(inviteeLastName);
    member.setState(MemberState.Pending);
    member.setPrimaryEmail(inviteeEmail);
    member.setJoinMethod(MemberJoinIntent.JoinByInvite);
    member.setMemberAccount(accnt);
    member.setStartDate(new Date());
    member.setCompany(inviteeCompany);

    this.memberRepository.save(member);
    String processInstanceId = execution.getId();

    StringBuilder urlBuilder = new StringBuilder();
    String hostUrl = platformCommonSettings.getPortalApplcationUrl().getValue();
    log.info("Host URL " + hostUrl);

    urlBuilder.append(hostUrl)
        .append("member/memberInviteSignupInput.xhtml?mid=")
        .append(member.getId()).append("&pid=").append(processInstanceId);
    String memberValidationUrl = urlBuilder.toString();

    /* Put everything in the context */
    execution.setVariable(
        ProcessContextParameterConstants.MEMBER_VALIDATION_URL,
        memberValidationUrl);

    log.info("Member validation Url " + memberValidationUrl);

    /* save the validation */
    WorkflowTransientState memberTransientState = this.memberTransientStateRepository
        .create();
    memberTransientState.setMemberId(member.getId());
    memberTransientState.setStateType(WorkflowTransientStateType.MemberInvite);
    memberTransientState.setProcessInstanceId(processInstanceId);

    List<WorkflowTransientStateAttribute> attributes = new ArrayList<WorkflowTransientStateAttribute>();
    memberTransientState.setAttributes(attributes);

    WorkflowTransientStateAttribute attributememberId = this.memberTransientStateRepository
        .createAttribute();
    attributememberId
        .setAttributeName(ProcessContextParameterConstants.MEMBER_ID);
    attributememberId.setAttributeValue(member.getId().toString());
    attributememberId
        .setAttributeType(MemberTransientStateAttributeType.LONG_TYPE);
    attributes.add(attributememberId);

    WorkflowTransientStateAttribute attrProcInstanceId = this.memberTransientStateRepository
        .createAttribute();
    attrProcInstanceId
        .setAttributeName(ProcessContextParameterConstants.PROCESS_INSTANCE_ID);
    attrProcInstanceId.setAttributeValue(processInstanceId);
    attrProcInstanceId
        .setAttributeType(MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(attrProcInstanceId);

    WorkflowTransientStateAttribute attributeInviteeEmail = this.memberTransientStateRepository
        .createAttribute(ProcessContextParameterConstants.INVITEE_EMAIL,
            inviteeEmail, MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(attributeInviteeEmail);

    WorkflowTransientStateAttribute attrInviterId = this.memberTransientStateRepository
        .createAttribute();
    attrInviterId.setAttributeName(ProcessContextParameterConstants.INVITER_ID);
    attrInviterId.setAttributeValue(inviterId.toString());
    attrInviterId.setAttributeType(MemberTransientStateAttributeType.LONG_TYPE);
    attributes.add(attrInviterId);

    WorkflowTransientStateAttribute attrJoinMethod = this.memberTransientStateRepository
        .createAttribute();
    attrJoinMethod
        .setAttributeName(ProcessContextParameterConstants.MEMBER_JOIN_METHOD);
    attrJoinMethod.setAttributeValue(MemberJoinIntent.JoinByInvite.toString());
    attrJoinMethod
        .setAttributeType(MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(attrJoinMethod);

    WorkflowTransientStateAttribute attrInviteType = this.memberTransientStateRepository
        .createAttribute();
    attrInviteType
        .setAttributeName(ProcessContextParameterConstants.MEMBER_INVITE_TYPE);
    attrInviteType.setAttributeValue(memberInviteType);
    attrInviteType
        .setAttributeType(MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(attrInviteType);

    WorkflowTransientStateAttribute attrInviteeFirstName = this.memberTransientStateRepository
        .createAttribute();
    attrInviteeFirstName
        .setAttributeName(ProcessContextParameterConstants.INVITEE_FIRSTNAME);
    attrInviteeFirstName.setAttributeValue(inviteeFirstName);
    attrInviteeFirstName
        .setAttributeType(MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(attrInviteeFirstName);

    WorkflowTransientStateAttribute attrInviteeLastName = this.memberTransientStateRepository
        .createAttribute();
    attrInviteeLastName
        .setAttributeName(ProcessContextParameterConstants.INVITEE_LASTNAME);
    attrInviteeLastName.setAttributeValue(inviteeLastName);
    attrInviteeLastName
        .setAttributeType(MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(attrInviteeLastName);

    this.memberTransientStateRepository.save(memberTransientState);

    execution.setVariable(ProcessContextParameterConstants.MEMBER_FIRSTNAME,
        inviteeFirstName);
    execution.setVariable(ProcessContextParameterConstants.MEMBER_LASTNAME,
        inviteeLastName);
    execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
        ActionResultType.validNewMember.toString());
  }

  private void handleInvitationToExistingMember(ActivityExecution execution,
      SocialPerson inviter, SocialPerson invitee, String memberInviteType) {

    String processInstanceId = execution.getId();

    StringBuilder urlBuilder = new StringBuilder();
    String hostUrl = platformCommonSettings.getPortalApplcationUrl().getValue();
    log.info("Host URL " + hostUrl);

    urlBuilder.append(hostUrl)
        .append("member/existingMemberAcceptInviteDecisionInput.xhtml?")
        .append("mid=").append(invitee.getId()).append("&inviterId=")
        .append(inviter.getId()).append("&pid=").append(processInstanceId);
    String memberAcceptUrl = urlBuilder.toString();

    urlBuilder = new StringBuilder();
    urlBuilder.append(hostUrl)
        .append("member/existingMemberRejectInviteDecisionInput.xhtml?")
        .append("mid=").append(invitee.getId()).append("&inviterId=")
        .append(inviter.getId()).append("&pid=").append(processInstanceId);
    String memberRejectUrl = urlBuilder.toString();

    /* Put everything in the context */
    execution.setVariable(
        ProcessContextParameterConstants.MEMBER_INVITE_ACCEPT_VALIDATION_URL,
        memberAcceptUrl);
    /* Put everything in the context */
    execution.setVariable(
        ProcessContextParameterConstants.MEMBER_INVITE_REJECT_VALIDATION_URL,
        memberRejectUrl);

    /* save the validation */
    WorkflowTransientState memberTransientState = this.memberTransientStateRepository
        .create();
    memberTransientState.setMemberId(invitee.getId());
    memberTransientState.setStateType(WorkflowTransientStateType.MemberInvite);
    memberTransientState.setProcessInstanceId(processInstanceId);

    List<WorkflowTransientStateAttribute> attributes = new ArrayList<WorkflowTransientStateAttribute>();
    memberTransientState.setAttributes(attributes);

    WorkflowTransientStateAttribute attributeInviteeEmail = this.memberTransientStateRepository
        .createAttribute(ProcessContextParameterConstants.INVITEE_EMAIL,
            invitee.getPrimaryEmail(),
            MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(attributeInviteeEmail);

    WorkflowTransientStateAttribute attributememberId = this.memberTransientStateRepository
        .createAttribute();
    attributememberId
        .setAttributeName(ProcessContextParameterConstants.MEMBER_ID);
    attributememberId.setAttributeValue(invitee.getId().toString());
    attributememberId
        .setAttributeType(MemberTransientStateAttributeType.LONG_TYPE);
    attributes.add(attributememberId);

    WorkflowTransientStateAttribute attrProcInstanceId = this.memberTransientStateRepository
        .createAttribute();
    attrProcInstanceId
        .setAttributeName(ProcessContextParameterConstants.PROCESS_INSTANCE_ID);
    attrProcInstanceId.setAttributeValue(processInstanceId);
    attrProcInstanceId
        .setAttributeType(MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(attrProcInstanceId);

    WorkflowTransientStateAttribute attrInviterId = this.memberTransientStateRepository
        .createAttribute();
    attrInviterId.setAttributeName(ProcessContextParameterConstants.INVITER_ID);
    attrInviterId.setAttributeValue(inviter.getId().toString());
    attrInviterId.setAttributeType(MemberTransientStateAttributeType.LONG_TYPE);
    attributes.add(attrInviterId);

    WorkflowTransientStateAttribute attrInviteType = this.memberTransientStateRepository
        .createAttribute();
    attrInviteType
        .setAttributeName(ProcessContextParameterConstants.MEMBER_INVITE_TYPE);
    attrInviteType.setAttributeValue(memberInviteType);
    attrInviteType
        .setAttributeType(MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(attrInviteType);

    WorkflowTransientStateAttribute attrInviteeFirstName = this.memberTransientStateRepository
        .createAttribute();
    attrInviteeFirstName
        .setAttributeName(ProcessContextParameterConstants.INVITEE_FIRSTNAME);
    attrInviteeFirstName.setAttributeValue(invitee.getFirstName());
    attrInviteeFirstName
        .setAttributeType(MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(attrInviteeFirstName);

    WorkflowTransientStateAttribute attrInviteeLastName = this.memberTransientStateRepository
        .createAttribute();
    attrInviteeLastName
        .setAttributeName(ProcessContextParameterConstants.INVITEE_LASTNAME);
    attrInviteeLastName.setAttributeValue(invitee.getLastName());
    attrInviteeLastName
        .setAttributeType(MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(attrInviteeLastName);

    this.memberTransientStateRepository.save(memberTransientState);
    execution.setVariable(ProcessContextParameterConstants.INVITEE_FIRSTNAME,
        invitee.getFirstName());
    execution.setVariable(ProcessContextParameterConstants.INVITEE_LASTNAME,
        invitee.getLastName());
    execution.setVariable(ProcessContextParameterConstants.MEMBER_FIRSTNAME,
        inviter.getFirstName());
    execution.setVariable(ProcessContextParameterConstants.MEMBER_LASTNAME,
        inviter.getLastName());
    execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
        ActionResultType.validExistingMember.toString());
  }
}
