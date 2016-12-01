package com.similan.domain.repository.community.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.activity.Review;

public interface JpaReviewRepository 
                 extends JpaRepository<Review, Long>{
	
	@Query("select count(review) from Review review where review.commentDetail.rating=:rating and review.reviewThreadId=:reviewThreadId")
	public Long getReviewCountByRatingAndThread(@Param("rating")Integer rating, @Param("reviewThreadId")Long reviewThreadId);
	
	@Query("select count(review) from Review review where review.reviewThreadId=:reviewThreadId")
	public Long getReviewCountForThread(@Param("reviewThreadId")Long reviewThreadId);

}
