package com.similan.service.api.comment;

import java.util.List;

import com.similan.service.api.comment.dto.basic.CommentDto;
import com.similan.service.api.comment.dto.basic.CommentReplyDto;
import com.similan.service.api.comment.dto.extended.CommentAndRepliesDto;
import com.similan.service.api.comment.dto.key.CommentKey;
import com.similan.service.api.comment.dto.key.ICommentableKey;
import com.similan.service.api.comment.dto.operation.NewCommentDto;
import com.similan.service.api.comment.dto.operation.NewCommentReplyDto;

public interface CommentService {

  <CommentableKey extends ICommentableKey> CommentDto<CommentableKey> postComment(
      CommentableKey commentableKey, NewCommentDto newCommentDto);

  <CommentableKey extends ICommentableKey> CommentReplyDto<CommentableKey> postReply(
      CommentKey<CommentableKey> commentKey, NewCommentReplyDto newReplyDto);

  <CommentableKey extends ICommentableKey> List<CommentAndRepliesDto<CommentableKey>> getComments(
      CommentableKey commentableKey);

}