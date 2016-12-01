package com.similan.domain.repository.bookmark.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.bookmark.Bookmark;
import com.similan.domain.entity.community.SocialActor;
import com.similan.service.api.base.dto.key.EntityType;

public interface JpaBookmarkRepository extends JpaRepository<Bookmark, Long> {

  @Query("select bookmark from Bookmark bookmark"
      + " where bookmark.owner.name = :ownerName"
      + " and bookmark.bookmarkable.type = :bookmarkableType"
      + " and bookmark.name = :name")
  Bookmark findOne(@Param("ownerName") String ownerName,
      @Param("bookmarkableType") EntityType bookmarkableType,
      @Param("name") String name);

  @Query("select bookmark from Bookmark bookmark"
      + " where bookmark.bookmarkable.type = :bookmarkableType"
      + " and bookmark.bookmarkable.id = :bookmarkableId")
  List<Bookmark> findByBookmarkable(
      @Param("bookmarkableType") EntityType bookmarkableType,
      @Param("bookmarkableId") long bookmarkableId);

  @Query("select bookmark from Bookmark bookmark"
      + " where bookmark.owner =:owner"
      + " and bookmark.bookmarkable.type = :bookmarkableType"
      + " and bookmark.bookmarkable.id = :bookmarkableId")
  List<Bookmark> findByOwnerAndBookmarkable(@Param("owner") SocialActor owner,
      @Param("bookmarkableType") EntityType bookmarkableType,
      @Param("bookmarkableId") long bookmarkableId);

  List<Bookmark> findByOwner(SocialActor owner);
}
