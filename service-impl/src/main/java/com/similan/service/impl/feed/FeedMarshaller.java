package com.similan.service.impl.feed;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.collaborationworkspace.SharedDocument;
import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.community.ExternalSocialPerson;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.document.Document;
import com.similan.domain.entity.feed.ExternalSharedViewFeedEntry;
import com.similan.domain.entity.feed.Feed;
import com.similan.domain.entity.feed.FeedEntry;
import com.similan.domain.entity.feed.InNetworkSharedViewFeedEntry;
import com.similan.domain.entity.feed.SurveyRequestFeedEntry;
import com.similan.domain.entity.feed.WallFeedEntry;
import com.similan.domain.entity.poll.PollEvent;
import com.similan.domain.entity.wall.IWallContainer;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.WallEntry;
import com.similan.domain.repository.collaborationworkspace.SharedDocumentRepository;
import com.similan.domain.repository.community.SocialActorRepository;
import com.similan.domain.repository.document.DocumentRepository;
import com.similan.domain.repository.feed.FeedRepository;
import com.similan.domain.share.ExternalShared;
import com.similan.domain.share.ISharable;
import com.similan.domain.share.InNetworkShared;
import com.similan.service.api.base.dto.basic.IKeyHolderDto;
import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.dto.basic.DocumentDto;
import com.similan.service.api.feed.dto.basic.ExternalSharedViewFeedEntryDto;
import com.similan.service.api.feed.dto.basic.FeedDto;
import com.similan.service.api.feed.dto.basic.FeedEntryDto;
import com.similan.service.api.feed.dto.basic.FeedEntryType;
import com.similan.service.api.feed.dto.basic.InNetworkSharedViewFeedEntryDto;
import com.similan.service.api.feed.dto.basic.SurveyRequestFeedEntryDto;
import com.similan.service.api.feed.dto.basic.WallFeedEntryDto;
import com.similan.service.api.feed.dto.key.FeedEntryKey;
import com.similan.service.api.feed.dto.key.FeedKey;
import com.similan.service.api.profile.dto.ExternalSocialPersonDto;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.key.IWallContainerKey;
import com.similan.service.impl.Marshaller;
import com.similan.service.impl.common.GenericReferenceMarshaller;
import com.similan.service.impl.community.SocialActorMarshaller;
import com.similan.service.impl.document.DocumentMarshaller;
import com.similan.service.impl.wall.WallMarshaller;

@Slf4j
@Component
public class FeedMarshaller extends Marshaller {
  @Autowired
  private FeedRepository feedRepository;
  @Autowired
  private SocialActorRepository socialActorRepository;
  @Autowired
  private SharedDocumentRepository sharedDocumentRepository;
  @Autowired
  private DocumentRepository documentRepository;
  @Autowired
  private SocialActorMarshaller actorMarshaller;
  @Autowired
  private GenericReferenceMarshaller genericReferenceMarshaller;
  @Autowired
  private WallMarshaller wallMarshaller;
  @Autowired
  private DocumentMarshaller documentMarshaller;

  public Feed unmarshallFeedKey(FeedKey feedKey, boolean required) {
    SocialActorKey ownerKey = feedKey.getOwner();
    String ownerName = ownerKey.getName();
    Feed feed = feedRepository.findOne(ownerName);

    /* for now we are creating if it does not exist */
    if (required && feed == null) {
      SocialActor owner = actorMarshaller
          .unmarshallActorKey(ownerKey, true);
      feed = feedRepository.create(owner);
      feedRepository.save(feed);
      log.info("Feed doesn't exist: creating one " + feed.getId());
    }
    return feed;
  }

  public FeedDto marshallFeed(Feed feed) {
    FeedKey feedKey = marshallFeedKey(feed);
    FeedDto feedDto = new FeedDto(feedKey);
    return feedDto;
  }

  public FeedKey marshallFeedKey(Feed feed) {
    SocialActor owner = feed.getOwner();
    SocialActorKey ownerKey = actorMarshaller
        .marshallActorKey(owner);
    FeedKey feedKey = new FeedKey(ownerKey);
    return feedKey;
  }

  public <WallContainerKey extends IWallContainerKey> WallFeedEntryDto<WallContainerKey> marshallWallFeedEntry(
      WallFeedEntry wallFeedEntry) {
    Feed feed = wallFeedEntry.getFeed();
    FeedKey feedKey = marshallFeedKey(feed);
    WallFeedEntryDto<WallContainerKey> wallFeedEntryDto = marshallWallFeedEntry(
        feedKey, wallFeedEntry);
    return wallFeedEntryDto;
  }

  private <WallContainerKey extends IWallContainerKey> WallFeedEntryDto<WallContainerKey> marshallWallFeedEntry(
      FeedKey feedKey, WallFeedEntry wallFeedEntry) {
    WallEntry wallEntry = wallFeedEntry.getWallEntry();

    FeedEntryKey entryKey = marshallEntryKey(feedKey, wallFeedEntry);
    Date date = wallFeedEntry.getDate();
    WallEntryDto<WallContainerKey> wallEntryDto =
        wallMarshaller.marshallEntry(wallEntry);

    Wall wall = wallEntry.getWall();
    GenericReference<IWallContainer> containerReference = wall.getContainer();
    IWallContainer container = genericReferenceMarshaller
        .findOne(containerReference, IWallContainer.class);
    IKeyHolderDto<WallContainerKey> containerDisplayable =
        genericReferenceMarshaller.marshall(container,
            IWallContainerKey.class);
    WallFeedEntryDto<WallContainerKey> wallFeedEntryDto = new WallFeedEntryDto<>(
        entryKey, date, containerDisplayable, wallEntryDto);
    return wallFeedEntryDto;
  }

