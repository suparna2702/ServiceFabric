package com.similan.service.api.comment.dto.basic;

import java.util.Date;

import com.similan.service.api.comment.dto.key.CommentReplyKey;
import com.similan.service.api.comment.dto.key.ICommentableKey;
import com.similan.service.api.community.dto.basic.ActorDto;

public class CommentReplyDto<CommentableKey extends ICommentableKey> extends
    AbstractCommentOrReplyDto<CommentReplyKey<CommentableKey>> {

  protected CommentReplyDto() {
  }

  public CommentReplyDto(CommentReplyKey<CommentableKey> key,
      ActorDto author, Date date, String content) {
    super(key, author, date, content);
  }

}
