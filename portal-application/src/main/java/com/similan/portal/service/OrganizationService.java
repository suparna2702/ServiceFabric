package com.similan.portal.service;

import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import com.similan.domain.entity.acccount.BusinessAccountType;
import com.similan.domain.entity.community.OrganizationType;
import com.similan.framework.dto.AdvancedSearchInput;
import com.similan.framework.dto.DomainSearchResultSet;
import com.similan.framework.dto.OrganizationBasicInfoDto;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.UpdateBusinessBrandingDto;
import com.similan.framework.dto.account.OrganizationAccountRecordDto;
import com.similan.framework.dto.community.ExternalBusinessPortalDto;
import com.similan.framework.dto.embed.EmbeddedUrlDto;
import com.similan.framework.dto.leadcapture.LeadCaptureQuestionDto;
import com.similan.framework.dto.leadcapture.LeadCaptureWizzardDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.partner.PartnerProgramDefinitionDto;
import com.similan.framework.dto.partner.PartnerSearchSettingsConfigurationDto;
import com.similan.framework.dto.partner.PartnerSettingsConfigurationDto;
import com.similan.framework.dto.partner.PartnershipDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

public interface OrganizationService {
  
   public List<MemberInfoDto> getTeamMembers(Long orgId);

   public List<ExternalBusinessPortalDto> getExternalPortals(Long businessId);

   public void deleteExternalPortals(List<ExternalBusinessPortalDto> deleteList);

   public List<ExternalBusinessPortalDto> addExternalPortals(Long ownerId,
      List<ExternalBusinessPortalDto> portalList);

   public void updateBranding(UpdateBusinessBrandingDto branding);

   public OrganizationBasicInfoDto getBusinessByName(String name);

   public OrganizationBasicInfoDto getBusinessByNameAndEmail(String name,
      String email);

   public List<BusinessAccountType> getAllBusinessAccountTypes();

   public BusinessAccountType getBusinessAccount(Long org);

   public OrganizationBasicInfoDto getBasicOrganizationInfo(long orgId);

   public PartnershipDto getPartnershipDtoById(Long id);

   public PartnerProgramDefinitionDto getPartnerProgramById(Long id);

  /**
   * 
   * @param orgInfo
   * @throws Exception
   */
  public void updateOrganization(OrganizationDetailInfoDto orgInfo)
      throws Exception;

  /**
   * 
   * @param id
   * @return
   * @throws Exception
   */
  public OrganizationDetailInfoDto getOrganization(long id) throws Exception;

   public OrganizationDetailInfoDto viewBusinessProfile(long orgProfileId,
      SocialActorKey viewerKey) throws Exception;

   public OrganizationDetailInfoDto createOrganization();

   public List<OrganizationBasicInfoDto> getVisibleBusinesses();

   public boolean associateOrganizationToMember(MemberInfoDto member,
      OrganizationBasicInfoDto selectedOrgTag);

  /**
   * 
   * @param orgTag
   * @return
   * @throws Exception
   */
  public OrganizationDetailInfoDto getOrgInfoFromOrgTag(
      OrganizationBasicInfoDto orgTag) throws Exception;

   public Long savePartnerProgram(PartnerProgramDefinitionDto partnerProgram);

   public void participatePartnerProgram(PartnerProgramDefinitionDto program);

   public List<PartnerProgramDefinitionDto> getPartnerPrograms();

  /**
   * 
   * @param valueOf
   * @return
   * @throws Exception
   * @throws Exception
   */
  public OrganizationDetailInfoDto getOrgById(Long valueOf) throws Exception;

   public void initiateBusinessInvite(MemberInfoDto inviter, String orgName,
      String inviteeEmail, OrganizationType type);

   public void businessInviteValidateBusinessAttrs(MemberInfoDto orgAdmin,
      OrganizationDetailInfoDto invitedOrg, String pid);

    public PartnerSettingsConfigurationDto getPartnerSettings(
      MemberInfoDto memberInfo, OrganizationDetailInfoDto orgInfo);

   public DomainSearchResultSet businessAssociateSearch(Long orgId,
      AdvancedSearchInput advSearchInput) throws Exception;

   public List<PartnershipDto> getPartnersForProgram(Long programId);

   public Boolean isPartnerForProgram(Long orgId, Long progId);

  /**
   * @param msId
   * @param processInstanceId
   * @param approved
   * @param comment
   */
  public void partnershipApprovalNotification(long msId,
      String processInstanceId, boolean approved, String comment);

   public void initiatePartnerParticipation(OrganizationDetailInfoDto partOrg,
      MemberInfoDto memberAdminInfo,
      PartnerProgramDefinitionDto selectedPartnerProgram);

   public void updatePartnerConfiguration(
      PartnerSearchSettingsConfigurationDto partnerSearchConfig)
      throws JAXBException;

   public PartnerSearchSettingsConfigurationDto getPartnerSearchSettings(Long id)
      throws JAXBException;

   public PartnerSettingsConfigurationDto generateAllEmbeddedUrls(Long settingsId);

   public List<OrganizationAccountRecordDto> getAccountRecords(
      OrganizationDetailInfoDto orgInfo);

   public LeadCaptureWizzardDto getLeadCaptureWizzard(
      OrganizationDetailInfoDto orgInfo);

   public void saveLeadCaptureWizzard(LeadCaptureWizzardDto wizzardDto,
      OrganizationDetailInfoDto orgInfo);

   public void saveLeadCaptureresponse(LeadCaptureWizzardDto captureWizzard,
      OrganizationDetailInfoDto orgInfo);

   public List<EmbeddedUrlDto> getEmbeddedUrls(OrganizationDetailInfoDto orgInfo);

   public void deleteLeadCaptureQuestion(OrganizationDetailInfoDto orgInfo,
      LeadCaptureQuestionDto question);

   public void approveConnection(OrganizationBasicInfoDto businessToConnect,
      String processInstanceId, boolean approved);

   public void initiatePartnerInvite(OrganizationBasicInfoDto participatingOrg,
      MemberInfoDto memberInfo, PartnerProgramDefinitionDto partnerProgram);

  /**
   * @param partnerProg
   * @param partnerOrgId
   * @param memStateId
   * @param inviteeId
   * @param processInstanceId
   */
  public void submitPartnerProgramApprovalFormInput(
      PartnerProgramDefinitionDto partnerProg, Long partnerOrgId,
      Long memStateId, Long inviteeId, String processInstanceId);

  /**
   * @param productRequestComment
   * @param member
   * @param orgInfo
   */
  public void requestProduct(String productRequestComment,
      MemberInfoDto member, OrganizationDetailInfoDto orgInfo);

   public Long getOrgIdFromEmbeddedId(String orgEmbeddedIdentity);

   public Long getOrganizationCountByType(OrganizationType type);

   public Map<String, Long> getPartnerSearchEventCountOverTimeperiodByOrg(
      int searchFromPeriod, int searchFrequency, Long businessActorId);

   public Map<String, Long> getPollEventCountOverTimeperiod(int startPeriod,
      int frequency, Long orgId);

   public Map<String, Long> getPollSubmissionCountOverTimeperiod(
      int startPeriod, int frequency, Long orgId);

  /**
   * @param member
   * @param orgInfo
   */
  public void follow(MemberInfoDto member, OrganizationDetailInfoDto orgInfo);

  /**
   * @param member
   * @param orgInfo
   * @return
   */
  public boolean isMemberFollowingOrg(MemberInfoDto member,
      OrganizationDetailInfoDto orgInfo);

}
