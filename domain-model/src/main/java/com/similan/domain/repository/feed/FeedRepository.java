package com.similan.domain.repository.feed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.feed.Feed;
import com.similan.domain.repository.feed.jpa.JpaFeedRepository;

@Repository
public class FeedRepository {
  @Autowired
  private JpaFeedRepository repository;

  public Feed save(Feed entity) {
    return repository.save(entity);
  }

  public Feed findOne(Long id) {
    return repository.findOne(id);
  }

  public Feed findOne(String ownerName) {
    return repository.findOne(ownerName);
  }
  
  public Feed findByOwner(Long id) {
    return repository.findByOwner(id);
  }

  public Feed create(SocialActor owner) {
    Feed feed = new Feed();
    feed.setOwner(owner);
    return feed;
  }

}
