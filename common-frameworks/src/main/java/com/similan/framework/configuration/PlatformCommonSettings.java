package com.similan.framework.configuration;

import javax.annotation.PostConstruct;

import lombok.Getter;
import lombok.Setter;

import org.springframework.stereotype.Component;

import com.similan.framework.Mutable;


@Component
@Getter @Setter
public class PlatformCommonSettings {
  private Mutable<String> portalApplcationUrl;
  private Mutable<String> platformEmailSenderAddress;
  private Mutable<String> platformSupportEmailAddress;
  private Mutable<String> adminEmailSenderAddress;
  private Mutable<String> rootImageDirectory;
  private Mutable<String> rootDataFileDirectory;
  private Mutable<String> dataDirectory;
  private Mutable<String> superAdminLoginForAdminApp;
  private Mutable<String> superAdminPasswordForAdminApp;
  private Mutable<String> embeddedSearchPageUrl;
  private Mutable<String> embeddedProductPageUrl;
  private Mutable<String> embeddedPartnerPageUrl;
  private Mutable<String> sitemapGenerationFrecuency;
  private Mutable<String> linkedInApiKey;
  private Mutable<String> linkedInApiSecretKey;
  private Mutable<String> dropboxApiKey;
  private Mutable<String> dropboxApiSecretKey;
  private Mutable<String> gDriveApiKey;
  private Mutable<String> gDriveApiSecretKey;
  private Mutable<String> googleDeveloperApiKey;
  private Mutable<String> googleDeveloperClientId;
  private Mutable<String> googleDeveloperClientSecret;
  private Mutable<String> twitterConsumerKey;
  private Mutable<String> twitterConsumerSecret;

  @PostConstruct
  public void initialize() {
    if (portalApplcationUrl == null || portalApplcationUrl.getValue() == null) {
      throw new IllegalStateException("Portal app URL must be set.");
    }
  }

}
