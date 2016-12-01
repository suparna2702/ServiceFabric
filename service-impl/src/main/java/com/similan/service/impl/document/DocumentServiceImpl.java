package com.similan.service.impl.document;

import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.collaborationworkspace.SharedDocument;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.document.Document;
import com.similan.domain.entity.document.DocumentCategory;
import com.similan.domain.entity.document.DocumentInstance;
import com.similan.domain.entity.document.DocumentLabel;
import com.similan.domain.entity.managementworkspace.ManagementWorkspace;
import com.similan.domain.repository.collaborationworkspace.SharedDocumentRepository;
import com.similan.domain.repository.document.DocumentRepository;
import com.similan.domain.repository.wall.WallEntryRepository;
import com.similan.service.api.asset.AssetStream;
import com.similan.service.api.asset.NewAssetStream;
import com.similan.service.api.asset.dto.basic.AssetMediaType;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.DocumentInstanceService;
import com.similan.service.api.document.DocumentService;
import com.similan.service.api.document.dto.basic.DocumentDto;
import com.similan.service.api.document.dto.error.DocumentErrorCode;
import com.similan.service.api.document.dto.error.DocumentException;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentAndViewerElementsDto;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentDto;
import com.similan.service.api.document.dto.extended.DocumentStatisticsDto;
import com.similan.service.api.document.dto.key.DocumentCategoryKey;
import com.similan.service.api.document.dto.key.DocumentInstanceKey;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.document.dto.key.DocumentLabelKey;
import com.similan.service.api.document.dto.operation.DocumentInfoUpdateDto;
import com.similan.service.api.document.dto.operation.NewDocumentDto;
import com.similan.service.api.document.dto.operation.NewDocumentInstanceDto;
import com.similan.service.api.document.dto.operation.NewExternallyManagedDocumentDto;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;
import com.similan.service.impl.base.ServiceImpl;
import com.similan.service.impl.community.SocialActorMarshaller;
import com.similan.service.impl.managementworkspace.ManagementWorkspaceMarshaller;
import com.similan.service.internal.impl.security.component.DocumentInstanceEnforcer;

@Slf4j
@Service
public class DocumentServiceImpl extends ServiceImpl implements DocumentService {
  @Autowired
  private DocumentInstanceService documentInstanceService;
  @Autowired
  private DocumentRepository documentRepository;
  @Autowired
  private SharedDocumentRepository sharedDocumentRepository;
  @Autowired
  private WallEntryRepository wallEntryRepository;
  @Autowired
  private DocumentInstanceEnforcer documentEnforcer;
  @Autowired
  private SocialActorMarshaller actorMarshaller;
  @Autowired
  private ManagementWorkspaceMarshaller managementWorkspaceMarshaller;
  @Autowired
  private DocumentMarshaller documentMarshaller;
  @Autowired
  private DocumentInstanceMarshaller documentInstanceMarshaller;

  @Override
  @Transactional
  public DocumentDto create(DocumentKey documentKey,
      NewExternallyManagedDocumentDto newDocumentDto) {

    throw new UnsupportedOperationException("Not yet implemented");

  }

  @Override
  @Transactional
  public DocumentInstanceAndDocumentDto create(DocumentKey documentKey,
      NewDocumentDto newDocumentDto, NewAssetStream assetStream) {

    ManagementWorkspaceKey workspaceKey = documentKey.getWorkspace();
    List<DocumentLabelKey> labelKeys = newDocumentDto.getLabels();
    List<DocumentCategoryKey> categoryKeys = newDocumentDto.getCategories();

    ManagementWorkspace workspace = managementWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);

    String name = documentKey.getName();
    String description = newDocumentDto.getDescription();
    List<DocumentLabel> labels = documentMarshaller
        .unmarshallLabelKeys(labelKeys);
    List<DocumentCategory> categories = documentMarshaller
        .unmarshallCategoryKeys(categoryKeys);

    Document document = documentRepository.create(workspace, name, description,
        labels, categories);
    documentRepository.save(document);

    SocialActorKey uploaderKey = newDocumentDto.getUploader();
    String explicitFilename = newDocumentDto.getExplicitFilename();

