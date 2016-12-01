package com.similan.service.api.news.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NewsItemDto implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private String header;

  private String newsDescription;

  private Date timePosted;

  private String categoryTag;

  private String authorName;

  private Long authorId;
}
