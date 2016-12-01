package com.similan.service.api.comment.dto.basic;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.base.dto.key.IKey;
import com.similan.service.api.community.dto.basic.ActorDto;

public abstract class AbstractCommentOrReplyDto<ConcreteKey extends IKey>
    extends KeyHolderDto<ConcreteKey> {

  @XmlElement
  private ActorDto author;

  @XmlAttribute
  private Date date;

  @XmlAttribute
  private String content;

  protected AbstractCommentOrReplyDto() {
  }

  public AbstractCommentOrReplyDto(ConcreteKey key,
      ActorDto author, Date date, String content) {
    super(key);
    this.author = author;
    this.date = date;
    this.content = content;
  }

  public ActorDto getAuthor() {
    return author;
  }

  public Date getDate() {
    return date;
  }

  public String getContent() {
    return content;
  }

}
