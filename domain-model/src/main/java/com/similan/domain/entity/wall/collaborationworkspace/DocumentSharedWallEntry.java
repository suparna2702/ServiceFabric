package com.similan.domain.entity.wall.collaborationworkspace;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "DocumentSharedWallEntry")
@DiscriminatorValue("COLLABORATION_WORKSPACE_DOCUMENT_SHARED")
public class DocumentSharedWallEntry extends
                  CollaborationWorkspaceDocumentWallEntry {

  public DocumentSharedWallEntry() {
  }

  public DocumentSharedWallEntry(int number, Date date) {
    super(WallEntryType.COLLABORATION_WORKSPACE_DOCUMENT_SHARED, number, date);
    this.setShowWall(Boolean.TRUE);
  }

}
