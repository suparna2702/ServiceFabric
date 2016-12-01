package com.similan.portal.view.externalStorage;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.client.utils.URIBuilder;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.similan.framework.dto.fileImport.CloudImportFileDto;
import com.similan.portal.view.BaseView;
import com.similan.service.api.externalservice.dto.ExternalServiceType;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;

@Slf4j
@Getter
@Setter
public abstract class ExternalContentManager extends BaseView {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private Token accessToken = null;
  private Token requestToken = null;
  private OAuthService oauthService = null;
  private String oauthToken = null;
  private boolean oauthOthenticationInitiated = false;

  public void startAuthentication(ManagementWorkspaceKey managementSpaceKey) {
    try {
      HttpServletRequest request = (HttpServletRequest) facesHelper
          .getExternalContext().getRequest();

      // save the management key in session
      FacesContext context = FacesContext.getCurrentInstance();
      context.getExternalContext().getSessionMap()
          .put("msname", managementSpaceKey.getName());

      URI uri = new URIBuilder(request.getRequestURL().toString()).build();

      String origRequest = uri.toString();
      log.info("Callback URL: " + origRequest);

      String targetUrl = getAuthorizationUrl(origRequest);

      log.info("Target URL: " + targetUrl);
      this.oauthOthenticationInitiated = true;

      FacesContext.getCurrentInstance().getExternalContext()
          .redirect(targetUrl);

    } catch (Exception e) {
      log.error("Error calling YouTube API ", e);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Error calling Youtube " + e.getMessage()));
    }
  }

  public boolean isConnected() {
    if (accessToken != null) {
      return true;
    } else {
      return false;
    }

  }

  public void performAuthentication(String oauthVerifier) {

    Verifier verifier = new Verifier(oauthVerifier);
    this.accessToken = oauthService.getAccessToken(requestToken, verifier);
    this.oauthOthenticationInitiated = false;
    log.debug("Access Token " + this.accessToken + " OAuth verifier "
        + oauthVerifier);

  }

  public String getAuthorizationUrl(String callbackUrl) {

    this.oauthService = this.getAuthorizationService(callbackUrl);
    String authorizationUrl = this.oauthService
        .getAuthorizationUrl(this.requestToken);
    log.debug("Authorization URL " + authorizationUrl);

    return authorizationUrl;
  }

  public void finishAuthentication() {
    log.debug("finishAuthentication called with token " + oauthToken);
    performAuthentication(oauthToken);
  }

  public abstract OAuthService getAuthorizationService(String callbackUrl);

  public abstract List<CloudImportFileDto> getRootFiles()
      throws URISyntaxException;

  public abstract List<CloudImportFileDto> getFilesForFolder(
      CloudImportFileDto data);

  public abstract void downloadFile(CloudImportFileDto data) throws Exception;

  public abstract ExternalServiceType getServiceType();

}