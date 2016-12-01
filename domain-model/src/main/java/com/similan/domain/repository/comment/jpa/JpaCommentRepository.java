package com.similan.domain.repository.comment.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.comment.Comment;
import com.similan.service.api.base.dto.key.EntityType;

public interface JpaCommentRepository extends JpaRepository<Comment, Long> {

  @Query("select comment from Comment comment"
      + " where comment.commentable.type = :commentableType"
      + " and comment.commentable.id = :commentableId"
      + " and comment.number = :number")
  Comment findOne(@Param("commentableType") EntityType commentableType,
      @Param("commentableId") long commentableId, @Param("number") int number);

  @Query("select comment from Comment comment"
      + " where comment.commentable.type = :commentableType"
      + " and comment.commentable.id = :commentableId"
      + " order by comment.number")
  List<Comment> findByCommentable(
      @Param("commentableType") EntityType commentableType,
      @Param("commentableId") long commentableId);

}
