package com.similan.service.impl.collaborationworkspace;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.SharedDocument;
import com.similan.domain.entity.comment.Comment;
import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.community.ExternalSocialPerson;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.document.Document;
import com.similan.domain.entity.document.DocumentInstance;
import com.similan.domain.repository.collaborationworkspace.SharedDocumentRepository;
import com.similan.domain.repository.community.ExternalSocialPersonRepository;
import com.similan.domain.repository.share.ExternalSharedRepository;
import com.similan.domain.share.ExternalShared;
import com.similan.domain.share.ISharable;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.service.api.asset.AssetStream;
import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.collaborationworkspace.CollaborationWorkspaceSharedDocumentService;
import com.similan.service.api.collaborationworkspace.dto.basic.SharedDocumentAndStatisticsDto;
import com.similan.service.api.collaborationworkspace.dto.basic.SharedDocumentDto;
import com.similan.service.api.collaborationworkspace.dto.error.CollaborationWorkspaceErrorCode;
import com.similan.service.api.collaborationworkspace.dto.error.CollaborationWorkspaceException;
import com.similan.service.api.collaborationworkspace.dto.extended.SharedDocumentAndDocumentInstanceAndDocumentAndViewerElementsDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.collaborationworkspace.dto.key.SharedDocumentKey;
import com.similan.service.api.collaborationworkspace.dto.operation.NewDocumentSharedWithOtherWorkspaceDto;
import com.similan.service.api.collaborationworkspace.dto.operation.NewExternalSharedDocumentDto;
import com.similan.service.api.collaborationworkspace.dto.operation.NewSharedDocumentCommentDto;
import com.similan.service.api.collaborationworkspace.dto.operation.NewSharedDocumentDto;
import com.similan.service.api.comment.CommentService;
import com.similan.service.api.comment.dto.basic.CommentDto;
import com.similan.service.api.comment.dto.operation.NewCommentDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.DocumentInstanceService;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentAndViewerElementsDto;
import com.similan.service.api.document.dto.key.DocumentInstanceKey;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;
import com.similan.service.api.profile.dto.ExternalSocialPersonDto;
import com.similan.service.api.share.ExternalSharedKey;
import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.impl.base.ServiceImpl;
import com.similan.service.impl.comment.CommentMarshaller;
import com.similan.service.impl.community.SocialActorMarshaller;
import com.similan.service.impl.document.DocumentInstanceMarshaller;
import com.similan.service.impl.document.DocumentMarshaller;
import com.similan.service.internal.api.email.EmailInternalService;
import com.similan.service.internal.api.email.EmailParameterConstants;
import com.similan.service.internal.api.email.io.NewEmail;
import com.similan.service.internal.api.event.EventInternalService;
import com.similan.service.internal.api.event.io.collaborationworkspace.CollaborationWorkspaceExternalSharedEvent;
import com.similan.service.internal.api.event.io.collaborationworkspace.DocumentCommentEvent;
import com.similan.service.internal.api.event.io.collaborationworkspace.DocumentDownloadedEvent;
import com.similan.service.internal.api.event.io.collaborationworkspace.DocumentSharedEvent;
import com.similan.service.internal.api.event.io.collaborationworkspace.DocumentSharedWithOtherWorkspaceEvent;
import com.similan.service.internal.api.event.io.collaborationworkspace.DocumentUnsharedEvent;
import com.similan.service.internal.api.event.io.collaborationworkspace.DocumentUpdatEvent;
import com.similan.service.internal.api.event.io.collaborationworkspace.DocumentViewedInWorkspaceEvent;
import com.similan.service.internal.api.event.io.externalshare.ExternalShareViewEvent;
import com.similan.service.internal.impl.security.component.CollaborationWorkspaceEnforcer;

