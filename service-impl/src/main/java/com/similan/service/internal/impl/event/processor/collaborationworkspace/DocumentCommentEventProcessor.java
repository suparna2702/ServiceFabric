package com.similan.service.internal.impl.event.processor.collaborationworkspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.SharedDocument;
import com.similan.domain.entity.comment.Comment;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.collaborationworkspace.CollaborationWorkspaceWallEntry;
import com.similan.domain.repository.collaborationworkspace.SharedDocumentRepository;
import com.similan.domain.repository.comment.CommentRepository;
import com.similan.domain.repository.community.SocialActorRepository;
import com.similan.service.internal.api.event.io.collaborationworkspace.DocumentCommentEvent;

@Component
public class DocumentCommentEventProcessor extends
    CollaborationWorkspaceEventProcessor<DocumentCommentEvent> {
  @Autowired
  private SharedDocumentRepository sharedDocumentRepository;
  @Autowired
  private CommentRepository commentRepository;
  @Autowired
  private SocialActorRepository socialActorRepository;

  @Override
  protected CollaborationWorkspaceWallEntry createWallEntry(
      DocumentCommentEvent event) {
    long sharedDocumentId = event.getDocumentId();
    SharedDocument sharedDocument = sharedDocumentRepository
        .findOne(sharedDocumentId);

    long commentId = event.getCommentId();
    Comment docComment = this.commentRepository.findOne(commentId);

    long commenterId = event.getInitiatorId();
    SocialActor commenter = this.socialActorRepository.findOne(commenterId);

    CollaborationWorkspace workspace = sharedDocument.getWorkspace();
    Wall wall = getWall(workspace);
    return getCollaborationWorkspaceWallEntryRepository()
        .createDocumentCommentWallEntry(wall, sharedDocument, commenter,
            docComment);
  }
}
