package com.similan.domain.repository.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.community.activity.Review;
import com.similan.domain.entity.community.activity.ReviewThread;
import com.similan.domain.repository.community.jpa.JpaReviewRepository;

@Repository
public class ReviewRepository {
  @Autowired
  private JpaReviewRepository repository;
	
	public Review findOne(Long id) {
    return repository.findOne(id);
  }
	
	public Review save(Review review) {
    return repository.save(review);
  }
	
	public Long getReviewCountByRatingAndThread(Integer rating, Long threadId) {
    return repository.getReviewCountByRatingAndThread(rating, threadId);
  }
	
	public Long getReviewCountForThread(Long threadId) {
    return repository.getReviewCountForThread(threadId);
  }
	
	public Review create(){
		Review review = new Review();
		return review;
	}
	
	public ReviewThread createReviewThread(){
		ReviewThread reviewThread = new ReviewThread();
		return reviewThread;
	}
}
