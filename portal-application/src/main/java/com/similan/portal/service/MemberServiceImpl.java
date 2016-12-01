package com.similan.portal.service;

import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.community.EmployeeRole;
import com.similan.domain.entity.community.MemberInviteType;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.event.LoginInfo;
import com.similan.domain.entity.event.LoginModeType;
import com.similan.domain.entity.lead.ContactMessageDetail;
import com.similan.domain.entity.lead.LeadType;
import com.similan.domain.entity.util.AddressDto;
import com.similan.domain.entity.util.AttributeConstantUtil;
import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.entity.util.WorkflowTransientStateAttribute;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.domain.repository.util.WorkflowTransientStateRepository;
import com.similan.framework.dto.AdvancedSearchInput;
import com.similan.framework.dto.CommunityEventDto;
import com.similan.framework.dto.CommunityEventType;
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
import com.similan.framework.dto.events.CommunitySpringEvent;
import com.similan.framework.dto.lead.LeadDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.importexport.csv.BeanImportingException;
import com.similan.framework.util.FaceletUtils;
import com.similan.portal.service.listener.LeadEvent;
import com.similan.portal.service.listener.MemberInviteEvent;
import com.similan.service.api.MemberManagementService;
import com.similan.service.api.OrganizationManagementService;
import com.similan.service.api.community.SocialActorMemberService;
import com.similan.service.api.community.SocialActorService;
import com.similan.service.api.community.dto.basic.MemberDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.community.dto.operation.MemberSignupDto;
import com.similan.service.api.feedback.dto.NewErrorReportingDto;
import com.similan.service.exception.ContactAlreadyExistsException;
import com.similan.service.exception.CoreServiceException;
import com.similan.service.impl.DomainSearchServiceImpl;
import com.similan.service.impl.MessagingServiceImpl;
import com.similan.service.impl.OrganizationManagementServiceImpl;

@Service("memberService")
@Slf4j
public class MemberServiceImpl extends BaseService implements MemberService {

  private static final long serialVersionUID = 1L;

  /**
   * Spring event publisher.
   */
  @Autowired
  private ApplicationEventPublisher eventPublisher;

  @Autowired
  private MemberManagementService accountManagementService;

  @Autowired
  private MessagingServiceImpl messagingService;

  @Autowired
  private OrganizationManagementService businessManagementService;

  @Autowired
  private DomainSearchServiceImpl searchService;

  @Autowired
  private SocialActorMemberService socialActorMemberService;

  @Autowired
  private SocialActorService socialActorService;
  
  @Autowired
  private SocialActorMemberService memberService;

  public MemberServiceImpl() {
    log.info("memsvr created");
  }

  @Transactional
  public Set<EmployeeRole> getMemberRoles(MemberInfoDto member,
      OrganizationDetailInfoDto orgInfo) {
    return accountManagementService.getMemberRoles(member, orgInfo);
  }

  /**
   * 1. Check it is embedded mode or inCommunity mode 2. If iCommunity mode then
   * use MemberInfo email to send 3. If embedded use the given email in
   * ContactLeadDto 4.
   * 
   * @param message
   * @param loggedMember
   */
  @Transactional
  public void sendContactMessageToMember(ContactMessageDetail message,
      MemberInfoDto loggedMember, MemberInfoDto memberToSend)
      throws ResourceNotFoundException, ParseErrorException, Exception {

    /*
     * catching the exception because we want to keep a record of the contact
     * even in case where geo coding service is not working
     */
    try {
      this.messagingService.sendContactMessageToMember(message, loggedMember,
          memberToSend);
    } catch (Exception exp) {
      log.error("Geo coding error ", exp);
    }

    LeadDto leadDto = this.getLeadDtoForMember(loggedMember, memberToSend);
    leadDto.setContact(message);
    leadDto.setLeadType(LeadType.ContactLead);

    LeadEvent event = new LeadEvent(leadDto);
    this.eventPublisher.publishEvent(event);

  }

  @Transactional
  public void createClickThroughLeadForMember(MemberInfoDto loggedMember,
      MemberInfoDto memberToSend) {

    LeadDto leadDto = this.getLeadDtoForMember(loggedMember, memberToSend);
    leadDto.setLeadType(LeadType.ClickThroughLead);
    LeadEvent event = new LeadEvent(leadDto);
    this.eventPublisher.publishEvent(event);
  }

