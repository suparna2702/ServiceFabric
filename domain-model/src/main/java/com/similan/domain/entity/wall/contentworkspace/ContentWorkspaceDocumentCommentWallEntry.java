package com.similan.domain.entity.wall.contentworkspace;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.similan.domain.entity.comment.Comment;
import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "ContentWorkspaceDocumentCommentWallEntry")
@DiscriminatorValue("CONTENT_WORKSPACE_DOCUMENT_COMMENT")
public class ContentWorkspaceDocumentCommentWallEntry extends
    ContentWorkspaceDocumentWallEntry {

  @ManyToOne
  private Comment docComment;

  public ContentWorkspaceDocumentCommentWallEntry() {

  }

  public ContentWorkspaceDocumentCommentWallEntry(int number, Date date,
      Integer documentVersion, Comment docComment) {
    super(WallEntryType.CONTENT_WORKSPACE_DOCUMENT_COMMENT, number, date,
        documentVersion);
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
