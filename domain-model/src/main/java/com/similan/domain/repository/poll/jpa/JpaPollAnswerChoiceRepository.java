package com.similan.domain.repository.poll.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.poll.PollAnswerChoice;

public interface JpaPollAnswerChoiceRepository 
               extends JpaRepository<PollAnswerChoice, Long> {

}
