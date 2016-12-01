package com.similan.domain.repository.poll.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.poll.PollAnswer;

public interface JpaPollAnswerRepository extends JpaRepository<PollAnswer, Long> {

}
