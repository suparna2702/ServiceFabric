package com.similan.service.api.document;

import com.similan.service.api.asset.AssetStream;
import com.similan.service.api.asset.NewAssetStream;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.dto.basic.DocumentDto;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentAndViewerElementsDto;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentDto;
import com.similan.service.api.document.dto.extended.DocumentStatisticsDto;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.document.dto.operation.DocumentInfoUpdateDto;
import com.similan.service.api.document.dto.operation.NewDocumentDto;
import com.similan.service.api.document.dto.operation.NewDocumentInstanceDto;
import com.similan.service.api.document.dto.operation.NewExternallyManagedDocumentDto;

public interface DocumentService {

  public DocumentDto create(DocumentKey documentKey,
      NewExternallyManagedDocumentDto newDocumentDto);

  public DocumentInstanceAndDocumentDto create(DocumentKey documentKey,
      NewDocumentDto newDocumentDto, NewAssetStream assetStream);

  public DocumentInstanceAndDocumentDto get(DocumentKey documentKey);

  public void delete(DocumentKey documentKey);

  public boolean lock(DocumentKey documentKey, SocialActorKey lockOwnerKey);

  public boolean unlock(DocumentKey documentKey, SocialActorKey lockOwnerKey);

  public SocialActorKey getLockOwner(DocumentKey documentKey);

  public AssetStream checkout(DocumentKey documentKey,
      SocialActorKey lockOwnerKey);

  public DocumentInstanceAndDocumentDto checkin(DocumentKey documentKey,
      NewDocumentInstanceDto newDocumentInstance, NewAssetStream assetStream);

  public DocumentStatisticsDto getStatistics(DocumentKey documentKey);

  public String getDocumentIcon(DocumentKey documentKey);

  public DocumentInstanceAndDocumentAndViewerElementsDto updateDocumentInfo(
      DocumentKey documentKey, DocumentInfoUpdateDto updateInfo);
}