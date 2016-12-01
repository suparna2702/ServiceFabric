package com.similan.domain.repository.wall;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.advertisement.DisplayNotice;
import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.partner.Partnership;
import com.similan.domain.entity.wall.IWallEntrySubject;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.WallEntry;
import com.similan.domain.entity.wall.advertisement.DisplayNoticeViewedWallEntry;
import com.similan.domain.entity.wall.network.BusinessProfileUpdateWallEntry;
import com.similan.domain.entity.wall.network.BusinessProfileViewWallEntry;
import com.similan.domain.entity.wall.network.MemberProfileUpdateWallEntry;
import com.similan.domain.entity.wall.network.MemberProfileViewWallEntry;
import com.similan.domain.entity.wall.partner.PartnerProgramJoinWallEntry;
import com.similan.domain.entity.wall.wall.WallPost;
import com.similan.domain.repository.common.GenericReferenceRepository;
import com.similan.domain.repository.wall.jpa.JpaWallEntryRepository;
import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.wall.dto.basic.WallEntryType;

@Repository
public class WallEntryRepository extends AbstractWallEntryRepository<WallEntry> {

  @Autowired
  private JpaWallEntryRepository repository;

  @Autowired
  private GenericReferenceRepository genericReferenceRepository;

  public WallEntry save(WallEntry entity) {
    return repository.save(entity);
  }

  public WallEntry findOne(Long id) {
    return repository.findOne(id);
  }

  public Long findDocumentDownloadedWallEntryCount(Long sharedDocumentId,
      long wallContainerId) {
    return repository.findDocumentDownloadedWallEntryCount(sharedDocumentId,
        wallContainerId);
  }

  public Long findDocumentViewedWallEntryCount(Long viewedDocument,
      long wallContainerId) {
    return repository.findDocumentViewedWallEntryCount(viewedDocument,
        wallContainerId);
  }

  public Long findCommentWallEntryCount(Long commentableId, Long wallContainerId) {
    return repository.findCommentWallEntryCount(commentableId, wallContainerId);
  }

  public Long findWallEntryCount(EntityType wallContainerType,
      WallEntryType entryType, long wallContainerId) {
    return repository.findWallEntryCount(wallContainerType, entryType,
        wallContainerId);
  }

  public WallEntry findOne(EntityType wallContainerType, long wallContainerId,
      int number) {
    return repository.findOne(wallContainerType, wallContainerId, number);
  }

  public Long findWallEntryCountByType(WallEntryType entryType) {
    return repository.findWallEntryCountByType(entryType);
  }

  public List<WallEntry> findLatestByWall(EntityType wallContainerType,
      long wallContainerId, Pageable pageable) {
    return repository.findLatestByWall(wallContainerType, wallContainerId,
        Boolean.TRUE, pageable);
  }

  public List<WallEntry> findLatestByWall(List<SocialActor> initiators,
      EntityType wallContainerType, long wallContainerId, Pageable pageable) {
    return repository.findLatestByWall(initiators, wallContainerType,
        wallContainerId, Boolean.TRUE, pageable);
  }

  public List<WallEntry> findAllExternalLinkReferencePostByWall(
      List<SocialActor> initiators, EntityType wallContainerType,
      long wallContainerId) {
    return repository.findAllExternalLinkReferencePostByWall(initiators,
        wallContainerType, wallContainerId);
  }

  public List<WallEntry> findAllExternalLinkReferencePostByWall(
      EntityType wallContainerType, long wallContainerId) {
    return repository.findAllExternalLinkReferencePostByWall(wallContainerType,
        wallContainerId);
  }

  public List<WallEntry> findMoreByWall(EntityType wallContainerType,
      long wallContainerId, int afterNumber, Pageable pageable) {
    return repository.findMoreByWall(wallContainerType, wallContainerId,
        afterNumber, Boolean.TRUE, pageable);
  }

  public List<WallEntry> findMoreByWall(List<SocialActor> initiators,
      EntityType wallContainerType, long wallContainerId, int afterNumber,
      Pageable pageable) {
    return repository.findMoreByWall(initiators, wallContainerType,
        wallContainerId, afterNumber, Boolean.TRUE, pageable);
  }

  public List<WallEntry> findByDateAndWall(EntityType wallContainerType,
      long wallContainerId, Date fromDate, Date toDate) {
    return repository.findByDateAndWall(wallContainerType, wallContainerId,
        fromDate, toDate);
  }

