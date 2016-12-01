package com.similan.domain.entity.wall.wall;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.similan.service.api.wall.dto.basic.LinkReferenceType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LinkReference {
  public static final int MAX_CONTENT = 5000;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;

  @Column
  private String url;

  @Column
  private String title;

  @Column
  private String imageUrl;

  @Column(length = MAX_CONTENT, columnDefinition = "TEXT")
  private String content;

  @Enumerated(EnumType.STRING)
  @Column
  private LinkReferenceType linkReferenceType = LinkReferenceType.WEBPAGE_ARTICLE;
}
