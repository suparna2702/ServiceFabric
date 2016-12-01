package com.similan.service.api.feed.dto.basic;

import lombok.Getter;

import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.document.dto.basic.DocumentDto;

@Getter
public class InNetworkSharedViewFeedEntryDto extends FeedEntryDto {
  
  private DocumentDto sharedDocument;

  private String header;

  private String message;
  
  private ActorDto sharedToActor;
  
  public InNetworkSharedViewFeedEntryDto(DocumentDto sharedDocument,
      ActorDto sharedToActor, String header, String message){
    this.sharedDocument = sharedDocument;
    this.sharedToActor = sharedToActor;
    this.header = header;
    this.message = message;
  }

  @Override
  public FeedEntryType getType() {
    return FeedEntryType.IN_NETWORK_SHARED_VIEW_ENTRY;
  }

}
