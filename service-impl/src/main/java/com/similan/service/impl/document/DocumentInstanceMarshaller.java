package com.similan.service.impl.document;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.asset.Asset;
import com.similan.domain.entity.asset.AssetInfo;
import com.similan.domain.entity.document.Document;
import com.similan.domain.entity.document.DocumentInstance;
import com.similan.domain.entity.document.DocumentPage;
import com.similan.domain.entity.document.DocumentViewerAsset;
import com.similan.domain.entity.document.DocumentViewerAttribute;
import com.similan.domain.entity.document.DocumentViewerItem;
import com.similan.domain.repository.document.DocumentInstanceRepository;
import com.similan.domain.repository.document.DocumentPageRepository;
import com.similan.domain.repository.document.DocumentViewerItemRepository;
import com.similan.service.api.asset.dto.basic.AssetInfoDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.dto.basic.DocumentDto;
import com.similan.service.api.document.dto.basic.DocumentInstanceDto;
import com.similan.service.api.document.dto.basic.DocumentPageDto;
import com.similan.service.api.document.dto.basic.DocumentViewerItemType;
import com.similan.service.api.document.dto.error.DocumentErrorCode;
import com.similan.service.api.document.dto.error.DocumentException;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentAndViewerElementsDto;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentDto;
import com.similan.service.api.document.dto.extended.DocumentViewerItemDto;
import com.similan.service.api.document.dto.key.DocumentInstanceKey;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.document.dto.key.DocumentPageKey;
import com.similan.service.api.document.dto.key.DocumentViewerItemKey;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;
import com.similan.service.impl.Marshaller;
import com.similan.service.internal.impl.asset.AssetMarshaller;

@Component
public class DocumentInstanceMarshaller extends Marshaller {
  @Autowired
  private DocumentInstanceRepository documentInstanceRepository;
  @Autowired
  private DocumentPageRepository documentPageRepository;
  @Autowired
  private DocumentViewerItemRepository documentViewerItemRepository;
  @Autowired
  private DocumentMarshaller documentMarshaller;
  @Autowired
  private AssetMarshaller assetMarshaller;
  
  public DocumentInstanceKey marshallDocumentInstanceKey(
      DocumentInstance documentInstance) {
    Document document = documentInstance.getDocument();

    DocumentKey documentKey = documentMarshaller
        .marshallDocumentKey(document);

    DocumentInstanceKey key = marshallDocumentInstanceKey(documentKey,
        documentInstance);
    return key;
  }

  private DocumentInstanceKey marshallDocumentInstanceKey(
      DocumentKey documentKey, DocumentInstance documentInstance) {
    int version = documentInstance.getVersion();

    DocumentInstanceKey key = new DocumentInstanceKey(documentKey, version);
    key.setId(documentInstance.getId());
    return key;
  }

  public DocumentInstanceDto marshallDocumentInstance(
      DocumentInstance documentInstance) {
    Asset originalAsset = documentInstance.getOriginalAsset();
    AssetInfo detectedInfo = originalAsset.getDetectedInfo();
    Asset iconAsset = documentInstance.getIconAsset();
    Asset textAsset = documentInstance.getTextAsset();

    DocumentInstanceKey key = marshallDocumentInstanceKey(documentInstance);
    AssetInfoDto detectedInfoDto = assetMarshaller
        .marshallInfo(detectedInfo);
    String viewerId = documentInstance.getViewerId();
    boolean hasIcon = iconAsset != null;
    boolean hasText = textAsset != null;

    DocumentInstanceDto documentInstanceDto = new DocumentInstanceDto(key,
        detectedInfoDto, viewerId, hasIcon, hasText);

    return documentInstanceDto;
  }

  public DocumentInstanceAndDocumentDto marshallDocumentInstanceAndDocument(
      DocumentInstance documentInstance) {
    Document document = documentInstance.getDocument();
    Asset originalAsset = documentInstance.getOriginalAsset();
    AssetInfo detectedInfo = originalAsset.getDetectedInfo();
    Asset iconAsset = documentInstance.getIconAsset();
    Asset textAsset = documentInstance.getTextAsset();

    DocumentDto documentDto = documentMarshaller
        .marshallDocument(document);
    DocumentKey documentKey = documentDto.getKey();
    DocumentInstanceKey key = marshallDocumentInstanceKey(documentKey,
        documentInstance);
    AssetInfoDto detectedInfoDto = assetMarshaller
        .marshallInfo(detectedInfo);
    String viewerId = documentInstance.getViewerId();
    boolean hasIcon = iconAsset != null;
    boolean hasText = textAsset != null;

    DocumentInstanceAndDocumentDto documentInstanceDto = new DocumentInstanceAndDocumentDto(
        key, detectedInfoDto, viewerId, hasIcon, hasText, documentDto);

    return documentInstanceDto;
  }

