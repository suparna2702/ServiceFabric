package com.similan.service.api.comment.dto.key;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;

public class CommentKey<CommentableKey extends ICommentableKey> extends Key {

  @XmlElement
  private CommentableKey commentable;

  @XmlAttribute
  private Integer number;

  public CommentKey() {
  }

  public CommentKey(CommentableKey commentable, Integer number) {
    this.commentable = commentable;
    this.number = number;
  }

  public CommentableKey getCommentable() {
    return commentable;
  }

  public Integer getNumber() {
    return number;
  }

  public EntityType getEntityType() {
    return EntityType.COMMENT;
  }

}
