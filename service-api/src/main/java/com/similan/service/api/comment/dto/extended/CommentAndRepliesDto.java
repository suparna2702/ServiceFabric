package com.similan.service.api.comment.dto.extended;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.comment.dto.basic.CommentDto;
import com.similan.service.api.comment.dto.basic.CommentReplyDto;
import com.similan.service.api.comment.dto.key.CommentKey;
import com.similan.service.api.comment.dto.key.ICommentableKey;

@XmlRootElement(name="commentAndReplies")
public class CommentAndRepliesDto<CommentableKey extends ICommentableKey>
    extends KeyHolderDto<CommentKey<CommentableKey>> {

  @XmlElement
  private CommentDto<CommentableKey> comment;

  @XmlElement(name = "reply")
  @XmlElementWrapper
  private List<CommentReplyDto<CommentableKey>> replies;

  protected CommentAndRepliesDto() {
  }

  public CommentAndRepliesDto(CommentDto<CommentableKey> comment,
      List<CommentReplyDto<CommentableKey>> replies) {
    super(comment == null ? null : comment.getKey());
    this.comment = comment;
    this.replies = replies;
  }

  public CommentDto<CommentableKey> getComment() {
    return comment;
  }

  public List<CommentReplyDto<CommentableKey>> getReplies() {
    return replies;
  }

}
