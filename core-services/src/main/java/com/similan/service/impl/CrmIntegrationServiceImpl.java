package com.similan.service.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.lead.CrmLeadSettings;
import com.similan.domain.repository.community.SocialOrganizationRepository;
import com.similan.domain.repository.lead.CrmLeadSettingsRepository;
import com.similan.domain.repository.lead.CrmLeadSyncRecordRepository;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.lead.CrmLeadDto;
import com.similan.framework.dto.lead.LeadDto;
import com.similan.framework.manager.email.EmailManager;
import com.similan.service.api.CoreServiceErrorCode;
import com.similan.service.api.CrmConnect;
import com.similan.service.api.CrmConnectFactory;
import com.similan.service.api.CrmIntegrationService;
import com.similan.service.api.OrganizationManagementService;
import com.similan.service.api.community.dto.basic.SocialActorContactDto;
import com.similan.service.exception.CoreServiceException;

@Slf4j
public class CrmIntegrationServiceImpl implements CrmIntegrationService {
  @Autowired
	private CrmConnectFactory crmConnectFactory;
	@Autowired
	private CrmLeadSettingsRepository crmLeadSettingsRepository;
	@Autowired
	private SocialOrganizationRepository socialOrganizationRepository;
	@Autowired
	private CrmLeadSyncRecordRepository crmLeadSyncRecordRepository;
	@Autowired
	private EmailManager emailManager;
	@Autowired
	private OrganizationManagementService organizationService;
	
	@Setter @Getter
	private String sender = null;
	@Setter @Getter
	private String syncSuccessfulTemplate;
	@Setter @Getter
	private String syncFailedTemplate;

    private final CopyOnWriteArraySet<Long> activeSynchronizationOrganizationIds = new CopyOnWriteArraySet<Long>();


	public CrmConnectFactory getCrmConnectFactory() {
		return crmConnectFactory;
	}

	public void setCrmConnectFactory(CrmConnectFactory crmConnectFactory) {
		this.crmConnectFactory = crmConnectFactory;
	}
	
	public boolean isSynchronizationInProcess(SocialActorContactDto contact) {
		/*OrganizationDetailInfoDto organizationInfo = null;
		if (contact instanceof MemberInfoDto) {
			MemberInfoDto memberInfo = (MemberInfoDto) contact;
			OrganizationBasicInfoDto employer = memberInfo.getEmployer();
			try {
				organizationInfo = this.organizationService
						.getOrganization(employer.getId());
			} catch (Exception exception) {
			}
		} else if (contact instanceof OrganizationDetailInfoDto) {
			organizationInfo = (OrganizationDetailInfoDto) contact;
		}
		
		return activeSynchronizationOrganizationIds.contains(organizationInfo.getId());*/
	  return false;
	}

