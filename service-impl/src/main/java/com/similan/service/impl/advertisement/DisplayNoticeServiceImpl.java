package com.similan.service.impl.advertisement;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.advertisement.DisplayNotice;
import com.similan.domain.entity.advertisement.DisplayNoticeLandingPage;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.repository.advertisement.DisplayNoticeLandingPageRepository;
import com.similan.domain.repository.advertisement.DisplayNoticeRepository;
import com.similan.service.api.advertisement.DisplayNoticeService;
import com.similan.service.api.advertisement.dto.basic.DisplayNoticeDto;
import com.similan.service.api.advertisement.dto.key.DisplayNoticeKey;
import com.similan.service.api.advertisement.dto.operation.DisplayNoticeLandingPageDto;
import com.similan.service.api.advertisement.dto.operation.NewDisplayNoticeCommentDto;
import com.similan.service.api.advertisement.dto.operation.NewDisplayNoticeDto;
import com.similan.service.api.advertisement.dto.operation.UpdateDisplayNoticeDto;
import com.similan.service.api.comment.CommentService;
import com.similan.service.api.comment.dto.basic.CommentDto;
import com.similan.service.api.comment.dto.operation.NewCommentDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.impl.base.ServiceImpl;
import com.similan.service.impl.community.SocialActorMarshaller;
import com.similan.service.internal.api.event.EventInternalService;
import com.similan.service.internal.api.event.io.advertisement.DisplayNoticeViewedEvent;

