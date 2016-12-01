package com.similan.process.action;

import java.util.EnumSet;

import lombok.extern.slf4j.Slf4j;

import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.EmployeeRole;
import com.similan.domain.entity.community.SocialEmployee;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.util.AttributeConstantUtil;
import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.entity.util.WorkflowTransientStateType;
import com.similan.domain.repository.community.SocialEmployeeRepository;
import com.similan.domain.repository.community.SocialOrganizationRepository;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.domain.repository.util.WorkflowTransientStateRepository;
import com.similan.framework.workflow.ProcessContextParameterConstants;
import com.similan.service.api.community.dto.basic.SocialEmployeeType;

@Slf4j
@Component
public class AnalyzeBusinessAssociationUrlValidationAction implements
    ActivityBehaviour {

  private static final long serialVersionUID = 1L;

  @Autowired
  private SocialPersonRepository personRepository;
  @Autowired
  private SocialOrganizationRepository organizationRepository;
  @Autowired
  private WorkflowTransientStateRepository memberTransientStateRepository;
  @Autowired
  private SocialEmployeeRepository socialEmployeeRepository;

  public void execute(ActivityExecution execution) throws Exception {
    Long memberId = (Long) execution
        .getVariable(ProcessContextParameterConstants.MEMBER_ID);
    Boolean approved = (Boolean) execution
        .getVariable(ProcessContextParameterConstants.BUSINESS_ASSOCIATION_APPROVAL);

    log.info("Associating org with member " + memberId + " status "
        + approved);

    if (memberId != null) {

      SocialPerson member = this.personRepository.findOne(memberId);
      WorkflowTransientState tranState = memberTransientStateRepository
          .findByMemberIdAndType(memberId,
              WorkflowTransientStateType.MemberAssociationWithOrg);

      String orgId = tranState.getAttributeByName(
          ProcessContextParameterConstants.ORGANIZATION_ID).getAttributeValue();

      SocialOrganization organization = this.organizationRepository
          .findOne(Long.valueOf(orgId));

      String employeeRole = tranState.getAttributeByName(
          AttributeConstantUtil.MEMBER_ORG_ASSOCIATION_ROLE)
          .getAttributeValue();

      SocialEmployeeType employeeRoleType = SocialEmployeeType
          .valueOf(employeeRole);

      execution.setVariable(ProcessContextParameterConstants.TO_EMAIL,
          member.getPrimaryEmail());
      execution.setVariable(ProcessContextParameterConstants.BUSINESS_NAME,
          organization.getBusinessName());
      execution.setVariable(ProcessContextParameterConstants.INVITEE_FIRSTNAME,
          member.getFirstName());

      if (approved == true) {

        SocialEmployee employee = this.socialEmployeeRepository.create(
            employeeRoleType, organization, member,
            EnumSet.of(EmployeeRole.REGULAR));
        this.socialEmployeeRepository.save(employee);
        member.setEmployer(employee);

        this.personRepository.save(member);
        this.organizationRepository.save(organization);

        execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
            ActionResultType.valid.toString());

        log.info("Employee role created "
            + employee.getContactTo().getDisplayName() + " action result "
            + ActionResultType.valid.toString());

      } else {
        execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
            ActionResultType.inValid.toString());
      }

      this.memberTransientStateRepository.delete(tranState);
    }

  }

}
