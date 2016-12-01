package com.similan.domain.repository.poll.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.poll.PollTemplateQuestion;

public interface JpaPollTemplateQuestionRepository 
                  extends JpaRepository<PollTemplateQuestion , Long> {
}
