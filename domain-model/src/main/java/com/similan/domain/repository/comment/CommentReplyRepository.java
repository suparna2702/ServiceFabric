package com.similan.domain.repository.comment;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.comment.Comment;
import com.similan.domain.entity.comment.CommentReply;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.repository.comment.jpa.JpaCommentReplyRepository;
import com.similan.service.api.base.dto.key.EntityType;

@Repository
public class CommentReplyRepository {
  @Autowired
  private JpaCommentReplyRepository repository;

  public CommentReply findOne(Long id) {
    return repository.findOne(id);
  }

  public CommentReply save(CommentReply entity) {
    return repository.save(entity);
  }

  public CommentReply findOne(EntityType commentableType,
      long commentableId, int commentNumber, int number) {
    return repository.findOne(commentableType,
      commentableId, commentNumber, number);
  }

  public List<CommentReply> findByComment(EntityType commentableType,
      long commentableId, int commentNumber) {
    return repository.findByComment(commentableType,
      commentableId, commentNumber);
  }

  private int getNextNumber(Comment comment) {
    CommentReply lastReply = comment.getLastReply();
    if (lastReply == null) {
      return 0;
    }
    int lastNumber = lastReply.getNumber();
    int nextNumber = lastNumber + 1;
    return nextNumber;
  }

  private void created(Comment comment, CommentReply createdReply) {
    comment.setLastReply(createdReply);
  }

  public CommentReply create(SocialActor author, Comment comment, String content) {
    int number = getNextNumber(comment);
    Date date = new Date();

    CommentReply reply = new CommentReply(number, date, content);
    reply.setAuthor(author);
    reply.setComment(comment);
    created(comment, reply);
    return reply;
  }

}
