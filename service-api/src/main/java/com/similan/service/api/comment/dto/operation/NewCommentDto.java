package com.similan.service.api.comment.dto.operation;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.operation.OperationDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

public class NewCommentDto extends OperationDto {

  @XmlElement
  private SocialActorKey author;

  @XmlAttribute
  private String content;

  public NewCommentDto() {
  }

  public NewCommentDto(String authorName, String content) {
    this(new SocialActorKey(authorName), content);
  }

  public NewCommentDto(SocialActorKey author, String content) {
    this.author = author;
    this.content = content;
  }

  public SocialActorKey getAuthor() {
    return author;
  }

  public String getContent() {
    return content;
  }

  @Override
  public String toString() {
    return "NewCommentDto [author=" + author + ", content=" + content + "]";
  }

}