@Slf4j
@Service
public class DisplayNoticeServiceImpl extends ServiceImpl implements
    DisplayNoticeService {
  @Autowired
  private DisplayNoticeRepository displayNoticeRepository;
  @Autowired
  private DisplayNoticeLandingPageRepository displayNoticeLandingPageRepository;
  @Autowired
  private CommentService commentService;
  @Autowired
  private EventInternalService eventInternalService;
  @Autowired
  private SocialActorMarshaller actorMarshaller;
  @Autowired
  private DisplayNoticeMarshaller displayNoticeMarshaller;

  @Override
  @Transactional
  public DisplayNoticeDto create(NewDisplayNoticeDto notice) {
    log.info("Creating new display notice " + notice);

    if (notice.getIconAsset() == null || notice.getCreator() == null
        || notice.getOwner() == null || StringUtils.isBlank(notice.getName())) {
      throw new IllegalArgumentException(
          "One of the mandatory parameter is null");
    }

    if (notice.getText() == null && notice.getUrl() == null) {
      throw new IllegalArgumentException("Provide either content or url");
    }

    SocialOrganization owner = (SocialOrganization) actorMarshaller
        .unmarshallActorKey(notice.getOwner(), true);
    SocialPerson creator = (SocialPerson)
        actorMarshaller.unmarshallActorKey(notice.getCreator(), true);

    DisplayNotice displayNotice = displayNoticeRepository.create(owner,
        creator, notice.getIconAsset(), notice.getName());
    displayNotice.setUuid(notice.getUuid());

    DisplayNoticeLandingPage page = this.displayNoticeLandingPageRepository
        .create(notice.getText(), notice.getUrl());
    this.displayNoticeLandingPageRepository.save(page);

    displayNotice.setLandingPage(page);
    displayNoticeRepository.save(displayNotice);

    DisplayNoticeDto retDto = displayNoticeMarshaller.marshallDisplayNotice(displayNotice);
    return retDto;
  }

  @Override
  @Transactional
  public List<DisplayNoticeDto> get(SocialActorKey ownerKey) {

    SocialActor owner = actorMarshaller
        .unmarshallActorKey(ownerKey, true);

    List<DisplayNotice> displayNoticeList = this.displayNoticeRepository
        .findByOwner((SocialOrganization) owner);

    return displayNoticeMarshaller
        .marshallDisplayNotices(displayNoticeList);
  }

  @Override
  @Transactional
  public List<DisplayNoticeDto> getActive(SocialActorKey ownerKey) {
    SocialActor owner = actorMarshaller
        .unmarshallActorKey(ownerKey, true);

    List<DisplayNotice> displayNoticeList = this.displayNoticeRepository
        .findByOwnerAndActive((SocialOrganization) owner, Boolean.TRUE);

    return displayNoticeMarshaller
        .marshallDisplayNotices(displayNoticeList);
  }

  @Override
  @Transactional
  public DisplayNoticeLandingPageDto viewLandingPage(
      DisplayNoticeKey noticeKey, SocialActorKey viewerKey) {

    log.info("Getting landing page for notice " + noticeKey + "for viewer "
        + viewerKey);

    SocialActor viewer = actorMarshaller
        .unmarshallActorKey(viewerKey, true);

    DisplayNotice displayNotice = this.displayNoticeRepository
        .findOne(noticeKey.getId());
    DisplayNoticeLandingPage landingPage = displayNotice.getLandingPage();

    DisplayNoticeLandingPageDto retLandingPage = displayNoticeMarshaller
        .marshallDisplayNoticeLandingPage(landingPage);
    retLandingPage.setName(displayNotice.getName());
    retLandingPage.setNoticeKey(noticeKey);

    DisplayNoticeViewedEvent event = new DisplayNoticeViewedEvent();
    event.setNoticeId(displayNotice.getId());
    event.setViewerId(viewer.getId());

    this.eventInternalService.fire(event);

    return retLandingPage;

  }

  @Override
  @Transactional
  public DisplayNoticeDto activate(DisplayNoticeKey noticeKey,
      SocialActorKey ownerKey, SocialActorKey activatorKey) {

    SocialActor owner = actorMarshaller
        .unmarshallActorKey(ownerKey, true);
    List<DisplayNotice> displayNoticeList = this.displayNoticeRepository
        .findByOwnerAndActive((SocialOrganization) owner, Boolean.TRUE);

    for (DisplayNotice displayNotice : displayNoticeList) {
      displayNotice.setActive(Boolean.FALSE);
      this.displayNoticeRepository.save(displayNotice);
    }

    DisplayNotice activeNotice = this.displayNoticeRepository.findOne(noticeKey
        .getId());
    activeNotice.setActive(Boolean.TRUE);
    this.displayNoticeRepository.save(activeNotice);

    return displayNoticeMarshaller
        .marshallDisplayNoticeKey(noticeKey, true);

  }

  @Override
  @Transactional
  public DisplayNoticeDto deActivate(DisplayNoticeKey noticeKey,
      SocialActorKey ownerKey, SocialActorKey activatorKey) {

    DisplayNotice activeNotice = this.displayNoticeRepository.findOne(noticeKey
        .getId());
    activeNotice.setActive(Boolean.FALSE);
    this.displayNoticeRepository.save(activeNotice);

    return displayNoticeMarshaller
        .marshallDisplayNoticeKey(noticeKey, true);
  }

  @Override
  @Transactional
  public CommentDto<DisplayNoticeKey> postComment(
      NewDisplayNoticeCommentDto newComment) {

    log.info("Comment posted for display notice " + newComment);

    NewCommentDto newCommentDto = new NewCommentDto(newComment.getAuthor()
        .getName(), newComment.getContent());

    CommentDto<DisplayNoticeKey> retComment = this.commentService.postComment(
        newComment.getDisplayNoticeKey(), newCommentDto);

    return retComment;
  }

  @Override
  @Transactional
  public DisplayNoticeDto get(DisplayNoticeKey noticeKey) {
    DisplayNotice activeNotice = this.displayNoticeRepository.findOne(noticeKey
        .getId());
    return displayNoticeMarshaller
        .marshallDisplayNotice(activeNotice);
  }

  @Override
  @Transactional
  public DisplayNoticeLandingPageDto getLandingPage(DisplayNoticeKey noticeKey) {
    DisplayNotice activeNotice = this.displayNoticeRepository.findOne(noticeKey
        .getId());
    return displayNoticeMarshaller
        .marshallDisplayNoticeLandingPage(activeNotice.getLandingPage());
  }

  @Override
  @Transactional
  public DisplayNoticeDto update(DisplayNoticeKey noticeKey,
      UpdateDisplayNoticeDto notice) {

    DisplayNotice activeNotice = this.displayNoticeRepository.findOne(noticeKey
        .getId());
    activeNotice.setIconAsset(notice.getIconAsset());
    activeNotice.setName(notice.getName());
    this.displayNoticeRepository.save(activeNotice);

    activeNotice.getLandingPage().setText(notice.getText());
    this.displayNoticeLandingPageRepository.save(activeNotice.getLandingPage());

    return displayNoticeMarshaller
        .marshallDisplayNotice(activeNotice);
  }

}
