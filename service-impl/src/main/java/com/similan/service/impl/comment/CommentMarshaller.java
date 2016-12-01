package com.similan.service.impl.comment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.comment.Comment;
import com.similan.domain.entity.comment.CommentReply;
import com.similan.domain.entity.comment.ICommentable;
import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.repository.comment.CommentReplyRepository;
import com.similan.domain.repository.comment.CommentRepository;
import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.comment.dto.basic.CommentDto;
import com.similan.service.api.comment.dto.basic.CommentReplyDto;
import com.similan.service.api.comment.dto.error.CommentErrorCode;
import com.similan.service.api.comment.dto.error.CommentException;
import com.similan.service.api.comment.dto.extended.CommentAndRepliesDto;
import com.similan.service.api.comment.dto.key.CommentKey;
import com.similan.service.api.comment.dto.key.CommentReplyKey;
import com.similan.service.api.comment.dto.key.ICommentableKey;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.impl.Marshaller;
import com.similan.service.impl.common.GenericReferenceMarshaller;
import com.similan.service.impl.community.SocialActorMarshaller;

@Component
public class CommentMarshaller extends Marshaller {
  @Autowired
  private CommentRepository commentRepository;
  @Autowired
  private CommentReplyRepository replyRepository;
  @Autowired
  private GenericReferenceMarshaller genericReferenceMarshaller;
  @Autowired
  private SocialActorMarshaller actorMarshaller;

  private <CommentableKey extends ICommentableKey> List<CommentAndRepliesDto<CommentableKey>> marshallCommentsAndReplies(
      CommentableKey commentableKey, List<Comment> comments) {
    List<CommentAndRepliesDto<CommentableKey>> commentAndRepliesDtos = new ArrayList<CommentAndRepliesDto<CommentableKey>>(
        comments.size());
    for (Comment comment : comments) {
      CommentAndRepliesDto<CommentableKey> commentAndRepliesDto = marshallCommentAndReplies(
          commentableKey, comment);
      commentAndRepliesDtos.add(commentAndRepliesDto);
    }
    return commentAndRepliesDtos;
  }

  private <CommentableKey extends ICommentableKey> CommentAndRepliesDto<CommentableKey> marshallCommentAndReplies(
      CommentableKey commentableKey, Comment comment) {
    CommentDto<CommentableKey> commentDto = marshallComment(commentableKey,
        comment);
    List<CommentReply> replies = comment.getReplies();
    CommentKey<CommentableKey> commentKey = commentDto.getKey();
    List<CommentReplyDto<CommentableKey>> replyDtos = marshallCommentReplies(
        commentKey, replies);
    CommentAndRepliesDto<CommentableKey> commentAndRepliesDto = new CommentAndRepliesDto<CommentableKey>(
        commentDto, replyDtos);
    return commentAndRepliesDto;
  }

  private <CommentableKey extends ICommentableKey> List<CommentReplyDto<CommentableKey>> marshallCommentReplies(
      CommentKey<CommentableKey> commentKey, List<CommentReply> replies) {
    List<CommentReplyDto<CommentableKey>> replyDtos = new ArrayList<CommentReplyDto<CommentableKey>>(
        replies.size());
    for (CommentReply reply : replies) {
      CommentReplyDto<CommentableKey> replyDto = marshallReply(commentKey,
          reply);
      replyDtos.add(replyDto);
    }
    return replyDtos;
  }

  public <CommentableKey extends ICommentableKey> CommentReplyDto<CommentableKey> marshallReply(
      CommentReply reply) {
    Comment comment = reply.getComment();
    CommentKey<CommentableKey> commentKey = marshallCommentKey(comment);
    return marshallReply(commentKey, reply);
  }

  private <CommentableKey extends ICommentableKey> CommentReplyDto<CommentableKey> marshallReply(
      CommentKey<CommentableKey> commentKey, CommentReply reply) {
    CommentReplyKey<CommentableKey> key = marshallReplyKey(commentKey, reply);
    SocialActor author = reply.getAuthor();
    ActorDto authorDto = actorMarshaller
        .marshallActor(author);
    Date date = reply.getDate();
    String content = reply.getContent();
    CommentReplyDto<CommentableKey> replyDto = new CommentReplyDto<CommentableKey>(
        key, authorDto, date, content);
    return replyDto;
  }

  public <CommentableKey extends ICommentableKey> CommentReplyKey<CommentableKey> marshallReplyKey(
      CommentReply reply) {
    Comment comment = reply.getComment();
    CommentKey<CommentableKey> commentKey = marshallCommentKey(comment);
    return marshallReplyKey(commentKey, reply);
  }

  private <CommentableKey extends ICommentableKey> CommentReplyKey<CommentableKey> marshallReplyKey(
      CommentKey<CommentableKey> commentKey, CommentReply reply) {
    int number = reply.getNumber();
    CommentReplyKey<CommentableKey> replyDto = new CommentReplyKey<CommentableKey>(
        commentKey, number);
    return replyDto;
  }

