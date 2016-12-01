package com.similan.domain.repository.event.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.event.SearchEvent;

public interface JpaSearchEventRepository 
         extends JpaRepository<SearchEvent, Long>{

}
