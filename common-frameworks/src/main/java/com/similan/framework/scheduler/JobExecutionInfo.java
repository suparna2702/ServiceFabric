package com.similan.framework.scheduler;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;

public class JobExecutionInfo {
	
    private JobDetail jobDetail;
    private CronTrigger cronTrigger;
    
	public JobDetail getJobDetail() {
		return jobDetail;
	}
	public void setJobDetail(JobDetail jobDetail) {
		this.jobDetail = jobDetail;
	}
	public CronTrigger getCronTrigger() {
		return cronTrigger;
	}
	public void setCronTrigger(CronTrigger cronTrigger) {
		this.cronTrigger = cronTrigger;
	}
}
