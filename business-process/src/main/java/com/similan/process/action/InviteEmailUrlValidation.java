package com.similan.process.action;

import java.util.EnumSet;

import lombok.extern.slf4j.Slf4j;

import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.Account;
import com.similan.domain.entity.community.EmployeeRole;
import com.similan.domain.entity.community.MemberState;
import com.similan.domain.entity.community.SocialContact;
import com.similan.domain.entity.community.SocialEmployee;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.entity.util.WorkflowTransientStateType;
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
public class InviteEmailUrlValidation implements ActivityBehaviour {
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
    log.info("Member to be validated " + memberId);

    if (memberId != null) {

      SocialPerson member = this.personRepository.findOne(memberId);
      log.info("Social person to be validated " + member.getFirstName());

      WorkflowTransientState tranState = memberTransientStateRepository
          .findByMemberIdAndType(memberId,
              WorkflowTransientStateType.MemberInvite);

      if (tranState != null) {

        execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
            ActionResultType.valid.toString());

        member.setState(MemberState.Active);
        member.setFirstName(tranState.getAttributeByName(
            ProcessContextParameterConstants.MEMBER_FIRSTNAME)
            .getAttributeValue());
        member.setLastName(tranState.getAttributeByName(
            ProcessContextParameterConstants.MEMBER_LASTNAME)
            .getAttributeValue());
        member
            .setCompany(tranState.getAttributeByName(
                ProcessContextParameterConstants.BUSINESS_NAME)
                .getAttributeValue());
        member.setPrimaryEmail(tranState.getAttributeByName(
            ProcessContextParameterConstants.EMAIL).getAttributeValue());

        if (member.getMemberAccount() == null) {
          Account account = new Account();
          account.setUserName(member.getPrimaryEmail());
          member.setMemberAccount(account);
        }

        member.getMemberAccount().setPassword(
            tranState.getAttributeByName(
                ProcessContextParameterConstants.PASSWORD).getAttributeValue());

        this.personRepository.save(member);
        Long inviterId = Long.valueOf(tranState.getAttributeByName(
            ProcessContextParameterConstants.INVITER_ID).getAttributeValue());
        String inviteType = tranState.getAttributeByName(
            ProcessContextParameterConstants.MEMBER_INVITE_TYPE)
            .getAttributeValue();

        log.info("Member is joining by invite process " + memberId);
        SocialPerson inviter = this.personRepository.findOne(inviterId);

        // relationships r bidirectional
        SocialContact contact = this.socialContactRepository.create(inviter,
            member);
        contact.setContactType(ContactType.valueOf(inviteType));
        this.socialContactRepository.save(contact);

        // check the collegue sitiation now
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
      } else {
        execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
            ActionResultType.inValid.toString());
      }
    } else {

      execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
          ActionResultType.inValid.toString());

    }

  }

}
