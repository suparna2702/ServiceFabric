package com.similan.service.api.wall.dto.operation;

import javax.xml.bind.annotation.XmlAttribute;

import com.similan.service.api.base.dto.operation.OperationDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

public class NewWallPostDto extends OperationDto {

  @XmlAttribute
  private SocialActorKey author;

  @XmlAttribute
  private String content;

  public NewWallPostDto() {
  }

  public NewWallPostDto(SocialActorKey author, String content) {
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
