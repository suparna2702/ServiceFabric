package com.similan.service.internal.impl.wall;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.common.IDomainEntity;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.wall.IWallContainer;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.WallEntry;
import com.similan.domain.repository.wall.WallEntryRepository;
import com.similan.domain.repository.wall.WallRepository;
import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.wall.dto.error.WallErrorCode;
import com.similan.service.api.wall.dto.error.WallException;
import com.similan.service.impl.base.ServiceImpl;
import com.similan.service.impl.common.GenericReferenceMarshaller;
import com.similan.service.internal.api.event.EventInternalService;
import com.similan.service.internal.api.event.io.wall.NewWallEntryEvent;
import com.similan.service.internal.api.wall.WallInternalService;

@Slf4j
@Service
public class WallInternalServiceImpl extends ServiceImpl implements
    WallInternalService {
  @Autowired
  private EventInternalService eventInternalService;
  @Autowired
  private WallRepository wallRepository;
  @Autowired
  private WallEntryRepository wallEntryRepository;
  @Autowired
  private GenericReferenceMarshaller genericReferenceMarshaller;
  @Resource(name = "wallRetrievers")
  private Map<EntityType, WallRetriever<?>> wallRetrievers;
  @Resource(name = "wallConsumerEvaluators")
  private Map<EntityType, ConsumerEvaluator<?>> consumerEvaluators;

  @Override
  public void post(WallEntry entry) {
    wallEntryRepository.save(entry);

    if (entry.getShowWall() != false) {
      log.info("Firing event for " + entry.getId() + " of type "
          + entry.getDate());
      Long entryId = entry.getId();
      NewWallEntryEvent event = new NewWallEntryEvent(entryId);
      eventInternalService.fire(event);
    }
    
  }

  @Override
  public Wall get(IWallContainer container) {
    EntityType containerType = container.getEntityType();
    Long containerId = container.getId();
    Wall wall = wallRepository.findOne(containerType, containerId);
    /*
     * for now if there is not a wall we will create one here
     */
    if (wall == null) {
      wall = wallRepository.create(container);
      wallRepository.save(wall);
    }
    return wall;
  }

  @Override
  public IWallContainer getContainer(Wall wall) {
    GenericReference<IWallContainer> containerReference = wall.getContainer();
    IWallContainer container = genericReferenceMarshaller
        .findOne(containerReference, IWallContainer.class);
    return container;
  }

  @Override
  public WallEntry getEntry(long entryId) {
    WallEntry entry = wallEntryRepository.findOne(entryId);
    if (entry == null) {
      throw new WallException(WallErrorCode.WALL_ENTRY_NOT_FOUND, "No such wall entry: " + entryId);
    }
    return entry;
  }

  @Override
  public Wall getWall(IDomainEntity element) {
    EntityType type = element.getEntityType();
    WallRetriever<?> retriever = wallRetrievers.get(type);
    if (retriever == null) {
      return null;
    }
    @SuppressWarnings("unchecked")
    WallRetriever<IDomainEntity> castedRetriever = (WallRetriever<IDomainEntity>) retriever;
    return castedRetriever.getWall(element);
  }

  @Override
  public Set<SocialActor> getConsumers(SocialActor initiator, Wall wall) {
    IWallContainer wallContainer = getContainer(wall);
    EntityType type = wallContainer.getEntityType();
    ConsumerEvaluator<?> evaluator = consumerEvaluators.get(type);
    if (evaluator == null) {
      return Collections.emptySet();
    }
    @SuppressWarnings("unchecked")
    ConsumerEvaluator<IWallContainer> castedEvaluator = ((ConsumerEvaluator<IWallContainer>) evaluator);
    return castedEvaluator.getConsumers(initiator, wallContainer);
  }

}
