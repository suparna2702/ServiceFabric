package com.similan.domain.repository.poll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.poll.PollTemplateAnswerChoice;
import com.similan.domain.repository.poll.jpa.JpaPollTemplateAnswerChoiceRepository;

@Repository
public class PollTemplateAnswerChoiceRepository {
  @Autowired
  private JpaPollTemplateAnswerChoiceRepository repository;
	
	public PollTemplateAnswerChoice findOne(Long id) {
    return repository.findOne(id);
  }
	public PollTemplateAnswerChoice save(PollTemplateAnswerChoice pollTemplateQuestionChoice) {
    return repository.save(pollTemplateQuestionChoice);
  }
	
	public PollTemplateAnswerChoice create(){
		PollTemplateAnswerChoice pollTemplateQuestionChoice = new PollTemplateAnswerChoice();
		return pollTemplateQuestionChoice;
	}

}