  public DocumentInstanceAndDocumentAndViewerElementsDto marshallDocumentInstanceAndDocumentAndViewerElements(
      DocumentInstance documentInstance) {
    Document document = documentInstance.getDocument();
    Asset originalAsset = documentInstance.getOriginalAsset();
    AssetInfo detectedInfo = originalAsset.getDetectedInfo();
    Asset iconAsset = documentInstance.getIconAsset();
    Asset textAsset = documentInstance.getTextAsset();
    List<DocumentViewerItem> items = documentInstance.getItems();
    List<DocumentPage> pages = documentInstance.getPages();

    DocumentDto documentDto = documentMarshaller
        .marshallDocument(document);
    DocumentKey documentKey = documentDto.getKey();
    DocumentInstanceKey key = marshallDocumentInstanceKey(documentKey,
        documentInstance);
    AssetInfoDto detectedInfoDto = assetMarshaller
        .marshallInfo(detectedInfo);
    String viewerId = documentInstance.getViewerId();
    boolean hasIcon = iconAsset != null;
    boolean hasText = textAsset != null;
    List<DocumentPageDto> pageDtos = marshallPages(key, pages);
    List<DocumentViewerItemDto> itemDtos = marshallViewerItems(key, items);

    DocumentInstanceAndDocumentAndViewerElementsDto documentInstanceDto = new DocumentInstanceAndDocumentAndViewerElementsDto(
        key, detectedInfoDto, viewerId, hasIcon, hasText, documentDto,
        pageDtos, itemDtos);

    return documentInstanceDto;
  }

  private List<DocumentViewerItemDto> marshallViewerItems(
      DocumentInstanceKey documentInstanceKey, List<DocumentViewerItem> items) {
    List<DocumentViewerItemDto> itemDtos = new ArrayList<DocumentViewerItemDto>();
    for (DocumentViewerItem item : items) {
      DocumentViewerItemDto itemDto = marshallViewerItem(documentInstanceKey,
          item);
      itemDtos.add(itemDto);
    }
    return itemDtos;
  }

  public DocumentViewerItemDto marshallViewerItem(DocumentViewerItem item) {
    DocumentInstance documentInstance = item.getDocumentInstance();
    DocumentInstanceKey documentInstanceKey = marshallDocumentInstanceKey(documentInstance);
    return marshallViewerItem(documentInstanceKey, item);
  }

  private DocumentViewerItemDto marshallViewerItem(
      DocumentInstanceKey documentInstanceKey, DocumentViewerItem item) {
    DocumentViewerItemKey itemKey = marshallViewerItemKey(documentInstanceKey,
        item);
    DocumentViewerItemType type = item.getType();
    String marshalledValue = marshallViewerItemValue(item);

    DocumentViewerItemDto itemDto = new DocumentViewerItemDto(itemKey, type,
        marshalledValue);
    return itemDto;
  }

  public DocumentViewerItemKey marshallViewerItemKey(DocumentViewerItem item) {
    DocumentInstance documentInstance = item.getDocumentInstance();
    DocumentInstanceKey documentInstanceKey = marshallDocumentInstanceKey(documentInstance);
    return marshallViewerItemKey(documentInstanceKey, item);
  }

  private DocumentViewerItemKey marshallViewerItemKey(
      DocumentInstanceKey documentInstanceKey, DocumentViewerItem item) {
    String name = item.getName();
    DocumentViewerItemKey key = new DocumentViewerItemKey(documentInstanceKey,
        name);
    return key;
  }

  private String marshallViewerItemValue(DocumentViewerItem item) {
    DocumentViewerItemType type = item.getType();
    switch (type) {
    case ASSET:
      DocumentViewerAsset viewerAsset = (DocumentViewerAsset) item;
      return marshallViewerAssetValue(viewerAsset);
    case ATTRIBUTE:
      DocumentViewerAttribute viewerAttribute = (DocumentViewerAttribute) item;
      return marshallViewerAttributeValue(viewerAttribute);
    default:
      throw new IllegalArgumentException("invalid document viewer item type: "
          + item.getType() + " for item " + item.getId());
    }
  }

  private String marshallViewerAttributeValue(
      DocumentViewerAttribute viewerAttribute) {
    String value = viewerAttribute.getValue();
    return value;
  }

  private String marshallViewerAssetValue(DocumentViewerAsset viewerAsset) {
    String value = null;
    return value;
  }

  public DocumentInstance unmarshallDocumentInstanceKey(
      DocumentInstanceKey documentInstanceKey, boolean required) {
    DocumentKey documentKey = documentInstanceKey.getDocument();
    ManagementWorkspaceKey workspaceKey = documentKey.getWorkspace();
    SocialActorKey workspaceOwnerKey = workspaceKey.getOwner();

    String workspaceOwnerName = workspaceOwnerKey.getName();
    String workspaceName = workspaceKey.getName();
    String documentName = documentKey.getName();
    Integer version = documentInstanceKey.getVersion();

    DocumentInstance documentInstance = documentInstanceRepository.findOne(
        workspaceOwnerName, workspaceName, documentName, version);
    if (documentInstance == null && required) {
      throw new DocumentException(DocumentErrorCode.DOCUMENT_INSTANCE_NOT_FOUND, "Document instance " + documentInstanceKey
          + " not found.");
    }
    return documentInstance;
  }