  private <CommentableKey extends ICommentableKey> CommentDto<CommentableKey> marshallComment(
      CommentableKey commentableKey, Comment comment) {
    CommentKey<CommentableKey> key = marshallCommentKey(commentableKey, comment);
    SocialActor author = comment.getAuthor();
    ActorDto authorDto = actorMarshaller
        .marshallActor(author);
    Date date = comment.getDate();
    String content = comment.getContent();

    CommentDto<CommentableKey> commentDto = new CommentDto<CommentableKey>(key,
        authorDto, date, content);
    return commentDto;
  }

  public <CommentableKey extends ICommentableKey> CommentKey<CommentableKey> marshallCommentKey(
      Comment comment) {
    GenericReference<ICommentable> commentableReference = comment
        .getCommentable();
    CommentableKey commentableKey = marshallCommentableKey(commentableReference);
    return marshallCommentKey(commentableKey, comment);
  }

  private <CommentableKey extends ICommentableKey> CommentKey<CommentableKey> marshallCommentKey(
      CommentableKey commentableKey, Comment comment) {
    int number = comment.getNumber();
    CommentKey<CommentableKey> key = new CommentKey<CommentableKey>(
        commentableKey, number);
    return key;
  }

  public <CommentableKey extends ICommentableKey> Comment unmarshallCommentKey(
      CommentKey<CommentableKey> commentKey, boolean required) {
    CommentableKey commentableKey = commentKey.getCommentable();
    ICommentable commentable = unmarshallCommentableKey(commentableKey,
        required);
    if (commentable == null) {
      if (required) {
        throw new CommentException(CommentErrorCode.COMMENT_NOT_FOUND, "Comment doesn't exist: " + commentKey);
      }
      return null;
    }

    EntityType commentableType = commentable.getEntityType();
    Long commentableId = commentable.getId();
    Integer number = commentKey.getNumber();

    Comment comment = commentRepository.findOne(commentableType, commentableId,
        number);
    if (required && comment == null) {
      throw new CommentException(CommentErrorCode.COMMENT_NOT_FOUND, "Comment doesn't exist: " + commentKey);
    }
    return comment;
  }

  public <CommentableKey extends ICommentableKey> ICommentable unmarshallCommentableKey(
      CommentableKey commentableKey, boolean required) {
    ICommentable entity = genericReferenceMarshaller
        .unmarshallKey(commentableKey, required, ICommentable.class);
    return entity;
  }

  public <CommentableKey extends ICommentableKey> CommentableKey marshallCommentableKey(
      GenericReference<ICommentable> commentableReference) {
    CommentableKey key = genericReferenceMarshaller
        .marshallKey(commentableReference, ICommentableKey.class);
    return key;
  }

  public <CommentableKey extends ICommentableKey> CommentableKey marshallCommentableKey(
      ICommentable commentable) {
    return genericReferenceMarshaller.marshallKey(
        commentable, ICommentableKey.class);
  }

  public <CommentableKey extends ICommentableKey> CommentReply unmarshallReplyKey(
      CommentReplyKey<CommentableKey> replyKey, boolean required) {
    CommentKey<CommentableKey> commentKey = replyKey.getComment();
    CommentableKey commentableKey = commentKey.getCommentable();
    ICommentable commentable = unmarshallCommentableKey(commentableKey,
        required);
    if (commentable == null) {
      if (required) {
        throw new CommentException(CommentErrorCode.COMMENT_REPLY_NOT_FOUND, "Comment reply doesn't exist: " + replyKey);
      }
      return null;
    }

    EntityType commentableType = commentable.getEntityType();
    Long commentableId = commentable.getId();
    Integer commentNumber = commentKey.getNumber();
    Integer number = replyKey.getNumber();

    CommentReply reply = replyRepository.findOne(commentableType,
        commentableId, commentNumber, number);
    if (required && reply == null) {
      throw new CommentException(CommentErrorCode.COMMENT_REPLY_NOT_FOUND, "Comment reply doesn't exist: " + replyKey);
    }
    return reply;
  }

  public <CommentableKey extends ICommentableKey> CommentDto<CommentableKey> marshallComment(
      Comment comment) {
    GenericReference<ICommentable> commentableReference = comment
        .getCommentable();
    CommentableKey commentableKey = marshallCommentableKey(commentableReference);
    return marshallComment(commentableKey, comment);
  }

  public <CommentableKey extends ICommentableKey> List<CommentAndRepliesDto<CommentableKey>> marshallCommentsAndReplies(
      ICommentable commentable, List<Comment> comments) {
    CommentableKey commentableKey = marshallCommentableKey(commentable);
    List<CommentAndRepliesDto<CommentableKey>> commentAndRepliesDtos = marshallCommentsAndReplies(
        commentableKey, comments);
    return commentAndRepliesDtos;
  }
}
