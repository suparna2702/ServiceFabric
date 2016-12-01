package com.similan.service.impl;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.similan.domain.entity.acccount.LeadPurchaseRecord;
import com.similan.domain.entity.acccount.OrganizationAccount;
import com.similan.domain.entity.common.Address;
import com.similan.domain.entity.community.SocialEmployee;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.lead.AcquiredLead;
import com.similan.domain.entity.lead.AffiliateLead;
import com.similan.domain.entity.lead.AffiliateLeadList;
import com.similan.domain.entity.lead.ClickThroughLead;
import com.similan.domain.entity.lead.ContactLead;
import com.similan.domain.entity.lead.ContactMessageDetail;
import com.similan.domain.entity.lead.CrmLeadFieldSettingConfig;
import com.similan.domain.entity.lead.CrmLeadSettings;
import com.similan.domain.entity.lead.Lead;
import com.similan.domain.entity.lead.LeadActivity;
import com.similan.domain.entity.lead.LeadAlertSettings;
import com.similan.domain.entity.lead.LeadAssignment;
import com.similan.domain.entity.lead.LeadComment;
import com.similan.domain.entity.lead.LeadMessage;
import com.similan.domain.entity.lead.LeadSearchFilterSetting;
import com.similan.domain.entity.lead.LeadSearchFilterSettingConfig;
import com.similan.domain.entity.lead.LeadStatusType;
import com.similan.domain.entity.lead.LeadType;
import com.similan.domain.entity.lead.SearchLead;
import com.similan.domain.entity.lead.SearchLeadDetail;
import com.similan.domain.entity.lead.TransferLead;
import com.similan.domain.entity.leadcapture.LeadCaptureQuestionChoiceResponse;
import com.similan.domain.entity.leadcapture.LeadCaptureQuestionResponse;
import com.similan.domain.entity.leadcapture.LeadCaptureResponse;
import com.similan.domain.entity.util.AddressDto;
import com.similan.domain.repository.account.OrganizationAccountRepository;
import com.similan.domain.repository.common.AddressRepository;
import com.similan.domain.repository.community.SocialOrganizationRepository;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.domain.repository.lead.AcquiredLeadRepository;
import com.similan.domain.repository.lead.AffiliateLeadListRepository;
import com.similan.domain.repository.lead.AffiliateLeadRepository;
import com.similan.domain.repository.lead.ClickThroughLeadRepository;
import com.similan.domain.repository.lead.ContactLeadRepository;
import com.similan.domain.repository.lead.CrmLeadSettingsRepository;
import com.similan.domain.repository.lead.FilteredAvailableLeadListRepository;
import com.similan.domain.repository.lead.LeadActivityRepository;
import com.similan.domain.repository.lead.LeadAlertSettingsRepository;
import com.similan.domain.repository.lead.LeadAssignmentRepository;
import com.similan.domain.repository.lead.LeadCommentRepository;
import com.similan.domain.repository.lead.LeadMessageRepository;
import com.similan.domain.repository.lead.LeadPurchaseRecordRepository;
import com.similan.domain.repository.lead.LeadSearchFilterSettingRepository;
import com.similan.domain.repository.lead.SearchLeadRepository;
import com.similan.domain.repository.lead.TransferLeadRepository;
import com.similan.domain.repository.process.BusinessProcessDefinitionRepository;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.SearchResultDto;
import com.similan.framework.dto.SearchResultItemDto;
import com.similan.framework.dto.SearchResultSummery;
import com.similan.framework.dto.lead.AffiliateLeadListDto;
import com.similan.framework.dto.lead.CrmLeadSettingsDto;
import com.similan.framework.dto.lead.LeadActivityDto;
import com.similan.framework.dto.lead.LeadAlertSettingsDto;
import com.similan.framework.dto.lead.LeadAssignmentDto;
import com.similan.framework.dto.lead.LeadCountOverPeriodStatDto;
import com.similan.framework.dto.lead.LeadDto;
import com.similan.framework.dto.lead.LeadMessageDto;
import com.similan.framework.dto.lead.LeadNoteDto;
import com.similan.framework.dto.lead.LeadSearchFilterSettingDto;
import com.similan.framework.dto.lead.LeadTransferStatusDto;
import com.similan.framework.dto.leadcapture.LeadCaptureQuestionChoiceResponseDto;
import com.similan.framework.dto.leadcapture.LeadCaptureQuestionResponseDto;
import com.similan.framework.dto.leadcapture.LeadCaptureResponseDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.geo.Geocoder;
import com.similan.framework.service.GeoCoderServiceImpl;
import com.similan.framework.util.BeanPropertyUpdator;
import com.similan.framework.workflow.ProcessContextParameterConstants;
import com.similan.framework.workflow.api.WorkflowManager;
import com.similan.framework.workflow.api.WorkflowResumeRequest;
import com.similan.service.api.CrmIntegrationService;
import com.similan.service.api.LeadManagementService;
import com.similan.service.exception.CoreServiceException;
import com.similan.service.exception.LeadDeleteException;

@Slf4j
public class LeadManagementServiceImpl implements LeadManagementService {
  @Autowired
  private MessagingServiceImpl messagingService;
  @Autowired
	private GeoCoderServiceImpl geoCoderService;
  @Autowired
	private Geocoder geocoder;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private ContactLeadRepository contactLeadRepository;
	@Autowired
	private ClickThroughLeadRepository clickThroughLeadRepository;
	@Autowired
	private SearchLeadRepository searchLeadRepository;
	@Autowired
	private LeadAssignmentRepository leadAssignmentRepository;
	@Autowired
	private LeadAlertSettingsRepository leadAlertSettingsRepository;
	@Autowired
	private CrmLeadSettingsRepository crmLeadSettingsRepository;
	@Autowired
	private SocialPersonRepository socialPersonRepository;
	@Autowired
	private SocialOrganizationRepository socialOrganizationRepository;
	@Autowired
	private LeadCommentRepository leadCommentRepository;
	@Autowired
	private LeadActivityRepository leadActivityRepository;
	@Autowired
	private LeadMessageRepository leadMessageRepository;
	@Autowired
	private AcquiredLeadRepository acquiredLeadRepository;
	@Autowired
	private AffiliateLeadRepository affiliateLeadRepository;
	@Autowired
	private AffiliateLeadListRepository affiliateLeadListRepository;
	@Autowired
	private LeadSearchFilterSettingRepository leadSearchFilterSettingRepository;
	@Autowired
	private FilteredAvailableLeadListRepository filteredAvailableLeadListRepository;
	@Autowired
	private OrganizationAccountRepository organizationAccountRepository;
	@Autowired
	private LeadPurchaseRecordRepository leadPurchaseRecordRepository;
  @Autowired
  private BusinessProcessDefinitionRepository businessProcessDefinitionRepository;
  @Autowired
  private WorkflowManager workflowManager;
  @Autowired
  private TransferLeadRepository transferLeadRepository;
  @Autowired
  private CrmIntegrationService crmIntegrationService;
  
  @Getter @Setter
	private BeanPropertyUpdator leadSearchFilterDtoToDomainAttributeUpdater;
  @Getter @Setter
	private BeanPropertyUpdator leadSearchFilterDomainToDtoAttributeUpdater;
  @Getter @Setter
	private BeanPropertyUpdator crmSettingsDtoToDomainAttributeUpdater;
  @Getter @Setter
	private BeanPropertyUpdator crmSettingsDomainToDtoAttributeUpdater;
  @Getter @Setter
	private BeanPropertyUpdator leadDtoToDomainAttributeUpdater;
  @Getter @Setter
	private BeanPropertyUpdator leadDomainToDtoAttributeUpdater;
  @Getter @Setter
	private BeanPropertyUpdator affiliateLeadDtoToDomainAttributeUpdater;
  @Getter @Setter
	private BeanPropertyUpdator affiliateLeadDomainToDtoAttributeUpdater;
  @Getter @Setter
	private BeanPropertyUpdator adressDtoToDomainAttributeUpdater;
  @Getter @Setter
	private BeanPropertyUpdator adressDomainToDtoAttributeUpdater;

	private JAXBContext jaxbContext = null;
	
	public JAXBContext getJaxbContext() {
		return jaxbContext;
	}

	public void setJaxbContext(JAXBContext jaxbContext) {
		this.jaxbContext = jaxbContext;
	}
	
	private LeadDto getLeadDtoFromLeadDomain(Lead leadDomain, LeadType leadType) throws Exception{
		LeadDto leadDto = new LeadDto();
		this.leadDomainToDtoAttributeUpdater.update(leadDomain, leadDto);
		
		if(leadDomain.getLocation() != null){
			Address addrDomain = leadDomain.getLocation();
			AddressDto addrDto = new AddressDto();
			this.adressDomainToDtoAttributeUpdater.update(addrDomain, addrDto);
			leadDto.setLocation(addrDto);
		}
		
		return leadDto;
	}

