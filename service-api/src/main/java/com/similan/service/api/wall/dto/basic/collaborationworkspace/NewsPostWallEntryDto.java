package com.similan.service.api.wall.dto.basic.collaborationworkspace;

import java.util.Date;

import lombok.ToString;

import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.news.dto.NewsDto;
import com.similan.service.api.wall.dto.basic.WallEntryType;
import com.similan.service.api.wall.dto.key.WallEntryKey;

@ToString
public class NewsPostWallEntryDto extends CollaborationWorkspaceWallEntryDto {

  private NewsDto news;

  public NewsPostWallEntryDto(NewsDto news, ActorDto sharer,
      WallEntryKey<CollaborationWorkspaceKey> key, Date date) {
    super(key, sharer, date);
    this.news = news;
  }

  @Override
  public WallEntryType getType() {
    return WallEntryType.NEWS_POST_WALL_ENTRY;
  }

  public NewsDto getNews() {
    return news;
  }

}
