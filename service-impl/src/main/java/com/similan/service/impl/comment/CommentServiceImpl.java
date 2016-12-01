package com.similan.service.impl.comment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.comment.Comment;
import com.similan.domain.entity.comment.CommentReply;
import com.similan.domain.entity.comment.ICommentable;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.repository.comment.CommentReplyRepository;
import com.similan.domain.repository.comment.CommentRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.comment.CommentService;
import com.similan.service.api.comment.dto.basic.CommentDto;
import com.similan.service.api.comment.dto.basic.CommentReplyDto;
import com.similan.service.api.comment.dto.extended.CommentAndRepliesDto;
import com.similan.service.api.comment.dto.key.CommentKey;
import com.similan.service.api.comment.dto.key.ICommentableKey;
import com.similan.service.api.comment.dto.operation.NewCommentDto;
import com.similan.service.api.comment.dto.operation.NewCommentReplyDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.impl.base.ServiceImpl;
import com.similan.service.impl.community.SocialActorMarshaller;
import com.similan.service.internal.api.email.EmailInternalService;
import com.similan.service.internal.api.event.EventInternalService;
import com.similan.service.internal.api.event.io.comment.CommentPostedEvent;

@Service
public class CommentServiceImpl extends ServiceImpl implements CommentService {
  @Autowired
  private CommentRepository commentRepository;
  @Autowired
  private CommentReplyRepository commentReplyRepository;
  @Autowired
  private EventInternalService eventService;
  @Autowired
  private PlatformCommonSettings commonSettings;
  @Autowired
  private EmailInternalService emailService;
  @Autowired
  private SocialActorMarshaller actorMarshaller;
  @Autowired
  private CommentMarshaller commentMarshaller;

  @Override
  @Transactional
  public <CommentableKey extends ICommentableKey> CommentDto<CommentableKey> postComment(
      CommentableKey commentableKey, NewCommentDto newCommentDto) {
	  
    SocialActorKey authorKey = newCommentDto.getAuthor();
    SocialActor author = actorMarshaller
        .unmarshallActorKey(authorKey, true);
    
    ICommentable commentable = commentMarshaller
        .unmarshallCommentableKey(commentableKey, true);
    String content = newCommentDto.getContent();

    Comment comment = commentRepository.create(author, commentable, content);
    commentRepository.save(comment);

    CommentDto<CommentableKey> commentDto = commentMarshaller.marshallComment(comment);

    CommentPostedEvent commentEvent = new CommentPostedEvent(comment.getId());
    this.eventService.fire(commentEvent);

    return commentDto;
  }

  @Override
  @Transactional
  public <CommentableKey extends ICommentableKey> CommentReplyDto<CommentableKey> postReply(
      CommentKey<CommentableKey> commentKey, NewCommentReplyDto newReplyDto) {
    SocialActorKey authorKey = newReplyDto.getAuthor();
    SocialActor author = actorMarshaller
        .unmarshallActorKey(authorKey, true);
    Comment comment = commentMarshaller
        .unmarshallCommentKey(commentKey, true);
    String content = newReplyDto.getContent();

    CommentReply reply = commentReplyRepository
        .create(author, comment, content);
    commentReplyRepository.save(reply);

    CommentReplyDto<CommentableKey> replyDto = commentMarshaller.marshallReply(reply);
    return replyDto;
  }

  @Override
  @Transactional(readOnly = true)
  public <CommentableKey extends ICommentableKey> List<CommentAndRepliesDto<CommentableKey>> getComments(
      CommentableKey commentableKey) {
    ICommentable commentable = commentMarshaller
        .unmarshallCommentableKey(commentableKey, true);

    EntityType commentableType = commentable.getEntityType();
    Long commentableId = commentable.getId();

    List<Comment> comments = commentRepository.findByCommentable(
        commentableType, commentableId);

    List<CommentAndRepliesDto<CommentableKey>> commentAndRepliesDtos = commentMarshaller.marshallCommentsAndReplies(commentable,
            comments);
    return commentAndRepliesDtos;
  }

  protected CommentRepository getCommentRepository() {
    return commentRepository;
  }

  protected CommentReplyRepository getCommentReplyRepository() {
    return commentReplyRepository;
  }
}
