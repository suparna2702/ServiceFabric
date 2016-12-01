package com.similan.service.internal.api.event.io.comment;

import com.similan.service.internal.api.event.io.Event;

public class CommentReplyPostedEvent extends Event {

  private static final long serialVersionUID = 1L;

  private Long commentReplyId;

  public CommentReplyPostedEvent(Long commentReplyId) {
    this.commentReplyId = commentReplyId;
  }

  public Long getCommentReplyId() {
    return commentReplyId;
  }

}
