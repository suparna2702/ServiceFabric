package com.similan.domain.repository.wall.collaborationworkspace;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspaceParticipation;
import com.similan.domain.entity.collaborationworkspace.SharedDocument;
import com.similan.domain.entity.collaborationworkspace.Task;
import com.similan.domain.entity.comment.Comment;
import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.activity.News;
import com.similan.domain.entity.wall.IWallEntrySubject;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.collaborationworkspace.CollaborationWorkspaceAccessedWallEntry;
import com.similan.domain.entity.wall.collaborationworkspace.CollaborationWorkspaceDocumentWallEntry;
import com.similan.domain.entity.wall.collaborationworkspace.CollaborationWorkspaceExternalSharedWallEntry;
import com.similan.domain.entity.wall.collaborationworkspace.CollaborationWorkspaceInNetworkSharedWallEntry;
import com.similan.domain.entity.wall.collaborationworkspace.CollaborationWorkspaceWallEntry;
import com.similan.domain.entity.wall.collaborationworkspace.DocumentCommentWallEntry;
import com.similan.domain.entity.wall.collaborationworkspace.DocumentDownloadedWallEntry;
import com.similan.domain.entity.wall.collaborationworkspace.DocumentSharedWallEntry;
import com.similan.domain.entity.wall.collaborationworkspace.DocumentSharedWithOtherWorkspaceWallEntry;
import com.similan.domain.entity.wall.collaborationworkspace.DocumentUnsharedWallEntry;
import com.similan.domain.entity.wall.collaborationworkspace.DocumentUpdateWallEntry;
import com.similan.domain.entity.wall.collaborationworkspace.DocumentViewedWallEntry;
import com.similan.domain.entity.wall.collaborationworkspace.NewsPostWallEntry;
import com.similan.domain.entity.wall.collaborationworkspace.ParticipantJoinedWallEntry;
import com.similan.domain.entity.wall.collaborationworkspace.TaskCreatedWallEntry;
import com.similan.domain.repository.common.GenericReferenceRepository;
import com.similan.domain.repository.wall.AbstractWallEntryRepository;
import com.similan.domain.share.ExternalShared;
import com.similan.domain.share.InNetworkShared;
import com.similan.service.api.wall.dto.basic.collaborationworkspace.CollaborationWorkspaceAccessedType;

/**
 * 
 * @author suparna1108
 *
 */
