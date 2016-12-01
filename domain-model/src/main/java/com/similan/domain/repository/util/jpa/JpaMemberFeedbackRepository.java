package com.similan.domain.repository.util.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.similan.domain.entity.util.MemberFeedback;
import com.similan.domain.entity.util.MemberFeedbackStatus;

public interface JpaMemberFeedbackRepository extends JpaRepository<MemberFeedback, Long>{
  
  List<MemberFeedback> findByStatus(MemberFeedbackStatus status);

}
