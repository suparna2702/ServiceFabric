package com.similan.service.impl.wall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.collaborationworkspace.SharedDocument;
import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.document.Document;
import com.similan.domain.entity.wall.IWallContainer;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.WallEntry;
import com.similan.domain.entity.wall.wall.LinkReference;
import com.similan.domain.entity.wall.wall.WallPost;
import com.similan.domain.repository.collaborationworkspace.CollaborationWorkspaceRepository;
import com.similan.domain.repository.collaborationworkspace.SharedDocumentRepository;
import com.similan.domain.repository.wall.LinkReferenceRepository;
import com.similan.domain.repository.wall.WallEntryRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.collaborationworkspace.dto.key.SharedDocumentKey;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.wall.DocumentHistoryFilter;
import com.similan.service.api.wall.WallService;
import com.similan.service.api.wall.dto.basic.LinkReferenceDto;
import com.similan.service.api.wall.dto.basic.LinkReferenceType;
import com.similan.service.api.wall.dto.basic.WallDto;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.basic.WallEntryStatisticsDto;
import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.api.wall.dto.basic.WallEntryTypeCountDto;
import com.similan.service.api.wall.dto.basic.WallPostDto;
import com.similan.service.api.wall.dto.key.IWallContainerKey;
import com.similan.service.api.wall.dto.key.WallKey;
import com.similan.service.api.wall.dto.operation.NewWallPostDto;
import com.similan.service.impl.base.ServiceImpl;
import com.similan.service.impl.collaborationworkspace.CollaborationWorkspaceMarshaller;
import com.similan.service.impl.community.SocialActorMarshaller;
import com.similan.service.impl.document.DocumentMarshaller;
import com.similan.service.internal.api.email.EmailInternalService;
import com.similan.service.internal.api.linkreference.LinkReferenceInternalService;
import com.similan.service.internal.api.wall.WallInternalService;

@Slf4j
@Service
public class WallServiceImpl extends ServiceImpl implements WallService {
  private final static int LATEST_COUNT = 20;
  private final static int MORE_COUNT = 20;

  @Autowired
  private WallEntryRepository wallEntryRepository;
  @Autowired
  private WallInternalService wallInternalService;
  @Autowired
  private SharedDocumentRepository sharedDocumentRepository;
  @Autowired
  private LinkReferenceRepository linkReferenceRepository;
  @Autowired
  private CollaborationWorkspaceRepository collaborationWorkspaceRepository;
  @Autowired
  private PlatformCommonSettings commonSettings;
  @Autowired
  private EmailInternalService emailService;
  @Autowired
  private LinkReferenceInternalService articleService;
  @Autowired
  private WallMarshaller wallMarshaller;
  @Autowired
  private SocialActorMarshaller actorMarshaller;
  @Autowired
  private CollaborationWorkspaceMarshaller collaborationWorkspaceMarshaller;
  @Autowired
  private DocumentMarshaller documentMarshaller;

  @Override
  @Transactional
  public <WallContainerKey extends IWallContainerKey> WallDto<WallContainerKey> get(
      WallKey<WallContainerKey> wallKey) {
    Wall wall = wallMarshaller.unmarshallWallKey(wallKey, true);
    WallDto<WallContainerKey> wallDto = wallMarshaller.marshallWall(wall);
    return wallDto;
  }

  @Override
  @Transactional
  public <WallContainerKey extends IWallContainerKey> WallPostDto<WallContainerKey> post(
      WallKey<WallContainerKey> wallKey, NewWallPostDto newPost) {

    SocialActorKey authorKey = newPost.getAuthor();

    Wall wall = wallMarshaller.unmarshallWallKey(wallKey, true);
    SocialActor author = actorMarshaller.unmarshallActorKey(authorKey, true);
    String content = newPost.getContent();

    WallPost post = wallEntryRepository.createPost(wall, author, content);
    post.setLink(extractLinkreference(content));
    wallInternalService.post(post);

    WallPostDto<WallContainerKey> postDto = wallMarshaller.marshallPost(post);

    return postDto;
  }

  private LinkReference extractLinkreference(String content) {
    String url = articleService.extractUrl(content);
    if (url == null) {
      return null;
    }
    try {
      LinkReferenceDto linkDto = articleService.readLinkReference(url);
      LinkReference link = LinkReference
          .builder()
          .url(linkDto.getUrl())
          .title(linkDto.getTitle())
          .linkReferenceType(linkDto.getLinkReferenceType())
          .content(
              StringUtils.abbreviate(linkDto.getContent(),
                  LinkReference.MAX_CONTENT)).imageUrl(linkDto.getImageUrl())
          .build();
      linkReferenceRepository.save(link);
      return link;
    } catch (Exception e) {
      log.error("Error getting link reference for " + url, e);
      return null;
    }
  }

