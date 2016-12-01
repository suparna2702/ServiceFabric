package com.similan.domain.entity.wall.contentworkspace;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

import com.similan.domain.share.ExternalShared;
import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "ContentWorkspaceDocumentExternalSharedWallEntry")
@DiscriminatorValue("CONTENT_WORKSPACE_DOCUMENT_EXTERNAL_SHARED")
@Getter
@Setter
public class ContentWorkspaceDocumentExternalSharedWallEntry extends
    ContentWorkspaceDocumentWallEntry {

  @ManyToOne
  @JoinColumn(nullable = true)
  private ExternalShared externalShared;

  public ContentWorkspaceDocumentExternalSharedWallEntry() {

  }

  public ContentWorkspaceDocumentExternalSharedWallEntry(ExternalShared shared,
      int number, Date date, Integer documentVersion) {
    super(WallEntryType.CONTENT_WORKSPACE_DOCUMENT_EXTERNAL_SHARED, number,
        date, documentVersion);
    this.setShowWall(Boolean.TRUE);
    this.externalShared = shared;

  }

}
