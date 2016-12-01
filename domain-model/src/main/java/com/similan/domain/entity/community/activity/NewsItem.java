package com.similan.domain.entity.community.activity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NewsItem implements Serializable {

  private static final long serialVersionUID = 1L;

  private String header;

  @Column(length = 5000)
  private String newsDescription;

  private Date timePosted;

  private String categoryTag;

  private String authorName;

  private Long authorId;

  private Long likeCount;

  private Long dislikeCount;

}