  public List<WallEntry> findWorkspaceEntryBySubject(long wallContainerId,
      Long subjectId, EntityType subjectType, Pageable pageable) {
    return repository.findWorkspaceEntryBySubject(wallContainerId, subjectId,
        subjectType, pageable);
  }

  public List<WallEntry> findWorkspaceEntryBySubject(Long subjectId,
      EntityType subjectType, Pageable pageable) {
    return repository.findWorkspaceEntryBySubject(subjectId, subjectType,
        pageable);
  }

  public List<WallEntry> findByWallEntryType(EntityType wallContainerType,
      long wallContainerId, WallEntryType wallEntryType) {
    return repository.findByWallEntryType(wallContainerType, wallContainerId,
        wallEntryType);
  }

  public PartnerProgramJoinWallEntry createPartnerJoinWallEntry(Wall wall,
      Partnership partnership) {

    int number = getNextNumber(wall);
    Date date = new Date();

    PartnerProgramJoinWallEntry wallEntry = new PartnerProgramJoinWallEntry(
        number, date);

    wallEntry.setPartnership(partnership);
    wallEntry.setPartnerProgram(partnership.getPartnerProgram());
    GenericReference<IWallEntrySubject> subject = genericReferenceRepository
        .create((IWallEntrySubject) partnership.getPartnerProgram()
            .getProgramOwner());

    wallEntry.setSubject(subject);
    created(wall, partnership.getPartner(), wallEntry);

    return wallEntry;
  }

  public WallPost createPost(Wall wall, SocialActor initiator, String content) {
    int number = getNextNumber(wall);
    Date date = new Date();

    WallPost entry = new WallPost(number, date, content);
    created(wall, initiator, entry);

    return entry;
  }

  public BusinessProfileUpdateWallEntry createBusinessProfileUpdateWallEntry(
      Wall wall, SocialActor initiator, SocialActor profileOwner) {

    int number = getNextNumber(wall);
    Date date = new Date();

    GenericReference<IWallEntrySubject> subject = genericReferenceRepository
        .create((IWallEntrySubject) profileOwner);

    BusinessProfileUpdateWallEntry entry = new BusinessProfileUpdateWallEntry(
        number, date);

    entry.setInitiator(initiator);
    entry.setSubject(subject);
    created(wall, initiator, entry);

    return entry;
  }

  public BusinessProfileViewWallEntry createBusinessProfileViewWallEntry(
      Wall wall, SocialActor initiator, SocialActor profileOwner) {

    int number = getNextNumber(wall);
    Date date = new Date();

    GenericReference<IWallEntrySubject> subject = genericReferenceRepository
        .create((IWallEntrySubject) profileOwner);

    BusinessProfileViewWallEntry entry = new BusinessProfileViewWallEntry(
        number, date);

    entry.setInitiator(initiator);
    entry.setSubject(subject);
    created(wall, initiator, entry);

    return entry;
  }

  public MemberProfileUpdateWallEntry createMemberProfileUpdateWallEntry(
      Wall wall, SocialActor initiator, SocialActor profileOwner) {

    int number = getNextNumber(wall);
    Date date = new Date();

    GenericReference<IWallEntrySubject> subject = genericReferenceRepository
        .create((IWallEntrySubject) profileOwner);

    MemberProfileUpdateWallEntry entry = new MemberProfileUpdateWallEntry(
        number, date);

    entry.setInitiator(initiator);
    entry.setSubject(subject);
    created(wall, initiator, entry);

    return entry;
  }

  public MemberProfileViewWallEntry createMemberProfileViewWallEntry(Wall wall,
      SocialActor initiator, SocialActor profileOwner) {

    int number = getNextNumber(wall);
    Date date = new Date();

    GenericReference<IWallEntrySubject> subject = genericReferenceRepository
        .create((IWallEntrySubject) profileOwner);

    MemberProfileViewWallEntry entry = new MemberProfileViewWallEntry(number,
        date);

    entry.setInitiator(initiator);
    entry.setSubject(subject);
    created(wall, initiator, entry);

    return entry;
  }

  public DisplayNoticeViewedWallEntry createDisplayNoticeViewedWallEntry(
      Wall wall, SocialActor initiator, DisplayNotice notice) {

    int number = getNextNumber(wall);
    Date date = new Date();

    GenericReference<IWallEntrySubject> subject = genericReferenceRepository
        .create((IWallEntrySubject) notice);

    DisplayNoticeViewedWallEntry entry = new DisplayNoticeViewedWallEntry(
        number, date);
    entry.setInitiator(initiator);
    entry.setSubject(subject);
    created(wall, initiator, entry);

    return entry;
  }

}
