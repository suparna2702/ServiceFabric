package com.similan.domain.repository.feed.jpa;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.feed.FeedEntry;
import com.similan.domain.entity.feed.SurveyRequestFeedEntry;
import com.similan.domain.entity.poll.PollEvent;

public interface JpaFeedEntryRepository extends JpaRepository<FeedEntry, Long> {

  @Query("select entry from SurveyRequestFeedEntry entry"
      + " where entry.pollEvent = :pollEvent")
  SurveyRequestFeedEntry find(@Param("pollEvent") PollEvent pollEvent);

  @Query("select entry from FeedEntry entry"
      + " where entry.feed.owner.name = :feedOwnerName"
      + " and entry.number = :number")
  FeedEntry findOne(@Param("feedOwnerName") String feedOwnerName,
      @Param("number") int number);

  @Query("select entry from FeedEntry entry"
      + " where entry.feed.owner.name = :feedOwnerName"
      + " and entry.showAll = :show"
      + " order by entry.date desc, entry.number desc")
  List<FeedEntry> findLatestByFeed(
      @Param("feedOwnerName") String feedOwnerName,
      @Param("show") Boolean show, Pageable pageable);

  @Query("select entry from FeedEntry entry"
      + " where entry.feed.owner.name = :feedOwnerName"
      + " and entry.number < :afterNumber"
      + " and entry.showAll = :show"
      + " order by entry.date desc, entry.number desc")
  List<FeedEntry> findMoreByFeed(@Param("feedOwnerName") String feedOwnerName,
      @Param("afterNumber") int afterNumber, @Param("show") Boolean show,
      Pageable pageable);

}
