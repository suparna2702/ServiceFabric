package com.similan.domain.repository.poll;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.poll.PollTemplate;
import com.similan.domain.repository.poll.jpa.JpaPollTemplateRepository;

@Repository
public class PollTemplateRepository {
  @Autowired
  private JpaPollTemplateRepository repository;
	
	public PollTemplate findOne(Long id) {
    return repository.findOne(id);
  }
	public PollTemplate save(PollTemplate pollTemplate) {
    return repository.save(pollTemplate);
  }
	public List<PollTemplate> findAll() {
    return repository.findAll();
  }
	public List<PollTemplate> findAllByOrganization(SocialOrganization owner) {
    return repository.findAllByOrganization(owner);
  }
	
	public PollTemplate create(){
		PollTemplate pollTemplate = new PollTemplate();
		return pollTemplate;
	}

}
