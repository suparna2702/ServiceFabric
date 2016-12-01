package com.similan.portal.view.business;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.PhotoViewOption;
import com.similan.domain.entity.metadata.StateType;
import com.similan.framework.dto.OrganizationAddressDto;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.community.ExternalBusinessPortalDto;
import com.similan.framework.dto.partner.PartnerProgramDefinitionDto;
import com.similan.framework.util.EmailValidator;
import com.similan.portal.databean.CountryBean;
import com.similan.portal.view.BaseView;
import com.similan.portal.view.handler.ImageDeletion;
import com.similan.portal.view.handler.ImageUpload;
import com.similan.service.api.community.dto.basic.SocialActorContactDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

@Scope("view")
@Component("orgView")
@Slf4j
public class OrganizationView extends BaseView {

  public static final String IMAGES_BUSINESS_DEFAULT_LOGO = "/images/businessLogo.jpg";
  private static final long serialVersionUID = 1L;

  @Autowired
  private OrganizationDetailInfoDto orgInfo = null;

  private String shareComment;

  private String commentToAdd;

  private List<StateType> selectedStateList = null;

  private List<PartnerProgramDefinitionDto> partnerPrograms = new ArrayList<PartnerProgramDefinitionDto>();

  private List<SocialActorContactDto> contactList = null;

  private List<ExternalBusinessPortalDto> externalPortalList = null;

  private List<ExternalBusinessPortalDto> deleteExternalPortalList = new ArrayList<ExternalBusinessPortalDto>();

  private ExternalBusinessPortalDto externalPortal = new ExternalBusinessPortalDto();

  @PostConstruct
  public void init() {
    log.info("Post init in organization view ");
    CountryBean countryBean = (CountryBean) this.findBackingBean("countryBean");
    this.selectedStateList = countryBean.getStateTypeList("United States");

    partnerPrograms = this.partnerManagementService
        .getPartnerProgramsForBusiness(orgInfo);

    SocialActorKey actorKey = this.getOrgKey(orgInfo);
    contactList = this.getNetworkService().getDirectConnections(actorKey);

    this.externalPortalList = this.getOrgService().getExternalPortals(
        orgInfo.getId());
  }

  public List<ExternalBusinessPortalDto> getExternalPortalList() {
    return externalPortalList;
  }

  public ExternalBusinessPortalDto getExternalPortal() {
    return externalPortal;
  }

  public void setExternalPortal(ExternalBusinessPortalDto externalPortal) {
    this.externalPortal = externalPortal;
  }

  public List<SocialActorContactDto> getContactList() {
    return contactList;
  }

   public void loadStatesForSelectedCountry() {
    CountryBean countryBean = (CountryBean) this.findBackingBean("countryBean");
    this.selectedStateList = countryBean.getStateTypeList(this.orgInfo
        .getSelectedLocation().getCountry());
    log.info("Country " + this.orgInfo.getSelectedLocation().getCountry()
        + " state list " + this.selectedStateList.size() + " content "
        + this.selectedStateList.get(0).getStateName());
  }

  public List<PartnerProgramDefinitionDto> getPartnerPrograms() {
    return partnerPrograms;
  }

  public void setPartnerPrograms(
      List<PartnerProgramDefinitionDto> partnerPrograms) {
    this.partnerPrograms = partnerPrograms;
  }

  public List<StateType> getSelectedStateList() {
    return selectedStateList;
  }

  public void setSelectedStateList(List<StateType> selectedStateList) {
    this.selectedStateList = selectedStateList;
  }

  public String getCommentToAdd() {
    return commentToAdd;
  }

  public void setCommentToAdd(String commentToAdd) {
    this.commentToAdd = commentToAdd;
  }

  public void addExternalPortal() {
    log.info("Adding new portal with name "
        + this.externalPortal.getPortalName() + " with url "
        + this.externalPortal.getPortalUrl());

    this.externalPortalList.add(this.externalPortal);
    this.externalPortal = new ExternalBusinessPortalDto();
  }

  public void deleteExternalPortal(String uuid) {
    log.info("Deleting external portal " + uuid);
    ExternalBusinessPortalDto extPortalDelete = null;

    for (ExternalBusinessPortalDto extPortal : this.externalPortalList) {
      if (extPortal.getUuid().equalsIgnoreCase(uuid)) {

        log.info("Deleting external portal " + extPortal.getUuid());
        extPortalDelete = extPortal;
        this.deleteExternalPortalList.add(extPortal);
      }
    }

    if (extPortalDelete != null) {
      this.externalPortalList.remove(extPortalDelete);
    }

  }

