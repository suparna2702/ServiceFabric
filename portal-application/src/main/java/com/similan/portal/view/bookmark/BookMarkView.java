package com.similan.portal.view.bookmark;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;
import com.similan.service.api.bookmark.dto.basic.BookmarkDto;
import com.similan.service.api.bookmark.dto.key.BookmarkKey;
import com.similan.service.api.community.dto.key.SocialActorKey;

@Scope("view")
@Component("bookMarkView")
@Slf4j
public class BookMarkView extends BaseView {

  private static final long serialVersionUID = 1L;


  @Autowired(required = true)
  private MemberInfoDto member = null;

  private SocialActorKey socialActorKey;

  private List<BookmarkDto> bookmarkList;

  @PostConstruct
  public void init() {
    log.info("Post init in bookmark view " + member.getId());
    this.socialActorKey = this.getMemberKey(member);

    try {
      bookmarkList = this.getBookmarkService().getByOwner(this.socialActorKey);
    } catch (Exception exp) {
      log.error("No bookmark is fetched ", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Cannot fetch bookmarks due to " + exp.getMessage()));
    }

  }

  public List<BookmarkDto> getBookmarkList() {
    return bookmarkList;
  }

  public void setBookmarkList(List<BookmarkDto> bookmarkList) {
    log.info("Selected bookmark " + bookmarkList);
    this.bookmarkList = bookmarkList;
  }

  public void deleteBookmark(BookmarkKey selectedBookmarkKey) throws IOException {

    log.info("Deleting bookmark " + selectedBookmarkKey);
    this.getBookmarkService().delete(selectedBookmarkKey);
    selectedBookmarkKey = null;

    FacesContext.getCurrentInstance().addMessage(
        null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
            "Bookmark deleted successfully "));
    String redirectUrl = this.getPlatformCommonSettings()
        .getPortalApplcationUrl().getValue() + "member/bookMarkView.xhtml";
    FacesContext.getCurrentInstance().getExternalContext()
        .redirect(redirectUrl);

  }

}
