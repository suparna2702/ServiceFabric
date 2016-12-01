package com.similan.service.api.document.dto.key;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;
import com.similan.service.api.community.dto.key.SocialActorKey;

public class DocumentLabelKey extends Key {

  @XmlElement
  private SocialActorKey owner;

  @XmlAttribute
  private String name;

  public DocumentLabelKey() {
  }

  public DocumentLabelKey(String ownerName, String name) {
    this(new SocialActorKey(ownerName), name);
  }

  public DocumentLabelKey(SocialActorKey owner, String name) {
    this.owner = owner;
    this.name = name;
  }

  public SocialActorKey getOwner() {
    return owner;
  }

  public EntityType getEntityType() {
    return EntityType.DOCUMENT_LABEL;
  }

  public String getName() {
    return name;
  }

}
