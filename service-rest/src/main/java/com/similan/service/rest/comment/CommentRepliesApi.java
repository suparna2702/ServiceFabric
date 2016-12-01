package com.similan.service.rest.comment;


import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.similan.service.api.base.dto.key.EntityPaths;
import com.similan.service.api.comment.CommentService;
import com.similan.service.api.comment.dto.basic.CommentReplyDto;
import com.similan.service.api.comment.dto.key.CommentKey;
import com.similan.service.api.comment.dto.key.ICommentableKey;
import com.similan.service.api.comment.dto.operation.NewCommentReplyDto;
import com.similan.service.rest.base.SimilanApi;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api(value = EntityPaths.PATH_COMMENT_REPLIES)
@Service
@Path(EntityPaths.PATH_COMMENT_REPLIES)
public class CommentRepliesApi extends SimilanApi {
  @Autowired
  private CommentService commentService;

  @ApiOperation(value = "Creates a new comment reply.", response = CommentReplyDto.class) 
  @POST
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response postReply(
      @PathParam("commentablePath") List<PathSegment> commentablePath,
      @PathParam("number") Integer number, NewCommentReplyDto newReplyDto) {
    ICommentableKey commentableKey = getKey(commentablePath,
        ICommentableKey.class);
    CommentKey<ICommentableKey> commentKey = new CommentKey<ICommentableKey>(
        commentableKey, number);

    CommentReplyDto<ICommentableKey> replyDto = commentService.postReply(
        commentKey, newReplyDto);
    URI location = buildUri(replyDto.getKey());
    return Response.created(location).entity(replyDto).build();
  }
}
