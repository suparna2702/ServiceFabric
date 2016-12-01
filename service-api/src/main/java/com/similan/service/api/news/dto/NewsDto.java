package com.similan.service.api.news.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.similan.service.api.wall.dto.basic.LinkReferenceDto;

@Getter
@Setter
@ToString
public class NewsDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  private NewsItemDto newsItem;

  private Long ownerId;

  private LinkReferenceDto linkReference;

}
