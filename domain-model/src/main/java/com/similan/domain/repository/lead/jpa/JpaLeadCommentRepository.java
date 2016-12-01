package com.similan.domain.repository.lead.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.lead.Lead;
import com.similan.domain.entity.lead.LeadComment;

public interface JpaLeadCommentRepository extends JpaRepository<LeadComment, Long> {
	
	@Query("select leadComment from LeadComment leadComment where leadComment.owner=:owner")
	public abstract List<LeadComment> getLeadCommentByOwner(@Param("owner")Lead owner);
	
}
