package com.similan.service.internal.impl.event.processor.contentworkspace;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.document.Document;
import com.similan.domain.entity.managementworkspace.ManagementWorkspace;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.contentworkspace.ContentWorkspaceDocumentInNetworkSharedWallEntry;
import com.similan.domain.entity.wall.contentworkspace.ContentWorkspaceWallEntry;
import com.similan.domain.repository.document.DocumentRepository;
import com.similan.domain.repository.share.InNetworkSharedRepository;
import com.similan.domain.share.ISharable;
import com.similan.domain.share.InNetworkShared;
import com.similan.service.internal.api.event.io.contentworkspace.ContentWorkspaceDocumentInNetworkSharedEvent;

@Slf4j
@Component
public class ContentWorkspaceDocumentInNetworkSharedEventProcessor
    extends
    ContentWorkspaceEventProcessor<ContentWorkspaceDocumentInNetworkSharedEvent> {
  @Autowired
  private InNetworkSharedRepository inNetworkSharedRepository;
  @Autowired
  private DocumentRepository documentRepository;

  @Override
  protected ContentWorkspaceWallEntry createWallEntry(
      ContentWorkspaceDocumentInNetworkSharedEvent event) {

    log.info("Event received " + event);

    InNetworkShared shared = this.inNetworkSharedRepository.findOne(event
        .getInNetworkSharedId());
    ManagementWorkspace workspace = this.getManagementWorkspaceRepository()
        .findOne(event.getContentWorkspaceId());
    Wall wall = getWall(workspace);

    GenericReference<ISharable> sharedDocumentRef = shared.getSharedEntity();
    Document sharedDocument = this.documentRepository.findOne(sharedDocumentRef
        .getId());

    ContentWorkspaceDocumentInNetworkSharedWallEntry wallEntry = this
        .getContentWorkspaceWallEntryRepository()
        .createContentWorkspaceDocumentInNetworkSharedWallEntry(wall, shared,
            sharedDocument);

    return wallEntry;
  }

}
