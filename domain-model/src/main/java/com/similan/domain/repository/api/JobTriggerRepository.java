package com.similan.domain.repository.api;

import java.util.List;

import com.similan.domain.job.JobTrigger;

public interface JobTriggerRepository {
	
	public List<JobTrigger> getAllTriggers();

}
