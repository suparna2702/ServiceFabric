package com.similan.domain.repository.community;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.Suggestion;
import com.similan.domain.entity.community.SuggestionSourceType;
import com.similan.domain.repository.community.jpa.JpaSuggestionRepository;

@Repository
public class SuggestionRepository {
  @Autowired
  private JpaSuggestionRepository repository;
	
	public Suggestion save(Suggestion suggestion) {
    return repository.save(suggestion);
  }
	
	public List<Suggestion> findAll() {
    return repository.findAll();
  }
	
	public List<Suggestion> findSuggestionBySourceType(SuggestionSourceType sourceType) {
    return repository.findSuggestionBySourceType(sourceType);
  }
	
	public List<Suggestion> findSuggestionBySocialActor(Long actorId) {
    return repository.findSuggestionBySocialActor(actorId);
  }
	
	public Suggestion create(){
		
		Suggestion suggestion = new Suggestion();
		return suggestion;
	}

}
