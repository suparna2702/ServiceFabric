package com.similan.domain.entity.comment;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;

import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.common.IDomainEntity;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.wall.IWallEntrySubject;
import com.similan.service.api.base.dto.key.EntityType;

@Entity(name = "Comment")
public class Comment implements IDomainEntity, IWallEntrySubject {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  private GenericReference<ICommentable> commentable;

  @Column(nullable = false)
  private int number;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false, name = "author_id")
  private SocialActor author;

  @Column(nullable = false)
  private Date date;

  @OneToOne(optional = true)
  @JoinColumn(nullable = true)
  private CommentReply lastReply;

  @OneToMany(mappedBy = "comment")
  @OrderColumn(name = "number")
  private List<CommentReply> replies;

  @Column(nullable = false, length = 1024)
  private String content;

  protected Comment() {
  }

  public Comment(int number, Date date, String content) {
    this.number = number;
    this.date = date;
    this.content = content;
  }
  
  @Override
  public EntityType getEntityType() {
    return EntityType.COMMENT;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setCommentable(GenericReference<ICommentable> commentable) {
    this.commentable = commentable;
  }

  public GenericReference<ICommentable> getCommentable() {
    return commentable;
  }

  public int getNumber() {
    return number;
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

  public void setLastReply(CommentReply lastReply) {
    this.lastReply = lastReply;
  }

  public CommentReply getLastReply() {
    return lastReply;
  }

  public List<CommentReply> getReplies() {
    return replies;
  }

  public String getContent() {
    return content;
  }

}
