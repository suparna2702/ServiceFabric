package com.similan.service.api.comment.dto.operation;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.operation.OperationDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

public class NewCommentReplyDto extends OperationDto {

  @XmlElement
  private SocialActorKey author;

  @XmlAttribute
  private String content;

  public NewCommentReplyDto() {
  }

  public NewCommentReplyDto(String authorName, String content) {
    this(new SocialActorKey(authorName), content);
  }

  public NewCommentReplyDto(SocialActorKey author, String content) {
    this.author = author;
    this.content = content;
  }

  public SocialActorKey getAuthor() {
    return author;
  }

  public String getContent() {
    return content;
  }

}
