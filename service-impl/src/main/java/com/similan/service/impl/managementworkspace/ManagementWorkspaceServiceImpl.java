package com.similan.service.impl.managementworkspace;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.collaborationworkspace.SharedDocument;
import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.community.EmployeeRole;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialEmployee;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.document.Document;
import com.similan.domain.entity.document.DocumentInstance;
import com.similan.domain.entity.managementworkspace.ManagementWorkspace;
import com.similan.domain.entity.managementworkspace.ManagementWorkspaceParticipation;
import com.similan.domain.repository.collaborationworkspace.SharedDocumentRepository;
import com.similan.domain.repository.community.SocialEmployeeRepository;
import com.similan.domain.repository.document.DocumentRepository;
import com.similan.domain.repository.managementworkspace.ManagementWorkspaceParticipationRepository;
import com.similan.domain.repository.managementworkspace.ManagementWorkspaceRepository;
import com.similan.domain.repository.share.InNetworkSharedRepository;
import com.similan.domain.repository.wall.WallEntryRepository;
import com.similan.domain.share.ISharable;
import com.similan.domain.share.InNetworkShared;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.service.api.asset.AssetStream;
import com.similan.service.api.asset.NewAssetStream;
import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.collaborationworkspace.CollaborationWorkspaceSharedDocumentService;
import com.similan.service.api.collaborationworkspace.dto.operation.NewInNetworkSharedDocumentDto;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.DocumentInstanceService;
import com.similan.service.api.document.DocumentService;
import com.similan.service.api.document.dto.basic.DocumentDto;
import com.similan.service.api.document.dto.error.DocumentErrorCode;
import com.similan.service.api.document.dto.error.DocumentException;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentAndViewerElementsDto;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentDto;
import com.similan.service.api.document.dto.key.DocumentInstanceKey;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.document.dto.operation.DocumentInfoUpdateDto;
import com.similan.service.api.document.dto.operation.NewDocumentDto;
import com.similan.service.api.document.dto.operation.NewDocumentInstanceDto;
import com.similan.service.api.document.dto.operation.NewExternallyManagedDocumentDto;
import com.similan.service.api.managementworkspace.ManagementWorkspaceService;
import com.similan.service.api.managementworkspace.dto.basic.ManagementWorkspaceDto;
import com.similan.service.api.managementworkspace.dto.basic.ManagementWorkspaceStatisticsDto;
import com.similan.service.api.managementworkspace.dto.error.ContentWorkspaceErrorCode;
import com.similan.service.api.managementworkspace.dto.error.ContentWorkspaceException;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceParticipationKey;
import com.similan.service.api.managementworkspace.dto.operation.NewManagementWorkspaceDto;
import com.similan.service.api.settings.NotificationConfigurationService;
import com.similan.service.api.settings.dto.ContentWorkspaceNotificationConfigurationDto;
import com.similan.service.api.share.InNetworkSharedKey;
import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.impl.base.ServiceImpl;
import com.similan.service.impl.community.SocialActorMarshaller;
import com.similan.service.impl.document.DocumentInstanceMarshaller;
import com.similan.service.impl.document.DocumentMarshaller;
import com.similan.service.internal.api.email.EmailInternalService;
import com.similan.service.internal.api.email.io.NewEmail;
import com.similan.service.internal.api.event.EventInternalService;
import com.similan.service.internal.api.event.io.contentworkspace.ContentWorkspaceDocumentActivityEvent;
import com.similan.service.internal.api.event.io.contentworkspace.ContentWorkspaceDocumentInNetworkSharedEvent;
import com.similan.service.internal.api.event.io.externalshare.InNetworkShareViewEvent;
import com.similan.service.internal.impl.security.ExecutionContext;
import com.similan.service.internal.impl.security.component.ManagementWorkspaceEnforcer;

