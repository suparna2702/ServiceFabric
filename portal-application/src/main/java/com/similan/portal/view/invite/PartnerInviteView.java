package com.similan.portal.view.invite;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.MemberInviteDto;
import com.similan.framework.dto.OrganizationBasicInfoDto;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.partner.ExistingPartnerInitiateDto;
import com.similan.framework.dto.partner.PartnerProgramDefinitionDto;
import com.similan.portal.view.BaseView;
import com.similan.service.exception.PartnerManagementServiceException;

@SuppressWarnings("deprecation")
@Scope("view")
@Component("partnerInviteView")
@Slf4j
public class PartnerInviteView extends BaseView {

  private static final long serialVersionUID = 1L;

  @Autowired
  private MemberInfoDto memberInfo;

  @Autowired
  private OrganizationDetailInfoDto orgInfo;

  private MemberInviteDto partnerInvite = new MemberInviteDto();

  // existing partner programs
  private List<PartnerProgramDefinitionDto> partnerProgramsForBusiness;

  private Long selectedPartnerProgram = Long.MIN_VALUE;

  private boolean existingBusiness = false;

  @PostConstruct
  public void init() {
    // get all the partner programs
    partnerProgramsForBusiness = this.getPartnerManagementService()
        .getPartnerProgramsForBusiness(orgInfo);
  }

  public Long getSelectedPartnerProgram() {
    return selectedPartnerProgram;
  }

  public void setSelectedPartnerProgram(Long selectedPartnerProgram) {
    this.selectedPartnerProgram = selectedPartnerProgram;
  }

  public boolean getExistingBusiness() {
    return existingBusiness;
  }

  public void setExistingBusiness(boolean existingBusiness) {
    this.existingBusiness = existingBusiness;
  }

  public MemberInviteDto getPartnerInvite() {
    return partnerInvite;
  }

  public void setPartnerInvite(MemberInviteDto partnerInvite) {
    this.partnerInvite = partnerInvite;
  }

  public List<PartnerProgramDefinitionDto> getPartnerProgramsForBusiness() {
    return partnerProgramsForBusiness;
  }

  public void setPartnerProgramsForBusiness(
      List<PartnerProgramDefinitionDto> partnerProgramsForBusiness) {
    this.partnerProgramsForBusiness = partnerProgramsForBusiness;
  }

  public void clearInputData() {
    this.partnerInvite.reset();
    this.existingBusiness = false;
  }

  public String getProgramName(long id) {
    for (PartnerProgramDefinitionDto program : partnerProgramsForBusiness) {
      if (program.getId() == id)
        log.info("Partner program name returned " + program.getName());
      return program.getName();
    }
    return "";
  }

  public PartnerProgramDefinitionDto findPartnerProgramById(Long id) {
    PartnerProgramDefinitionDto partnerProgram = null;
    // Find the program for the selected ID
    for (PartnerProgramDefinitionDto program : partnerProgramsForBusiness) {
      if (program.getId() == id)
        partnerProgram = program;
    }

    return partnerProgram;
  }

  public List<SelectItem> getPartnerProgramsSelectItems() {
    List<SelectItem> items = new ArrayList<SelectItem>();

    if (partnerProgramsForBusiness == null) {
      return items;
    }
    for (PartnerProgramDefinitionDto program : partnerProgramsForBusiness) {
      items.add(new SelectItem(program.getId(), program.getName()));
    }
    return items;
  }

