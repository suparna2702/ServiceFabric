package com.similan.adminapp.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.primefaces.event.SelectEvent;

import com.similan.adminapp.model.MemberInfoSelectionModel;
import com.similan.framework.dto.member.MemberInfoDto;

@ViewScoped
@ManagedBean(name = "memberAdminView")
@Slf4j
public class MemberAdminView extends BaseAdminView {

  private static final long serialVersionUID = 1L;

  private List<MemberInfoDto> memberInfoList = null;

  private MemberInfoSelectionModel memberSelectionModel = null;

  private MemberInfoDto selectedMember;

  private int numActiveMember = 0;

  @PostConstruct
  public void init() {

    log.info("Post init in member admin view");
  }

  public MemberInfoSelectionModel getEmbeddedMembers() {

    if (memberSelectionModel == null) {
      try {
        List<MemberInfoDto> memberList = this.getMemberAdminService()
            .getEmbeddedMembers();
        if (memberList != null) {
          numActiveMember = memberList.size();
        }
        memberSelectionModel = new MemberInfoSelectionModel(memberList);
      } catch (Exception exp) {
        log.error("Error ", exp);
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure",
                "Failed to get embedded members due to " + exp.getMessage()));
      }

    }

    return memberSelectionModel;
  }

  public void setEmbeddedMembers(MemberInfoSelectionModel memberList) {
    log.info("setting embedded member " + memberList);
    this.memberSelectionModel = memberList;
  }

  public MemberInfoSelectionModel getOnlineMembers() {

    if (memberSelectionModel == null) {
      try {
        List<MemberInfoDto> memberList = this.getMemberAdminService()
            .getOnlineMembers(-1);
        if (this.memberInfoList != null) {
          log.info("Number of online members " + memberList.size());
        }
        memberSelectionModel = new MemberInfoSelectionModel(memberList);
      } catch (Exception exp) {
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure",
                "Failed to get online members due to " + exp.getMessage()));
      }

    }

    return memberSelectionModel;

  }

  public void setOnlineMembers(MemberInfoSelectionModel memberList) {
    this.memberSelectionModel = memberList;
  }

  public List<MemberInfoDto> getMemberInfoList() {

    if (memberInfoList == null) {
      try {
        this.memberInfoList = this.getMemberAdminService().getMembers(-1);
      } catch (Exception exp) {
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure",
                "Failed to get members due to " + exp.getMessage()));
      }

    }

    return memberInfoList;
  }

  public int getNumActiveMember() {
    return numActiveMember;
  }

  public void setMemberInfoList(List<MemberInfoDto> memberInfoList) {
    this.memberInfoList = memberInfoList;
  }

  public void reactivateMember(Long memberId) {
    log.info("Reactivating member " + memberId);
  }

  public void suspendMember(Long memberId) {
    log.info("Suspending member " + memberId);
  }

  public MemberInfoSelectionModel getMemberSelectionModel() {
    return memberSelectionModel;
  }

  public void setMemberSelectionModel(
      MemberInfoSelectionModel memberSelectionModel) {
    this.memberSelectionModel = memberSelectionModel;
  }

  public MemberInfoDto getSelectedMember() {
    return selectedMember;
  }

  public void setSelectedMember(MemberInfoDto selectedMember) {
    log.info("Member selected " + selectedMember.getId());
    this.selectedMember = selectedMember;
  }

  public void deleteMember(Long memberId) {
    log.info("Deleting member with id " + memberId);
    try {
      this.memberAdminService.deleteMember(memberId);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
              "Deleted member with id " + memberId));
    } catch (Exception exp) {
      log.error("Cannot delete member " + memberId, exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Cannot delete member with id " + memberId + " reason "
                  + exp.getMessage()));
    }
  }

  public void onRowSelect(SelectEvent event) {
    this.selectedMember = (MemberInfoDto) event.getObject();
    log.info("Selected member " + this.selectedMember);
  }

}
