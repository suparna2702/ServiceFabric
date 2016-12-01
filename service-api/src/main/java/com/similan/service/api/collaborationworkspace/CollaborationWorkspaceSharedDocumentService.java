package com.similan.service.api.collaborationworkspace;

import java.net.URISyntaxException;
import java.util.List;

import com.similan.service.api.asset.AssetStream;
import com.similan.service.api.collaborationworkspace.dto.basic.SharedDocumentAndStatisticsDto;
import com.similan.service.api.collaborationworkspace.dto.basic.SharedDocumentDto;
import com.similan.service.api.collaborationworkspace.dto.extended.SharedDocumentAndDocumentInstanceAndDocumentAndViewerElementsDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.collaborationworkspace.dto.key.SharedDocumentKey;
import com.similan.service.api.collaborationworkspace.dto.operation.NewDocumentSharedWithOtherWorkspaceDto;
import com.similan.service.api.collaborationworkspace.dto.operation.NewExternalSharedDocumentDto;
import com.similan.service.api.collaborationworkspace.dto.operation.NewSharedDocumentCommentDto;
import com.similan.service.api.collaborationworkspace.dto.operation.NewSharedDocumentDto;
import com.similan.service.api.comment.dto.basic.CommentDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentAndViewerElementsDto;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.managementworkspace.dto.key.ManagementWorkspaceKey;
import com.similan.service.api.share.ExternalSharedKey;

public interface CollaborationWorkspaceSharedDocumentService {

  public SharedDocumentAndDocumentInstanceAndDocumentAndViewerElementsDto getForViewer(
      Long id, SocialActorKey viewerKey) throws URISyntaxException;

  void notifyDocumentUpdated(ManagementWorkspaceKey workspaceKey,
      DocumentKey docKey, SocialActorKey updaterKey) throws URISyntaxException;

  SharedDocumentDto get(SharedDocumentKey sharedDocumentKey);

  SharedDocumentAndDocumentInstanceAndDocumentAndViewerElementsDto getForViewer(
      SharedDocumentKey sharedDocumentKey, SocialActorKey viewerKey)
      throws URISyntaxException;

  AssetStream download(SocialActorKey downloader,
      SharedDocumentKey sharedDocumentKey);

  List<SharedDocumentDto> getByWorkspace(CollaborationWorkspaceKey workspaceKey);

  List<SharedDocumentAndStatisticsDto> getSharedDocumentAndStatistics(
      CollaborationWorkspaceKey workspaceKey, SocialActorKey viewerKey);

  SharedDocumentAndStatisticsDto getSharedDocumentAndStatistics(
      SharedDocumentKey docKey);

  public DocumentInstanceAndDocumentAndViewerElementsDto getForViewer(
      ExternalSharedKey sharedKey);

  void share(SharedDocumentKey sharedDocumentKey,
      NewExternalSharedDocumentDto newDocumentShare) throws URISyntaxException;

  SharedDocumentDto share(SharedDocumentKey sharedDocumentKey,
      NewSharedDocumentDto newSharedDocumentDto) throws URISyntaxException;

  SharedDocumentDto unshare(SharedDocumentKey sharedDocumentKey,
      SocialActorKey unsharerKey);

  List<SharedDocumentDto> unshare(DocumentKey sharedDocumentKey,
      SocialActorKey unsharerKey);

  SharedDocumentDto share(
      SharedDocumentKey sharedDocumentKey,
      NewDocumentSharedWithOtherWorkspaceDto newDocumentSharedWithOtherWorkspaceDto)
      throws URISyntaxException;

  CommentDto<SharedDocumentKey> post(NewSharedDocumentCommentDto comment)
      throws URISyntaxException;

}
