package com.similan.domain.entity.community;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity(name="Suggestion")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "SuggestionDiscriminatorType", 
                     discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("Suggestion")
public class Suggestion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Embedded
	private SuggestionInfo suggestionInfo;
	
	public SuggestionInfo getSuggestionInfo() {
		return suggestionInfo;
	}

	public void setSuggestionInfo(SuggestionInfo suggestionInfo) {
		this.suggestionInfo = suggestionInfo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
