package com.similan.service.rest.comment;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.similan.service.api.base.dto.key.EntityPaths;
import com.similan.service.api.comment.CommentService;
import com.similan.service.api.comment.dto.basic.CommentDto;
import com.similan.service.api.comment.dto.extended.CommentAndRepliesDto;
import com.similan.service.api.comment.dto.key.ICommentableKey;
import com.similan.service.api.comment.dto.operation.NewCommentDto;
import com.similan.service.rest.base.SimilanApi;
import com.wordnik.swagger.annotations.Api;

@Api(EntityPaths.PATH_COMMENTS)
@Service
@Path(EntityPaths.PATH_COMMENTS)
public class CommentsApi extends SimilanApi {
  @Autowired
  private CommentService commentService;

  @POST
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response postComment(
      @PathParam("commentablePath") List<PathSegment> commentablePath,
      NewCommentDto newCommentDto) {
    ICommentableKey commentableKey = getKey(commentablePath,
        ICommentableKey.class);
    CommentDto<ICommentableKey> commentDto = commentService.postComment(
        commentableKey, newCommentDto);
    URI location = buildUri(commentDto.getKey());
    return Response.created(location).entity(commentDto).build();
  }

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getComments(
      @PathParam("commentablePath") List<PathSegment> commentablePath) {
    ICommentableKey commentableKey = getKey(commentablePath,
        ICommentableKey.class);
    List<CommentAndRepliesDto<ICommentableKey>> commentAndRepliesDtos = commentService
        .getComments(commentableKey);
    return Response.ok(
        new GenericEntity<List<CommentAndRepliesDto<ICommentableKey>>>(
            commentAndRepliesDtos) {
        }).build();
  }
}