  private LeadDto getLeadDtoForMember(MemberInfoDto loggedMember,
      MemberInfoDto memberToSend) {

    LeadDto leadDto = new LeadDto();
    leadDto.setForSocialActorId(memberToSend.getId());
    leadDto.setFromSocialActorId(loggedMember.getId());
    leadDto.setContactEmail(loggedMember.getEmail());
    leadDto.setContactPhone(loggedMember.getMobilePhone());
    leadDto.setName(loggedMember.getFullName());
    leadDto.setFirstName(loggedMember.getFirstName());
    leadDto.setLastName(loggedMember.getLastName());
    leadDto.setLeadPhoto(loggedMember.getPhotoLocation());

    AddressDto addrDto = new AddressDto();
    addrDto.setCountry(loggedMember.getCountry());
    addrDto.setZipCode(loggedMember.getZipCode());
    addrDto.setStreet(loggedMember.getStreet());
    addrDto.setCity(loggedMember.getCity());
    leadDto.setLocation(addrDto);

    return leadDto;

  }

  /**
   * 
   * @param memberInfo
   *          This method initiates the sign up process
   */
  @Transactional
  public boolean memberSignupInitiate(MemberInfoDto memberInfo) {
    log.info(" Member first name in memberSignupInitiate "
        + memberInfo.getFirstName());

    if (memberInfo != null) {

      this.memberService.signUp(new MemberSignupDto(memberInfo.getFirstName(),
          memberInfo.getLastName(), memberInfo.getUserName(),
          memberInfo.getPassword(), memberInfo.getEmail()));
    }

    return true;
  }

  public MessagingServiceImpl getMessagingService() {
    return messagingService;
  }

  public void setMessagingService(MessagingServiceImpl messagingService) {
    this.messagingService = messagingService;
  }

  public DomainSearchServiceImpl getSearchService() {
    return searchService;
  }

  public void setSearchService(DomainSearchServiceImpl searchService) {
    this.searchService = searchService;
  }

  public MemberManagementService getAccountManagementService() {
    return accountManagementService;
  }

  public void setAccountManagementService(
      MemberManagementService accountManagementService) {
    this.accountManagementService = accountManagementService;
  }

  public MemberInviteListDto handleFileUpload(FileUploadEvent event) {

    if (event != null) {

      log.info("Uploaded file name " + event.getFile().getFileName());
      try {
        InputStreamReader streamReader = new InputStreamReader(event.getFile()
            .getInputstream());
        MemberInviteListDto memberInviteList = this.accountManagementService
            .inviteListFromStream(streamReader);

        log.info("Number of members uploaded "
            + memberInviteList.getMemberInvites().size());
        return memberInviteList;
      } catch (BeanImportingException bie) {
        throw bie;
      } catch (Exception exp) {
        log.error("Import error in bulk invite ", exp);
      }
    }

    return null;

  }

  @Transactional
  public MemberInviteListDto memberBulkUpload(UploadedFile uploadFile) {

    MemberInviteListDto inviteeList = new MemberInviteListDto();
    log.info("memberBulkUpload " + uploadFile);

    return inviteeList;
  }

  @Transactional
  public boolean memberBulkInvite(MemberInviteListDto inviteList,
      MemberInfoDto memberInfo) {
    if (inviteList == null || inviteList.getMemberInvites() == null) {
      log.warn("Received null invite list, aborting...");
      return false;
    }
    log.info("Invite list received in Bulk Invite "
        + inviteList.getMemberInvites().size());

    int total = 0;
    List<MemberInviteDto> invites = inviteList.getMemberInvites();
    for (MemberInviteDto invite : invites) {
      try {
        doInvite(memberInfo, invite);
      } catch (ContactAlreadyExistsException contactAlreadyExistsException) {
        // Just skip existing contacts
      }
      total++;
    }
    log.info("Sent " + total + " invites");
    return true;
  }

  /**
   * @param memberInfo
   * @param invite
   */
  protected void doInvite(MemberInfoDto memberInfo, MemberInviteDto invite) {
    if (log.isDebugEnabled()) {
      log.debug("Sending invite for " + invite);
    }
    this.eventPublisher.publishEvent(new MemberInviteEvent(invite
        .setInvitee(memberInfo)));
  }

