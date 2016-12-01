package com.similan.domain.entity.wall.collaborationworkspace;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

import com.similan.domain.share.InNetworkShared;
import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "CollaborationWorkspaceInNetworkSharedWallEntry")
@DiscriminatorValue("COLLABORATION_WORKSPACE_DOCUMENT_INNETWORK_SHARED")
@Getter
@Setter
public class CollaborationWorkspaceInNetworkSharedWallEntry extends
    CollaborationWorkspaceDocumentWallEntry {

  @ManyToOne
  @JoinColumn(nullable = true)
  private InNetworkShared inNetworkShared;

  public CollaborationWorkspaceInNetworkSharedWallEntry() {

  }

  public CollaborationWorkspaceInNetworkSharedWallEntry(
      InNetworkShared inNetworkShared, int number, Date date) {
    super(WallEntryType.COLLABORATION_WORKSPACE_DOCUMENT_EXTERNAL_SHARED,
        number, date);

    this.setShowWall(Boolean.TRUE);
    this.inNetworkShared = inNetworkShared;

  }

}
