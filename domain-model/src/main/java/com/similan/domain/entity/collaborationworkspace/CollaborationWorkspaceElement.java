package com.similan.domain.entity.collaborationworkspace;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

import com.similan.domain.common.DomainBaseEntity;
import com.similan.domain.entity.comment.Comment;
import com.similan.domain.entity.comment.ICommentable;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.poll.IPollSubject;
import com.similan.domain.entity.wall.IWallEntrySubject;

@MappedSuperclass
@Getter
@Setter
public abstract class CollaborationWorkspaceElement extends DomainBaseEntity
    implements ICommentable, IWallEntrySubject, IPollSubject {

  @Column(nullable = false)
  private Date creationDate;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private CollaborationWorkspace workspace;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private SocialActor creator;

  @OneToOne(optional = true)
  @JoinColumn(nullable = true)
  private Comment lastComment;

  public CollaborationWorkspaceElement() {
  }

  public CollaborationWorkspaceElement(Date creationDate) {
    this.creationDate = creationDate;
  }

}
