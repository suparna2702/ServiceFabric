package com.similan.portal.oauth;

import lombok.extern.slf4j.Slf4j;

import org.scribe.model.OAuthConfig;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

@Slf4j
public class Google2Api extends BaseOAuth2ScriveApi {
  private static final String AUTHORIZE_URL = "https://accounts.google.com/o/oauth2/auth?client_id=%s&redirect_uri=%s&response_type=code";
  private static final String SCOPED_AUTHORIZE_URL = String.format(
      "%s&scope=%%s", AUTHORIZE_URL);
  private static final String ACCESSTOKEN_ENDPOINT_URL = "https://accounts.google.com/o/oauth2/token";

  @Override
  public String getAccessTokenEndpoint() {
    return ACCESSTOKEN_ENDPOINT_URL;
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config) {
    Preconditions.checkValidUrl(config.getCallback(),
        "Valid url is required for a callback");
    if (config.hasScope()) {
      String retUrl = String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(),
          OAuthEncoder.encode(config.getCallback()),
          OAuthEncoder.encode(config.getScope()));
      log.info("Scoped url " + retUrl);
      return retUrl;
    } else {
      String retUrl = String.format(AUTHORIZE_URL, config.getApiKey(),
          OAuthEncoder.encode(config.getCallback()));
      log.info("UnScoped url " + retUrl);
      return retUrl;
    }
  }

}
