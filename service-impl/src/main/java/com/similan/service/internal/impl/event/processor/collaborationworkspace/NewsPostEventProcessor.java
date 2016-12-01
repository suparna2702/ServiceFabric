package com.similan.service.internal.impl.event.processor.collaborationworkspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.community.activity.News;
import com.similan.domain.entity.wall.Wall;
import com.similan.domain.entity.wall.collaborationworkspace.CollaborationWorkspaceWallEntry;
import com.similan.domain.entity.wall.collaborationworkspace.NewsPostWallEntry;
import com.similan.domain.repository.collaborationworkspace.CollaborationWorkspaceRepository;
import com.similan.domain.repository.community.NewsRepository;
import com.similan.service.internal.api.event.io.collaborationworkspace.NewsPostEvent;

@Component
public class NewsPostEventProcessor extends
    CollaborationWorkspaceEventProcessor<NewsPostEvent> {
  @Autowired
  private NewsRepository newsRepository;
  @Autowired
  private CollaborationWorkspaceRepository collaborationWorkspaceRepository;

  @Override
  protected CollaborationWorkspaceWallEntry createWallEntry(
      NewsPostEvent event) {

    News news = this.newsRepository.findOne(event.getNewsId());
    CollaborationWorkspace workspace = collaborationWorkspaceRepository
        .findOne(event.getWorkspaceId());
    Wall wall = getWall(workspace);

    NewsPostWallEntry entry = this
        .getCollaborationWorkspaceWallEntryRepository().createNewsWallEntry(
            wall, news);
    return entry;
  }

}
