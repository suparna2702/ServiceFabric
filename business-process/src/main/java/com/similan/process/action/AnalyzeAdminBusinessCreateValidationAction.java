package com.similan.process.action;

import java.util.EnumSet;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.acccount.BusinessAccountType;
import com.similan.domain.entity.common.Address;
import com.similan.domain.entity.community.EmployeeRole;
import com.similan.domain.entity.community.OrganizationStatus;
import com.similan.domain.entity.community.OrganizationType;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialContact;
import com.similan.domain.entity.community.SocialEmployee;
import com.similan.domain.entity.community.SocialMemberVisibilityType;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.util.AddressDto;
import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.entity.util.WorkflowTransientStateAttribute;
import com.similan.domain.repository.account.BusinessAccountTypeRepository;
import com.similan.domain.repository.community.SocialContactRepository;
import com.similan.domain.repository.community.SocialEmployeeRepository;
import com.similan.domain.repository.community.SocialOrganizationRepository;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.domain.repository.util.WorkflowTransientStateRepository;
import com.similan.framework.service.GeoCoderService;
import com.similan.framework.workflow.ProcessContextParameterConstants;
import com.similan.service.api.community.dto.basic.BusinessAccountSubscriptionType;
import com.similan.service.api.community.dto.basic.SocialEmployeeType;
import com.similan.service.api.connection.dto.basic.ContactType;

