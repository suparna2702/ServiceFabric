package com.similan.service.api.wall.dto.basic.contentspace;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;

import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.document.dto.basic.DocumentDto;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;
import com.similan.service.api.wall.dto.key.WallEntryKey;

public abstract class ContentWorkspaceDocumentWallEntryDto extends
    ContentWorkspaceWallEntryDto {

  @XmlAttribute
  private DocumentDto document;

  @XmlAttribute
  private Integer documentVersion;

  protected ContentWorkspaceDocumentWallEntryDto() {

  }

  public ContentWorkspaceDocumentWallEntryDto(DocumentDto document,
      ActorDto initiatorDto,
      WallEntryKey<ManagementWorkspaceKey> key, Date date,
      Integer documentVersion) {
    super(key, initiatorDto, date);
    this.document = document;
    this.documentVersion = documentVersion;
  }

  public DocumentDto getDocument() {
    return document;
  }

  public Integer getDocumentVersion() {
    return documentVersion;
  }

  @Override
  public String toString() {
    return "ContentWorkspaceDocumentWallEntryDto [document=" + document
        + ", documentVersion=" + documentVersion + "]";
  }

}
