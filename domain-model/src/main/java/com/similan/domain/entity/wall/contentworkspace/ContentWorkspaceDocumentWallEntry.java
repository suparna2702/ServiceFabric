package com.similan.domain.entity.wall.contentworkspace;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "ContentWorkspaceDocumentWallEntry")
public class ContentWorkspaceDocumentWallEntry extends
    ContentWorkspaceWallEntry {

  @Column
  private Integer documentVersion;

  public ContentWorkspaceDocumentWallEntry() {

  }

  protected ContentWorkspaceDocumentWallEntry(WallEntryType type, int number,
      Date date, Integer documentVersion) {
    super(type, number, date);
    this.documentVersion = documentVersion;
  }

  public Integer getDocumentVersion() {
    return documentVersion;
  }

  public void setDocumentVersion(Integer documentVersion) {
    this.documentVersion = documentVersion;
  }

}
