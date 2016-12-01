package com.similan.service.api.feed.dto.basic;

import lombok.Getter;

import com.similan.service.api.document.dto.basic.DocumentDto;
import com.similan.service.api.profile.dto.ExternalSocialPersonDto;

@Getter
public class ExternalSharedViewFeedEntryDto extends FeedEntryDto {

  private DocumentDto sharedDocument;

  private String header;

  private String message;

  private ExternalSocialPersonDto sharedWith;

  public ExternalSharedViewFeedEntryDto(DocumentDto sharedDocument,
      ExternalSocialPersonDto sharedWith, String header, String message) {
    this.sharedDocument = sharedDocument;
    this.sharedWith = sharedWith;
    this.header = header;
    this.message = message;
  }

  @Override
  public FeedEntryType getType() {
    return FeedEntryType.EXTERNAL_SHARED_VIEW_ENTRY;
  }

}
