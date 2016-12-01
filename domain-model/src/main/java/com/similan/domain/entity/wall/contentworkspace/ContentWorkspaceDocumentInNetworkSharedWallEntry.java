package com.similan.domain.entity.wall.contentworkspace;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

import com.similan.domain.share.InNetworkShared;
import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "ContentWorkspaceDocumentInNetworkSharedWallEntry")
@DiscriminatorValue("CONTENT_WORKSPACE_DOCUMENT_INNETWORK_SHARED")
@Getter
@Setter
public class ContentWorkspaceDocumentInNetworkSharedWallEntry extends
    ContentWorkspaceDocumentWallEntry {

  @ManyToOne
  @JoinColumn(nullable = true)
  private InNetworkShared inNetworkShared;

  public ContentWorkspaceDocumentInNetworkSharedWallEntry() {

  }

  public ContentWorkspaceDocumentInNetworkSharedWallEntry(
      InNetworkShared inNetworkShared, int number, Date date, Integer version) {

    super(WallEntryType.CONTENT_WORKSPACE_DOCUMENT_INNETWORK_SHARED, number,
        date, version);

    this.setShowWall(Boolean.TRUE);
    this.inNetworkShared = inNetworkShared;

  }

}
