package com.similan.domain.repository.community.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.activity.ReviewThread;

public interface JpaReviewThreadRepository 
         extends JpaRepository<ReviewThread, Long> {
	
	@Query("select reviewThread from ReviewThread reviewThread where reviewThread.socialActorId=:actorId")
	public ReviewThread findBySocialActor(@Param("actorId")Long actorId);

}
