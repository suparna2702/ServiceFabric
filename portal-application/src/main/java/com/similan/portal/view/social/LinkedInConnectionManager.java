package com.similan.portal.view.social;

import java.util.List;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.network.LinkedInConnection;
import com.similan.portal.view.BaseView;

@Scope("session")
@Component("linkedInConnectionmanager")
@Slf4j
public class LinkedInConnectionManager extends BaseView {

  private static final long serialVersionUID = 1L;

  private Token linkedInRequestToken = null;
  private OAuthService oauthService;
  private Token accessToken = null;

  @PostConstruct
  public void init() {
    log.info("Initializing linkedin connection manager");
  }

  public String getLinkedInAuthorizationUrl(String callbackUrl) {

    oauthService = new ServiceBuilder().provider(LinkedInApi.class)
        .apiKey(platformCommonSettings.getLinkedInApiKey().getValue())
        .apiSecret(platformCommonSettings.getLinkedInApiSecretKey().getValue())
        .callback(callbackUrl).build();

    linkedInRequestToken = oauthService.getRequestToken();
    log.info("Linkedin auth token " + linkedInRequestToken);

    String linkedInAuthUrl = oauthService
        .getAuthorizationUrl(linkedInRequestToken);
    log.info("Linkedin auth URL " + linkedInAuthUrl);

    return linkedInAuthUrl;
  }

  public void performAuthentication(String oauthVerifier) {
    if (linkedInRequestToken == null)
      throw new NullPointerException("linkedInRequestToken");

    Verifier verifier = new Verifier(oauthVerifier);
    accessToken = oauthService.getAccessToken(linkedInRequestToken, verifier);

  }

  public MemberInfoDto updateMemberInfoFromLinkedInProfileData(
      MemberInfoDto data) throws Exception {
    if (accessToken == null)
      throw new Exception(
          "Cannot perform update because you are not signed into LinkedIn");

    return socialService.mergeMemberWithLinkedInData(data,
        accessToken.getToken(), accessToken.getSecret());

  }

  public List<LinkedInConnection> getLinkedInConnections() throws Exception {
    if (accessToken == null)
      throw new Exception(
          "Cannot get connections because you are not signed into LinkedIn");

    return socialService.getLinkedInConnections(accessToken.getToken(),
        accessToken.getSecret());

  }

  public void inviteLinkedInInitiate(List<LinkedInConnection> invitees,
      MemberInfoDto inviter) throws Exception {

    if (accessToken == null)
      throw new Exception(
          "Cannot get connections because you are not signed into LinkedIn");

    socialService.inviteLinkedInInitiate(invitees, inviter,
        accessToken.getToken(), accessToken.getSecret());

  }
}
