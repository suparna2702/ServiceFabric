package com.similan.domain.repository.poll.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.poll.PollTemplateAnswerChoice;

public interface JpaPollTemplateAnswerChoiceRepository extends
		JpaRepository<PollTemplateAnswerChoice, Long> {

}