  @Override
  @Transactional
  public <WallContainerKey extends IWallContainerKey> List<WallEntryDto<WallContainerKey>> getLatest(
      WallKey<WallContainerKey> wallKey) {
    return this.getLatest(null, wallKey);
  }

  @Override
  @Transactional
  public <WallContainerKey extends IWallContainerKey> List<WallEntryDto<WallContainerKey>> getLatest(
      List<SocialActorKey> initiators, WallKey<WallContainerKey> wallKey) {

    List<SocialActor> initiatorList = new ArrayList<SocialActor>();
    if (initiators != null) {
      for (SocialActorKey actorKey : initiators) {
        SocialActor actor = actorMarshaller.unmarshallActorKey(actorKey, true);
        initiatorList.add(actor);
      }
    }

    Wall wall = wallMarshaller.unmarshallWallKey(wallKey, true);

    GenericReference<IWallContainer> container = wall.getContainer();

    EntityType wallContainerType = container.getType();
    long wallContainerId = container.getId();

    if (initiatorList != null && initiatorList.size() > 0) {
      List<WallEntry> entries = wallEntryRepository.findLatestByWall(
          initiatorList, wallContainerType, wallContainerId, new PageRequest(0,
              LATEST_COUNT));
      List<WallEntryDto<WallContainerKey>> entriesDto = wallMarshaller
          .marshallEntries(wall, entries);
      return entriesDto;
    } else {
      List<WallEntry> entries = wallEntryRepository.findLatestByWall(
          wallContainerType, wallContainerId, new PageRequest(0, LATEST_COUNT));
      List<WallEntryDto<WallContainerKey>> entriesDto = wallMarshaller
          .marshallEntries(wall, entries);
      return entriesDto;
    }
  }

  @Override
  @Transactional
  public <WallContainerKey extends IWallContainerKey> List<WallEntryDto<WallContainerKey>> getAllExternalLinkReferencePosts(
      List<SocialActorKey> initiators, WallKey<WallContainerKey> wallKey) {

    List<SocialActor> initiatorList = new ArrayList<SocialActor>();
    if (initiators != null) {
      for (SocialActorKey actorKey : initiators) {
        SocialActor actor = actorMarshaller.unmarshallActorKey(actorKey, true);
        initiatorList.add(actor);
      }
    }

    Wall wall = wallMarshaller.unmarshallWallKey(wallKey, true);
    GenericReference<IWallContainer> container = wall.getContainer();
    EntityType wallContainerType = container.getType();
    long wallContainerId = container.getId();

    if (initiatorList != null && initiatorList.size() > 0) {
      List<WallEntry> entries = wallEntryRepository
          .findAllExternalLinkReferencePostByWall(initiatorList,
              wallContainerType, wallContainerId);
      List<WallEntryDto<WallContainerKey>> entriesDto = wallMarshaller
          .marshallEntries(wall, entries);
      return entriesDto;
    } else {
      List<WallEntry> entries = wallEntryRepository
          .findAllExternalLinkReferencePostByWall(wallContainerType,
              wallContainerId);
      List<WallEntryDto<WallContainerKey>> entriesDto = wallMarshaller
          .marshallEntries(wall, entries);
      return entriesDto;
    }

  }

  @Override
  @Transactional
  public <WallContainerKey extends IWallContainerKey> List<WallEntryDto<WallContainerKey>> getMore(
      WallKey<WallContainerKey> wallKey, Integer afterNumber) {

    return this.getMore(null, wallKey, afterNumber);
  }

  @Override
  @Transactional
  public <WallContainerKey extends IWallContainerKey> List<WallEntryDto<WallContainerKey>> getMore(
      List<SocialActorKey> initiators, WallKey<WallContainerKey> wallKey,
      Integer afterNumber) {

    List<SocialActor> initiatorList = new ArrayList<SocialActor>();
    if (initiators != null) {
      for (SocialActorKey actorKey : initiators) {
        SocialActor actor = actorMarshaller.unmarshallActorKey(actorKey, true);
        initiatorList.add(actor);
      }
    }

    Wall wall = wallMarshaller.unmarshallWallKey(wallKey, true);

    GenericReference<IWallContainer> container = wall.getContainer();

    EntityType wallContainerType = container.getType();
    long wallContainerId = container.getId();

    if (initiatorList != null && initiatorList.size() > 0) {
      List<WallEntry> entries = wallEntryRepository.findMoreByWall(
          initiatorList, wallContainerType, wallContainerId, afterNumber,
          new PageRequest(0, MORE_COUNT));

      List<WallEntryDto<WallContainerKey>> entriesDto = wallMarshaller
          .marshallEntries(wall, entries);
      return entriesDto;
    } else {
      List<WallEntry> entries = wallEntryRepository.findMoreByWall(
          wallContainerType, wallContainerId, afterNumber, new PageRequest(0,
              MORE_COUNT));

      List<WallEntryDto<WallContainerKey>> entriesDto = wallMarshaller
          .marshallEntries(wall, entries);
      return entriesDto;
    }

  }