	public void startLeadSync(SocialActorContactDto contact) throws Exception {
		/*OrganizationDetailInfoDto organizationInfo = null;
		String memberEmail = null;
		String organizationEmail = null;
		Map<String, Object> arguments = new HashMap<String, Object>();
		if (contact instanceof MemberInfoDto) {
			MemberInfoDto memberInfo = (MemberInfoDto) contact;
			OrganizationBasicInfoDto employer = memberInfo.getEmployer();
			try {
				organizationInfo = this.organizationService
						.getOrganization(employer.getId());
			} catch (Exception exception) {
			}
			memberEmail = memberInfo.getEmail();
		} else if (contact instanceof OrganizationDetailInfoDto) {
			organizationInfo = (OrganizationDetailInfoDto) contact;
			memberEmail = organizationInfo.getEmail();
		}
 
		try {
			if(activeSynchronizationOrganizationIds.add(organizationInfo.getId()) == false) { 	
				log.warn("Attempt to perform CRM synchronization for organization with ID "
						+ organizationInfo.getId()
						+ " when an synchronization is already in progress.");
				return;
			}

			Object fullName = organizationInfo.getFullName();
			arguments.put("fullName", fullName);

			organizationEmail = organizationInfo.getEmail();
			log.info("Starting synchronizationvfor  "
					+ organizationInfo.getId());
			SocialOrganization organization = socialOrganizationRepository
					.findOne(organizationInfo.getId());
			if (organization == null)
				throw new CoreServiceException(
						CoreServiceErrorCode.ORGANIZATION_ID_NOT_FOUND);

			CrmLeadSettings crmLeadSettings = crmLeadSettingsRepository
					.getByOwner(organization);
			if (crmLeadSettings == null)
				throw new CoreServiceException(
						CoreServiceErrorCode.CRM_LEAD_SYNC_SETTINGS_NOT_FOUND);

			log.info("Getting CrmConnect from factory with type "
					+ crmLeadSettings.getCrmSystemType());
			CrmConnect crmConnect = crmConnectFactory
					.getCrmConnect(crmLeadSettings);

			if (crmConnect == null) {
				log.error("No CrmConnect found from factory for system type "
						+ crmLeadSettings.getCrmSystemType());
				throw new CoreServiceException(
						CoreServiceErrorCode.CRM_CONNECT_NOT_FOUND);
			}

			log.info("Found CrmConnect from factory for "
					+ crmLeadSettings.getCrmSystemType());

			log.info("Perorming sync");

			CrmLeadSyncRecord crmLeadSyncRecord = crmConnect
					.performLeadSync(crmLeadSettings);

			log.info("Sync complete with results " + crmLeadSyncRecord);

			log.info("Saving sync record");
			crmLeadSyncRecordRepository.save(crmLeadSyncRecord);

			arguments.put("systemType", crmLeadSettings.getCrmSystemType()
					.toString());

			if (crmLeadSyncRecord.getNumberOfNewLeadsFromCrm() != null)
				arguments.put("numberOfNewLeadsFromCrm", Long
						.toString(crmLeadSyncRecord
								.getNumberOfNewLeadsFromCrm()));
			else
				arguments.put("numberOfNewLeadsFromCrm", 0l);

			if (crmLeadSyncRecord.getNumberOfNewLeadsFromBr() != null)
				arguments.put("numberOfNewLeadsFromBr",
						Long.toString(crmLeadSyncRecord
								.getNumberOfNewLeadsFromBr()));
			else
				arguments.put("numberOfNewLeadsFromBr", 0l);

			if (crmLeadSyncRecord.getNumberOfUpdatesFromBr() != null)
				arguments
						.put("numberOfUpdatesFromBr", Long
								.toString(crmLeadSyncRecord
										.getNumberOfUpdatesFromBr()));
			else
				arguments.put("numberOfUpdatesFromBr", 0l);

			if (crmLeadSyncRecord.getNumberOfUpdatesFromCrm() != null)
				arguments.put("numberOfUpdatesFromCrm",
						Long.toString(crmLeadSyncRecord
								.getNumberOfUpdatesFromCrm()));
			else
				arguments.put("numberOfUpdatesFromCrm", 0l);

			try {
				emailManager.send(sender,
						getTargetEmail(memberEmail, organizationEmail),
						syncSuccessfulTemplate,
						arguments);
			} catch (Exception exception) {
				log.warn(
						"Unable to send result email to "
								+ getTargetEmail(memberEmail, organizationEmail),
						exception);
			}
			log.info("Sync completed successfuly");
		} catch (CoreServiceException coreServiceException) {
			if (coreServiceException.getInnerException() == null)
				arguments.put("exception", coreServiceException.getMessage());
			else
				arguments.put("exception", coreServiceException
						.getInnerException().getMessage());

			try {
				emailManager.send(sender,
						getTargetEmail(memberEmail, organizationEmail),
						syncFailedTemplate, arguments);
			} catch (Exception exception) {
				log.warn(
						"Unable to send result email to "
								+ getTargetEmail(memberEmail, organizationEmail),
						exception);
			}
		} finally {
			activeSynchronizationOrganizationIds.remove(organizationInfo.getId());
		}*/
	}

	public CrmLeadDto getRemoteLeadDtoForLead(LeadDto leadDto) {
		Long orgId = leadDto.getForSocialActorId();
		SocialOrganization organization = this.socialOrganizationRepository
				.findOne(orgId);

		if (organization == null)
			throw new CoreServiceException(
					CoreServiceErrorCode.ORGANIZATION_ID_NOT_FOUND);

		CrmLeadSettings crmLeadSettings = this.crmLeadSettingsRepository
				.getByOwner(organization);

		if (crmLeadSettings == null)
			return null;

		CrmConnect crmConnect = crmConnectFactory
				.getCrmConnect(crmLeadSettings);

		if (crmConnect == null) {
			log.error("No CrmConnect found from factory for system type "
					+ crmLeadSettings.getCrmSystemType());
			throw new CoreServiceException(
					CoreServiceErrorCode.CRM_CONNECT_NOT_FOUND);
		}

		log.info("Found CrmConnect from factory for "
				+ crmLeadSettings.getCrmSystemType());

		log.info("Getting remote Dto for lead");

		return crmConnect.getRemoteLeadDtoForLead(crmLeadSettings, leadDto);

	}

	private String getTargetEmail(String memberEmail, String organizationEmail) {
		String emailAddresses = null;
		if (memberEmail == null && organizationEmail == null) {
			emailAddresses = null;
		} else {
			if (memberEmail == null) {
				emailAddresses = organizationEmail;
			} else {
				emailAddresses = memberEmail;
			}
		}
		return emailAddresses;
	}

	public void synchronizeAll() {
		List<CrmLeadSettings> crmLeadSettings = this.crmLeadSettingsRepository
				.findAll();

		for (CrmLeadSettings crmLeadSetting : crmLeadSettings) {
			try {
				SocialOrganization organization = crmLeadSetting.getOwner();
				OrganizationDetailInfoDto orgInfo = this.organizationService
						.getOrganization(organization.getId());
				//startLeadSync(orgInfo);
			} catch (Exception e) {
				log.error("Unable to execute scheduled CRM sync for organization ID: "
						+ crmLeadSetting.getId());
			}
		}
	}

}
