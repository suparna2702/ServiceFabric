package com.similan.domain.entity.wall.collaborationworkspace;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.similan.domain.entity.comment.Comment;
import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "DocumentCommentWallEntry")
@DiscriminatorValue("COLLABORATION_WORKSPACE_DOCUMENT_COMMENT")
public class DocumentCommentWallEntry extends
    CollaborationWorkspaceDocumentWallEntry {

  @ManyToOne
  private Comment docComment;

  public DocumentCommentWallEntry() {

  }

  public DocumentCommentWallEntry(int number, Date date, Comment docComment) {
    super(WallEntryType.COLLABORATION_WORKSPACE_DOCUMENT_COMMENT, number, date);
    this.setShowWall(Boolean.TRUE);
    this.docComment = docComment;
  }

  public Comment getDocComment() {
    return docComment;
  }

  public void setDocComment(Comment docComment) {
    this.docComment = docComment;
  }

}
