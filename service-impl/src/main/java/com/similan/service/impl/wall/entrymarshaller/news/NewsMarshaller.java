package com.similan.service.impl.wall.entrymarshaller.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.activity.News;
import com.similan.domain.entity.community.activity.NewsItem;
import com.similan.domain.entity.wall.wall.LinkReference;
import com.similan.service.api.news.dto.NewsDto;
import com.similan.service.api.news.dto.NewsItemDto;
import com.similan.service.api.wall.dto.basic.LinkReferenceDto;
import com.similan.service.impl.Marshaller;
import com.similan.service.impl.wall.entrymarshaller.link.LinkReferenceMarshaller;

@Component
public class NewsMarshaller extends Marshaller {
  @Autowired
  private LinkReferenceMarshaller linkReferenceMarshaller;

  public NewsDto marshallNews(News news) {

    NewsDto newsDto = new NewsDto();
    newsDto.setNewsItem(this.marshallNewsItem(news.getNewsItem()));
    newsDto.setId(news.getId());
    newsDto.setOwnerId(news.getOwner().getId());

    LinkReference linkReference = news.getLink();
    if (linkReference != null) {
      LinkReferenceDto linkReferenceDto = this.linkReferenceMarshaller
          .marshall(linkReference);
      newsDto.setLinkReference(linkReferenceDto);
    }

    return newsDto;
  }

  private NewsItemDto marshallNewsItem(NewsItem newsItem) {

    NewsItemDto retNewsItem = new NewsItemDto();
    retNewsItem.setAuthorId(newsItem.getAuthorId());
    retNewsItem.setAuthorName(newsItem.getAuthorName());
    retNewsItem.setCategoryTag(newsItem.getCategoryTag());
    retNewsItem.setHeader(newsItem.getHeader());
    retNewsItem.setNewsDescription(newsItem.getNewsDescription());
    retNewsItem.setTimePosted(newsItem.getTimePosted());

    return retNewsItem;
  }

}
