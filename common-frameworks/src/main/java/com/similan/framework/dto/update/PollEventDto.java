package com.similan.framework.dto.update;

import java.util.Date;

import com.similan.service.api.community.dto.basic.ActorDto;

public class PollEventDto extends WallEventDto {

  private static final long serialVersionUID = 1L;

  private Long pollid;

  private Long fromId;

  private String pollRunHeader;

  private String pollRunDescription;

  private Date pollDueDate;

  private ActorDto pollFor;

  public Long getFromId() {
    return fromId;
  }

  public void setFromId(Long fromId) {
    this.fromId = fromId;
  }

  public Long getPollid() {
    return pollid;
  }

  public void setPollid(Long pollid) {
    this.pollid = pollid;
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

  public ActorDto getPollFor() {
    return pollFor;
  }

  public void setPollFor(ActorDto pollFor) {
    this.pollFor = pollFor;
  }

  @Override
  public String toString() {
    return "PollEventDto [pollid=" + pollid + ", fromId=" + fromId
        + ", pollRunHeader=" + pollRunHeader + ", pollRunDescription="
        + pollRunDescription + ", pollDueDate=" + pollDueDate + ", pollFor="
        + pollFor + "]";
  }

}
