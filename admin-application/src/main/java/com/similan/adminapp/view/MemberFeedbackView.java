package com.similan.adminapp.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lombok.extern.slf4j.Slf4j;

import com.similan.framework.dto.util.MemberFeedbackDto;

@ViewScoped
@ManagedBean(name = "memberFeedbackView")
@Slf4j
public class MemberFeedbackView extends BaseAdminView {

  private static final long serialVersionUID = 1L;
  
  private List<MemberFeedbackDto> feedbackList;
  
  @PostConstruct
  public void init() {
    log.info("Initializing MemberFeedbackView ");
    feedbackList = this.getMemberAdminService().getAllMemberFeedbacks();
  }

  public List<MemberFeedbackDto> getFeedbackList() {
    return feedbackList;
  }

  public void setFeedbackList(List<MemberFeedbackDto> feedbackList) {
    this.feedbackList = feedbackList;
  }
  
  

}
