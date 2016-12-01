package com.similan.portal.view.invite;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.PhotoViewOption;
import com.similan.framework.dto.MemberInviteDto;
import com.similan.framework.dto.MemberInviteListDto;
import com.similan.framework.dto.OrganizationBasicInfoDto;
import com.similan.framework.dto.OrganizationBasicInfoList;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.network.LinkedInConnection;
import com.similan.framework.dto.partner.PartnerProgramDefinitionDto;
import com.similan.framework.importexport.csv.BeanImportingException;
import com.similan.portal.service.InviteResponse;
import com.similan.portal.service.MemberService;
import com.similan.portal.view.BaseView;
import com.similan.portal.view.social.LinkedInConnectionManager;
import com.similan.service.exception.ContactAlreadyExistsException;
import com.similan.service.exception.CoreServiceException;

@Scope("view")
@Component("memberInviteOldView")
@Slf4j
public class MemberInviteOldView extends BaseView {
  private static final long serialVersionUID = 1L;

  private MemberInviteDto memberInvite = new MemberInviteDto();

  private MemberInviteListDto memberInviteList;

  private String multiInviteMode;
  private String singleInviteMode;
  private List<PartnerProgramDefinitionDto> partnerProgramsForBusiness;
  private boolean disableBusinessEmailField = false;
  private boolean showPartnerInviteCheckBox = true;
  private boolean businessInviteCheckBox = true;
  private Long selectedPartnerProgram = null;
  private boolean inviteeIsMember = true;
  private OrganizationBasicInfoList orgList;
  private String oauthVerifier = null;

  @Autowired
  private MemberInfoDto memberInfo;

  private OrganizationDetailInfoDto orgInfo;

  private List<LinkedInConnection> networkConnections = null;
  private LinkedInConnection selectedNetworkConnections[] = new LinkedInConnection[0];

  private boolean linkedInUpdatePerformed = false;

  @PostConstruct
  public void init() {

    try {

      OrganizationBasicInfoDto orgTag = memberInfo.getEmployer();
      if (orgTag != null) {
        this.orgInfo = this.getOrgService().getOrganization(
            memberInfo.getEmployer().getId());
      }
    } catch (Exception exp) {
      log.error("Cannot find org in invite view ", exp);
    }

    log.info("Found logged in member " + memberInfo.getFirstName());

    if (orgInfo != null && orgInfo.getBusinessPrimaryEmail() != null) {
      // fetch all the organizations for partner and business invite
      this.populatePotentialPartners();

      // get all the partner programs
      partnerProgramsForBusiness = this.getPartnerManagementService()
          .getPartnerProgramsForBusiness(orgInfo);

      if (orgList != null && orgList.getOrgList() != null
          && orgList.getOrgList().size() > 0) {

        if (partnerProgramsForBusiness != null
            && partnerProgramsForBusiness.size() > 0) {
          showPartnerInviteCheckBox = true;
        } else {
          showPartnerInviteCheckBox = false;
        }
      } else {
        showPartnerInviteCheckBox = false;
        businessInviteCheckBox = false;
      }
    } else {
      showPartnerInviteCheckBox = false;
      businessInviteCheckBox = false;

    }

    // now evaluate and determine the mode to show
    // appropriate page
    if (showPartnerInviteCheckBox == true) {
      singleInviteMode = "partner";
    } else if (businessInviteCheckBox == true) {
      singleInviteMode = "business";
    } else {
      singleInviteMode = "contact";
    }

    this.multiInviteMode = "email";
  }

  private void populatePotentialPartners() {
    List<OrganizationBasicInfoDto> orgBasicInfoList = this.orgService
        .getVisibleBusinesses();

    // we do not want to show the same org to be a partner
    for (OrganizationBasicInfoDto org : orgBasicInfoList) {
      if (org.getId() == orgInfo.getId()) {
        orgBasicInfoList.remove(org);
        break;
      }
    }

    orgList = new OrganizationBasicInfoList(orgBasicInfoList);
  }

  public String getOrganizationLogo(String logoLocation) {
    return PhotoViewOption.ShowPhoto.effectivePhoto("images/businessLogo.jpg",
        logoLocation);
  }

