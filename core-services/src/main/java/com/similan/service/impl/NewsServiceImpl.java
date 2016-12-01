package com.similan.service.impl;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.community.activity.News;
import com.similan.domain.entity.community.activity.NewsItem;
import com.similan.domain.entity.wall.wall.LinkReference;
import com.similan.domain.repository.collaborationworkspace.CollaborationWorkspaceRepository;
import com.similan.domain.repository.community.NewsRepository;
import com.similan.domain.repository.community.SocialOrganizationRepository;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.domain.repository.wall.LinkReferenceRepository;
import com.similan.service.api.NewsService;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.news.dto.NewsDto;
import com.similan.service.api.wall.dto.basic.LinkReferenceDto;
import com.similan.service.api.wall.dto.basic.LinkReferenceType;
import com.similan.service.exception.CoreServiceException;
import com.similan.service.impl.wall.entrymarshaller.link.LinkReferenceMarshaller;
import com.similan.service.impl.wall.entrymarshaller.news.NewsMarshaller;
import com.similan.service.internal.api.event.EventInternalService;
import com.similan.service.internal.api.event.io.collaborationworkspace.NewsPostEvent;
import com.similan.service.internal.api.linkreference.LinkReferenceInternalService;

@Slf4j
public class NewsServiceImpl implements NewsService {
  private final static int LATEST_COUNT = 5;

  @Autowired
  private NewsRepository newsRepository;

  @Autowired
  private SocialOrganizationRepository organizationRepository;

  @Autowired
  private SocialPersonRepository personRepository;

  @Autowired
  private LinkReferenceInternalService articleService;

  @Autowired
  private LinkReferenceRepository linkReferenceRepository;

  @Autowired
  private LinkReferenceMarshaller linkReferenceMarshaller;

  @Autowired
  private CollaborationWorkspaceRepository collaborationWorkspaceRepository;

  @Autowired
  private EventInternalService eventInternalService;

  @Autowired
  private NewsMarshaller newsMarshaller;

  @Override
  @Transactional
  public NewsDto create(SocialActorKey writerKey, NewsDto newsDto) {

    SocialOrganization owner = this.organizationRepository.findOne(newsDto
        .getOwnerId());
    SocialPerson writer = this.personRepository.findOne(writerKey.getId());

    News newsFeed = this.newsRepository.create();

    NewsItem newsItem = new NewsItem();
    newsItem.setAuthorId(newsDto.getNewsItem().getAuthorId());
    newsItem.setAuthorName(newsDto.getNewsItem().getAuthorName());
    newsItem.setCategoryTag(newsDto.getNewsItem().getCategoryTag());
    newsItem.setHeader(newsDto.getNewsItem().getHeader());
    newsItem.setNewsDescription(newsDto.getNewsItem().getNewsDescription());
    newsItem.setTimePosted(newsDto.getNewsItem().getTimePosted());

    newsFeed.setNewsItem(newsItem);
    newsFeed.setOwner(owner);
    newsFeed.setCreator(writer);

    LinkReference linkReference = this.extractLinkreference(newsDto
        .getNewsItem().getNewsDescription());

    if (linkReference != null) {
      newsFeed.setLink(linkReference);
      LinkReferenceDto linkReferenceDto = this.linkReferenceMarshaller
          .marshall(linkReference);
      newsDto.setLinkReference(linkReferenceDto);
      newsDto.getNewsItem().setHeader(linkReferenceDto.getTitle());
    }

    this.newsRepository.save(newsFeed);
    newsDto.setId(newsFeed.getId());
    return newsDto;

  }

  private LinkReference extractLinkreference(String content) {
    String url = articleService.extractUrl(content);
    if (url == null) {
      return null;
    }
    try {
      LinkReferenceDto linkDto = articleService.readLinkReference(url);
      LinkReference link = LinkReference
          .builder()
          .url(linkDto.getUrl())
          .title(linkDto.getTitle())
          .linkReferenceType(linkDto.getLinkReferenceType())
          .content(
              StringUtils.abbreviate(linkDto.getContent(),
                  LinkReference.MAX_CONTENT)).imageUrl(linkDto.getImageUrl())
          .build();
      linkReferenceRepository.save(link);
      return link;
    } catch (Exception e) {
      log.error("Error getting link reference for " + url, e);
      return null;
    }
  }

  @Override
  @Transactional
  public List<NewsDto> get(SocialActorKey org, SocialActorKey requester) {
    List<NewsDto> newsDtoList = new ArrayList<NewsDto>();

    SocialOrganization owner = this.organizationRepository.findOne(org.getId());
    List<News> newsList = this.newsRepository.findAllByOwner(owner);

    if (newsList != null) {
      log.info("Number of news item retrieved " + newsList.size());

      for (News news : newsList) {

        NewsDto newsDto = this.newsMarshaller.marshallNews(news);
        newsDtoList.add(newsDto);
      }

    }

    return newsDtoList;
  }

  @Override
  @Transactional
  public NewsDto getNews(Long newsId) {

    News news = this.newsRepository.findOne(newsId);
    return this.newsMarshaller.marshallNews(news);
  }

  @Override
  @Transactional
  public void newsViewed(Long id, SocialActorKey viewer) {
    log.info("News item viewed " + id + " by " + viewer.getName());

  }

  @Override
  @Transactional
  public void publish(SocialActorKey publisher, NewsDto newsDto,
      List<SocialActorKey> publishedTo) {
    log.info("News item published " + newsDto.getId() + " by "
        + publisher.getName());

  }

  @Override
  @Transactional
  public void publishToPartners(SocialActorKey publisher, NewsDto newsDto) {
    log.info("News item published to partners " + newsDto.getId() + " by "
        + publisher.getName());

    News newsToPublish = this.newsRepository.findOne(newsDto.getId());
    if (newsToPublish == null) {
      throw new CoreServiceException("Cannot publish news" + newsDto.getId()
          + " since it does not exist ");
    }

    SocialOrganization owner = newsToPublish.getOwner();
    List<CollaborationWorkspace> partnerWorkspaceList = this.collaborationWorkspaceRepository
        .findByOwnerAndPartnerWorkspace(owner, Boolean.TRUE);

    for (CollaborationWorkspace workspace : partnerWorkspaceList) {

      NewsPostEvent event = new NewsPostEvent();
      event.setNewsId(newsToPublish.getId());
      event.setWorkspaceId(workspace.getId());
      log.info("Sending news event " + event);

      this.eventInternalService.fire(event);
    }

  }

  @Override
  @Transactional
  public List<NewsDto> getLatest(SocialActorKey org, SocialActorKey requester) {

    List<NewsDto> newsDtoList = new ArrayList<NewsDto>();
    SocialOrganization owner = this.organizationRepository.findOne(org.getId());
    List<News> newsList = this.newsRepository.findLatestByOwner(owner,
        new PageRequest(0, LATEST_COUNT));

    if (newsList != null) {
      log.info("Number of news item retrieved " + newsList.size());

      for (News news : newsList) {

        NewsDto newsDto = this.newsMarshaller.marshallNews(news);
        newsDtoList.add(newsDto);
      }

    }

    return newsDtoList;
  }

}
