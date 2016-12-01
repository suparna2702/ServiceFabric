package com.similan.domain.entity.wall.comment;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.similan.domain.entity.comment.CommentReply;
import com.similan.domain.entity.wall.WallEntry;
import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "CommentReplyPostedWallEntry")
@DiscriminatorValue("COMMENT_REPLY_POSTED")
public class CommentReplyPostedWallEntry extends WallEntry {

  @ManyToOne
  @JoinColumn
  private CommentReply commentReply;

  protected CommentReplyPostedWallEntry() {

  }

  public CommentReplyPostedWallEntry(int number, Date date) {
    super(WallEntryType.COMMENT_REPLY_POSTED, number, date);
    this.setShowWall(Boolean.TRUE);
  }

  public CommentReply getCommentReply() {
    return commentReply;
  }

  public void setCommentReply(CommentReply commentReply) {
    this.commentReply = commentReply;
  }

}
