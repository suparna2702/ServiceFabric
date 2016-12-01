package com.similan.service.api.feed.dto.basic;

import java.util.Date;

import com.similan.service.api.community.dto.basic.ActorDto;

public class SurveyRequestFeedEntryDto extends FeedEntryDto {

  private ActorDto pollFrom;

  private String pollRunHeader;

  private String pollRunDescription;

  private Date pollDueDate;

  private Long pollEventId;

  public SurveyRequestFeedEntryDto(ActorDto pollFrom,
      String pollRunHeader, String pollRunDescription, Date pollDueDate,
      Long pollEventId) {
    this.pollFrom = pollFrom;
    this.pollRunHeader = pollRunHeader;
    this.pollRunDescription = pollRunDescription;
    this.pollDueDate = pollDueDate;
    this.pollEventId = pollEventId;
    this.pollDueDate = pollDueDate;
  }

  @Override
  public FeedEntryType getType() {
    return FeedEntryType.SURVEY_REQUEST_ENTRY;
  }

  public ActorDto getPollFrom() {
    return pollFrom;
  }

  public String getPollRunHeader() {
    return pollRunHeader;
  }

  public String getPollRunDescription() {
    return pollRunDescription;
  }

  public Date getPollDueDate() {
    return pollDueDate;
  }

  public Long getPollEventId() {
    return pollEventId;
  }

}
