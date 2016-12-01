package com.similan.domain.entity.wall.comment;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.similan.domain.entity.comment.Comment;
import com.similan.domain.entity.wall.WallEntry;
import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "CommentPostedWallEntry")
@DiscriminatorValue("COMMENT_POSTED")
public class CommentPostedWallEntry extends WallEntry {

  @ManyToOne
  @JoinColumn
  private Comment comment;

  protected CommentPostedWallEntry() {

  }

  public CommentPostedWallEntry(int number, Date date) {
    super(WallEntryType.COMMENT_POSTED, number, date);
    this.setShowWall(Boolean.TRUE);
  }

  public Comment getComment() {
    return comment;
  }

  public void setComment(Comment comment) {
    this.comment = comment;
  }

}