    NewDocumentInstanceDto newDocumentInstance = new NewDocumentInstanceDto(
        uploaderKey, explicitFilename);
    DocumentInstanceAndDocumentDto documentInstanceDto = documentInstanceService
        .create(documentKey, newDocumentInstance, assetStream);
    return documentInstanceDto;
  }

  @Override
  @Transactional(readOnly = true)
  public DocumentInstanceAndDocumentDto get(DocumentKey documentKey) {
    Document document = documentMarshaller.unmarshallDocumentKey(documentKey,
        true);
    DocumentInstance lastInstance = document.getLastInstance();
    DocumentInstanceAndDocumentDto documentInstanceDto = documentInstanceMarshaller
        .marshallDocumentInstanceAndDocument(lastInstance);
    return documentInstanceDto;
  }

  @Override
  @Transactional
  public void delete(DocumentKey documentKey) {
    Document document = documentMarshaller.unmarshallDocumentKey(documentKey,
        true);
    document.setDeletionDate(new Date());
  }

  @Override
  @Transactional
  public boolean lock(DocumentKey documentKey, SocialActorKey lockOwnerKey) {
    Document document = documentMarshaller.unmarshallDocumentKey(documentKey,
        true);
    SocialActor lockOwner = actorMarshaller.unmarshallActorKey(lockOwnerKey,
        true);
    SocialActor currentLockOwner = document.getLockOwner();
    if (currentLockOwner == lockOwner) {
      return false;
    } else if (currentLockOwner != null) {
      SocialActorKey currentLockOwnerKey = actorMarshaller
          .marshallActorKey(currentLockOwner);
      throw new DocumentException(DocumentErrorCode.DOCUMENT_ALREADY_LOCKED,
          "Lock on docuemnt  " + documentKey + " is not held by "
              + lockOwnerKey + ", but by " + currentLockOwnerKey);
    } else {
      // if not locked
      document.setLockOwner(lockOwner);
      return true;
    }
  }

  @Override
  @Transactional
  public boolean unlock(DocumentKey documentKey, SocialActorKey lockOwnerKey) {
    Document document = documentMarshaller.unmarshallDocumentKey(documentKey,
        true);
    SocialActor lockOwner = actorMarshaller.unmarshallActorKey(lockOwnerKey,
        true);
    SocialActor currentLockOwner = document.getLockOwner();
    if (currentLockOwner == lockOwner) {
      document.setLockOwner(null);
      return true;
    } else if (currentLockOwner != null) {
      SocialActorKey currentLockOwnerKey = actorMarshaller
          .marshallActorKey(currentLockOwner);
      throw new DocumentException(DocumentErrorCode.DOCUMENT_LOCK_NOT_HELD,
          "Lock on docuemnt  " + documentKey + " is not held by "
              + lockOwnerKey + ", but by " + currentLockOwnerKey);
    } else {
      // if not locked
      return false;
    }
  }

  @Override
  @Transactional(readOnly = true)
  public SocialActorKey getLockOwner(DocumentKey documentKey) {
    Document document = documentMarshaller.unmarshallDocumentKey(documentKey,
        true);
    SocialActor lockOwner = document.getLockOwner();
    if (lockOwner == null) {
      return null;
    }
    SocialActorKey lockOwnerKey = actorMarshaller.marshallActorKey(lockOwner);
    return lockOwnerKey;
  }

  @Override
  @Transactional
  public AssetStream checkout(DocumentKey documentKey,
      SocialActorKey lockOwnerKey) {
    lock(documentKey, lockOwnerKey);
    return retrieveLastOriginal(documentKey);
  }

  private AssetStream retrieveLastOriginal(DocumentKey documentKey) {
    Document document = documentMarshaller.unmarshallDocumentKey(documentKey,
        true);
    DocumentInstance lastInstance = document.getLastInstance();
    DocumentInstanceKey lastInstanceKey = documentInstanceMarshaller
        .marshallDocumentInstanceKey(lastInstance);
    return documentInstanceService.retrieveOriginal(lastInstanceKey);
  }

  @Override
  @Transactional
  public DocumentInstanceAndDocumentDto checkin(DocumentKey documentKey,
      NewDocumentInstanceDto newDocumentInstanceDto, NewAssetStream assetStream) {
    SocialActorKey uploaderKey = newDocumentInstanceDto.getUploader();
    DocumentInstanceAndDocumentDto documentInstanceDto = documentInstanceService
        .create(documentKey, newDocumentInstanceDto, assetStream);
    unlock(documentKey, uploaderKey);
    return documentInstanceDto;

  }

  @Override
  @Transactional
  public DocumentStatisticsDto getStatistics(DocumentKey documentKey) {

    Document doc = documentMarshaller.unmarshallDocumentKey(documentKey, true);
    List<SharedDocument> sharedDocList = this.sharedDocumentRepository
        .findAllForDocument(doc);

    Long viewCount = 0L;
    Long downloadCount = 0L;
    Long commentCount = 0L;

    for (SharedDocument sharedDocument : sharedDocList) {
      Long sharedDownloadCount = this.wallEntryRepository
          .findDocumentDownloadedWallEntryCount(sharedDocument.getId(),
              sharedDocument.getWorkspace().getId());

      Long sharedViewCount = this.wallEntryRepository
          .findDocumentViewedWallEntryCount(sharedDocument.getId(),
              sharedDocument.getWorkspace().getId());

      Long sharedCommentCount = this.wallEntryRepository
          .findCommentWallEntryCount(sharedDocument.getId(), sharedDocument
              .getWorkspace().getId());

      viewCount += sharedViewCount;
      downloadCount += sharedDownloadCount;
      commentCount += sharedCommentCount;

    }

    // now get stats from management space
    ManagementWorkspace managementWorkspace = doc.getWorkspace();
    Long managementWorkspaceViewCount = this.wallEntryRepository
        .findDocumentViewedWallEntryCount(doc.getId(),
            managementWorkspace.getId());

    log.info("Viewed count of " + doc.getId() + " in management space "
        + managementWorkspace.getId() + " is " + managementWorkspaceViewCount);

    viewCount += managementWorkspaceViewCount;

    Long managementWorkspaceDownloadCount = this.wallEntryRepository
        .findDocumentDownloadedWallEntryCount(doc.getId(),
            managementWorkspace.getId());

    log.info("Download count of " + doc.getId() + " in management space "
        + managementWorkspace.getId() + " is "
        + managementWorkspaceDownloadCount);

    downloadCount += managementWorkspaceDownloadCount;

    DocumentStatisticsDto statisticsDto = new DocumentStatisticsDto(
        documentKey, viewCount, commentCount, downloadCount);
    return statisticsDto;
  }

  @Override
  @Transactional
  public String getDocumentIcon(DocumentKey documentKey) {
    DocumentInstanceAndDocumentDto documentInstance = this.get(documentKey);
    return this.getAssetMediaTypeIcon(documentInstance);
  }

  public String getMimeTypeIcon(String mimeType) {
    log.warn("Getting icon for " + mimeType);

    if (mimeType.toLowerCase().startsWith("image"))
      return "page_white_picture48.gif";
    if ("application/pdf".equalsIgnoreCase(mimeType))
      return "page_white_acrobat48.gif";
    if ("application/msword".equalsIgnoreCase(mimeType))
      return "word48.gif";
    if ("application/vnd.ms-excel".equalsIgnoreCase(mimeType))
      return "excel48.gif";
    if ("application/vnd.ms-powerpoint".equalsIgnoreCase(mimeType))
      return "powerpoint48.gif";
    if ("Folder".equalsIgnoreCase(mimeType)
        || "application/vnd.google-apps.folder".equalsIgnoreCase(mimeType))
      return "folder48.gif";
    return "documentlist48.png";
  }

  public String getPageStorageKey(DocumentInstanceAndDocumentDto data) {
    // {workspaceOwnerName}/{workspaceName}/{documentName}/{documentInstanceVersion}/{number}",
    // method = RequestMethod.GET
    String documentStorageKey = StringUtils.EMPTY;
    if (StringUtils.isBlank(documentStorageKey)) {
      try {
        StringBuilder builder = new StringBuilder();
        builder
            .append(
                data.getKey().getDocument().getWorkspace().getOwner().getName())
            .append('/')
            .append(
                URLEncoder.encode(data.getKey().getDocument().getWorkspace()
                    .getName(), "ISO-8859-1"))
            .append('/')
            .append(
                URLEncoder.encode(data.getKey().getDocument().getName(),
                    "ISO-8859-1")).append('/')
            .append(data.getKey().getVersion());

        documentStorageKey = builder.toString();
      } catch (Exception e) {
        documentStorageKey = "";
      }
    }
    return documentStorageKey;
  }

  public String getAssetMediaTypeIcon(
      DocumentInstanceAndDocumentDto documentInstance) {

    if (documentInstance.getHasIcon()) {
      return "/spring/documenticon/" + this.getPageStorageKey(documentInstance);
    }
    AssetMediaType type = documentInstance.getDetectedInfo().getMediaType();
    return "/images/mimetype/" + getMimeTypeIcon(type.getDescriptor());
  }

  @Override
  @Transactional
  public DocumentInstanceAndDocumentAndViewerElementsDto updateDocumentInfo(
      DocumentKey documentKey, DocumentInfoUpdateDto updateInfo) {

    if (StringUtils.isBlank(updateInfo.getName())) {
      throw new DocumentException(DocumentErrorCode.DOCUMENT_NAME_EMPTY,
          " document name cannopt be empty for document  " + documentKey);
    }

    Document document = documentMarshaller.unmarshallDocumentKey(documentKey,
        true);

    if (!StringUtils.isBlank(updateInfo.getName())) {
      document.setName(updateInfo.getName());
    }

    if (!StringUtils.isBlank(updateInfo.getDescription())) {
      document.setDescription(updateInfo.getDescription());
    }

    this.documentRepository.save(document);
    DocumentInstance lastInstance = document.getLastInstance();
    DocumentInstanceAndDocumentAndViewerElementsDto documentInstanceDto = this.documentInstanceService
        .getForViewer(documentInstanceMarshaller
            .marshallDocumentInstanceKey(lastInstance));
    return documentInstanceDto;
  }
}
