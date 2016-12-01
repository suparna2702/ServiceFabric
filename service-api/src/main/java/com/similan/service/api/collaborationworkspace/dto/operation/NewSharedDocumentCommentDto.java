package com.similan.service.api.collaborationworkspace.dto.operation;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.operation.OperationDto;
import com.similan.service.api.collaborationworkspace.dto.key.SharedDocumentKey;
import com.similan.service.api.community.dto.key.SocialActorKey;

public class NewSharedDocumentCommentDto extends OperationDto {

  @XmlElement
  private SocialActorKey author;

  @XmlAttribute
  private String content;

  @XmlAttribute
  private SharedDocumentKey document;

  protected NewSharedDocumentCommentDto() {

  }

  public NewSharedDocumentCommentDto(SocialActorKey author, String content,
      SharedDocumentKey document) {
    this.author = author;
    this.content = content;
    this.document = document;
  }

  public SocialActorKey getAuthor() {
    return author;
  }

  public String getContent() {
    return content;
  }

  public SharedDocumentKey getDocument() {
    return document;
  }

  @Override
  public String toString() {
    return "NewSharedDocumentCommentDto [author=" + author + ", content="
        + content + ", document=" + document + "]";
  }

}
