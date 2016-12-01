package com.similan.domain.entity.wall.contentworkspace;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "ContentWorkspaceDocumentCheckOutWallEntry")
@DiscriminatorValue("CONTENT_WORKSPACE_DOCUMENT_CHECKOUT")
public class ContentWorkspaceDocumentCheckOutWallEntry extends
    ContentWorkspaceDocumentWallEntry {

  public ContentWorkspaceDocumentCheckOutWallEntry() {

  }

  public ContentWorkspaceDocumentCheckOutWallEntry(int number, Date date,
      Integer documentVersion) {
    
    super(WallEntryType.CONTENT_WORKSPACE_DOCUMENT_CHECKOUT, number, date,
        documentVersion);
    this.setShowWall(Boolean.TRUE);
  }

}
