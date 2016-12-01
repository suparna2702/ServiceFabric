package com.similan.service.internal.api.event.io.comment;

import com.similan.service.internal.api.event.io.Event;

public class CommentPostedEvent extends Event {

  private static final long serialVersionUID = 1L;

  private Long commentId;

  public CommentPostedEvent(Long commentId) {
    this.commentId = commentId;
  }

  public Long getCommentId() {
    return commentId;
  }

}
