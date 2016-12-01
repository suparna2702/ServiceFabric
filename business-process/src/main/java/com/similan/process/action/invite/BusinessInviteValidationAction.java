package com.similan.process.action.invite;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.MemberJoinIntent;
import com.similan.domain.entity.community.MemberState;
import com.similan.domain.entity.community.MemberTransientStateAttributeType;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.entity.util.WorkflowTransientStateAttribute;
import com.similan.domain.entity.util.WorkflowTransientStateType;
import com.similan.domain.repository.community.SocialOrganizationRepository;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.domain.repository.util.WorkflowTransientStateRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.workflow.ProcessContextParameterConstants;
import com.similan.process.action.ActionResultType;
import com.similan.service.api.community.dto.basic.BusinessAccountSubscriptionType;

@Slf4j
@Component
public class BusinessInviteValidationAction implements ActivityBehaviour {
  private static final long serialVersionUID = 1L;

  @Autowired
  private SocialPersonRepository memberRepository;
  @Autowired
  private WorkflowTransientStateRepository memberTransientStateRepository;
  @Autowired
  private PlatformCommonSettings platformCommonSettings;
  @Autowired
  private SocialOrganizationRepository organizationRepository;

  public void execute(ActivityExecution execution) throws Exception {

    /**
     * 1. Validate whether all the input parameters are present (member id,
     * organization email, type of association) 2. Then save them in trasient
     * state 3. Move on
     * 
     */

    String inviteeOrgName = (String) execution
        .getVariable(ProcessContextParameterConstants.ORG_INVITEE_NAME);
    String inviteeEmail = (String) execution
        .getVariable(ProcessContextParameterConstants.INVITEE_EMAIL);
    String orgInviteAssociationType = (String) execution
        .getVariable(ProcessContextParameterConstants.ORG_INVITE_ASSOCIATION_TYPE);
    Long inviterId = (Long) execution
        .getVariable(ProcessContextParameterConstants.INVITER_ID);

    SocialPerson inviter = memberRepository.findOne(inviterId);
    StringBuilder urlBuilder = new StringBuilder();
    String hostUrl = platformCommonSettings.getPortalApplcationUrl().getValue();
    String processInstanceId = execution.getId();

    /* save the validation */
    WorkflowTransientState memberTransientState = this.memberTransientStateRepository
        .create();
    memberTransientState.setProcessInstanceId(processInstanceId);
    memberTransientState
        .setStateType(WorkflowTransientStateType.BusinessInvite);

    List<WorkflowTransientStateAttribute> attributes = new ArrayList<WorkflowTransientStateAttribute>();
    memberTransientState.setAttributes(attributes);

    /* to send business INVITE or CONNECTION email */
    WorkflowTransientStateAttribute attrInviteeEmail = this.memberTransientStateRepository
        .createAttribute(ProcessContextParameterConstants.INVITEE_EMAIL,
            inviteeEmail, MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(attrInviteeEmail);

    /* The process Instance ID for notification */
    WorkflowTransientStateAttribute attrProcInstanceId = this.memberTransientStateRepository
        .createAttribute(ProcessContextParameterConstants.PROCESS_INSTANCE_ID,
            processInstanceId, MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(attrProcInstanceId);

    /* This is for making the connection with INVITER's Business */
    WorkflowTransientStateAttribute attrInviterId = this.memberTransientStateRepository
        .createAttribute(ProcessContextParameterConstants.INVITER_ID,
            inviterId.toString(), MemberTransientStateAttributeType.LONG_TYPE);
    attributes.add(attrInviterId);

    /* To create type of connections */
    WorkflowTransientStateAttribute attrInviteType = this.memberTransientStateRepository
        .createAttribute(
            ProcessContextParameterConstants.ORG_INVITE_ASSOCIATION_TYPE,
            orgInviteAssociationType,
            MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(attrInviteType);

    /* Organization name either to be created or associated */
    WorkflowTransientStateAttribute attrOrgName = this.memberTransientStateRepository
        .createAttribute(ProcessContextParameterConstants.ORG_INVITEE_NAME,
            inviteeOrgName, MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(attrOrgName);

    /*
     * before moving forward we should check whether this business exists or not
     * *
     */

    SocialOrganization orgExist = this.organizationRepository
        .findOrgByEmail(inviteeEmail);
    String businessInviteValidationUrl = StringUtils.EMPTY;

    if (orgExist != null) {
      log.info("Case of inviting existing org" + orgExist.getBusinessName()
          + " business email " + orgExist.getPrimaryEmail());

      /*
       * we just send an email for the approval of business then we wait for
       * approval from the business entity
       */
      urlBuilder.append(hostUrl)
          .append("business/businessConnectionApprove.xhtml?pid=")
          .append(processInstanceId).append("&oid=").append(orgExist.getId())
          .append("&status=");

      execution.setVariable(ProcessContextParameterConstants.MEMBER_FIRSTNAME,
          inviter.getFirstName());
      execution.setVariable(ProcessContextParameterConstants.MEMBER_LASTNAME,
          inviter.getLastName());
      execution.setVariable(ProcessContextParameterConstants.BUSINESS_NAME,
          orgExist.getBusinessName());
      execution.setVariable(
          ProcessContextParameterConstants.BUSINESS_CONTACT_APPROVAL_URL,
          urlBuilder.toString() + "true");
      execution.setVariable(
          ProcessContextParameterConstants.BUSINESS_CONTACT_REJECT_URL,
          urlBuilder.toString() + "false");

      execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
          ActionResultType.validExistingBusiness.toString());
    } else {

      /* we are creating this new member so that we have one admin */
      String name = UUID.randomUUID().toString();
      SocialPerson orgAdmin = this.memberRepository.create(name);
      orgAdmin.setPrimaryEmail(inviteeEmail);
      orgAdmin.setState(MemberState.Pending);
      this.memberRepository.save(orgAdmin);

      memberTransientState.setMemberId(orgAdmin.getId());

      /* since it is a new organization it will be a JOIN */
      WorkflowTransientStateAttribute attrJoinMethod = this.memberTransientStateRepository
          .createAttribute(ProcessContextParameterConstants.MEMBER_JOIN_METHOD,
              MemberJoinIntent.JoinByInvite.toString(),
              MemberTransientStateAttributeType.STRING_TYPE);
      attributes.add(attrJoinMethod);

      WorkflowTransientStateAttribute attributeBusinessAccountType = this.memberTransientStateRepository
          .createAttribute(
              ProcessContextParameterConstants.BUSINESS_ACCOUNT_TYPE,
              BusinessAccountSubscriptionType.BUSINESS_STANDARD,
              MemberTransientStateAttributeType.STRING_TYPE);
      attributes.add(attributeBusinessAccountType);

      urlBuilder.append(hostUrl)
          .append("business/businessInviteBasicAttrEntry.xhtml?mid=")
          .append(orgAdmin.getId()).append("&pid=").append(processInstanceId);
      businessInviteValidationUrl = urlBuilder.toString();

      log.info("Member validation Url " + businessInviteValidationUrl);
      execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
          ActionResultType.validNewBusiness.toString());

      execution.setVariable(
          ProcessContextParameterConstants.MEMBER_VALIDATION_URL,
          businessInviteValidationUrl);
    }

    execution.setVariable(ProcessContextParameterConstants.TO_EMAIL,
        inviteeEmail);
    this.memberTransientStateRepository.save(memberTransientState);

  }

}
