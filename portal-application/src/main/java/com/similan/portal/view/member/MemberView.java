package com.similan.portal.view.member;

import java.net.URL;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.PhotoViewOption;
import com.similan.domain.entity.metadata.StateType;
import com.similan.domain.entity.util.AddressDto;
import com.similan.framework.dto.OrganizationBasicInfoDto;
import com.similan.framework.dto.OrganizationBasicInfoList;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberExpertiseInfo;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.member.MemberInterestInfo;
import com.similan.framework.util.EmailValidator;
import com.similan.portal.service.MemberService;
import com.similan.portal.view.BaseView;
import com.similan.portal.view.handler.ImageDeletion;
import com.similan.portal.view.handler.ImageUpload;
import com.similan.portal.view.social.LinkedInConnectionManager;
import com.similan.service.api.CoreServiceErrorCode;
import com.similan.service.exception.CoreServiceException;

@Scope("view")
@Component("memberView")
@Slf4j
public class MemberView extends BaseView {

  private static final long serialVersionUID = 1L;

  @Autowired(required = true)
  private MemberInfoDto member = null;

  private OrganizationDetailInfoDto memberOrg = new OrganizationDetailInfoDto();

  private String commentToAdd;

  private String expertiseToAdd;

  private String interestToAdd;

  private String currentPassword;

  private boolean showOrgCreateButton = false;

  private List<StateType> selectedStateList = null;

  private AddressDto orgToCreateAddr = new AddressDto();

  private boolean pendingBusinessCreationAssociation = false;

  private OrganizationBasicInfoList orgList;

  private String oauthVerifier = null;

  private boolean linkedInUpdatePerformed = false;

  @PostConstruct
  public void init() {
    log.info("Post init in member view ");
    if (member != null && member.getAddressDto() != null) {
      this.loadStatesForSelectedCountry();
    }

    /*
     * we get the org list only when member doesnt have an employer and there is
     * a possibility of creating a business
     */
    if (member != null) {
      if (member.getEmployer() == null) {
        pendingBusinessCreationAssociation = this.memberService
            .isPendingBusinessCreateOrAssociateFlow(member);

        List<OrganizationBasicInfoDto> orgBasicInfoList = this.orgService
            .getVisibleBusinesses();
        orgList = new OrganizationBasicInfoList(orgBasicInfoList);
      }

    }
  }

  public String getMemberPhoto() {
    log.info("Returning member photo ");
    if (this.member != null
        && StringUtils.isNotBlank(this.member.getPhotoLocation())) {
      return this.member.getPhotoLocation();
    }

    return IMAGES_MEMBER_DEFAULT_PHOTO;
  }

  public void setMemberPhoto(String photo) {
    this.member.setPhotoLocation(photo);
  }

  public boolean isMemberPhotoExists() {
    return this.member != null
        && StringUtils.isNotBlank(this.member.getPhotoLocation())
        && !StringUtils.equalsIgnoreCase(this.member.getPhotoLocation(),
            IMAGES_MEMBER_DEFAULT_PHOTO);
  }

  public void photoViewOptionSelected(AjaxBehaviorEvent ajaxEvent) {
    log.info("value Selected " + member.getPhotoViewOption());
  }

  public OrganizationBasicInfoList getOrgList() {
    return orgList;
  }

  public void setOrgList(OrganizationBasicInfoList orgList) {
    this.orgList = orgList;
  }

  public boolean getPendingBusinessCreationAssociation() {
    return pendingBusinessCreationAssociation;
  }

  public void setPendingBusinessCreationAssociation(
      boolean pendingBusinessCreationAssociation) {
    this.pendingBusinessCreationAssociation = pendingBusinessCreationAssociation;
  }

  public AddressDto getOrgToCreateAddr() {
    return orgToCreateAddr;
  }

  public void setOrgToCreateAddr(AddressDto orgToCreateAddr) {
    this.orgToCreateAddr = orgToCreateAddr;
  }

  public List<StateType> getSelectedStateList() {
    return selectedStateList;
  }

  public void setSelectedStateList(List<StateType> selectedStateList) {
    this.selectedStateList = selectedStateList;
  }

  public void loadStatesForSelectedCountry() {
    this.selectedStateList = this.getStatesForCountry(member.getAddressDto()
        .getCountry());

    log.info("Country " + member.getCountry() + " state list "
        + this.selectedStateList.size() + " content " + this.selectedStateList);
  }

  public void loadStatesForSelectedEmployerCountry() {
    this.selectedStateList = this.getStatesForCountry(orgToCreateAddr
        .getCountry());

    log.info("Country " + member.getCountry() + " state list "
        + this.selectedStateList.size() + " content " + this.selectedStateList);
  }