  public void deletePartnerProgram(Long partnerPrgId) {
    log.info("Deleting program " + partnerPrgId);
    FacesContext.getCurrentInstance().addMessage(
        null,
        new FacesMessage(FacesMessage.SEVERITY_WARN, "Message",
            "Deleting a partner program is not supported"));

  }

  /**
   * Adding a location in memory
   */
  public void addLocation() {

    log.info("Adding location to org " + this.getOrgInfo().getBusinessName()
        + " location " + this.getOrgInfo().getSelectedLocation().getStreet());

    OrganizationAddressDto selAddress = this.getOrgInfo().getSelectedLocation();

    if (selAddress.isCopyContactFromPrimary()) {

      selAddress.setLocationEmail(this.getOrgInfo().getBusinessPrimaryEmail());
      // selAddress.setLocationFax(this.getOrgInfo().) NO PRIMARY BUSINESS FAX
      // AVAILABLE
      selAddress.setLocationPhone(this.getOrgInfo().getBusinessPhone());
      selAddress.setLocationUrl(this.getOrgInfo().getHomePageUrl());
    }

    this.orgInfo.getLocations().add(selAddress);
    this.getOrgInfo().setSelectedLocation(new OrganizationAddressDto());
  }

  public void deleteLocation(OrganizationAddressDto addr) {

    log.info("Deleting location to org "
        + this.getOrgInfo().getBusinessName() + " location " + addr.toString());
    this.getOrgInfo().removeLocation(addr);
  }

  public void setSelectedLocation(OrganizationAddressDto addr) {

    log.info("Setting selected location to org "
        + this.getOrgInfo().getBusinessName() + " location " + addr.toString());
    this.getOrgInfo().setSelectedLocation(addr);
  }

  public void setSelectedLocationToNew() {
    this.getOrgInfo().setSelectedLocation(new OrganizationAddressDto());
  }

  public void editLocation() {

    log.info("Edit location to org " + this.getOrgInfo().getBusinessName()
        + " location " + this.getOrgInfo().getSelectedLocation().toString());
    this.getOrgInfo().editLocation();
    this.getOrgInfo().setSelectedLocation(new OrganizationAddressDto());

  }

  public void onRowLocationSelect(SelectEvent event) {
    log.info("Row selection event " + event);
    OrganizationAddressDto selectedLocation = (OrganizationAddressDto) event
        .getObject();

    log.info("Location selected " + selectedLocation.getCountry());
    this.getOrgInfo().setSelectedLocation(selectedLocation);
  }

  public String getShareComment() {
    return shareComment;
  }

  public void setShareComment(String shareComment) {
    this.shareComment = shareComment;
  }

  public OrganizationDetailInfoDto getOrgInfo() {
    return orgInfo;
  }

  public void setOrgInfo(OrganizationDetailInfoDto orgInfo) {
    this.orgInfo = orgInfo;
  }

  public void connect() {
    System.out.println("Org connect .....");
  }

  public void follow() {
    log.info("Org follow .....");
  }

  public void handleBannerUpload(final FileUploadEvent event) {
    this.imageUploadHandler.handleImageUpload(new ImageUpload() {

      public void update() throws Exception {
        orgService.updateOrganization(orgInfo);
      }

      public void setImageKey(String key) {
        orgInfo.setPartnerBannerLocation(key);
      }

      public String getType() {
        return "branding";
      }

      public String getId() {
        return String.valueOf(orgInfo.getId());
      }

      public UploadedFile getFile() {
        return event.getFile();
      }

      public String currentKey() {
        return orgInfo.getPartnerBannerLocation();
      }
    });
  }

  public void handleLogoUpload(final FileUploadEvent event) {
    this.imageUploadHandler.handleImageUpload(new ImageUpload() {

      public void update() throws Exception {
        orgService.updateOrganization(orgInfo);
      }

      public void setImageKey(String key) {
        orgInfo.setLogoLocation(key);
        orgInfo.setLogoViewOptionType(PhotoViewOption.ShowPhoto);
      }

      public String getType() {
        return "business";
      }

      public String getId() {
        return String.valueOf(orgInfo.getId());
      }

      public UploadedFile getFile() {
        return event.getFile();
      }

      public String currentKey() {
        return orgInfo.getLogoLocation();
      }
    });
  }

  public void deleteLogo() {
    this.imageUploadHandler.handleImageDeletion(new ImageDeletion() {

      public String getCurrentKey() {
        return orgInfo.getLogoLocation();
      }

      public void update() throws Exception {
        OrganizationDetailInfoDto temp = orgService.getOrgById(orgInfo.getId());
        temp.setLogoLocation(IMAGES_BUSINESS_DEFAULT_LOGO);
        orgService.updateOrganization(temp);
        orgInfo.setLogoLocation(temp.getLogoLocation());
      }
    });
  }

  public void setBusinessLogo(String photoUrl) {

  }

