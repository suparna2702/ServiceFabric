package com.similan.portal.view.business;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.UpdateBusinessBrandingDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;
import com.similan.portal.view.handler.ImageUpload;

@Scope("view")
@Component("businessBrandingView")
@Slf4j
public class BusinessBrandingView extends BaseView {

  private static final long serialVersionUID = 1L;

  @Autowired(required = true)
  private MemberInfoDto member = null;

  @Autowired
  private OrganizationDetailInfoDto orgInfo;

  private String partnerWelcomeMessage;

  private String partnerBannerText;

  private String partnerBannerLocation;

  private String partnerBannerTextColor;

  @PostConstruct
  public void init() {
    this.partnerBannerText = orgInfo.getPartnerBannerText();
    this.partnerWelcomeMessage = orgInfo.getPartnerWelcomeMessage();
    this.partnerBannerLocation = orgInfo.getPartnerBannerLocation();

    if (StringUtils.isBlank(this.partnerBannerLocation)) {
      this.partnerBannerLocation = "#FFFFFF";
    }

    log.info("Getting org brand info for " + orgInfo.getId()
        + " banner text " + this.partnerBannerText + " banner location "
        + this.partnerBannerLocation + " Banner text color "
        + this.partnerBannerTextColor);
  }

  public String getPartnerBannerTextColor() {
    return partnerBannerTextColor;
  }

  public void setPartnerBannerTextColor(String partnerBannerTextColor) {
    this.partnerBannerTextColor = partnerBannerTextColor;
  }

  public String getPartnerWelcomeMessage() {
    return partnerWelcomeMessage;
  }

  public void setPartnerWelcomeMessage(String partnerWelcomeMessage) {
    this.partnerWelcomeMessage = partnerWelcomeMessage;
  }

  public String getPartnerBannerText() {
    return partnerBannerText;
  }

  public void setPartnerBannerText(String partnerBannerText) {
    this.partnerBannerText = partnerBannerText;
  }

  public String getPartnerBannerLocation() {
    if (StringUtils.isBlank(partnerBannerLocation)) {
      return IMAGES_DEFAULT_PARTNER_BANNER;
    }
    return partnerBannerLocation;
  }
  
  public String getBrandLogo(){
    return "/images/brandlogo/voxoxlogo.png";
  }

  public void setPartnerBannerLocation(String partnerBannerLocation) {
    this.partnerBannerLocation = partnerBannerLocation;
  }

  public void handleBannerLogoUpload(final FileUploadEvent event) {
    log.info("File logo " + event.getFile().getFileName());
  }

  public void handleBannerUpload(final FileUploadEvent event) {
    this.imageUploadHandler.handleImageUpload(new ImageUpload() {

      public void update() throws Exception {
        log.info("No updates now ");
      }

      public void setImageKey(String key) {
        partnerBannerLocation = key;
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

  public void clearBanner() {
    partnerBannerLocation = StringUtils.EMPTY;
  }

  public String updateBusinessBrandingProfile() {
    UpdateBusinessBrandingDto branding = new UpdateBusinessBrandingDto(
        orgInfo.getId(), partnerWelcomeMessage, partnerBannerText,
        partnerBannerLocation, partnerBannerTextColor);
    orgService.updateBranding(branding);

    return "result";
  }

}
