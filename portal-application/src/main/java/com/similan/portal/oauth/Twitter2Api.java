package com.similan.portal.oauth;

import org.scribe.model.OAuthConfig;

public class Twitter2Api extends BaseOAuth2ScriveApi {
  private static final String ACCESSTOKEN_ENDPOINT_URL = "https://api.twitter.com/oauth/access_token";
  private static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";

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
