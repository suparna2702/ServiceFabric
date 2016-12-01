package com.similan.service.api.comment.dto.key;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;

public class CommentReplyKey<CommentableKey extends ICommentableKey> extends
    Key {

  @XmlElement
  private CommentKey<CommentableKey> comment;

  @XmlAttribute
  private Integer number;

  public CommentReplyKey() {
  }

  public CommentReplyKey(CommentableKey commentable, Integer commentNumber,
      Integer number) {
    this(new CommentKey<CommentableKey>(commentable, commentNumber), number);
  }

  public CommentReplyKey(CommentKey<CommentableKey> comment, Integer number) {
    this.comment = comment;
    this.number = number;
  }

  public CommentKey<CommentableKey> getComment() {
    return comment;
  }

  public Integer getNumber() {
    return number;
  }

  public EntityType getEntityType() {
    return EntityType.COMMENT_REPLY;
  }

}