  @Transactional
  public InviteResponse memberMultiInvite(MemberInfoDto memberInfo) {
    log.info("Multi Invite " + memberInfo.toString());
    String text = memberInfo.getMultiInviteEmails();
    log.debug("Emails to send the invite to: " + text);
    if (StringUtils.isBlank(text)) {
      return new InviteResponse();
    }
    String[] emails = text.split(",");
    EmailValidator validator = EmailValidator.getInstance();

    int successful = 0;
    InviteResponse response = new InviteResponse();
    for (String email : emails) {
      email = email.trim();
      if (!validator.isValid(email)) {
        response.getFailed().add(email);
        continue;
      }
      MemberInviteDto invite = new MemberInviteDto();
      invite.setEmail(email);
      invite.setMemberType(MemberInviteType.Know.name());
      doInvite(memberInfo, invite);
      successful++;
      response.getSuccessful().add(email);
    }
    log.info("Sent " + successful + " invites, out of " + emails.length);
    return response;
  }

  @Transactional
  public MemberInfoDto memberSignupComplete(String processInstanceId) {
    log.info("Process instance id " + processInstanceId);

    MemberManagementService accountManagementService = this
        .getAccountManagementService();
    MemberDto memberDto = memberService.verify(processInstanceId);

    try {
      // FIXME The repository should be injected here.
      SocialPersonRepository memberRepo = (SocialPersonRepository) this
          .getSpringBean("socialPersonRepository");
      log.info("Member to be fetched " + memberDto.getId());

      SocialPerson validatedMember = memberRepo.findOne(memberDto.getId());
      log.info("Member fetched " + validatedMember);

      MemberInfoDto memberInfo = new MemberInfoDto();
      memberInfo.setId(validatedMember.getId());
      memberInfo.setFirstName(validatedMember.getFirstName());

      return memberInfo;

    } catch (Exception exp) {
      log.error("Exception in memberSignupComplete ", exp);
    }

    return null;

  }

  /**
   * 
   * @param memberInfo
   * @throws Exception
   */
  @Transactional
  public void memberUpdate(MemberInfoDto memberInfo) throws Exception {
    log.info(" Updating member with first name " + memberInfo.getFirstName());
    this.accountManagementService.memberUpdate(memberInfo);
  }

  /**
   * 
   * @param memberInfo
   * @throws Exception
   */
  @Transactional
  public DomainSearchResultSet memberSearch(MemberInfoDto memberInfo,
      SearchItemType searchItem, boolean embedded) throws Exception {

    /* get the search string */
    DomainSearchResultSet retSearchResult = null;
    log.info("Search string " + memberInfo.getSearch() + "member id "
        + memberInfo.getId());

    if (embedded != true) {
      retSearchResult = this.searchService.memberSearch(memberInfo.getSearch(),
          memberInfo.getId(), searchItem, null);
    } else {
      retSearchResult = this.searchService.memberSearch(memberInfo.getSearch(),
          memberInfo.getId(), searchItem, null);
    }

    return retSearchResult;

  }

  /**
   * 
   * @param memberInfo
   * @return
   * @throws Exception
   */
  public boolean logout(MemberInfoDto memberInfo, String sessionId) {

    /*
     * Here we just reset member info and return true
     */
    memberInfo.reset();

    CommunityEventDto eventDto = new CommunityEventDto();
    eventDto.setEventType(CommunityEventType.MemberLogoutEvent);
    eventDto.setEventGenerationTime(new Date());
    eventDto.setEventGeneratorId(memberInfo.getId());

    LoginInfo loginInfo = new LoginInfo();
    loginInfo.setLoginMode(LoginModeType.InCommunity);
    loginInfo.setWebSessionId(sessionId);
    eventDto.setLoginInfo(loginInfo);

    CommunitySpringEvent logoutEvent = new CommunitySpringEvent(eventDto);
    this.eventPublisher.publishEvent(logoutEvent);

    return true;
  }

