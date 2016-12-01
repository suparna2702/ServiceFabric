package com.similan.portal.view.survey;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.poll.PollRunEventDto;
import com.similan.framework.dto.poll.PollTemplateDto;
import com.similan.portal.view.BaseView;
import com.similan.service.api.community.dto.basic.SocialActorContactDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

@Scope("view")
@Component("pollRunView")
@Slf4j
public class PollRunView extends BaseView {

  private static final long serialVersionUID = 1L;

  @Autowired
  private OrganizationDetailInfoDto orgInfo;

  @Autowired
  private MemberInfoDto memberInfo;

  private List<SocialActorContactDto> contactList = null;

  protected SocialActorContactDto[] selectedContacts;

  private String pollRunHeader;

  private String pollRunDescription;

  private Date pollDueDate;

  private Long pollId;

  private PollTemplateDto pollTemplate;

  private SocialActorKey memberKey;

  @PostConstruct
  public void init() {
    log.info("Initializing poll run View");

    try {

      String pid = this.getContextParam("pid");
      log.info("Poll id to be fetched " + pid);

      if (pid != null) {
        pollId = Long.valueOf(pid);
        pollTemplate = this.getCampaignManagementService().getPollTemplateByid(
            pollId);
      }

      memberKey = this.getMemberKey(memberInfo);
      contactList = this.getNetworkService().getExtendedConnections(memberKey);

    } catch (Exception exp) {
      log.error("Error fetching contact", exp);
    }
  }

  public List<SocialActorContactDto> getContactList() {
    return contactList;
  }

  public void setContactList(List<SocialActorContactDto> contactList) {
    this.contactList = contactList;
  }

  public SocialActorContactDto[] getSelectedContacts() {
    return selectedContacts;
  }

  public void setSelectedContacts(SocialActorContactDto[] selectedContacts) {
    this.selectedContacts = selectedContacts;
  }

  public SocialActorKey getMemberKey() {
    return memberKey;
  }

  public void setMemberKey(SocialActorKey memberKey) {
    this.memberKey = memberKey;
  }

  public OrganizationDetailInfoDto getOrgInfo() {
    return orgInfo;
  }

  public void setOrgInfo(OrganizationDetailInfoDto orgInfo) {
    this.orgInfo = orgInfo;
  }

  public MemberInfoDto getMemberInfo() {
    return memberInfo;
  }

  public void setMemberInfo(MemberInfoDto memberInfo) {
    this.memberInfo = memberInfo;
  }

  public PollTemplateDto getPollTemplate() {
    return pollTemplate;
  }

  public void setPollTemplate(PollTemplateDto pollTemplate) {
    this.pollTemplate = pollTemplate;
  }

  public Long getPollId() {
    return pollId;
  }

  public void setPollId(Long pollId) {
    this.pollId = pollId;
  }

  public String getPollRunHeader() {
    return pollRunHeader;
  }

  public void setPollRunHeader(String pollRunHeader) {
    this.pollRunHeader = pollRunHeader;
  }

  public String getPollRunDescription() {
    return pollRunDescription;
  }

  public void setPollRunDescription(String pollRunDescription) {
    this.pollRunDescription = pollRunDescription;
  }

  public Date getPollDueDate() {
    return pollDueDate;
  }

  public void setPollDueDate(Date pollDueDate) {
    this.pollDueDate = pollDueDate;
  }

  public void runPoll() {
    log.info("Running poll for contact " + this.selectedContacts
        + " header " + this.pollRunHeader + " description "
        + this.pollRunDescription + " due date " + this.pollDueDate
        + " poll id " + pollId);

    PollRunEventDto runEvent = new PollRunEventDto();
    runEvent.setPollDueDate(pollDueDate);
    runEvent.setPollId(pollId);
    runEvent.setPollRunDescription(pollRunDescription);
    runEvent.setPollRunHeader(pollRunHeader);
    runEvent.setSelectedContacts(selectedContacts);
    runEvent.setOrgId(orgInfo.getId());
    runEvent.setMemberFrom(this.memberInfo.getId());

    try {
      this.getCampaignManagementService().runPollForPartners(runEvent);

      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
              "The survey has been sent to the assigned contacts"));
    } catch (Exception exception) {
      log.error("Unable to send the survey to the assigned contacts ",
          exception);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed",
              "Unable to send the survey to the assigned contacts "
                  + exception.getMessage()));
    }
  }

}
