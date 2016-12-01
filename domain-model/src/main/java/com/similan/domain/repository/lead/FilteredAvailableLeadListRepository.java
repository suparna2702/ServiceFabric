package com.similan.domain.repository.lead;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.lead.FilteredAvailableLeadList;
import com.similan.domain.repository.lead.jpa.JpaFilteredAvailableLeadListRepository;

@Repository
public class FilteredAvailableLeadListRepository {
  @Autowired
  private JpaFilteredAvailableLeadListRepository repository;
	
	public FilteredAvailableLeadList findOne(Long id) {
    return repository.findOne(id);
  }
	
	public FilteredAvailableLeadList save(FilteredAvailableLeadList filteredList) {
    return repository.save(filteredList);
  }
	
	public List<FilteredAvailableLeadList> findAll() {
    return repository.findAll();
  }
	
	public FilteredAvailableLeadList findBySocialActor(Long socialActorId) {
    return repository.findBySocialActor(socialActorId);
  }
	
	public FilteredAvailableLeadList create(){
		FilteredAvailableLeadList filteredList = new FilteredAvailableLeadList();
		return filteredList;
	}

}
