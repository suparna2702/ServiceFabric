package com.similan.domain.repository.wall.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.wall.WallEntry;
import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.wall.dto.basic.WallEntryType;

public interface JpaWallEntryRepository extends JpaRepository<WallEntry, Long> {

  @Query("select count(entry) from WallEntry entry"
      + " where entry.class in ('COLLABORATION_WORKSPACE_DOCUMENT_DOWNLOADED', 'CONTENT_WORKSPACE_DOCUMENT_DOWNLOADED')"
      + " and entry.subject.id = :subjectId"
      + " and entry.wall.container.id = :wallContainerId")
  Long findDocumentDownloadedWallEntryCount(@Param("subjectId") Long subjectId,
      @Param("wallContainerId") long wallContainerId);

  @Query("select count(entry) from WallEntry entry"
      + " where entry.class in ('COLLABORATION_WORKSPACE_DOCUMENT_VIEWED', 'CONTENT_WORKSPACE_DOCUMENT_VIEWED')"
      + " and entry.subject.id = :subjectId"
      + " and entry.wall.container.id = :wallContainerId")
  Long findDocumentViewedWallEntryCount(@Param("subjectId") Long subjectId,
      @Param("wallContainerId") long wallContainerId);

  @Query("select count(entry) from CommentPostedWallEntry entry"
      + " where entry.comment.commentable.id = :commentableId"
      + " and entry.wall.container.id = :wallContainerId")
  Long findCommentWallEntryCount(@Param("commentableId") Long commentableId,
      @Param("wallContainerId") Long wallContainerId);

  @Query("select count(entry) from WallEntry entry"
      + " where entry.wall.container.type = :wallContainerType"
      + " and entry.type = :entryType"
      + " and entry.wall.container.id = :wallContainerId")
  Long findWallEntryCount(
      @Param("wallContainerType") EntityType wallContainerType,
      @Param("entryType") WallEntryType entryType,
      @Param("wallContainerId") long wallContainerId);

  @Query("select count(entry) from WallEntry entry where entry.type = :entryType")
  Long findWallEntryCountByType(@Param("entryType") WallEntryType entryType);

  @Query("select entry from WallEntry entry"
      + " where entry.wall.container.type = :wallContainerType"
      + " and entry.wall.container.id = :wallContainerId"
      + " and entry.number = :number")
  WallEntry findOne(@Param("wallContainerType") EntityType wallContainerType,
      @Param("wallContainerId") long wallContainerId,
      @Param("number") int number);

  @Query("select entry from WallEntry entry"
      + " where entry.wall.container.type = :wallContainerType"
      + " and entry.wall.container.id = :wallContainerId"
      + " and entry.showWall = :show"
      + " order by entry.date desc, entry.number desc")
  List<WallEntry> findLatestByWall(
      @Param("wallContainerType") EntityType wallContainerType,
      @Param("wallContainerId") long wallContainerId,
      @Param("show") Boolean show, Pageable pageable);

  @Query("select entry from WallEntry entry"
      + " where entry.initiator in (:initiators)"
      + " and entry.wall.container.type = :wallContainerType"
      + " and entry.wall.container.id = :wallContainerId"
      + " and entry.showWall = :show"
      + " order by entry.date desc, entry.number desc")
  List<WallEntry> findLatestByWall(
      @Param("initiators") List<SocialActor> initiators,
      @Param("wallContainerType") EntityType wallContainerType,
      @Param("wallContainerId") long wallContainerId,
      @Param("show") Boolean show, Pageable pageable);

  @Query("select entry from WallEntry entry"
      + " where entry.wall.container.type = :wallContainerType"
      + " and entry.wall.container.id = :wallContainerId"
      + " and entry.showWall = :show" + " and entry.number < :afterNumber"
      + " order by entry.date desc, entry.number desc")
  List<WallEntry> findMoreByWall(
      @Param("wallContainerType") EntityType wallContainerType,
      @Param("wallContainerId") long wallContainerId,
      @Param("afterNumber") int afterNumber, @Param("show") Boolean show,
      Pageable pageable);

  @Query("select entry from WallEntry entry"
      + " where entry.initiator in (:initiators)"
      + " and entry.wall.container.type = :wallContainerType"
      + " and entry.wall.container.id = :wallContainerId"
      + " and entry.showWall = :show" + " and entry.number < :afterNumber"
      + " order by entry.date desc, entry.number desc")
  List<WallEntry> findMoreByWall(
      @Param("initiators") List<SocialActor> initiators,
      @Param("wallContainerType") EntityType wallContainerType,
      @Param("wallContainerId") long wallContainerId,
      @Param("afterNumber") int afterNumber, @Param("show") Boolean show,
      Pageable pageable);

  @Query("select entry from WallEntry entry"
      + " where entry.wall.container.type = :wallContainerType"
      + " and entry.wall.container.id = :wallContainerId"
      + " and (entry.date >= :fromDate and" + "   entry.date < :toDate)"
      + " order by entry.date asc, entry.number asc")
  List<WallEntry> findByDateAndWall(
      @Param("wallContainerType") EntityType wallContainerType,
      @Param("wallContainerId") long wallContainerId,
      @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

  @Query("select entry from WallEntry entry"
      + " where entry.wall.container.type = :wallContainerType"
      + " and entry.wall.container.id = :wallContainerId"
      + " and entry.type = :wallEntryType"
      + " order by entry.date desc, entry.number desc")
  public List<WallEntry> findByWallEntryType(
      @Param("wallContainerType") EntityType wallContainerType,
      @Param("wallContainerId") long wallContainerId,
      @Param("wallEntryType") WallEntryType wallEntryType);

  @Query("select entry from WallEntry entry where"
      + " entry.wall.container.id = :wallContainerId"
      + " and entry.subject.id = :subjectId"
      + " and entry.subject.type = :subjectType"
      + " order by entry.date desc, entry.number desc")
  public List<WallEntry> findWorkspaceEntryBySubject(
      @Param("wallContainerId") long wallContainerId,
      @Param("subjectId") Long subjectId,
      @Param("subjectType") EntityType subjectType, Pageable pageable);

  @Query("select entry from WallEntry entry where"
      + " entry.subject.id = :subjectId"
      + " and entry.subject.type = :subjectType"
      + " order by entry.date desc, entry.number desc")
  public List<WallEntry> findWorkspaceEntryBySubject(
      @Param("subjectId") Long subjectId,
      @Param("subjectType") EntityType subjectType, Pageable pageable);

  @Query("select entry from WallEntry entry"
      + " where entry.initiator in (:initiators)"
      + " and entry.wall.container.type = :wallContainerType"
      + " and entry.wall.container.id = :wallContainerId"
      + " and entry.class in ('WALL_POST')" 
      + " and entry.link is not null"
      + " order by entry.date desc, entry.number desc")
  List<WallEntry> findAllExternalLinkReferencePostByWall(
      @Param("initiators") List<SocialActor> initiators,
      @Param("wallContainerType") EntityType wallContainerType,
      @Param("wallContainerId") long wallContainerId);

  @Query("select entry from WallEntry entry"
      + " where entry.wall.container.type = :wallContainerType"
      + " and entry.wall.container.id = :wallContainerId"
      + " and entry.class in ('WALL_POST')" 
      + " and entry.link is not null"
      + " order by entry.date desc, entry.number desc")
  List<WallEntry> findAllExternalLinkReferencePostByWall(
      @Param("wallContainerType") EntityType wallContainerType,
      @Param("wallContainerId") long wallContainerId);

}
