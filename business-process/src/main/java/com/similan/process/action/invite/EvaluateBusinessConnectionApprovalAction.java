package com.similan.process.action.invite;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.SocialContact;
import com.similan.domain.entity.community.SocialEmployee;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.repository.community.SocialContactRepository;
import com.similan.domain.repository.community.SocialOrganizationRepository;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.domain.repository.util.WorkflowTransientStateRepository;
import com.similan.framework.workflow.ProcessContextParameterConstants;
import com.similan.service.api.connection.dto.basic.ContactType;

@Slf4j
@Component
public class EvaluateBusinessConnectionApprovalAction implements
    ActivityBehaviour {
  private static final long serialVersionUID = 1L;

  @Autowired
  private WorkflowTransientStateRepository memberTransientStateRepository;
  @Autowired
  private SocialOrganizationRepository organizationRepository;
  @Autowired
  private SocialPersonRepository personRepository;
  @Autowired
  private SocialContactRepository socialContactRepository;

  /**
   * 1. If approved then create a connection 2. If rejected do nothing 3. In
   * both case email will be sent to the inviting party
   */
  public void execute(ActivityExecution execution) throws Exception {

    log.info("Evaluating business connection approval ");
    WorkflowTransientState tranState = memberTransientStateRepository
        .findByProcessInstance(execution.getId());
    /* get approval status from context */
    Boolean approved = (Boolean) execution
        .getVariable(ProcessContextParameterConstants.BUSINESS_CONNECTION_APPROVAL);
    if (tranState != null) {

      /* we have to send the email */
      String inviteeEmail = tranState.getAttributeByName(
          ProcessContextParameterConstants.INVITEE_EMAIL).getAttributeValue();
      log.info("Invitee email " + inviteeEmail);
      String inviterId = tranState.getAttributeByName(
          ProcessContextParameterConstants.INVITER_ID).getAttributeValue();
      String associationType = tranState.getAttributeByName(
          ProcessContextParameterConstants.ORG_INVITE_ASSOCIATION_TYPE)
          .getAttributeValue();

      SocialOrganization contact = this.organizationRepository
          .findOrgByEmail(inviteeEmail);

      /* we have to send the INVITER of the result */
      SocialPerson inviterPerson = this.personRepository.findOne(Long
          .valueOf(inviterId));
      execution.setVariable(ProcessContextParameterConstants.TO_EMAIL,
          inviterPerson.getPrimaryEmail());
      SocialOrganization contactOwner = null;
      if (inviterPerson != null) {
        SocialEmployee inviterEmployee = inviterPerson.getEmployer();
        if (inviterEmployee != null) {
          contactOwner = (SocialOrganization) inviterEmployee.getContactFrom();
          log.info("Contact owner " + contactOwner.getBusinessName());
        }
      }

      if (approved != true) {
        execution
            .setVariable(
                ProcessContextParameterConstants.BUSINESS_ASSOCIATION_APPROVAL_STATUS,
                "rejected");
      } else {
        execution
            .setVariable(
                ProcessContextParameterConstants.BUSINESS_ASSOCIATION_APPROVAL_STATUS,
                "approved");
        /* create the association */
        if (contactOwner != null && contact != null) {
          SocialContact businessContact = this.socialContactRepository.create(
              contactOwner, contact);
          log.info("Association type " + associationType);
          if (StringUtils.isNotEmpty(associationType)) {
            businessContact
                .setContactType(ContactType.valueOf(associationType));
          } else {
            businessContact.setContactType(ContactType.Other);
          }
          this.socialContactRepository.save(businessContact);

        }
      }

    }

    this.memberTransientStateRepository.delete(tranState);
  }

}
