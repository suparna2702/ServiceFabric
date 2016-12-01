package com.similan.process.action;

import java.util.EnumSet;

import lombok.extern.slf4j.Slf4j;

import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.EmployeeRole;
import com.similan.domain.entity.community.SocialContact;
import com.similan.domain.entity.community.SocialEmployee;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.repository.community.SocialContactRepository;
import com.similan.domain.repository.community.SocialEmployeeRepository;
import com.similan.domain.repository.community.SocialOrganizationRepository;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.domain.repository.util.WorkflowTransientStateRepository;
import com.similan.framework.workflow.ProcessContextParameterConstants;
import com.similan.service.api.community.dto.basic.SocialEmployeeType;
import com.similan.service.api.connection.dto.basic.ContactType;

@Slf4j
@Component
public class ExistingMemberAcceptedInviteEmailUrlValidation implements
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
  @Autowired
  private SocialContactRepository socialContactRepository;

  /**
   * @return
   */
  public void execute(ActivityExecution execution) throws Exception {
    Long memberId = (Long) execution
        .getVariable(ProcessContextParameterConstants.MEMBER_ID);
    log.info("Start ExistingMemberAcceptedInviteEmailUrlValidation");

    if (memberId != null) {

      SocialPerson member = this.personRepository.findOne(memberId);
      log.info("Social person to be validated " + member.getFirstName());

      WorkflowTransientState tranState = memberTransientStateRepository
          .findByMemberIdAndProcessInstanceId(memberId, execution.getId());

      if (tranState != null) {

        Long inviterId = Long.valueOf(tranState.getAttributeByName(
            ProcessContextParameterConstants.INVITER_ID).getAttributeValue());
        String inviteType = tranState.getAttributeByName(
            ProcessContextParameterConstants.MEMBER_INVITE_TYPE)
            .getAttributeValue();
        String inviteeFirstName = tranState.getAttributeByName(
            ProcessContextParameterConstants.INVITEE_FIRSTNAME)
            .getAttributeValue();
        String inviteeLastName = tranState.getAttributeByName(
            ProcessContextParameterConstants.INVITEE_LASTNAME)
            .getAttributeValue();

        log.info("Member is joining by invite process " + memberId);
        SocialPerson inviter = this.personRepository.findOne(inviterId);

        SocialContact contact = this.socialContactRepository.create(inviter,
            member);
        contact.setContactType(ContactType.valueOf(inviteType));
        this.socialContactRepository.save(contact);

        // check whether its a collegue type if so we have to create an
        // appropriate
        // relationship with between Invitee and Inviter's employer
        // point to note that in case this member already has am existing
        // employer
        // we dont do anything for now

        if (inviteType.equalsIgnoreCase(ContactType.Collegue.toString()) == true) {
          SocialEmployee employeeRel = inviter.getEmployer();
          if (employeeRel != null) {

            // 1st check whether the inviter has an employer
            SocialEmployee inviterEmployeRel = member.getEmployer();

            if (inviterEmployeRel == null) {
              SocialOrganization employer = (SocialOrganization) employeeRel
                  .getContactFrom();

              // create an employer for this member
              SocialEmployee inviteeEmployee = this.socialEmployeeRepository
                  .create(SocialEmployeeType.Admin, employer, member,
                      EnumSet.of(EmployeeRole.REGULAR));
              this.socialEmployeeRepository.save(inviteeEmployee);
              member.setEmployer(inviteeEmployee);
              this.personRepository.save(member);

            }

          }
        }

        memberTransientStateRepository.delete(tranState);

        execution.setVariable(
            ProcessContextParameterConstants.INVITEE_FIRSTNAME,
            inviteeFirstName);
        execution.setVariable(
            ProcessContextParameterConstants.INVITEE_LASTNAME, inviteeLastName);
        execution.setVariable(
            ProcessContextParameterConstants.MEMBER_FIRSTNAME,
            inviter.getFirstName());
        execution.setVariable(ProcessContextParameterConstants.MEMBER_LASTNAME,
            inviter.getLastName());
      }
    }
    log.info("Finished ExistingMemberAcceptedInviteEmailUrlValidation");

  }
}
