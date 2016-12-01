package com.similan.domain.entity.wall.collaborationworkspace;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.similan.domain.entity.community.activity.News;
import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "NewsPostWallEntry")
@DiscriminatorValue("NEWS_POST_WALL_ENTRY")
public class NewsPostWallEntry extends CollaborationWorkspaceWallEntry {

  @ManyToOne(optional = true)
  @JoinColumn(nullable = true)
  private News news;

  protected NewsPostWallEntry() {

  }

  public NewsPostWallEntry(int number, Date date, News news) {
    super(WallEntryType.NEWS_POST_WALL_ENTRY, number, date);
    this.setShowWall(Boolean.TRUE);
    this.news = news;
  }

  public News getNews() {
    return news;
  }

  public void setNews(News news) {
    this.news = news;
  }

}
