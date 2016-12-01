package com.similan.adminapp.view;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;

@ViewScoped
@ManagedBean(name = "emailNotificationView")
@Slf4j
public class EmailNotificationView extends BaseAdminView {

  private static final long serialVersionUID = 1L;

  private String emailSubject = StringUtils.EMPTY;

  private String emailContent = StringUtils.EMPTY;

  @PostConstruct
  public void init() {
    log.info("Post init in EmailNotificationView view");

  }

  public void sendEmailToAllMembers() {
    try {
      log.info("Sending emails to every body " + this.emailContent
          + this.emailSubject + " email header " + this.emailSubject);
      
      if (StringUtils.isBlank(emailSubject)) {
        emailSubject = "Notification from BusinessReach Admin";
      }

      this.getMemberAdminService().sendEmailToAllMembers(emailSubject,
          emailContent);

      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
              "Successfully sent message "));

    } catch (Exception exp) {
      log.error("Error sending email ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure",
              "Failure to send email due to " + exp.getMessage()));
    }

    emailContent = StringUtils.EMPTY;
    emailSubject = StringUtils.EMPTY;
  }

  public String getEmailSubject() {
    return emailSubject;
  }

  public void setEmailSubject(String emailSubject) {
    this.emailSubject = emailSubject;
  }

  public String getEmailContent() {
    return emailContent;
  }

  public void setEmailContent(String emailContent) {
    this.emailContent = emailContent;
  }

}
