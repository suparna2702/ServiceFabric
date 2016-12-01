package com.similan.portal.view.survey;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.poll.PollTemplateDto;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("pollTemplateManagementView")
public class PollTemplateManagementView extends BaseView {
  private static final long serialVersionUID = 1L;

  private List<PollTemplateDto> pollTemplateList;

  @Autowired
  private OrganizationDetailInfoDto orgInfo;

  @PostConstruct
  public void init() {
    pollTemplateList = this.getCampaignManagementService().getAllPollTemplates(
        orgInfo);
  }

  public OrganizationDetailInfoDto getOrgInfo() {
    return orgInfo;
  }

  public void setOrgInfo(OrganizationDetailInfoDto orgInfo) {
    this.orgInfo = orgInfo;
  }

  public List<PollTemplateDto> getPollTemplateList() {
    return pollTemplateList;
  }

  public void setPollTemplateList(List<PollTemplateDto> pollTemplateList) {
    this.pollTemplateList = pollTemplateList;
  }

}
