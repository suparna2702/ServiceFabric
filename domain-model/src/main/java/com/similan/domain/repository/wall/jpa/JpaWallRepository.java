package com.similan.domain.repository.wall.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.wall.Wall;
import com.similan.service.api.base.dto.key.EntityType;

public interface JpaWallRepository extends JpaRepository<Wall, Long> {

  @Query("select wall from Wall wall"
      + " where wall.container.type = :containerType"
      + " and wall.container.id = :containerId")
  Wall findOne(@Param("containerType") EntityType containerType,
      @Param("containerId") long containerId);

}
