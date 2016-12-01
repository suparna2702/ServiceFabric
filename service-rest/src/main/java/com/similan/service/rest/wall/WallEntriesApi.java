package com.similan.service.rest.wall;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.similan.service.api.base.dto.key.EntityPaths;
import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.wall.WallService;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.basic.WallPostDto;
import com.similan.service.api.wall.dto.key.IWallContainerKey;
import com.similan.service.api.wall.dto.key.WallKey;
import com.similan.service.api.wall.dto.operation.NewWallPostDto;
import com.similan.service.rest.base.SimilanApi;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api(value = EntityPaths.PATH_WALL_ENTRIES)
@Service
@Path(EntityPaths.PATH_WALL_ENTRIES)
public class WallEntriesApi extends SimilanApi {
  @Autowired
  private WallService wallService;

  @POST
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response post(
      @PathParam("wallContainerPath") List<PathSegment> wallContainerPath,
      NewWallPostDto newWallPostDto) {
    IWallContainerKey wallContainerKey = getKey(wallContainerPath,
        IWallContainerKey.class);
    WallKey<IWallContainerKey> wallKey = new WallKey<IWallContainerKey>(
        wallContainerKey);
    WallPostDto<IWallContainerKey> wallPostDto = wallService.post(wallKey,
        newWallPostDto);
    URI location = buildUri(wallPostDto.getKey());
    return Response.created(location).entity(wallPostDto).build();
  }

  @ApiOperation(value = "Retrieves wall entries.", response = WallEntryDto.class, responseContainer = "List")
  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  public List<WallEntryDto<IWallContainerKey>> get(
      @PathParam("wallContainerPath") List<PathSegment> wallContainerPath, @QueryParam("afterNumber") Integer afterNumber) {
    IWallContainerKey wallContainerKey = getKey(wallContainerPath,
        IWallContainerKey.class);
    WallKey<IWallContainerKey> wallKey = new WallKey<IWallContainerKey>(
        wallContainerKey);
    List<WallEntryDto<IWallContainerKey>> wallEntries =
        afterNumber == null ?
            wallService.getLatest(wallKey) :
            wallService.getMore(wallKey, afterNumber);
    return wallEntries;
  }

  @ApiOperation(value = "Retrieves wall entries by date.", response = WallEntryDto.class, responseContainer = "List")
  @GET
  @Path("/by-date")
  @Produces(MediaType.APPLICATION_JSON)
  public List<WallEntryDto<IWallContainerKey>> getByDate(
      @PathParam("wallContainerPath") List<PathSegment> wallContainerPath,
      @QueryParam("fromDate") String fromDate,
      @QueryParam("toDate") String toDate) {
    IWallContainerKey wallContainerKey = getKey(wallContainerPath,
        IWallContainerKey.class);
    WallKey<IWallContainerKey> wallKey = new WallKey<IWallContainerKey>(
        wallContainerKey);
    List<WallEntryDto<IWallContainerKey>> wallEntries = wallService.getByDate(
        wallKey, EntityType.valueOfTimestamp(fromDate),
        EntityType.valueOfTimestamp(toDate));
    return wallEntries;
  }
}
