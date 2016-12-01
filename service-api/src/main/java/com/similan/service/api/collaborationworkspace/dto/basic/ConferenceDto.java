package com.similan.service.api.collaborationworkspace.dto.basic;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.collaborationworkspace.dto.key.ConferenceKey;
import com.similan.service.api.community.dto.key.SocialActorKey;

public class ConferenceDto extends KeyHolderDto<ConferenceKey> {

  @XmlAttribute
  private Date creationDate;

  @XmlElement
  private SocialActorKey organizer;

  protected ConferenceDto() {
  }

  public ConferenceDto(ConferenceKey key, Date creationDate,
      SocialActorKey organizer) {
    super(key);
    this.creationDate = creationDate;
    this.organizer = organizer;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public SocialActorKey getOrganizer() {
    return organizer;
  }
}