  @Override
  @Transactional
  public <WallContainerKey extends IWallContainerKey> List<WallEntryDto<WallContainerKey>> getByDate(
      WallKey<WallContainerKey> wallKey, Date fromDate, Date toDate) {
    Wall wall = wallMarshaller.unmarshallWallKey(wallKey, true);

    GenericReference<IWallContainer> container = wall.getContainer();

    EntityType wallContainerType = container.getType();
    long wallContainerId = container.getId();

    List<WallEntry> entries = wallEntryRepository.findByDateAndWall(
        wallContainerType, wallContainerId, fromDate, toDate);

    List<WallEntryDto<WallContainerKey>> entriesDto = wallMarshaller
        .marshallEntries(wall, entries);
    return entriesDto;
  }

  @Override
  @Transactional
  public <WallContainerKey extends IWallContainerKey> List<WallEntryDto<WallContainerKey>> getWorkspaceDocumentHistory(
      WallKey<WallContainerKey> wallKey, SharedDocumentKey documentKey) {

    Wall wall = wallMarshaller.unmarshallWallKey(wallKey, true);
    GenericReference<IWallContainer> container = wall.getContainer();
    long wallContainerId = container.getId();
    SharedDocument doc = collaborationWorkspaceMarshaller
        .unmarshallSharedDocumentKey(documentKey, true);
    List<WallEntry> entries = wallEntryRepository.findWorkspaceEntryBySubject(
        wallContainerId, doc.getId(), EntityType.SHARED_DOCUMENT,
        new PageRequest(0, LATEST_COUNT));

    List<WallEntryDto<WallContainerKey>> entriesDto = wallMarshaller
        .marshallDocumentEntries(wall, entries);
    return entriesDto;
  }

  @Override
  @Transactional
  public <WallContainerKey extends IWallContainerKey> List<WallEntryDto<WallContainerKey>> geDocumentHistory(
      DocumentKey documentKey, DocumentHistoryFilter filter) {

    Document doc = documentMarshaller.unmarshallDocumentKey(documentKey, true);
    List<SharedDocument> sharedDocList = this.sharedDocumentRepository
        .findAllForDocument(doc);

    List<WallEntry> entries = new ArrayList<WallEntry>();

    // get the ones from content space
    if (filter.equals(DocumentHistoryFilter.ALL)
        || filter.equals(DocumentHistoryFilter.CONTENT_WORKSPACE)) {
      entries.addAll(wallEntryRepository.findWorkspaceEntryBySubject(
          doc.getId(), EntityType.DOCUMENT, new PageRequest(0, LATEST_COUNT)));
    }

    // get the ones from collab space
    if (filter.equals(DocumentHistoryFilter.ALL)
        || filter.equals(DocumentHistoryFilter.COLLABORATION_WORKSPACE)) {
      for (SharedDocument sharedDoc : sharedDocList) {
        entries.addAll(wallEntryRepository.findWorkspaceEntryBySubject(
            sharedDoc.getId(), EntityType.SHARED_DOCUMENT, new PageRequest(0,
                LATEST_COUNT)));
      }
    }

    List<WallEntryDto<WallContainerKey>> entriesDto = wallMarshaller
        .marshallDocumentEntries(entries);
    Collections.sort(entriesDto);

    return entriesDto;
  }

  @Override
  @Transactional
  public WallEntryStatisticsDto getBasicStatistics() {

    Long total = 0L;
    List<WallEntryTypeCountDto> wallEntryCount = new ArrayList<WallEntryTypeCountDto>();

    WallEntryType[] entryTypes = WallEntryType.values();
    for (WallEntryType entryType : entryTypes) {
      Long count = this.wallEntryRepository.findWallEntryCountByType(entryType);
      WallEntryTypeCountDto entryCount = new WallEntryTypeCountDto(count,
          entryType);
      log.info("Wall entry count " + entryCount);
      wallEntryCount.add(entryCount);
      total += count;
    }

    log.info("Wall entry total count " + total);
    WallEntryStatisticsDto retStatistics = new WallEntryStatisticsDto(total,
        wallEntryCount);
    return retStatistics;
  }

}
