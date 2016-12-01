package com.similan.domain.entity.wall.contentworkspace;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "ContentWorkspaceDocumentUploadedWallEntry")
@DiscriminatorValue("CONTENT_WORKSPACE_DOCUMENT_UPLOADED")
public class ContentWorkspaceDocumentUploadedWallEntry extends
    ContentWorkspaceDocumentWallEntry {

  protected ContentWorkspaceDocumentUploadedWallEntry() {

  }

  public ContentWorkspaceDocumentUploadedWallEntry(int number, Date date,
      Integer documentVersion) {
    super(WallEntryType.CONTENT_WORKSPACE_DOCUMENT_UPLOADED, number, date,
        documentVersion);
    this.setShowWall(Boolean.TRUE);
  }

}