  private List<DocumentPageDto> marshallPages(
      DocumentInstanceKey documentInstanceKey, List<DocumentPage> items) {
    List<DocumentPageDto> itemDtos = new ArrayList<DocumentPageDto>();
    for (DocumentPage item : items) {
      DocumentPageDto itemDto = marshallPage(documentInstanceKey, item);
      itemDtos.add(itemDto);
    }
    return itemDtos;
  }

  public DocumentPageDto marshallPage(DocumentPage page) {
    DocumentInstance documentInstance = page.getDocumentInstance();
    DocumentInstanceKey documentInstanceKey = marshallDocumentInstanceKey(documentInstance);
    return marshallPage(documentInstanceKey, page);
  }

  private DocumentPageDto marshallPage(DocumentInstanceKey documentInstanceKey,
      DocumentPage page) {
    Asset thumbnailAsset = page.getThumbnailAsset();

    DocumentPageKey pageKey = marshallPageKey(documentInstanceKey, page);
    boolean hasThumbnail = thumbnailAsset != null;

    DocumentPageDto pageDto = new DocumentPageDto(pageKey, hasThumbnail);
    return pageDto;
  }

  public DocumentPage unmarshallPageKey(DocumentPageKey pageKey,
      boolean required) {
    DocumentInstanceKey documentInstanceKey = pageKey.getDocumentInstance();
    DocumentKey documentKey = documentInstanceKey.getDocument();
    ManagementWorkspaceKey workspaceKey = documentKey.getWorkspace();
    SocialActorKey workspaceOwnerKey = workspaceKey.getOwner();

    String workspaceOwnerName = workspaceOwnerKey.getName();
    String workspaceName = workspaceKey.getName();
    String documentName = documentKey.getName();
    Integer documentInstanceVersion = documentInstanceKey.getVersion();
    Integer number = pageKey.getNumber();

    DocumentPage page = documentPageRepository.findOne(workspaceOwnerName,
        workspaceName, documentName, documentInstanceVersion, number);
    if (page == null && required) {
      throw new DocumentException(DocumentErrorCode.DOCUMENT_PAGE_NOT_FOUND, "Document page " + pageKey + " not found.");
    }
    return page;
  }

  public DocumentViewerAsset unmarshallViewerAssetKey(
      DocumentViewerItemKey itemKey, boolean required) {
    DocumentViewerItem item = unmarshallViewerItemKey(itemKey, required);
    if (item != null && item.getType() != DocumentViewerItemType.ASSET) {
      throw new DocumentException(DocumentErrorCode.DOCUMENT_VIEWER_ITEM_NOT_FOUND, "Document viewer item " + itemKey
          + " is not an asset.");
    }
    return (DocumentViewerAsset) item;
  }

  public DocumentViewerAttribute unmarshallViewerAttributeKey(
      DocumentViewerItemKey itemKey, boolean required) {
    DocumentViewerItem item = unmarshallViewerItemKey(itemKey, required);
    if (item != null && item.getType() != DocumentViewerItemType.ATTRIBUTE) {
      throw new DocumentException(DocumentErrorCode.DOCUMENT_VIEWER_ITEM_NOT_AN_ATTRIBUTE, "Document viewer item " + itemKey
          + " is not an attribute.");
    }
    return (DocumentViewerAttribute) item;
  }

  public DocumentViewerItem unmarshallViewerItemKey(
      DocumentViewerItemKey itemKey, boolean required) {
    DocumentInstanceKey documentInstanceKey = itemKey.getDocumentInstance();
    DocumentKey documentKey = documentInstanceKey.getDocument();
    ManagementWorkspaceKey workspaceKey = documentKey.getWorkspace();
    SocialActorKey workspaceOwnerKey = workspaceKey.getOwner();

    String workspaceOwnerName = workspaceOwnerKey.getName();
    String workspaceName = workspaceKey.getName();
    String documentName = documentKey.getName();
    Integer documentInstanceVersion = documentInstanceKey.getVersion();
    String name = itemKey.getName();

    DocumentViewerItem item = documentViewerItemRepository.findOne(
        workspaceOwnerName, workspaceName, documentName,
        documentInstanceVersion, name);
    if (item == null && required) {
      throw new DocumentException(DocumentErrorCode.DOCUMENT_VIEWER_ITEM_NOT_FOUND, "Document viewer item " + itemKey
          + " not found.");
    }
    return item;
  }

  public DocumentPageKey marshallPageKey(DocumentPage page) {
    DocumentInstance documentInstance = page.getDocumentInstance();

    DocumentInstanceKey documentInstanceKey = marshallDocumentInstanceKey(documentInstance);

    return marshallPageKey(documentInstanceKey, page);
  }

  public DocumentPageKey marshallPageKey(
      DocumentInstanceKey documentInstanceKey, DocumentPage page) {

    int number = page.getNumber();

    DocumentPageKey key = new DocumentPageKey(documentInstanceKey, number);
    return key;
  }

}
