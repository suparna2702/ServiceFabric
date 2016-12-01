package com.similan.portal.view.advertisement;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;
import com.similan.service.api.advertisement.dto.basic.DisplayNoticeDto;
import com.similan.service.api.advertisement.dto.key.DisplayNoticeKey;
import com.similan.service.api.advertisement.dto.operation.DisplayNoticeLandingPageDto;
import com.similan.service.api.advertisement.dto.operation.NewDisplayNoticeCommentDto;
import com.similan.service.api.comment.dto.extended.CommentAndRepliesDto;

@Scope("view")
@Component("displayNoticeLandingPageView")
@Slf4j
public class DisplayNoticeLandingPageView extends BaseView {

  private static final long serialVersionUID = 1L;

  @Autowired(required = true)
  private MemberInfoDto member = null;

  @Autowired
  private OrganizationDetailInfoDto orgInfo = null;

  private String wallComment = StringUtils.EMPTY;

  private List<CommentAndRepliesDto<DisplayNoticeKey>> wallComments = null;

  private DisplayNoticeDto displayNotice = null;

  private DisplayNoticeLandingPageDto landingPage = null;

  @PostConstruct
  public void init() {

    String noticeParamId = this.getContextParam("page");
    log.info("Display notice landing page view for " + noticeParamId);

    DisplayNoticeKey noticeKey = new DisplayNoticeKey(noticeParamId);
    displayNotice = this.getDisplayNoticeService().get(noticeKey);
    landingPage = this.getDisplayNoticeService().viewLandingPage(noticeKey,
        this.getMemberKey(member));
    this.wallComments = commentService.getComments(this.landingPage
        .getNoticeKey());
  }

  public MemberInfoDto getMember() {
    return member;
  }

  public void setMember(MemberInfoDto member) {
    this.member = member;
  }

  public DisplayNoticeDto getDisplayNotice() {
    return displayNotice;
  }

  public DisplayNoticeLandingPageDto getLandingPage() {
    return landingPage;
  }

  public String getWallComment() {
    return wallComment;
  }

  public void setWallComment(String wallComment) {
    this.wallComment = wallComment;
  }

  public List<CommentAndRepliesDto<DisplayNoticeKey>> getWallComments() {
    return wallComments;
  }

  public void postWallComment() {
    log.info("Posting doc wall comment " + this.wallComment);

    NewDisplayNoticeCommentDto commentDto = new NewDisplayNoticeCommentDto(
        this.getMemberKey(member), this.wallComment,
        this.landingPage.getNoticeKey());

    try {
      this.getDisplayNoticeService().postComment(commentDto);

    } catch (Exception exp) {
      log.error("Error ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Error posting comment."));
    }

    this.wallComments = commentService.getComments(this.landingPage
        .getNoticeKey());
    this.wallComment = StringUtils.EMPTY;

    FacesContext.getCurrentInstance().addMessage(
        null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Comment Saved",
            "Your comment has been saved."));

  }

}
