package com.similan.service.impl.document;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.asset.Asset;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.document.Document;
import com.similan.domain.entity.document.DocumentInstance;
import com.similan.domain.entity.document.DocumentPage;
import com.similan.domain.entity.document.DocumentViewerAsset;
import com.similan.domain.repository.document.DocumentInstanceRepository;
import com.similan.service.api.asset.AssetStream;
import com.similan.service.api.asset.NewAssetStream;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.DocumentInstanceService;
import com.similan.service.api.document.dto.error.DocumentErrorCode;
import com.similan.service.api.document.dto.error.DocumentException;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentAndViewerElementsDto;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentDto;
import com.similan.service.api.document.dto.key.DocumentInstanceKey;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.document.dto.key.DocumentPageKey;
import com.similan.service.api.document.dto.key.DocumentViewerItemKey;
import com.similan.service.api.document.dto.operation.NewDocumentInstanceDto;
import com.similan.service.impl.base.ServiceImpl;
import com.similan.service.impl.community.SocialActorMarshaller;
import com.similan.service.internal.api.asset.AssetInternalService;
import com.similan.service.internal.impl.security.component.DocumentInstanceEnforcer;

@Service
public class DocumentInstanceServiceImpl extends ServiceImpl implements
    DocumentInstanceService {
  @Autowired
  private DocumentInstanceRepository documentInstanceRepository;
  @Autowired
  private AssetInternalService assetService;
  @Autowired
  private  DocumentInstanceProcessor documentInstanceProcessor;
  @Autowired
  private DocumentInstanceEnforcer documentInstanceEnforcer;
  @Autowired
  private DocumentMarshaller documentMarshaller;
  @Autowired
  private SocialActorMarshaller actorMarshaller;
  @Autowired
  private DocumentInstanceMarshaller documentInstanceMarshaller;
  
  @Override
  @Transactional
  public DocumentInstanceAndDocumentDto create(DocumentKey documentKey,
      final NewDocumentInstanceDto newDocumentInstance,
      NewAssetStream assetStream) {
    checkNotNull(documentKey);
    checkNotNull(newDocumentInstance);
    checkNotNull(assetStream);

    Document document = documentMarshaller
        .unmarshallDocumentKey(documentKey, true);
    SocialActorKey uploaderKey = newDocumentInstance.getUploader();
    SocialActor uploader = actorMarshaller
        .unmarshallActorKey(uploaderKey, true);
    SocialActor lockOwner = document.getLockOwner();

    if (lockOwner != null && lockOwner != uploader) {
      SocialActorKey lockOwnerKey = actorMarshaller
          .marshallActorKey(lockOwner);
      throw new DocumentException(DocumentErrorCode.DOCUMENT_LOCK_NOT_HELD,  "Lock on docuemnt  " + documentKey
          + " is not held by " + uploaderKey + ", but by " + lockOwnerKey);
    }

    DocumentInstance documentInstance = documentInstanceRepository
        .create(document);
    documentInstanceRepository.save(documentInstance);

    documentInstanceEnforcer.uploadDocument(documentInstance).checkAllowed();

    String explicitFilename = newDocumentInstance.getExplicitFilename();
    documentInstanceProcessor.create(documentInstance, explicitFilename,
        assetStream);

    DocumentInstanceAndDocumentDto documentInstanceDto = documentInstanceMarshaller.marshallDocumentInstanceAndDocument(
            documentInstance);
    return documentInstanceDto;
  }

  @Override
  @Transactional(readOnly = true)
  public DocumentInstanceAndDocumentDto get(
      DocumentInstanceKey documentInstanceKey) {
    DocumentInstance documentInstance = documentInstanceMarshaller.unmarshallDocumentInstanceKey(
            documentInstanceKey, true);
    DocumentInstanceAndDocumentDto documentInstanceDto = documentInstanceMarshaller.marshallDocumentInstanceAndDocument(
            documentInstance);
    return documentInstanceDto;
  }

  @Override
  @Transactional(readOnly = true)
  public DocumentInstanceAndDocumentAndViewerElementsDto getForViewer(
      DocumentInstanceKey documentInstanceKey) {
    DocumentInstance documentInstance = documentInstanceMarshaller.unmarshallDocumentInstanceKey(
            documentInstanceKey, true);
    DocumentInstanceAndDocumentAndViewerElementsDto viewerDocumentInstanceDto = documentInstanceMarshaller
        .marshallDocumentInstanceAndDocumentAndViewerElements(documentInstance);
    return viewerDocumentInstanceDto;
  }

  @Override
  @Transactional(readOnly = true)
  public AssetStream retrieveOriginal(DocumentInstanceKey documentInstanceKey) {
    DocumentInstance documentInstance = documentInstanceMarshaller.unmarshallDocumentInstanceKey(
            documentInstanceKey, true);
    Asset asset = documentInstance.getOriginalAsset();
    AssetStream retriever = assetService.retrieve(asset);
    return retriever;
  }

  @Override
  @Transactional(readOnly = true)
  public AssetStream retrieveIcon(DocumentInstanceKey documentInstanceKey) {
    DocumentInstance documentInstance = documentInstanceMarshaller.unmarshallDocumentInstanceKey(
            documentInstanceKey, true);
    Asset asset = documentInstance.getIconAsset();
    if (asset == null) {
      throw new IllegalArgumentException(
          "Document instance doesn't have an icon: " + documentInstanceKey);
    }
    AssetStream retriever = assetService.retrieve(asset);
    return retriever;
  }

  @Override
  @Transactional(readOnly = true)
  public AssetStream retrieveText(DocumentInstanceKey documentInstanceKey) {
    DocumentInstance documentInstance = documentInstanceMarshaller.unmarshallDocumentInstanceKey(
            documentInstanceKey, true);
    Asset asset = documentInstance.getTextAsset();
    if (asset == null) {
      throw new IllegalArgumentException(
          "Document instance doesn't have an extracted text file: "
              + documentInstanceKey);
    }
    AssetStream retriever = assetService.retrieve(asset);
    return retriever;
  }

  @Override
  @Transactional(readOnly = true)
  public AssetStream retrievePage(DocumentPageKey pageKey) {
    DocumentPage documentPage = documentInstanceMarshaller.unmarshallPageKey(pageKey, true);
    Asset asset = documentPage.getPageAsset();
    AssetStream retriever = assetService.retrieve(asset);
    return retriever;
  }

  @Override
  @Transactional(readOnly = true)
  public AssetStream retrievePageThumbnail(DocumentPageKey pageKey) {
    DocumentPage documentPage = documentInstanceMarshaller.unmarshallPageKey(pageKey, true);
    Asset asset = documentPage.getThumbnailAsset();
    if (asset == null) {
      throw new IllegalArgumentException(
          "Document instance doesn't have a thumbnail: " + pageKey);
    }
    AssetStream retriever = assetService.retrieve(asset);
    return retriever;
  }

  @Override
  @Transactional(readOnly = true)
  public AssetStream retrieveViewerAsset(DocumentViewerItemKey itemKey) {
    DocumentViewerAsset item = documentInstanceMarshaller
        .unmarshallViewerAssetKey(itemKey, true);
    DocumentViewerAsset viewerAsset = (DocumentViewerAsset) item;
    Asset asset = viewerAsset.getAsset();
    AssetStream retriever = assetService.retrieve(asset);
    return retriever;
  }
}
