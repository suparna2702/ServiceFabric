package com.similan.service.internal.impl.event.processor.contentworkspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.comment.Comment;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.document.Document;
import com.similan.domain.entity.managementworkspace.ManagementWorkspace;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.contentworkspace.ContentWorkspaceWallEntry;
import com.similan.domain.repository.comment.CommentRepository;
import com.similan.domain.repository.document.DocumentRepository;
import com.similan.service.internal.api.event.io.contentworkspace.ContentWorkspaceDocumentActivityEvent;

@Component
public class ContentWorkspaceDocumentActivityEventProcessor extends
    ContentWorkspaceEventProcessor<ContentWorkspaceDocumentActivityEvent> {
  @Autowired
  private DocumentRepository documentRepository;
  @Autowired
  private CommentRepository commentRepository;

  @Override
  protected ContentWorkspaceWallEntry createWallEntry(
      ContentWorkspaceDocumentActivityEvent event) {

    Document document = this.documentRepository.findOne(event.getDocumentId());
    SocialActor initiator = this.getSocialActorRepository().findOne(
        event.getInitiatorId());

    ManagementWorkspace workspace = this.getManagementWorkspaceRepository()
        .findOne(event.getContentWorkspaceId());
    Wall wall = getWall(workspace);

    ContentWorkspaceWallEntry retEntry = null;
    switch (event.getEntryType()) {
    case CONTENT_WORKSPACE_DOCUMENT_CHECKOUT: {
      retEntry = this.getContentWorkspaceWallEntryRepository()
          .createDocumentViewedWallEntry(wall, document, initiator);
      break;
    }
    case CONTENT_WORKSPACE_DOCUMENT_CHECKIN: {
      retEntry = this.getContentWorkspaceWallEntryRepository()
          .createDocumentCheckInWallEntry(wall, document, initiator);
      break;
    }
    case CONTENT_WORKSPACE_DOCUMENT_VIEWED: {
      retEntry = this.getContentWorkspaceWallEntryRepository()
          .createDocumentViewedWallEntry(wall, document, initiator);
      break;
    }
    case CONTENT_WORKSPACE_DOCUMENT_DOWNLOADED:
      retEntry = this.getContentWorkspaceWallEntryRepository()
          .createDocumentDownloadWallEntry(wall, document, initiator);
      break;
    case CONTENT_WORKSPACE_DOCUMENT_UPLOADED:
      retEntry = this.getContentWorkspaceWallEntryRepository()
          .createDocumentUploadWallEntry(wall, document, initiator);
      break;
    case CONTENT_WORKSPACE_DOCUMENT_UPDATED:
      retEntry = this.getContentWorkspaceWallEntryRepository()
          .createDocumentUpdateWallEntry(wall, document, initiator);
      break;
    case CONTENT_WORKSPACE_DOCUMENT_COMMENT:
      Comment docComment = this.commentRepository.findOne(event.getCommentId());
      retEntry = this
          .getContentWorkspaceWallEntryRepository()
          .createDocumentCommentWallEntry(wall, document, initiator, docComment);
      break;
    default:
      break;
    }
    return retEntry;
  }
}
