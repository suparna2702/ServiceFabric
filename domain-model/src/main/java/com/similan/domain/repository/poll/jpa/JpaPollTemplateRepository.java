package com.similan.domain.repository.poll.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.poll.PollTemplate;

public interface JpaPollTemplateRepository extends JpaRepository<PollTemplate, Long> {
	
	@Query("select pollTemplate from PollTemplate pollTemplate where pollTemplate.pollOwner=:owner")
	List<PollTemplate> findAllByOrganization(@Param("owner")SocialOrganization owner);

}
