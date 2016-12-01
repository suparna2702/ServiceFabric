package com.similan.domain.entity.wall.contentworkspace;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "ContentWorkspaceDocumentDownloadedWallEntry")
@DiscriminatorValue("CONTENT_WORKSPACE_DOCUMENT_DOWNLOADED")
public class ContentWorkspaceDocumentDownloadedWallEntry extends
    ContentWorkspaceDocumentWallEntry {

  protected ContentWorkspaceDocumentDownloadedWallEntry() {

  }

  public ContentWorkspaceDocumentDownloadedWallEntry(int number, Date date,
      Integer documentVersion) {
    super(WallEntryType.CONTENT_WORKSPACE_DOCUMENT_DOWNLOADED, number, date,
        documentVersion);
    this.setShowWall(Boolean.TRUE);
  }

}
