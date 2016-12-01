package com.similan.domain.entity.community.activity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.wall.wall.LinkReference;

@Entity(name = "News")
@Getter
@Setter
@ToString
public class News {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;

  @Embedded
  private NewsItem newsItem;

  @ManyToOne
  @JoinColumn
  private SocialOrganization owner;

  @ManyToOne
  @JoinColumn
  private SocialPerson creator;
  
  @OneToOne
  @JoinColumn
  private LinkReference link;

}
