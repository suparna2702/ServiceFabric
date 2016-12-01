package com.similan.portal.view.invite;

import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.network.LinkedInConnection;
import com.similan.portal.model.LinkedInConnectionSelectionModel;
import com.similan.portal.view.BaseView;
import com.similan.portal.view.social.LinkedInConnectionManager;

@Scope("view")
@Component("linkedInMemberInviteView")
@Slf4j
public class LinkedInMemberInviteView extends BaseView {

  private static final long serialVersionUID = 1L;

  @Autowired
  private MemberInfoDto memberInfo;

  private String oauthVerifier = null;

  private List<LinkedInConnection> networkConnections = null;
  private LinkedInConnectionSelectionModel selectableNetworkConnections = null;
  private LinkedInConnection[] selectedNetworkConnections = null;
  private boolean linkedInUpdatePerformed = false;

  @PostConstruct
  public void init() {
    log.info("Importing Linkedin Contacts ");

  }

  public void importLinkedInContacts() {
    log.info("Importing Linkedin Contacts ");
    try {
      linkedInUpdatePerformed = false;

      FacesContext facesContext = FacesContext.getCurrentInstance();
      LinkedInConnectionManager linkedInConnectionManager = (LinkedInConnectionManager) facesContext
          .getApplication().evaluateExpressionGet(facesContext,
              "#{linkedInConnectionmanager}", LinkedInConnectionManager.class);

      HttpServletRequest request = (HttpServletRequest) facesHelper
          .getExternalContext().getRequest();

      URL url = new URL(request.getRequestURL().toString());
      URL newUrl = new URL(url.getProtocol(), url.getHost(), url.getPort(),
          request.getRequestURI());

      String origRequest = newUrl.toString();

      log.info("Callback set to: " + origRequest);
      String targetUrl = linkedInConnectionManager
          .getLinkedInAuthorizationUrl(origRequest);
      ExternalContext externalContext = facesHelper.getExternalContext();
      externalContext.redirect(targetUrl);

    } catch (Exception e) {
      log.error("Linkedin error", e);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Error importing linkedin contacts " + e.getMessage()));
    }

  }

  public void clearlInviteLinkedInContacts() {
    log.info("Clearing linkedin connections");
    if (networkConnections != null) {
      networkConnections.clear();
    }
  }

  public void handleReturnFromLinkedIn() {
    try {
      if (oauthVerifier == null || linkedInUpdatePerformed == true) {
        return;
      }
      log.info("Handling linkedIn callback");
      FacesContext facesContext = FacesContext.getCurrentInstance();
      LinkedInConnectionManager linkedInConnectionManager = (LinkedInConnectionManager) facesContext
          .getApplication().evaluateExpressionGet(facesContext,
              "#{linkedInConnectionmanager}", LinkedInConnectionManager.class);
      linkedInConnectionManager.performAuthentication(oauthVerifier);

      networkConnections = linkedInConnectionManager.getLinkedInConnections();

      Collections.sort(networkConnections,
          new Comparator<LinkedInConnection>() {

            public int compare(LinkedInConnection connection1,
                LinkedInConnection connection2) {
              int sort = connection1.getLastName().compareToIgnoreCase(
                  connection2.getLastName());
              if (sort != 0)
                return sort;
              else
                return connection1.getFirstName().compareToIgnoreCase(
                    connection2.getFirstName());
            }
          });

      // populate selectable data model
      this.selectableNetworkConnections = new LinkedInConnectionSelectionModel(
          networkConnections);

    } catch (Exception e) {
      log.error("Linkedin error", e);

      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Cannot connect to Linkedin due to " + e.getMessage()));

    }
    linkedInUpdatePerformed = true;
  }

  public void inviteNetworkConnections() {
    try {

      if (this.selectableNetworkConnections == null
          || selectableNetworkConnections.getWrappedData() == null) {
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning",
                "No connection selected"));
      }

      @SuppressWarnings("unchecked")
      List<LinkedInConnection> connectionList = (List<LinkedInConnection>) selectableNetworkConnections
          .getWrappedData();

      log.info("Inviting Connections for " + connectionList.size());
      FacesContext facesContext = FacesContext.getCurrentInstance();
      LinkedInConnectionManager linkedInConnectionManager = (LinkedInConnectionManager) facesContext
          .getApplication().evaluateExpressionGet(facesContext,
              "#{linkedInConnectionmanager}", LinkedInConnectionManager.class);

      linkedInConnectionManager.inviteLinkedInInitiate(connectionList,
          memberInfo);
      networkConnections = null;

      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
              "Your connections have been sent an invitation"));

    } catch (Exception e) {
      log.error("Error while performing invitation", e);
      FacesContext.getCurrentInstance()
          .addMessage(
              null,
              new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e
                  .getMessage()));

    }
  }

  public boolean isLinkedInUpdatePerformed() {
    return linkedInUpdatePerformed;
  }

  public void setLinkedInUpdatePerformed(boolean linkedInUpdatePerformed) {
    this.linkedInUpdatePerformed = linkedInUpdatePerformed;
  }

  public String getOauthVerifier() {
    return oauthVerifier;
  }

  public void setOauthVerifier(String oauthVerifier) {
    this.oauthVerifier = oauthVerifier;
  }

  public LinkedInConnectionSelectionModel getSelectableNetworkConnections() {
    return selectableNetworkConnections;
  }

  public void setSelectableNetworkConnections(
      LinkedInConnectionSelectionModel selectableNetworkConnections) {
    this.selectableNetworkConnections = selectableNetworkConnections;
  }

  public LinkedInConnection[] getSelectedNetworkConnections() {
    return selectedNetworkConnections;
  }

  public void setSelectedNetworkConnections(
      LinkedInConnection[] selectedNetworkConnections) {
    this.selectedNetworkConnections = selectedNetworkConnections;
  }

  public List<LinkedInConnection> getNetworkConnections() {
    return networkConnections;
  }

  public void setNetworkConnections(List<LinkedInConnection> networkConnections) {
    this.networkConnections = networkConnections;
  }

}
