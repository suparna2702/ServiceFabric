package com.similan.service.api.wall.dto.basic.collaborationworkspace;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.collaborationworkspace.dto.basic.SharedDocumentDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.wall.dto.key.WallEntryKey;

public abstract class CollaborationWorkspaceDocumentWallEntryDto extends
    CollaborationWorkspaceWallEntryDto {

  @XmlElement
  private SharedDocumentDto document;

  @XmlAttribute
  private Integer documentVersion;

  protected CollaborationWorkspaceDocumentWallEntryDto() {

  }

  protected CollaborationWorkspaceDocumentWallEntryDto(
      SharedDocumentDto document, ActorDto initiatorDto,
      WallEntryKey<CollaborationWorkspaceKey> key, Date date,
      Integer documentVersion) {
    super(key, initiatorDto, date);

    this.document = document;
    this.documentVersion = documentVersion;
  }

  public SharedDocumentDto getDocument() {
    return document;
  }

  public Integer getDocumentVersion() {
    return documentVersion;
  }

}
