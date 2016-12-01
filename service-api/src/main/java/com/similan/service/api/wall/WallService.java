package com.similan.service.api.wall;

import java.util.Date;
import java.util.List;

import com.similan.service.api.collaborationworkspace.dto.key.SharedDocumentKey;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.document.dto.key.DocumentKey;
import com.similan.service.api.wall.dto.basic.WallDto;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.basic.WallEntryStatisticsDto;
import com.similan.service.api.wall.dto.basic.WallPostDto;
import com.similan.service.api.wall.dto.key.IWallContainerKey;
import com.similan.service.api.wall.dto.key.WallKey;
import com.similan.service.api.wall.dto.operation.NewWallPostDto;

public interface WallService {

  <WallContainerKey extends IWallContainerKey> WallPostDto<WallContainerKey> post(
      WallKey<WallContainerKey> wallKey, NewWallPostDto newWallPostDto);

  <WallContainerKey extends IWallContainerKey> WallDto<WallContainerKey> get(
      WallKey<WallContainerKey> wallKey);

  <WallContainerKey extends IWallContainerKey> List<WallEntryDto<WallContainerKey>> getWorkspaceDocumentHistory(
      WallKey<WallContainerKey> wallKey, SharedDocumentKey document);

  <WallContainerKey extends IWallContainerKey> List<WallEntryDto<WallContainerKey>> geDocumentHistory(
      DocumentKey document, DocumentHistoryFilter filter);

  <WallContainerKey extends IWallContainerKey> List<WallEntryDto<WallContainerKey>> getLatest(
      WallKey<WallContainerKey> wallKey);

  <WallContainerKey extends IWallContainerKey> List<WallEntryDto<WallContainerKey>> getMore(
      WallKey<WallContainerKey> wallKey, Integer afterNumber);

  <WallContainerKey extends IWallContainerKey> List<WallEntryDto<WallContainerKey>> getByDate(
      WallKey<WallContainerKey> wallKey, Date fromDate, Date toDate);

  WallEntryStatisticsDto getBasicStatistics();

  <WallContainerKey extends IWallContainerKey> List<WallEntryDto<WallContainerKey>> getLatest(
      List<SocialActorKey> initiators, WallKey<WallContainerKey> wallKey);

  <WallContainerKey extends IWallContainerKey> List<WallEntryDto<WallContainerKey>> getMore(
      List<SocialActorKey> initiators, WallKey<WallContainerKey> wallKey,
      Integer afterNumber);

  public <WallContainerKey extends IWallContainerKey> List<WallEntryDto<WallContainerKey>> getAllExternalLinkReferencePosts(
      List<SocialActorKey> initiators, WallKey<WallContainerKey> wallKey);

}
