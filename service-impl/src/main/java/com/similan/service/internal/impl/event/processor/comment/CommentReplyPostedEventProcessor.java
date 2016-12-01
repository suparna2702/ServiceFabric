package com.similan.service.internal.impl.event.processor.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.comment.Comment;
import com.similan.domain.entity.comment.CommentReply;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.comment.CommentReplyPostedWallEntry;
import com.similan.domain.repository.comment.CommentReplyRepository;
import com.similan.domain.repository.wall.comment.CommentWallEntryRepository;
import com.similan.service.internal.api.event.io.comment.CommentReplyPostedEvent;

@Component
public class CommentReplyPostedEventProcessor extends
    AbstractCommentPostedEventProcessor<CommentReplyPostedEvent> {
  @Autowired
  private CommentReplyRepository commentReplyRepository;
  @Autowired
  private CommentWallEntryRepository commentWallEntryRepository;

  @Override
  public void preProcess(CommentReplyPostedEvent event) {
    CommentReply commentReply = this.commentReplyRepository.findOne(event
        .getCommentReplyId());
    Comment comment = commentReply.getComment();
    Wall wall = getWall(comment);
    if (wall == null) {
      return;
    }
    CommentReplyPostedWallEntry commentReplyEntry = this.commentWallEntryRepository
        .createCommentReplyPostedEntry(wall, commentReply);
    post(commentReplyEntry);
  }
}
