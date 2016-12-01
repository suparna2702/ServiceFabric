package com.similan.framework.dto.poll;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.similan.domain.entity.poll.PollSubmissionType;
import com.similan.framework.dto.OrganizationBasicInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.update.PollEventDto;

public class PollSubmissionDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  private Long pollTemplateId;

  private PollEventDto pollEventFor;

  private Date timeStamp;

  private PollSubmissionType submitterType;

  private MemberInfoDto submitterMemberInfo;

  private OrganizationBasicInfoDto submitterOrgInfo;

  private List<PollAnswerDto> answers;

  public PollSubmissionDto() {
    id = Long.MIN_VALUE;
    answers = new ArrayList<PollAnswerDto>();
  }

  public List<PollAnswerDto> getAnswers() {
    return answers;
  }

  public void setAnswers(List<PollAnswerDto> answers) {
    this.answers = answers;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPollTemplateId() {
    return pollTemplateId;
  }

  public void setPollTemplateId(Long pollTemplateId) {
    this.pollTemplateId = pollTemplateId;
  }

  public PollEventDto getPollEventFor() {
    return pollEventFor;
  }

  public void setPollEventFor(PollEventDto pollEventFor) {
    this.pollEventFor = pollEventFor;
  }

  public Date getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(Date timeStamp) {
    this.timeStamp = timeStamp;
  }

  public PollSubmissionType getSubmitterType() {
    return submitterType;
  }

  public void setSubmitterType(PollSubmissionType submitterType) {
    this.submitterType = submitterType;
  }

  public MemberInfoDto getSubmitterMemberInfo() {
    return submitterMemberInfo;
  }

  public void setSubmitterMemberInfo(MemberInfoDto submitterMemberInfo) {
    this.submitterMemberInfo = submitterMemberInfo;
  }

  public OrganizationBasicInfoDto getSubmitterOrgInfo() {
    return submitterOrgInfo;
  }

  public void setSubmitterOrgInfo(OrganizationBasicInfoDto submitterOrgInfo) {
    this.submitterOrgInfo = submitterOrgInfo;
  }

}
