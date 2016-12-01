package com.similan.domain.repository.comment.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.comment.CommentReply;
import com.similan.service.api.base.dto.key.EntityType;

public interface JpaCommentReplyRepository extends
    JpaRepository<CommentReply, Long> {

  @Query("select reply from CommentReply reply"
      + " where reply.comment.commentable.type = :commentableType"
      + " and reply.comment.commentable.id = :commentableId"
      + " and reply.comment.number = :commentNumber"
      + " and reply.number = :number")
  CommentReply findOne(@Param("commentableType") EntityType commentableType,
      @Param("commentableId") long commentableId,
      @Param("commentNumber") int commentNumber, @Param("number") int number);

  @Query("select reply from CommentReply reply"
      + " where reply.comment.commentable.type = :commentableType"
      + " and reply.comment.commentable.id = :commentableId"
      + " and reply.comment.number = :commentNumber" + " order by reply.number")
  List<CommentReply> findByComment(
      @Param("commentableType") EntityType commentableType,
      @Param("commentableId") long commentableId,
      @Param("commentNumber") int commentNumber);
}