  public OrganizationDetailInfoDto getMemberOrg() {
    return memberOrg;
  }

  public void setMemberOrg(OrganizationDetailInfoDto memberOrg) {
    this.memberOrg = memberOrg;
  }

  public boolean isShowOrgCreateButton() {
    return this.getShowOrgCreateButton();
  }

  public boolean getShowOrgCreateButton() {

    return this.showOrgCreateButton;
  }

  public void setShowOrgCreateButton(boolean showOrgCreateButton) {
    this.showOrgCreateButton = showOrgCreateButton;
  }

  public String getCurrentPassword() {
    return currentPassword;
  }

  public void setCurrentPassword(String currentPassword) {
    this.currentPassword = currentPassword;
  }

  public void moreUpdates() {
    log.info("More updates are fetched");
  }

  public void embeddedLogin() {

    String memberId = this.getContextParam("mid");

    log.info("embedded login with member id =  " + memberId);

    if (memberId == null || memberId.equals("")) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "Embedded Login Status", "No member specified"));
      return;
    }
    try {

      this.member = memberService.embeddedLogin(memberId);

    } catch (CoreServiceException exp) {

      log.info("Error embedded login ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure",
              "Failure to login due to " + exp.getErrorCode()));
    } catch (Exception exp) {

      log.info("Error embedded login ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure",
              "Failure to login due to " + exp.getMessage()));
    }

  }

  /**
   * member profile update
   */
  public String updateMemberProfile() {
    log.info("Updating edit profile of member " + member.getEmail());

    try {

      if (this.member == null) {
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Member Update Status", "Cannot update null member"));
        return "error";
      }

      if (StringUtils.isEmpty(this.member.getEmail()) == true) {
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Member Update Status",
                "Cannot update member with invalid email"));
        return "error";
      }

      if (EmailValidator.validate(this.member.getEmail()) != true) {
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Member Update Status",
                "Cannot update member with invalid email"));
        return "error";
      }

      this.memberService.memberUpdate(this.member);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Member Update Status",
              "Successfully updated member profile"));
    } catch (Exception exp) {
      log.error("Error updating member ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Member Update Status",
              "Failure to update member profile"));
    }

    return "result";
  }

  public MemberInfoDto getMember() {
    return member;
  }

  public void setMember(MemberInfoDto member) {
    this.member = member;
  }

  public void handleFileUpload(final FileUploadEvent event) {
    String result = this.imageUploadHandler
        .handleImageUpload(new ImageUpload() {

          public void update() throws Exception {
            memberService.memberUpdate(member);
          }

          public void setImageKey(String key) {

            member.setPhotoViewOption(PhotoViewOption.ShowPhoto.name());
            member.setPhotoLocation(key);
          }

          public String getType() {
            return "member";
          }

          public String getId() {
            return String.valueOf(member.getId());
          }

          public UploadedFile getFile() {
            return event.getFile();
          }

          public String currentKey() {
            return member.getPhotoLocation();
          }
        });
    if (result != null) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "Error uploading image", result));
    }
  }

  public void deletePhoto() {
    this.imageUploadHandler.handleImageDeletion(new ImageDeletion() {

      public void update() throws Exception {
        MemberInfoDto temp = memberService.getMemberById(member.getId());
        temp.setPhotoLocation(IMAGES_MEMBER_DEFAULT_PHOTO);
        memberService.memberUpdate(temp);
        member.setPhotoLocation(temp.getPhotoLocation());
      }

      public String getCurrentKey() {
        return member.getPhotoLocation();
      }
    });
  }

  public void followUpdateFilter() {
    log.info("Member followUpdateFilter ...");
  }

  public void followConnectionFilter() {
    log.info("Member followConnectionFilter ...");
  }

  public void followGroupFilter() {
    log.info("Member followGroupFilter ...");
  }

  public void viewAllUpdates() {
    log.info("Member viewAllUpdates ...");
  }

  public void follow() {
    System.out.println("Member to follow ...");
  }

  public String getCommentToAdd() {
    return commentToAdd;
  }

  public void setCommentToAdd(String commentToAdd) {
    this.commentToAdd = commentToAdd;
  }

  public void addInterest() {
    log.info("Adding interest " + interestToAdd);

    if (StringUtils.isNotBlank(interestToAdd)) {
      MemberInterestInfo interestInfo = new MemberInterestInfo();
      interestInfo.setInterest(interestToAdd);
      this.member.getInterest().add(interestInfo);
      interestToAdd = StringUtils.EMPTY;
    }
  }

  public void addExpertise() {
    log.info("Adding expertise " + expertiseToAdd);

    if (StringUtils.isNotBlank(expertiseToAdd)) {
      MemberExpertiseInfo expertiseInfo = new MemberExpertiseInfo();
      expertiseInfo.setExpertise(expertiseToAdd);
      this.member.getExpertise().add(expertiseInfo);
      expertiseToAdd = StringUtils.EMPTY;
    }
  }

  public String getExpertiseToAdd() {
    return expertiseToAdd;
  }

  public void setExpertiseToAdd(String expertiseToAdd) {
    System.out.println("Setting expertise expertise " + expertiseToAdd);
    this.expertiseToAdd = expertiseToAdd;
  }

  public String getInterestToAdd() {
    return interestToAdd;
  }

  public void setInterestToAdd(String interestToAdd) {
    System.out.println("Adding interest " + interestToAdd);
    this.interestToAdd = interestToAdd;
  }

  public MemberService getMemberService() {
    return memberService;
  }

  public void deleteInterest() {

    if (this.member.getSelectedMemberInterest() == null) {
      log.info("No selected interest");
      return;
    }

    if (this.member.getInterest() != null) {
      for (MemberInterestInfo interestInfo : this.member.getInterest()) {
        if (interestInfo.getInterest().contentEquals(
            this.member.getSelectedMemberInterest().getInterest()) == true) {
          log.info("deleting interest " + interestInfo.getInterest());
          this.member.getInterest().remove(interestInfo);
          this.member.getDeletedInterestInfo().add(interestInfo);
          break;
        }
      }
    }
  }

  public void deleteExpertise() {

    if (this.member.getSelectedMemberExpertise() == null) {
      log.info("No selected expertise");
      return;
    }

    for (MemberExpertiseInfo expertiseInfo : this.member.getExpertise()) {
      if (expertiseInfo.getExpertise().contentEquals(
          this.member.getSelectedMemberExpertise().getExpertise()) == true) {
        log.info("deleting expertise " + expertiseInfo.getExpertise());
        this.member.getExpertise().remove(expertiseInfo);
        this.member.getDeletedExpertiseInfo().add(expertiseInfo);
        break;
      }
    }
  }

  public void changePassword() {

    try {
      log.info("Changing password to " + this.member.getNewPassword()
          + " from " + this.member.getPassword());

      if (StringUtils.equals(this.currentPassword, this.member.getPassword())) {
        this.member.setPassword(currentPassword);
        this.memberService.changePassword(this.member);
        FacesContext
            .getCurrentInstance()
            .addMessage(
                null,
                new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Info",
                    "Your password has been successfully change. Please use your new password next time you log into the system."));
      } else {
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                "The current password you enterd is invalid"));

      }
    } catch (Exception exp) {
      log.info("Error changing password ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Error changing your password " + exp.getMessage()));
    }
  }

  public void clearPasswordFields() {
    this.currentPassword = null;
    this.member.setNewPassword(null);
    this.member.setConfirmPassword(null);
  }

  public void changeEmail() {
    try {

      this.member.setEmail(this.member.getNewEmail());
      log.info("Changing email to " + this.member.getNewEmail());

      if (StringUtils.equals(this.member.getNewEmail(),
          this.member.getConfirmEmail())) {
        this.memberService.changeEmail(this.member);
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
                "An email has been sent to your new email for validation"));
      } else {
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                "The New Email and the Confirm New Email should match"));

      }
    } catch (CoreServiceException exp) {
      if (exp.getErrorCode().equals(
          CoreServiceErrorCode.DUPLICATE_PRIMARY_EMAIL_FOUND)) {
        log.error("Error changing email", exp);
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                "The email address selected already exists in the system"));
      } else {
        log.error("Error changing email", exp);
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                "Error changing email" + exp.getMessage()));
      }
    } catch (Exception exp) {
      log.error("Error changing email", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Error changing email" + exp.getMessage()));
    }

  }

  public void updateFromLinkedIn() {
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
      log.error("LinkedIn error", e);
    }

  }

  public void handleReturnFromLinkedIn() {
    try {
      if (oauthVerifier == null || linkedInUpdatePerformed == true) {
        return;
      }
      log.info("Handling linkedIn callback");
      FacesContext facesContext = FacesContext.getCurrentInstance();
      LinkedInConnectionManager linkedInConnectionManager = (LinkedInConnectionManager) facesContext
          .getApplication().evaluateExpressionGet(facesContext,
              "#{linkedInConnectionmanager}", LinkedInConnectionManager.class);
      linkedInConnectionManager.performAuthentication(oauthVerifier);

      member = linkedInConnectionManager
          .updateMemberInfoFromLinkedInProfileData(member);

      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
              "Your information has been imported from LinkedIn. "
                  + "Please review your profile and click save."));
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

  public String getOauthVerifier() {
    return oauthVerifier;
  }

  public void setOauthVerifier(String oauthVerifier) {
    this.oauthVerifier = oauthVerifier;
  }
}