	/**
	 * 
	 * @param contactLead
	 * @return
	 * @throws Exception 
	 */
	private LeadDto getLeadDtoFromContactLead(ContactLead contactLead) throws Exception {
		LeadDto lead = this.getLeadDtoFromLeadDomain(contactLead, LeadType.ContactLead);
		if(contactLead.getAssociatedResponse() != null){
			LeadCaptureResponseDto response = this.getLeadCaptureResponseDtoFromDomain(contactLead.getAssociatedResponse());
			lead.setResponse(response);
		}
		return lead;
	}
	
	public LeadCaptureResponseDto getLeadCaptureResponseDtoFromDomain(LeadCaptureResponse responseDomain){
		LeadCaptureResponseDto responseDto = new LeadCaptureResponseDto();
		
		if(responseDomain != null){
			responseDto.setId(responseDomain.getId());
			responseDto.setAdditionalComments(responseDomain.getAdditionalComments());
			responseDto.setCompany(responseDomain.getCompany());
			responseDto.setLeadName(responseDomain.getLeadName());
			responseDto.setTimeStamp(responseDomain.getTimeStamp());
			
			/* now the question responses */
			if(responseDomain.getQuestionResponse() != null){
				for(LeadCaptureQuestionResponse questionResponse : responseDomain.getQuestionResponse()){
					LeadCaptureQuestionResponseDto questionResponseDto = new LeadCaptureQuestionResponseDto();
					questionResponseDto.setId(questionResponse.getId());
					questionResponseDto.setQuestionId(questionResponse.getQuestionId());
					questionResponseDto.setQuestionText(questionResponse.getQuestionText());
					questionResponseDto.setQuestionType(questionResponse.getQuestionType());
					responseDto.getQuestionResponse().add(questionResponseDto);
					
					/* lets put the choices now */
					if(questionResponse.getChoiceResponses() != null){
						for(LeadCaptureQuestionChoiceResponse choiceResponse : questionResponse.getChoiceResponses()){
							LeadCaptureQuestionChoiceResponseDto choiceResponseDto = new LeadCaptureQuestionChoiceResponseDto();
							choiceResponseDto.setChoiceId(choiceResponse.getChoiceId());
							choiceResponseDto.setId(choiceResponse.getId());
							choiceResponseDto.setQuestionChoiceText(choiceResponse.getQuestionChoiceText());
							questionResponseDto.getChoiceResponses().add(choiceResponseDto);
							
						}
					}
				}
			}
		}
		
		return responseDto;
		
	}


	/**
	 * @author supapal
	 * @param actorId
	 * @return
	 */
	public Map<LeadType, List<LeadCountOverPeriodStatDto>> getLeadCountOverPeriodStat(
			long actorId) {

		/**
		 * 1. Objective is to get lead count for a specific time period 2. We
		 * will devide the time period of leads into equal five segments 3. Get
		 * all leads order by time for a specific actor 4. Create the segments
		 * 5. Then iterate and put them in segment bucket
		 */
		Map<LeadType, List<LeadCountOverPeriodStatDto>> retMap = new HashMap<LeadType, List<LeadCountOverPeriodStatDto>>();
		return retMap;
	}

	public Map<LeadType, Long> getLeadCountStatSocialActor(long actorId) {
		Map<LeadType, Long> statMap = new HashMap<LeadType, Long>();

		Long contactLeadCount = this.contactLeadRepository
				.getLeadCountSocialActor(actorId);
		statMap.put(LeadType.ContactLead, contactLeadCount);

		Long clickLeadCount = this.clickThroughLeadRepository
				.getLeadCountSocialActor(actorId);
		statMap.put(LeadType.ClickThroughLead, clickLeadCount);

		Long searchLeadCount = this.searchLeadRepository
				.getLeadCountSocialActor(actorId);
		statMap.put(LeadType.SearchLead, searchLeadCount);
		
		Long transferLeadCount = this.transferLeadRepository
				.getLeadCountSocialActor(actorId);
		statMap.put(LeadType.TransferLead, transferLeadCount);

		Long acquiredLeadCount = this.acquiredLeadRepository
				.getLeadCountSocialActor(actorId);
		statMap.put(LeadType.AcquiredLead, acquiredLeadCount);
		
		return statMap;
	}

	private LeadDto getLeadDtoFromClickThroughLead(
			ClickThroughLead clickThroughLead) throws Exception {
		LeadDto lead = this.getLeadDtoFromLeadDomain(clickThroughLead, LeadType.ClickThroughLead);
		return lead;

	}

	private LeadDto getLeadDtoFromSearchLead(SearchLead searchLead) throws Exception {

		LeadDto lead = this.getLeadDtoFromLeadDomain(searchLead, LeadType.SearchLead);
		return lead;
	}

	/**
	 * 
	 * @param acquiredLead
	 * @return
	 * @throws Exception 
	 */
	private LeadDto getLeadDtoFromAcquiredLead(AcquiredLead acquiredLead) throws Exception {

		LeadDto lead = this.getLeadDtoFromLeadDomain(acquiredLead, LeadType.AcquiredLead);
		return lead;
	}

	/**
	 * unifies all the leads into one list
	 */
	public List<LeadDto> getAllMemberLeads(Long memberId) throws Exception {

		log.info("Leads to be fetched for social actor " + memberId);
		List<LeadDto> leadList = this.getAllSocialActorLead(memberId);

		return leadList;
	}
	
	private List<LeadDto> getAllSocialActorLead(Long socialActorId) throws Exception {

		log.info("Leads to be fetched for social actor " + socialActorId);
		List<LeadDto> leadList = new ArrayList<LeadDto>();

		List<ContactLead> contactLeads = this.contactLeadRepository
				.findLeadsForSocialActor(socialActorId);
		for (ContactLead contactLead : contactLeads) {

			LeadDto lead = this.getLeadDtoFromContactLead(contactLead);
			leadList.add(lead);
		}

		List<ClickThroughLead> clickThroughLeads = this.clickThroughLeadRepository
				.findLeadsForSocialActor(socialActorId);
		for (ClickThroughLead clickThroughLead : clickThroughLeads) {

			LeadDto lead = this
					.getLeadDtoFromClickThroughLead(clickThroughLead);
			leadList.add(lead);
		}

		// FIXME: we do not load the search leads now since
		// they clutter the UI. Later we will provide 
		// UI based settings that will provide info 
		// as to what kind of leads to load
		/*List<SearchLead> searchLeads = this.searchLeadRepository
				.findLeadsForSocialActor(socialActorId);
		for (SearchLead searchLead : searchLeads) {
			LeadDto lead = this.getLeadDtoFromSearchLead(searchLead);
			leadList.add(lead);
		}*/

		List<AcquiredLead> acquiredLeads = this.acquiredLeadRepository
				.findLeadsForSocialActor(socialActorId);
		for (AcquiredLead acquiredLead : acquiredLeads) {
			LeadDto lead = this.getLeadDtoFromAcquiredLead(acquiredLead);
			leadList.add(lead);
		}

		List<TransferLead> transferLeads = this.transferLeadRepository
				.findAcceptedOrPendingLeadsForSocialActor(socialActorId);
		for (TransferLead transferLead : transferLeads) {
			LeadDto lead = this.getLeadDtoFromTransferLead(transferLead);
			leadList.add(lead);
		}

		return leadList;

	}

	/**
	 * unifies all the leads into one list
	 */
	public List<LeadDto> getAllOrganizationLeads(Long orgId) throws Exception {

		log.info("Leads to be fetched for org " + orgId);
		List<LeadDto> leadList = this.getAllSocialActorLead(orgId);

		return leadList;
	}
	
	private LeadDto getLeadDtoFromTransferLead(TransferLead transferLead) throws Exception {

		LeadDto lead = this.getLeadDtoFromLeadDomain(transferLead, LeadType.TransferLead);
		lead.setTransferState(transferLead.getTransferState());
		return lead;
	}



