package com.similan.service.rest.actor;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.similan.service.api.base.dto.key.EntityPaths;
import com.similan.service.api.community.SocialActorService;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.rest.base.SimilanApi;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api(value = EntityPaths.PATH_ACTOR)
@Service
@Path(EntityPaths.PATH_ACTOR)
public class ActorApi extends SimilanApi {
  @Autowired
  private SocialActorService actorService;

  @ApiOperation(value = "Retrieves the actor information.") 
  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  public ActorDto get(@PathParam("name") String name) {
    SocialActorKey actorKey = new SocialActorKey(name);
    return actorService.getActor(actorKey);
  }
}
