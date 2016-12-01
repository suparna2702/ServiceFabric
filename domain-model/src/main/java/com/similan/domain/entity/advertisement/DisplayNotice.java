package com.similan.domain.entity.advertisement;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

import com.similan.domain.entity.comment.Comment;
import com.similan.domain.entity.comment.ICommentable;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.wall.IWallEntrySubject;
import com.similan.service.api.base.dto.key.EntityType;

@Getter
@Setter
@Entity(name = "DisplayNotice")
public class DisplayNotice implements ICommentable, IWallEntrySubject {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String uuid = UUID.randomUUID().toString();

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private SocialOrganization owner;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private SocialPerson creator;

  @Column
  private String iconAsset;

  @Column
  private Date createDate;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private DisplayNoticeLandingPage landingPage;

  @Column
  private Boolean active = Boolean.FALSE;

  @OneToOne(optional = true)
  @JoinColumn(nullable = true)
  private Comment lastComment;

  @Override
  public EntityType getEntityType() {
    return EntityType.DISPLAY_NOTICE;
  }

  @Override
  public void setLastComment(Comment lastComment) {
    this.lastComment = lastComment;
  }

  @Override
  public Comment getLastComment() {
    return this.lastComment;
  }

}
