package com.similan.framework.dto.poll;

import java.io.Serializable;

public class PollRunStatisticsDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private long eventCount;

  private long submissionCount;

  public PollRunStatisticsDto() {
    eventCount = 0L;
    submissionCount = 0L;
  }

  public long getEventCount() {
    return eventCount;
  }

  public void setEventCount(long eventCount) {
    this.eventCount = eventCount;
  }

  public long getSubmissionCount() {
    return submissionCount;
  }

  public void setSubmissionCount(long submissionCount) {
    this.submissionCount = submissionCount;
  }

  public Long getTotalRequestCount() {
    return (eventCount + submissionCount);
  }

  @Override
  public String toString() {
    return "PollRunStatisticsDto [eventCount=" + eventCount
        + ", submissionCount=" + submissionCount + "]";
  }

}
