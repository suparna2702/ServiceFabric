package com.similan.service.rest.document;

import java.io.InputStream;
import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.similan.service.api.asset.NewAssetStream;
import com.similan.service.api.asset.dto.basic.AssetMetadataDto;
import com.similan.service.api.base.dto.key.EntityPaths;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.DocumentInstanceService;
import com.similan.service.api.document.DocumentService;
import com.similan.service.api.document.dto.extended.DocumentInstanceAndDocumentDto;
import com.similan.service.api.document.dto.extended.DocumentStatisticsDto;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.document.dto.operation.NewDocumentDto;
import com.similan.service.api.document.dto.operation.NewDocumentInstanceDto;
import com.similan.service.rest.base.SimilanApi;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api(value = EntityPaths.PATH_DOCUMENTS)
@Service
@Path(EntityPaths.PATH_DOCUMENTS)
public class DocumentsApi extends SimilanApi {
  @Autowired
  private DocumentService documentService;

  @Autowired
  private DocumentInstanceService documentInstanceService;

  @ApiOperation(value = "Creates a new document.", response = DocumentInstanceAndDocumentDto.class) 
  @PUT
  @Path("")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  public Response create(
      @PathParam("") DocumentKey documentKey,
      @Multipart(type = MediaType.APPLICATION_JSON) NewDocumentDto newDocumentDto,
      @HeaderParam("Content-Disposition") String contentDisposition,
      @HeaderParam("Content-Type") String contentType,
      @Multipart InputStream stream) {
    NewAssetStream assetStream = new NewAssetStream(new AssetMetadataDto(
        contentType, contentDisposition), stream);
    DocumentInstanceAndDocumentDto documentInstance = documentService.create(
        documentKey, newDocumentDto, assetStream);
    URI location = buildUri(documentKey);
    return Response.created(location).entity(documentInstance).build();
  }

  @ApiOperation(value = "Creates a new version of an existing document.", response = DocumentInstanceAndDocumentDto.class) 
  @POST
  @Path("")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createNewInstance(
      @PathParam("") DocumentKey documentKey,
      @Multipart(type = MediaType.APPLICATION_JSON) NewDocumentInstanceDto newDocumentInstanceDto,
      @HeaderParam("Content-Disposition") String contentDisposition,
      @HeaderParam("Content-Type") String contentType,
      @Multipart InputStream stream) {
    NewAssetStream assetStream = new NewAssetStream(new AssetMetadataDto(
        contentType, contentDisposition), stream);
    DocumentInstanceAndDocumentDto documentInstance = documentInstanceService
        .create(documentKey, newDocumentInstanceDto, assetStream);
    URI location = buildUri(documentInstance.getKey());
    return Response.created(location).entity(documentInstance).build();
  }

  @ApiOperation(value = "Retrieves an existing document.") 
  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  public DocumentInstanceAndDocumentDto get(@PathParam("") DocumentKey documentKey) {
    return documentService.get(documentKey);
  }

  @ApiOperation(value = "Deletes a document.") 
  @DELETE
  @Path("")
  public void delete(@PathParam("") DocumentKey documentKey) {
    documentService.delete(documentKey);
  }

  @ApiOperation(value = "Locks a document.") 
  @PUT
  @Path(REST_SEGMENT_LOCK)
  public Response lock(@PathParam("") DocumentKey documentKey,
      SocialActorKey locker) {
    boolean newLock = documentService.lock(documentKey, locker);
    if (newLock) {
      URI location = buildUri(documentKey, REST_SEGMENT_LOCK);
      return Response.created(location).build();
    } else {
      return Response.noContent().build();
    }
  }

  @ApiOperation(value = "Unlocks a document.") 
  @DELETE
  @Path(REST_SEGMENT_LOCK)
  public Response unlock(@PathParam("") DocumentKey documentKey,
      SocialActorKey locker) {
    boolean wasLocked = documentService.unlock(documentKey, locker);
    if (wasLocked) {
      return Response.noContent().build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }

  @ApiOperation(value = "Retrieves the document lock owner.", response = SocialActorKey.class) 
  @GET
  @Path(REST_SEGMENT_LOCK)
  public Response getLockOwner(@PathParam("") DocumentKey documentKey) {
    SocialActorKey currentLockOwner = documentService.getLockOwner(documentKey);
    if (currentLockOwner == null) {
      return Response.status(Status.NOT_FOUND).build();
    } else {
      return Response.ok(currentLockOwner).build();
    }
  }

  @ApiOperation(value = "Retrieves the document statistics.")
  @GET
  @Path(REST_SEGMENT_STATISTICS)
  @Produces(MediaType.APPLICATION_JSON)
  public DocumentStatisticsDto getStatistics(@PathParam("") DocumentKey documentKey) {
    DocumentStatisticsDto statistics = documentService
        .getStatistics(documentKey);
    return statistics;
  }

}
