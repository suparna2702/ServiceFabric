package com.similan.portal.view.advertisement;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;
import com.similan.portal.view.handler.ImageUpload;
import com.similan.service.api.advertisement.dto.basic.DisplayNoticeDto;
import com.similan.service.api.advertisement.dto.key.DisplayNoticeKey;
import com.similan.service.api.advertisement.dto.operation.DisplayNoticeLandingPageDto;
import com.similan.service.api.advertisement.dto.operation.NewDisplayNoticeDto;
import com.similan.service.api.advertisement.dto.operation.UpdateDisplayNoticeDto;

@Scope("view")
@Component("displayNoticeCreateEditView")
@Slf4j
@Getter
@Setter
public class DisplayNoticeCreateEditView extends BaseView {

  private static final long serialVersionUID = 1L;

  @Autowired(required = true)
  private MemberInfoDto member = null;

  @Autowired
  private OrganizationDetailInfoDto orgInfo = null;

  private List<DisplayNoticeDto> displayNoticeList = new ArrayList<DisplayNoticeDto>();

  private String name = StringUtils.EMPTY;

  private String iconAsset = IMAGES_DEFAULT_PARTNER_BANNER;

  private String uuid = UUID.randomUUID().toString();

  private String url = StringUtils.EMPTY;

  private String content = StringUtils.EMPTY;

  private DisplayNoticeLandingPageDto tobeEditedLandingPage = null;

  private DisplayNoticeDto tobeEdited = null;

  private boolean externalUrl = false;

  @PostConstruct
  public void init() {
    log.info("Display notice view");
    this.displayNoticeList = this.getDisplayNoticeService().get(
        this.getOrgKey(orgInfo));
  }

  public List<DisplayNoticeDto> getDisplayNoticeList() {
    return displayNoticeList;
  }

  public void deActivate(DisplayNoticeKey key) {
    log.info("De Activating Display notice " + key);
    this.getDisplayNoticeService().deActivate(key, this.getOrgKey(orgInfo),
        this.getMemberKey(member));
  }

  public void activate(DisplayNoticeKey key) {
    log.info("Activating Display notice " + key);
    this.getDisplayNoticeService().activate(key, this.getOrgKey(orgInfo),
        this.getMemberKey(member));
  }

  public void editDisplayNotice(DisplayNoticeKey noticeKey) {

    this.tobeEdited = this.getDisplayNoticeService().get(noticeKey);
    DisplayNoticeLandingPageDto tobeEditedLandingPage = this
        .getDisplayNoticeService().getLandingPage(noticeKey);

    this.uuid = tobeEdited.getUuid();
    this.iconAsset = tobeEdited.getIconAsset();
    this.name = tobeEdited.getName();
    this.content = tobeEditedLandingPage.getText();
  }

  public void viewLandingPage(DisplayNoticeKey key) {
    log.info("View landing page for " + key);

    try {
      FacesContext
          .getCurrentInstance()
          .getExternalContext()
          .redirect(
              "/advertisement/displayNoticeLandingPageView.xhtml?page="
                  + key.getId());
    } catch (Exception exp) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Redirection failed "));
      return;
    }
  }

  public void handleAssetIconload(final FileUploadEvent event) {
    String result = this.imageUploadHandler
        .handleImageUpload(new ImageUpload() {

          public void update() throws Exception {
            log.info("We dont do anything now");
          }

          public void setImageKey(String key) {
            iconAsset = key;
          }

          public String getType() {
            return "display-notice";
          }

          public String getId() {
            return String.valueOf(uuid);
          }

          public UploadedFile getFile() {
            return event.getFile();
          }

          public String currentKey() {
            log.info("Current key " + iconAsset);
            return iconAsset;
          }
        });

    if (result != null) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "Error uploading image", result));
    }
  }

  public void update() {
    if (log.isDebugEnabled()) {
      log.debug("Updating current display notice " + this.tobeEdited.getUuid());
    }

    if (this.tobeEdited == null) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "No display notice selected to be edited "));
      return;
    }

    if (StringUtils.isBlank(this.name)) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Name cannot be empty "));
      return;
    }

    if (StringUtils.isBlank(this.content)) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Name cannot be empty "));
      return;
    }

    if (StringUtils.isBlank(this.iconAsset)) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Select an asset image "));
      return;
    }

    UpdateDisplayNoticeDto updateNotice = new UpdateDisplayNoticeDto();
    updateNotice.setName(name);
    updateNotice.setText(content);
    updateNotice.setIconAsset(iconAsset);

    DisplayNoticeDto updatedNotice = this.getDisplayNoticeService().update(
        this.tobeEdited.getKey(), updateNotice);

    // replace the notice
    for (DisplayNoticeDto notice : displayNoticeList) {
      if (notice.getUuid().contentEquals(updatedNotice.getUuid())) {
        displayNoticeList.remove(notice);
        displayNoticeList.add(updatedNotice);
        break;
      }
    }
  }

  public void create() {
    log.info("Creating new Display notice ");

    if (StringUtils.isBlank(this.name)) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Name cannot be empty "));
      return;
    }

    if (StringUtils.isBlank(this.iconAsset)) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Select an asset image "));
      return;
    }

    if (StringUtils.isBlank(this.content) && StringUtils.isBlank(this.url)) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Provide either content or external url "));
      return;
    }

    NewDisplayNoticeDto newDisplayNotice = new NewDisplayNoticeDto();
    newDisplayNotice.setIconAsset(iconAsset);
    newDisplayNotice.setName(name);
    newDisplayNotice.setText(content);
    newDisplayNotice.setUrl(url);
    newDisplayNotice.setOwner(this.getOrgKey(orgInfo));
    newDisplayNotice.setCreator(this.getMemberKey(member));
    newDisplayNotice.setUuid(uuid);

    log.info("Creating new display notice " + newDisplayNotice);
    DisplayNoticeDto newNotice = this.getDisplayNoticeService().create(
        newDisplayNotice);
    this.displayNoticeList.add(newNotice);

    FacesContext.getCurrentInstance().addMessage(
        null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
            "Banner notice has been saved."));

    name = StringUtils.EMPTY;
    iconAsset = IMAGES_DEFAULT_PARTNER_BANNER;
    url = StringUtils.EMPTY;
    content = StringUtils.EMPTY;
    externalUrl = false;
    uuid = UUID.randomUUID().toString();

  }

}
