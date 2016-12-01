package com.similan.domain.repository.poll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.poll.PollAnswer;
import com.similan.domain.repository.poll.jpa.JpaPollAnswerRepository;

@Repository
public class PollAnswerRepository {
  @Autowired
  private JpaPollAnswerRepository repository;
	
	public PollAnswer findOne(Long id) {
    return repository.findOne(id);
  }
	public PollAnswer save(PollAnswer answer) {
    return repository.save(answer);
  }
	
	public PollAnswer create(){
		PollAnswer answer = new PollAnswer();
		return answer;
	}
	

}
