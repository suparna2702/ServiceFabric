package com.similan.portal.view.survey;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.update.PollEventDto;
import com.similan.portal.model.PollEventModel;
import com.similan.portal.view.BaseView;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.community.dto.key.SocialActorKey;

@Scope("view")
@Component("pollDashboardView")
@Slf4j
public class PollDashboardView extends BaseView {

  private static final long serialVersionUID = 1L;

  @Autowired(required = true)
  private MemberInfoDto memberInfo;

  @Autowired(required = true)
  private OrganizationDetailInfoDto orgInfo;

  private List<PollEventModel> pollRequests;

  @PostConstruct
  public void init() {
    log.info("Post init in poll dashboard view view ");
    List<PollEventDto> pollEventList = this.getCampaignService()
        .getPollEventForMember(memberInfo);
    pollRequests = new ArrayList<PollEventModel>();
    for (PollEventDto event : pollEventList) {
      SocialActorKey actorKey = this.getSocialActorKey(event.getFromId());
      ActorDto requestor = this.getSocialActorService()
          .getActor(actorKey);
      PollEventModel eventModel = new PollEventModel(event, requestor);
      pollRequests.add(eventModel);

    }
  }

  public MemberInfoDto getMemberInfo() {
    return memberInfo;
  }

  public void setMemberInfo(MemberInfoDto memberInfo) {
    this.memberInfo = memberInfo;
  }

  public OrganizationDetailInfoDto getOrgInfo() {
    return orgInfo;
  }

  public void setOrgInfo(OrganizationDetailInfoDto orgInfo) {
    this.orgInfo = orgInfo;
  }

  public List<PollEventModel> getPollRequests() {
    return pollRequests;
  }

}