@Repository
public class CollaborationWorkspaceWallEntryRepository extends
    AbstractWallEntryRepository<CollaborationWorkspaceWallEntry> {

  @Autowired
  private GenericReferenceRepository genericReferenceRepository;

  public ParticipantJoinedWallEntry createMemberJoinedEntry(Wall wall,
      CollaborationWorkspaceParticipation participation) {

    GenericReference<IWallEntrySubject> subject = genericReferenceRepository
        .create((IWallEntrySubject) participation);

    int number = getNextNumber(wall);
    Date date = new Date();

    ParticipantJoinedWallEntry entry = new ParticipantJoinedWallEntry(number,
        date);

    entry.setSubject(subject);
    SocialActor initiator = participation.getParticipant();
    created(wall, initiator, entry);

    return entry;
  }

  public CollaborationWorkspaceAccessedWallEntry createWorkspaceAccessedWallEntry(
      Wall wall, SocialActor accessor, CollaborationWorkspace workspace,
      CollaborationWorkspaceAccessedType accessType) {

    int number = getNextNumber(wall);
    Date date = new Date();

    CollaborationWorkspaceAccessedWallEntry entry = new CollaborationWorkspaceAccessedWallEntry(
        number, date);
    GenericReference<IWallEntrySubject> subject = genericReferenceRepository
        .create((IWallEntrySubject) workspace);
    entry.setSubject(subject);
    entry.setAccessType(accessType);
    created(wall, accessor, entry);
    return entry;
  }

  private void setCollaborationWorkspaceDocumentWallEntryProperty(
      CollaborationWorkspaceDocumentWallEntry entry, SharedDocument document) {
    GenericReference<IWallEntrySubject> subject = genericReferenceRepository
        .create((IWallEntrySubject) document);
    entry.setSubject(subject);

    entry.setDocumentVersion(document.getDocument().getLastInstance()
        .getVersion());
  }

  public DocumentSharedWallEntry createDocumentSharedEntry(Wall wall,
      SharedDocument document) {

    int number = getNextNumber(wall);
    Date date = new Date();

    DocumentSharedWallEntry entry = new DocumentSharedWallEntry(number, date);
    this.setCollaborationWorkspaceDocumentWallEntryProperty(entry, document);
    SocialActor initiator = document.getCreator();
    created(wall, initiator, entry);
    return entry;
  }

  public DocumentViewedWallEntry createDocumentViewedWallEntry(Wall wall,
      SharedDocument document, SocialActor viewer) {

    int number = getNextNumber(wall);
    Date date = new Date();

    DocumentViewedWallEntry entry = new DocumentViewedWallEntry(number, date);
    this.setCollaborationWorkspaceDocumentWallEntryProperty(entry, document);
    entry.setInitiator(viewer);
    created(wall, viewer, entry);

    return entry;
  }

  public DocumentCommentWallEntry createDocumentCommentWallEntry(Wall wall,
      SharedDocument document, SocialActor viewer, Comment docComment) {

    int number = getNextNumber(wall);
    Date date = new Date();

    DocumentCommentWallEntry entry = new DocumentCommentWallEntry(number, date,
        docComment);
    this.setCollaborationWorkspaceDocumentWallEntryProperty(entry, document);
    entry.setInitiator(viewer);
    created(wall, viewer, entry);

    return entry;
  }

  public TaskCreatedWallEntry createTaskCreatedEntry(Wall wall, Task task) {

    GenericReference<IWallEntrySubject> subject = genericReferenceRepository
        .create((IWallEntrySubject) task);

    int number = getNextNumber(wall);
    Date date = new Date();

    TaskCreatedWallEntry entry = new TaskCreatedWallEntry(number, date);

    entry.setSubject(subject);
    SocialActor initiator = task.getCreator();
    created(wall, initiator, entry);
    return entry;

  }

  public DocumentDownloadedWallEntry createDocumentDownloadedEntry(Wall wall,
      SocialActor downloader, SharedDocument document) {
    int number = getNextNumber(wall);
    Date date = new Date();
    DocumentDownloadedWallEntry entry = new DocumentDownloadedWallEntry(number,
        date);
    this.setCollaborationWorkspaceDocumentWallEntryProperty(entry, document);
    created(wall, downloader, entry);
    return entry;
  }

  public DocumentSharedWithOtherWorkspaceWallEntry createDocumentSharedWithOtherWorkspaceWallEntry(
      Wall wall, SharedDocument sharedDocument,
      CollaborationWorkspace workspaceFrom) {

    int number = getNextNumber(wall);
    Date date = new Date();

    DocumentSharedWithOtherWorkspaceWallEntry wallEntry = new DocumentSharedWithOtherWorkspaceWallEntry(
        number, date);

    this.setCollaborationWorkspaceDocumentWallEntryProperty(wallEntry,
        sharedDocument);
    wallEntry.setSharedFromSpace(workspaceFrom);

    created(wall, sharedDocument.getCreator(), wallEntry);
    return wallEntry;
  }

  public DocumentUpdateWallEntry createDocumentUpdatedWallEntry(Wall wall,
      SharedDocument sharedDocument, SocialActor updater) {

    int number = getNextNumber(wall);
    Date date = new Date();

    DocumentUpdateWallEntry wallEntry = new DocumentUpdateWallEntry(number,
        date, sharedDocument.getDocument().getLastInstance().getVersion());
    this.setCollaborationWorkspaceDocumentWallEntryProperty(wallEntry,
        sharedDocument);

    created(wall, sharedDocument.getCreator(), wallEntry);
    return wallEntry;
  }

  public DocumentUnsharedWallEntry createDocumentUnsharedWallEntry(Wall wall,
      SharedDocument sharedDocument, SocialActor unsharer) {

    int number = getNextNumber(wall);
    Date date = new Date();

    DocumentUnsharedWallEntry wallEntry = new DocumentUnsharedWallEntry(number,
        date);
    this.setCollaborationWorkspaceDocumentWallEntryProperty(wallEntry,
        sharedDocument);
    created(wall, unsharer, wallEntry);

    return wallEntry;
  }

  public NewsPostWallEntry createNewsWallEntry(Wall wall, News news) {
    int number = getNextNumber(wall);
    Date date = new Date();

    NewsPostWallEntry wallEntry = new NewsPostWallEntry(number, date, news);
    created(wall, news.getCreator(), wallEntry);
    return wallEntry;

  }

  public CollaborationWorkspaceExternalSharedWallEntry createCollaborationWorkspaceExternalSharedWallEntry(
      Wall wall, ExternalShared shared, SharedDocument sharedDocument) {
    int number = getNextNumber(wall);
    Date date = new Date();

    CollaborationWorkspaceExternalSharedWallEntry wallEntry = new CollaborationWorkspaceExternalSharedWallEntry(
        shared, number, date);
    this.setCollaborationWorkspaceDocumentWallEntryProperty(wallEntry,
        sharedDocument);

    created(wall, shared.getSharedBy(), wallEntry);
    return wallEntry;
  }

  public CollaborationWorkspaceInNetworkSharedWallEntry createCollaborationWorkspaceInNetworkSharedWallEntry(
      Wall wall, InNetworkShared inNetworkShared, SharedDocument sharedDocument) {

    int number = getNextNumber(wall);
    Date date = new Date();

    CollaborationWorkspaceInNetworkSharedWallEntry wallEntry = new CollaborationWorkspaceInNetworkSharedWallEntry(
        inNetworkShared, number, date);
    this.setCollaborationWorkspaceDocumentWallEntryProperty(wallEntry,
        sharedDocument);

    created(wall, inNetworkShared.getSharedBy(), wallEntry);
    return wallEntry;
  }
}
