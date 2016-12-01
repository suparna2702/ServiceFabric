package com.similan.service.rest.feed;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.similan.service.api.base.dto.key.EntityPaths;
import com.similan.service.api.feed.FeedService;
import com.similan.service.api.feed.dto.basic.FeedEntryDto;
import com.similan.service.api.feed.dto.key.FeedKey;
import com.similan.service.rest.base.SimilanApi;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api(value = EntityPaths.PATH_FEED_ENTRIES)
@Service
@Path(EntityPaths.PATH_FEED_ENTRIES)
public class FeedEntriesApi extends SimilanApi {
  @Autowired
  private FeedService feedService;

  @ApiOperation(value = "Retrieves feed entries.", response = FeedEntryDto.class, responseContainer = "List")
  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  public Response get(@PathParam("feed.owner.name") String feedOwnerName,
      @QueryParam("afterNumber") Integer afterNumber) {
    FeedKey feedKey = new FeedKey(feedOwnerName);
    List<FeedEntryDto> feedEntries = afterNumber == null ? feedService
        .getLatest(feedKey) : feedService.getMore(feedKey, afterNumber);
    return Response.ok(new GenericEntity<List<FeedEntryDto>>(feedEntries) {
    }).build();
  }
}
