package com.similan.service.impl;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.opengis.geometry.coordinate.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.acccount.BusinessAccountType;
import com.similan.domain.entity.common.Address;
import com.similan.domain.entity.community.ExternalBusinessPortal;
import com.similan.domain.entity.community.OrganizationType;
import com.similan.domain.entity.community.PartnerSettingsConfiguration;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialContact;
import com.similan.domain.entity.community.SocialEmbeddedIdentity;
import com.similan.domain.entity.community.SocialEmployee;
import com.similan.domain.entity.community.SocialMemberVisibilityType;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.lead.ContactLead;
import com.similan.domain.entity.lead.LeadStatusType;
import com.similan.domain.entity.lead.LeadType;
import com.similan.domain.entity.leadcapture.LeadCaptureQuestion;
import com.similan.domain.entity.leadcapture.LeadCaptureQuestionChoice;
import com.similan.domain.entity.leadcapture.LeadCaptureQuestionChoiceResponse;
import com.similan.domain.entity.leadcapture.LeadCaptureQuestionResponse;
import com.similan.domain.entity.leadcapture.LeadCaptureResponse;
import com.similan.domain.entity.leadcapture.LeadCaptureWizzardTemplate;
import com.similan.domain.entity.partner.PartnerSearchSettingsConfiguration;
import com.similan.domain.entity.process.BusinessProcessDefinition;
import com.similan.domain.entity.util.AddressDto;
import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.entity.util.WorkflowTransientStateAttribute;
import com.similan.domain.entity.util.WorkflowTransientStateType;
import com.similan.domain.repository.account.BusinessAccountTypeRepository;
import com.similan.domain.repository.common.AddressRepository;
import com.similan.domain.repository.community.ExternalBusinessPortalRepository;
import com.similan.domain.repository.community.PartnerSettingsConfigurationRepository;
import com.similan.domain.repository.community.SocialContactRepository;
import com.similan.domain.repository.community.SocialEmbeddedIdentityRepository;
import com.similan.domain.repository.community.SocialEmployeeRepository;
import com.similan.domain.repository.community.SocialOrganizationRepository;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.domain.repository.lead.ContactLeadRepository;
import com.similan.domain.repository.leadcapture.LeadCaptureQuestionChoiceRepository;
import com.similan.domain.repository.leadcapture.LeadCaptureQuestionChoiceResponseRepository;
import com.similan.domain.repository.leadcapture.LeadCaptureQuestionRepository;
import com.similan.domain.repository.leadcapture.LeadCaptureQuestionResponseRepository;
import com.similan.domain.repository.leadcapture.LeadCaptureResponseRepository;
import com.similan.domain.repository.leadcapture.LeadCaptureWizzardTemplateRepository;
import com.similan.domain.repository.partner.PartnerPreQualificationQuestionResponseRepository;
import com.similan.domain.repository.partner.PartnerProgramDefinitionRepository;
import com.similan.domain.repository.partner.PartnerProgramRequiredAttributeRepository;
import com.similan.domain.repository.partner.PartnerProgramResponseRepository;
import com.similan.domain.repository.partner.PartnerSearchSettingsConfigurationRepository;
import com.similan.domain.repository.partner.PartnershipRepository;
import com.similan.domain.repository.process.BusinessProcessDefinitionRepository;
import com.similan.domain.repository.util.WorkflowTransientStateRepository;
import com.similan.domain.util.GeometryUtils;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.dto.NewBusinessDto;
import com.similan.framework.dto.OrganizationAddressDto;
import com.similan.framework.dto.OrganizationBasicInfoDto;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.UpdateBusinessBrandingDto;
import com.similan.framework.dto.community.ExternalBusinessPortalDto;
import com.similan.framework.dto.embed.EmbeddedUrlDto;
import com.similan.framework.dto.leadcapture.LeadCaptureQuestionChoiceDto;
import com.similan.framework.dto.leadcapture.LeadCaptureQuestionDto;
import com.similan.framework.dto.leadcapture.LeadCaptureWizzardDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.partner.PartnerProgramDefinitionDto;
import com.similan.framework.dto.partner.PartnerSearchSettingsConfigurationDto;
import com.similan.framework.dto.partner.PartnerSettingsConfigurationDto;
import com.similan.framework.dto.review.ReviewCommentSummeryDto;
import com.similan.framework.geo.Geocoder;
import com.similan.framework.manager.email.EmailManager;
import com.similan.framework.util.BeanPropertyUpdator;
import com.similan.framework.util.EmailValidator;
import com.similan.framework.workflow.ProcessContextParameterConstants;
import com.similan.framework.workflow.api.WorkflowInitialRequest;
import com.similan.framework.workflow.api.WorkflowManager;
import com.similan.framework.workflow.api.WorkflowResponse;
import com.similan.framework.workflow.api.WorkflowResponseCode;
import com.similan.framework.workflow.api.WorkflowResumeRequest;
import com.similan.service.api.OrganizationManagementService;
import com.similan.service.api.community.dto.basic.BusinessAccountSubscriptionType;
import com.similan.service.exception.ContactAlreadyExistsException;
import com.similan.service.exception.CoreServiceException;
import com.similan.service.marshallers.BusinessMarshaller;
import com.similan.service.marshallers.ExternalBusinessPortalMarchaller;
import com.similan.service.marshallers.PartnerProgramMarshaller;

