package com.similan.domain.entity.poll;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "PollEvent")
public class PollEvent {

  @Column
  private Long pollid;

  @Column
  private String pollRunHeader;

  @Column(length = 5000)
  private String pollRunDescription;

  @Column
  private Date pollDueDate;

  @Column
  private Boolean responded;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;

  @Column
  private Long updateFrom;

  @Column
  private Long updateMemberFrom;

  @Column
  private Long updateFor;

  @Column
  private Date timeStap;

  @Column
  private Integer priority = 0;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUpdateMemberFrom() {
    return updateMemberFrom;
  }

  public void setUpdateMemberFrom(Long updateMemberFrom) {
    this.updateMemberFrom = updateMemberFrom;
  }

  public Long getUpdateFrom() {
    return updateFrom;
  }

  public void setUpdateFrom(Long updateFrom) {
    this.updateFrom = updateFrom;
  }

  public Date getTimeStap() {
    return timeStap;
  }

  public void setTimeStap(Date timeStap) {
    this.timeStap = timeStap;
  }

  public Long getUpdateFor() {
    return updateFor;
  }

  public void setUpdateFor(Long updateFor) {
    this.updateFor = updateFor;
  }

  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
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

  public Boolean getResponded() {
    return responded;
  }

  public void setResponded(Boolean responded) {
    this.responded = responded;
  }
}
