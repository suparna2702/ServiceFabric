package com.similan.domain.repository.poll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.poll.PollAnswerChoice;
import com.similan.domain.repository.poll.jpa.JpaPollAnswerChoiceRepository;

@Repository
public class PollAnswerChoiceRepository {
  @Autowired
  private JpaPollAnswerChoiceRepository repository;
	
	public PollAnswerChoice save(PollAnswerChoice answerChoice) {
    return repository.save(answerChoice);
  }
	public PollAnswerChoice findOne(Long id) {
    return repository.findOne(id);
  }
	
	public PollAnswerChoice create(){
		PollAnswerChoice choice = new PollAnswerChoice();
		return choice;
	}

}
