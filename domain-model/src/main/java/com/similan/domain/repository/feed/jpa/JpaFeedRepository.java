package com.similan.domain.repository.feed.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.feed.Feed;

public interface JpaFeedRepository extends JpaRepository<Feed, Long> {

  @Query("select feed from Feed feed" + " where feed.owner.name = :ownerName")
  Feed findOne(@Param("ownerName") String ownerName);
  
  @Query("select feed from Feed feed" + " where feed.owner.id = :id")
  Feed findByOwner(@Param("id")Long id);

}
