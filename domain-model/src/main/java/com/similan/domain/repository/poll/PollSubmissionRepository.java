package com.similan.domain.repository.poll;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.poll.PollSubmission;
import com.similan.domain.repository.poll.jpa.JpaPollSubmissionRepository;

@Repository
public class PollSubmissionRepository {
  @Autowired
  private JpaPollSubmissionRepository repository;

  public PollSubmission findOne(Long id) {
    return repository.findOne(id);
  }

  public PollSubmission save(PollSubmission submission) {
    return repository.save(submission);
  }

  public List<PollSubmission> findByPollTemplate(Long templateId) {
    return repository.findByPollTemplate(templateId);
  }

  public Long getPollSubmissionEventCountInDateRange(Date fromDate, Date toDate) {
    return repository.getPollSubmissionEventCountInDateRange(fromDate, toDate);
  }

  public Long getPollSubmissionEventCountInDateRange(Date fromDate,
      Date toDate, Long forOrg) {
    return repository.getPollSubmissionEventCountInDateRange(fromDate, toDate,
        forOrg);
  }

  public Long getPollSubmissionEventCount(Long templateId) {
    return this.repository.getPollSubmissionEventCount(templateId);
  }

  public PollSubmission create() {
    PollSubmission submission = new PollSubmission();
    return submission;
  }

}
