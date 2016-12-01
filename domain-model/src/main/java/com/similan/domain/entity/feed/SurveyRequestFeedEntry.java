package com.similan.domain.entity.feed;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.similan.domain.entity.poll.PollEvent;
import com.similan.service.api.feed.dto.basic.FeedEntryType;

@Entity(name = "SurveyRequestFeedEntry")
@DiscriminatorValue("SURVEY_REQUEST_ENTRY")
public class SurveyRequestFeedEntry extends FeedEntry {

  @ManyToOne(optional = true)
  @JoinColumn(nullable = true)
  private PollEvent pollEvent;

  protected SurveyRequestFeedEntry() {

  }

  public SurveyRequestFeedEntry(int number, Date date) {
    super(FeedEntryType.SURVEY_REQUEST_ENTRY, number, date);
  }

  public PollEvent getPollEvent() {
    return pollEvent;
  }

  public void setPollEvent(PollEvent pollEvent) {
    this.pollEvent = pollEvent;
  }

}
