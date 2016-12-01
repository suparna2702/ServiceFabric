package com.similan.domain.repository.community;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.activity.News;
import com.similan.domain.repository.community.jpa.JpaNewsRepository;

@Repository
public class NewsRepository {
  @Autowired
  private JpaNewsRepository repository;

  public List<News> findAll() {
    return repository.findAll();
  }

  public News findOne(Long id) {
    return repository.findOne(id);
  }

  public News save(News newsFeed) {
    return repository.save(newsFeed);
  }

  public void deleteNewsItem(Long id) {
    repository.deleteNewsItem(id);
  }

  public List<News> findAllByOwner(SocialOrganization owner) {
    return repository.findAllByOwner(owner);
  }

  public List<News> findLatestByOwner(SocialOrganization owner,
      Pageable pageable) {
    return repository.findLatestByOwner(owner, pageable);
  }

  public List<News> findMoreByOwner(SocialOrganization owner, Date afterDate,
      Pageable pageable) {
    return repository.findMoreByOwner(owner, afterDate, pageable);
  }

  public News create() {
    News newItem = new News();
    return newItem;
  }

}
