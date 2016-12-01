package com.similan.service.api.wall.dto.basic.contentspace;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.document.dto.basic.DocumentDto;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;
import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.api.wall.dto.key.WallEntryKey;

public class ContentWorkspaceDocumentUpdateWallEntryDto extends
    ContentWorkspaceDocumentWallEntryDto {

  protected ContentWorkspaceDocumentUpdateWallEntryDto() {

  }

  public ContentWorkspaceDocumentUpdateWallEntryDto(DocumentDto document,
      ActorDto initiatorDto, WallEntryKey<ManagementWorkspaceKey> key,
      Date date, Integer documentVersion) {
    super(document, initiatorDto, key, date, documentVersion);
  }

  @Override
  public WallEntryType getType() {
    return WallEntryType.CONTENT_WORKSPACE_DOCUMENT_UPDATED;
  }

}
