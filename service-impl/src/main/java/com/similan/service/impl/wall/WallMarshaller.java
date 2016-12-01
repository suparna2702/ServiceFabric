package com.similan.service.impl.wall;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.wall.IWallContainer;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.WallEntry;
import com.similan.domain.entity.wall.wall.WallPost;
import com.similan.domain.repository.wall.WallRepository;
import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.wall.dto.basic.WallDto;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.api.wall.dto.basic.WallPostDto;
import com.similan.service.api.wall.dto.error.WallErrorCode;
import com.similan.service.api.wall.dto.error.WallException;
import com.similan.service.api.wall.dto.key.IWallContainerKey;
import com.similan.service.api.wall.dto.key.WallEntryKey;
import com.similan.service.api.wall.dto.key.WallKey;
import com.similan.service.impl.Marshaller;
import com.similan.service.impl.common.GenericReferenceMarshaller;
import com.similan.service.impl.community.SocialActorMarshaller;

@Slf4j
@Component
public class WallMarshaller extends Marshaller {
  @Autowired
  private WallRepository wallRepository;
  @Autowired
  private GenericReferenceMarshaller genericReferenceMarshaller;
  @Autowired
  private SocialActorMarshaller actorMarshaller;
  @Resource(name = "wallEntryMarshallers")
  private Map<WallEntryType, WallEntryMarshaller<?, ?>> wallEntryMarshallers;

  public <WallContainerKey extends IWallContainerKey> Wall unmarshallWallKey(
      WallKey<WallContainerKey> wallKey, boolean required) {
    WallContainerKey containerKey = wallKey.getContainer();
    IWallContainer container = unmarshallContainerKey(containerKey, required);
    if (container == null) {
      if (required) {
        throw new WallException(WallErrorCode.WALL_NOT_FOUND, "Wall doesn't exist: " + wallKey);
      }
      return null;
    }

    EntityType containerType = container.getEntityType();
    Long containerId = container.getId();

    Wall wall = wallRepository.findOne(containerType, containerId);
    /*
     * for now if there is not a wall we will create one here
     */
    if (required && wall == null) {
      log.info("Wall doesn't exist creating one : " + wallKey);
      wall = wallRepository.create(container);
      wallRepository.save(wall);
    }
    return wall;
  }

  public <WallContainerKey extends IWallContainerKey> WallDto<WallContainerKey> marshallWall(
      Wall wall) {
    WallKey<WallContainerKey> wallKey = marshallWallKey(wall);
    WallDto<WallContainerKey> wallDto = new WallDto<>(wallKey);
    return wallDto;
  }

  public <WallContainerKey extends IWallContainerKey> WallKey<WallContainerKey> marshallWallKey(
      Wall wall) {
    GenericReference<IWallContainer> containerReference = wall.getContainer();
    WallContainerKey containerKey = marshallContainerKey(containerReference);
    WallKey<WallContainerKey> wallKey = new WallKey<WallContainerKey>(
        containerKey);
    return wallKey;
  }

  public <WallContainerKey extends IWallContainerKey> WallContainerKey marshallContainerKey(
      GenericReference<IWallContainer> containerReference) {
    WallContainerKey containerKey = genericReferenceMarshaller.marshallKey(containerReference,
            IWallContainerKey.class);
    return containerKey;
  }

  public <WallContainerKey extends IWallContainerKey> IWallContainer unmarshallContainerKey(
      WallContainerKey containerKey, boolean required) {
    IWallContainer entity = genericReferenceMarshaller
        .unmarshallKey(containerKey, required, IWallContainer.class);
    return entity;
  }

  @SuppressWarnings("unchecked")
  public <WallContainerKey extends IWallContainerKey> WallPostDto<WallContainerKey> marshallPost(
      WallPost post) {
    return (WallPostDto<WallContainerKey>) marshallEntry(post);
  }

  public <WallContainerKey extends IWallContainerKey> WallEntryKey<WallContainerKey> marshallEntryKey(
      WallEntry entry) {
    Wall wall = entry.getWall();
    WallKey<WallContainerKey> wallKey = marshallWallKey(wall);
    WallEntryKey<WallContainerKey> entryKey = marshallEntryKey(wallKey, entry);
    return entryKey;
  }

  private <WallContainerKey extends IWallContainerKey> WallEntryKey<WallContainerKey> marshallEntryKey(
      WallKey<WallContainerKey> wallKey, WallEntry entry) {
    Integer number = entry.getNumber();
    WallEntryKey<WallContainerKey> entryKey = new WallEntryKey<>(wallKey,
        number);
    return entryKey;
  }

  public <WallContainerKey extends IWallContainerKey> WallEntryDto<WallContainerKey> marshallEntry(
      WallEntry entry) {
    Wall wall = entry.getWall();
    WallKey<WallContainerKey> wallKey = marshallWallKey(wall);
    WallEntryDto<WallContainerKey> entryDto = marshallEntry(wallKey, entry);
    return entryDto;
  }

  private <WallContainerKey extends IWallContainerKey> WallEntryDto<WallContainerKey> marshallEntry(
      WallKey<WallContainerKey> wallKey, WallEntry entry) {
    WallEntryType type = entry.getType();
    @SuppressWarnings("unchecked")
    WallEntryMarshaller<WallEntry, WallContainerKey> wallEntryMarshaller = (WallEntryMarshaller<WallEntry, WallContainerKey>) wallEntryMarshallers
        .get(type);
    if (wallEntryMarshaller == null) {
      throw new IllegalArgumentException("Unknown type: " + type);
    }
    WallEntryKey<WallContainerKey> entryKey = marshallEntryKey(entry);
    SocialActor initiator = entry.getInitiator();
    ActorDto initiatorDto = actorMarshaller.marshallActor(initiator);
    Date date = entry.getDate();
    return wallEntryMarshaller.marshall(entryKey, initiatorDto, date, entry);
  }

  public <WallContainerKey extends IWallContainerKey> List<WallEntryDto<WallContainerKey>> marshallEntries(
      Wall wall, List<WallEntry> entries) {
    WallKey<WallContainerKey> wallKey = marshallWallKey(wall);
    List<WallEntryDto<WallContainerKey>> entryDtos = new ArrayList<>(
        entries.size());
    for (WallEntry entry : entries) {
      WallEntryDto<WallContainerKey> entryDto = marshallEntry(wallKey, entry);
      entryDtos.add(entryDto);
    }
    return entryDtos;
  }

  public <WallContainerKey extends IWallContainerKey> List<WallEntryDto<WallContainerKey>> marshallDocumentEntries(
      List<WallEntry> entries) {

    List<WallEntryDto<WallContainerKey>> entryDtos = new ArrayList<>(
        entries.size());

    for (WallEntry entry : entries) {

      Wall wall = entry.getWall();
      WallKey<WallContainerKey> wallKey = marshallWallKey(wall);

      WallEntryDto<WallContainerKey> entryDto = marshallEntry(wallKey, entry);
      entryDtos.add(entryDto);
    }

    return entryDtos;
  }

  public <WallContainerKey extends IWallContainerKey> List<WallEntryDto<WallContainerKey>> marshallDocumentEntries(
      Wall wall, List<WallEntry> entries) {

    WallKey<WallContainerKey> wallKey = marshallWallKey(wall);
    List<WallEntryDto<WallContainerKey>> entryDtos = new ArrayList<>(
        entries.size());
    for (WallEntry entry : entries) {
      WallEntryDto<WallContainerKey> entryDto = marshallEntry(wallKey, entry);
      entryDtos.add(entryDto);
    }
    return entryDtos;
  }

}
