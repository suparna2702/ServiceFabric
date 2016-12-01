package com.similan.domain.entity.wall.contentworkspace;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "ContentWorkspaceDocumentCheckInWallEntry")
@DiscriminatorValue("CONTENT_WORKSPACE_DOCUMENT_CHECKIN")
public class ContentWorkspaceDocumentCheckInWallEntry extends
    ContentWorkspaceDocumentWallEntry {

  public ContentWorkspaceDocumentCheckInWallEntry(int number, Date date,
      Integer documentVersion) {
    super(WallEntryType.CONTENT_WORKSPACE_DOCUMENT_CHECKIN, number, date,
        documentVersion);
    this.setShowWall(Boolean.TRUE);
  }

  public ContentWorkspaceDocumentCheckInWallEntry() {

  }

}
