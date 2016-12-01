package com.similan.service.api.wall.dto.basic.comment;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.basic.IKeyHolderDto;
import com.similan.service.api.comment.dto.basic.CommentDto;
import com.similan.service.api.comment.dto.key.ICommentableKey;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.api.wall.dto.key.IWallContainerKey;
import com.similan.service.api.wall.dto.key.WallEntryKey;

public class CommentPostedEntryDto<WallContainerKey extends IWallContainerKey>
    extends WallEntryDto<WallContainerKey> {

  @XmlElement
  private IKeyHolderDto<ICommentableKey> commentable;

  @XmlElement
  private CommentDto<ICommentableKey> comment;

  protected CommentPostedEntryDto() {
  }

  public CommentPostedEntryDto(WallEntryKey<WallContainerKey> key,
      ActorDto initiator, Date date,
      IKeyHolderDto<ICommentableKey> commentable,
      CommentDto<ICommentableKey> comment) {
    super(key, initiator, date);
    this.commentable = commentable;
    this.comment = comment;
  }

  public IKeyHolderDto<ICommentableKey> getCommentable() {
    return commentable;
  }

  public CommentDto<ICommentableKey> getComment() {
    return comment;
  }

  @Override
  public WallEntryType getType() {
    return WallEntryType.COMMENT_POSTED;
  }

}