@Slf4j
@Service
public class CollaborationWorkspaceSharedDocumentServiceImpl extends
    ServiceImpl implements CollaborationWorkspaceSharedDocumentService {
  @Autowired
  private SharedDocumentRepository sharedDocumentRepository;
  @Autowired
  private EmailInternalService emailMessagingService;
  @Autowired
  private PlatformCommonSettings commonSettings;
  @Autowired
  private EventInternalService eventInternalService;
  @Autowired
  private DocumentInstanceService documentInstanceService;
  @Autowired
  private CollaborationWorkspaceNotificationConfigurationFilter notificationFilter;
  @Autowired
  private CommentService commentService;
  @Autowired
  private ExternalSocialPersonRepository externalSocialPersonRepository;
  @Autowired
  private ExternalSharedRepository externalSharedRepository;
  @Autowired
  private CollaborationWorkspaceMarshaller collaborationWorkspaceMarshaller;
  @Autowired
  private DocumentInstanceMarshaller documentInstanceMarshaller;
  @Autowired
  private SocialActorMarshaller actorMarshaller;
  @Autowired
  private DocumentMarshaller documentMarshaller;
  @Autowired
  private CommentMarshaller commentMarshaller;
  @Autowired
  private CollaborationWorkspaceEnforcer collaborationWorkspaceEnforcer;

  protected CollaborationWorkspaceSharedDocumentServiceImpl() {
    this.sharedDocumentRepository = null;
    this.emailMessagingService = null;
    this.commonSettings = null;
    this.eventInternalService = null;
    this.documentInstanceService = null;
  }

  @Override
  @Transactional
  public SharedDocumentDto get(SharedDocumentKey sharedDocumentKey) {
    SharedDocument sharedDoc = collaborationWorkspaceMarshaller
        .unmarshallSharedDocumentKey(sharedDocumentKey, true);
    SharedDocumentDto retDto = collaborationWorkspaceMarshaller
        .marshallSharedDocument(sharedDoc);
    return retDto;
  }

  @Override
  @Transactional
  public SharedDocumentAndDocumentInstanceAndDocumentAndViewerElementsDto getForViewer(
      Long id, SocialActorKey viewerKey) throws URISyntaxException {

    SharedDocument sharedDoc = this.sharedDocumentRepository.findOne(id);
    log.info("Found shared document " + sharedDoc.getId() + " name "
        + sharedDoc.getUuid());

    SharedDocumentKey sharedDocKey = collaborationWorkspaceMarshaller
        .marshallSharedDocumentKey(sharedDoc);

    log.info("Shared document key " + sharedDocKey);

    SharedDocumentAndDocumentInstanceAndDocumentAndViewerElementsDto retDto = this
        .getForViewer(sharedDoc, sharedDocKey, viewerKey);

    return retDto;
  }

  @Override
  @Transactional
  public DocumentInstanceAndDocumentAndViewerElementsDto getForViewer(
      ExternalSharedKey sharedKey) {

    ExternalShared shared = this.externalSharedRepository
        .findBySharedName(sharedKey.getName());

    GenericReference<ISharable> sharedEntity = shared.getSharedEntity();
    EntityType entiyType = sharedEntity.getType();

    switch (entiyType) {
    case SHARED_DOCUMENT: {
      SharedDocument sharedDocument = this.sharedDocumentRepository
          .findOne(sharedEntity.getId());

      if (sharedDocument.getUnshared() == Boolean.TRUE) {
        throw new CollaborationWorkspaceException(
            CollaborationWorkspaceErrorCode.SHARED_DOCUMENT_UNSHARED,
            "Document " + sharedKey + " document is no longer available.");
      }

      DocumentInstanceAndDocumentAndViewerElementsDto docInstanceViewerElement = this.documentInstanceMarshaller
          .marshallDocumentInstanceAndDocumentAndViewerElements(sharedDocument
              .getDocument().getLastInstance());

      // for feed distribution
      eventInternalService.fire(new ExternalShareViewEvent(shared.getId()));

      // send email notification as well
      Map<String, Object> parameters = new HashMap<String, Object>();
      parameters.put("workspaceName", sharedDocument.getWorkspace().getName());
      parameters.put("documentName", sharedDocument.getDocument().getName());
      parameters.put("viewerName", shared.getSharedTo().getDisplayName());

      String logoUrl = commonSettings.getPortalApplcationUrl().getValue()
          + "spring/assets"
          + sharedDocument.getWorkspace().getOwner().getImage();
      parameters.put(EmailParameterConstants.BUSINESS_LOGO, logoUrl);

      List<SocialActor> recepients = new ArrayList<SocialActor>();
      recepients.add(shared.getSharedBy());

      NewEmail newEmail = new NewEmail(shared.getSharedBy(), recepients,
          "collabSpaceExternalDocumentSharedView.vm", parameters);
      this.emailMessagingService.send(newEmail);

      return docInstanceViewerElement;
    }
    default:
      break;
    }

    return null;
  }

  @Override
  @Transactional
  public SharedDocumentAndDocumentInstanceAndDocumentAndViewerElementsDto getForViewer(
      SharedDocumentKey sharedDocumentKey, SocialActorKey viewerKey)
      throws URISyntaxException {

    SharedDocument sharedDoc = collaborationWorkspaceMarshaller
        .unmarshallSharedDocumentKey(sharedDocumentKey, true);

    SharedDocumentAndDocumentInstanceAndDocumentAndViewerElementsDto retDto = this
        .getForViewer(sharedDoc, sharedDocumentKey, viewerKey);

    return retDto;

  }

  private SharedDocumentAndDocumentInstanceAndDocumentAndViewerElementsDto getForViewer(
      SharedDocument sharedDoc, SharedDocumentKey sharedDocumentKey,
      SocialActorKey viewerKey) throws URISyntaxException {

    Document doc = sharedDoc.getDocument();

    log.info("Document associated with shared doc " + doc.getName());

    CollaborationWorkspace workspace = sharedDoc.getWorkspace();

    SocialActor viewer = actorMarshaller.unmarshallActorKey(viewerKey, true);

    DocumentInstance docInstance = doc.getLastInstance();
    log.info("Document instance associated with doc "
        + docInstance.getVersion());

    DocumentInstanceAndDocumentAndViewerElementsDto docInstanceViewerElement = this.documentInstanceMarshaller
        .marshallDocumentInstanceAndDocumentAndViewerElements(docInstance);
    log.info("Document instance associated with doc "
        + docInstanceViewerElement.getViewerId());

    SocialActorKey sharer = new SocialActorKey(sharedDoc.getCreator().getName());
    SharedDocumentAndDocumentInstanceAndDocumentAndViewerElementsDto retDto = new SharedDocumentAndDocumentInstanceAndDocumentAndViewerElementsDto(
        sharedDocumentKey, docInstanceViewerElement, sharer,
        sharedDoc.getCreationDate());
    retDto.setUnshared(sharedDoc.getUnshared());

    if (log.isDebugEnabled()) {
      log.info("Shared status " + sharedDoc.getUnshared()
          + " return dto shared status " + retDto.getUnshared());
    }

    // if it is unshared already no need to send email notification etc.
    if (retDto.getUnshared() == Boolean.TRUE) {
      return retDto;
    }

    // for feed distribution
    eventInternalService.fire(new DocumentViewedInWorkspaceEvent(sharedDoc
        .getId(), viewer.getId()));

    // Send email
    String hostUrl = commonSettings.getPortalApplcationUrl().getValue();
    URI uri = new URIBuilder(hostUrl
        + "collabspace/workspaceDocumentDetail.xhtml").setParameter("sdoc",
        sharedDoc.getId().toString()).build();

    log.info("sharedDocumentUrl " + uri.toString());

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("sharedDocumentUrl", uri.toString());
    parameters.put("workspaceName", workspace.getName());
    parameters.put("documentName", doc.getName());
    parameters.put("viewerName", viewer.getDisplayName());

    String logoUrl = commonSettings.getPortalApplcationUrl().getValue()
        + "spring/assets" + sharedDoc.getWorkspace().getOwner().getImage();
    parameters.put(EmailParameterConstants.BUSINESS_LOGO, logoUrl);

    List<SocialActor> participants = this.notificationFilter.getParticipants(
        workspace, viewer,
        WallEntryType.COLLABORATION_WORKSPACE_DOCUMENT_VIEWED);

    NewEmail newEmail = new NewEmail(viewer, participants,
        "collabSpaceDocumentViewer.vm", parameters);
    this.emailMessagingService.send(newEmail);

    return retDto;
  }

  @Override
  @Transactional
  public List<SharedDocumentDto> getByWorkspace(
      CollaborationWorkspaceKey workspaceKey) {

    CollaborationWorkspace workspace = collaborationWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);

    List<SharedDocument> sharedDocuments = sharedDocumentRepository
        .findByWorkspace(workspace);

    List<SharedDocumentDto> sharedDocumentDtos = collaborationWorkspaceMarshaller
        .marshallSharedDocuments(sharedDocuments);
    return sharedDocumentDtos;
  }

  private Boolean isDocumentExists(Document document,
      CollaborationWorkspace workspace) {

    log.info("Evaluating document exists or not " + document.getName()
        + " in workspace " + workspace.getName());
    List<SharedDocument> docs = this.sharedDocumentRepository
        .findSharedDocumentInWorkspace(document, workspace);

    if (docs != null && docs.size() > 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 
   * @param sharedDocumentKey
   * @param newDocumentShare
   * @throws URISyntaxException
   */
  @Override
  @Transactional
  public void share(SharedDocumentKey sharedDocumentKey,
      NewExternalSharedDocumentDto newDocumentShare) throws URISyntaxException {

    SharedDocument sharedDoc = collaborationWorkspaceMarshaller
        .unmarshallSharedDocumentKey(sharedDocumentKey, true);

    SocialActor sharer = actorMarshaller.unmarshallActorKey(
        newDocumentShare.getSharer(), true);

    List<ExternalSocialPersonDto> shareList = newDocumentShare.getShareList();

    for (ExternalSocialPersonDto expernalPerson : shareList) {

      ExternalSocialPerson sharedTo = this.externalSocialPersonRepository
          .findByPrimaryEmail(expernalPerson.getPrimaryEmail());

      if (sharedTo == null) {
        sharedTo = this.externalSocialPersonRepository.create(
            expernalPerson.getFirstName(), expernalPerson.getLastName(),
            expernalPerson.getMobilePhone(), expernalPerson.getPrimaryEmail());
        this.externalSocialPersonRepository.save(sharedTo);
      }

      ExternalShared shared = this.externalSharedRepository.create(
          (SocialPerson) sharer, sharedTo, (ISharable) sharedDoc);
      shared.setHeader(newDocumentShare.getHeader());
      shared.setMessage(newDocumentShare.getMessage());
      this.externalSharedRepository.save(shared);

      // fire the event
      CollaborationWorkspaceExternalSharedEvent sharedEvent = new CollaborationWorkspaceExternalSharedEvent();
      sharedEvent.setExternalShared(shared.getId());
      sharedEvent.setSharedFromWorkspace(sharedDoc.getWorkspace().getId());
      this.eventInternalService.fire(sharedEvent);

      Map<String, Object> parameters = new HashMap<String, Object>();
      parameters.put("shareFrom", sharer.getDisplayName());
      parameters.put("shareTo", sharedTo.getDisplayName());

      String logoUrl = commonSettings.getPortalApplcationUrl().getValue()
          + "spring/assets" + sharedDoc.getWorkspace().getOwner().getImage();
      parameters.put(EmailParameterConstants.BUSINESS_LOGO, logoUrl);

      String hostUrl = commonSettings.getPortalApplcationUrl().getValue();
      URI uri = new URIBuilder(hostUrl
          + "collabspace/externalSharedDocumentView.xhtml").setParameter(
          "share", shared.getSharedName()).build();

      log.info("sharedDocumentUrl " + uri.toString());

      parameters.put("sharedDocumentUrl", uri.toString());
      parameters.put("documentName", sharedDoc.getDocument().getName());

      List<SocialActor> sharedToList = new ArrayList<SocialActor>();
      sharedToList.add(sharedTo);

      NewEmail newEmail = new NewEmail(sharer, sharedToList,
          "collabSpaceDocumentExternalShare.vm", parameters);
      this.emailMessagingService.send(newEmail);
    }

  }

  @Override
  @Transactional
  public SharedDocumentDto share(
      SharedDocumentKey sharedDocumentKey,
      NewDocumentSharedWithOtherWorkspaceDto newDocumentSharedWithOtherWorkspaceDto)
      throws URISyntaxException {

    SocialActorKey actorKey = newDocumentSharedWithOtherWorkspaceDto
        .getSharer();
    SocialPerson sharer = (SocialPerson) actorMarshaller.unmarshallActorKey(
        actorKey, true);

    CollaborationWorkspaceKey workspaceFromKey = sharedDocumentKey
        .getWorkspace();
    CollaborationWorkspace workspaceFrom = collaborationWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceFromKey, true);

    CollaborationWorkspaceKey workspaceToKey = newDocumentSharedWithOtherWorkspaceDto
        .getWorkspaceToKey();
    CollaborationWorkspace workspaceTo = collaborationWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceToKey, true);

    DocumentKey documentKey = sharedDocumentKey.getDocument();
    Document document = documentMarshaller.unmarshallDocumentKey(documentKey,
        true);

    // check whether it has been shared already
    if (isDocumentExists(document, workspaceTo)) {
      throw new CollaborationWorkspaceException(
          CollaborationWorkspaceErrorCode.DOCUMENT_ALREADY_SHARED,
          "Cannot shared the document since it already exists");
    }

    SharedDocument sharedDocument = this.sharedDocumentRepository.create(
        workspaceTo, sharer, new Date(), document);
    this.sharedDocumentRepository.save(sharedDocument);

    List<SocialActor> participants = this.notificationFilter.getParticipants(
        workspaceTo, sharer,
        WallEntryType.COLLABORATION_WORKSPACE_DOCUMENT_SHARED);

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("workspaceFromName", workspaceFrom.getName());
    parameters.put("workspaceToName", workspaceTo.getName());

    String logoUrl = commonSettings.getPortalApplcationUrl().getValue()
        + "spring/assets" + workspaceFrom.getOwner().getImage();
    parameters.put(EmailParameterConstants.BUSINESS_LOGO, logoUrl);

    String hostUrl = commonSettings.getPortalApplcationUrl().getValue();
    URI uri = new URIBuilder(hostUrl
        + "collabspace/workspaceDocumentDetail.xhtml").setParameter("sdoc",
        sharedDocument.getId().toString()).build();

    log.info("sharedDocumentUrl " + uri.toString());

    parameters.put("sharedDocumentUrl", uri.toString());
    parameters.put("documentName", documentKey.getName());
    String fullName = sharer.getFirstName() + " " + sharer.getLastName();
    parameters.put("sharedBy", fullName);

    NewEmail newEmail = new NewEmail(sharer, participants,
        "collabSpaceToCollabSpaceDocumentShare.vm", parameters);
    this.emailMessagingService.send(newEmail);

    eventInternalService.fire(new DocumentSharedWithOtherWorkspaceEvent(
        sharedDocument.getId(), workspaceFrom.getId()));

    SharedDocumentDto sharedDocumentDto = collaborationWorkspaceMarshaller
        .marshallSharedDocument(sharedDocument);

    return sharedDocumentDto;

  }

  @Override
  @Transactional
  public SharedDocumentDto share(SharedDocumentKey sharedDocumentKey,
      NewSharedDocumentDto newSharedDocumentDto) throws URISyntaxException {

    SocialActorKey actorKey = newSharedDocumentDto.getSharer();
    SocialPerson sharer = (SocialPerson) actorMarshaller.unmarshallActorKey(
        actorKey, true);

    CollaborationWorkspaceKey workspaceKey = sharedDocumentKey.getWorkspace();
    CollaborationWorkspace workspace = collaborationWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);

    DocumentKey documentKey = sharedDocumentKey.getDocument();
    Document document = documentMarshaller.unmarshallDocumentKey(documentKey,
        true);

    // check whether it has been shared already
    if (isDocumentExists(document, workspace)) {
      throw new CollaborationWorkspaceException(
          CollaborationWorkspaceErrorCode.DOCUMENT_ALREADY_SHARED,
          "Cannot shared the document since it already exists");
    }

    SharedDocument sharedDocument = this.sharedDocumentRepository.create(
        workspace, sharer, new Date(), document);
    this.sharedDocumentRepository.save(sharedDocument);

    List<SocialActor> participants = this.notificationFilter.getParticipants(
        workspace, sharer,
        WallEntryType.COLLABORATION_WORKSPACE_DOCUMENT_SHARED);

    String hostUrl = commonSettings.getPortalApplcationUrl().getValue();
    URI uri = new URIBuilder(hostUrl
        + "collabspace/workspaceDocumentDetail.xhtml").setParameter("sdoc",
        sharedDocument.getId().toString()).build();

    log.info("sharedDocumentUrl " + uri.toString());

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("workspaceName", workspace.getName());
    parameters.put("sharedDocumentUrl", uri.toString());
    parameters.put("documentName", documentKey.getName());
    String fullName = sharer.getFirstName() + " " + sharer.getLastName();
    parameters.put("sharedBy", fullName);

    String logoUrl = commonSettings.getPortalApplcationUrl().getValue()
        + "spring/assets" + workspace.getOwner().getImage();
    parameters.put(EmailParameterConstants.BUSINESS_LOGO, logoUrl);

    NewEmail newEmail = new NewEmail(sharer, participants,
        "collabSpaceDocumentShare.vm", parameters);
    this.emailMessagingService.send(newEmail);

    eventInternalService.fire(new DocumentSharedEvent(sharedDocument.getId()));

    SharedDocumentDto sharedDocumentDto = collaborationWorkspaceMarshaller
        .marshallSharedDocument(sharedDocument);
    return sharedDocumentDto;
  }

  @Override
  @Transactional
  public AssetStream download(SocialActorKey downloader,
      SharedDocumentKey sharedDocumentKey) {

    SocialActor downloaderActor = actorMarshaller.unmarshallActorKey(
        downloader, true);
    SharedDocument sharedDocument = collaborationWorkspaceMarshaller
        .unmarshallSharedDocumentKey(sharedDocumentKey, true);

    DocumentInstanceKey docInstanceKey = this.documentInstanceMarshaller
        .marshallDocumentInstanceKey(sharedDocument.getDocument()
            .getLastInstance());
    AssetStream assetStream = documentInstanceService
        .retrieveOriginal(docInstanceKey);

    /* now fire the download event */
    DocumentDownloadedEvent downloadEvent = new DocumentDownloadedEvent(
        sharedDocument.getId(), downloaderActor.getId());
    this.eventInternalService.fire(downloadEvent);

    return assetStream;
  }

  @Override
  @Transactional
  public List<SharedDocumentAndStatisticsDto> getSharedDocumentAndStatistics(
      CollaborationWorkspaceKey workspaceKey, SocialActorKey viewerKey) {

    CollaborationWorkspace workspace = collaborationWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);
    List<SharedDocument> sharedDocumentList = this.sharedDocumentRepository
        .findByWorkspace(workspace);

    List<SharedDocumentAndStatisticsDto> retList = collaborationWorkspaceMarshaller
        .marshallSharedDocumentsAndStatistics(sharedDocumentList);
    return retList;
  }

  @Override
  @Transactional
  public SharedDocumentAndStatisticsDto getSharedDocumentAndStatistics(
      SharedDocumentKey docKey) {
    SharedDocument sharedDoc = collaborationWorkspaceMarshaller
        .unmarshallSharedDocumentKey(docKey, true);
    SharedDocumentAndStatisticsDto ret = collaborationWorkspaceMarshaller
        .marshallSharedDocumentAndStatistics(sharedDoc);
    return ret;
  }

  @Override
  @Transactional
  public CommentDto<SharedDocumentKey> post(NewSharedDocumentCommentDto comment)
      throws URISyntaxException {

    log.info("Comment posted for document " + comment);
    SharedDocument sharedDocument = collaborationWorkspaceMarshaller
        .unmarshallSharedDocumentKey(comment.getDocument(), true);
    SocialActor commenter = actorMarshaller.unmarshallActorKey(
        comment.getAuthor(), true);

    NewCommentDto newCommentDto = new NewCommentDto(comment.getAuthor()
        .getName(), comment.getContent());
    CommentDto<SharedDocumentKey> retComment = this.commentService.postComment(
        comment.getDocument(), newCommentDto);

    CollaborationWorkspace workspace = collaborationWorkspaceMarshaller
        .unmarshallWorkspaceKey(comment.getDocument().getWorkspace(), true);

    Comment postedComment = commentMarshaller.unmarshallCommentKey(
        retComment.getKey(), true);

    // send notification email
    String hostUrl = commonSettings.getPortalApplcationUrl().getValue();
    URI uri = new URIBuilder(hostUrl
        + "collabspace/workspaceDocumentDetail.xhtml").setParameter("sdoc",
        sharedDocument.getId().toString()).build();

    log.info("sharedDocumentUrl " + uri.toString());

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("workspaceName", comment.getDocument().getWorkspace()
        .getName());
    parameters.put("sharedDocumentUrl", uri.toString());
    parameters.put("documentName", comment.getDocument().getDocument()
        .getName());

    String fullName = commenter.getDisplayName();
    parameters.put("commentedBy", fullName);

    String logoUrl = commonSettings.getPortalApplcationUrl().getValue()
        + "spring/assets" + workspace.getOwner().getImage();
    parameters.put(EmailParameterConstants.BUSINESS_LOGO, logoUrl);

    List<SocialActor> participants = this.notificationFilter.getParticipants(
        workspace, commenter,
        WallEntryType.COLLABORATION_WORKSPACE_DOCUMENT_COMMENT);

    NewEmail newEmail = new NewEmail(commenter, participants,
        "collabSpaceDocumentComment.vm", parameters);
    this.emailMessagingService.send(newEmail);

    // fire the event
    DocumentCommentEvent commentEvent = new DocumentCommentEvent(
        sharedDocument.getId(), commenter.getId(), postedComment.getId());
    eventInternalService.fire(commentEvent);

    return retComment;
  }

  @Override
  public void notifyDocumentUpdated(ManagementWorkspaceKey workspaceKey,
      DocumentKey docKey, SocialActorKey updaterKey) throws URISyntaxException {
    /**
     * 1. Find all the collab workspaces where this document is shared 2. Find
     * all the perticipants 3. Notify
     */
    SocialActor updater = actorMarshaller.unmarshallActorKey(updaterKey, true);

    log.info("notifyDocumentUpdated for management workspace " + workspaceKey
        + " for document " + docKey + " by actor " + updaterKey);

    Document document = documentMarshaller.unmarshallDocumentKey(docKey, true);

    /* get all the shared documents */
    List<SharedDocument> sharedDocuments = this.sharedDocumentRepository
        .findAllForDocument(document);

    if (sharedDocuments != null) {
      log.info("Number of workspace to be updated " + sharedDocuments.size());
    }

    for (SharedDocument sharedDoc : sharedDocuments) {
      CollaborationWorkspace workspace = sharedDoc.getWorkspace();

      /* now send email notification to all of them */
      String hostUrl = commonSettings.getPortalApplcationUrl().getValue();
      URI uri = new URIBuilder(hostUrl
          + "collabspace/workspaceDocumentDetail.xhtml").setParameter("sdoc",
          sharedDoc.getId().toString()).build();

      Map<String, Object> parameters = new HashMap<String, Object>();

      log.info("sharedDocumentUrl " + uri.toString());
      parameters.put(EmailParameterConstants.WORKSPACE_NAME, sharedDoc
          .getWorkspace().getName());
      parameters.put(EmailParameterConstants.SHARED_DOCUMENT_URL,
          uri.toString());
      parameters.put(EmailParameterConstants.SHARED_DOCUMENT_NAME, sharedDoc
          .getDocument().getName());

      String logoUrl = commonSettings.getPortalApplcationUrl().getValue()
          + "spring/assets" + workspace.getOwner().getImage();
      parameters.put(EmailParameterConstants.BUSINESS_LOGO, logoUrl);

      List<SocialActor> participants = this.notificationFilter.getParticipants(
          workspace, workspace.getCreator(),
          WallEntryType.COLLABORATION_WORKSPACE_DOCUMENT_UPDATED);

      NewEmail newEmail = new NewEmail(updater, participants,
          "collabSpaceDocumentUpdated.vm", parameters);
      this.emailMessagingService.send(newEmail);

      /* fire the event */
      DocumentUpdatEvent updatedEvent = new DocumentUpdatEvent(
          sharedDoc.getId(), updater.getId());
      eventInternalService.fire(updatedEvent);
    }

  }

  @Override
  @Transactional
  public SharedDocumentDto unshare(SharedDocumentKey sharedDocumentKey,
      SocialActorKey unsharerKey) {

    SocialActor unsharer = this.actorMarshaller.unmarshallActorKey(unsharerKey,
        true);

    SharedDocument sharedDocument = this.collaborationWorkspaceMarshaller
        .unmarshallSharedDocumentKey(sharedDocumentKey, true);

    collaborationWorkspaceEnforcer.unshare(sharedDocument.getWorkspace())
        .checkAllowed();

    sharedDocument.setUnshared(Boolean.TRUE);
    this.sharedDocumentRepository.save(sharedDocument);

    List<SocialActor> participants = this.notificationFilter.getParticipants(
        sharedDocument.getWorkspace(), unsharer,
        WallEntryType.COLLABORATION_WORKSPACE_DOCUMENT_UNSHARED);

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put(EmailParameterConstants.WORKSPACE_NAME, sharedDocument
        .getWorkspace().getName());
    parameters.put(EmailParameterConstants.SHARED_DOCUMENT_NAME, sharedDocument
        .getDocument().getName());
    parameters.put(EmailParameterConstants.WORKSPACE_PARTICIPANT_NAME,
        unsharer.getDisplayName());

    String logoUrl = commonSettings.getPortalApplcationUrl().getValue()
        + "spring/assets" + sharedDocument.getWorkspace().getOwner().getImage();
    parameters.put(EmailParameterConstants.BUSINESS_LOGO, logoUrl);

    NewEmail newEmail = new NewEmail(unsharer, participants,
        "collabSpaceDocumentUnshare.vm", parameters);
    this.emailMessagingService.send(newEmail);

    DocumentUnsharedEvent unsharedEvent = new DocumentUnsharedEvent();
    unsharedEvent.setSharedDocumentId(sharedDocument.getId());
    unsharedEvent.setUnsharer(unsharer.getId());

    eventInternalService.fire(unsharedEvent);
    return this.collaborationWorkspaceMarshaller
        .marshallSharedDocument(sharedDocument);
  }

  @Override
  @Transactional
  public List<SharedDocumentDto> unshare(DocumentKey documentKey,
      SocialActorKey unsharerKey) {

    Document document = documentMarshaller.unmarshallDocumentKey(documentKey,
        true);

    /* get all the shared documents */
    List<SharedDocument> sharedDocuments = this.sharedDocumentRepository
        .findAllForDocument(document);

    List<SharedDocumentDto> retSharedDocument = new ArrayList<SharedDocumentDto>();
    for (SharedDocument sharedDoc : sharedDocuments) {

      try {
        SharedDocumentKey sharedDocKey = this.collaborationWorkspaceMarshaller
            .marshallSharedDocumentKey(sharedDoc);
        SharedDocumentDto sharedDocumentDto = this.unshare(sharedDocKey,
            unsharerKey);
        retSharedDocument.add(sharedDocumentDto);
      } catch (Exception exp) {
        log.error("Cannot unshare from " + sharedDoc.getWorkspace().getName(),
            exp);
      }

    }

    return retSharedDocument;
  }
}
