package com.similan.domain.repository.comment;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.comment.Comment;
import com.similan.domain.entity.comment.ICommentable;
import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.repository.comment.jpa.JpaCommentRepository;
import com.similan.domain.repository.common.GenericReferenceRepository;
import com.similan.service.api.base.dto.key.EntityType;

@Repository
public class CommentRepository {
  @Autowired
  private JpaCommentRepository repository;

  @Autowired
  private GenericReferenceRepository genericReferenceRepository;

  public Comment save(Comment entity) {
    return repository.save(entity);
  }

  public Comment findOne(Long id) {
    return repository.findOne(id);
  }

  public Comment findOne(EntityType commentableType,
      long commentableId, int number) {
    return repository.findOne(commentableType,
      commentableId, number);
  }

  public List<Comment> findByCommentable(EntityType commentableType,
      long commentableId) {
    return repository.findByCommentable(commentableType,
      commentableId);
  }

  protected int getNextNumber(ICommentable commentable) {
    Comment lastComment = commentable.getLastComment();
    if (lastComment == null) {
      return 0;
    }
    int lastNumber = lastComment.getNumber();
    int nextNumber = lastNumber + 1;
    return nextNumber;
  }

  protected void created(ICommentable commentable, Comment creatadComment) {
    commentable.setLastComment(creatadComment);
  }

  public Comment create(SocialActor author, ICommentable commentable,
      String content) {
    int number = getNextNumber(commentable);
    Date date = new Date();
    GenericReference<ICommentable> commentableReference = genericReferenceRepository
        .create(commentable);

    Comment comment = new Comment(number, date, content);
    comment.setAuthor(author);
    comment.setCommentable(commentableReference);
    created(commentable, comment);
    return comment;
  }
}