  @Transactional
  public boolean memberSingleInvite(MemberInviteDto invite,
      MemberInfoDto memberInfo) {

    log.info("Invoking the member INVITE inviter " + memberInfo.getId()
        + " invitee  " + invite);
    this.accountManagementService.inviteInitiate(invite.getFirstName(),
        invite.getLastName(), invite.getEmail(), invite.getCompany(),
        invite.getMemberInviteType(), memberInfo.getFirstName(),
        memberInfo.getLastName(), memberInfo.getId());

    return true;
  }

  /**
   * 
   * @param memberInfo
   * @return
   * @throws CoreServiceException
   */
  @Transactional
  public MemberInfoDto memberLogin(String email, String password)
      throws CoreServiceException {

    MemberInfoDto memberInfo = null;

    if (accountManagementService != null) {
      memberInfo = this.accountManagementService.memberLogin(email, password);
      log.info("Member info found " + memberInfo.getFullName());
    }

    CommunityEventDto eventDto = new CommunityEventDto();
    eventDto.setEventType(CommunityEventType.MemberLoginEvent);
    eventDto.setEventGenerationTime(new Date());
    eventDto.setEventGeneratorId(memberInfo.getId());

    LoginInfo loginInfo = new LoginInfo();
    loginInfo.setLoginMode(LoginModeType.InCommunity);
    loginInfo.setWebSessionId(FaceletUtils.getSessionId());
    eventDto.setLoginInfo(loginInfo);

    CommunitySpringEvent loginEvent = new CommunitySpringEvent(eventDto);
    this.eventPublisher.publishEvent(loginEvent);

    return memberInfo;
  }

  /**
   * 
   * @param memberInfo
   * @return
   * @throws CoreServiceException
   */
  @Transactional
  public MemberInfoDto embeddedLogin(String uuid) throws CoreServiceException {

    MemberInfoDto memberInfo = null;

    if (accountManagementService != null) {
      memberInfo = this.accountManagementService.embeddedLogin(uuid);
      log.info("Member info found " + memberInfo.getFullName());
    }

    CommunityEventDto eventDto = new CommunityEventDto();
    eventDto.setEventType(CommunityEventType.MemberLoginEvent);
    eventDto.setEventGenerationTime(new Date());
    eventDto.setEventGeneratorId(memberInfo.getId());

    LoginInfo loginInfo = new LoginInfo();
    loginInfo.setLoginMode(LoginModeType.Embedded);
    loginInfo.setWebSessionId(FaceletUtils.getSessionId());
    eventDto.setLoginInfo(loginInfo);

    CommunitySpringEvent loginEvent = new CommunitySpringEvent(eventDto);
    this.eventPublisher.publishEvent(loginEvent);

    return memberInfo;
  }

  public void contact(MemberInfoDto info, DomainSearchResult selectedResult) {
    log.info("action event from tab to contact " + info.toString());
  }

  @Transactional
  public void associateOrganization(MemberInfoDto member,
      OrganizationBasicInfoDto selectedOrgTag) {

    OrganizationManagementServiceImpl orgService = this
        .getOrganizationService();
    orgService.associateOrganizationToMember(member, selectedOrgTag);
  }

  @Transactional
  public boolean isPendingBusinessCreateOrAssociateFlow(MemberInfoDto memberInfo) {
    OrganizationManagementServiceImpl orgService = this
        .getOrganizationService();
    return orgService.isPendingBusinessCreateOrAssociateFlow(memberInfo);
  }

  @Override
  @Transactional
  public void initiateOrganizationCreate(MemberInfoDto memberInfo,
      NewBusinessDto business) {

    log.info("Business to be created " + business.getBusinessName()
        + " for member " + memberInfo.getFirstName());
    OrganizationManagementServiceImpl orgService = this
        .getOrganizationService();
    orgService.initiateOrganizationCreate(memberInfo, business);
  }

  @Transactional
  public void createBusinessEntityNotification(Long memberId, String pid,
      boolean approved, String notificationMsg) {

    log.info("Org association member " + memberId + " notification msg "
        + notificationMsg);
    OrganizationManagementServiceImpl orgService = this
        .getOrganizationService();
    orgService.createBusinessEntityNotification(memberId, pid, notificationMsg,
        approved);
  }

