package com.similan.domain.entity.wall.contentworkspace;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "ContentWorkspaceDocumentUpdateWallEntry")
@DiscriminatorValue("CONTENT_WORKSPACE_DOCUMENT_UPDATED")
@Getter
@Setter
public class ContentWorkspaceDocumentUpdateWallEntry extends
    ContentWorkspaceDocumentWallEntry {
  
  protected ContentWorkspaceDocumentUpdateWallEntry(){
    
  }

  public ContentWorkspaceDocumentUpdateWallEntry(int number, Date date,
      Integer documentVersion) {
    super(WallEntryType.CONTENT_WORKSPACE_DOCUMENT_UPDATED, number, date,
        documentVersion);
    this.setShowWall(Boolean.TRUE);
  }

}
