package com.similan.portal.view.member;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;
import com.similan.portal.view.wall.WallPostingView;
import com.similan.service.api.community.dto.basic.SocialActorContactDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.feed.dto.basic.FeedDto;
import com.similan.service.api.feed.dto.basic.FeedEntryDto;
import com.similan.service.api.feed.dto.key.FeedKey;
import com.similan.service.api.wall.dto.basic.WallDto;
import com.similan.service.api.wall.dto.key.WallKey;
import com.similan.service.api.wall.dto.operation.NewWallPostDto;

@Scope("view")
@Component("memberNetworkView")
@Slf4j
public class MemberNetworkView extends BaseView implements WallPostingView {

  private static final long serialVersionUID = 1L;
  private static final int MINIMUM_NUMBER_OF_ENTRY = 1;

  @Autowired(required = true)
  private MemberInfoDto member = null;

  private WallDto<SocialActorKey> memberNetworkWall = null;
  private List<FeedEntryDto> feedEntries = null;
  private List<SocialActorContactDto> contactList = null;
  private FeedDto feed = null;
  private SocialActorKey socialActorKey;
  private String postContent;

  @PostConstruct
  public void init() {
    log.info("Post init in member network view ");

    /* get the wall */
    socialActorKey = this.getMemberKey(member);
    WallKey<SocialActorKey> wallKey = new WallKey<SocialActorKey>(
        socialActorKey);
    memberNetworkWall = this.getWallService().get(wallKey);
    FeedKey feedKey = new FeedKey(socialActorKey);
    feed = this.getFeedService().get(feedKey);

    log.info("The feed key " + feed.toString());
  }
  
  public MemberInfoDto getMember() {
    return member;
  }

  public void setMember(MemberInfoDto member) {
    this.member = member;
  }

  public List<SocialActorContactDto> getContactList() {
    if (contactList == null) {
      SocialActorKey actorKey = this.getMemberKey(member);
      contactList = this.getNetworkService().getDirectConnections(actorKey);
    }
    return contactList;
  }

  public WallDto<SocialActorKey> getMemberNetworkWall() {
    return memberNetworkWall;
  }

  public void setMemberNetworkWall(WallDto<SocialActorKey> memberNetworkWall) {
    this.memberNetworkWall = memberNetworkWall;
  }

  public List<FeedEntryDto> getFeedEntries() {
    if (feedEntries == null || feedEntries.size() <= 0) {
      feedEntries = this.getFeedService().getLatest(this.feed.getKey());
    }
    return feedEntries;
  }

  public String getPostContent() {
    return postContent;
  }

  public void setPostContent(String postContent) {
    this.postContent = postContent;
  }

  public void moreFeedEntry() {
    if (this.feedEntries != null
        && this.feedEntries.size() > MINIMUM_NUMBER_OF_ENTRY) {
      FeedEntryDto lastFeedEntry = this.feedEntries
          .get(this.feedEntries.size() - 1);
      List<FeedEntryDto> moreFeedEntry = this.getFeedService().getMore(
          this.feed.getKey(), lastFeedEntry.getKey().getNumber());

      if (moreFeedEntry != null && moreFeedEntry.size() > 0) {
        this.feedEntries.addAll(moreFeedEntry);
      }
    }
  }

  public void post() {

    try {

      log.info("Posting comment " + this.postContent);

      /* create a wall post */
      NewWallPostDto newPost = new NewWallPostDto(this.getMemberKey(member),
          this.postContent);
      this.getWallService().post(this.memberNetworkWall.getKey(), newPost);

      /* also goes to feed */
      feedEntries = this.getFeedService().getLatest(this.feed.getKey());
    } catch (Exception exp) {
      log.error("Error", exp);
      FacesContext.getCurrentInstance().addMessage(
          null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
              "Comment cannot be posted for " + exp.getMessage()));
    }

    this.postContent = StringUtils.EMPTY;
  }

}
