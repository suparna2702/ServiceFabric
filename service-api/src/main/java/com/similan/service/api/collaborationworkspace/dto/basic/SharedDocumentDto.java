package com.similan.service.api.collaborationworkspace.dto.basic;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.collaborationworkspace.dto.key.SharedDocumentKey;
import com.similan.service.api.community.dto.key.SocialActorKey;

public class SharedDocumentDto extends KeyHolderDto<SharedDocumentKey> {

  @XmlElement
  private SocialActorKey sharer;

  @XmlAttribute
  private Date shareDate;

  @XmlAttribute
  private Boolean unshared;

  @XmlAttribute
  private String description;

  protected SharedDocumentDto() {
  }

  public SharedDocumentDto(SharedDocumentKey key, SocialActorKey sharer,
      Date shareDate) {
    super(key);
    this.sharer = sharer;
    this.shareDate = shareDate;
  }

  public SocialActorKey getSharer() {
    return sharer;
  }

  public Date getShareDate() {
    return shareDate;
  }

  public Boolean getUnshared() {
    return unshared;
  }

  public void setUnshared(Boolean unshared) {
    this.unshared = unshared;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
