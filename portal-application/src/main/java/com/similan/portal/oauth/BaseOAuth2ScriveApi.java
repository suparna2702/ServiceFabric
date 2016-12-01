package com.similan.portal.oauth;

import lombok.extern.slf4j.Slf4j;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuth20ServiceImpl;
import org.scribe.oauth.OAuthService;

@Slf4j
public abstract class BaseOAuth2ScriveApi extends DefaultApi20 {

  @Override
  public AccessTokenExtractor getAccessTokenExtractor() {
    return new JsonAccessTokenExtractor();
  }

  @Override
  public Verb getAccessTokenVerb() {
    return Verb.POST;
  }

  @Override
  public OAuthService createService(final OAuthConfig config) {
    log.info("OAuth 2 config for google " + config.toString());

    // Google OAuth uses POST requests and thus requires more hacks
    return new OAuth20ServiceImpl(this, config) {
      @Override
      public Token getAccessToken(Token requestToken, Verifier verifier) {
        OAuthRequest request = new OAuthRequest(getAccessTokenVerb(),
            getAccessTokenEndpoint());
        request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        request.addBodyParameter(OAuthConstants.CLIENT_SECRET,
            config.getApiSecret());
        request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
        request.addBodyParameter(OAuthConstants.REDIRECT_URI,
            config.getCallback());
        request.addBodyParameter("grant_type", "authorization_code");

        if (config.hasScope()) {
          request.addBodyParameter(OAuthConstants.SCOPE, config.getScope());
        }

        Response response = request.send();
        return getAccessTokenExtractor().extract(response.getBody());
      }
    };
  }

}
