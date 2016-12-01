package com.similan.domain.entity.comment;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.similan.domain.entity.common.IDomainEntity;
import com.similan.domain.entity.community.SocialActor;
import com.similan.service.api.base.dto.key.EntityType;

@Entity(name = "CommentReply")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "comment_id",
    "number" }) })
public class CommentReply implements IDomainEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  @Column(nullable = false)
  private int number;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false, name = "author_id")
  private SocialActor author;

  @Column(nullable = false)
  private Date date;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private Comment comment;

  @Column(nullable = false, length = 1024)
  private String content;

  protected CommentReply() {
  }

  public CommentReply(int number, Date date, String content) {
    this.number = number;
    this.date = date;
    this.content = content;
  }
  
  @Override
  public EntityType getEntityType() {
    return EntityType.COMMENT_REPLY;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getNumber() {
    return number;
  }

  public Comment getComment() {
    return comment;
  }

  public void setComment(Comment comment) {
    this.comment = comment;
  }

  public SocialActor getAuthor() {
    return author;
  }

  public void setAuthor(SocialActor author) {
    this.author = author;
  }

  public Date getDate() {
    return date;
  }

  public String getContent() {
    return content;
  }

}
