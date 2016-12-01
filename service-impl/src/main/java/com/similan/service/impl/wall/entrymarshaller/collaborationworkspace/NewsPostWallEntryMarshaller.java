package com.similan.service.impl.wall.entrymarshaller.collaborationworkspace;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.activity.News;
import com.similan.domain.entity.wall.collaborationworkspace.NewsPostWallEntry;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.news.dto.NewsDto;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.basic.collaborationworkspace.NewsPostWallEntryDto;
import com.similan.service.api.wall.dto.key.WallEntryKey;
import com.similan.service.impl.community.SocialActorMarshaller;
import com.similan.service.impl.wall.WallEntryMarshaller;
import com.similan.service.impl.wall.entrymarshaller.news.NewsMarshaller;

@Component
public class NewsPostWallEntryMarshaller extends
    WallEntryMarshaller<NewsPostWallEntry, CollaborationWorkspaceKey> {
  @Autowired
  private NewsMarshaller newsMarshaller;
  @Autowired
  private SocialActorMarshaller actorMarshaller;

  @Override
  public WallEntryDto<CollaborationWorkspaceKey> marshall(
      WallEntryKey<CollaborationWorkspaceKey> entryKey,
      ActorDto initiatorDto, Date date, NewsPostWallEntry entry) {

    News news = entry.getNews();
    NewsDto newsDto = newsMarshaller.marshallNews(news);
    ActorDto sharer = actorMarshaller.marshallActor(news.getCreator());

    NewsPostWallEntryDto retEntry = new NewsPostWallEntryDto(newsDto, sharer,
        entryKey, entry.getDate());

    return retEntry;
  }
}
