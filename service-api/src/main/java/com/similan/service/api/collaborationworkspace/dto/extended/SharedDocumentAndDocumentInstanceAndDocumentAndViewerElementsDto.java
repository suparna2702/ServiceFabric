package com.similan.service.api.collaborationworkspace.dto.extended;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.collaborationworkspace.dto.key.SharedDocumentKey;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentAndViewerElementsDto;

public class SharedDocumentAndDocumentInstanceAndDocumentAndViewerElementsDto
    extends KeyHolderDto<SharedDocumentKey> {

  @XmlElement
  private DocumentInstanceAndDocumentAndViewerElementsDto documentInstance;

  @XmlElement
  private SocialActorKey sharer;

  @XmlAttribute
  private Date shareDate;

  private Boolean unshared = Boolean.FALSE;

  protected SharedDocumentAndDocumentInstanceAndDocumentAndViewerElementsDto() {
  }

  public SharedDocumentAndDocumentInstanceAndDocumentAndViewerElementsDto(
      SharedDocumentKey key,
      DocumentInstanceAndDocumentAndViewerElementsDto documentInstance,
      SocialActorKey sharer, Date shareDate) {
    super(key);
    this.documentInstance = documentInstance;
    this.sharer = sharer;
    this.shareDate = shareDate;
  }

  public DocumentInstanceAndDocumentAndViewerElementsDto getDocumentInstance() {
    return documentInstance;
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

}
