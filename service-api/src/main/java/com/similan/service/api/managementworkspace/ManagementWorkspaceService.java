package com.similan.service.api.managementworkspace;

import java.net.URISyntaxException;
import java.util.List;

import com.similan.service.api.asset.AssetStream;
import com.similan.service.api.asset.NewAssetStream;
import com.similan.service.api.collaborationworkspace.dto.operation.NewInNetworkSharedDocumentDto;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.dto.basic.DocumentDto;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentAndViewerElementsDto;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentDto;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.document.dto.operation.DocumentInfoUpdateDto;
import com.similan.service.api.document.dto.operation.NewDocumentDto;
import com.similan.service.api.document.dto.operation.NewDocumentInstanceDto;
import com.similan.service.api.document.dto.operation.NewExternallyManagedDocumentDto;
import com.similan.service.api.managementworkspace.dto.basic.ManagementWorkspaceDto;
import com.similan.service.api.managementworkspace.dto.basic.ManagementWorkspaceStatisticsDto;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceParticipationKey;
import com.similan.service.api.managementworkspace.dto.operation.NewManagementWorkspaceDto;
import com.similan.service.api.share.InNetworkSharedKey;

public interface ManagementWorkspaceService {

  public DocumentInstanceAndDocumentDto create(
      ManagementWorkspaceKey workspaceKey, DocumentKey documentKey,
      NewDocumentDto newDocumentDto, NewAssetStream assetStream);

  public ManagementWorkspaceDto create(ManagementWorkspaceKey workspaceKey,
      NewManagementWorkspaceDto newWorkspace);

  public ManagementWorkspaceDto get(ManagementWorkspaceKey workspaceKey);

  public void share(DocumentKey sharedDocumentKey,
      NewInNetworkSharedDocumentDto newDocumentShare) throws URISyntaxException;

  public Boolean isDocumentWithSameNameExists(
      ManagementWorkspaceKey workspaceKey, DocumentKey docKey);

  public List<DocumentInstanceAndDocumentDto> getDocuments(
      ManagementWorkspaceKey workspaceKey);

  public List<ActorDto> getParticipants(ManagementWorkspaceKey workspaceKey);

  public ManagementWorkspaceParticipationKey addPerticipant(
      ManagementWorkspaceKey workspaceKey, SocialActorKey pertKey)
      throws URISyntaxException;

  public List<ManagementWorkspaceDto> getForOwner(SocialActorKey ownerKey);

  public List<ManagementWorkspaceDto> getForParticipant(SocialActorKey pertKey);

  public ManagementWorkspaceStatisticsDto getStatistics(
      ManagementWorkspaceKey workspaceKey);

  public DocumentInstanceAndDocumentAndViewerElementsDto getDocument(
      ManagementWorkspaceKey workspaceKey, DocumentKey documentKey,
      SocialActorKey requesterKey);

  public DocumentInstanceAndDocumentAndViewerElementsDto getForViewer(
      InNetworkSharedKey sharedKey);

  public AssetStream download(ManagementWorkspaceKey workspaceKey,
      SocialActorKey downloader, DocumentKey documentKey)
      throws URISyntaxException;

  public DocumentDto upload(ManagementWorkspaceKey workspaceKey,
      NewExternallyManagedDocumentDto newDocumentDto);

  public void upload(ManagementWorkspaceKey workspaceKey,
      SocialActorKey uploader, DocumentKey documentKey)
      throws URISyntaxException;

  public boolean checkOutDocument(ManagementWorkspaceKey workspaceKey,
      SocialActorKey initiator, DocumentKey documentKey)
      throws URISyntaxException;

  public boolean checkInDocument(ManagementWorkspaceKey workspaceKey,
      DocumentKey documentKey, NewAssetStream assetStream,
      NewDocumentInstanceDto newDocumentInstance) throws URISyntaxException;

  public void delete(ManagementWorkspaceKey workspaceKey,
      SocialActorKey initiator, DocumentKey documentKey);

  public DocumentInstanceAndDocumentAndViewerElementsDto updateDocumentInfo(
      ManagementWorkspaceKey workspaceKey, DocumentKey documentKey,
      SocialActorKey initiatorKey, DocumentInfoUpdateDto updateInfo)
      throws URISyntaxException;

}
