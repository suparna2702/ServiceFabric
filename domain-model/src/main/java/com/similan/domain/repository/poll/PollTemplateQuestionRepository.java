package com.similan.domain.repository.poll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.poll.PollTemplateQuestion;
import com.similan.domain.repository.poll.jpa.JpaPollTemplateQuestionRepository;

@Repository
public class PollTemplateQuestionRepository {
  @Autowired
  private JpaPollTemplateQuestionRepository repository;
	
	public PollTemplateQuestion findOne(Long id) {
    return repository.findOne(id);
  }
	public PollTemplateQuestion save(PollTemplateQuestion pollTemplate) {
    return repository.save(pollTemplate);
  }
	public void delete(Long id) {
    repository.delete(id);
  }
	
	public PollTemplateQuestion create(){
		PollTemplateQuestion pollTemplateQuestion = new PollTemplateQuestion();
		return pollTemplateQuestion;
	}


}
