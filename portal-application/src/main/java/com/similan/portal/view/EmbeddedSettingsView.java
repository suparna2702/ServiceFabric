package com.similan.portal.view;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

@Scope("view")
@Component("embeddedSettingsView")
@Slf4j
public class EmbeddedSettingsView extends BaseView {

  private static final long serialVersionUID = 1L;

  @Autowired
  private OrganizationDetailInfoDto business = null;

  private SocialActorKey businessKey = null;

  private String embeddedLoginUrl = StringUtils.EMPTY;

  @PostConstruct
  public void init() {
    log.info("Initializing view for Business " + business.getId());
    this.businessKey = this.getSocialActorService().transitional_getKey(
        business.getId());

    embeddedLoginUrl = "partner/partnerBrandedLogin.xhtml?oid="
        + this.businessKey.getName();

    log.info("Embedded Login Url for Partners " + embeddedLoginUrl);
  }

  public String getEmbeddedLoginUrl() {
    return embeddedLoginUrl;
  }

}
