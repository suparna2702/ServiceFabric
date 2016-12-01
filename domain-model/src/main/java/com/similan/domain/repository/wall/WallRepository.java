package com.similan.domain.repository.wall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.wall.IWallContainer;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.repository.common.GenericReferenceRepository;
import com.similan.domain.repository.wall.jpa.JpaWallRepository;
import com.similan.service.api.base.dto.key.EntityType;

@Repository
public class WallRepository {
  @Autowired
  private JpaWallRepository repository;

  @Autowired
  private GenericReferenceRepository genericReferenceRepository;

  public Wall save(Wall entity) {
    return repository.save(entity);
  }

  public Wall findOne(Long id) {
    return repository.findOne(id);
  }

  public Wall findOne(EntityType containerType, long containerId) {
    return repository.findOne(containerType, containerId);
  }

  public Wall create(IWallContainer container) {
    GenericReference<IWallContainer> containerReference = genericReferenceRepository
        .create(container);

    Wall wall = new Wall();
    wall.setContainer(containerReference);
    return wall;
  }
}
