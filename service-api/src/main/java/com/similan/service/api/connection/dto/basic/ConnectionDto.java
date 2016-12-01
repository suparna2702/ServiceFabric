package com.similan.service.api.connection.dto.basic;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.connection.dto.key.ConnectionKey;

public class ConnectionDto extends KeyHolderDto<ConnectionKey> {

  @XmlElement
  private ActorDto contact;

  @XmlAttribute
  private ContactType type;

  @XmlAttribute
  private Date date;

  protected ConnectionDto() {
  }

  public ConnectionDto(ConnectionKey key, ActorDto contact,
      ContactType type, Date date) {
    super(key);
    this.contact = contact;
    this.date = date;
    this.type = type;
  }

  public ActorDto getContact() {
    return contact;
  }

  public ContactType getType() {
    return type;
  }

  public Date getContactDate() {
    return date;
  }

}