  public List<SelectItem> getPartnerProgramsForBusinessSelectItems() {
    List<SelectItem> items = new ArrayList<SelectItem>();

    if (partnerProgramsForBusiness == null) {
      return items;
    }
    for (PartnerProgramDefinitionDto program : partnerProgramsForBusiness) {
      items.add(new SelectItem(program.getId(), program.getName()));
    }
    return items;
  }

  public OrganizationBasicInfoList getOrgList() {
    return orgList;
  }

  public void setOrgList(OrganizationBasicInfoList orgList) {
    this.orgList = orgList;
  }

  public boolean isBusinessInviteCheckBox() {
    return businessInviteCheckBox;
  }

  public void setBusinessInviteCheckBox(boolean businessInviteCheckBox) {
    this.businessInviteCheckBox = businessInviteCheckBox;
  }

  public Long getSelectedPartnerProgram() {
    return selectedPartnerProgram;
  }

  public void setSelectedPartnerProgram(Long selectedPartnerProgram) {
    this.selectedPartnerProgram = selectedPartnerProgram;
  }

  public void inviteMultiple() {
    log.info("Multi invite emails " + memberInfo.getMultiInviteEmails());
    if (StringUtils.isBlank(memberInfo.getMultiInviteEmails())) {
      facesHelper.addMessage(null, FacesMessage.SEVERITY_ERROR,
          "No email addresses provided", "");
      return;
    }
    InviteResponse response = memberService.memberMultiInvite(memberInfo);
    memberInfo.setMultiInviteEmails("");

    // FIXME Use a resource bundle.
    for (String email : response.getSuccessful()) {
      facesHelper.addMessage("inputarea", FacesMessage.SEVERITY_INFO,
          "Success", "Successfully sent invite to " + email);
    }
    for (String email : response.getFailed()) {
      facesHelper.addMessage("inputarea", FacesMessage.SEVERITY_WARN, "Failed",
          "Unable to send invite to " + email);
    }
  }

