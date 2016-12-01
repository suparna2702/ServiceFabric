package com.similan.domain.repository.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.util.NonmemberContactMessage;
import com.similan.domain.repository.util.jpa.JpaNonmemberContactMessageRepository;

@Repository
public class NonmemberContactMessageRepository {
  @Autowired
  private JpaNonmemberContactMessageRepository repository;
	
	public NonmemberContactMessage save(NonmemberContactMessage message) {
    return repository.save(message);
  }
	
	public NonmemberContactMessage create(String subject, String email, String comment){
		
		NonmemberContactMessage message = new NonmemberContactMessage();
		message.setEmail(email);
		message.setComment(comment);
		message.setSubject(subject);
		
		return message;
	}
	
	

}
