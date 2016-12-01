package com.similan.portal.view.invite;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.MemberInviteDto;
import com.similan.framework.dto.MemberInviteListDto;
import com.similan.framework.dto.OrganizationBasicInfoDto;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;
import com.similan.service.api.connection.dto.basic.ContactType;
import com.similan.service.exception.ContactAlreadyExistsException;
import com.similan.service.exception.MemberManagementServiceException;

@SuppressWarnings("deprecation")
@Scope("view")
@Component("memberInviteView")
@Slf4j
public class MemberInviteView extends BaseView {

  private static final long serialVersionUID = 1L;

  @Autowired
  private MemberInfoDto memberInfo;

  @Autowired(required = true)
  private OrganizationDetailInfoDto orgInfo;

  private MemberInviteDto memberInvite = new MemberInviteDto();

  private MemberInviteListDto memberInviteList;

  private boolean existingmember = false;

  public MemberInviteDto getMemberInvite() {
    return memberInvite;
  }

  public void setMemberInvite(MemberInviteDto memberInvite) {
    this.memberInvite = memberInvite;
  }

  public MemberInviteListDto getMemberInviteList() {
    return memberInviteList;
  }

  public void setMemberInviteList(MemberInviteListDto memberInviteList) {
    this.memberInviteList = memberInviteList;
  }

  public boolean isExistingmember() {
    return existingmember;
  }

  public void setExistingmember(boolean existingmember) {
    this.existingmember = existingmember;
  }

  private void resetInputDate() {
    log.info("Clearing invite data ");
    this.memberInvite.reset();
    this.existingmember = false;
  }

  public void clearlInviteSingle() {
    this.resetInputDate();
  }

  private void sendSingleInvite(MemberInviteDto memberInvite,
      MemberInfoDto memberInfo) {
    try {

      // forcing it to college now since it is an employee invite
      memberInvite.setMemberInvite(ContactType.Collegue);
      memberService.memberSingleInvite(memberInvite, memberInfo);
      String inviteMsg = "An invitation email has been sent to "
          + memberInvite.getEmail()
          + " you will receive a notifification upon "
          + "acceptance of the invitation request. ";

      FacesContext.getCurrentInstance().addMessage(null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Message", inviteMsg));

    } catch (ContactAlreadyExistsException contactAlreadyExistsException) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR,
              "Invite Request Failure", "Member with email address "
                  + memberInvite.getEmail() + " is already a contact"));
    } catch (MemberManagementServiceException exp) {
      log.error("Cannot invite member ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invite Failure", exp
              .getMessage()));

    } catch (Exception exp) {

      log.error("Invite failed", exp);
      String errMsg = "Failed to invite " + memberInvite.getFirstName() + " "
          + memberInvite.getLastName();
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invite Failure",
              errMsg));
    }

    // reset all the data
    this.resetInputDate();

  }

  public void inviteSingle() {

    if (existingmember != true) {
      MemberInfoDto existingMember = memberService
          .isMemberWithEmailExists(memberInvite.getEmail());

      if (existingMember != null) {
        log.info("Member exists with email address.");

        if (existingMember.getEmail().equalsIgnoreCase(
            this.memberInfo.getEmail())) {
          FacesContext.getCurrentInstance().addMessage(
              null,
              new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invite Error",
                  " Sorry! You cannot invite yourself"));
          return;
        }

        if (existingMember.getEmployer() != null) {
          OrganizationBasicInfoDto employer = existingMember.getEmployer();
          if (orgInfo.getId() == employer.getId()) {
            FacesContext
                .getCurrentInstance()
                .addMessage(
                    null,
                    new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Invite Error",
                        " Sorry! The member "
                            + existingMember.getFullName()
                            + " is already an employee of the same business as yours "
                            + employer.getName()));
            return;
          }
        }

        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Invite Message",
                " A member with the email address" + memberInvite.getEmail()
                    + " with name " + existingMember.getFullName()
                    + "already exists in the network. Please press "
                    + "invite to connect to this member"));
        memberInvite.setInvitee(existingMember);
        memberInvite.setFirstName(existingMember.getFirstName());
        memberInvite.setLastName(existingMember.getLastName());
        memberInvite.setEmail(existingMember.getEmail());
        memberInvite.setConfirmEmail(existingMember.getEmail());
        memberInvite.setPhotoLocation(existingMember.getImagePath());
        existingmember = true;
        return;

      } else {

        log.info("Member does not exist with email address.");
        Boolean inputError = Boolean.TRUE;
        StringBuilder erroMessage = new StringBuilder();

        if (StringUtils.isBlank(memberInvite.getFirstName())) {
          inputError = Boolean.FALSE;
          erroMessage.append("Invitee First Name cannot be empty ");
        }

        if (StringUtils.isBlank(memberInvite.getLastName())) {
          inputError = Boolean.FALSE;
          erroMessage.append(" Invitee Last Name cannot be empty ");
        }

        if (EmailValidator.getInstance().isValid(memberInvite.getEmail()) != true) {
          inputError = Boolean.FALSE;
          erroMessage.append(" Invitee Email is not valid ");
        }

        if (memberInvite.getEmail().equalsIgnoreCase(
            memberInvite.getConfirmEmail()) != true) {
          inputError = Boolean.FALSE;
          erroMessage.append(" Email is not same as confirm email ");
        }

        if (inputError != true) {

          FacesContext.getCurrentInstance().addMessage(
              null,
              new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                  erroMessage.toString()));
          return;
        } else {

          this.sendSingleInvite(this.memberInvite, this.memberInfo);
          return;

        }
      }
    } else {
      // inviting an existing member
      this.sendSingleInvite(this.memberInvite, this.memberInfo);
      return;

    }

  }

}