@Slf4j
@Component
public class AnalyzeAdminBusinessCreateValidationAction implements
    ActivityBehaviour {
  private static final long serialVersionUID = 1L;

  @Autowired
  private SocialPersonRepository personRepository;
  @Autowired
  private SocialOrganizationRepository organizationRepository;
  @Autowired
  private WorkflowTransientStateRepository memberTransientStateRepository;
  @Autowired
  private GeoCoderService geoCoderService;
  @Autowired
  private SocialEmployeeRepository socialEmployeeRepository;
  @Autowired
  private SocialContactRepository socialContactRepository;
  @Autowired
  private BusinessAccountTypeRepository businessAccountTypeRepository;

  public void execute(ActivityExecution execution) throws Exception {

    Long memberId = (Long) execution
        .getVariable(ProcessContextParameterConstants.MEMBER_ID);
    log.info("member id " + memberId + " process instance id "
        + execution.getId());

    Boolean approved = Boolean.TRUE;

    if (memberId != null) {

      //find the admin
      SocialPerson member = this.personRepository.findOne(memberId);
      WorkflowTransientState tranState = memberTransientStateRepository
          .findByProcessInstance(execution.getId());

      String memberInviterId = null;

      /*
       * This attribute only be valid for member inviting business but this
       * action is shared between two work flows
       */
      WorkflowTransientStateAttribute invterAttribute = tranState
          .getAttributeByName(ProcessContextParameterConstants.INVITER_ID);
      if (invterAttribute != null) {
        memberInviterId = invterAttribute.getAttributeValue();
      }

      String businessAccountType = BusinessAccountSubscriptionType.BUSINESS_STANDARD;
      
      //lets see anything have been specified or not
      WorkflowTransientStateAttribute businessAccountStateAttr = tranState.getAttributeByName(
          ProcessContextParameterConstants.BUSINESS_ACCOUNT_TYPE);
      if(businessAccountStateAttr != null){
        businessAccountType = businessAccountStateAttr.getAttributeValue();
      }

      String businessName = tranState.getAttributeByName(
          ProcessContextParameterConstants.BUSINESS_NAME).getAttributeValue();
      String businessEmail = tranState.getAttributeByName(
          ProcessContextParameterConstants.BUSINESS_EMAIL).getAttributeValue();
      String businessPhoneNumber = tranState.getAttributeByName(
          ProcessContextParameterConstants.BUSINESS_PHONE_NUMBER)
          .getAttributeValue();
      String businessAddrStreet = tranState.getAttributeByName(
          ProcessContextParameterConstants.BUSINESS_ADDR_STREET)
          .getAttributeValue();
      String businessAddrCity = tranState.getAttributeByName(
          ProcessContextParameterConstants.BUSINESS_ADDR_CITY)
          .getAttributeValue();
      String businessAddrState = tranState.getAttributeByName(
          ProcessContextParameterConstants.BUSINESS_ADDR_STATE)
          .getAttributeValue();
      String businessAddrCountry = tranState.getAttributeByName(
          ProcessContextParameterConstants.BUSINESS_ADDR_COUNTRY)
          .getAttributeValue();
      String businessAddrZipCode = tranState.getAttributeByName(
          ProcessContextParameterConstants.BUSINESS_ADDR_ZIPCODE)
          .getAttributeValue();
      String associationType = ContactType.Other.toString();
      if (tranState
          .getAttributeByName(ProcessContextParameterConstants.ORG_INVITE_ASSOCIATION_TYPE) != null) {
        associationType = tranState.getAttributeByName(
            ProcessContextParameterConstants.ORG_INVITE_ASSOCIATION_TYPE)
            .getAttributeValue();

      }

      log.info("business name " + businessName);

      SocialOrganization organization = this.organizationRepository
          .findOrgByName(businessName);
      boolean existsandactive = (organization != null && organization
          .getStatus() == OrganizationStatus.Active);

      log.info("business exists = " + existsandactive);
      if (approved == true && !existsandactive) {
        if (organization == null) {
          String name = UUID.randomUUID().toString();
          organization = this.organizationRepository.create(
              OrganizationType.UNSPECIFIED, name);
        }

        organization.setOrganizationType(OrganizationType.UNSPECIFIED);
        organization.setBusinessName(businessName);
        organization.setPrimaryEmail(businessEmail);
        organization.setBusinessPhoneNumber(businessPhoneNumber);
        organization.setStatus(OrganizationStatus.Active);
        organization
            .setVisibilityType(SocialMemberVisibilityType.VisiblePublic);

        this.organizationRepository.save(organization);

        // setup the account
        if (StringUtils.isBlank(businessAccountType)) {
          BusinessAccountType accountType = businessAccountTypeRepository
              .findByName(BusinessAccountSubscriptionType.BUSINESS_STANDARD);
          organization.setAccountType(accountType);
        }
        else {
          BusinessAccountType accountType = businessAccountTypeRepository
              .findByName(businessAccountType);
          organization.setAccountType(accountType);
        }

        this.organizationRepository.save(organization);
        
        SocialEmployee socialEmployee = socialEmployeeRepository.create(
            SocialEmployeeType.Admin, organization, member,
            EnumSet.of(EmployeeRole.BUSINESS_ADMIN));
        socialEmployeeRepository.save(socialEmployee);

        member.setEmployer(socialEmployee);
        this.personRepository.save(member);

        AddressDto addrDto = new AddressDto();
        addrDto.setStreet(businessAddrStreet);
        addrDto.setCity(businessAddrCity);
        addrDto.setState(businessAddrState);
        addrDto.setCountry(businessAddrCountry);
        addrDto.setZipCode(businessAddrZipCode);

        Address addr = this.geoCoderService.getVerifiedAddressFromDto(addrDto);
        if (addr != null) {
          addr.setAddrName(businessName);
          organization.getAddresses().add(addr);
        }

        /* update the relationship */
        this.organizationRepository.save(organization);
        this.personRepository.save(member);

        execution.setVariable(
            ProcessContextParameterConstants.MEMBER_FIRSTNAME,
            member.getFirstName());
        execution.setVariable(ProcessContextParameterConstants.BUSINESS_EMAIL,
            businessEmail);
        execution.setVariable(ProcessContextParameterConstants.TO_EMAIL,
            member.getPrimaryEmail());
        execution.setVariable(ProcessContextParameterConstants.BUSINESS_NAME,
            businessName);
        execution.setVariable(
            ProcessContextParameterConstants.BUSINESS_PHONE_NUMBER,
            businessPhoneNumber);
        execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
            ActionResultType.valid.toString());
        /* business invite case */
        if (memberInviterId != null) {

          /* create social contact for org and inviter */
          SocialPerson memberInviter = this.personRepository.findOne(Long
              .valueOf(memberInviterId));
          SocialContact contactMemberInviter = this.socialContactRepository
              .create(memberInviter, organization);
          socialContactRepository.save(contactMemberInviter);

          /* create a contact for the organization */
          SocialContact contactOrg = this.socialContactRepository.create(
              organization, memberInviter);
          this.socialContactRepository.save(contactOrg);

          /* create a business contact for the inviter's org */
          if (memberInviter.getEmployer() != null) {
            SocialOrganization employer = (SocialOrganization) memberInviter
                .getEmployer().getContactFrom();
            SocialContact contactBusiness = this.socialContactRepository
                .create((SocialActor) employer, (SocialActor) organization);

            if (StringUtils.isNotEmpty(associationType)) {
              contactBusiness.setContactType(ContactType
                  .valueOf(associationType));
            } else {
              contactBusiness.setContactType(ContactType.Other);
            }

            this.socialContactRepository.save(contactBusiness);

          }

        }
      } else {
        execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
            ActionResultType.inValid.toString());
      }

      this.memberTransientStateRepository.delete(tranState);

    }
  }

}
