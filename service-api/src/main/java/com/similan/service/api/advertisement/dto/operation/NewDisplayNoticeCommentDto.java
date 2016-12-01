package com.similan.service.api.advertisement.dto.operation;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.advertisement.dto.key.DisplayNoticeKey;
import com.similan.service.api.base.dto.operation.OperationDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

public class NewDisplayNoticeCommentDto extends OperationDto {

  @XmlElement
  private SocialActorKey author;

  @XmlAttribute
  private String content;

  @XmlAttribute
  private DisplayNoticeKey displayNoticeKey;

  protected NewDisplayNoticeCommentDto() {

  }

  public NewDisplayNoticeCommentDto(SocialActorKey author, String content,
      DisplayNoticeKey displayNoticeKey) {
    this.author = author;
    this.content = content;
    this.displayNoticeKey = displayNoticeKey;
  }

  public SocialActorKey getAuthor() {
    return author;
  }

  public String getContent() {
    return content;
  }

  public DisplayNoticeKey getDisplayNoticeKey() {
    return displayNoticeKey;
  }

}
