package com.similan.process.action;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.util.AttributeConstantUtil;
import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.entity.util.WorkflowTransientStateAttribute;
import com.similan.domain.entity.util.WorkflowTransientStateType;
import com.similan.domain.repository.community.SocialOrganizationRepository;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.domain.repository.util.WorkflowTransientStateRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.workflow.ProcessContextParameterConstants;
import com.similan.service.api.community.dto.basic.SocialEmployeeType;

@Slf4j
@Component
public class OrganizationAssociationInputValidation implements
    ActivityBehaviour {
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
     * 1. Get member id of the member to be associated 2. Get the org id 3. Get
     * the association type 5. Create a MemberTransientState 6. Move on
     */
    Long memberId = (Long) execution
        .getVariable(ProcessContextParameterConstants.MEMBER_ID);
    SocialPerson member = this.memberRepository.findOne(memberId);

    Long orgId = (Long) execution
        .getVariable(ProcessContextParameterConstants.ORGANIZATION_ID);
    SocialOrganization orgToAssociate = this.organizationRepository
        .findOne(orgId);

    SocialEmployeeType associationType = SocialEmployeeType
        .valueOf((String) execution
            .getVariable(ProcessContextParameterConstants.ORGANIZATION_MEMBER_ASSOCIATION_TYPE));

    log.info("member id " + memberId + " business id " + orgId
        + " employee type " + associationType);

    /**
     * Validations: 1. Member should not be associated as an employee to another
     * org 2. Member should not be associated with this org already
     */

    if (member.getEmployer() != null) {

      if (member.getEmployer().getId() == orgToAssociate.getId()) {

        /* we have problem we have to throw exceptions */
        log.info("Cann not associate with same org twice");

        execution.setVariable(
            ProcessContextParameterConstants.ACTION_FAILURE_CAUSE,
            "Cann not associate with same org twice");
        execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
            ActionResultType.inValid.toString());
      }
    }

    WorkflowTransientState memberAssociationState = this.memberTransientStateRepository
        .create();
    memberAssociationState.setId(memberId);
    memberAssociationState
        .setStateType(WorkflowTransientStateType.MemberAssociationWithOrg);

    WorkflowTransientStateAttribute attributeEmployeeType = this.memberTransientStateRepository
        .createAttribute();
    attributeEmployeeType
        .setAttributeName(AttributeConstantUtil.MEMBER_ORG_ASSOCIATION_ROLE);
    attributeEmployeeType.setAttributeType(AttributeConstantUtil.ATTRIBUTE_TYPE_STRING);
    attributeEmployeeType.setAttributeValue(associationType.toString());

    WorkflowTransientStateAttribute orgAttribute = this.memberTransientStateRepository
        .createAttribute();
    orgAttribute
        .setAttributeName(ProcessContextParameterConstants.ORGANIZATION_ID);
    orgAttribute.setAttributeType(AttributeConstantUtil.ATTRIBUTE_TYPE_LONG);
    orgAttribute.setAttributeValue(orgId.toString());

    List<WorkflowTransientStateAttribute> attributes = new ArrayList<WorkflowTransientStateAttribute>();
    attributes.add(attributeEmployeeType);
    attributes.add(orgAttribute);
    memberAssociationState.setAttributes(attributes);
    this.memberTransientStateRepository.save(memberAssociationState);

    String hostUrl = platformCommonSettings.getPortalApplcationUrl().getValue();
    String processInstanceId = execution.getId();

    StringBuilder urlBuilderApprove = new StringBuilder();
    urlBuilderApprove.append(hostUrl)
        .append("member/memberOrgAssociationApprove.xhtml?mid=")
        .append(member.getId()).append("&pid=").append(processInstanceId);
    String memberValidationApproveUrl = urlBuilderApprove.toString();

    StringBuilder urlBuilderReject = new StringBuilder();
    urlBuilderReject.append(hostUrl)
        .append("member/memberOrgAssociationReject.xhtml?mid=")
        .append(member.getId()).append("&pid=").append(processInstanceId);
    String memberValidationRejectUrl = urlBuilderReject.toString();

    /* Put everything in the context */
    execution.setVariable(
        ProcessContextParameterConstants.BUSINESS_ENTITY_APPROVE_URL,
        memberValidationApproveUrl);
    execution.setVariable(
        ProcessContextParameterConstants.BUSINESS_ENTITY_REJECT_URL,
        memberValidationRejectUrl);
    execution.setVariable(ProcessContextParameterConstants.ORG_PRIMARY_EMAIL,
        orgToAssociate.getPrimaryEmail());
    execution.setVariable(ProcessContextParameterConstants.EMAIL,
        member.getPrimaryEmail());
    execution.setVariable(ProcessContextParameterConstants.BUSINESS_NAME,
        orgToAssociate.getBusinessName());
    execution.setVariable(ProcessContextParameterConstants.TO_EMAIL,
        member.getPrimaryEmail());
    execution.setVariable(ProcessContextParameterConstants.MEMBER_FIRSTNAME,
        member.getFirstName());
    execution.setVariable(ProcessContextParameterConstants.MEMBER_LASTNAME,
        member.getLastName());
    execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
        ActionResultType.valid.toString());
  }

}
