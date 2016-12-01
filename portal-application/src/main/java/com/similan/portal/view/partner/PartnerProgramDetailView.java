package com.similan.portal.view.partner;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.partner.PartnerProgramDefinitionDto;
import com.similan.framework.dto.partner.PartnershipDto;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("partnerProgramDetailView")
@Slf4j
public class PartnerProgramDetailView extends BaseView {

  private static final long serialVersionUID = 1L;

  @Autowired
  private OrganizationDetailInfoDto orgInfo;

  @Autowired
  private MemberInfoDto memberInfo;

  private PartnerProgramDefinitionDto partnerProgram = null;

  private List<PartnershipDto> partners = null;

  private boolean sameLoggedInOrg = true;

  @PostConstruct
  public void init() {

    try {

      String partnerProgramIdParam = this.getContextParam("pid");
      log.info("Partner program id " + partnerProgramIdParam);

      if (partnerProgramIdParam != null) {
        Long partnerProgramId = Long.valueOf(partnerProgramIdParam);
        partnerProgram = this.getPartnerManagementService()
            .getPartnerProgramById(partnerProgramId);
      }

      if (partnerProgram.getOwner() != null) {
        log.info("Owner id " + orgInfo.getId()
            + " logged in member business id "
            + partnerProgram.getOwner().getId());

        if (orgInfo.getId().compareTo(partnerProgram.getOwner().getId()) == 0) {
          sameLoggedInOrg = true;
          log.info(" Same business entities " + sameLoggedInOrg);
        } else {
          sameLoggedInOrg = false;
          log.info(" Different business entities " + sameLoggedInOrg);
        }
      }

      // Sort the questions
      if (partnerProgram != null) {

        partners = this.getPartnerManagementService().getPartnersForProgram(
            partnerProgram.getId());
      }
    } catch (Exception exp) {

      log.error("Security exception ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "You do not have privilage to view the Partner Program "));
    }

  }

  public OrganizationDetailInfoDto getOrgInfo() {
    return orgInfo;
  }

  public List<PartnershipDto> getPartners() {
    return partners;
  }

  public boolean isSameLoggedInOrg() {
    return sameLoggedInOrg;
  }

  public void setSameLoggedInOrg(boolean sameLoggedInOrg) {
    this.sameLoggedInOrg = sameLoggedInOrg;
  }

  public PartnerProgramDefinitionDto getPartnerProgram() {
    return partnerProgram;
  }

  public void setPartnerProgram(PartnerProgramDefinitionDto partnerProgram) {
    this.partnerProgram = partnerProgram;
  }

  public void participateInPartnerProgram() {
    log.info("The org " + orgInfo.getId() + " participating in "
        + partnerProgram.getId() + " owned by "
        + partnerProgram.getOwner().getId());
    /**
     * 1. If the participating organization is same then cannot participate 2.
     * Cannot participate if already part of the program
     */
    if (sameLoggedInOrg == true) {

      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_WARN, "Message",
              "Cannot participate in youw own partner program"));
    } else {
      boolean isParticipating = this.getPartnerManagementService()
          .isPartnerForProgram(this.orgInfo.getId(), partnerProgram.getId());

      if (isParticipating == true) {
        FacesContext
            .getCurrentInstance()
            .addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Message",
                    "Cannot participate since you are already participating in this program"));
      } else {
        if (StringUtils.isEmpty(orgInfo.getEmail()) == true) {
          FacesContext
              .getCurrentInstance()
              .addMessage(
                  null,
                  new FacesMessage(FacesMessage.SEVERITY_WARN, "Message",
                      "Cannot participate since you have an invalid email address "));
          return;
        }

        orgService.initiatePartnerParticipation(orgInfo, memberInfo,
            partnerProgram);
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Message",
                "Your participation process has been initiated"));
      }
    }
  }
}