  private void sendSingleInvite(MemberInviteDto memberInvite,
      MemberInfoDto memberInfo) {

    try {

      PartnerProgramDefinitionDto partnerProgram = this
          .findPartnerProgramById(this.selectedPartnerProgram);

      ExistingPartnerInitiateDto partnerInitiateDto = new ExistingPartnerInitiateDto(
          partnerInvite.getBusinessName(), partnerInvite.getBusinessEmail(),
          partnerInvite.getFirstName(), partnerInvite.getLastName(),
          this.memberInfo.getId(), partnerProgram.getId());

      this.getPartnerManagementService().initiateExistingPartnerInvite(
          partnerInitiateDto);

      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Thanks",
              "Your invitation has been sent"));

    } catch (PartnerManagementServiceException exp) {
      log.error("Error in participating program", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sorry", exp
              .getMessage()));

    } catch (Exception exp) {
      log.error("Error in participating program", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sorry",
              "Error in partner invitation " + exp.getMessage()));
    }

    this.clearInputData();

  }

  public void inviteSingle() {
    log.info("Single partner invite ");

    if (existingBusiness != true) {
      Boolean inputError = Boolean.TRUE;
      StringBuilder erroMessage = new StringBuilder();

      // cannot have blank business name or email
      if (StringUtils.isEmpty(partnerInvite.getBusinessName())) {
        inputError = Boolean.FALSE;
        erroMessage.append(" Partner Business name cannot be empty ");
      }

      if (StringUtils.isEmpty(partnerInvite.getBusinessEmail())) {
        inputError = Boolean.FALSE;
        erroMessage.append(" Partner Business email cannot be empty ");
      }

      if (EmailValidator.getInstance()
          .isValid(partnerInvite.getBusinessEmail()) != true) {
        inputError = Boolean.FALSE;
        erroMessage.append(" Invitee Email is not valid ");
      }

      if (this.selectedPartnerProgram == Long.MIN_VALUE) {
        inputError = Boolean.FALSE;
        erroMessage.append(" Select a valid partner program ");
      }

      if (inputError != true) {

        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", erroMessage
                .toString()));
        return;
      } else {

        // check for existing member
        MemberInfoDto existingMember = memberService
            .isMemberWithEmailExists(partnerInvite.getBusinessEmail());

        if (existingMember != null) {
          log.info("Member exists with email address."
              + partnerInvite.getBusinessEmail());
          this.clearInputData();

          FacesContext.getCurrentInstance().addMessage(
              null,
              new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invite Error",
                  " Sorry! A member exists with that email "
                      + partnerInvite.getBusinessEmail()));
          return;
        }

        // check for existing business
        OrganizationBasicInfoDto business = this.getOrgService()
            .getBusinessByNameAndEmail(partnerInvite.getBusinessName(),
                partnerInvite.getBusinessEmail());

        if (business != null) {
          // check for whether this business is already a partner
          Boolean existingPartner = this.getPartnerManagementService()
              .isPartnerForProgram(business.getId(),
                  this.selectedPartnerProgram);

          if (existingPartner) {
            // this business is already a partner so cannot be invited again
            FacesContext
                .getCurrentInstance()
                .addMessage(
                    null,
                    new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Error",
                        "The selected business "
                            + business.getName()
                            + " is already a partner for the selected partner program. Thus cannot be "
                            + "invited again for the same program"));
            return;
          } else {
            // inviting existing business to be partner
            existingBusiness = true;
            this.partnerInvite.setBusinessEmail(business.getEmail());
            this.partnerInvite.setBusinessName(business.getName());
            this.partnerInvite.setOrgTag(business);
            this.partnerInvite.setInvitee(memberInfo);
            this.setExistingBusiness(true);

            FacesContext
                .getCurrentInstance()
                .addMessage(
                    null,
                    new FacesMessage(
                        FacesMessage.SEVERITY_INFO,
                        "Info",
                        "The selected business "
                            + business.getName()
                            + " is already a member of BusinessReach network. Please review the information "
                            + "below and if you so desire can invite as partner "));
            return;
          }
        } else {
          // not an existing business so we can go ahead and send invite
          this.sendSingleInvite(partnerInvite, memberInfo);
          return;
        }
      }
    }

    // send invite for an existing Business
    this.sendSingleInvite(partnerInvite, memberInfo);
    return;

  }

}
