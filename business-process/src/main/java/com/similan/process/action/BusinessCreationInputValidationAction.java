package com.similan.process.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.MemberTransientStateAttributeType;
import com.similan.domain.entity.community.OrganizationStatus;
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
import com.similan.service.api.community.dto.basic.BusinessAccountSubscriptionType;

@Component
public class BusinessCreationInputValidationAction implements ActivityBehaviour {
  private static final long serialVersionUID = 1L;

  @Autowired
  private WorkflowTransientStateRepository memberTransientStateRepository;
  @Autowired
  private PlatformCommonSettings platformCommonSettings;
  @Autowired
  private SocialPersonRepository memberRepository;
  @Autowired
  private SocialOrganizationRepository organizationRepository;

  public void execute(ActivityExecution execution) throws Exception {
    /**
     * 1. Ensure all the input parameters required for creating a business
     * profile is there 2. Ensure there is not another business with the same
     * name 3. Member who is creating is not employee of another organization
     * 
     */
    Long memberId = (Long) execution
        .getVariable(ProcessContextParameterConstants.MEMBER_ID);
    String businessName = (String) execution
        .getVariable(ProcessContextParameterConstants.BUSINESS_NAME);
    String businessPhone = (String) execution
        .getVariable(ProcessContextParameterConstants.BUSINESS_PHONE_NUMBER);
    String businessEmail = (String) execution
        .getVariable(ProcessContextParameterConstants.BUSINESS_EMAIL);
    String businessAddrStreet = (String) execution
        .getVariable(ProcessContextParameterConstants.BUSINESS_ADDR_STREET);
    String businessAddrCity = (String) execution
        .getVariable(ProcessContextParameterConstants.BUSINESS_ADDR_CITY);
    String businessAddrState = (String) execution
        .getVariable(ProcessContextParameterConstants.BUSINESS_ADDR_STATE);
    String businessAddrZipCode = (String) execution
        .getVariable(ProcessContextParameterConstants.BUSINESS_ADDR_ZIPCODE);
    String businessAddrCountry = (String) execution
        .getVariable(ProcessContextParameterConstants.BUSINESS_ADDR_COUNTRY);
    String businessAccountType = (String) execution
        .getVariable(ProcessContextParameterConstants.BUSINESS_ACCOUNT_TYPE);

    if (StringUtils.isBlank(businessAccountType)) {
      businessAccountType = BusinessAccountSubscriptionType.BUSINESS_STANDARD;
    }

    /* get the member */
    SocialPerson member = this.memberRepository.findOne(memberId);
    if (member == null) {

      execution.setVariable(
          ProcessContextParameterConstants.ACTION_FAILURE_CAUSE,
          "Member does not exist");
      execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
          ActionResultType.inValid.toString());
      return;
    }

    execution.setVariable(ProcessContextParameterConstants.MEMBER_FIRSTNAME,
        member.getFirstName());
    execution.setVariable(ProcessContextParameterConstants.MEMBER_ID, member
        .getId());
    execution.setVariable(ProcessContextParameterConstants.TO_EMAIL,
        member.getPrimaryEmail());

    SocialOrganization org = this.organizationRepository
        .findOrgByName(businessName);
    if (org != null && org.getStatus() == OrganizationStatus.Active) {

      execution.setVariable(
          ProcessContextParameterConstants.ACTION_FAILURE_CAUSE,
          "An active organization with same name exists");
      execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
          ActionResultType.inValid.toString());
      return;

    }
    
    execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
        ActionResultType.valid.toString());

    WorkflowTransientState memberTransientState = this.memberTransientStateRepository
        .create();
    memberTransientState.setMemberId(member.getId());
    memberTransientState
        .setStateType(WorkflowTransientStateType.BusinessCreationByMember);
    memberTransientState.setProcessInstanceId(execution.getId());

    List<WorkflowTransientStateAttribute> attributes = new ArrayList<WorkflowTransientStateAttribute>();
    memberTransientState.setAttributes(attributes);

    WorkflowTransientStateAttribute attributeBusinessName = this.memberTransientStateRepository
        .createAttribute(ProcessContextParameterConstants.BUSINESS_NAME,
            businessName, MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(attributeBusinessName);

    WorkflowTransientStateAttribute attributeBusinessAccountType = this.memberTransientStateRepository
        .createAttribute(
            ProcessContextParameterConstants.BUSINESS_ACCOUNT_TYPE,
            businessAccountType, MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(attributeBusinessAccountType);

    WorkflowTransientStateAttribute attributeBusinessEmail = this.memberTransientStateRepository
        .createAttribute(ProcessContextParameterConstants.BUSINESS_EMAIL,
            businessEmail, MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(attributeBusinessEmail);

    WorkflowTransientStateAttribute attributeBusinessPhone = this.memberTransientStateRepository
        .createAttribute(
            ProcessContextParameterConstants.BUSINESS_PHONE_NUMBER,
            businessPhone, MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(attributeBusinessPhone);

    WorkflowTransientStateAttribute attributeBusinessAddrStreet = this.memberTransientStateRepository
        .createAttribute(ProcessContextParameterConstants.BUSINESS_ADDR_STREET,
            businessAddrStreet, MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(attributeBusinessAddrStreet);

    WorkflowTransientStateAttribute attributeBusinessAddrCity = this.memberTransientStateRepository
        .createAttribute(ProcessContextParameterConstants.BUSINESS_ADDR_CITY,
            businessAddrCity, MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(attributeBusinessAddrCity);

    WorkflowTransientStateAttribute attributeBusinessAddrState = this.memberTransientStateRepository
        .createAttribute(ProcessContextParameterConstants.BUSINESS_ADDR_STATE,
            businessAddrState, MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(attributeBusinessAddrState);

    WorkflowTransientStateAttribute attributeBusinessAddrZipCode = this.memberTransientStateRepository
        .createAttribute(
            ProcessContextParameterConstants.BUSINESS_ADDR_ZIPCODE,
            businessAddrZipCode, MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(attributeBusinessAddrZipCode);

    WorkflowTransientStateAttribute attributeBusinessAddrCountry = this.memberTransientStateRepository
        .createAttribute(
            ProcessContextParameterConstants.BUSINESS_ADDR_COUNTRY,
            businessAddrCountry, MemberTransientStateAttributeType.STRING_TYPE);
    attributes.add(attributeBusinessAddrCountry);

    this.memberTransientStateRepository.save(memberTransientState);

  }

}
