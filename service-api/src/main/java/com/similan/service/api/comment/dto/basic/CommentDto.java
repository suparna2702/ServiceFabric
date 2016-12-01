package com.similan.service.api.comment.dto.basic;

import java.util.Date;

import com.similan.service.api.comment.dto.key.CommentKey;
import com.similan.service.api.comment.dto.key.ICommentableKey;
import com.similan.service.api.community.dto.basic.ActorDto;

public class CommentDto<CommentableKey extends ICommentableKey> extends
    AbstractCommentOrReplyDto<CommentKey<CommentableKey>> {

  protected CommentDto() {
  }

  public CommentDto(CommentKey<CommentableKey> key,
      ActorDto author, Date date, String content) {
    super(key, author, date, content);
  }
}
