package com.similan.framework.dto.util;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.similan.domain.entity.util.MemberFeedbackStatus;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.service.api.feedback.dto.MemberFeedbackType;

@Getter
@Setter
@ToString
public class MemberFeedbackDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;
  
  private String uuid;

  private MemberInfoDto member;

  private String memberFeedback;

  private Date feedbackDate;

  private MemberFeedbackStatus status;

  private MemberFeedbackType feedbackType;

}
