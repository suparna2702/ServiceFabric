package com.similan.framework.dto;

import java.io.Serializable;

public class UpdateBusinessBrandingDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  private String partnerWelcomeMessage;

  private String partnerBannerText;

  private String partnerBannerLocation;

  private String partnerBannerTextColor;

  public UpdateBusinessBrandingDto(Long id, String partnerWelcomeMessage,
      String partnerBannerText, String partnerBannerLocation,
      String partnerBannerTextColor) {
    this.id = id;
    this.partnerWelcomeMessage = partnerWelcomeMessage;
    this.partnerBannerText = partnerBannerText;
    this.partnerBannerLocation = partnerBannerLocation;
    this.partnerBannerTextColor = partnerBannerTextColor;
  }

  public String getPartnerBannerTextColor() {
    return partnerBannerTextColor;
  }

  public Long getId() {
    return id;
  }

  public String getPartnerWelcomeMessage() {
    return partnerWelcomeMessage;
  }

  public String getPartnerBannerText() {
    return partnerBannerText;
  }

  public String getPartnerBannerLocation() {
    return partnerBannerLocation;
  }

}
