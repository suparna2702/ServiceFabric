package com.similan.domain.repository.util;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.util.MemberFeedback;
import com.similan.domain.entity.util.MemberFeedbackStatus;
import com.similan.domain.repository.util.jpa.JpaMemberFeedbackRepository;
import com.similan.service.api.feedback.dto.MemberFeedbackType;

@Repository
public class MemberFeedbackRepository {
  @Autowired
  private JpaMemberFeedbackRepository repository;

  public List<MemberFeedback> findAll() {
    return this.repository.findAll();
  }

  public List<MemberFeedback> findByStatus(MemberFeedbackStatus status) {
    return this.repository.findByStatus(status);
  }

  public MemberFeedback save(MemberFeedback feedback) {
    return repository.save(feedback);
  }

  public MemberFeedback create(String feedback, SocialPerson reporter,
      MemberFeedbackType feedbackType) {

    MemberFeedback memFeedback = new MemberFeedback();
    memFeedback.setReporter(reporter);
    memFeedback.setMemberFeedback(feedback);
    memFeedback.setFeedbackDate(new Date());
    memFeedback.setStatus(MemberFeedbackStatus.OPEN);
    memFeedback.setFeedbackType(feedbackType);

    return memFeedback;
  }

}
