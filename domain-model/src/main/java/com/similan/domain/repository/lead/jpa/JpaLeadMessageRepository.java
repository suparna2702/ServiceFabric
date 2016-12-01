package com.similan.domain.repository.lead.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.lead.Lead;
import com.similan.domain.entity.lead.LeadMessage;

public interface JpaLeadMessageRepository extends JpaRepository<LeadMessage, Long> {
	
	@Query("select leadMessage from LeadMessage leadMessage where leadMessage.owner=:owner")
	public List<LeadMessage> getLeadCommentByOwner(@Param("owner")Lead owner);

	@Query("delete from LeadMessage leadMessage where leadMessage.id=:id")
	public abstract void delete(@Param("id")Long id);
}
