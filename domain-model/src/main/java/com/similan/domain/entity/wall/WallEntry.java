package com.similan.domain.entity.wall;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.similan.domain.entity.comment.Comment;
import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.common.IDomainEntity;
import com.similan.domain.entity.community.SocialActor;
import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "WallEntry")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING, length = 63)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "wall_id",
    "number" }))
public abstract class WallEntry implements IDomainEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private Wall wall;

  @Column(nullable = false)
  private int number;

  @Enumerated(EnumType.STRING)
  @Column(length = 100, nullable = false, insertable = false, updatable = false)
  private WallEntryType type;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private SocialActor initiator;

  @Column(nullable = false)
  private Date date;

  @OneToOne(optional = true)
  @JoinColumn(nullable = true)
  private Comment lastComment;
  
  @Column
  private Boolean showWall = Boolean.TRUE;
  
  private GenericReference<IWallEntrySubject> subject;
  
  protected WallEntry() {
  }

  public WallEntry(WallEntryType type, int number, Date date) {
    this.type = type;
    this.number = number;
    this.date = date;
    this.showWall = true;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  public Boolean getShowWall() {
    return showWall;
  }

  public void setShowWall(Boolean show) {
    this.showWall = show;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.WALL_ENTRY;
  }

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public WallEntryType getType() {
    return type;
  }

  public Wall getWall() {
    return wall;
  }

  public void setWall(Wall wall) {
    this.wall = wall;
  }

  public Date getDate() {
    return date;
  }

  public SocialActor getInitiator() {
    return initiator;
  }

  public void setInitiator(SocialActor initiator) {
    this.initiator = initiator;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Comment getLastComment() {
    return lastComment;
  }

  public void setLastComment(Comment lastComment) {
    this.lastComment = lastComment;
  }

  public GenericReference<IWallEntrySubject> getSubject() {
	  return subject;
  }

  public void setSubject(GenericReference<IWallEntrySubject> subject) {
	  this.subject = subject;
  }
  

}
