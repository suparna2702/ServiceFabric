package com.similan.process.action;

import java.util.HashSet;
import java.util.Set;
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
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.service.GeoCoderServiceImpl;
import com.similan.framework.workflow.ProcessContextParameterConstants;
import com.similan.service.api.community.dto.basic.BusinessAccountSubscriptionType;
import com.similan.service.api.community.dto.basic.SocialEmployeeType;
import com.similan.service.api.community.dto.error.BusinessErrorCode;
import com.similan.service.api.community.dto.error.BusinessException;

@Slf4j
@Component
public class BusinessCreationAction implements ActivityBehaviour {
  private static final long serialVersionUID = 1L;

  @Autowired
  private PlatformCommonSettings platformCommonSettings;
  @Autowired
  private SocialPersonRepository personRepository;
  @Autowired
  private SocialOrganizationRepository organizationRepository;
  @Autowired
  private WorkflowTransientStateRepository memberTransientStateRepository;
  @Autowired
  private GeoCoderServiceImpl geoCoderService;
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

    if (memberId != null) {

      // find the admin
      SocialPerson member = this.personRepository.findOne(memberId);
      WorkflowTransientState tranState = memberTransientStateRepository
          .findByProcessInstance(execution.getId());

      /*
       * This attribute only be valid for member inviting business but this
       * action is shared between two work flows
       */
      WorkflowTransientStateAttribute invterAttribute = tranState
          .getAttributeByName(ProcessContextParameterConstants.INVITER_ID);
      if (invterAttribute != null) {
        invterAttribute.getAttributeValue();
      }

      String businessAccountType = BusinessAccountSubscriptionType.BUSINESS_STANDARD;

      // lets see anything have been specified or not
      WorkflowTransientStateAttribute businessAccountStateAttr = tranState
          .getAttributeByName(ProcessContextParameterConstants.BUSINESS_ACCOUNT_TYPE);
      if (businessAccountStateAttr != null) {
        businessAccountType = businessAccountStateAttr.getAttributeValue();
      }

      // get all the stored values from state
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
      if (tranState
          .getAttributeByName(ProcessContextParameterConstants.ORG_INVITE_ASSOCIATION_TYPE) != null) {
        tranState.getAttributeByName(
            ProcessContextParameterConstants.ORG_INVITE_ASSOCIATION_TYPE)
            .getAttributeValue();

      }

      log.info("business name " + businessName);

      if (this.organizationRepository.findOrgByName(businessName) != null) {
        log.info("business exists = " + businessName
            + " cannot create business ");
        throw new BusinessException(BusinessErrorCode.DUPLICATE_BUSINESS_NAME,
            "Cannot create Business since it exists ");
      }

      String name = UUID.randomUUID().toString();
      SocialOrganization organization = this.organizationRepository.create(
          OrganizationType.RESELLER, name);

      organization.setOrganizationType(OrganizationType.RESELLER);
      organization.setBusinessName(businessName);
      organization.setPrimaryEmail(businessEmail);
      organization.setBusinessPhoneNumber(businessPhoneNumber);
      organization.setStatus(OrganizationStatus.Active);
      organization.setVisibilityType(SocialMemberVisibilityType.VisiblePublic);

      // setup the account
      if (StringUtils.isBlank(businessAccountType)) {
        BusinessAccountType accountType = businessAccountTypeRepository
            .findByName(BusinessAccountSubscriptionType.BUSINESS_STANDARD);
        organization.setAccountType(accountType);
      } else {
        BusinessAccountType accountType = businessAccountTypeRepository
            .findByName(businessAccountType);
        organization.setAccountType(accountType);
      }

      this.organizationRepository.save(organization);

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

      // set the employee
      Set<EmployeeRole> roleSet = new HashSet<EmployeeRole>();
      roleSet.add(EmployeeRole.BUSINESS_ADMIN);
      roleSet.add(EmployeeRole.MANAGEMENT_WORKSPACE_ADMIN);
      roleSet.add(EmployeeRole.PARTNER_PROGRAM_ADMIN);

      SocialEmployee socialEmployee = socialEmployeeRepository.create(
          SocialEmployeeType.Admin, organization, member, roleSet);
      socialEmployeeRepository.save(socialEmployee);

      member.setEmployer(socialEmployee);
      this.personRepository.save(member);

      String hostUrl = platformCommonSettings.getPortalApplcationUrl()
          .getValue();
      StringBuilder urlBusinessPublicProfile = new StringBuilder();
      urlBusinessPublicProfile.append(hostUrl).append(
          "business/businessPublicProfile.xhtml");

      execution.setVariable(ProcessContextParameterConstants.MEMBER_FIRSTNAME,
          member.getFirstName());
      execution.setVariable(ProcessContextParameterConstants.MEMBER_LASTNAME,
          member.getLastName());
      execution.setVariable(ProcessContextParameterConstants.BUSINESS_EMAIL,
          businessEmail);
      execution.setVariable(ProcessContextParameterConstants.TO_EMAIL,
          member.getPrimaryEmail());
      execution.setVariable(ProcessContextParameterConstants.BUSINESS_NAME,
          businessName);
      execution.setVariable(
          ProcessContextParameterConstants.BUSINESS_PHONE_NUMBER,
          businessPhoneNumber);
      execution.setVariable(
          ProcessContextParameterConstants.BUSINESS_PUBLIC_PROFILE,
          urlBusinessPublicProfile.toString());

      execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT,
          ActionResultType.valid.toString());

      this.memberTransientStateRepository.delete(tranState);

    }
  }

}