@Slf4j
public class OrganizationManagementServiceImpl implements
    OrganizationManagementService {
  @Autowired
  private ExternalBusinessPortalRepository externalBusinessPortalRepository;
  @Autowired
  private ExternalBusinessPortalMarchaller externalBusinessPortalMarchaller;
  @Autowired
  private BusinessAccountTypeRepository businessAccountTypeRepository;
  @Autowired
  private WorkflowTransientStateRepository transientStateRepository;
  @Autowired
  private PartnerManagementServiceImpl partnerManagementService;
  @Autowired
  private SocialOrganizationRepository organizationRepository;
  @Autowired
  private SocialPersonRepository memberRepository;
  @Autowired
  private AddressRepository addressRepository;
  @Autowired
  private BusinessProcessDefinitionRepository businessProcessDefinitionRepository;
  @Autowired
  private PartnerSearchSettingsConfigurationRepository partnerSearchSettingsRepository;
  @Autowired
  private WorkflowManager workflowManager;
  @Autowired
  @Qualifier("organizationBasicAttributeUpdaterInfo")
  private BeanPropertyUpdator orgInfoAttributeUpdater;
  @Autowired
  @Qualifier("organizationBasicAttributeUpdater")
  private BeanPropertyUpdator orgAttributeUpdater;
  @Autowired
  private PartnerProgramDefinitionRepository partnerProgramRepository;
  @Autowired
  private PartnerSettingsConfigurationRepository partnerConfigRepository;
  @Autowired
  private SocialEmbeddedIdentityRepository embeddedIdentityRepository;
  @Autowired
  private LeadCaptureWizzardTemplateRepository leadCaptureWizzardTemplateRepository;
  @Autowired
  private LeadCaptureQuestionRepository leadCaptureQuestionRepository;
  @Autowired
  private LeadCaptureQuestionChoiceRepository leadCaptureQuestionChoiceRepository;
  @Autowired
  private LeadCaptureResponseRepository leadCaptureResponseRepository;
  @Autowired
  private LeadCaptureQuestionResponseRepository leadCaptureQuestionResponseRepository;
  @Autowired
  private LeadCaptureQuestionChoiceResponseRepository leadCaptureQuestionChoiceResponseRepository;
  @Autowired
  private ContactLeadRepository contactLeadRepository;
  @Autowired
  private PartnershipRepository partnershipRepository;
  @Autowired
  private PlatformCommonSettings platformCommonSettings;
  @Autowired
  private PartnerProgramRequiredAttributeRepository partnerProgramRequiredAttributeRepository;
  @Autowired
  private PartnerPreQualificationQuestionResponseRepository partnerPreQualificationQuestionResponseRepository;
  @Autowired
  private PartnerProgramResponseRepository partnerProgramResponseRepository;
  @Autowired
  private Geocoder geocoder;
  @Autowired
  private EmailManager emailManager;
  @Autowired
  private PlatformCommonSettings commonSettings;
  @Autowired
  private PartnerProgramMarshaller partnerProgramMarshaller;
  @Autowired
  private SocialEmployeeRepository socialEmployeeRepository;
  @Autowired
  private SocialContactRepository socialContactRepository;
  @Autowired
  private BusinessMarshaller businessMarshaller;
  
  private JAXBContext jaxbContext = null;

  private Map<String, String> embeddedUrlMap = new HashMap<String, String>();


  public OrganizationDetailInfoDto getOrganizationFromEmbeddedIdentity(
      String uuID) throws Exception {
    SocialEmbeddedIdentity embeddedIdentity = this.embeddedIdentityRepository
        .findEmbeddedIdentity(uuID);
    if (embeddedIdentity == null) {
      throw new CoreServiceException("Embedded idenity with id " + uuID
          + " does not exist");
    }

    SocialActor orgActor = embeddedIdentity.getEmbeddedActor();
    if (orgActor == null) {
      throw new CoreServiceException("Embedded idenity with id " + uuID
          + " does not have associated actor");
    }

    if (orgActor instanceof SocialOrganization) {
      OrganizationDetailInfoDto retOrg = this.getOrganization(orgActor.getId());
      return retOrg;
    } else {
      throw new CoreServiceException("Embedded idenity with id " + uuID
          + " is not a business");
    }
  }

  public OrganizationBasicInfoDto getBasicOrganizationInfo(long orgId) {

    log.info("Getting basic org info for " + orgId);
    OrganizationBasicInfoDto basicOrgInfo = new OrganizationBasicInfoDto();
    SocialOrganization socialOrg = this.organizationRepository.findOne(orgId);

    /* copy and send */
    basicOrgInfo.setName(socialOrg.getBusinessName());
    basicOrgInfo.setEmail(socialOrg.getPrimaryEmail());
    basicOrgInfo.setId(socialOrg.getId());
    basicOrgInfo.setIndustry(socialOrg.getIndustry());
    basicOrgInfo.setLogoLocation(socialOrg.getLogoUrl());

    return basicOrgInfo;
  }

  /**
   * fetches or from DB
   */
  public OrganizationDetailInfoDto getOrganization(long orgId) throws Exception {

    SocialOrganization socialOrg = this.organizationRepository.findOne(orgId);
    if (socialOrg == null) {
      throw new CoreServiceException("Organization with id " + orgId
          + " does not exist");
    }

    OrganizationDetailInfoDto orgInfo = new OrganizationDetailInfoDto();
    orgInfo.setName(socialOrg.getName());

    /* update all the relevant fields */
    this.orgInfoAttributeUpdater.update(socialOrg, orgInfo);
    orgInfo.setPartnerBannerLocation(socialOrg.getPartnerBannerLocation());
    orgInfo.setPartnerBannerText(socialOrg.getPartnerBannerText());
    orgInfo.setPartnerWelcomeMessage(socialOrg.getPartnerWelcomeMessage());
    orgInfo.setPartnerBannerTextColor(socialOrg.getPartnerBannerTextColor());
    orgInfo.setShowPartners(socialOrg.getShowPartners());

    /* get all the organization location and copy (For enrique to review ) */
    List<Address> locationList = socialOrg.getAddresses();
    for (Address location : locationList) {
      double[] coordinates = null;
      if (location.getPosition() != null) {
        coordinates = GeometryUtils.toCoordinates(location.getPosition());
      }

      OrganizationAddressDto orgAddr = new OrganizationAddressDto(
          location.getId(), location.getCountry(), location.getCity(),
          location.getCounty(), "", location.getNumber() == null ? null
              : location.getNumber() + " " + location.getStreet(),
          location.getZipCode(), location.getState(),
          coordinates == null ? null : coordinates[1],
          coordinates == null ? null : coordinates[0]);

      orgAddr.setName(location.getAddrName());
      orgAddr.setLocationType(location.getLocationType());
      orgAddr.setLocationEmail(location.getLocationEmail());
      orgAddr.setLocationFax(location.getLocationFax());
      orgAddr.setLocationPhone(location.getLocationPhone());
      orgAddr.setLocationUrl(location.getLocationUrl());

      orgInfo.getLocations().add(orgAddr);
      log.info("org location " + orgAddr.getStreet());
    }

    /* set the first visible address */
    if ((orgInfo.getLocations() != null) && (orgInfo.getLocations().size() > 0)) {

      OrganizationAddressDto searchVisibleAddr = orgInfo.getLocations().get(0);
      log.info("Search visible addr " + searchVisibleAddr.getStreet());
      orgInfo.setPrimarySearchVisibleLocation(orgInfo.getLocations().get(0));
    }

    List<PartnerProgramDefinitionDto> participatingProgram = this.partnerManagementService
        .getParticipatingPartnerPrograms(socialOrg);
    orgInfo.setParticipatingPartnerPrograms(participatingProgram);
    orgInfo.setMemberVisibilityType(socialOrg.getVisibilityType());

    return orgInfo;
  }

  public List<MemberInfoDto> getTeamMembers(Long orgId) {
    SocialOrganization socialOrg = this.organizationRepository.findOne(orgId);
    return populateOrganizationEmployee(socialOrg);
  }

  public List<MemberInfoDto> populateOrganizationEmployee(SocialOrganization org) {
    List<MemberInfoDto> teamMembers = new ArrayList<MemberInfoDto>();
    if (org != null) {
      try {
        List<SocialEmployee> employeeList = this.socialEmployeeRepository
            .findByContactFrom(org);
        for (SocialEmployee employee : employeeList) {
          SocialPerson person = (SocialPerson) employee.getContactTo();
          MemberInfoDto teamMember = new MemberInfoDto();
          teamMember.setFirstName(person.getFirstName());
          teamMember.setLastName(person.getLastName());
          teamMember.setEmail(person.getPrimaryEmail());
          teamMember.setTitle(person.getRole());
          teamMember.setBusinessPhone(person.getBusinessPhone());
          teamMember.setMobilePhone(person.getMobilePhone());
          teamMember.setPhotoLocation(person.getPhotoLocation());

          teamMembers.add(teamMember);
        }
      } catch (Exception exp) {
        log.error("Cannot fetch employees", exp);
      }
    }

    return teamMembers;
  }

  @Override
  public List<OrganizationBasicInfoDto> getBusinessByVisibility(
      SocialMemberVisibilityType visibility) {

    List<OrganizationBasicInfoDto> activeBusinessList = new ArrayList<OrganizationBasicInfoDto>();
    List<SocialOrganization> orgList = this.organizationRepository
        .findAllOrgByVisibilityType(visibility);

    for (SocialOrganization business : orgList) {

      log.info("org retrieved " + business.getId());
      OrganizationBasicInfoDto orgTag = new OrganizationBasicInfoDto();
      orgTag.setId(business.getId());
      orgTag.setName(business.getBusinessName());
      orgTag.setLogoLocation(business.getLogoUrl());
      orgTag.setEmail(business.getPrimaryEmail());
      orgTag.setPhone(business.getBusinessPhoneNumber());
      orgTag.setMemberVisibilityType(business.getVisibilityType());
      orgTag.setCreationDate(business.getCreationDate());
      orgTag.setStatus(business.getStatus());

      if (business.getAccountType() != null) {
        orgTag.setAccountType(business.getAccountType().getName());
      } else {
        orgTag
            .setAccountType(BusinessAccountSubscriptionType.BUSINESS_STANDARD);
      }

      activeBusinessList.add(orgTag);
    }

    return activeBusinessList;
  }

  public List<SocialOrganization> findAll() {
    return this.organizationRepository.findAll();
  }

  /**
   * update the organization
   */
  public void updateOrganization(OrganizationDetailInfoDto orgInfo)
      throws Exception {

    if (StringUtils.isEmpty(orgInfo.getEmail()) == true) {
      throw new CoreServiceException(
          "Cannot save a business with NULL email address");
    } else {
      StringUtils.strip(orgInfo.getEmail());
      if (EmailValidator.validate(orgInfo.getEmail()) != true) {
        throw new CoreServiceException(
            "Cannot save a business with invalid email address "
                + orgInfo.getEmail());
      }
    }

    SocialOrganization socialOrg = this.organizationRepository.findOne(orgInfo
        .getId());
    log.info("Updating organization " + orgInfo.getId() + " name "
        + orgInfo.getBusinessName());

    /* basic attributes */
    this.orgAttributeUpdater.update(orgInfo, socialOrg);
    socialOrg.setPartnerBannerLocation(orgInfo.getPartnerBannerLocation());
    socialOrg.setPartnerBannerText(orgInfo.getPartnerBannerText());
    socialOrg.setPartnerWelcomeMessage(orgInfo.getPartnerWelcomeMessage());
    socialOrg.setShowPartners(orgInfo.getShowPartners());

    /*
     * location update 1. There can be new location check the Id and add them 2.
     * There can be deleted address. Remove them
     */

    /* Update org list */
    this.updateOrganizationLocation(socialOrg, orgInfo);
    this.organizationRepository.save(socialOrg);

  }

  /**
   * 
   * @param orgLocationList
   * @param addressListSrc
   * @throws Exception
   */
  private void updateOrganizationLocation(SocialOrganization socialOrg,
      OrganizationDetailInfoDto orgInfo) throws Exception {

    log.info("Updating organization location " + socialOrg.getId() + " name "
        + socialOrg.getBusinessName());

    /* Now edit and delete are not supported so only add */
    List<OrganizationAddressDto> addressListSrc = orgInfo.getLocations();
    log.info("Number of locations in source " + addressListSrc.size());

    List<Address> addressListDest = socialOrg.getAddresses();

    for (OrganizationAddressDto addr : addressListSrc) {

      /* if an existing address that hasn't changed, simply skip. */
      if (addr.getId() != Long.MIN_VALUE && addr.getChanged() == false) {
        continue;
      }

      /*
       * we are doing this to avoid edit. Here for update we are 1st deleting
       * and creating a new record
       */
      Iterator<Address> iterator = addressListDest.iterator();
      Address orgLocation = null;
      while (iterator.hasNext()) {
        Address location = iterator.next();
        if (location.getId() == addr.getId()) {
          orgLocation = location;
        }
      }

      AddressDto geoAddress = null;

      try {
        AddressDto[] address = null;
        if (addr.getCountry() != null) {
          address = geocoder.geocode(
              addr.getStreet() + ", " + addr.getCity() + ", " + addr.getState()
                  + " " + addr.getZipCode() + ", " + addr.getCountry(), null,
              null, "en");
        } else {
          AddressDto adto = new AddressDto();
          adto.setStreet(addr.getStreet());
          adto.setCity(addr.getCity());
          adto.setState(addr.getState());
          adto.setCountry(addr.getCountry());
          adto.setZipCode(addr.getZipCode());
          address = new AddressDto[] { adto };
        }
        log.info("Organization location found " + address.length);

        if (address.length >= 1) {

          geoAddress = address[0];
          addr.setCity(geoAddress.getCity());
          addr.setCountry(geoAddress.getCountry());
          addr.setCounty(geoAddress.getCounty());
          addr.setLatitude(geoAddress.getLatitude());
          addr.setLongitude(geoAddress.getLongitude());
          addr.setZipCode(geoAddress.getZipCode());

          log.info("Organization location to be created " + geoAddress);

        }
      } catch (Exception exp) {
        log.error("Cannot update org address : Geo coder error ", exp);
      }

      Position position = null;
      String area = null;

      String formattedAddress = null;
      String searchableAddress = null;

      if (geoAddress != null) {
        position = GeometryUtils.toPositon(geoAddress.getLongitude(),
            geoAddress.getLatitude());
        area = geoAddress.getArea();
        formattedAddress = geoAddress.getFormattedAddress();
        searchableAddress = geoAddress.getSearchableAddress();
      } else {
        formattedAddress = "";
        formattedAddress += StringUtils.isBlank(addr.getStreetNumber()) ? ""
            : addr.getStreetNumber();
        formattedAddress += StringUtils.isBlank(addr.getStreet()) ? ""
            : (StringUtils.isBlank(addr.getStreetNumber()) ? "" : " ")
                + addr.getStreetNumber();
        formattedAddress += StringUtils.isBlank(addr.getCounty()) ? "" : ", "
            + addr.getCounty();
        formattedAddress += StringUtils.isBlank(addr.getCity()) ? "" : ", "
            + addr.getCity();
        formattedAddress += StringUtils.isBlank(addr.getState()) ? "" : ", "
            + addr.getState();
        formattedAddress += StringUtils.isBlank(addr.getZipCode()) ? "" : " "
            + addr.getZipCode();
        formattedAddress += StringUtils.isBlank(addr.getCountry()) ? "" : ", "
            + addr.getCountry();
        searchableAddress = formattedAddress;
      }

      if (orgLocation == null) {
        orgLocation = this.addressRepository.create(addr.getName(),
            addr.getLocationType(), position, addr.getStreetNumber(),
            addr.getStreet(), addr.getCity(), area, addr.getCounty(),
            addr.getState(), addr.getZipCode(), addr.getCountry(),
            formattedAddress, searchableAddress);
        orgLocation.setLocationEmail(addr.getLocationEmail());
        orgLocation.setLocationFax(addr.getLocationFax());
        orgLocation.setLocationPhone(addr.getLocationPhone());
        orgLocation.setLocationUrl(addr.getLocationUrl());
        addressListDest.add(orgLocation);
        this.addressRepository.save(orgLocation);
        addr.setId(orgLocation.getId());
      } else {
        orgLocation.setPosition(position);
        orgLocation.setNumber(addr.getStreetNumber());
        orgLocation.setStreet(addr.getStreet());
        orgLocation.setCity(addr.getCity());
        orgLocation.setArea(area);
        orgLocation.setCounty(addr.getCounty());
        orgLocation.setState(addr.getState());
        orgLocation.setZipCode(addr.getZipCode());
        orgLocation.setCountry(addr.getCountry());
        orgLocation.setFormattedAddress(formattedAddress);
        orgLocation.setSearchableAddress(searchableAddress);
        orgLocation.setAddrName(addr.getName());
        orgLocation.setLocationType(addr.getLocationType());
        orgLocation.setLocationEmail(addr.getLocationEmail());
        orgLocation.setLocationFax(addr.getLocationFax());
        orgLocation.setLocationPhone(addr.getLocationPhone());
        orgLocation.setLocationUrl(addr.getLocationUrl());
      }

    }

    /* The delete loop */
    if (addressListDest != null && orgInfo.getDeletedAddressList().size() >= 0) {
      log.info("Number of locations in dest " + addressListDest.size());

      for (OrganizationAddressDto addr : orgInfo.getDeletedAddressList()) {
        log.info("Deleting address " + addr);

        Iterator<Address> iterator = addressListDest.iterator();
        while (iterator.hasNext()) {
          Address location = iterator.next();
          if (location.getId() == addr.getId()) {
            log.info("Removing location " + location.getId());
            iterator.remove();
            this.addressRepository.delete(location.getId());
          }
        }
      }

      /* we don't need them any more */
      orgInfo.getDeletedAddressList().clear();
    }
  }

  public List<SocialOrganization> getAllOrganization() {
    return this.organizationRepository.findAll();
  }

  public SocialOrganization createOrganization() {
    String uniqueName = UUID.randomUUID().toString();
    SocialOrganization socialOrg = this.organizationRepository.create(
        OrganizationType.UNSPECIFIED, uniqueName);
    this.organizationRepository.save(socialOrg);

    return socialOrg;
  }

  public void createBusinessEntityNotification(Long memberId, String pid,
      String notificationMsg, boolean approved) {

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put(ProcessContextParameterConstants.MEMBER_ID, memberId);
    parameters.put(
        ProcessContextParameterConstants.BUSINESS_ENTITY_NOTIFICATION_MSG,
        notificationMsg);

    /* approved */
    parameters.put(
        ProcessContextParameterConstants.BUSINESS_ASSOCIATION_APPROVAL,
        new Boolean(approved));

    parameters.put(ProcessContextParameterConstants.BUSINESS_ENTITY_APPROVAL,
        new Boolean(approved));

    WorkflowResumeRequest resumeRequest = new WorkflowResumeRequest();
    resumeRequest.setExecutionParameters(parameters);
    resumeRequest.setWorkflowInstanceId(pid);
    this.workflowManager.resume(resumeRequest);
  }

  public void associateBusinessEntityNotification(Long memberId, String pid,
      boolean approved) {

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put(ProcessContextParameterConstants.MEMBER_ID, memberId);

    /* approved */
    parameters.put(
        ProcessContextParameterConstants.BUSINESS_ASSOCIATION_APPROVAL,
        new Boolean(approved));

    WorkflowResumeRequest resumeRequest = new WorkflowResumeRequest();
    resumeRequest.setExecutionParameters(parameters);
    resumeRequest.setWorkflowInstanceId(pid);
    this.workflowManager.resume(resumeRequest);
  }

  public boolean isPendingBusinessCreateOrAssociateFlow(MemberInfoDto memberInfo) {
    boolean retValue = false;
    // check whether there is a business create workflow is pending
    // check whether a business association flow is pending
    List<WorkflowTransientState> transStateList = this.transientStateRepository
        .findByType(WorkflowTransientStateType.BusinessCreationByMember);

    for (WorkflowTransientState transState : transStateList) {
      WorkflowTransientStateAttribute attr = transState
          .getAttributeByName(ProcessContextParameterConstants.MEMBER_ID);
      if (attr != null) {
        if (attr.getAttributeValue().equalsIgnoreCase(
            memberInfo.getId().toString())) {
          return true;
        }
      }
    }

    List<WorkflowTransientState> transStateOrgAssocList = this.transientStateRepository
        .findByType(WorkflowTransientStateType.MemberAssociationWithOrg);
    for (WorkflowTransientState transState : transStateOrgAssocList) {
      WorkflowTransientStateAttribute attr = transState
          .getAttributeByName(ProcessContextParameterConstants.MEMBER_ID);
      if (attr != null) {
        if (attr.getAttributeValue().equalsIgnoreCase(
            memberInfo.getId().toString())) {
          return true;
        }
      }
    }

    return retValue;
  }

  @Override
  public boolean initiateOrganizationCreate(MemberInfoDto memberInfo,
      NewBusinessDto business) {

    /*
     * we have see whether a pending business process to create / specify
     * business is there
     */

    if (this.isPendingBusinessCreateOrAssociateFlow(memberInfo) == true) {
      throw new CoreServiceException(
          "An existing workflow is pending cannot initiate another one");
    }

    BusinessProcessDefinition businessCreationProcess = this.businessProcessDefinitionRepository
        .findByName("businessCreationByMember");
    log.info("Member creation business process "
        + businessCreationProcess.getName());

    WorkflowInitialRequest initialRequest = new WorkflowInitialRequest();
    initialRequest.setFlowDefinitionName(businessCreationProcess.getName());

    /* set the parameters */
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put(ProcessContextParameterConstants.MEMBER_ID,
        memberInfo.getId());
    parameters.put(ProcessContextParameterConstants.BUSINESS_NAME,
        business.getBusinessName());
    parameters.put(ProcessContextParameterConstants.BUSINESS_EMAIL,
        business.getBusinessPrimaryEmail());
    parameters.put(ProcessContextParameterConstants.BUSINESS_PHONE_NUMBER,
        business.getBusinessPhone());
    parameters.put(ProcessContextParameterConstants.BUSINESS_ADDR_STREET,
        business.getStreet());
    parameters.put(ProcessContextParameterConstants.BUSINESS_ADDR_CITY,
        business.getCity());
    parameters.put(ProcessContextParameterConstants.BUSINESS_ADDR_STATE,
        business.getState());
    parameters.put(ProcessContextParameterConstants.BUSINESS_ADDR_COUNTRY,
        business.getCountry());
    parameters.put(ProcessContextParameterConstants.BUSINESS_ADDR_ZIPCODE,
        business.getZipCode());

    /* set the parameters */
    initialRequest.setExecutionParameters(parameters);

    /* execute the workflow */
    this.workflowManager.initiate(initialRequest);

    return true;

  }

  public boolean associateOrganizationToMember(MemberInfoDto member,
      OrganizationBasicInfoDto selectedOrgTag) {

    BusinessProcessDefinition associationProcess = this.businessProcessDefinitionRepository
        .findByName("memberBusinessAssociation");
    log.info("Member association business process "
        + associationProcess.getName());

    WorkflowInitialRequest initialRequest = new WorkflowInitialRequest();
    initialRequest.setFlowDefinitionName(associationProcess.getName());

    /* set the parameters */
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put(ProcessContextParameterConstants.MEMBER_ID, member.getId());
    parameters.put(ProcessContextParameterConstants.ORGANIZATION_ID,
        selectedOrgTag.getId());
    parameters.put(
        ProcessContextParameterConstants.ORGANIZATION_MEMBER_ASSOCIATION_TYPE,
        member.getMemberOrgAssociationRole().toString());

    /* set the parameters */
    initialRequest.setExecutionParameters(parameters);

    /* execute the workflow */
    WorkflowResponse wfResponse = this.workflowManager.initiate(initialRequest);

    if (wfResponse != null) {

      if (wfResponse.getResponseCode().equals(WorkflowResponseCode.Success) == true) {
        return true;
      } else {
        return false;
      }

    }

    return false;
  }

  @Transactional
  public void memberBusinessAssociationNotification(Long memberId,
      String processInstanceId, Boolean status) {
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put(ProcessContextParameterConstants.MEMBER_ID, memberId);
    parameters.put(
        ProcessContextParameterConstants.BUSINESS_ASSOCIATION_APPROVAL, status);

    log.info("Member id " + memberId + " process instance id "
        + processInstanceId + " status " + status);

    WorkflowResumeRequest resumeRequest = new WorkflowResumeRequest();
    resumeRequest.setExecutionParameters(parameters);
    resumeRequest.setWorkflowInstanceId(processInstanceId);
    this.workflowManager.resume(resumeRequest);
  }

  public Set<MemberInfoDto> getContacts(OrganizationDetailInfoDto orgInfo,
      MemberInfoDto memberInfo) {

    SocialOrganization socialOrg = this.organizationRepository.findOne(orgInfo
        .getId());
    List<SocialContact> contactList = this.socialContactRepository
        .findByContactFrom(socialOrg);
    Set<MemberInfoDto> users = new TreeSet<MemberInfoDto>();

    for (SocialContact contact : contactList) {

      SocialActor contactActor = contact.getContactTo();
      if ((contactActor != null) && (contactActor instanceof SocialPerson)) {

        SocialPerson person = (SocialPerson) contactActor;
        MemberInfoDto user = new MemberInfoDto();
        user.setId(person.getId());
        user.setTitle(person.getRole());
        user.setPhotoLocation(person.getPhotoLocation());
        List<Address> addresses = person.getAddresses();
        if (!addresses.isEmpty()) {
          Address address = addresses.iterator().next();
          user.setCountry(address.getCountry());
          user.setCity(address.getCity());
        }

        if (person.getEmployer() != null) {
          SocialOrganization empOrg = (SocialOrganization) person.getEmployer()
              .getContactFrom();
          user.setCompany(empOrg.getBusinessName());
        }

        user.setEmail(person.getPrimaryEmail());
        user.setFirstName(person.getFirstName());
        user.setLastName(person.getLastName());

        users.add(user);

      }
    }

    return users;
  }

  public List<PartnerProgramDefinitionDto> getParticipatingPartnerPrograms(
      Long orgId) {
    SocialOrganization socialOrg = this.organizationRepository.findOne(orgId);
    return this.partnerManagementService
        .getParticipatingPartnerPrograms(socialOrg);
  }

  /**
   * This initiates the business invite 1) User can enter a name and email 2)
   * Check whether the email belongs to an existing member or business 2.1) For
   * existing member check if the members employer has the same name as the
   * business name specified. If not throw an exception and stop the process.
   * Need to provide error information to existing member as they may have a
   * typo 2.2) For existing business we move forward but do a name match. If
   * does not match it is an user error so rectify and process 5) In future we
   * will provide option where we system will fetch the admin member emails to
   * show in UI for the inviter to select whom to send the business invite
   */
  public void initiateBusinessInvite(MemberInfoDto inviter, String orgName,
      String inviteeEmail, OrganizationType type) {

    SocialPerson member = memberRepository.findOne(inviter.getId());
    validateBusinessInvitee(inviteeEmail, orgName, member);

    /*
     * lets check whether a business invite is already pending for this email
     * from this member or any other member associated with the employer of this
     * member if pending we do not move forward
     */

    List<WorkflowTransientState> transStateList = this.transientStateRepository
        .findByType(WorkflowTransientStateType.BusinessInvite);
    if (transStateList != null) {
      for (WorkflowTransientState transState : transStateList) {
        WorkflowTransientStateAttribute attrInviteeEmail = transState
            .getAttributeByName(ProcessContextParameterConstants.INVITEE_EMAIL);
        WorkflowTransientStateAttribute attrInviterId = transState
            .getAttributeByName(ProcessContextParameterConstants.INVITER_ID);

        if (attrInviterId != null && attrInviteeEmail != null) {

          /*
           * if the same invitee we should go ahead with the rest of the check
           */
          if (attrInviteeEmail.getAttributeValue().equalsIgnoreCase(
              inviteeEmail)) {
            SocialPerson memberInviter = memberRepository.findOne(Long
                .valueOf(attrInviterId.getAttributeValue()));
            if (memberInviter != null) {
              if (memberInviter.getEmployer() != null) {
                SocialOrganization prevInviterEmployer = (SocialOrganization) memberInviter
                    .getEmployer().getContactFrom();
                if (member.getEmployer() != null) {

                  /*
                   * both employer matches and that is the only way we can
                   * determine this
                   */
                  SocialOrganization currEmployer = (SocialOrganization) member
                      .getEmployer().getContactFrom();
                  if (currEmployer.getId().equals(prevInviterEmployer.getId())) {
                    throw new CoreServiceException(
                        "A business INVITE is already in process. Cannot initiate another INVITE at this time ");
                  }
                }
              }
            }
          }
        }
      }
    }

    BusinessProcessDefinition businessCreationProcess = this.businessProcessDefinitionRepository
        .findByName("businessInvite");
    log.info("Business invite business process "
        + businessCreationProcess.getName());

    WorkflowInitialRequest initialRequest = new WorkflowInitialRequest();
    initialRequest.setFlowDefinitionName(businessCreationProcess.getName());

    /* set the parameters */
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put(ProcessContextParameterConstants.ORG_INVITEE_NAME, orgName);
    parameters.put(
        ProcessContextParameterConstants.ORG_INVITE_ASSOCIATION_TYPE,
        type.toString());
    parameters
        .put(ProcessContextParameterConstants.INVITEE_EMAIL, inviteeEmail);
    parameters
        .put(ProcessContextParameterConstants.INVITER_ID, inviter.getId());

    /* set the parameters */
    initialRequest.setExecutionParameters(parameters);

    /* execute the workflow */
    this.workflowManager.initiate(initialRequest);

  }

  /**
   * 1) User can enter a name and email 2) Check whether the email belongs to an
   * existing member or business 2.1) For existing member check if the members
   * employer has the same name as the business name specified. If not throw an
   * exception and stop the process. Need to provide error information to
   * existing member as they may have a typo 2.2) For existing business we move
   * forward but do a name match. If does not match it is an user error so
   * rectify and process 5) In future we will provide option where we system
   * will fetch the admin member emails to show in UI for the inviter to select
   * whom to send the business invite
   * 
   * @param inviteeEmail
   * @param member
   */
  private void validateBusinessInvitee(String inviteeEmail, String orgName,
      SocialPerson member) {

    if (StringUtils.isEmpty(inviteeEmail) == true) {
      throw new CoreServiceException(" The organization email "
          + " cannot be empty ");
    }

    SocialOrganization memberBusiness = null;
    if (member.getEmployer() != null) {
      memberBusiness = (SocialOrganization) member.getEmployer()
          .getContactFrom();
      log.info("Employer " + memberBusiness.getBusinessName());
    }

    /**
     * if you do not have a business association then you cannot really do a
     * business INVITE
     */
    if (memberBusiness == null) {
      throw new CoreServiceException(
          "You are not associated with any business entity"
              + " so cannot perform a business invite");
    }

    /**
     * Now check whether there is any member exists with the same email
     */
    SocialPerson inviteePerson = this.memberRepository
        .findByEmail(inviteeEmail);
    if (inviteePerson != null) {
      if (inviteePerson.getEmployer() != null) {
        SocialOrganization inviteeOrg = (SocialOrganization) inviteePerson
            .getEmployer().getContactFrom();

        throw new CoreServiceException(
            " A member with the same email id exists in community  "
                + " which cannot be used for a business invite. "
                + " please invite the business email of the specified member's business "
                + inviteeOrg.getBusinessName() + " to invite "
                + inviteeOrg.getPrimaryEmail());
      } else {
        throw new CoreServiceException(" A member with the same email "
            + inviteeEmail + " exists in community thus "
            + "cannot be used for a business invite. "
            + " please provide an email of a business entity");
      }
    }

    String primaryEmail = StringUtils.EMPTY;
    primaryEmail = memberBusiness.getPrimaryEmail();

    if (primaryEmail.equalsIgnoreCase(inviteeEmail) == true) {
      log.info("Invitee and inviter belogs to the same organization "
          + " thus cannot be connected " + inviteeEmail);
      throw new ContactAlreadyExistsException(primaryEmail);
    }

    List<SocialContact> contacts = this.socialContactRepository
        .findByContactFrom(memberBusiness);
    for (SocialContact contact : contacts) {
      SocialActor actor = contact.getContactTo();
      String email = null;
      if (actor != null) {
        if (actor instanceof SocialPerson)
          email = ((SocialPerson) actor).getPrimaryEmail();
        else if (actor instanceof SocialOrganization)
          email = ((SocialOrganization) actor).getPrimaryEmail();
      }
      if (email != null && email.equals(inviteeEmail))
        throw new ContactAlreadyExistsException(email);
    }
  }

  public void businessInviteValidateBusinessAttrs(MemberInfoDto orgAdmin,
      OrganizationDetailInfoDto invitedOrg, String pid) {

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters
        .put(ProcessContextParameterConstants.MEMBER_ID, orgAdmin.getId());

    parameters.put(ProcessContextParameterConstants.BUSINESS_PHONE_NUMBER,
        invitedOrg.getBusinessPhone());
    parameters.put(ProcessContextParameterConstants.BUSINESS_EMAIL,
        invitedOrg.getBusinessPrimaryEmail());
    log.info("Business email " + invitedOrg.getBusinessPrimaryEmail());

    parameters.put(ProcessContextParameterConstants.BUSINESS_ADDR_STREET,
        invitedOrg.getSelectedLocation().getStreet());
    parameters.put(ProcessContextParameterConstants.BUSINESS_ADDR_CITY,
        invitedOrg.getSelectedLocation().getCity());
    parameters.put(ProcessContextParameterConstants.BUSINESS_ADDR_STATE,
        invitedOrg.getSelectedLocation().getState());
    parameters.put(ProcessContextParameterConstants.BUSINESS_ADDR_COUNTRY,
        invitedOrg.getSelectedLocation().getCountry());
    parameters.put(ProcessContextParameterConstants.BUSINESS_ADDR_ZIPCODE,
        invitedOrg.getSelectedLocation().getZipCode());

    /* info on member to be created */
    parameters.put(ProcessContextParameterConstants.MEMBER_FIRSTNAME,
        orgAdmin.getFirstName());
    parameters.put(ProcessContextParameterConstants.MEMBER_LASTNAME,
        orgAdmin.getLastName());
    parameters.put(ProcessContextParameterConstants.PASSWORD,
        orgAdmin.getPassword());
    log.info("Business creation for " + orgAdmin.getId() + " pid " + pid);

    WorkflowResumeRequest resumeRequest = new WorkflowResumeRequest();
    resumeRequest.setExecutionParameters(parameters);
    resumeRequest.setWorkflowInstanceId(pid);
    this.workflowManager.resume(resumeRequest);
  }

  public void businessInviteBusinessCreation(Long memberId, boolean approved,
      String pid) {

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put(ProcessContextParameterConstants.MEMBER_ID, memberId);
    parameters.put(ProcessContextParameterConstants.BUSINESS_ENTITY_APPROVAL,
        new Boolean(approved));

    WorkflowResumeRequest resumeRequest = new WorkflowResumeRequest();
    resumeRequest.setExecutionParameters(parameters);
    resumeRequest.setWorkflowInstanceId(pid);
    this.workflowManager.resume(resumeRequest);
  }

  public PartnerSettingsConfigurationDto getPartnerSettings(
      MemberInfoDto memberInfo, OrganizationDetailInfoDto orgInfo) {

    /**
     * 1. Check for embedded identity 2. If not present create one 3. Create
     * partner settings if needed 4. Generate URLs if needed 5. Return
     */
    SocialPerson person = this.memberRepository.findOne(memberInfo.getId());
    SocialEmbeddedIdentity embeddedIdentity = this.embeddedIdentityRepository
        .findEmbeddedIdentity(person);
    PartnerSettingsConfigurationDto retSettings = new PartnerSettingsConfigurationDto();

    if (embeddedIdentity == null) {
      embeddedIdentity = this.embeddedIdentityRepository.create(person);
      this.embeddedIdentityRepository.save(embeddedIdentity);
    }

    SocialOrganization socialOrg = this.organizationRepository.findOne(orgInfo
        .getId());
    PartnerSettingsConfiguration partnerConfig = this.partnerConfigRepository
        .findPartnerConfigByOrg(socialOrg);

    if (partnerConfig == null) {

      partnerConfig = this.partnerConfigRepository.create(socialOrg);
      String baseUrl = this.platformCommonSettings.getPortalApplcationUrl()
          .getValue();
      StringBuilder searchUrl = new StringBuilder()
          .append(baseUrl)
          .append(
              this.platformCommonSettings.getEmbeddedSearchPageUrl().getValue())
          .append("?mid=").append(embeddedIdentity.getUuid());
      partnerConfig.setEmbeddedCommunitySearchUrl(searchUrl.toString());
      partnerConfig.setInCommunitySearchUrl(searchUrl.toString());
      this.partnerConfigRepository.save(partnerConfig);
    }

    /* copy to DTO and return */
    retSettings.setEmbeddedCommunitySearchUrl(partnerConfig
        .getEmbeddedCommunitySearchUrl());
    retSettings
        .setInCommunitySearchUrl(partnerConfig.getInCommunitySearchUrl());

    return retSettings;
  }

  /**
   * 
   * @param partnerSearchConfig
   * @throws JAXBException
   */
  public void updatePartnerConfiguration(
      PartnerSearchSettingsConfigurationDto partnerSearchConfig)
      throws JAXBException {
    log.info("Updating profile " + partnerSearchConfig.toString());

    if (partnerSearchConfig.getId() != Long.MIN_VALUE) {

      /* This is update case */
      PartnerSearchSettingsConfiguration config = this.partnerSearchSettingsRepository
          .findByOrgId(partnerSearchConfig.getOrgId());

      /* Unmarshall and save */
      this.jaxbContext = JAXBContext
          .newInstance(PartnerSearchSettingsConfigurationDto.class);
      Marshaller marshaller = this.jaxbContext.createMarshaller();
      Writer strWriter = new StringWriter();
      marshaller.marshal(partnerSearchConfig, strWriter);
      config.setConfigurationSettings(strWriter.toString());

      this.partnerSearchSettingsRepository.save(config);
    } else {

      /* This is new case */
      PartnerSearchSettingsConfiguration config = this.partnerSearchSettingsRepository
          .create();
      config.setOrgId(partnerSearchConfig.getOrgId());

      /* Unmarshall and save */
      this.jaxbContext = JAXBContext
          .newInstance(PartnerSearchSettingsConfigurationDto.class);
      Marshaller marshaller = this.jaxbContext.createMarshaller();
      Writer strWriter = new StringWriter();
      marshaller.marshal(partnerSearchConfig, strWriter);
      config.setConfigurationSettings(strWriter.toString());

      this.partnerSearchSettingsRepository.save(config);
    }
  }

  public PartnerSearchSettingsConfigurationDto getPartnerSearchSettings(Long id)
      throws JAXBException {

    PartnerSearchSettingsConfiguration config = this.partnerSearchSettingsRepository
        .findByOrgId(id);

    PartnerSearchSettingsConfigurationDto partnerSearchSettings = null;
    if (config != null) {

      this.jaxbContext = JAXBContext
          .newInstance(PartnerSearchSettingsConfigurationDto.class);
      ByteArrayInputStream inputStream = new ByteArrayInputStream(config
          .getConfigurationSettings().getBytes());
      Unmarshaller unmarshaller = this.jaxbContext.createUnmarshaller();
      partnerSearchSettings = (PartnerSearchSettingsConfigurationDto) unmarshaller
          .unmarshal(inputStream);
      partnerSearchSettings.setId(config.getId());
      partnerSearchSettings.setOrgId(config.getOrgId());

      return partnerSearchSettings;
    }

    return null;
  }

  public PartnerSettingsConfigurationDto generateAllEmbeddedUrls(Long settingsId) {

    return null;
  }

  public List<EmbeddedUrlDto> getEmbeddedUrls(OrganizationDetailInfoDto orgInfo) {
    List<EmbeddedUrlDto> embeddedUrlList = new ArrayList<EmbeddedUrlDto>();

    /* get the platform url */
    String platformUrl = this.platformCommonSettings.getPortalApplcationUrl()
        .getValue();

    /* get the embedded identity */
    SocialOrganization embeddedOrg = this.organizationRepository
        .findOne(orgInfo.getId());
    log.info("Found org with id " + embeddedOrg.getId());
    SocialEmbeddedIdentity embeddedIdentity = embeddedIdentityRepository
        .findEmbeddedIdentity(embeddedOrg);

    if (embeddedIdentity == null) {
      embeddedIdentity = this.embeddedIdentityRepository.create(embeddedOrg);
      this.embeddedIdentityRepository.save(embeddedIdentity);
    }

    /* create the full Urls for each map entry */
    Set<String> keySet = this.embeddedUrlMap.keySet();

    for (String urlKey : keySet) {
      EmbeddedUrlDto urlDto = new EmbeddedUrlDto();
      String urlValue = this.embeddedUrlMap.get(urlKey);
      String fullUrl = platformUrl + urlValue + "?oeid="
          + embeddedIdentity.getUuid();
      String helpUrl = platformUrl + "help/" + urlValue;
      urlDto.setFullUrl(fullUrl);
      urlDto.setUrlTag(urlKey);
      urlDto.setHelpUrl(helpUrl);

      embeddedUrlList.add(urlDto);
    }

    return embeddedUrlList;
  }

  public void saveLeadCaptureWizzard(LeadCaptureWizzardDto wizzardDto,
      OrganizationDetailInfoDto orgInfo) {

    LeadCaptureWizzardTemplate wizzard = null;
    SocialOrganization owner = this.organizationRepository.findOne(orgInfo
        .getId());

    if (wizzardDto.getId() == Long.MIN_VALUE) {
      wizzard = this.leadCaptureWizzardTemplateRepository
          .createWizzardTemplate();
      wizzard.setOwner(owner);
      this.leadCaptureWizzardTemplateRepository.save(wizzard);

      /* set the id for update */
      wizzardDto.setId(wizzard.getId());
    } else {
      wizzard = this.leadCaptureWizzardTemplateRepository.findByOwner(owner);
    }

    /* enter each question */
    for (LeadCaptureQuestionDto leadCaptureQuestionDto : wizzardDto
        .getQuestions()) {

      if (leadCaptureQuestionDto.getId() == Long.MIN_VALUE) {

        LeadCaptureQuestion leadCaptureQuestion = this.leadCaptureQuestionRepository
            .create();
        leadCaptureQuestion.setQuestionText(leadCaptureQuestionDto
            .getQuestionText());
        leadCaptureQuestion.setQuestionType(leadCaptureQuestionDto
            .getQuestionType());
        leadCaptureQuestion.setRequired(leadCaptureQuestionDto.getRequired());
        leadCaptureQuestion.setSerialId(leadCaptureQuestionDto.getSerialId());

        /* question choices */
        for (LeadCaptureQuestionChoiceDto leadCaptureQuestionChoiceDto : leadCaptureQuestionDto
            .getQuestionChoices()) {
          LeadCaptureQuestionChoice leadCaptureQuestionChoice = this.leadCaptureQuestionChoiceRepository
              .create();
          leadCaptureQuestionChoice
              .setQuestionChoiceText(leadCaptureQuestionChoiceDto
                  .getQuestionChoiceText());
          leadCaptureQuestionChoice.setSerialId(leadCaptureQuestionChoiceDto
              .getSerialId());
          this.leadCaptureQuestionChoiceRepository
              .save(leadCaptureQuestionChoice);

          leadCaptureQuestionChoiceDto.setId(leadCaptureQuestionChoice.getId());
          leadCaptureQuestion.getQuestionChoices().add(
              leadCaptureQuestionChoice);
        }

        this.leadCaptureQuestionRepository.save(leadCaptureQuestion);
        leadCaptureQuestionDto.setId(leadCaptureQuestion.getId());
        wizzard.getQuestions().add(leadCaptureQuestion);
      }

    }

    this.leadCaptureWizzardTemplateRepository.save(wizzard);

  }

  public LeadCaptureWizzardDto getLeadCaptureWizzard(
      OrganizationDetailInfoDto orgInfo) {

    /**
     * 1. get the wizzard 2. get all questions 3. question choices 4. copy to
     * DTO
     */
    SocialOrganization owner = this.organizationRepository.findOne(orgInfo
        .getId());
    LeadCaptureWizzardTemplate wizzard = this.leadCaptureWizzardTemplateRepository
        .findByOwner(owner);

    if (wizzard != null) {

      LeadCaptureWizzardDto wizzardDto = new LeadCaptureWizzardDto();
      wizzardDto.setId(wizzard.getId());

      for (LeadCaptureQuestion leadCaptureQuestion : wizzard.getQuestions()) {
        LeadCaptureQuestionDto leadCaptureQuestionDto = new LeadCaptureQuestionDto();
        leadCaptureQuestionDto.setId(leadCaptureQuestion.getId());
        leadCaptureQuestionDto.setQuestionText(leadCaptureQuestion
            .getQuestionText());
        leadCaptureQuestionDto.setQuestionType(leadCaptureQuestion
            .getQuestionType());
        leadCaptureQuestionDto.setSerialId(leadCaptureQuestion.getSerialId());

        /* get all the choices */
        for (LeadCaptureQuestionChoice leadCaptureQuestionChoice : leadCaptureQuestion
            .getQuestionChoices()) {
          LeadCaptureQuestionChoiceDto leadCaptureQuestionChoiceDto = new LeadCaptureQuestionChoiceDto();
          leadCaptureQuestionChoiceDto.setId(leadCaptureQuestionChoice.getId());
          leadCaptureQuestionChoiceDto
              .setQuestionChoiceText(leadCaptureQuestionChoice
                  .getQuestionChoiceText());
          leadCaptureQuestionChoiceDto.setSerialId(leadCaptureQuestionChoice
              .getSerialId());
          leadCaptureQuestionDto.getQuestionChoices().add(
              leadCaptureQuestionChoiceDto);
        }

        wizzardDto.getQuestions().add(leadCaptureQuestionDto);
      }

      return wizzardDto;
    }

    return null;
  }

  public void saveLeadCaptureresponse(LeadCaptureWizzardDto captureWizzard,
      OrganizationDetailInfoDto orgInfo) {

    /**
     * 1. Create a response and save 2. Create a contact lead
     */
    SocialOrganization owner = this.organizationRepository.findOne(orgInfo
        .getId());
    LeadCaptureResponse captureResponse = this.leadCaptureResponseRepository
        .create();
    captureResponse.setOwner(owner);
    captureResponse.setLeadName(captureWizzard.getLeadName());
    captureResponse.setLeadEmailAddress(captureWizzard.getLeadEmailAddress());
    captureResponse.setAdditionalComments(captureWizzard
        .getAdditionalComments());
    captureResponse.setCompany(captureWizzard.getCompany());
    captureResponse.setTimeStamp(new Date());

    /* create and save the question responses */
    for (LeadCaptureQuestionDto questionDto : captureWizzard.getQuestions()) {
      LeadCaptureQuestionResponse questionResponse = this.leadCaptureQuestionResponseRepository
          .create();
      questionResponse.setQuestionId(questionDto.getId());
      questionResponse.setQuestionText(questionDto.getQuestionText());
      questionResponse.setQuestionType(questionDto.getQuestionType());

      if (questionDto.getTextInput() == true) {
        LeadCaptureQuestionChoiceResponse choiceResponse = this.leadCaptureQuestionChoiceResponseRepository
            .create();
        choiceResponse.setQuestionChoiceText(questionDto.getTextAnswerInput());
        this.leadCaptureQuestionChoiceResponseRepository.save(choiceResponse);
        questionResponse.getChoiceResponses().add(choiceResponse);
      } else {
        if (questionDto.getMultiChoice() == true) {

          /* now the choice response for those questions */
          for (LeadCaptureQuestionChoiceDto questionChoiceDto : questionDto
              .getSelectedChoices()) {
            LeadCaptureQuestionChoiceResponse choiceResponse = this.leadCaptureQuestionChoiceResponseRepository
                .create();
            choiceResponse.setChoiceId(questionChoiceDto.getId());
            choiceResponse.setQuestionChoiceText(questionChoiceDto
                .getQuestionChoiceText());
            this.leadCaptureQuestionChoiceResponseRepository
                .save(choiceResponse);
            questionResponse.getChoiceResponses().add(choiceResponse);
          }
        } else {
          if (questionDto.getSingleChoice() == true
              && questionDto.getSelectedChoice() != null) {
            LeadCaptureQuestionChoiceResponse choiceResponse = this.leadCaptureQuestionChoiceResponseRepository
                .create();
            choiceResponse.setChoiceId(questionDto.getSelectedChoice().getId());
            choiceResponse.setQuestionChoiceText(questionDto
                .getSelectedChoice().getQuestionChoiceText());
            this.leadCaptureQuestionChoiceResponseRepository
                .save(choiceResponse);
            questionResponse.getChoiceResponses().add(choiceResponse);
          }
        }
      }

      this.leadCaptureQuestionResponseRepository.save(questionResponse);
      captureResponse.getQuestionResponse().add(questionResponse);
    }

    this.leadCaptureResponseRepository.save(captureResponse);

    /* now lets create a contact lead */
    ContactLead contactLead = this.contactLeadRepository.create();
    contactLead.setLeadSource("Via lead cature wizzard");
    contactLead.setLeadType(LeadType.ContactLead);
    contactLead.setLeadStatus(LeadStatusType.Unassigned);
    contactLead.setAssociatedResponse(captureResponse);
    contactLead.setTimeStamp(new Date());
    contactLead.setForSocialActorId(orgInfo.getId());
    contactLead.setContactEmail(captureWizzard.getLeadEmailAddress());
    contactLead.setName(captureWizzard.getLeadName());
    contactLead.setCompany(captureWizzard.getCompany());
    contactLead.setContactPhone(captureWizzard.getPhoneNumber());

    this.contactLeadRepository.save(contactLead);
  }

  public ReviewCommentSummeryDto getReviewSummary(
      OrganizationDetailInfoDto orgInfo) {

    return null;
  }

  public void deleteLeadCaptureQuestion(OrganizationDetailInfoDto orgInfo,
      LeadCaptureQuestionDto questionDto) {

    SocialOrganization owner = this.organizationRepository.findOne(orgInfo
        .getId());
    LeadCaptureWizzardTemplate wizzard = this.leadCaptureWizzardTemplateRepository
        .findByOwner(owner);

    List<LeadCaptureQuestion> newList = new ArrayList<LeadCaptureQuestion>();
    Iterator<LeadCaptureQuestion> questionIterator = wizzard.getQuestions()
        .iterator();

    while (questionIterator.hasNext()) {
      // FIXME: Workaround for oneToMany delete issue
      // Add everything but the deleted question to the list.
      LeadCaptureQuestion question = questionIterator.next();
      if (question.getId().equals(questionDto.getId())) {
        this.leadCaptureQuestionRepository.delete(question.getId());
      } else {
        newList.add(question);
      }
    }

    wizzard.setQuestions(newList);
    this.leadCaptureWizzardTemplateRepository.save(wizzard);

  }

  public void approveConnection(OrganizationBasicInfoDto businessToConnect,
      String processInstId, boolean approved) {

    log.info("Approve connection for business " + businessToConnect.getId()
        + " approval status " + approved);

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put(
        ProcessContextParameterConstants.BUSINESS_CONNECTION_APPROVAL,
        new Boolean(approved));

    WorkflowResumeRequest resumeRequest = new WorkflowResumeRequest();
    resumeRequest.setExecutionParameters(parameters);
    resumeRequest.setWorkflowInstanceId(processInstId);
    this.workflowManager.resume(resumeRequest);

  }

  public void requestProduct(String productRequestComment,
      MemberInfoDto member, OrganizationDetailInfoDto orgInfo) {

    String toAddress = orgInfo.getBusinessPrimaryEmail();

    if (EmailValidator.validate(toAddress) == true) {

      Map<String, Object> emailInputParams = new HashMap<String, Object>();
      if (member.getFirstName() != null)
        emailInputParams.put("fromFirstName", member.getFirstName());
      if (member.getLastName() != null)
        emailInputParams.put("fromLastName", member.getLastName());
      if (productRequestComment != null)
        emailInputParams.put("requestProductComment", productRequestComment);

      String fromAddress = this.commonSettings.getPlatformEmailSenderAddress()
          .getValue();

      /* send email */
      try {
        this.emailManager.send(fromAddress, toAddress,
            "requestProductEmail.vm", emailInputParams);
      } catch (Exception exception) {
        throw new CoreServiceException("Unable to send product request email",
            exception);
      }
    }

  }

  public Long getOrgIdFromEmbeddedId(String orgEmbeddedIdentity) {
    SocialEmbeddedIdentity embeddedIdentity = this.embeddedIdentityRepository
        .findEmbeddedIdentity(orgEmbeddedIdentity);
    SocialActor org = embeddedIdentity.getEmbeddedActor();
    return org.getId();
  }

  public Long getOrganizationCountByType(OrganizationType type) {
    return this.organizationRepository.getOrganizationCountByType(type);
  }

  public void shareComment(OrganizationDetailInfoDto orgInfo,
      String shareComment) {
    throw new UnsupportedOperationException();
  }

  public void follow(MemberInfoDto member, OrganizationDetailInfoDto orgInfo) {
    // Not needed for trial
    /*
     * SocialPerson person = this.memberRepository.findOne(member.getId());
     * 
     * Set<SocialPerson> followers = socialOrg.getFolloweds();
     * 
     * boolean duplicate = false;
     * 
     * // Guard against duplicate follows for (SocialPerson follower :
     * followers) { if (follower.getId().equals(person.getId())) duplicate =
     * true; }
     * 
     * if (duplicate == false) { log.info("Adding follower to org");
     * followers.add(person);
     * 
     * organizationRepository.save(socialOrg);
     * log.info("Added follower to organization"); } else { log.info(
     * "Follower was not added to organization because person was already following"
     * ); }
     */
  }

  public boolean isMemberFollowingOrg(MemberInfoDto member,
      OrganizationDetailInfoDto orgInfo) {

    // Not needed for trial
    /*
     * SocialOrganization socialOrg = this.organizationRepository
     * .findOne(orgInfo.getId());
     * 
     * SocialPerson person = this.memberRepository.findOne(member.getId());
     * 
     * Set<SocialPerson> followers = socialOrg.getFolloweds();
     * 
     * for (SocialPerson follower : followers) { if
     * (follower.getId().equals(person.getId())) return true; }
     */
    return false;
  }

  @Override
  @Transactional
  public List<BusinessAccountType> getAllBusinessAccountTypes() {
    return this.businessAccountTypeRepository.findAll();
  }

  @Override
  @Transactional
  public BusinessAccountType getBusinessAccount(Long org) {
    SocialOrganization business = this.organizationRepository.findOne(org);
    return business.getAccountType();
  }

  @Override
  @Transactional
  public BusinessAccountType upgradeBusinessAccount(Long org,
      BusinessAccountType upgradeAccountTo) {

    SocialOrganization business = this.organizationRepository.findOne(org);
    BusinessAccountType currentAccount = business.getAccountType();

    if (currentAccount.getUpgradeSequence() > upgradeAccountTo
        .getUpgradeSequence()) {
      // cannot upgrade account to
      throw new CoreServiceException("Account cannot be upgraded from "
          + currentAccount.getName() + " to "
          + upgradeAccountTo.getUpgradeSequence());
    }

    business.setAccountType(upgradeAccountTo);
    this.organizationRepository.save(business);

    return upgradeAccountTo;
  }

  @Override
  @Transactional
  public BusinessAccountType getBusinessAccountByName(String name) {
    return this.businessAccountTypeRepository.findByName(name);

  }

  @Override
  @Transactional
  public BusinessAccountType downgradeBusinessAccount(Long org,
      BusinessAccountType downgradeAccountTo) {
    SocialOrganization business = this.organizationRepository.findOne(org);
    BusinessAccountType currentAccount = business.getAccountType();

    if (currentAccount.getUpgradeSequence() < downgradeAccountTo
        .getUpgradeSequence()) {
      // cannot upgrade account to
      throw new CoreServiceException("Account cannot be down graded from "
          + currentAccount.getName() + " to "
          + downgradeAccountTo.getUpgradeSequence());
    }

    business.setAccountType(downgradeAccountTo);
    this.organizationRepository.save(business);

    return downgradeAccountTo;
  }

  @Override
  @Transactional
  public OrganizationBasicInfoDto getBusinessByName(String name) {
    SocialOrganization business = this.organizationRepository
        .findByBusinessName(name);
    if (business != null) {
      return this.businessMarshaller.marshallBusiness(business);
    }

    return null;
  }

  @Override
  @Transactional
  public OrganizationBasicInfoDto getBusinessByNameAndEmail(String name,
      String email) {
    SocialOrganization business = this.organizationRepository
        .findByBusinessNameAndPrimaryEmail(name, email);
    if (business != null) {
      return this.businessMarshaller.marshallBusiness(business);
    }

    return null;
  }

  @Override
  @Transactional
  public void updateBranding(UpdateBusinessBrandingDto branding) {

    SocialOrganization business = this.organizationRepository.findOne(branding
        .getId());

    business.setPartnerBannerLocation(branding.getPartnerBannerLocation());
    business.setPartnerBannerText(branding.getPartnerBannerText());
    business.setPartnerWelcomeMessage(branding.getPartnerWelcomeMessage());
    business.setPartnerBannerTextColor(branding.getPartnerBannerTextColor());

    log.info("Saving branding banner  " + branding.getPartnerBannerLocation()
        + " banner text " + branding.getPartnerBannerText());

    this.organizationRepository.save(business);
  }

  @Override
  @Transactional
  public void deleteExternalPortals(List<ExternalBusinessPortalDto> deleteList) {

    for (ExternalBusinessPortalDto extPortal : deleteList) {
      if (extPortal.getId().equals(Long.MIN_VALUE) != true) {
        log.info("Deleting external portal " + extPortal.getUuid() + " name "
            + extPortal.getPortalName());

        ExternalBusinessPortal portal = this.externalBusinessPortalRepository
            .findOne(extPortal.getId());
        if (portal != null) {
          this.externalBusinessPortalRepository.delete(portal);
        }
      }

    }

  }

  @Override
  @Transactional
  public List<ExternalBusinessPortalDto> addExternalPortals(Long ownerId,
      List<ExternalBusinessPortalDto> portalList) {

    List<ExternalBusinessPortalDto> retList = new ArrayList<ExternalBusinessPortalDto>();

    SocialOrganization owner = this.organizationRepository.findOne(ownerId);
    for (ExternalBusinessPortalDto extPortalDto : portalList) {

      if (extPortalDto.getId().equals(Long.MIN_VALUE)) {
        log.info("External portal added with URL "
            + extPortalDto.getPortalUrl() + " UUID " + extPortalDto.getUuid());

        String[] schemes = { "http", "https" };
        UrlValidator urlValidator = new UrlValidator(schemes);
        if (urlValidator.isValid(extPortalDto.getPortalUrl()) != true) {
          log.info("External portal added with URL "
              + extPortalDto.getPortalUrl() + " is invalid adding http://");
          String newUrl = "http://" + extPortalDto.getPortalUrl();
          extPortalDto.setPortalUrl(newUrl);
        }

        ExternalBusinessPortal externalPortal = this.externalBusinessPortalRepository
            .create(owner, extPortalDto.getPortalName(),
                extPortalDto.getPortalUrl());

        this.externalBusinessPortalRepository.save(externalPortal);
        ExternalBusinessPortalDto retDto = this.externalBusinessPortalMarchaller
            .marshall(externalPortal);
        retList.add(retDto);
      }

    }

    return retList;

  }

  @Override
  @Transactional
  public List<ExternalBusinessPortalDto> getExternalPortals(Long ownerId) {
    SocialOrganization owner = this.organizationRepository.findOne(ownerId);
    List<ExternalBusinessPortal> portalList = this.externalBusinessPortalRepository
        .findByOwner(owner);
    return this.externalBusinessPortalMarchaller.marshall(portalList);
  }

}
