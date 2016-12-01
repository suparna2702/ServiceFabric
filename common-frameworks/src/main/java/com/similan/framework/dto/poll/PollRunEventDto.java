package com.similan.framework.dto.poll;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import com.similan.service.api.community.dto.basic.SocialActorContactDto;

public class PollRunEventDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String pollRunHeader;

  private String pollRunDescription;

  private Date pollDueDate;

  protected SocialActorContactDto[] selectedContacts;

  protected Long pollId;

  protected Long orgId;

  protected Long memberFrom;

  public Long getMemberFrom() {
    return memberFrom;
  }

  public void setMemberFrom(Long memberFrom) {
    this.memberFrom = memberFrom;
  }

  public Long getOrgId() {
    return orgId;
  }

  public void setOrgId(Long orgId) {
    this.orgId = orgId;
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

  public Long getPollId() {
    return pollId;
  }

  public void setPollId(Long pollId) {
    this.pollId = pollId;
  }

  public SocialActorContactDto[] getSelectedContacts() {
    return selectedContacts;
  }

  public void setSelectedContacts(SocialActorContactDto[] selectedContacts) {
    this.selectedContacts = selectedContacts;
  }

  @Override
  public String toString() {
    return "PollRunEventDto [pollRunHeader=" + pollRunHeader
        + ", pollRunDescription=" + pollRunDescription + ", pollDueDate="
        + pollDueDate + ", selectedContacts="
        + Arrays.toString(selectedContacts) + ", pollId=" + pollId + ", orgId="
        + orgId + "]";
  }

}
