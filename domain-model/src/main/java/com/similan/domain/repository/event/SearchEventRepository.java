package com.similan.domain.repository.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.event.SearchEvent;
import com.similan.domain.repository.event.jpa.JpaSearchEventRepository;

@Repository
public class SearchEventRepository {
  @Autowired
  private JpaSearchEventRepository repository;
	
	public SearchEvent save(SearchEvent message) {
    return repository.save(message);
  }
	
	public List<SearchEvent> save(Iterable<SearchEvent> eventList) {
    return repository.save(eventList);
  }
	
	public SearchEvent create(){
		SearchEvent searchEvent = 
				new SearchEvent();
		return searchEvent;
	}

}