  public FeedEntryKey marshallEntryKey(FeedEntry entry) {
    Feed feed = entry.getFeed();
    FeedKey feedKey = marshallFeedKey(feed);
    FeedEntryKey entryKey = marshallEntryKey(feedKey, entry);
    return entryKey;
  }

  private FeedEntryKey marshallEntryKey(FeedKey feedKey, FeedEntry entry) {
    Integer number = entry.getNumber();
    FeedEntryKey entryKey = new FeedEntryKey(feedKey, number);
    return entryKey;
  }

  public FeedEntryDto marshallEntry(FeedEntry entry) {
    Feed feed = entry.getFeed();
    FeedKey feedKey = marshallFeedKey(feed);
    FeedEntryDto entryDto = marshallEntry(feedKey, entry);
    return entryDto;
  }

  private FeedEntryDto marshallEntry(FeedKey feedKey, FeedEntry entry) {
    FeedEntryType type = entry.getType();
    switch (type) {
    case WALL_ENTRY:
      return marshallWallFeedEntry(feedKey, (WallFeedEntry) entry);
    case SURVEY_REQUEST_ENTRY:
      return marshallSurveyRequestFeedEntry(feedKey,
          (SurveyRequestFeedEntry) entry);
    case EXTERNAL_SHARED_VIEW_ENTRY:
      return marshallExternalSharedViewFeedEntry(feedKey,
          (ExternalSharedViewFeedEntry) entry);
    case IN_NETWORK_SHARED_VIEW_ENTRY:
      return marshallInNetworkSharedViewFeedEntry(feedKey,
          (InNetworkSharedViewFeedEntry) entry);
    default:
      throw new IllegalArgumentException("Unknown type: " + type);
    }
  }

  private FeedEntryDto marshallInNetworkSharedViewFeedEntry(FeedKey feedKey,
      InNetworkSharedViewFeedEntry entry) {

    InNetworkShared inNetworkShared = entry.getInNetworkShared();
    GenericReference<ISharable> sharedEntity = inNetworkShared
        .getSharedEntity();

    Document sharedDocument = this.documentRepository.findOne(sharedEntity
        .getId());
    DocumentDto document = documentMarshaller
        .marshallDocument(sharedDocument);

    SocialActor sharedWith = inNetworkShared.getSharedToActor();
    ActorDto sharedWithDto = actorMarshaller.marshallActor(sharedWith);

    InNetworkSharedViewFeedEntryDto retDto = new InNetworkSharedViewFeedEntryDto(
        document, sharedWithDto, inNetworkShared.getHeader(),
        inNetworkShared.getMessage());
    return retDto;

  }

  private FeedEntryDto marshallSurveyRequestFeedEntry(FeedKey feedKey,
      SurveyRequestFeedEntry entry) {

    PollEvent pollEvent = entry.getPollEvent();
    SocialActor actor = this.socialActorRepository.findOne(pollEvent
        .getUpdateMemberFrom());

    ActorDto pollFrom = actorMarshaller.marshallActor(actor);

    SurveyRequestFeedEntryDto retFeedEntry = new SurveyRequestFeedEntryDto(
        pollFrom, pollEvent.getPollRunHeader(),
        pollEvent.getPollRunDescription(), pollEvent.getPollDueDate(),
        pollEvent.getId());

    return retFeedEntry;
  }

  private FeedEntryDto marshallExternalSharedViewFeedEntry(FeedKey feedKey,
      ExternalSharedViewFeedEntry entry) {

    ExternalShared externalShared = entry.getExternalShared();
    GenericReference<ISharable> sharedEntity = entry.getExternalShared()
        .getSharedEntity();
    EntityType entiyType = sharedEntity.getType();

    switch (entiyType) {
    case SHARED_DOCUMENT: {
      SharedDocument sharedDocument = this.sharedDocumentRepository
          .findOne(sharedEntity.getId());
      DocumentDto document = documentMarshaller
          .marshallDocument(sharedDocument.getDocument());

      ExternalSocialPerson sharedWith = externalShared.getSharedTo();
      ExternalSocialPersonDto sharedWithDto =
          actorMarshaller.marshallExternalSocialPersonDto(sharedWith);

      ExternalSharedViewFeedEntryDto retDto = new ExternalSharedViewFeedEntryDto(
          document, sharedWithDto, externalShared.getHeader(),
          externalShared.getMessage());
      return retDto;
    }
    default:
      break;
    }
    return null;
  }

  public List<FeedEntryDto> marshallEntriesOfSameFeed(List<FeedEntry> entries) {
    List<FeedEntryDto> entryDtos = new ArrayList<>(entries.size());
    if (entries.isEmpty()) {
      return entryDtos;
    }
    FeedEntry first = entries.get(0);
    Feed feed = first.getFeed();
    FeedKey feedKey = marshallFeedKey(feed);
    for (FeedEntry entry : entries) {
      FeedEntryDto entryDto = marshallEntry(feedKey, entry);
      entryDtos.add(entryDto);
    }
    return entryDtos;
  }

}
