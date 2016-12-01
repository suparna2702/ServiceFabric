package com.similan.portal.view.business;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.community.ExternalBusinessPortalDto;
import com.similan.portal.view.BaseView;
import com.similan.service.api.community.dto.key.SocialActorKey;

@Scope("view")
@Component("brandedBusinessView")
@Slf4j
public class BrandedBusinessView extends BaseView {

  private static final long serialVersionUID = 1L;

  private OrganizationDetailInfoDto business = null;

  private List<ExternalBusinessPortalDto> externalPortalList = null;

  @PostConstruct
  public void init() {
    String businessKeyName = this.getContextParam("oid");
    log.info("Business key requested " + businessKeyName);

    SocialActorKey businessKey = new SocialActorKey(businessKeyName);
    Long businessId = this.getSocialActorService().transitional_getId(
        businessKey);

    try {
      
      business = this.getOrgService().getOrgById(businessId);
      log.info("Found Business with key " + businessKeyName + " id "
          + businessId + " name " + business.getBusinessName());

      this.externalPortalList = this.getOrgService().getExternalPortals(
          business.getId());

    } catch (Exception exp) {
      log.error("Cannot fetch business", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Error getting business with Id " + businessKeyName));
    }

  }

  public String getBrandLogo() {
    return "/images/brandlogo/voxoxlogo.png";
  }

  public String getBusinessHomeUrl() {
    String url = "www.businessreach.com";
    if (StringUtils.isBlank(business.getHomePageUrl()) != true) {
      url = business.getHomePageUrl();
    }

    log.info("Business Home Page URL : " + url);
    return url;
  }

  public String getBusinessPartnerBanner() {
    return "/images/brandlogo/voxoxbanner.png";
  }

  public String getBusinessLogo() {
    return this.business.getLogoViewOptionType().effectivePhoto(
        IMAGES_PRODUCT_DEFAULT_LOGO, business.getLogoLocation());
  }

  public OrganizationDetailInfoDto getBusiness() {
    return business;
  }

  public List<ExternalBusinessPortalDto> getExternalPortalList() {
    return externalPortalList;
  }

  public void setExternalPortalList(
      List<ExternalBusinessPortalDto> externalPortalList) {
    this.externalPortalList = externalPortalList;
  }

}