  /**
   * 
   * @param member
   * @param newEmail
   * @throws CoreServiceException
   */
  @Transactional
  public void changeEmail(MemberInfoDto member) throws CoreServiceException {

    if (member != null) {
      this.accountManagementService.memberEmailChangeInitiate(member);
    }
  }

  @Transactional
  public void changePassword(MemberInfoDto member) {

    if (member != null) {
      this.accountManagementService.memberPasswordChange(member);
    }
  }

  @Transactional
  public void changeEmailComplete(long memberId, String processInstanceId) {
    accountManagementService.memberEmailChangeComplete(memberId,
        processInstanceId);
  }

  @Transactional
  public void memberOrgAssociationNotification(Long memberId, String pid,
      boolean approved) {

    log.info("Member id " + memberId + " process instance id " + pid
        + " status " + approved);

    this.businessManagementService.memberBusinessAssociationNotification(
        memberId, pid, Boolean.valueOf(approved));
  }

  @Transactional
  public void addMemberFeedback(Long memberId, String memberFeedback) {

    /* get member feedback repo and save it */
    this.accountManagementService.addMemberFeedback(memberId, memberFeedback);
  }

  @Transactional
  public void contactUs(NonmemberContactMessageInfoDto message) {

    this.accountManagementService.contactUs(message);
  }

  @Transactional
  public MemberInfoDto getMemberById(Long valueOf) throws Exception {
    return accountManagementService.memberById(valueOf);
  }

  @Transactional
  public MemberInfoDto viewMemberPublicProfile(Long memberViewed,
      SocialActorKey memberVieweing) throws Exception {

    SocialActorKey memberViewedKey = this.socialActorService
        .transitional_getKey(memberViewed);
    if (memberVieweing.equals(memberViewedKey)) {
      this.socialActorMemberService.viewMemberProfile(memberVieweing,
          memberViewedKey);
    }
    return accountManagementService.memberById(memberViewed);
  }

  @Transactional
  public DomainSearchResultSet advancedSearch(MemberInfoDto searchInfo,
      AdvancedSearchInput advSearchInput) throws Exception {

    DomainSearchResultSet resultSet = this.searchService.advancedSearch(
        searchInfo, advSearchInput);
    return resultSet;
  }

  private LeadDto getLeadDtoForOrganization(MemberInfoDto loggedMember,
      OrganizationDetailInfoDto orgToSend) {

    LeadDto leadDto = new LeadDto();
    leadDto.setForSocialActorId(orgToSend.getId());
    leadDto.setContactEmail(loggedMember.getEmail());
    leadDto.setContactPhone(loggedMember.getBusinessPhone());
    leadDto.setName(loggedMember.getFullName());

    leadDto.setFromSocialActorId(loggedMember.getId());
    leadDto.setFirstName(loggedMember.getFirstName());
    leadDto.setLastName(loggedMember.getLastName());
    leadDto.setLeadPhoto(loggedMember.getPhotoLocation());

    AddressDto addrDto = new AddressDto();
    addrDto.setCity(loggedMember.getCity());
    addrDto.setCountry(loggedMember.getCountry());
    addrDto.setZipCode(loggedMember.getZipCode());
    addrDto.setStreet(loggedMember.getStreet());
    leadDto.setLocation(addrDto);

    return leadDto;
  }

  @Transactional
  public void sendContactMessageToOrganization(ContactMessageDetail message,
      MemberInfoDto loggedMember, OrganizationDetailInfoDto orgToSend)
      throws ResourceNotFoundException, ParseErrorException, Exception {

    log.info("Sending message to org " + orgToSend.getId());

    /*
     * catching the exception because we want to keep a record of the contact
     * even in case where geo coding service is not working
     */
    try {
      this.messagingService.sendContactMessageToOrganization(message,
          loggedMember, orgToSend);
    } catch (Exception exp) {
      log.error("Geo coding error ", exp);
    }

    LeadDto leadDto = this.getLeadDtoForOrganization(loggedMember, orgToSend);
    leadDto.setContact(message);
    leadDto.setLeadType(LeadType.ContactLead);

    LeadEvent event = new LeadEvent(leadDto);
    this.eventPublisher.publishEvent(event);

  }