  public void inviteBulk() {

    try {
      memberService.memberBulkInvite(memberInviteList, memberInfo);
      int count = memberInviteList.getMemberInvites().size();
      memberInviteList = new MemberInviteListDto();
      // FIXME Use internationalization properly!!
      facesHelper.addMessage(null, FacesMessage.SEVERITY_INFO, "Success",
          "Sent " + count + " invites");
    } catch (Exception exp) {

      log.error("Error inviting business "
          + this.getMemberInvite().getOrgTag().getName(), exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure",
              "Error inviting business "
                  + this.getMemberInvite().getOrgTag().getName() + " reason "
                  + exp.getMessage()));

    }
  }

  public void businessInvite() {

    // cannot do partner invite ifyou are
    // not associated with business
    if (orgInfo == null || StringUtils.isEmpty(orgInfo.getEmail()) == true) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "You cannot invite a business since you are not "
                  + "associated with any valid business"));
      return;
    }

    // the invitee business email also should be valid
    if (StringUtils.isEmpty(this.memberInvite.getOrgTag().getEmail()) == true) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "You cannot invite a business since the invitee business "
                  + "does not have a valid email business"));
      return;

    }

    // check whether it is same business
    if (orgInfo.getEmail().equalsIgnoreCase(
        this.memberInvite.getOrgTag().getEmail())) {

      FacesContext
          .getCurrentInstance()
          .addMessage(
              null,
              new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                  "Error in business invitation,cannot invite same business as partner"));
      return;
    }

    log.info("Business invite of org "
        + this.getMemberInvite().getOrgTag().getName());

    try {
      if (disableBusinessEmailField == true
          || StringUtils.equals(memberInvite.getBusinessEmail(),
              memberInvite.getConfirmBusinessEmail())) {

        String businessName = this.getMemberInvite().getOrgTag().getName();
        log.info("Business name " + businessName);

        this.orgService.initiateBusinessInvite(memberInfo, businessName, this
            .getMemberInvite().getBusinessEmail(), this.getMemberInvite()
            .getBusinessType());

        String message = "Thanks for inviting "
            + this.getMemberInvite().getOrgTag().getName()
            + " an email has been sent to "
            + this.getMemberInvite().getBusinessEmail()
            + " for furthar processing";

        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Message", message));
      } else {
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                "Email and Confirm Email must match"));
      }
    } catch (ContactAlreadyExistsException contactAlreadyExistsException) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invite Failure",
              "Business with email address " + memberInvite.getBusinessEmail()
                  + " is already a contact"));
    } catch (CoreServiceException exp) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invite Failure", exp
              .getMessage()));

    } catch (Exception exp) {
      log.error("Error inviting business "
          + this.getMemberInvite().getOrgTag().getName(), exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure",
              "Error inviting business "
                  + this.getMemberInvite().getOrgTag().getName() + " reason "
                  + exp.getMessage()));
    }

  }

   public void handleFileUpload(FileUploadEvent event) {
    if (event == null) {
      log.warn("Received null file upload event");
      return;
    }

    log.info("Uploaded file name " + event.getFile().getFileName());
    try {
      this.memberInviteList = this.memberService.handleFileUpload(event);
      log.info("Invitee list " + this.memberInviteList.toString());
    } catch (BeanImportingException bie) {
      log.error("Error while importing beans " + bie.getMessage());
      facesHelper.addDirectMessage(null, FacesMessage.SEVERITY_ERROR,
          bie.getMessage());
    }
  }

  public void onBusinessNameChanged() {

    log.info("Selected business in onBusinessNameChanged "
        + memberInvite.getOrgTag());
    if (memberInvite != null
        && memberInvite.getOrgTag().getId() != Long.MIN_VALUE) {
      memberInvite.setBusinessEmail(memberInvite.getOrgTag().getEmail());
      setDisableBusinessEmailField(true);
    } else {
      setDisableBusinessEmailField(false);
    }
  }

  public boolean isShowPartnerInviteCheckBox() {
    return showPartnerInviteCheckBox;
  }

  public void setShowPartnerInviteCheckBox(boolean showPartnerInviteCheckBox) {
    this.showPartnerInviteCheckBox = showPartnerInviteCheckBox;
  }

  public void setPartnerProgramsForBusiness(
      List<PartnerProgramDefinitionDto> partnerProgramsForBusiness) {
    this.partnerProgramsForBusiness = partnerProgramsForBusiness;
  }

  public List<PartnerProgramDefinitionDto> getPartnerProgramsForBusiness() {
    return partnerProgramsForBusiness;
  }

  /**
   * 1. Check whether it is an existing business or new business 2. If existing
   * business we check whether it is already a partner for the selected program
   * 3. If so we do not procedd 4. If not we proceed
   */
  public void partnerInvite() {
    

  }

  public void onEmailAddressChanged() {
    log.error("Email address changed to " + memberInvite.getEmail());
    MemberInfoDto existingMember = memberService.isMemberWithEmailExists(memberInvite.getEmail());

    if (existingMember != null) {
      log.info("Member exists with email address.");
      setInviteeIsMember(true);
    } else {
      log.info("Member does not exist with email address.");
      setInviteeIsMember(false);
    }
  }

  public MemberInviteDto getMemberInvite() {
    return memberInvite;
  }

  public MemberInviteListDto getMemberInviteList() {
    return memberInviteList;
  }

  public void setMemberInviteList(MemberInviteListDto memberInviteList) {
    this.memberInviteList = memberInviteList;
  }

  public void setMemberInvite(MemberInviteDto memberInvite) {
    this.memberInvite = memberInvite;
  }

  public void setMemberService(MemberService memberService) {
    this.memberService = memberService;
  }

  public MemberInfoDto getMemberInfo() {
    return memberInfo;
  }

  public void setMemberInfo(MemberInfoDto memberInfo) {
    this.memberInfo = memberInfo;
  }

  public String getMultiInviteMode() {
    return multiInviteMode;
  }

  public void setMultiInviteMode(String multiInviteMode) {
    this.multiInviteMode = multiInviteMode;
  }

  public boolean isDisableBusinessEmailField() {
    return disableBusinessEmailField;
  }

  public void setDisableBusinessEmailField(boolean disableBusinessEmailField) {
    this.disableBusinessEmailField = disableBusinessEmailField;
  }

  public String getSingleInviteMode() {
    return singleInviteMode;
  }

  public void setSingleInviteMode(String singleInviteMode) {
    this.singleInviteMode = singleInviteMode;
  }

  public boolean isInviteeIsMember() {
    return inviteeIsMember;
  }

  public void setInviteeIsMember(boolean inviteeIsMember) {
    this.inviteeIsMember = inviteeIsMember;
  }

  public String getProgramLocation(long id) {
    for (PartnerProgramDefinitionDto program : partnerProgramsForBusiness) {
      if (program.getId() == id)
        return program.getLogoLocation();
    }
    return "";
  }

  public String getProgramName(long id) {
    for (PartnerProgramDefinitionDto program : partnerProgramsForBusiness) {
      if (program.getId() == id)
        return program.getName();
    }
    return "";
  }

  public void inviteLinkedInContacts() {
    try {
      linkedInUpdatePerformed = false;

      FacesContext facesContext = FacesContext.getCurrentInstance();
      LinkedInConnectionManager linkedInConnectionManager = (LinkedInConnectionManager) facesContext
          .getApplication().evaluateExpressionGet(facesContext,
              "#{linkedInConnectionmanager}", LinkedInConnectionManager.class);

      HttpServletRequest request = (HttpServletRequest) facesHelper
          .getExternalContext().getRequest();

      URL url = new URL(request.getRequestURL().toString());
      URL newUrl = new URL(url.getProtocol(), url.getHost(), url.getPort(),
          request.getRequestURI());

      String origRequest = newUrl.toString();

      log.info("Callback set to: " + origRequest);
      String targetUrl = linkedInConnectionManager
          .getLinkedInAuthorizationUrl(origRequest);
      ExternalContext externalContext = facesHelper.getExternalContext();

      externalContext.redirect(targetUrl);
    } catch (Exception e) {
      log.error("Linkedin error", e);
    }

  }

  public void handleReturnFromLinkedIn() {
    try {
      if (oauthVerifier == null || linkedInUpdatePerformed == true) {
        return;
      }
      singleInviteMode = "network";
      log.info("Handling linkedIn callback");
      FacesContext facesContext = FacesContext.getCurrentInstance();
      LinkedInConnectionManager linkedInConnectionManager = (LinkedInConnectionManager) facesContext
          .getApplication().evaluateExpressionGet(facesContext,
              "#{linkedInConnectionmanager}", LinkedInConnectionManager.class);
      linkedInConnectionManager.performAuthentication(oauthVerifier);

      networkConnections = linkedInConnectionManager.getLinkedInConnections();

      Collections.sort(networkConnections,
          new Comparator<LinkedInConnection>() {

            public int compare(LinkedInConnection o1, LinkedInConnection o2) {
              int sort = o1.getLastName().compareToIgnoreCase(o2.getLastName());
              if (sort != 0)
                return sort;
              else
                return o1.getFirstName().compareToIgnoreCase(o2.getFirstName());
            }
          });

    } catch (Exception e) {
      log.error("Linkedin error", e);

      FacesContext.getCurrentInstance()
          .addMessage(
              null,
              new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e
                  .getMessage()));

    }
    linkedInUpdatePerformed = true;
  }

  public void inviteNetworkConnections() {
    try {
      singleInviteMode = "network";
      log.info("Inviting Connections for "
          + getSelectedNetworkConnections().length);
      FacesContext facesContext = FacesContext.getCurrentInstance();
      LinkedInConnectionManager linkedInConnectionManager = (LinkedInConnectionManager) facesContext
          .getApplication().evaluateExpressionGet(facesContext,
              "#{linkedInConnectionmanager}", LinkedInConnectionManager.class);

      linkedInConnectionManager.inviteLinkedInInitiate(
          (List<LinkedInConnection>) Arrays.asList(selectedNetworkConnections),
          memberInfo);
      networkConnections = null;
      selectedNetworkConnections = new LinkedInConnection[0];

      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
              "Your connections have been sent an invitation"));

    } catch (Exception e) {
      log.error("Error while performing invitation", e);
      FacesContext.getCurrentInstance()
          .addMessage(
              null,
              new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e
                  .getMessage()));

    }
  }

  public String getOauthVerifier() {
    return oauthVerifier;
  }

  public void setOauthVerifier(String oauthVerifier) {
    this.oauthVerifier = oauthVerifier;
  }

  public LinkedInConnection[] getSelectedNetworkConnections() {
    return selectedNetworkConnections;
  }

  public void setSelectedNetworkConnections(
      LinkedInConnection[] selectedNetworkConnections) {
    this.selectedNetworkConnections = selectedNetworkConnections;
  }

  public List<LinkedInConnection> getNetworkConnections() {
    return networkConnections;
  }

  public void setNetworkConnections(List<LinkedInConnection> networkConnections) {
    this.networkConnections = networkConnections;
  }

}
