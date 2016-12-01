package com.similan.service.api;

import java.util.List;

import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.news.dto.NewsDto;

public interface NewsService {
  
   public NewsDto create(SocialActorKey writer, NewsDto newsDto);

   public void publish(SocialActorKey publisher, NewsDto newsDto,
      List<SocialActorKey> publishedTo);

   public void publishToPartners(SocialActorKey publisher, NewsDto newsDto);

   public List<NewsDto> get(SocialActorKey org, SocialActorKey requester);
  
   public List<NewsDto> getLatest(SocialActorKey org, SocialActorKey requester);

   public NewsDto getNews(Long id);

   public void newsViewed(Long id, SocialActorKey viewer);

}
