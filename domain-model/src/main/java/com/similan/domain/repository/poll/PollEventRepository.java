package com.similan.domain.repository.poll;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.poll.PollEvent;
import com.similan.domain.repository.poll.jpa.JpaPollEventRepository;

@Repository
public class PollEventRepository {
  @Autowired
  private JpaPollEventRepository repository;

  public PollEvent save(PollEvent pollEvent) {
    return repository.save(pollEvent);
  }

  public PollEvent findOne(Long id) {
    return repository.findOne(id);
  }

  public List<PollEvent> findByFor(Long forActor) {
    return repository.findByFor(forActor);
  }

  public List<PollEvent> findByFrom(Long fromActor, Long pollId) {
    return repository.findByFrom(fromActor, pollId);
  }

  public List<PollEvent> findByPollTemplate(Long templateId) {
    return repository.findByPollTemplate(templateId);
  }

  public Long getPollEventCountInDateRange(Date fromDate, Date toDate) {
    return repository.getPollEventCountInDateRange(fromDate, toDate);
  }

  public Long getPollEventCountInDateRange(Date fromDate, Date toDate,
      Long forOrg) {
    return repository.getPollEventCountInDateRange(fromDate, toDate, forOrg);
  }

  public Long findPollEventCountFor(Long orgId) {
    return repository.findPollEventCountFor(orgId);
  }

  public Long getPollEventCount(Long templateId) {
    return repository.getPollEventCount(templateId);
  }

  public PollEvent create() {
    PollEvent pollEvent = new PollEvent();
    return pollEvent;
  }

}
