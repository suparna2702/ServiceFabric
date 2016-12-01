package com.similan.service.api.document;

import com.similan.service.api.asset.AssetStream;
import com.similan.service.api.asset.NewAssetStream;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentAndViewerElementsDto;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentDto;
import com.similan.service.api.document.dto.key.DocumentInstanceKey;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.document.dto.key.DocumentPageKey;
import com.similan.service.api.document.dto.key.DocumentViewerItemKey;
import com.similan.service.api.document.dto.operation.NewDocumentInstanceDto;

public interface DocumentInstanceService {

  DocumentInstanceAndDocumentDto create(DocumentKey documentKey,
      NewDocumentInstanceDto newDocumentInstance, NewAssetStream assetStream);

  DocumentInstanceAndDocumentDto get(DocumentInstanceKey documentInstanceKey);

  DocumentInstanceAndDocumentAndViewerElementsDto getForViewer(
      DocumentInstanceKey documentInstanceKey);

  AssetStream retrieveOriginal(DocumentInstanceKey documentInstanceKey);

  AssetStream retrieveIcon(DocumentInstanceKey documentInstanceKey);

  AssetStream retrieveText(DocumentInstanceKey documentInstanceKey);

  AssetStream retrievePage(DocumentPageKey pageKey);

  AssetStream retrievePageThumbnail(DocumentPageKey pageKey);

  AssetStream retrieveViewerAsset(DocumentViewerItemKey itemKey);

}