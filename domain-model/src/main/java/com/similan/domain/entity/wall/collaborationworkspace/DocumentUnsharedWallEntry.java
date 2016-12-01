package com.similan.domain.entity.wall.collaborationworkspace;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "DocumentUnsharedWallEntry")
@DiscriminatorValue("COLLABORATION_WORKSPACE_DOCUMENT_UNSHARED")
public class DocumentUnsharedWallEntry extends
    CollaborationWorkspaceDocumentWallEntry {

  public DocumentUnsharedWallEntry() {

  }

  public DocumentUnsharedWallEntry(int number, Date date) {
    super(WallEntryType.COLLABORATION_WORKSPACE_DOCUMENT_UNSHARED, number, date);
    this.setShowWall(Boolean.TRUE);
  }

}
