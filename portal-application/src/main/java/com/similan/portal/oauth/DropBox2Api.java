package com.similan.portal.oauth;

import lombok.extern.slf4j.Slf4j;

import org.scribe.model.OAuthConfig;

@Slf4j
public class DropBox2Api extends BaseOAuth2ScriveApi {
  private static final String AUTHORIZE_URL = "https://www.dropbox.com/1/oauth2/authorize?client_id=%s&response_type=code&redirect_uri=%s";
  private static final String ACCESSTOKEN_ENDPOINT_URL = "https://api.dropbox.com/1/oauth2/token?grant_type=authorization_code";

  @Override
  public String getAccessTokenEndpoint() {
    return ACCESSTOKEN_ENDPOINT_URL;
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config) {
    return String.format(AUTHORIZE_URL, config.getApiKey(),
        config.getCallback());
  }

}
