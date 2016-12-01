package com.similan.domain.repository.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.activity.ReviewThread;
import com.similan.domain.repository.community.jpa.JpaReviewThreadRepository;

@Repository
public class ReviewThreadRepository {
  @Autowired
  private JpaReviewThreadRepository repository;
	
	public ReviewThread save(ReviewThread reviewThread) {
    return repository.save(reviewThread);
  }
	
	public ReviewThread findOne(Long threadId) {
    return repository.findOne(threadId);
  }
	
	public ReviewThread findBySocialActor(Long actorId) {
    return repository.findBySocialActor(actorId);
  }
	
	public ReviewThread create(){
		ReviewThread reviewThread = new ReviewThread();
		return reviewThread;
	}
}
