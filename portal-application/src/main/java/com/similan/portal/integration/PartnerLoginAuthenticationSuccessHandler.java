package com.similan.portal.integration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Slf4j
public class PartnerLoginAuthenticationSuccessHandler extends
    SavedRequestAwareAuthenticationSuccessHandler {


  @Override
  public void onAuthenticationSuccess(HttpServletRequest request,
      HttpServletResponse response, Authentication authentication)
      throws ServletException, IOException {

    log.info("The authentication information "
        + "authentication target url " + this.getDefaultTargetUrl()
        + " alwaysUseDefaultTargetUrl " + this.isAlwaysUseDefaultTargetUrl()
        + " target url " + this.getTargetUrlParameter());
    
    this.setDefaultTargetUrl("/collabspace/workspaceMain.xhtml");
    this.setTargetUrlParameter("/collabspace/workspaceMain.xhtml");
    
    super.onAuthenticationSuccess(request, response, authentication);
  }

}
