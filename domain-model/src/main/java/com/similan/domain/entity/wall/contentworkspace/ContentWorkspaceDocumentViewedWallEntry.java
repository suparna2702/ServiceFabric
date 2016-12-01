package com.similan.domain.entity.wall.contentworkspace;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "ContentWorkspaceDocumentViewedWallEntry")
@DiscriminatorValue("CONTENT_WORKSPACE_DOCUMENT_VIEWED")
public class ContentWorkspaceDocumentViewedWallEntry extends
    ContentWorkspaceDocumentWallEntry {

  public ContentWorkspaceDocumentViewedWallEntry(int number, Date date,
      Integer documentVersion) {
    super(WallEntryType.CONTENT_WORKSPACE_DOCUMENT_VIEWED, number, date,
        documentVersion);
    this.setShowWall(Boolean.TRUE);
  }

  public ContentWorkspaceDocumentViewedWallEntry() {

  }

}
