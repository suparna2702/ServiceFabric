package com.similan.service.api;

import java.util.List;

import javax.xml.bind.JAXBException;

import com.similan.domain.entity.acccount.BusinessAccountType;
import com.similan.domain.entity.community.OrganizationType;
import com.similan.domain.entity.community.SocialMemberVisibilityType;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.framework.dto.NewBusinessDto;
import com.similan.framework.dto.OrganizationBasicInfoDto;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.UpdateBusinessBrandingDto;
import com.similan.framework.dto.account.OrganizationAccountRecordDto;
import com.similan.framework.dto.community.ExternalBusinessPortalDto;
import com.similan.framework.dto.embed.EmbeddedUrlDto;
import com.similan.framework.dto.leadcapture.LeadCaptureQuestionDto;
import com.similan.framework.dto.leadcapture.LeadCaptureWizzardDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.partner.PartnerSearchSettingsConfigurationDto;
import com.similan.framework.dto.partner.PartnerSettingsConfigurationDto;
import com.similan.framework.dto.review.ReviewCommentSummeryDto;

public interface OrganizationManagementService {

  public List<MemberInfoDto> getTeamMembers(Long orgId);

  public List<ExternalBusinessPortalDto> getExternalPortals(Long businessId);

  public void deleteExternalPortals(List<ExternalBusinessPortalDto> deleteList);

  public List<ExternalBusinessPortalDto> addExternalPortals(Long ownerId,
      List<ExternalBusinessPortalDto> portalList);

  public void memberBusinessAssociationNotification(Long memberId,
      String processInstanceId, Boolean status);

  public void updateBranding(UpdateBusinessBrandingDto branding);

  public OrganizationBasicInfoDto getBusinessByName(String name);

  public OrganizationBasicInfoDto getBusinessByNameAndEmail(String name,
      String email);

  public List<OrganizationBasicInfoDto> getBusinessByVisibility(
      SocialMemberVisibilityType visibility);

  public boolean initiateOrganizationCreate(MemberInfoDto memberInfo,
      NewBusinessDto business);

  public List<BusinessAccountType> getAllBusinessAccountTypes();

  public BusinessAccountType upgradeBusinessAccount(Long org,
      BusinessAccountType upgradeAccountTo);

  public BusinessAccountType downgradeBusinessAccount(Long org,
      BusinessAccountType downgradeAccountTo);

  public BusinessAccountType getBusinessAccountByName(String name);

  public BusinessAccountType getBusinessAccount(Long org);

  public OrganizationDetailInfoDto getOrganizationFromEmbeddedIdentity(
      String uuID) throws Exception;

  public OrganizationBasicInfoDto getBasicOrganizationInfo(long orgId);

  public OrganizationDetailInfoDto getOrganization(long orgId) throws Exception;

  public void updateOrganization(OrganizationDetailInfoDto orgInfo)
      throws Exception;

  public SocialOrganization createOrganization();

  public void initiateBusinessInvite(MemberInfoDto inviter, String orgName,
      String inviteeEmail, OrganizationType type);

  public void businessInviteValidateBusinessAttrs(MemberInfoDto orgAdmin,
      OrganizationDetailInfoDto invitedOrg, String pid);

  public void businessInviteBusinessCreation(Long memberId, boolean approved,
      String pid);

  public PartnerSettingsConfigurationDto getPartnerSettings(
      MemberInfoDto memberInfo, OrganizationDetailInfoDto orgInfo);

  public void updatePartnerConfiguration(
      PartnerSearchSettingsConfigurationDto partnerSearchConfig)
      throws JAXBException;

  public PartnerSearchSettingsConfigurationDto getPartnerSearchSettings(Long id)
      throws JAXBException;

  public PartnerSettingsConfigurationDto generateAllEmbeddedUrls(Long settingsId);

  public LeadCaptureWizzardDto getLeadCaptureWizzard(
      OrganizationDetailInfoDto orgInfo);

  public void saveLeadCaptureWizzard(LeadCaptureWizzardDto wizzardDto,
      OrganizationDetailInfoDto orgInfo);

  public List<EmbeddedUrlDto> getEmbeddedUrls(OrganizationDetailInfoDto orgInfo);

  public ReviewCommentSummeryDto getReviewSummary(
      OrganizationDetailInfoDto orgInfo);

  public List<SocialOrganization> findAll();

  public void deleteLeadCaptureQuestion(OrganizationDetailInfoDto orgInfo,
      LeadCaptureQuestionDto question);

  public void approveConnection(OrganizationBasicInfoDto businessToConnect,
      String processInstId, boolean approved);

  public void requestProduct(String productRequestComment,
      MemberInfoDto member, OrganizationDetailInfoDto orgInfo);

  public void shareComment(OrganizationDetailInfoDto orgInfo,
      String shareComment);

  public void follow(MemberInfoDto member, OrganizationDetailInfoDto orgInfo);

  public boolean isMemberFollowingOrg(MemberInfoDto member,
      OrganizationDetailInfoDto orgInfo);

}
