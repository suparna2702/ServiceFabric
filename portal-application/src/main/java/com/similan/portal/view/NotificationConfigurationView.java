package com.similan.portal.view;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.EmployeeRole;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.settings.dto.CollaborationWorkspaceNotificationConfigurationDto;
import com.similan.service.api.settings.dto.ContentWorkspaceNotificationConfigurationDto;
import com.similan.service.api.settings.dto.PartnerProgramNotificationConfigurationDto;
import com.similan.service.api.settings.dto.ProfileNotificationConfigurationDto;

@Scope("view")
@Component("notificationConfigurationView")
@Slf4j
@Getter
@Setter
public class NotificationConfigurationView extends BaseView {

  private static final long serialVersionUID = 1L;

  @Autowired(required = true)
  private MemberInfoDto member = null;

  @Autowired
  private OrganizationDetailInfoDto orgInfo;

  private SocialActorKey memberKey = null;

  private SocialActorKey organizationKey = null;

  private Boolean businessAdmin = Boolean.FALSE;

  private ProfileNotificationConfigurationDto profileConfig = null;

  private CollaborationWorkspaceNotificationConfigurationDto collabWorkspaceConfig = null;

  private ContentWorkspaceNotificationConfigurationDto contentWorkspaceConfig = null;

  private PartnerProgramNotificationConfigurationDto partnerProgramConfig = null;

  // for business wide settings

  private ProfileNotificationConfigurationDto profileBusinessConfig = null;

  private CollaborationWorkspaceNotificationConfigurationDto collabWorkspaceBusinessConfig = null;

  private ContentWorkspaceNotificationConfigurationDto contentWorkspaceBusinessConfig = null;

  private PartnerProgramNotificationConfigurationDto partnerProgramBusinessConfig = null;

  @PostConstruct
  public void init() {
    log.info("Post init notification configuration view for member "
        + member.getId());

    this.memberKey = this.getMemberKey(member);

    this.profileConfig = this.getNotificationConfigurationService()
        .getProfileConfig(memberKey);

    this.collabWorkspaceConfig = this.getNotificationConfigurationService()
        .getCollabWorkspaceConfig(memberKey);

    this.contentWorkspaceConfig = this.getNotificationConfigurationService()
        .getContentWorkspaceConfig(memberKey);

    this.partnerProgramConfig = this.getNotificationConfigurationService()
        .getPartnerProgramConfig(memberKey);

    if (orgInfo != null) {
      organizationKey = this.getOrgKey(orgInfo);
      this.profileBusinessConfig = this.getNotificationConfigurationService()
          .getProfileConfig(organizationKey);

      this.collabWorkspaceBusinessConfig = this
          .getNotificationConfigurationService().getCollabWorkspaceConfig(
              organizationKey);

      this.contentWorkspaceBusinessConfig = this
          .getNotificationConfigurationService().getContentWorkspaceConfig(
              organizationKey);

      this.partnerProgramBusinessConfig = this
          .getNotificationConfigurationService().getPartnerProgramConfig(
              organizationKey);

      Set<EmployeeRole> roles = this.getMemberService().getMemberRoles(member,
          orgInfo);
      if (roles != null) {
        for (EmployeeRole role : roles) {
          if (role.equals(EmployeeRole.BUSINESS_ADMIN)
              || role.equals(EmployeeRole.COLLABORATION_WORKSPACE_ADMIN)) {
            businessAdmin = Boolean.TRUE;
          }
        }
      }

      log.info("Admin value " + businessAdmin);

    }
  }

  public void saveProfileConfig() {
    log.info("Saving profile config " + this.profileConfig);
    this.getNotificationConfigurationService().update(this.profileConfig,
        memberKey);

    FacesContext.getCurrentInstance().addMessage(
        null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
            "Successfully updated profile notification configuration"));
  }

  public void saveCollabWorkspaceConfig() {
    log.info("Saving collab workspace config " + this.collabWorkspaceConfig);
    this.getNotificationConfigurationService().update(collabWorkspaceConfig,
        memberKey);

    FacesContext.getCurrentInstance().addMessage(
        null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
            "Successfully updated collaboration workspace"
                + " notification configuration"));
  }

  public void saveContentWorkspaceConfig() {
    log.info("Saving content workspace config " + this.contentWorkspaceConfig);
    this.getNotificationConfigurationService().update(contentWorkspaceConfig,
        memberKey);

    FacesContext.getCurrentInstance().addMessage(
        null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
            "Successfully updated content workspace"
                + " notification configuration"));
  }

  public void savePartnerProgramConfig() {
    log.info("Saving partner config " + this.partnerProgramConfig);
    this.getNotificationConfigurationService().update(partnerProgramConfig,
        memberKey);

    FacesContext.getCurrentInstance().addMessage(
        null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
            "Successfully updated partner program"
                + " notification configuration"));
  }

  public void saveProfileBusinessConfig() {
    log.info("Saving profile config " + this.profileBusinessConfig);
    this.getNotificationConfigurationService().update(
        this.profileBusinessConfig, organizationKey);

    FacesContext.getCurrentInstance().addMessage(
        null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
            "Successfully updated profile notification configuration"));
  }

  public void saveCollabWorkspaceBusinessConfig() {
    log.info("Saving collab workspace config "
        + this.collabWorkspaceBusinessConfig);
    this.getNotificationConfigurationService().update(
        collabWorkspaceBusinessConfig, organizationKey);

    FacesContext.getCurrentInstance().addMessage(
        null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
            "Successfully updated collaboration workspace"
                + " notification configuration"));
  }

  public void saveContentWorkspaceBusinessConfig() {
    log.info("Saving content workspace config "
        + this.contentWorkspaceBusinessConfig);
    this.getNotificationConfigurationService().update(
        contentWorkspaceBusinessConfig, organizationKey);

    FacesContext.getCurrentInstance().addMessage(
        null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
            "Successfully updated content workspace"
                + " notification configuration"));
  }

  public void savePartnerProgramBusinessConfig() {
    log.info("Saving partner config " + this.partnerProgramBusinessConfig);
    this.getNotificationConfigurationService().update(
        partnerProgramBusinessConfig, organizationKey);

    FacesContext.getCurrentInstance().addMessage(
        null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
            "Successfully updated partner program"
                + " notification configuration"));
  }

}
