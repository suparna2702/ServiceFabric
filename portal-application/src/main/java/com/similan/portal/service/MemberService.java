package com.similan.portal.service;

import java.util.List;
import java.util.Set;

import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.similan.domain.entity.community.EmployeeRole;
import com.similan.domain.entity.lead.ContactMessageDetail;
import com.similan.framework.dto.AdvancedSearchInput;
import com.similan.framework.dto.CommunityEventDto;
import com.similan.framework.dto.DomainSearchResult;
import com.similan.framework.dto.DomainSearchResultSet;
import com.similan.framework.dto.MemberInviteDto;
import com.similan.framework.dto.MemberInviteListDto;
import com.similan.framework.dto.NewBusinessDto;
import com.similan.framework.dto.NonmemberContactMessageInfoDto;
import com.similan.framework.dto.OrganizationBasicInfoDto;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.SearchItemType;
import com.similan.framework.dto.community.MemberInviteInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.feedback.dto.NewErrorReportingDto;
import com.similan.service.exception.CoreServiceException;

public interface MemberService {

  public Set<EmployeeRole> getMemberRoles(MemberInfoDto member,
      OrganizationDetailInfoDto orgInfo);

  public void reportProblem(NewErrorReportingDto error);

  public void deleteInviteRequest(Long inviteRequestId);

  public void reInviteMember(Long inviteRequestId);

  public List<MemberInviteInfoDto> findPendingInvitesByInviter(
      Long inviterParamId);

  public boolean isPendingBusinessCreateOrAssociateFlow(MemberInfoDto memberInfo);

  boolean memberSignupInitiate(MemberInfoDto memberInfo);

  MemberInviteListDto handleFileUpload(FileUploadEvent event);

  MemberInviteListDto memberBulkUpload(UploadedFile uploadFile);

  boolean memberBulkInvite(MemberInviteListDto inviteList,
      MemberInfoDto memberInfo);

  InviteResponse memberMultiInvite(MemberInfoDto memberInfo);

  MemberInfoDto memberSignupComplete(String processInstanceId);

  void memberUpdate(MemberInfoDto memberInfo) throws Exception;

  DomainSearchResultSet memberSearch(MemberInfoDto memberInfo,
      SearchItemType searchItem, boolean embedded) throws Exception;

  boolean logout(MemberInfoDto memberInfo, String sessionId);

  boolean memberSingleInvite(MemberInviteDto invite, MemberInfoDto memberInfo);

  MemberInfoDto memberLogin(String email, String password)
      throws CoreServiceException;

  MemberInfoDto embeddedLogin(String uuid) throws CoreServiceException;

  void contact(MemberInfoDto info, DomainSearchResult selectedResult);

  void associateOrganization(MemberInfoDto member,
      OrganizationBasicInfoDto selectedOrgTag);

  void changeEmail(MemberInfoDto member) throws CoreServiceException;

  void changeEmailComplete(long memberId, String processInstanceId);

  void changePassword(MemberInfoDto member);

  void createBusinessEntityNotification(Long memberId, String pid,
      boolean approved, String notificationMsg);

  public void initiateOrganizationCreate(MemberInfoDto memberInfo,
      NewBusinessDto business);

  public void memberOrgAssociationNotification(Long memberId, String pid,
      boolean approved);

  public void addMemberFeedback(Long memberId, String memberFeedback);

  public void contactUs(NonmemberContactMessageInfoDto message);

  public MemberInfoDto getMemberById(Long valueOf) throws Exception;

  public MemberInfoDto viewMemberPublicProfile(Long memberViewed,
      SocialActorKey memberVieweing) throws Exception;

  public DomainSearchResultSet advancedSearch(MemberInfoDto searchInfo,
      AdvancedSearchInput advSearchInput) throws Exception;

  public void sendContactMessageToOrganization(ContactMessageDetail message,
      MemberInfoDto loggedMember, OrganizationDetailInfoDto orgToSend)
      throws ResourceNotFoundException, ParseErrorException, Exception;

  public void sendContactMessageToMember(ContactMessageDetail message,
      MemberInfoDto loggedMember, MemberInfoDto memberToSend)
      throws ResourceNotFoundException, ParseErrorException, Exception;

  public void createClickThroughLeadForMember(MemberInfoDto loggedMember,
      MemberInfoDto memberToSend);

  public void createClickThroughLeadForOrganization(MemberInfoDto loggedMember,
      OrganizationDetailInfoDto orgToSend);

  public List<CommunityEventDto> getLoginInfoList(Long id);

  public void forgotPasswordInitiate(String forgotPasswordEmail)
      throws CoreServiceException;

  public MemberInfoDto processForgotPasswordLink(Long memberId,
      String processInstanceId);

  public void changeForgottenPasswordComplete(MemberInfoDto member, String pid);

  public void memberInviteSignupInput(String pid, MemberInfoDto member);

  public void existingMemberAcceptInviteDecision(long id,
      String processInstanceId);

  public void existingMemberRejectInviteDecision(long id,
      String processInstanceId);

  public MemberInfoDto isMemberWithEmailExists(String email);

  public void shareComment(MemberInfoDto member, String shareComment);
}