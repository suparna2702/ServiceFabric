package com.similan.domain.entity.wall.collaborationworkspace;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

import com.similan.domain.share.ExternalShared;
import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "CollaborationWorkspaceExternalSharedWallEntry")
@DiscriminatorValue("COLLABORATION_WORKSPACE_DOCUMENT_EXTERNAL_SHARED")
@Getter
@Setter
public class CollaborationWorkspaceExternalSharedWallEntry extends
    CollaborationWorkspaceDocumentWallEntry {

  @ManyToOne
  @JoinColumn(nullable = true)
  private ExternalShared externalShared;

  public CollaborationWorkspaceExternalSharedWallEntry() {

  }

  public CollaborationWorkspaceExternalSharedWallEntry(ExternalShared shared,
      int number, Date date) {
    super(WallEntryType.COLLABORATION_WORKSPACE_DOCUMENT_EXTERNAL_SHARED,
        number, date);
    this.setShowWall(Boolean.TRUE);
    this.externalShared = shared;
  }

}