  public void photoViewOptionSelected(AjaxBehaviorEvent ajaxEvent) {
    log.info("value Selected " + this.orgInfo.getLogoViewOptionType());
  }

  public String getBusinessLogo() {
    return this.orgInfo.getLogoViewOptionType().effectivePhoto(
        IMAGES_BUSINESS_DEFAULT_LOGO, orgInfo.getLogoLocation());
  }

  public boolean isbusinessLogoExists() {
    return this.orgInfo != null
        && StringUtils.isNotBlank(this.orgInfo.getLogoLocation())
        && !StringUtils.equalsIgnoreCase(this.orgInfo.getLogoLocation(),
            IMAGES_BUSINESS_DEFAULT_LOGO);
  }

  public List<String> getSelectedOptions() {
    return this.orgInfo.getSelectedOptions();
  }

  public void setSelectedOptions(List<String> selectedOptions) {
    this.orgInfo.setSelectedOptions(selectedOptions);
  }

  public void editSettings() {
    log.info("Organization settings update ...");
  }

  /**
   * Updates the business profile
   */
  public String updateBusinessProfile() {

    try {

      log.info("Updating Organization profile "
          + this.orgInfo.getBusinessName());
      if (this.orgInfo == null) {
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_WARN, "Update Status",
                "Cannot save a null business entity"));
        return "error";
      }

      if (StringUtils.isEmpty(this.orgInfo.getEmail()) == true) {
        FacesContext
            .getCurrentInstance()
            .addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Update Status",
                    "Cannot save a null business entity without a valid email address"));
        return "error";
      }

      if (EmailValidator.validate(this.orgInfo.getEmail()) != true) {
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Member Update Status",
                "Cannot update business with invalid email"));
        return "error";
      }

      this.orgService.updateOrganization(this.orgInfo);
      this.orgService.deleteExternalPortals(this.deleteExternalPortalList);
      this.orgService.addExternalPortals(this.orgInfo.getId(),
          this.externalPortalList);

      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Update Status",
              "Successfully updated business attributes"));

    } catch (Exception exp) {
      log.error("Failed to update ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Update Status",
              "Failed to update business attributes"));
    }

    return "result";
  }

  public void moreComments() {
    System.out.println("More comments are fetched ...");
  }

  public void commentSubmit() {
    log.info("Comments to be submitted ..." + shareComment + " Rating "
        + this.orgInfo.getNewCommentRating());
  }

  public void followUpdateFilter() {
    log.info("Org followUpdateFilter ...");
  }

  public void followConnectionFilter() {
    log.info("Org followConnectionFilter ...");
  }

  public void addPartnerProgram() {
    facesHelper.redirect("partnerProgramCreate.xhtml");
  }

  public void followGroupFilter() {
    log.info("Org followGroupFilter ...");
  }

  public void shareCommentEveryOne() {
    log.info("Sharing comment " + this.shareComment + " with every one ");
    this.shareComment = "";
  }

  public void shareCommentConnection() {
    log.info("Sharing comment " + this.shareComment
        + " with shareCommentConnection ");
    this.shareComment = "";
  }

  public void shareCommentTweet() {
    log.info("Sharing comment " + this.shareComment
        + " with shareCommentTweet ");
    this.shareComment = "";
  }

  public void deletePartnerProgram() {

    log.info("Partner program delete");

    if (this.orgInfo.getSelectedPartnerProgram() != null) {
      log.info("Deleting Partner program "
          + this.orgInfo.getSelectedPartnerProgram().getId());
    } else {
      log.info("No partner program selected");
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Message",
              "Please select a partner program"));

    }
  }

  public void createPartnerProgram() {
    facesHelper.redirect("/partner/partnerProgramCreateEdit.xhtml");
  }

  public void editPartnerProgram() {

    if (this.orgInfo.getSelectedPartnerProgram() != null) {

      long programId = this.orgInfo.getSelectedPartnerProgram().getId();
      log.info("View Partner program " + programId);
      String retUrl = "/partner/partnerProgramCreateEdit.xhtml?pid="
          + programId;
      facesHelper.redirect(retUrl);
    } else {
      log.info("No partner program selected for edit");
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Message",
              "Please select a partner program first"));

    }
  }

  /**
   * Launches the view screen
   */
  public void viewPartnerProgram() {

    log.info("View Partner program");

    if (this.orgInfo.getSelectedPartnerProgram() != null) {

      long programId = this.orgInfo.getSelectedPartnerProgram().getId();
      log.info("View Partner program " + programId);
      String retUrl = "/partner/partnerProgramView.xhtml?pid=" + programId;
      facesHelper.redirect(retUrl);
    } else {
      log.info("No partner program selected");
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Message",
              "Please select a partner program"));

    }

  }

}
