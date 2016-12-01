package com.similan.domain.repository.community.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.activity.News;

public interface JpaNewsRepository extends JpaRepository<News, Long> {

  @Query("select news from News news where news.owner=:owner "
      + "order by news.newsItem.timePosted desc")
  public List<News> findAllByOwner(@Param("owner") SocialOrganization owner);

  @Query("select news from News news where news.owner=:owner "
      + "order by news.newsItem.timePosted desc")
  public List<News> findLatestByOwner(@Param("owner") SocialOrganization owner,
      Pageable pageable);

  @Query("select news from News news where news.owner=:owner "
      + "and (news.newsItem.timePosted < :afterDate or "
      + "news.newsItem.timePosted = :afterDate) "
      + "order by news.newsItem.timePosted desc")
  public List<News> findMoreByOwner(@Param("owner") SocialOrganization owner,
      @Param("afterDate") Date afterDate, Pageable pageable);

  @Query("delete from News news where news.id=:id")
  public void deleteNewsItem(@Param("id") Long id);

}
