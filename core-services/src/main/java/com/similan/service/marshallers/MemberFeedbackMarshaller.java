package com.similan.service.marshallers;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.util.MemberFeedback;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.domain.repository.util.MemberFeedbackRepository;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.util.MemberFeedbackDto;

@Slf4j
public class MemberFeedbackMarshaller {


  @Autowired
  private SocialPersonRepository memberRepository;

  @Autowired
  private MemberFeedbackRepository feedbackRepository;

  @Autowired
  private MemberMarshaller memberMarshaller;

  public MemberFeedbackDto marshallMemberFeedback(MemberFeedback feedback) {
    SocialPerson feedbackProvider = feedback.getReporter();

    MemberFeedbackDto feedBackDto = new MemberFeedbackDto();
    feedBackDto.setId(feedback.getId());
    feedBackDto.setFeedbackDate(feedback.getFeedbackDate());
    feedBackDto.setMemberFeedback(feedback.getMemberFeedback());
    feedBackDto.setStatus(feedback.getStatus());
    feedBackDto.setFeedbackType(feedback.getFeedbackType());
    feedBackDto.setUuid(feedback.getUuid());

    if (feedbackProvider != null) {

      MemberInfoDto member = memberMarshaller.marshallMember(feedbackProvider);
      feedBackDto.setMember(member);
    }

    return feedBackDto;
  }

  public List<MemberFeedbackDto> marshallMemberFeedbacks(
      List<MemberFeedback> feedbacks) {
    List<MemberFeedbackDto> retFeedbacks = new ArrayList<MemberFeedbackDto>();
    for (MemberFeedback feedback : feedbacks) {
      retFeedbacks.add(marshallMemberFeedback(feedback));
    }
    return retFeedbacks;
  }

}
