package com.similan.service.api;

import java.util.List;
import java.util.Map;

import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.similan.domain.entity.lead.LeadType;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.lead.AffiliateLeadListDto;
import com.similan.framework.dto.lead.CrmLeadSettingsDto;
import com.similan.framework.dto.lead.LeadActivityDto;
import com.similan.framework.dto.lead.LeadAlertSettingsDto;
import com.similan.framework.dto.lead.LeadAssignmentDto;
import com.similan.framework.dto.lead.LeadDto;
import com.similan.framework.dto.lead.LeadMessageDto;
import com.similan.framework.dto.lead.LeadNoteDto;
import com.similan.framework.dto.lead.LeadSearchFilterSettingDto;
import com.similan.framework.dto.lead.LeadTransferStatusDto;
import com.similan.framework.dto.member.MemberInfoDto;

public interface LeadManagementService {
	
	public void storeLead(LeadDto lead) throws Exception;
	
	public List<LeadDto> getAllMemberLeads(Long memberId) throws Exception;
	
	public List<LeadDto> getAllOrganizationLeads(Long orgId) throws Exception;
	
	public Map<LeadType, Long> getLeadCountStatSocialActor(long actorId);
	
	//public void assignLeads(LeadDto[] leads, Contact[] contacts, MemberInfoDto member);
	
	public List<LeadAssignmentDto> getAllLeadAssignmentsForSocialActor(Long socialActorId) throws Exception;
	
	public LeadDto getLead(Long id, LeadType leadType) throws Exception;
	
	public void saveLeadAlertSettings(MemberInfoDto memberInfo, 
            LeadAlertSettingsDto leadAlertSettingsDto);
	
	public LeadAlertSettingsDto getLeadAlertSettings(MemberInfoDto memberInfo);
	
	public List<LeadSearchFilterSettingDto> getLeadSearchFilterSettings(MemberInfoDto memberInfo)  throws Exception;
	
	public void saveSearchFilterSetting(MemberInfoDto memberInfo, 
            LeadSearchFilterSettingDto filterSettingsDto) throws Exception;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public LeadSearchFilterSettingDto getLeadSearchFilterSettingsById(Long id) throws Exception;
	
	public void saveCrmSettings(OrganizationDetailInfoDto orgInfo, CrmLeadSettingsDto crmSettingsDto) throws Exception;
	
	public CrmLeadSettingsDto getCrmSettings(OrganizationDetailInfoDto orgInfo) throws Exception;
	
	public void addLeadNote(LeadNoteDto leadNote, LeadDto lead, MemberInfoDto commenter);
	
	public void addLeadActivity(LeadActivityDto leadActivity, LeadDto lead);
	
	public List<LeadNoteDto> getLeadComments(LeadDto leadDto);
	
	public List<LeadActivityDto> getLeadActivity(LeadDto leadDto);
	
	public List<LeadActivityDto> getLeadActivityByOrg(OrganizationDetailInfoDto orgInfo);
	
	public void saveImportedLeads(List<LeadDto> leadList, OrganizationDetailInfoDto orgInfo) throws Exception;
	
	public void sendLeadMessage(LeadMessageDto leadMessage, LeadDto lead, MemberInfoDto commenter)throws ResourceNotFoundException, ParseErrorException, Exception;
	
	public List<LeadMessageDto> getLeadSentMessage(LeadDto lead);
	
	public void saveAffiliateLeadList(AffiliateLeadListDto uploadLeadList) throws Exception;

	public List<AffiliateLeadListDto> getAffiliateLeadListForBusiness(
			OrganizationDetailInfoDto orgInfo);

	public AffiliateLeadListDto getAffiliateLeadListDetail(Long listId) throws Exception;
	
	public List<LeadDto> getAffiliateLeadsByFilter(Long filterId) throws Exception;
	
	public void purchaseAffilateLead(OrganizationDetailInfoDto orgInfo, LeadDto affLeadDto);
	
	public void saveLead(LeadDto lead, OrganizationDetailInfoDto orgInfo) throws Exception;

	public void saveLead(LeadDto lead, OrganizationDetailInfoDto orgInfo, boolean overrideTimestamp) throws Exception;

	public void approveLeadTransfer(Long leadId, Long memberId);
	
	public void rejectLeadTransfer(Long leadId, Long memberId);

	/**
	 * @param id
	 * @param activeLead
	 * @param availableLead
	 * @param transferredLead
	 * @param searchLead
	 * @param unreadOnly
	 * @return
	 */
	public List<LeadDto> getMemberLeads(Long id, LeadType[] includeTypes,
			boolean includeUnreadOnly) throws Exception;

	/**
	 * @param id
	 * @return
	 */
	public int getCountNewLeads(Long id);

	/**
	 * @param id
	 * @return
	 */
	public int getCountAvailableAffiliateLeads(Long id);

	public void deleteLeadNote(LeadNoteDto noteTobeDeleted);
	
	public void deleteLeads(LeadDto[] leadsToDelete);
	
	public void deleteLead(LeadDto leadDelete);

	/**
	 * @param lead
	 * @return
	 */
	public List<LeadTransferStatusDto> getLeadTransferStatus(LeadDto lead);

	/**
	 * @param leadNoteDto
	 */
	public void updateLeadNote(LeadNoteDto leadNoteDto);

	/**
	 * @param contact
	 */
	//public void performCrmSync(Contact contact);
	
	/**
	 * @param leadDto
	 * @return
	 * @throws Exception
	 */
	public List<LeadDto> getTransferRelatedLeads(LeadDto leadDto) throws Exception;

	/**
	 * @param localActivity
	 */
	public void updateLeadActivity(LeadActivityDto localActivity);

	/**
	 * @param activity
	 */
	public void deleteLeadActivity(LeadActivityDto activity);
}

