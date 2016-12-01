package com.similan.service.api.advertisement;

import java.util.List;

import com.similan.service.api.advertisement.dto.basic.DisplayNoticeDto;
import com.similan.service.api.advertisement.dto.key.DisplayNoticeKey;
import com.similan.service.api.advertisement.dto.operation.DisplayNoticeLandingPageDto;
import com.similan.service.api.advertisement.dto.operation.NewDisplayNoticeCommentDto;
import com.similan.service.api.advertisement.dto.operation.NewDisplayNoticeDto;
import com.similan.service.api.advertisement.dto.operation.UpdateDisplayNoticeDto;
import com.similan.service.api.comment.dto.basic.CommentDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

public interface DisplayNoticeService {

  public DisplayNoticeDto create(NewDisplayNoticeDto notice);

  public DisplayNoticeDto update(DisplayNoticeKey noticeKey,
      UpdateDisplayNoticeDto notice);

  public List<DisplayNoticeDto> getActive(SocialActorKey ownerKey);

  public DisplayNoticeDto get(DisplayNoticeKey noticeKey);

  public DisplayNoticeLandingPageDto getLandingPage(DisplayNoticeKey noticeKey);

  public List<DisplayNoticeDto> get(SocialActorKey business);

  public DisplayNoticeLandingPageDto viewLandingPage(
      DisplayNoticeKey noticeKey, SocialActorKey viewerKey);

  public DisplayNoticeDto activate(DisplayNoticeKey notice,
      SocialActorKey ownerKey, SocialActorKey activatorKey);

  public DisplayNoticeDto deActivate(DisplayNoticeKey notice,
      SocialActorKey ownerKey, SocialActorKey activatorKey);

  public CommentDto<DisplayNoticeKey> postComment(
      NewDisplayNoticeCommentDto newComment);

}