@Slf4j
@Service
public class ManagementWorkspaceServiceImpl extends ServiceImpl implements
    ManagementWorkspaceService {

  private static int SIZE_MORE_THAN_ONE = 1;
  private static int SIZE_ONE = 1;

  @Autowired
  private ManagementWorkspaceRepository managementWorkspaceRepository;
  @Autowired
  private DocumentRepository documentRepository;
  @Autowired
  private ManagementWorkspaceParticipationRepository participantRepository;
  @Autowired
  private EmailInternalService emailMessagingService;
  @Autowired
  private PlatformCommonSettings platformCommonSettings;
  @Autowired
  private ManagementWorkspaceEnforcer managementWorkspaceEnforcer;
  @Autowired
  private DocumentInstanceService documentInstanceService;
  @Autowired
  private ExecutionContext executionContext;
  @Autowired
  private EventInternalService eventInternalService;
  @Autowired
  private WallEntryRepository wallEntryRepository;
  @Autowired
  private PlatformCommonSettings commonSettings;
  @Autowired
  private NotificationConfigurationService notificationConfigurationService;
  @Autowired
  private CollaborationWorkspaceSharedDocumentService collaborationWorkspaceSharedDocumentService;
  @Autowired
  private InNetworkSharedRepository inNetworkSharedRepository;
  @Autowired
  private SocialEmployeeRepository socialEmployeeRepository;
  @Autowired
  private SharedDocumentRepository sharedDocumentRepository;
  @Autowired
  private DocumentService documentService;
  @Autowired
  private ManagementWorkspaceMarshaller managementWorkspaceMarshaller;
  @Autowired
  private DocumentMarshaller documentMarshaller;
  @Autowired
  private DocumentInstanceMarshaller documentInstanceMarshaller;
  @Autowired
  private SocialActorMarshaller actorMarshaller;

  @Override
  @Transactional
  public void share(DocumentKey sharedDocumentKey,
      NewInNetworkSharedDocumentDto newDocumentShare) throws URISyntaxException {

    log.info("Sharing with in in network contact " + newDocumentShare);

    ManagementWorkspace workspace = managementWorkspaceMarshaller
        .unmarshallWorkspaceKey(newDocumentShare.getWorkspaceKey(), true);

    Document sharedDocument = documentMarshaller.unmarshallDocumentKey(
        sharedDocumentKey, true);

    SocialActor sharer = actorMarshaller.unmarshallActorKey(
        newDocumentShare.getSharer(), true);

    for (SocialActorKey sharedToKey : newDocumentShare.getSharedTo()) {
      SocialActor sharedTo = actorMarshaller.unmarshallActorKey(sharedToKey,
          true);

      if (sharedTo instanceof SocialOrganization) {
        log.info("This is a business so all admins will get the share email");

        List<SocialEmployee> adminList = this
            .getBusinessAdmins((SocialOrganization) sharedTo);

        if (adminList != null && adminList.size() > 0) {
          sharedTo = adminList.get(0).getContactTo();
          log.info("Sharing with business admin " + sharedTo.getDisplayName());
        } else {
          // we dont do any share
          continue;
        }
      }

      InNetworkShared inNetworkShared = this.inNetworkSharedRepository.create(
          (SocialPerson) sharer, (SocialPerson) sharedTo, sharedDocument);
      this.inNetworkSharedRepository.save(inNetworkShared);

      // fire the shared event
      ContentWorkspaceDocumentInNetworkSharedEvent event = new ContentWorkspaceDocumentInNetworkSharedEvent();
      event.setInNetworkSharedId(inNetworkShared.getId());
      event.setContentWorkspaceId(workspace.getId());
      this.eventInternalService.fire(event);

      Map<String, Object> parameters = new HashMap<String, Object>();
      parameters.put("shareFrom", sharer.getDisplayName());
      parameters.put("shareTo", sharedTo.getDisplayName());

      String hostUrl = commonSettings.getPortalApplcationUrl().getValue();
      URI uri = new URIBuilder(hostUrl
          + "docspace/inNetworkSharedDocumentView.xhtml").setParameter("share",
          inNetworkShared.getSharedName()).build();

      log.info("sharedDocumentUrl " + uri.toString());

      parameters.put("sharedDocumentUrl", uri.toString());
      parameters.put("documentName", sharedDocument.getName());

      List<SocialActor> sharedToList = new ArrayList<SocialActor>();
      sharedToList.add(sharedTo);

      NewEmail newEmail = new NewEmail(sharer, sharedToList,
          "managementSpaceDocumentInNetworkShare.vm", parameters);
      this.emailMessagingService.send(newEmail);
    }

  }

  @Override
  @Transactional
  public ManagementWorkspaceDto create(ManagementWorkspaceKey workspaceKey,
      NewManagementWorkspaceDto newWorkspaceDto) {
    SocialActorKey ownerKey = workspaceKey.getOwner();

    SocialOrganization owner = (SocialOrganization) actorMarshaller
        .unmarshallActorKey(ownerKey, true);
    String name = workspaceKey.getName();

    List<ManagementWorkspace> existingManagementSpace = managementWorkspaceRepository
        .findByNameAndOwner(name, owner);
    if (existingManagementSpace != null && existingManagementSpace.size() > 0) {
      throw new ContentWorkspaceException(
          ContentWorkspaceErrorCode.DUPLICATE_WORKSPACE_NAME,
          "Unable to create workspace with name " + name + " since another "
              + "content workspace exists with the for the owner "
              + owner.getDisplayName());
    }

    managementWorkspaceEnforcer.create(owner).checkAllowed();
    ManagementWorkspace workspace = managementWorkspaceRepository.create(owner,
        name);

    SocialActor creator = executionContext.invoker();
    workspace.setCreator(creator);

    workspace.setDescription(newWorkspaceDto.getDescription());
    workspace.setCreationDate(new Date());
    managementWorkspaceRepository.save(workspace);

    ManagementWorkspaceParticipation perticipation = participantRepository
        .create(workspace.getCreator(), workspace);
    participantRepository.save(perticipation);

    ManagementWorkspaceDto workspaceDto = managementWorkspaceMarshaller
        .marshallWorkspace(workspace);
    return workspaceDto;
  }

  @Override
  @Transactional(readOnly = true)
  public ManagementWorkspaceDto get(ManagementWorkspaceKey workspaceKey) {
    ManagementWorkspace workspace = managementWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);
    managementWorkspaceEnforcer.get(workspace).checkAllowed();
    ManagementWorkspaceDto workspaceDto = managementWorkspaceMarshaller
        .marshallWorkspace(workspace);
    return workspaceDto;
  }

  @Override
  @Transactional(readOnly = true)
  public List<DocumentInstanceAndDocumentDto> getDocuments(
      ManagementWorkspaceKey workspaceKey) {
    ManagementWorkspace workspace = managementWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);
    managementWorkspaceEnforcer.getDocuments(workspace).checkAllowed();
    List<Document> documents = managementWorkspaceRepository
        .findDocuments(workspace.getId());

    List<DocumentInstanceAndDocumentDto> documentDtos = documentMarshaller
        .marshallDocumentInstanceAndDocuments(documents);
    return documentDtos;
  }

  @Override
  @Transactional
  public List<ManagementWorkspaceDto> getForOwner(SocialActorKey ownerKey) {
    // TODO: create WS on actor creation and make this method read-only
    SocialOrganization owner = (SocialOrganization) actorMarshaller
        .unmarshallActorKey(ownerKey, true);
    managementWorkspaceEnforcer.getForOwner(owner).checkAllowed();
    List<ManagementWorkspace> workspaces = owner.getManagementWorkspaces();
    if (workspaces.isEmpty()) {
      workspaces = Collections.singletonList(createDefault(owner));
    }
    List<ManagementWorkspaceDto> workspaceDtos = managementWorkspaceMarshaller
        .marshallWorkspaces(workspaces);
    return workspaceDtos;
  }

  private ManagementWorkspace createDefault(SocialActor owner) {
    ManagementWorkspace workspace = managementWorkspaceRepository.create(owner,
        "Default");
    managementWorkspaceRepository.save(workspace);
    owner.setDefaultManagementWorkspace(workspace);

    ManagementWorkspaceParticipation perticipation = participantRepository
        .create(owner, workspace);
    participantRepository.save(perticipation);

    return workspace;
  }

  @Override
  @Transactional
  public Boolean isDocumentWithSameNameExists(
      ManagementWorkspaceKey workspaceKey, DocumentKey docKey) {

    ManagementWorkspace workspace = managementWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);

    try {
      Document docCompared = this.documentMarshaller.unmarshallDocumentKey(
          docKey, true);

      managementWorkspaceEnforcer.get(workspace).checkAllowed();
      List<Document> docList = this.documentRepository.find(workspace,
          docKey.getName());

      if (docList != null) {
        // if there are multiple docs with same name
        // sure duplicate exists
        if (docList.size() > SIZE_MORE_THAN_ONE) {
          return true;
        } else if (docList.size() == SIZE_ONE) {
          // if one doc then check whether
          // it is the same one. If so then it does not count
          Document document = docList.get(0);
          if (document.getId() == docCompared.getId()) {
            return false;
          } else {
            return true;
          }
        }

      }

    } catch (DocumentException exp) {
      if (exp.getCode() == DocumentErrorCode.DOCUMENT_NOT_FOUND) {
        return false;
      }

      // if other type of exception then
      // just re-throw
      throw exp;
    }

    return false;
  }

  @Override
  @Transactional
  public List<ActorDto> getParticipants(ManagementWorkspaceKey workspaceKey) {
    ManagementWorkspace workspace = managementWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);
    managementWorkspaceEnforcer.getParticipants(workspace).checkAllowed();
    List<ManagementWorkspaceParticipation> percipationList = this.participantRepository
        .findByWorkspace(workspace);
    List<ActorDto> pertList = new ArrayList<ActorDto>();
    for (ManagementWorkspaceParticipation actorPerticipation : percipationList) {
      ActorDto actorDto = actorMarshaller.marshallActor(actorPerticipation
          .getParticipant());
      pertList.add(actorDto);

    }
    return pertList;
  }

  @Override
  @Transactional
  public ManagementWorkspaceParticipationKey addPerticipant(
      ManagementWorkspaceKey workspaceKey, SocialActorKey pertKey)
      throws URISyntaxException {
    SocialActor participant = actorMarshaller.unmarshallActorKey(pertKey, true);
    ManagementWorkspace workspace = managementWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);
    managementWorkspaceEnforcer.addParticipant(workspace).checkAllowed();

    // check duplicate
    ManagementWorkspaceParticipation participation = this.participantRepository
        .findByWorkspaceAndParticipant(workspace, participant);
    if (participation != null) {
      throw new ContentWorkspaceException(
          ContentWorkspaceErrorCode.ALREADY_A_PARTICIPANT,
          "Duplicate participant not allowed " + participant.getDisplayName()
              + " is already a member of the content space "
              + workspaceKey.getName());
    }

    // create participation
    ManagementWorkspaceParticipation perticipation = this.participantRepository
        .create(participant, workspace);
    this.participantRepository.save(perticipation);

    // send email
    String hostUrl = commonSettings.getPortalApplcationUrl().getValue();

    URI uri = new URIBuilder(hostUrl + "docspace/contentWorkspaceDetails.xhtml")
        .setParameter("msname", workspace.getName()).build();

    log.info("Document uploaded URL " + uri.toString());

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("workspaceName", workspace.getName());
    parameters.put("perticipantName", participant.getDisplayName());
    parameters.put("inviterName", workspace.getCreator().getDisplayName());
    parameters.put("workspaceUrl", uri.toString());

    List<SocialActor> paerticipantList = fetchParticipants(workspace,
        WallEntryType.CONTENT_WORKSPACE_PARTICIPANT_JOINED);

    NewEmail newEmail = new NewEmail(workspace.getCreator(), paerticipantList,
        "managementSpaceAddPerticipantNotification.vm", parameters);
    this.emailMessagingService.send(newEmail);

    return managementWorkspaceMarshaller
        .marshallWorkspaceParticipationKey(perticipation);

  }

  @Override
  @Transactional
  public List<ManagementWorkspaceDto> getForParticipant(
      SocialActorKey participantKey) {
    SocialPerson participant = (SocialPerson) actorMarshaller
        .unmarshallActorKey(participantKey, true);
    managementWorkspaceEnforcer.getForParticipant(participant).checkAllowed();
    List<ManagementWorkspaceParticipation> perticipationList = this.participantRepository
        .findByParticipant(participant);
    List<ManagementWorkspaceDto> retDto = new ArrayList<ManagementWorkspaceDto>();
    for (ManagementWorkspaceParticipation workspace : perticipationList) {
      ManagementWorkspaceDto workspaceDto = managementWorkspaceMarshaller
          .marshallWorkspace(workspace.getWorkspace());
      retDto.add(workspaceDto);
    }
    return retDto;
  }

  @Override
  @Transactional
  public DocumentInstanceAndDocumentAndViewerElementsDto getDocument(
      ManagementWorkspaceKey workspaceKey, DocumentKey documentKey,
      SocialActorKey requesterKey) {

    SocialPerson participant = (SocialPerson) actorMarshaller
        .unmarshallActorKey(requesterKey, true);

    ManagementWorkspace managementWorkspace = managementWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);

    managementWorkspaceEnforcer.getForParticipant(participant).checkAllowed();

    Document document = documentMarshaller.unmarshallDocumentKey(documentKey,
        true);
    DocumentInstance docInstance = document.getLastInstance();

    DocumentInstanceAndDocumentAndViewerElementsDto documentInstance = this.documentInstanceService
        .getForViewer(documentInstanceMarshaller
            .marshallDocumentInstanceKey(docInstance));

    eventInternalService.fire(new ContentWorkspaceDocumentActivityEvent(
        document.getId(), participant.getId(), managementWorkspace.getId(),
        WallEntryType.CONTENT_WORKSPACE_DOCUMENT_VIEWED));

    return documentInstance;
  }

  @Override
  @Transactional
  public AssetStream download(ManagementWorkspaceKey workspaceKey,
      SocialActorKey downloaderKey, DocumentKey documentKey)
      throws URISyntaxException {

    SocialPerson downloader = (SocialPerson) actorMarshaller
        .unmarshallActorKey(downloaderKey, true);

    ManagementWorkspace managementWorkspace = managementWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);

    managementWorkspaceEnforcer.getForParticipant(downloader).checkAllowed();

    Document document = documentMarshaller.unmarshallDocumentKey(documentKey,
        true);
    DocumentInstanceKey docInstanceKey = documentInstanceMarshaller
        .marshallDocumentInstanceKey(document.getLastInstance());

    AssetStream assetStream = documentInstanceService
        .retrieveOriginal(docInstanceKey);

    eventInternalService.fire(new ContentWorkspaceDocumentActivityEvent(
        document.getId(), downloader.getId(), managementWorkspace.getId(),
        WallEntryType.CONTENT_WORKSPACE_DOCUMENT_DOWNLOADED));

    // send email
    String hostUrl = commonSettings.getPortalApplcationUrl().getValue();
    URI uri = new URIBuilder(hostUrl + "docspace/viewDocumentDetails.xhtml")
        .setParameter("wsowner", managementWorkspace.getOwner().getName())
        .setParameter("msname", managementWorkspace.getName())
        .setParameter("dname", document.getName()).build();

    log.info("Document uploaded URL " + uri.toString());

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("workspaceName", managementWorkspace.getName());
    parameters.put("downloaderName", downloader.getDisplayName());
    parameters.put("documentUrl", uri.toString());
    parameters.put("documentName", documentKey.getName());

    List<SocialActor> participantList = fetchParticipants(managementWorkspace,
        WallEntryType.CONTENT_WORKSPACE_DOCUMENT_DOWNLOADED);

    NewEmail newEmail = new NewEmail(managementWorkspace.getCreator(),
        participantList, "managementSpaceDocumentDownload.vm", parameters);
    this.emailMessagingService.send(newEmail);

    return assetStream;
  }

  @Override
  @Transactional
  public void upload(ManagementWorkspaceKey workspaceKey,
      SocialActorKey uploaderKey, DocumentKey documentKey)
      throws URISyntaxException {

    SocialPerson uploader = (SocialPerson) actorMarshaller.unmarshallActorKey(
        uploaderKey, true);

    ManagementWorkspace managementWorkspace = managementWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);

    Document document = documentMarshaller.unmarshallDocumentKey(documentKey,
        true);

    managementWorkspaceEnforcer.getForParticipant(uploader).checkAllowed();
    eventInternalService.fire(new ContentWorkspaceDocumentActivityEvent(
        document.getId(), uploader.getId(), managementWorkspace.getId(),
        WallEntryType.CONTENT_WORKSPACE_DOCUMENT_UPLOADED));

    String hostUrl = commonSettings.getPortalApplcationUrl().getValue();

    URI uri = new URIBuilder(hostUrl + "docspace/viewDocumentDetails.xhtml")
        .setParameter("wsowner", managementWorkspace.getOwner().getName())
        .setParameter("msname", managementWorkspace.getName())
        .setParameter("dname", document.getName()).build();

    log.info("Document uploaded URL " + uri.toString());

    List<SocialActor> participants = fetchParticipants(managementWorkspace,
        WallEntryType.CONTENT_WORKSPACE_DOCUMENT_UPLOADED);

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("workspaceName", managementWorkspace.getName());
    parameters.put("documentUrl", uri.toString());
    parameters.put("documentName", documentKey.getName());
    String fullName = uploader.getFirstName() + " " + uploader.getLastName();
    parameters.put("uploader", fullName);

    NewEmail newEmail = new NewEmail(uploader, participants,
        "managementSpaceDocumentUpload.vm", parameters);
    this.emailMessagingService.send(newEmail);
  }

  @Override
  @Transactional
  public ManagementWorkspaceStatisticsDto getStatistics(
      ManagementWorkspaceKey workspaceKey) {

    ManagementWorkspace workspace = managementWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);

    Long documentCount = Long.valueOf(String.valueOf(this.documentRepository
        .countByManagementWorkspace(workspace)));

    Long participantCount = participantRepository.participantCount(workspace);

    Long documentViewCount = this.wallEntryRepository.findWallEntryCount(
        EntityType.MANAGEMENT_WORKSPACE,
        WallEntryType.CONTENT_WORKSPACE_DOCUMENT_VIEWED, workspace.getId());

    Long documentDownloadCount = this.wallEntryRepository.findWallEntryCount(
        EntityType.MANAGEMENT_WORKSPACE,
        WallEntryType.CONTENT_WORKSPACE_DOCUMENT_DOWNLOADED, workspace.getId());

    ManagementWorkspaceStatisticsDto stats = new ManagementWorkspaceStatisticsDto(
        documentCount, participantCount, documentViewCount,
        documentDownloadCount);

    return stats;
  }

  @SuppressWarnings("incomplete-switch")
  private List<SocialActor> fetchParticipants(ManagementWorkspace workspace,
      WallEntryType entryType) {

    List<ManagementWorkspaceParticipation> participations = this.participantRepository
        .findByWorkspace(workspace);

    List<SocialActor> participants = new ArrayList<>(participations.size());
    for (ManagementWorkspaceParticipation participation : participations) {

      SocialActor participant = participation.getParticipant();
      if (participant instanceof SocialOrganization) {
        participants.add(participation.getParticipant());
        continue;
      }

      SocialActorKey participantKey = new SocialActorKey(participant.getName());
      SocialActorKey businessKey = new SocialActorKey(workspace.getOwner()
          .getName());

      ContentWorkspaceNotificationConfigurationDto config = this.notificationConfigurationService
          .getContentWorkspaceConfig(participantKey);
      ContentWorkspaceNotificationConfigurationDto businessConfig = this.notificationConfigurationService
          .getContentWorkspaceConfig(businessKey);

      switch (entryType) {
      case CONTENT_WORKSPACE_DOCUMENT_VIEWED: {
        if (businessConfig.getContentSpaceDocumentViewed() != null
            && businessConfig.getContentSpaceDocumentViewed() != false) {
          if (config.getContentSpaceDocumentViewed() != null
              && config.getContentSpaceDocumentViewed() != false) {
            participants.add(participation.getParticipant());
          }
        }

        break;
      }
      case CONTENT_WORKSPACE_DOCUMENT_DOWNLOADED: {
        if (businessConfig.getContentSpaceDocumentDownload() != null
            && businessConfig.getContentSpaceDocumentDownload() != false) {
          if (config.getContentSpaceDocumentDownload() != null
              && config.getContentSpaceDocumentDownload() != false) {
            participants.add(participation.getParticipant());
          }
        }

        break;
      }
      case CONTENT_WORKSPACE_DOCUMENT_COMMENT: {
        if (businessConfig.getContentSpaceDocumentComment() != null
            && businessConfig.getContentSpaceDocumentComment() != false) {
          if (config.getContentSpaceDocumentComment() != null
              && config.getContentSpaceDocumentComment() != false) {
            participants.add(participation.getParticipant());
          }
        }

        break;
      }
      case CONTENT_WORKSPACE_DOCUMENT_UPLOADED: {
        if (businessConfig.getContentSpaceDocumentUpload() != null
            && businessConfig.getContentSpaceDocumentUpload() != false) {
          if (config.getContentSpaceDocumentUpload() != null
              && config.getContentSpaceDocumentUpload() != false) {
            participants.add(participation.getParticipant());
          }
        }

        break;
      }
      case CONTENT_WORKSPACE_DOCUMENT_CHECKIN: {
        if (businessConfig.getContentSpaceDocumentCheckIn() != null
            && businessConfig.getContentSpaceDocumentCheckIn() != false) {
          if (config.getContentSpaceDocumentCheckIn() != null
              && config.getContentSpaceDocumentCheckIn() != false) {
            participants.add(participation.getParticipant());
          }
        }

        break;
      }
      case CONTENT_WORKSPACE_DOCUMENT_CHECKOUT:
        if (businessConfig.getContentSpaceDocumentCheckOut() != null
            && businessConfig.getContentSpaceDocumentCheckOut() != false) {
          if (config.getContentSpaceDocumentCheckOut() != null
              && config.getContentSpaceDocumentCheckOut() != false) {
            participants.add(participation.getParticipant());
          }
        }

        break;

      case CONTENT_WORKSPACE_DOCUMENT_DELETE: {
        participants.add(participation.getParticipant());
        break;
      }

      case CONTENT_WORKSPACE_DOCUMENT_UPDATED: {
        participants.add(participation.getParticipant());
        break;
      }

      }

    }

    return participants;
  }

  @Override
  @Transactional
  public boolean checkOutDocument(ManagementWorkspaceKey workspaceKey,
      SocialActorKey initiatorKey, DocumentKey documentKey)
      throws URISyntaxException {

    SocialPerson initiator = (SocialPerson) actorMarshaller.unmarshallActorKey(
        initiatorKey, true);

    ManagementWorkspace managementWorkspace = managementWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);

    Document document = documentMarshaller.unmarshallDocumentKey(documentKey,
        true);

    eventInternalService.fire(new ContentWorkspaceDocumentActivityEvent(
        document.getId(), initiator.getId(), managementWorkspace.getId(),
        WallEntryType.CONTENT_WORKSPACE_DOCUMENT_CHECKOUT));

    String hostUrl = commonSettings.getPortalApplcationUrl().getValue();

    URI uri = new URIBuilder(hostUrl + "docspace/viewDocumentDetails.xhtml")
        .setParameter("wsowner", managementWorkspace.getOwner().getName())
        .setParameter("msname", managementWorkspace.getName())
        .setParameter("dname", document.getName()).build();

    log.info("Document uploaded URL " + uri.toString());

    List<SocialActor> participants = fetchParticipants(managementWorkspace,
        WallEntryType.CONTENT_WORKSPACE_DOCUMENT_CHECKOUT);

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("workspaceName", managementWorkspace.getName());
    parameters.put("documentUrl", uri.toString());
    parameters.put("documentName", documentKey.getName());
    String fullName = initiator.getFirstName() + " " + initiator.getLastName();
    parameters.put("initiatorName", fullName);

    NewEmail newEmail = new NewEmail(initiator, participants,
        "managementSpaceDocumentCheckOut.vm", parameters);
    this.emailMessagingService.send(newEmail);

    return true;
  }

  @Override
  @Transactional
  public boolean checkInDocument(ManagementWorkspaceKey workspaceKey,
      DocumentKey documentKey, NewAssetStream assetStream,
      NewDocumentInstanceDto newDocumentInstance) throws URISyntaxException {

    SocialActorKey initiatorKey = newDocumentInstance.getUploader();
    this.documentService.checkin(documentKey, newDocumentInstance, assetStream);

    boolean result = documentService.unlock(documentKey, initiatorKey);

    SocialPerson initiator = (SocialPerson) actorMarshaller.unmarshallActorKey(
        initiatorKey, true);

    ManagementWorkspace managementWorkspace = managementWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);

    Document document = documentMarshaller.unmarshallDocumentKey(documentKey,
        true);

    eventInternalService.fire(new ContentWorkspaceDocumentActivityEvent(
        document.getId(), initiator.getId(), managementWorkspace.getId(),
        WallEntryType.CONTENT_WORKSPACE_DOCUMENT_CHECKIN));

    String hostUrl = commonSettings.getPortalApplcationUrl().getValue();

    URI uri = new URIBuilder(hostUrl + "docspace/viewDocumentDetails.xhtml")
        .setParameter("wsowner", managementWorkspace.getOwner().getName())
        .setParameter("msname", managementWorkspace.getName())
        .setParameter("dname", document.getName()).build();

    log.info("Document checked-in URL " + uri.toString());

    List<SocialActor> participants = fetchParticipants(managementWorkspace,
        WallEntryType.CONTENT_WORKSPACE_DOCUMENT_CHECKIN);

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("workspaceName", managementWorkspace.getName());
    parameters.put("documentUrl", uri.toString());
    parameters.put("documentName", documentKey.getName());
    String fullName = initiator.getFirstName() + " " + initiator.getLastName();
    parameters.put("initiatorName", fullName);

    NewEmail newEmail = new NewEmail(initiator, participants,
        "managementSpaceDocumentCheckIn.vm", parameters);
    this.emailMessagingService.send(newEmail);

    /* also notify all the collab spaces where document is shared */
    collaborationWorkspaceSharedDocumentService.notifyDocumentUpdated(
        workspaceKey, documentKey, initiatorKey);

    return result;
  }

  @Override
  @Transactional
  public DocumentInstanceAndDocumentAndViewerElementsDto getForViewer(
      InNetworkSharedKey sharedKey) {

    InNetworkShared shared = this.inNetworkSharedRepository
        .findBySharedName(sharedKey.getName());

    GenericReference<ISharable> sharedEntity = shared.getSharedEntity();

    Document sharedDocument = this.documentRepository.findOne(sharedEntity
        .getId());
    DocumentInstanceAndDocumentAndViewerElementsDto docInstanceViewerElement = this.documentInstanceMarshaller
        .marshallDocumentInstanceAndDocumentAndViewerElements(sharedDocument
            .getLastInstance());

    // for feed distribution
    eventInternalService.fire(new InNetworkShareViewEvent(shared.getId()));

    // send email notification as well
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("workspaceName", sharedDocument.getWorkspace().getName());
    parameters.put("documentName", sharedDocument.getName());
    parameters.put("viewerName", shared.getSharedToActor().getDisplayName());

    List<SocialActor> recepients = new ArrayList<SocialActor>();
    recepients.add(shared.getSharedBy());

    NewEmail newEmail = new NewEmail(shared.getSharedBy(), recepients,
        "managementSpaceDocumentSharedView.vm", parameters);
    this.emailMessagingService.send(newEmail);

    return docInstanceViewerElement;

  }

  public List<SocialEmployee> getBusinessAdmins(SocialOrganization business) {

    Set<EmployeeRole> roles = new HashSet<EmployeeRole>();
    roles.add(EmployeeRole.BUSINESS_ADMIN);
    List<SocialEmployee> adminList = socialEmployeeRepository
        .findByContactFromAndRolesIn(business, roles);
    return adminList;
  }

  @Override
  @Transactional
  public void delete(ManagementWorkspaceKey workspaceKey,
      SocialActorKey initiatorKey, DocumentKey documentKey) {

    SocialPerson initiator = (SocialPerson) actorMarshaller.unmarshallActorKey(
        initiatorKey, true);

    Document document = documentMarshaller.unmarshallDocumentKey(documentKey,
        true);

    if (document.getLockOwner() != null) {
      throw new DocumentException(DocumentErrorCode.DOCUMENT_LOCKED,
          "Cannot delete document " + documentKey.getName()
              + " it is locked by " + document.getLockOwner().getDisplayName());
    }

    ManagementWorkspace managementWorkspace = managementWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);

    List<SharedDocument> sharedList = this.sharedDocumentRepository
        .findAllForDocument(document);

    if (sharedList != null && sharedList.size() > 0) {

      // if the document shared in collab space has been unshared from all
      // we allow delete
      for (SharedDocument sharedDoc : sharedList) {
        if (sharedDoc.getUnshared() != true) {
          throw new ContentWorkspaceException(
              ContentWorkspaceErrorCode.DOCUMENT_IS_SHARED,
              "Cannot delete document " + documentKey.getName()
                  + " it is shared with collaboration workspaces");
        }
      }

    }

    GenericReference<ISharable> sharedEntity = new GenericReference<ISharable>(
        EntityType.DOCUMENT, document.getId());
    if (this.inNetworkSharedRepository.findBySharedEntity(sharedEntity) != null) {
      throw new ContentWorkspaceException(
          ContentWorkspaceErrorCode.DOCUMENT_IS_SHARED_EXTERNALLY,
          "Cannot delete document " + documentKey.getName()
              + " it is shared externaly");
    }

    log.info("Deleting document " + documentKey);
    this.documentService.delete(documentKey);

    List<SocialActor> participants = fetchParticipants(managementWorkspace,
        WallEntryType.CONTENT_WORKSPACE_DOCUMENT_CHECKIN);

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("workspaceName", managementWorkspace.getName());
    parameters.put("documentName", documentKey.getName());
    String fullName = initiator.getFirstName() + " " + initiator.getLastName();
    parameters.put("initiatorName", fullName);

    NewEmail newEmail = new NewEmail(initiator, participants,
        "managementSpaceDocumentDelete.vm", parameters);
    this.emailMessagingService.send(newEmail);

  }

  @Override
  @Transactional
  public DocumentInstanceAndDocumentDto create(
      ManagementWorkspaceKey workspaceKey, DocumentKey documentKey,
      NewDocumentDto newDocumentDto, NewAssetStream assetStream) {

    if (this.isDocumentWithSameNameExists(documentKey.getWorkspace(),
        documentKey)) {
      throw new ContentWorkspaceException(
          ContentWorkspaceErrorCode.DUPLICATE_DOCUMENT_NAME,
          "Document with name " + documentKey.getName()
              + " exists in workspace");
    }

    return this.documentService
        .create(documentKey, newDocumentDto, assetStream);
  }

  @Override
  @Transactional
  public DocumentDto upload(ManagementWorkspaceKey workspaceKey,
      NewExternallyManagedDocumentDto newDocumentDto) {

    DocumentKey documentKey = new DocumentKey(workspaceKey,
        newDocumentDto.getExplicitFilename());

    if (this.isDocumentWithSameNameExists(documentKey.getWorkspace(),
        documentKey)) {
      throw new ContentWorkspaceException(
          ContentWorkspaceErrorCode.DUPLICATE_DOCUMENT_NAME,
          "Document with name " + documentKey.getName()
              + " exists in workspace");
    }

    return this.documentService.create(documentKey, newDocumentDto);

  }

  @SuppressWarnings("deprecation")
  @Override
  @Transactional
  public DocumentInstanceAndDocumentAndViewerElementsDto updateDocumentInfo(
      ManagementWorkspaceKey workspaceKey, DocumentKey documentKey,
      SocialActorKey initiatorKey, DocumentInfoUpdateDto updateInfo)
      throws URISyntaxException {

    if (this.isDocumentWithSameNameExists(documentKey.getWorkspace(),
        new DocumentKey(documentKey.getWorkspace(), updateInfo.getName()))) {
      throw new ContentWorkspaceException(
          ContentWorkspaceErrorCode.DUPLICATE_DOCUMENT_NAME,
          "Document with name " + documentKey.getName()
              + " exists in workspace");
    }

    SocialPerson initiator = (SocialPerson) actorMarshaller.unmarshallActorKey(
        initiatorKey, true);

    ManagementWorkspace managementWorkspace = managementWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);

    DocumentInstanceAndDocumentAndViewerElementsDto updatedDoc = this.documentService
        .updateDocumentInfo(documentKey, updateInfo);

    // for feed distribution
    ContentWorkspaceDocumentActivityEvent event = new ContentWorkspaceDocumentActivityEvent(
        updatedDoc.getDocument().getKey().getId(), initiator.getId(),
        managementWorkspace.getId(),
        WallEntryType.CONTENT_WORKSPACE_DOCUMENT_UPDATED);

    eventInternalService.fire(event);

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("workspaceName", workspaceKey.getName());
    parameters.put("prevDocumentName", documentKey.getName());
    parameters.put("newDocumentName", updateInfo.getName());

    String fullName = initiator.getFirstName() + " " + initiator.getLastName();
    parameters.put("initiatorName", fullName);

    String hostUrl = commonSettings.getPortalApplcationUrl().getValue();
    URI uri = new URIBuilder(hostUrl + "docspace/viewDocumentDetails.xhtml")
        .setParameter("wsowner", workspaceKey.getOwner().getName())
        .setParameter("msname", workspaceKey.getName())
        .setParameter("dname", updatedDoc.getDocument().getKey().getName())
        .build();
    parameters.put("documentUrl", uri.toString());

    List<SocialActor> participants = fetchParticipants(managementWorkspace,
        WallEntryType.CONTENT_WORKSPACE_DOCUMENT_UPDATED);

    NewEmail newEmail = new NewEmail(initiator, participants,
        "managementSpaceDocumentInfoUpdate.vm", parameters);
    this.emailMessagingService.send(newEmail);

    return updatedDoc;
  }
}