  @Transactional
  public void createClickThroughLeadForOrganization(MemberInfoDto loggedMember,
      OrganizationDetailInfoDto orgToSend) {
    LeadDto leadDto = this.getLeadDtoForOrganization(loggedMember, orgToSend);
    leadDto.setLeadType(LeadType.ClickThroughLead);

    LeadEvent event = new LeadEvent(leadDto);
    this.eventPublisher.publishEvent(event);

  }

  @Transactional
  public List<CommunityEventDto> getLoginInfoList(Long id) {
    return this.accountManagementService.getLoginInfoList(id);
  }

  @Transactional
  public void forgotPasswordInitiate(String forgotPasswordEmail)
      throws CoreServiceException {
    this.accountManagementService.forgotPasswordInitiate(forgotPasswordEmail);
  }

  @Transactional
  public MemberInfoDto processForgotPasswordLink(Long memberId,
      String processInstanceId) {
    try {

      // FIXME The repository should be injected here.
      WorkflowTransientStateRepository memberTransientStateRepository = (WorkflowTransientStateRepository) this
          .getSpringBean("workflowTransientStateRepository");

      WorkflowTransientState tranState = memberTransientStateRepository
          .findByMemberIdAndProcessInstanceId(memberId, processInstanceId);

      if (tranState != null) {
        List<WorkflowTransientStateAttribute> attrList = tranState
            .getAttributes();
        String savedProcessId = null;

        for (WorkflowTransientStateAttribute attr : attrList) {
          if (attr.getAttributeName().contentEquals(
              AttributeConstantUtil.MEMBER_FORGOT_PASSWORD_KEY) == true) {
            savedProcessId = attr.getAttributeValue();
            break;
          }
        }
        if (savedProcessId != null && processInstanceId != null
            && processInstanceId.equals(savedProcessId)) {

          // FIXME The repository should be injected here.
          SocialPersonRepository memberRepo = (SocialPersonRepository) this
              .getSpringBean("socialPersonRepository");
          log.info("Member to be fetched " + memberId);

          SocialPerson validatedMember = memberRepo.findOne(memberId);
          log.info("Member fetched " + validatedMember);

          MemberInfoDto memberInfo = new MemberInfoDto();
          memberInfo.setId(validatedMember.getId());
          memberInfo.setFirstName(validatedMember.getFirstName());

          return memberInfo;
        }
      }
    } catch (Exception exp) {
      log.error("Exception in processForgotPasswordLink ", exp);

    }

    return null;
  }

  @Transactional
  public void changeForgottenPasswordComplete(MemberInfoDto memberInfo,
      String pid) {
    accountManagementService.memberChangeForgottenPasswordComplete(memberInfo,
        pid);
  }

  @Transactional
  public void memberInviteSignupInput(String pid, MemberInfoDto member) {
    accountManagementService.memberInviteSignupInputComplete(member, pid);
  }

  @Transactional
  public void existingMemberAcceptInviteDecision(long id,
      String processInstanceId) {
    accountManagementService.existingMemberAcceptInviteDecision(id,
        processInstanceId);
  }

  @Transactional
  public void existingMemberRejectInviteDecision(long id,
      String processInstanceId) {
    accountManagementService.existingMemberRejectInviteDecision(id,
        processInstanceId);
  }

  @Transactional
  public MemberInfoDto isMemberWithEmailExists(String email) {
    return accountManagementService.isMemberWithEmailExists(email);
  }

  @Transactional
  public void shareComment(MemberInfoDto member, String shareComment) {
    accountManagementService.shareComment(member, shareComment);
  }

  /*
   * @param inviterParamId
   * 
   * @return
   */
  @Transactional
  public List<MemberInviteInfoDto> findPendingInvitesByInviter(
      Long inviterParamId) {
    return this.accountManagementService
        .findPendingInvitesByInviter(inviterParamId);
  }

  @Transactional
  public void reInviteMember(Long inviteRequestId) {
    this.accountManagementService.reInviteMember(inviteRequestId);
  }

  @Transactional
  public void deleteInviteRequest(Long inviteRequestId) {
    this.accountManagementService.deleteInviteRequest(inviteRequestId);
  }

  @Override
  @Transactional
  public void reportProblem(NewErrorReportingDto error) {
    this.accountManagementService.reportProblem(error);

  }
}