	public void storeSearchLeads(SearchResultDto searchResult) {

		List<SearchResultItemDto> searchResultItems = searchResult
				.getSearchResults();
		SearchResultSummery resultSummery = searchResult.getResultSummary();

		Date currentDate = new Date();

		for (SearchResultItemDto searchresultItem : searchResultItems) {

			SearchLead searchLead = this.searchLeadRepository.create();
			searchLead.setLeadType(LeadType.SearchLead);
			searchLead.setTimeStamp(currentDate);
			searchLead.setContactEmail(searchresultItem.getContactEmail());
			searchLead.setContactPhone(searchresultItem.getContactPhone());
			searchLead.setName(searchresultItem.getName());
			searchLead.setForSocialActorId(searchresultItem.getForSocialActorId());
			searchLead.setFromSocialActorId(searchResult.getFromSocialActorId());
			
			SearchLeadDetail searchLeadDetail = new SearchLeadDetail();
			searchLeadDetail.setSearchString(resultSummery.getSearchString());
			searchLeadDetail.setRank(searchresultItem.getRank());
			searchLeadDetail.setTotalRecordsFound(Long.parseLong(String
					.valueOf(resultSummery.getTotalRecords())));
			searchLead.setSearchDetail(searchLeadDetail);
			
			this.searchLeadRepository.save(searchLead);
		}
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	public void storeLead(LeadDto lead) throws Exception {

		log.info("Storing lead of type " + lead.getLeadType());

		if (lead.getLeadType().equals(LeadType.ContactLead) == true) {

			ContactMessageDetail contactInfo = lead.getContact();

			ContactLead contactLead = this.contactLeadRepository.create();
			contactLead.setContactMessageDetail(contactInfo);
			contactLead.setLeadType(LeadType.ContactLead);
            this.leadDtoToDomainAttributeUpdater.update(lead, contactLead);
			this.contactLeadRepository.save(contactLead);

		} else {

			ClickThroughLead clickThroughLead = this.clickThroughLeadRepository
					.create();
			this.leadDtoToDomainAttributeUpdater.update(lead, clickThroughLead);

			clickThroughLead.setLeadType(LeadType.ClickThroughLead);
			this.clickThroughLeadRepository.save(clickThroughLead);
		}
	}

	public List<LeadAssignmentDto> getAllLeadAssignmentsForSocialActor(
			Long socialActorId) throws Exception {

		List<LeadAssignmentDto> assignmentList = new ArrayList<LeadAssignmentDto>();
		List<LeadAssignment> assignedLeads = this.leadAssignmentRepository
				.getAssignmentsById(socialActorId);

		for (LeadAssignment assignment : assignedLeads) {

			LeadAssignmentDto leadAssignDto = new LeadAssignmentDto();
			LeadDto leadDto = null;

			if (assignment.getLeadType().equals(LeadType.ClickThroughLead) == true) {
				ClickThroughLead clickThroughLead = this.clickThroughLeadRepository
						.findOne(assignment.getAssociatedLead());
				leadDto = this.getLeadDtoFromClickThroughLead(clickThroughLead);
			}

			if (assignment.getLeadType().equals(LeadType.ContactLead) == true) {
				ContactLead contactLead = this.contactLeadRepository
						.findOne(assignment.getAssociatedLead());
				leadDto = this.getLeadDtoFromContactLead(contactLead);
			}

			if (assignment.getLeadType().equals(LeadType.SearchLead) == true) {
				SearchLead searchLead = this.searchLeadRepository
						.findOne(assignment.getAssociatedLead());
				leadDto = this.getLeadDtoFromSearchLead(searchLead);
			}

			if (assignment.getLeadType().equals(LeadType.AcquiredLead) == true) {
				AcquiredLead acquiredLead = this.acquiredLeadRepository
						.findOne(assignment.getAssociatedLead());
				leadDto = this.getLeadDtoFromAcquiredLead(acquiredLead);
			}

			leadAssignDto.setTheLead(leadDto);
			assignmentList.add(leadAssignDto);
		}

		return assignmentList;
	}

/*	public void assignLeads(LeadDto[] leads, Contact[] contacts, MemberInfoDto member) {

		BusinessProcess transferLeadProcess = this.businessProcessDefinitionRepository
				.findByProcessType(BusinessProcessType.transferLead);

		for (Contact contact : contacts) {
			WorkflowInitialRequest initialRequest = new WorkflowInitialRequest();
			initialRequest.setFlowDefinitionName(transferLeadProcess.getName());

			 set the parameters 
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(
					ProcessContextParameterConstants.TRANSFER_LEAD_LEAD_LIST,
					Arrays.asList(leads));
			parameters.put(
					ProcessContextParameterConstants.TRANSFER_LEAD_CONTACT,
					contact);
			parameters
					.put(ProcessContextParameterConstants.MEMBER_ID, member.getId());
			parameters
				.put(ProcessContextParameterConstants.BUSINESS_NAME, member.getCompany());
		

			 set the parameters 
			initialRequest.setExecutionParameters(parameters);

			 execute the workflow 
			this.workflowManager.initiate(initialRequest);
		}
	}*/

	public LeadDto getLead(Long id, LeadType leadType) throws Exception {

		LeadDto leadDto = null;
		if (leadType.equals(LeadType.ClickThroughLead) == true) {
			ClickThroughLead clickThroughLead = this.clickThroughLeadRepository
					.findOne(id);
			leadDto = this.getLeadDtoFromClickThroughLead(clickThroughLead);
		}

		if (leadType.equals(LeadType.ContactLead) == true) {
			ContactLead contactLead = this.contactLeadRepository.findOne(id);
			leadDto = this.getLeadDtoFromContactLead(contactLead);
		}

		if (leadType.equals(LeadType.SearchLead) == true) {
			SearchLead searchLead = this.searchLeadRepository.findOne(id);
			leadDto = this.getLeadDtoFromSearchLead(searchLead);
		}

		if (leadType.equals(LeadType.AcquiredLead) == true) {
			AcquiredLead acquiredLead = this.acquiredLeadRepository.findOne(id);
			leadDto = this.getLeadDtoFromAcquiredLead(acquiredLead);
		}
		
		if (leadType.equals(LeadType.TransferLead) == true) {
			TransferLead transferLead = this.transferLeadRepository.findOne(id);
			leadDto = this.getLeadDtoFromTransferLead(transferLead);
		}

		return leadDto;
	}

	public void saveLeadAlertSettings(MemberInfoDto memberInfo,
			LeadAlertSettingsDto leadAlertSettingsDto) {

		/* new settings */
		LeadAlertSettings leadAlertSettings = null;
		SocialPerson owner = this.socialPersonRepository.findOne(memberInfo
				.getId());

		if (leadAlertSettingsDto.getId() == Long.MIN_VALUE) {
			leadAlertSettings = this.leadAlertSettingsRepository.create();
			leadAlertSettings.setOwner(owner);
			leadAlertSettingsDto.setId(leadAlertSettings.getId());
		} else {
			leadAlertSettings = this.leadAlertSettingsRepository
					.getByOwner(owner);
		}

		/* update the values */
		if (leadAlertSettings != null) {
			log.info("Saving alert settings "
					+ leadAlertSettingsDto.toString());
			leadAlertSettingsDto.updateDomainObject(leadAlertSettings);
			this.leadAlertSettingsRepository.save(leadAlertSettings);
		}
	}

	public LeadAlertSettingsDto getLeadAlertSettings(MemberInfoDto memberInfo) {

		SocialPerson owner = this.socialPersonRepository.findOne(memberInfo
				.getId());
		LeadAlertSettings leadAlertSettings = this.leadAlertSettingsRepository
				.getByOwner(owner);

		LeadAlertSettingsDto alertSettings = new LeadAlertSettingsDto();
		alertSettings.updateFromDomainObject(leadAlertSettings);
		log.info("Returning alert settings " + alertSettings.toString());
		return alertSettings;
	}

	public void saveSearchFilterSetting(MemberInfoDto memberInfo,
			LeadSearchFilterSettingDto filterSettingsDto) throws Exception {
		log.info("Saving search filter " + filterSettingsDto.toString());

		LeadSearchFilterSetting searchFilterSetting = null;
		this.jaxbContext = JAXBContext
				.newInstance(LeadSearchFilterSettingConfig.class);
		Marshaller marshaller = this.jaxbContext.createMarshaller();
		Writer strWriter = new StringWriter();
		marshaller.marshal(filterSettingsDto.getSearchFilterSettingConfig(),
				strWriter);

		if (filterSettingsDto.getId() != Long.MIN_VALUE) {
			searchFilterSetting = this.leadSearchFilterSettingRepository
					.findOne(filterSettingsDto.getId());
		} else {
			searchFilterSetting = this.leadSearchFilterSettingRepository
					.create();
			searchFilterSetting.setActiveSince(new Date());
			SocialPerson owner = this.socialPersonRepository.findOne(memberInfo
					.getId());
			searchFilterSetting.setOwner(owner);
		}

		this.leadSearchFilterDtoToDomainAttributeUpdater.update(
				filterSettingsDto, searchFilterSetting);
		searchFilterSetting.setConfigListItems(strWriter.toString());
		AddressDto[] addresses = geocoder.geocode(
				searchFilterSetting.getStreet() + ", "
						+ searchFilterSetting.getCity() + ", "
						+ searchFilterSetting.getState() + " "
						+ searchFilterSetting.getZipCode() + ", "
						+ searchFilterSetting.getCountry(), null, null, "en");

		if (addresses.length >= 1) {
			AddressDto addressDto = addresses[0];
			searchFilterSetting.setLongitude(addressDto.getLongitude());
			searchFilterSetting.setLatitude(addressDto.getLatitude());
		}

		log.info("Saving lead settings with longitude "
				+ searchFilterSetting.getLongitude() + " latitude "
				+ searchFilterSetting.getLatitude());
		this.leadSearchFilterSettingRepository.save(searchFilterSetting);

		/* updating the id for the new one */
		filterSettingsDto.setId(searchFilterSetting.getId());

	}

	public List<LeadSearchFilterSettingDto> getLeadSearchFilterSettings(
			MemberInfoDto memberInfo) throws Exception {
		log.info("Getting search filter for " + memberInfo.getId());

		List<LeadSearchFilterSettingDto> retFilterList = new ArrayList<LeadSearchFilterSettingDto>();
		SocialPerson owner = this.socialPersonRepository.findOne(memberInfo
				.getId());
		List<LeadSearchFilterSetting> filterSettingList = this.leadSearchFilterSettingRepository
				.getByOwner(owner);

		this.jaxbContext = JAXBContext
				.newInstance(LeadSearchFilterSettingConfig.class);
		Unmarshaller unmarshaller = this.jaxbContext.createUnmarshaller();

		for (LeadSearchFilterSetting searchFilterSetting : filterSettingList) {
			LeadSearchFilterSettingDto searchFilterSettingDto = new LeadSearchFilterSettingDto();
			this.leadSearchFilterDomainToDtoAttributeUpdater.update(
					searchFilterSetting, searchFilterSettingDto);
			InputStream strInputStream = new ReaderInputStream(new StringReader(
					searchFilterSetting.getConfigListItems()));

			LeadSearchFilterSettingConfig settings = (LeadSearchFilterSettingConfig) unmarshaller
					.unmarshal(strInputStream);
			searchFilterSettingDto.updateListSettingsFromDomain(settings);
			retFilterList.add(searchFilterSettingDto);
		}

		return retFilterList;
	}

	public LeadSearchFilterSettingDto getLeadSearchFilterSettingsById(Long id)
			throws Exception {

		this.jaxbContext = JAXBContext
				.newInstance(LeadSearchFilterSettingConfig.class);
		Unmarshaller unmarshaller = this.jaxbContext.createUnmarshaller();

		LeadSearchFilterSetting searchFilterSetting = this.leadSearchFilterSettingRepository
				.findOne(id);
		LeadSearchFilterSettingDto searchFilterSettingDto = new LeadSearchFilterSettingDto();

		this.leadSearchFilterDomainToDtoAttributeUpdater.update(
				searchFilterSetting, searchFilterSettingDto);
		InputStream strInputStream = new ReaderInputStream(new StringReader(
				searchFilterSetting.getConfigListItems()));

		LeadSearchFilterSettingConfig settings = (LeadSearchFilterSettingConfig) unmarshaller
				.unmarshal(strInputStream);
		searchFilterSettingDto.updateListSettingsFromDomain(settings);

		return searchFilterSettingDto;

	}

	public CrmLeadSettingsDto getCrmSettings(OrganizationDetailInfoDto orgInfo)
			throws Exception {

		CrmLeadSettingsDto crmSettingsDto = null;
		SocialOrganization owner = this.socialOrganizationRepository
				.findOne(orgInfo.getId());
		CrmLeadSettings crmLeadSetting = this.crmLeadSettingsRepository
				.getByOwner(owner);

		if (crmLeadSetting != null) {
			crmSettingsDto = new CrmLeadSettingsDto();
			this.crmSettingsDomainToDtoAttributeUpdater.update(crmLeadSetting,
					crmSettingsDto);
			crmSettingsDto.setParameters(new HashMap<String, String>(
					crmLeadSetting.getParameters()));

			if (crmLeadSetting.getConfigListItems() != null) {
				this.jaxbContext = JAXBContext
						.newInstance(CrmLeadFieldSettingConfig.class);
				Unmarshaller unmarshaller = this.jaxbContext
						.createUnmarshaller();

				InputStream strInputStream = new ReaderInputStream(new StringReader(
						crmLeadSetting.getConfigListItems()));
				CrmLeadFieldSettingConfig config = (CrmLeadFieldSettingConfig) unmarshaller
						.unmarshal(strInputStream);
				crmSettingsDto.setCrmFieldSettingConfig(config);
			}
		}

		return crmSettingsDto;
	}

	private Lead getLeadfromLeadDto(LeadDto lead) {
		Lead retLead = null;

		LeadType type = lead.getLeadType();
		if (type == null)
			type = LeadType.AcquiredLead;

		switch (type) {
		case AcquiredLead:
			retLead = this.acquiredLeadRepository.findOne(lead.getId());
			break;
		case ClickThroughLead:
			retLead = this.clickThroughLeadRepository.findOne(lead.getId());
			break;
		case ContactLead:
			retLead = this.contactLeadRepository.findOne(lead.getId());
			break;
		case SearchLead:
			retLead = this.searchLeadRepository.findOne(lead.getId());
			break;
		case TransferLead:
			retLead = this.transferLeadRepository.findOne(lead.getId());
		default:
			break;
		}
		
		return retLead;
	}

	public void saveCrmSettings(OrganizationDetailInfoDto orgInfo,
			CrmLeadSettingsDto crmSettingsDto) throws Exception {

		CrmLeadSettings crmLeadSetting = null;
		this.jaxbContext = JAXBContext
				.newInstance(CrmLeadFieldSettingConfig.class);
		Marshaller marshaller = this.jaxbContext.createMarshaller();
		Writer strWriter = new StringWriter();
		marshaller
				.marshal(crmSettingsDto.getCrmFieldSettingConfig(), strWriter);

		if (crmSettingsDto.getId() == null || crmSettingsDto.getId() ==Long.MIN_VALUE) {
			log.info("Creating a new crm setting ");
			SocialOrganization owner = this.socialOrganizationRepository
					.findOne(orgInfo.getId());
			crmLeadSetting = this.crmLeadSettingsRepository.create();
			crmLeadSetting.setOwner(owner);
		} else {
			log.info("Getting crm setting " + crmSettingsDto.getId());
			crmLeadSetting = this.crmLeadSettingsRepository
					.findOne(crmSettingsDto.getId());
		}

		this.crmSettingsDtoToDomainAttributeUpdater.update(crmSettingsDto,
				crmLeadSetting);
		crmLeadSetting.setConfigListItems(strWriter.toString());
		crmLeadSetting.setParameters(new HashMap<String, String>(crmSettingsDto
				.getParameters()));
		this.crmLeadSettingsRepository.save(crmLeadSetting);

	}

	public List<LeadNoteDto> getLeadComments(LeadDto leadDto) {
		Lead lead = this.getLeadfromLeadDto(leadDto);
		SortedSet<LeadNoteDto> leadCommentDtosSet = new TreeSet<LeadNoteDto>(new Comparator<LeadNoteDto>() {

			public int compare(LeadNoteDto lead1, LeadNoteDto lead2) {
				if (lead1 == lead2)
					return 0;
				long leadId1;
				long leadId2;
				if (lead1 == null)
					leadId1 = 0;
				else if (lead1.getId() == null)
					leadId1 = 0;
				else
					leadId1 = lead1.getId();

				if (lead2 == null)
					leadId2 = 0;
				if (lead2.getId() == null)
					leadId2 = 0;
				else
					leadId2 = lead2.getId();
				    	
				if(leadId1 < leadId2)
					return -1;
				else if (leadId2 < leadId1)
					return 1;
				else return 0;
		}});
		
		getLeadComments(lead, leadCommentDtosSet);
		
		return new ArrayList<LeadNoteDto>(leadCommentDtosSet);
		
	}

	public void getLeadComments(Lead lead, Set<LeadNoteDto> leadCommentDtos) {
		getLeadComments(lead, leadCommentDtos, true);
	}

	public void getLeadComments(Lead lead, Set<LeadNoteDto> leadCommentDtos,
			boolean isRoot) {

		List<LeadComment> leadComments = this.leadCommentRepository
				.getLeadCommentByOwner(lead);

		for (LeadComment comment : leadComments) {
			LeadNoteDto leadNoteDto = new LeadNoteDto();
			leadNoteDto.setId(comment.getId());
			leadNoteDto.setLeadNote(comment.getLeadNote());
			leadNoteDto.setChildNote(!isRoot);
			if (comment.getCommenter() != null) {
				leadNoteDto.setCommenterFirstName(comment.getCommenter()
						.getFirstName());
				leadNoteDto.setCommenterLastName(comment.getCommenter()
						.getLastName());
				// get the photo again so that if photo has changed it will be
				// the updated one
				String commenterPhoto = this.socialPersonRepository
						.findPhotoById(comment.getCommenter().getId());
				if (StringUtils.isEmpty(commenterPhoto) != true) {
					leadNoteDto.setCommenterPhoto(commenterPhoto);
				}

				leadNoteDto.setCommenterId(comment.getCommenter().getId());
			}

			leadCommentDtos.add(leadNoteDto);
		}

		List<TransferLead> transferredLeads = this.transferLeadRepository
				.findActiveByParent(lead);
		for (Lead transferredLead : transferredLeads) {
			getLeadComments(transferredLead, leadCommentDtos, false);
		}
	}

	
	public void addLeadNote(LeadNoteDto leadNote, LeadDto leadDto, MemberInfoDto commenter) {

		/**
		 * 1. get the lead from LeadDto 2. Create a new note 3. Save
		 */
		SocialPerson commenterMember = null;
		if(commenter != null)
			 commenterMember = this.socialPersonRepository.findOne(commenter.getId());
		Lead lead = this.getLeadfromLeadDto(leadDto);
		LeadComment leadComment = this.leadCommentRepository.create();
		leadComment.setLeadNote(leadNote.getLeadNote());
		leadComment.setOwner(lead);
		leadComment.setCommenter(commenterMember);
		this.leadCommentRepository.save(leadComment);

		leadNote.setId(leadComment.getId());
	}

	public List<LeadActivityDto> getLeadActivity(LeadDto leadDto) {
		Lead lead = this.getLeadfromLeadDto(leadDto);
		List<LeadActivityDto> activityDtoList = new ArrayList<LeadActivityDto>();
		List<LeadActivity> leadActivityList = this.leadActivityRepository
				.getLeadActivityByOwner(lead);

		for (LeadActivity activity : leadActivityList) {
			LeadActivityDto leadActivityDto = new LeadActivityDto();
			leadActivityDto.setId(activity.getId());
			leadActivityDto.setActivityDescription(activity
					.getActivityDescription());
			leadActivityDto.setDueDate(activity.getDueDate());
			leadActivityDto.setLeadActivityType(activity.getLeadActivityType());
			leadActivityDto.setSubjectActivity(activity.getSubjectActivity());
			leadActivityDto.setStartDate(activity.getStartDate());
			leadActivityDto.setEndDate(activity.getEndDate());		 	
			leadActivityDto.setVenue(activity.getVenue());			
			leadActivityDto.setTimeStamp(activity.getTimeStamp());
			activityDtoList.add(leadActivityDto);
		}

		return activityDtoList;
	}

	public void addLeadActivity(LeadActivityDto leadActivity, LeadDto leadDto) {
		Lead lead = this.getLeadfromLeadDto(leadDto);
		LeadActivity activity = this.leadActivityRepository.create();
		activity.setOwner(lead);
		activity.setLeadActivityType(leadActivity.getLeadActivityType());
		activity.setActivityDescription(leadActivity.getActivityDescription());
		activity.setDueDate(leadActivity.getDueDate());
		activity.setSubjectActivity(leadActivity.getSubjectActivity());
		activity.setStartDate(leadActivity.getStartDate());
		activity.setEndDate(leadActivity.getEndDate());			
		activity.setVenue(leadActivity.getVenue());			
		activity.setTimeStamp(leadActivity.getTimeStamp());

		this.leadActivityRepository.save(activity);
		leadActivity.setId(activity.getId());
	}
	
	public void updateLeadActivity(LeadActivityDto leadActivity) {
		LeadActivity activity = this.leadActivityRepository.findOne(leadActivity.getId());
		activity.setLeadActivityType(leadActivity.getLeadActivityType());
		activity.setActivityDescription(leadActivity.getActivityDescription());
		activity.setDueDate(leadActivity.getDueDate());
		activity.setSubjectActivity(leadActivity.getSubjectActivity());
		activity.setStartDate(leadActivity.getStartDate());
		activity.setEndDate(leadActivity.getEndDate());			
		activity.setVenue(leadActivity.getVenue());
		
		this.leadActivityRepository.save(activity);
	}	

	public List<LeadActivityDto> getLeadActivityByOrg(OrganizationDetailInfoDto orgInfo) {

		List<LeadActivityDto> activities = new ArrayList<LeadActivityDto>();
		List<LeadActivity> activityList = this.leadActivityRepository
				.getLeadActivityByOrganization(orgInfo.getId());

		for (LeadActivity activity : activityList) {
			LeadActivityDto leadActivityDto = new LeadActivityDto();
			leadActivityDto.setId(activity.getId());
			leadActivityDto.setActivityDescription(activity
					.getActivityDescription());
			leadActivityDto.setDueDate(activity.getDueDate());
			leadActivityDto.setLeadActivityType(activity.getLeadActivityType());
			leadActivityDto.setSubjectActivity(activity.getSubjectActivity());
			leadActivityDto.setTimeStamp(activity.getTimeStamp());
			leadActivityDto.setStartDate(activity.getStartDate());
			leadActivityDto.setEndDate(activity.getEndDate());			
			leadActivityDto.setVenue(activity.getVenue());
			activities.add(leadActivityDto);
		}

		return activities;
	}

	public void saveImportedLeads(List<LeadDto> leadList,
			OrganizationDetailInfoDto orgInfo) throws Exception {

		if (leadList != null) {

			for (LeadDto leadImport : leadList) {

				AcquiredLead acquiredLead = this.acquiredLeadRepository
						.create();
				
				acquiredLead.setForSocialActorId(orgInfo.getId());
				this.leadDtoToDomainAttributeUpdater.update(leadImport, acquiredLead);
				this.acquiredLeadRepository.save(acquiredLead);
			}
			
		}
	}

	public List<LeadMessageDto> getLeadSentMessage(LeadDto leadDto) {

		List<LeadMessageDto> leadMessageDto = new ArrayList<LeadMessageDto>();
		Lead lead = this.getLeadfromLeadDto(leadDto);
		List<LeadMessage> leadMessageList = this.leadMessageRepository
				.getLeadCommentByOwner(lead);

		for (LeadMessage message : leadMessageList) {
			LeadMessageDto messageDto = new LeadMessageDto();
			messageDto.setId(message.getId());
			messageDto.setLeadNote(message.getLeadNote());
			
			if(message.getCommenter() != null){
				messageDto.setCommenterFirstName(message.getCommenter().getFirstName());
				messageDto.setCommenterLastName(message.getCommenter().getLastName());
				messageDto.setCommenterId(message.getCommenter().getId());
				
				//the photo might have changed so we reload the photo again
				String commenterPhoto = this.socialPersonRepository.findPhotoById(message.getCommenter().getId());
				if(commenterPhoto != null){
					messageDto.setCommenterPhoto(commenterPhoto);
				}
			}
			
			leadMessageDto.add(messageDto);
		}

		return leadMessageDto;
	}

	/**
	 * 
	 * @param leadMessage
	 * @param lead
	 * @throws Exception 
	 * @throws ParseErrorException 
	 * @throws ResourceNotFoundException 
	 */
	public void sendLeadMessage(LeadMessageDto leadMessageDto, LeadDto leadDto, MemberInfoDto commenter) throws ResourceNotFoundException, ParseErrorException, Exception {

		SocialPerson commenterMember = this.socialPersonRepository.findOne(commenter.getId());
		Lead lead = this.getLeadfromLeadDto(leadDto);
		LeadMessage leadMessage = this.leadMessageRepository.create();
		leadMessage.setLeadNote(leadMessageDto.getLeadNote());
		leadMessage.setOwner(lead);
		leadMessage.setCommenter(commenterMember);		

		/* send the message 1st */
		this.messagingService.sendLeadMessage(leadMessageDto, leadDto, commenter);
		this.leadMessageRepository.save(leadMessage);
	}

	public void saveAffiliateLeadList(AffiliateLeadListDto uploadLeadList) throws Exception {

		/* save all the affiliate leads */
		List<LeadDto> affiliateLeadListDto = uploadLeadList
				.getLeadList();
		if (affiliateLeadListDto != null && affiliateLeadListDto.size() >= 0) {
			
			List<AffiliateLead> affiliateLeads = new ArrayList<AffiliateLead>();

			for (LeadDto affiliateleadDto : affiliateLeadListDto) {

				AffiliateLead affiliateLead = this.affiliateLeadRepository
						.create();
				this.affiliateLeadDtoToDomainAttributeUpdater.update(affiliateleadDto, affiliateLead);
				affiliateLead.setTitle(affiliateleadDto.getTitle());
				affiliateLead.setDescription(affiliateleadDto.getDescription());
				affiliateLead.setNameVerified(affiliateleadDto
						.getNameVerified());
				affiliateLead.setIntentVerified(affiliateleadDto
						.getIntentVerified());
				affiliateLead.setAffiliateId(affiliateleadDto
						.getForSocialActorId());
				affiliateLead.setDefaultPrice(uploadLeadList.getDefaultPrice());
				
				if (affiliateleadDto.getLocation() != null) {
					Address leadAddr = this.geoCoderService
							.getVerifiedAddressFromDto(affiliateleadDto
									.getLocation());
					affiliateLead.setLocation(leadAddr);
				}

				affiliateLead.setTimeStamp(new Date());
				this.affiliateLeadRepository.save(affiliateLead);

				affiliateLeads.add(affiliateLead);
			}

			/* create the lead list */
			AffiliateLeadList affiliateLeadList = this.affiliateLeadListRepository
					.create();
			affiliateLeadList.setIngestedLeads(affiliateLeads);
			affiliateLeadList.setAffiliateId(uploadLeadList.getAffiliateId());
			affiliateLeadList.setIngestionTime(new Date());
			affiliateLeadList.setName(uploadLeadList.getName());
			affiliateLeadList.setDefaultPrice(uploadLeadList.getDefaultPrice());
			affiliateLeadList.setSourceDescription(uploadLeadList
					.getSourceDescription());
			this.affiliateLeadListRepository.save(affiliateLeadList);
		}
	}

	public List<AffiliateLeadListDto> getAffiliateLeadListForBusiness(
			OrganizationDetailInfoDto orgInfo) {

		List<AffiliateLeadListDto> affiliateLeadListDto = new ArrayList<AffiliateLeadListDto>();
		
		List<AffiliateLeadList> affiliateLeadList = null;
		if(orgInfo != null){
			affiliateLeadList = this.affiliateLeadListRepository.findByOwner(orgInfo.getId());
		}
		else {
			affiliateLeadList = this.affiliateLeadListRepository.findAll();
		}

		for (AffiliateLeadList affList : affiliateLeadList) {

			AffiliateLeadListDto affListDto = new AffiliateLeadListDto();
			affListDto.setId(affList.getId());
			affListDto.setAffiliateId(affList.getAffiliateId());
			affListDto.setIngestionTime(affList.getIngestionTime());
			affListDto.setName(affList.getName());
			affListDto.setDefaultPrice(affList.getDefaultPrice());
			affListDto.setSourceDescription(affList.getSourceDescription());

			affiliateLeadListDto.add(affListDto);
		}

		return affiliateLeadListDto;
	}

	private LeadDto getAffiliateLeadDto(AffiliateLead affLead) throws Exception {
		
		LeadDto affLeadDto = new LeadDto();
        this.affiliateLeadDomainToDtoAttributeUpdater.update(affLead, affLeadDto);
        Long leadSourceId = affLead.getAffiliateId();
        if(leadSourceId != null){
        SocialOrganization leadSourceOrganization = this.socialOrganizationRepository.findOne(leadSourceId);
        if(leadSourceOrganization != null)
        	affLeadDto.setLeadSource(leadSourceOrganization.getBusinessName());
        }

        if(affLead.getLocation() != null){
			AddressDto addr = new AddressDto();
			addr.setStreet(affLead.getLocation().getStreet());
			addr.setCity(affLead.getLocation().getCity());
			addr.setState(affLead.getLocation().getState());
			addr.setCounty(affLead.getLocation().getCountry());
			addr.setZipCode(affLead.getLocation().getZipCode());
			affLeadDto.setLocation(addr);
		}
		return affLeadDto;
	}

	public AffiliateLeadListDto getAffiliateLeadListDetail(Long listId) throws Exception{
		
		AffiliateLeadList affList = this.affiliateLeadListRepository
				.findOne(listId);

		if (affList != null) {

			AffiliateLeadListDto affListDto = new AffiliateLeadListDto();
			affListDto.setId(affList.getId());
			affListDto.setAffiliateId(affList.getAffiliateId());
			affListDto.setIngestionTime(affList.getIngestionTime());
			affListDto.setName(affList.getName());
			affListDto.setDefaultPrice(affList.getDefaultPrice());
			affListDto.setSourceDescription(affList.getSourceDescription());

			List<AffiliateLead> ingestedLeads = affList.getIngestedLeads();
			
			List<LeadDto> ingestedLeadsDto = new ArrayList<LeadDto>();
			affListDto.setLeadList(ingestedLeadsDto);

			/* get all the leads */
			for (AffiliateLead affLead : ingestedLeads) {
				LeadDto affLeadDto = this.getAffiliateLeadDto(affLead);
				ingestedLeadsDto.add(affLeadDto);
				
				//get the address now 
				if(affLead.getLocation() != null){
					AddressDto addr = new AddressDto();
					addr.setStreet(affLead.getLocation().getStreet());
					addr.setCity(affLead.getLocation().getCity());
					addr.setState(affLead.getLocation().getState());
					addr.setCounty(affLead.getLocation().getCountry());
					addr.setZipCode(affLead.getLocation().getZipCode());
					affLeadDto.setLocation(addr);
				}
			}

			return affListDto;
		}

		return null;
	}

	public List<LeadDto> getAffiliateLeadsByFilter(Long filterId) throws Exception {
		LeadSearchFilterSetting searchFilterSetting = this.leadSearchFilterSettingRepository
				.findOne(filterId);



		List<LeadDto> affiliateLeadList = new ArrayList<LeadDto>();
		
		Page<AffiliateLead> affiliateLeads = this.affiliateLeadRepository
				.searchAffiliateLeads(searchFilterSetting, new PageRequest(0, Integer.MAX_VALUE));

		Long orgId = getSearchFilterOwnerOrganization(searchFilterSetting);
		
		for (AffiliateLead affLead : affiliateLeads) {
			boolean alreadyPuchased = false;

			// Filter out leads that have already been purchased by this lead
			for (AcquiredLead acquiredLead : affLead.getAcquiredLeads()) {

				if (acquiredLead.getForSocialActorId().equals(orgId)) {
					alreadyPuchased = true;
					continue;
				}
			}
			if (alreadyPuchased == false) {
				LeadDto affLeadDto = this.getAffiliateLeadDto(affLead);
				affiliateLeadList.add(affLeadDto);
			}
		}
		searchFilterSetting.setLastResultCount(affiliateLeads.getTotalElements());
		leadSearchFilterSettingRepository.save(searchFilterSetting);
		return affiliateLeadList;
	}

	private Long getSearchFilterOwnerOrganization(
			LeadSearchFilterSetting searchFilterSetting) {
		Long orgId = null;
		SocialPerson searchOwner = searchFilterSetting.getOwner();
		if (searchOwner != null) {
			SocialEmployee employer = searchOwner.getEmployer();
			if (employer != null) {
				SocialOrganization org = (SocialOrganization)employer.getContactFrom();
				if (org != null)
					orgId = org.getId();
			}
		}
		return orgId;
	}

	public void purchaseAffilateLead(OrganizationDetailInfoDto orgInfo, LeadDto affLeadDto) {
		/**
		 * 1. Get the affiliate lead 2. Get the social org 3. Create acquired
		 * lead 4. Connect acquired and affiliate lead
		 */
		AffiliateLead affLead = this.affiliateLeadRepository.findOne(affLeadDto
				.getId());

		/* create a acquired lead */
		AcquiredLead acquiredLead = this.acquiredLeadRepository.create();
		acquiredLead.setCompany(affLead.getCompany());
		acquiredLead.setContactEmail(affLead.getContactEmail());
		acquiredLead.setContactPhone(affLead.getContactPhone());
		acquiredLead.setFirstName(affLead.getFirstName());
		acquiredLead.setLastName(affLead.getLastName());
		acquiredLead.setIndustry(affLead.getIndustry());
		acquiredLead.setKeyword(affLead.getKeyword());
		
		acquiredLead.setTimeStamp(new Date());
		acquiredLead.setLeadType(LeadType.AcquiredLead);
		acquiredLead.setForSocialActorId(orgInfo.getId());
		acquiredLead.setLeadStatus(LeadStatusType.Unassigned);
		acquiredLead.setSicCode(affLead.getIndustry());
		this.acquiredLeadRepository.save(acquiredLead);

		/* connect them together */
		affLead.getAcquiredLeads().add(acquiredLead);
		this.affiliateLeadRepository.save(affLead);

		/* create a purchase record */
		SocialOrganization socialOrg = this.socialOrganizationRepository
				.findOne(orgInfo.getId());
		OrganizationAccount orgAccount = this.organizationAccountRepository
				.findByOrg(socialOrg);

		/* create one */
		if (orgAccount == null) {
			orgAccount = this.organizationAccountRepository.create();
			orgAccount.setOwner(socialOrg);
			this.organizationAccountRepository.save(orgAccount);
			log.info("Created org account " + socialOrg.getId()
					+ " account id " + orgAccount.getId());
		}

		LeadPurchaseRecord leadPurchaseRecord = this.organizationAccountRepository
				.createLeadPurchaseRecord(affLead, acquiredLead);
		leadPurchaseRecord.setTransactionAmount(affLeadDto.getPurchasePrice());
		this.leadPurchaseRecordRepository.save(leadPurchaseRecord);

		orgAccount.getAccountRecords().add(leadPurchaseRecord);
		this.organizationAccountRepository.save(orgAccount);
	}

	public void saveLead(LeadDto lead, OrganizationDetailInfoDto orgInfo,
			boolean overrideTimestamp) throws Exception {
		if (lead.getId() == null)
			createLead(lead, orgInfo, overrideTimestamp);
		else
			updateLead(lead, orgInfo, overrideTimestamp);
	}

	public void saveLead(LeadDto lead, OrganizationDetailInfoDto orgInfo) throws Exception{
		saveLead(lead, orgInfo, false);
	}

	public void createLead(LeadDto lead, OrganizationDetailInfoDto orgInfo,
			boolean overrideTimestamp) throws Exception {

		Lead newLead = null;
		LeadType type = lead.getLeadType();
		if (type == null)
			type = LeadType.AcquiredLead;

		switch (type) {
		case AcquiredLead:
			newLead = this.acquiredLeadRepository.create();
			newLead.setLeadType(LeadType.AcquiredLead);
			break;
		case ClickThroughLead:
			newLead = this.clickThroughLeadRepository.create();
			newLead.setLeadType(LeadType.ClickThroughLead);
			break;
		case ContactLead:
			newLead = this.contactLeadRepository.create();
			newLead.setLeadType(LeadType.ContactLead);
			break;
		case SearchLead:
			newLead = this.searchLeadRepository.create();
			newLead.setLeadType(LeadType.SearchLead);
			break;
		default:
			// Cant save
			return;
		}

		this.leadDtoToDomainAttributeUpdater.update(lead, newLead);
	
		if(lead.getLocation() != null){
			Address leadAddr = this.geoCoderService.getVerifiedAddressFromDto(lead.getLocation());
			newLead.setLocation(leadAddr);
		}
		
		if (overrideTimestamp == true)
			newLead.setTimeStamp(lead.getTimeStamp());
		else
			newLead.setTimeStamp(new Date());
		
		newLead.setForSocialActorId(orgInfo.getId());
		newLead.setLeadStatus(LeadStatusType.Unassigned);
		newLead.setViewed(Boolean.FALSE);
		newLead.setDateCreated(new Date());

		switch (type) {
		case AcquiredLead:
			this.acquiredLeadRepository.save((AcquiredLead) newLead);
			break;
		case ClickThroughLead:
			this.clickThroughLeadRepository.save((ClickThroughLead) newLead);
			break;
		case ContactLead:
			this.contactLeadRepository.save((ContactLead) newLead);
			break;
		case SearchLead:
			this.searchLeadRepository.save((SearchLead) newLead);
			break;
		default:
			// Cant save
			return;
		}
		lead.setDateCreated(newLead.getDateCreated());
		lead.setId(newLead.getId());
	}

	public void updateLead(LeadDto lead, OrganizationDetailInfoDto orgInfo,
			boolean overrideTimestamp) throws Exception {

		Lead updateLead = null;
        
		updateLead = this.getLeadfromLeadDto(lead);

		if(updateLead == null){
			return;
		}
		
		if(lead.getLocation() != null){
			Address leadAddr = this.geoCoderService.getVerifiedAddressFromDto(lead.getLocation());
			updateLead.setLocation(leadAddr);
		}
		
		this.leadDtoToDomainAttributeUpdater.update(lead, updateLead);
		if (overrideTimestamp == true)
			updateLead.setTimeStamp(lead.getTimeStamp());
		else
			updateLead.setTimeStamp(new Date());
		
		updateLead.setForSocialActorId(orgInfo.getId());
		
		saveLeadBasedOnType(updateLead);
		
		updateTransferHeiarchy(updateLead);
	}
	
	public List<LeadDto> getTransferRelatedLeads(LeadDto leadDto) throws Exception{
		Lead lead = this.getLeadfromLeadDto(leadDto);
		Lead rootLead = null;
		if (lead instanceof TransferLead) {
			rootLead = findTransferLeadRoot((TransferLead) lead);
		} else
			rootLead = lead;
		
		List<Lead> relatedLeads = new ArrayList<Lead>();
		findChildLeadsForLead(rootLead, relatedLeads);
		
		List<LeadDto> relatedLeadsDto = new ArrayList<LeadDto>();
		for(Lead relatedLead : relatedLeads) {
			relatedLeadsDto.add(getLeadDtoFromLeadDomain(relatedLead, relatedLead.getLeadType()));
		}
		
		return relatedLeadsDto;
	}
	
	private void findChildLeadsForLead(Lead rootLead,
			List<Lead> relatedLeads) {
		relatedLeads.add(rootLead);
		
		List<TransferLead> transferLeads = this.transferLeadRepository
				.findActiveByParent(rootLead);

		for (Lead transferLead : transferLeads) {
			findChildLeadsForLead(transferLead, relatedLeads);
		}

	}
	
	private void updateTransferHeiarchy(Lead updateLead) {
		Lead rootNode = null;
		if (updateLead instanceof TransferLead) {
			rootNode = findTransferLeadRoot((TransferLead) updateLead);
		} else
			rootNode = updateLead;

		cascadeLeadUpdateToTransferChildren(rootNode, updateLead);
	}

	private Lead findTransferLeadRoot(TransferLead transferLead) {
		Lead parent = transferLead.getParentLead();
		if (parent == null)
			throw new CoreServiceException(
					"Transfer Lead does not have a parent");

		if (parent instanceof TransferLead) {
			return findTransferLeadRoot((TransferLead) parent);
		} else
			return parent;
	}

	private void cascadeLeadUpdateToTransferChildren(Lead rootLead,
			Lead updateLead) {
		if (!rootLead.getId().equals(updateLead.getId())) {
			rootLead.setCompany(updateLead.getCompany());
			rootLead.setContactEmail(updateLead.getContactEmail());
			rootLead.setContactPhone(updateLead.getContactPhone());
			rootLead.setFirstName(updateLead.getFirstName());
			rootLead.setIndustry(updateLead.getIndustry());
			rootLead.setKeyword(updateLead.getKeyword());
			rootLead.setLastName(updateLead.getLastName());
			rootLead.setLeadPhotoLocation(updateLead.getLeadPhotoLocation());
			rootLead.setLocation(updateLead.getLocation());
			rootLead.setName(updateLead.getName());
			rootLead.setSicCode(updateLead.getSicCode());
			saveLeadBasedOnType(rootLead);
		}
		List<TransferLead> transferLeads = this.transferLeadRepository
				.findActiveByParent(rootLead);

		for (Lead transferLead : transferLeads) {
			cascadeLeadUpdateToTransferChildren(transferLead, updateLead);
		}

	}

	private void saveLeadBasedOnType(Lead lead) {
		LeadType type = lead.getLeadType();
		if (type == null)
			type = LeadType.AcquiredLead;
		
		switch (type) {
		case AcquiredLead:
			this.acquiredLeadRepository.save((AcquiredLead) lead);
			break;
		case ClickThroughLead:
			this.clickThroughLeadRepository.save((ClickThroughLead) lead);
			break;
		case ContactLead:
			this.contactLeadRepository.save((ContactLead) lead);
			break;
		case SearchLead:
			this.searchLeadRepository.save((SearchLead) lead);
			break;
		case TransferLead:
			this.transferLeadRepository.save((TransferLead) lead);
			break;			
		default:
			// Cant save
			return;
		}
	}
	
	/**
	 * @param id
	 */
	public void approveLeadTransfer(Long leadId, Long memberId) {

		TransferLead transferLead = this.transferLeadRepository.findOne(leadId);
		
		if (transferLead != null) {
			/* set the parameters */
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters
			.put(ProcessContextParameterConstants.ACTION_RESULT, "approved");		 
			parameters
			.put(ProcessContextParameterConstants.TRANSFER_LEAD_ID, leadId);
			parameters
			.put(ProcessContextParameterConstants.MEMBER_ID, memberId);
			try {

				WorkflowResumeRequest resumeRequest = new WorkflowResumeRequest();
				resumeRequest.setExecutionParameters(parameters);
				resumeRequest
						.setWorkflowInstanceId(transferLead.getTransferLeadRequest().getWorkflowProcessId());
				this.workflowManager.resume(resumeRequest);

			} catch (Exception exp) {
				log.error("Error in work flow signalling ", exp);
			}
		}
	}

	/**
	 * @param id
	 */
	public void rejectLeadTransfer(Long leadId, Long memberId) {

		TransferLead transferLead = this.transferLeadRepository.findOne(leadId);

		if (transferLead != null) {
			/* set the parameters */
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters
			.put(ProcessContextParameterConstants.ACTION_RESULT, "rejected");	
			parameters
			.put(ProcessContextParameterConstants.TRANSFER_LEAD_ID, leadId);
			parameters
			.put(ProcessContextParameterConstants.MEMBER_ID, memberId);
			
			try {

				WorkflowResumeRequest resumeRequest = new WorkflowResumeRequest();
				resumeRequest.setExecutionParameters(parameters);
				resumeRequest
					.setWorkflowInstanceId(transferLead.getTransferLeadRequest().getWorkflowProcessId());
				this.workflowManager.resume(resumeRequest);

			} catch (Exception exp) {
				log.error("Error in work flow signalling ", exp);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.similan.service.api.LeadManagementService#getMemberLeads(java.lang.Long, boolean, boolean, boolean, boolean, boolean)
	 */
	public List<LeadDto> getMemberLeads(Long id, LeadType[] includeLeadTypes,
			boolean includeUnreadOnly)throws Exception{
		log.info("Leads to be fetched for social actor " + id);
		List<LeadDto> leadList = this.getAllSocialActorLead(id, includeLeadTypes,
				 includeUnreadOnly);

		return leadList;
	}

	private List<LeadDto> getAllSocialActorLead(Long socialActorId,
			LeadType[] includeLeadTypes, boolean includeUnreadOnly)
			throws Exception {

		log.info("Leads to be fetched for social actor " + socialActorId);
		List<LeadDto> leadList = new ArrayList<LeadDto>();
		List<LeadType> types = Arrays.asList(includeLeadTypes);

		if (types.contains(LeadType.ContactLead)) {
			List<ContactLead> contactLeads = this.contactLeadRepository
					.findLeadsForSocialActor(socialActorId);
			for (ContactLead contactLead : contactLeads) {
				if (includeUnreadOnly == false
						|| (includeUnreadOnly == true && contactLead
								.getViewed() == false)) {
					LeadDto lead = this.getLeadDtoFromContactLead(contactLead);
					leadList.add(lead);
				}
			}
		}

		if (types.contains(LeadType.ClickThroughLead)) {
			List<ClickThroughLead> clickThroughLeads = this.clickThroughLeadRepository
					.findLeadsForSocialActor(socialActorId);
			for (ClickThroughLead clickThroughLead : clickThroughLeads) {
				if (includeUnreadOnly == false
						|| (includeUnreadOnly == true && clickThroughLead
								.getViewed() == false)) {

					LeadDto lead = this
							.getLeadDtoFromClickThroughLead(clickThroughLead);
					leadList.add(lead);
				}
			}
		}

		if (types.contains(LeadType.SearchLead)) {
			List<SearchLead> searchLeads = this.searchLeadRepository
					.findLeadsForSocialActor(socialActorId);
			for (SearchLead searchLead : searchLeads) {
				if (includeUnreadOnly == false
						|| (includeUnreadOnly == true && searchLead.getViewed() == false)) {
					LeadDto lead = this.getLeadDtoFromSearchLead(searchLead);
					leadList.add(lead);
				}
			}
		}

		if (types.contains(LeadType.AcquiredLead)) {
			List<AcquiredLead> acquiredLeads = this.acquiredLeadRepository
					.findLeadsForSocialActor(socialActorId);
			for (AcquiredLead acquiredLead : acquiredLeads) {
				if (includeUnreadOnly == false
						|| (includeUnreadOnly == true && acquiredLead
								.getViewed() == false)) {

					LeadDto lead = this
							.getLeadDtoFromAcquiredLead(acquiredLead);
					leadList.add(lead);
				}
			}
		}

		if (types.contains(LeadType.TransferLead)) {
			List<TransferLead> transferLeads = this.transferLeadRepository
					.findAcceptedOrPendingLeadsForSocialActor(socialActorId);
			for (TransferLead transferLead : transferLeads) {
				if (includeUnreadOnly == false
						|| (includeUnreadOnly == true && transferLead
								.getViewed() == false)) {

					LeadDto lead = this
							.getLeadDtoFromTransferLead(transferLead);
					leadList.add(lead);
				}
			}
		}

		return leadList;
	}

	public int getCountNewLeads(Long id) {
		int count = 0;

		count += this.contactLeadRepository
				.getNotViewedLeadCountSocialActor(id);
		count += this.clickThroughLeadRepository
				.getNotViewedLeadCountSocialActor(id);
		
		//FIXME: for now we exclude search lead
		//later we will have configuration driven filter
		//count += this.searchLeadRepository
				//.getNotViewedLeadCountSocialActor(id);
		count += this.acquiredLeadRepository
				.getNotViewedLeadCountSocialActor(id);
		count += this.transferLeadRepository
				.getNotViewedLeadCountSocialActor(id);
		return count;

	}
	
	/* (non-Javadoc)
	 * @see com.similan.service.api.LeadManagementService#getCountAvailableAffiliateLeads(java.lang.Long)
	 */
	public int getCountAvailableAffiliateLeads(Long id) {
		int count = 0;
		SocialPerson owner = this.socialPersonRepository.findOne(id);
		
		List<LeadSearchFilterSetting> filterSettingList = this.leadSearchFilterSettingRepository
				.getByOwner(owner);
		if (filterSettingList != null) {
			for (LeadSearchFilterSetting setting : filterSettingList) {
				if (setting.getLastResultCount() != null)
					count += setting.getLastResultCount();
			}
		}
		return count;
	}

	public void deleteLeadNote(LeadNoteDto noteTobeDeleted) {
		if(noteTobeDeleted.getId() != Long.MIN_VALUE){
			log.info("Deleteing lead note " + noteTobeDeleted);
			this.leadCommentRepository.delete(noteTobeDeleted.getId());
		}
		
	}
	public void deleteLeadActivity(LeadActivityDto activity) {
		if(activity.getId() != Long.MIN_VALUE){
			log.info("Deleteing lead activity " + activity);
			this.leadActivityRepository.delete(activity.getId());
		}
		
	}
	public void deleteLead(LeadDto leadDelete) {
		Lead lead = this.getLeadfromLeadDto(leadDelete);
		if (lead == null)
			return;
		
		validateCanDeleteLead(lead);
		log.info("Deleting lead with id " + leadDelete.getId());

		// FIXME: Couldn't create a deleteByOwner in the repositories as it would 
		// result in a "org.hibernate.hql.QueryExecutionRequestException: Not supported for DML operations"
		// For now, fetching all of the comments messages and activities and deleting them one by one.
		
		for(LeadComment comment : this.leadCommentRepository.getLeadCommentByOwner(lead)) {
			this.leadCommentRepository.delete(comment.getId());}
		
		for(LeadMessage message : this.leadMessageRepository.getLeadCommentByOwner(lead)){
			this.leadMessageRepository.delete(message.getId());}
		
		for(LeadActivity activity : this.leadActivityRepository.getLeadActivityByOwner(lead)){
			this.leadActivityRepository.delete(activity.getId());}
		// End Fixme 

		switch (leadDelete.getLeadType()) {
		case AcquiredLead:
			this.acquiredLeadRepository.delete(lead.getId());
			break;
		case ClickThroughLead:
			this.clickThroughLeadRepository.delete(lead.getId());
			break;
		case ContactLead:
			this.contactLeadRepository.delete(lead.getId());
			break;
		case SearchLead:
			this.searchLeadRepository.delete(lead.getId());
			break;
		default:
			// Cant save
			return;
		}

	}
	
	private void validateCanDeleteLead(Lead leadDelete) {
		if (leadDelete.getLeadType() == LeadType.TransferLead)
			throw new LeadDeleteException( 
					"Cannot delete leads of type TransferLead");

		List<TransferLead> leads = this.transferLeadRepository
				.findActiveOrPendingByParent(leadDelete);

		if (leads.size() > 0)
			throw new LeadDeleteException(
					"Cannot delete leads that are the parent of transferred leads");
	}

	public void deleteLeads(LeadDto[] leadsToDelete){
		if(leadsToDelete != null && leadsToDelete.length > 0){
			
			log.info("Deleting leads ");
			for(LeadDto leadDelete : leadsToDelete){
				deleteLead(leadDelete);
			}
		}
		else {
			log.info("No lead to delete the list is empty ");
		}
	}

	public List<LeadTransferStatusDto> getLeadTransferStatus(LeadDto leadDto) {
		Lead lead = this.getLeadfromLeadDto(leadDto);
		List<LeadTransferStatusDto> statusList = new ArrayList<LeadTransferStatusDto> ();
		List<TransferLead> leads = this.transferLeadRepository.findActiveOrPendingByParent(lead);

		for(TransferLead transferLead : leads) {
			LeadTransferStatusDto statusDto = new LeadTransferStatusDto();
			SocialOrganization org =  this.socialOrganizationRepository.findOne(transferLead.getForSocialActorId());
			statusDto.setTransfereeName(org.getBusinessName());
			statusDto.setTransferState(transferLead.getTransferState());
			statusDto.setDateCreated(transferLead.getDateCreated());
			
			statusList.add(statusDto);
		}
		return statusList;
	}

	public void updateLeadNote(LeadNoteDto leadNoteDto) {
		LeadComment leadComment = this.leadCommentRepository
				.findOne(leadNoteDto.getId());
		leadComment.setLeadNote(leadNoteDto.getLeadNote());
		leadComment.getLeadNote().setTimeStamp(new Date());
		this.leadCommentRepository.save(leadComment);

	}

	/*public void performCrmSync(Contact contact) {

		if (this.crmIntegrationService.isSynchronizationInProcess(contact))
			throw new CoreServiceException(
					CoreServiceErrorCode.CRM_SYNC_ALREADY_IN_PROCESS);

	}*/
}

