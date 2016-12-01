package com.similan.domain.repository.community.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.Suggestion;
import com.similan.domain.entity.community.SuggestionSourceType;

public interface JpaSuggestionRepository extends JpaRepository<Suggestion, Long> {
	
	@Query("select suggestion from Suggestion suggestion where suggestion.suggestionInfo.suggestionType=:sourceType")
	public List<Suggestion> findSuggestionBySourceType(@Param("sourceType")SuggestionSourceType sourceType);
	
	@Query("select suggestion from Suggestion suggestion where suggestion.suggestionInfo.suggestionFor=:actorId")
	public List<Suggestion> findSuggestionBySocialActor(@Param("actorId")Long actorId);

}
