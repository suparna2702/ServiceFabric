package com.similan.portal.service;

import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.acccount.BusinessAccountType;
import com.similan.domain.entity.community.OrganizationType;
import com.similan.domain.entity.community.SocialMemberVisibilityType;
import com.similan.domain.entity.community.SocialOrganization;
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
import com.similan.framework.dto.review.ReviewCommentDto;
import com.similan.framework.dto.review.ReviewCommentTagDto;
import com.similan.portal.service.listener.ReviewCommentEvent;
import com.similan.portal.service.listener.ReviewCommentTagEvent;
import com.similan.service.api.community.SocialActorBusinessService;
import com.similan.service.api.community.SocialActorService;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.impl.CustomerEngagementAnalyticsServiceImpl;
import com.similan.service.impl.DomainSearchServiceImpl;
import com.similan.service.impl.OrganizationManagementServiceImpl;
import com.similan.service.impl.PartnerManagementServiceImpl;

@Service("orgService")
@Slf4j
public class OrganizationServiceImpl extends BaseService implements
    OrganizationService {

  private static final long serialVersionUID = 1L;

  /**
   * Spring event publisher.
   */
  @Autowired
  private ApplicationEventPublisher eventPublisher;

  @Autowired
  private OrganizationManagementServiceImpl orgService;

  @Autowired
  private DomainSearchServiceImpl searchService;

  @Autowired
  private PartnerManagementServiceImpl partnerManagementService;

  @Autowired
  CustomerEngagementAnalyticsServiceImpl customerEngagementAnalyticsService;

  @Autowired
  SocialActorBusinessService socialActorBusinessService;

  @Autowired
  private SocialActorService socialActorService;

  public CustomerEngagementAnalyticsServiceImpl getCustomerEngagementAnalyticsService() {
    return customerEngagementAnalyticsService;
  }

  public void setCustomerEngagementAnalyticsService(
      CustomerEngagementAnalyticsServiceImpl customerEngagementAnalyticsService) {
    this.customerEngagementAnalyticsService = customerEngagementAnalyticsService;
  }

  public OrganizationServiceImpl() {
    log.info("Organization service created");
  }

  public DomainSearchServiceImpl getSearchService() {
    return searchService;
  }

  public void setSearchService(DomainSearchServiceImpl searchService) {
    this.searchService = searchService;
  }

  public ApplicationEventPublisher getEventPublisher() {
    return eventPublisher;
  }

  public void setEventPublisher(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  public OrganizationManagementServiceImpl getOrgService() {
    return orgService;
  }

  public void setOrgService(OrganizationManagementServiceImpl orgService) {
    this.orgService = orgService;
  }

  public PartnerManagementServiceImpl getPartnerManagementService() {
    return partnerManagementService;
  }

  public void setPartnerManagementService(
      PartnerManagementServiceImpl partnerManagementService) {
    this.partnerManagementService = partnerManagementService;
  }

  @Transactional
  public void updateOrganization(OrganizationDetailInfoDto orgInfo)
      throws Exception {
    orgService.updateOrganization(orgInfo);
  }

  @Transactional
  public OrganizationDetailInfoDto getOrganization(long id) throws Exception {
    OrganizationDetailInfoDto orgInfo = orgService.getOrganization(id);

    return orgInfo;
  }

  @Transactional
  public List<OrganizationBasicInfoDto> getVisibleBusinesses() {

    return orgService
        .getBusinessByVisibility(SocialMemberVisibilityType.VisiblePublic);
  }

  @Transactional
  public PartnershipDto getPartnershipDtoById(Long id) {
    return this.partnerManagementService.getPartnershipDtoById(id);
  }

  @Transactional
  public OrganizationBasicInfoDto getBasicOrganizationInfo(long orgId) {
    return this.orgService.getBasicOrganizationInfo(orgId);
  }

  @Transactional
  public OrganizationDetailInfoDto createOrganization() {

    OrganizationDetailInfoDto orgInfo = new OrganizationDetailInfoDto();
    try {

      SocialOrganization org = orgService.createOrganization();
      orgInfo.setId(org.getId());
    } catch (Exception exp) {
      exp.printStackTrace();
    }

    return orgInfo;
  }

  @Transactional
  public boolean associateOrganizationToMember(MemberInfoDto member,
      OrganizationBasicInfoDto selectedOrgTag) {

    boolean retValue = orgService.associateOrganizationToMember(member,
        selectedOrgTag);
    return retValue;
  }

  @Transactional
  public OrganizationDetailInfoDto getOrgInfoFromOrgTag(
      OrganizationBasicInfoDto orgTag) throws Exception {

    OrganizationDetailInfoDto orgInfo = orgService.getOrganization(orgTag
        .getId());
    return orgInfo;
  }

  @Transactional
  public Long savePartnerProgram(PartnerProgramDefinitionDto partnerProgram) {
    return this.partnerManagementService.savePartnerProgram(partnerProgram);
  }

  @Transactional
  public void participatePartnerProgram(PartnerProgramDefinitionDto program) {

  }

  @Transactional
  public PartnerProgramDefinitionDto getPartnerProgramById(Long id) {
    return this.partnerManagementService.getPartnerProgramById(id);
  }

  @Transactional
  public List<PartnerProgramDefinitionDto> getPartnerPrograms() {
    return this.partnerManagementService.getPartnerPrograms();
  }

  @Transactional
  public OrganizationDetailInfoDto getOrgById(Long valueOf) throws Exception {
    return orgService.getOrganization(valueOf);
  }

  @Transactional
  public void initiateBusinessInvite(MemberInfoDto inviter, String orgName,
      String inviteeEmail, OrganizationType type) {
    orgService.initiateBusinessInvite(inviter, orgName, inviteeEmail, type);

  }

  @Transactional
  public void businessInviteValidateBusinessAttrs(MemberInfoDto orgAdmin,
      OrganizationDetailInfoDto invitedOrg, String pid) {

    orgService.businessInviteValidateBusinessAttrs(orgAdmin, invitedOrg, pid);

  }

  @Transactional
  public PartnerSettingsConfigurationDto getPartnerSettings(
      MemberInfoDto memberInfo, OrganizationDetailInfoDto orgInfo) {

    return orgService.getPartnerSettings(memberInfo, orgInfo);
  }

  public void addReviewComment(ReviewCommentDto reviewComment) {

    log.info("Adding comments " + reviewComment.getCommentInfo().getComment());

    ReviewCommentEvent reviewCommentEvent = new ReviewCommentEvent(
        reviewComment);
    this.eventPublisher.publishEvent(reviewCommentEvent);
  }

  public void addReviewCommentTag(ReviewCommentTagDto commentTag) {
    log.info("Adding comment tag " + commentTag.getReviewTagType()
        + " for comment " + commentTag.getReviewCommentId());

    ReviewCommentTagEvent tagEvent = new ReviewCommentTagEvent(commentTag);
    this.eventPublisher.publishEvent(tagEvent);

  }

  @Transactional
  public DomainSearchResultSet businessAssociateSearch(Long orgId,
      AdvancedSearchInput advSearchInput) throws Exception {
    return this.searchService.businessAssociateSearch(orgId, advSearchInput);
  }

  @Transactional
  public List<PartnershipDto> getPartnersForProgram(Long programId) {
    return this.partnerManagementService.getPartnersForProgram(programId);
  }

  @Transactional
  public void partnershipApprovalNotification(long msId,
      String processInstanceId, boolean approved, String comment) {
    this.partnerManagementService.partnerProgramApprovalNotification(msId,
        processInstanceId, approved, comment);

  }

  @Transactional
  public void initiatePartnerParticipation(OrganizationDetailInfoDto partOrg,
      MemberInfoDto memberAdminInfo,
      PartnerProgramDefinitionDto selectedPartnerProgram) {
    this.partnerManagementService.participateInPartnerProgram(partOrg,
        selectedPartnerProgram, memberAdminInfo);

  }

  @Transactional
  public void updatePartnerConfiguration(
      PartnerSearchSettingsConfigurationDto partnerSearchConfig)
      throws JAXBException {
    this.getOrgService().updatePartnerConfiguration(partnerSearchConfig);
  }

  @Transactional
  public PartnerSearchSettingsConfigurationDto getPartnerSearchSettings(Long id)
      throws JAXBException {

    return this.getOrgService().getPartnerSearchSettings(id);
  }

  @Transactional
  public PartnerSettingsConfigurationDto generateAllEmbeddedUrls(Long settingsId) {
    return this.getOrgService().generateAllEmbeddedUrls(settingsId);
  }

  @Transactional
  public List<OrganizationAccountRecordDto> getAccountRecords(
      OrganizationDetailInfoDto orgInfo) {
    throw new UnsupportedOperationException();
  }

  @Transactional
  public LeadCaptureWizzardDto getLeadCaptureWizzard(
      OrganizationDetailInfoDto orgInfo) {
    return this.orgService.getLeadCaptureWizzard(orgInfo);
  }

  @Transactional
  public void saveLeadCaptureWizzard(LeadCaptureWizzardDto wizzardDto,
      OrganizationDetailInfoDto orgInfo) {
    this.orgService.saveLeadCaptureWizzard(wizzardDto, orgInfo);
  }

  @Transactional
  public void saveLeadCaptureresponse(LeadCaptureWizzardDto captureWizzard,
      OrganizationDetailInfoDto orgInfo) {
    this.orgService.saveLeadCaptureresponse(captureWizzard, orgInfo);
  }

  @Transactional
  public List<EmbeddedUrlDto> getEmbeddedUrls(OrganizationDetailInfoDto orgInfo) {
    return this.orgService.getEmbeddedUrls(orgInfo);
  }

  @Transactional
  public void deleteLeadCaptureQuestion(OrganizationDetailInfoDto orgInfo,
      LeadCaptureQuestionDto question) {
    this.orgService.deleteLeadCaptureQuestion(orgInfo, question);

  }

  @Transactional
  public void approveConnection(OrganizationBasicInfoDto businessToConnect,
      String processInstId, boolean approved) {

    this.orgService.approveConnection(businessToConnect, processInstId,
        approved);

  }

  @Override
  @Transactional
  public void initiatePartnerInvite(OrganizationBasicInfoDto participatingOrg,
      MemberInfoDto memberInfo, PartnerProgramDefinitionDto partnerProgram) {
    this.partnerManagementService.initiatePartnerInvite(participatingOrg,
        memberInfo, partnerProgram);

  }

  @Transactional
  public void submitPartnerProgramApprovalFormInput(
      PartnerProgramDefinitionDto partnerProg, Long partnerOrgId,
      Long memStateId, Long inviteeId, String processInstanceId) {
    this.partnerManagementService.submitPartnerProgramApprovalFormInput(
        partnerProg, partnerOrgId, memStateId, inviteeId, processInstanceId);

  }

  @Transactional
  public Boolean isPartnerForProgram(Long orgId, Long progId) {
    return this.partnerManagementService.isPartnerForProgram(orgId, progId);
  }

  @Transactional
  public void requestProduct(String productRequestComment,
      MemberInfoDto member, OrganizationDetailInfoDto orgInfo) {
    this.orgService.requestProduct(productRequestComment, member, orgInfo);
  }

  @Transactional
  public Long getOrgIdFromEmbeddedId(String orgEmbeddedIdentity) {
    return this.orgService.getOrgIdFromEmbeddedId(orgEmbeddedIdentity);
  }

  @Transactional
  public Long getOrganizationCountByType(OrganizationType type) {
    return this.orgService.getOrganizationCountByType(type);
  }

  @Transactional
  public void shareComment(OrganizationDetailInfoDto orgInfo,
      String shareComment) {
    this.orgService.shareComment(orgInfo, shareComment);
  }

  @Transactional
  public Map<String, Long> getPartnerSearchEventCountOverTimeperiodByOrg(
      int searchFromPeriod, int searchFrequency, Long businessActorId) {
    return this.customerEngagementAnalyticsService
        .getPartnerSearchEventCountOverTimeperiodByOrg(searchFromPeriod,
            searchFrequency, businessActorId);
  }

  @Transactional
  public Map<String, Long> getPollEventCountOverTimeperiod(int startPeriod,
      int frequency, Long orgId) {
    return this.customerEngagementAnalyticsService
        .getPollEventCountOverTimeperiod(startPeriod, frequency, orgId);
  }

  @Transactional
  public Map<String, Long> getPollSubmissionCountOverTimeperiod(
      int startPeriod, int frequency, Long orgId) {
    return this.customerEngagementAnalyticsService
        .getPollSubmissionCountOverTimeperiod(startPeriod, frequency, orgId);
  }

  @Transactional
  public void follow(MemberInfoDto member, OrganizationDetailInfoDto orgInfo) {
    this.orgService.follow(member, orgInfo);
  }

  @Transactional
  public boolean isMemberFollowingOrg(MemberInfoDto member,
      OrganizationDetailInfoDto orgInfo) {
    return this.orgService.isMemberFollowingOrg(member, orgInfo);
  }

  @Override
  @Transactional
  public OrganizationDetailInfoDto viewBusinessProfile(long orgProfileId,
      SocialActorKey viewerKey) throws Exception {
    OrganizationDetailInfoDto org = getOrganization(orgProfileId);
    SocialActorKey businessProfile = this.socialActorService
        .transitional_getKey(orgProfileId);
    this.socialActorBusinessService.viewBusinessProfile(viewerKey,
        businessProfile);
    return org;
  }

  @Override
  @Transactional
  public List<BusinessAccountType> getAllBusinessAccountTypes() {
    return this.orgService.getAllBusinessAccountTypes();
  }

  @Override
  @Transactional
  public BusinessAccountType getBusinessAccount(Long org) {
    return this.orgService.getBusinessAccount(org);
  }

  @Override
  @Transactional
  public OrganizationBasicInfoDto getBusinessByName(String name) {
    return this.orgService.getBusinessByName(name);
  }

  @Override
  @Transactional
  public OrganizationBasicInfoDto getBusinessByNameAndEmail(String name,
      String email) {
    return this.orgService.getBusinessByNameAndEmail(name, email);
  }

  @Override
  @Transactional
  public void updateBranding(UpdateBusinessBrandingDto branding) {
    this.orgService.updateBranding(branding);

  }

  @Override
  @Transactional
  public List<ExternalBusinessPortalDto> getExternalPortals(Long businessId) {
    return this.orgService.getExternalPortals(businessId);
  }

  @Override
  @Transactional
  public void deleteExternalPortals(List<ExternalBusinessPortalDto> deleteList) {
    this.orgService.deleteExternalPortals(deleteList);

  }

  @Override
  @Transactional
  public List<ExternalBusinessPortalDto> addExternalPortals(Long ownerId,
      List<ExternalBusinessPortalDto> portalList) {
    return this.orgService.addExternalPortals(ownerId, portalList);
  }

  @Override
  @Transactional
  public List<MemberInfoDto> getTeamMembers(Long orgId) {
    return this.orgService.getTeamMembers(orgId);
  }

}
