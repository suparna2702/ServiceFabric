package com.similan.portal.view.advertisement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;
import com.similan.service.api.news.dto.NewsDto;
import com.similan.service.api.news.dto.NewsItemDto;

@Scope("view")
@Component("newsFeedView")
@Slf4j
public class NewsFeedView extends BaseView {

  private static final long serialVersionUID = 1L;

  @Autowired
  private OrganizationDetailInfoDto orgInfo = null;

  @Autowired(required = false)
  private MemberInfoDto memberInfo = null;

  private NewsItemDto newsItem;

  private List<NewsDto> newsList;

  @PostConstruct
  public void init() {

    newsItem = new NewsItemDto();
    if (orgInfo != null) {
      newsList = this.getNewsService().get(this.getOrgKey(orgInfo),
          this.getMemberKey(memberInfo));
    }

    if (newsList == null) {
      newsList = new ArrayList<NewsDto>();
    }

  }

  public List<NewsDto> getNewsList() {
    return newsList;
  }

  public void setNewsList(List<NewsDto> newsList) {
    this.newsList = newsList;
  }

  public NewsItemDto getNewsItem() {
    return newsItem;
  }

  public void setNewsItem(NewsItemDto newsItem) {
    this.newsItem = newsItem;
  }

  public void postNews() {
    log.info("News feed view " + this.newsItem.toString());

    if (StringUtils.isEmpty(this.newsItem.getHeader())) {
      this.newsItem.setHeader("News");
    }

    if (StringUtils.isEmpty(this.newsItem.getNewsDescription())) {
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure",
              "Cannot save a news item with empty content"));
      return;
    }

    String desc = this.newsItem.getNewsDescription();
    desc = desc.replaceAll(
        "<script\\b[^<]*(?:(?!<\\/script>)<[^<]*)*<\\/script>", "");
    this.newsItem.setNewsDescription(desc);

    this.newsItem.setTimePosted(new Date());
    this.newsItem.setAuthorId(memberInfo.getId());
    this.newsItem.setAuthorName(memberInfo.getFullName());

    NewsDto newsDto = new NewsDto();
    newsDto.setNewsItem(this.newsItem);
    newsDto.setOwnerId(orgInfo.getId());

    this.getNewsService().create(this.getMemberKey(memberInfo), newsDto);
    newsList.add(newsDto);
    newsItem = new NewsItemDto();

  }

}
