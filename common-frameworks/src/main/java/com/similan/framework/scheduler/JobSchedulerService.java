package com.similan.framework.scheduler;

import java.text.ParseException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

import com.similan.domain.job.JobTrigger;
import com.similan.domain.job.JobTriggerParameter;
import com.similan.domain.repository.api.JobTriggerRepository;


@Slf4j
public class JobSchedulerService implements InitializingBean, 
                                            ApplicationListener<ApplicationEvent> {

	@Autowired
	protected JobTriggerRepository jobTriggerRepository;

	/**
	 * Quatz's scheduler
	 */
	private Scheduler scheduler = null;

	public JobSchedulerService(final Scheduler theScheduler) {
		this.scheduler = theScheduler;
	}

	/**
	 * starts the service
	 * 
	 * @throws SchedulerException
	 * @throws ClassNotFoundException 
	 * @throws ParseException 
	 */
	public void startService() throws SchedulerException, ParseException, 
	                                  ClassNotFoundException {

		if (this.scheduler.isShutdown()) {
			log.warn("The Service can't be started once it has been shutdown!");
			return;
		}

		this.scheduler.start();
		
		/* schedule all jobs now */
		List<JobTrigger> triggers = this.jobTriggerRepository.getAllTriggers();
		
		for(JobTrigger trigger : triggers){
			this.scheduleJob(trigger);
		}
		
		log.info("Service started.");
	}

	/**
	 * stops the service
	 * 
	 * @param theWaitForJobsToComplete
	 * @throws SchedulerException
	 */
	public void stopService(final boolean theWaitForJobsToComplete)
			throws SchedulerException {

		if (this.scheduler.isStarted()) {
			log.info("Stoping the Job Scheduler Service.");
			this.scheduler.shutdown(theWaitForJobsToComplete);
		} else {
			log.warn("Job Scheduler Service is not started.");
		}
	}

	/**
	 * Pauses the service, which can be later restarted.
	 * 
	 * @throws SchedulerException
	 */
	public void pauseService() throws SchedulerException {

		if (this.scheduler.isInStandbyMode()) {
			log.warn("Job Scheduler Service is already in stand by mode.");
			return;
		}
		this.scheduler.standby();
	}

	/**
	 * Starts the service if it has veen shutdown and resumes all the jobs if it
	 * was in stand by mode.
	 * 
	 * @author suparna
	 * @throws SchedulerException
	 */
	public void restartService() throws SchedulerException {

		log.info("Restarting the service.");

		if (this.scheduler.isInStandbyMode()) {
			this.scheduler.start();
		} else {
			log.warn("Job Scheduler Service is not in stand by mode.");
		}
	}

	/**
	 * Returns the status of the underlying scheduler. The status is a human
	 * readable description.
	 * 
	 * @return either "NOT STARTED", "STANDING BY", "SHUT DOWN", or "RUNNING"
	 * @throws SchedulerException
	 */
	public SchedulerServiceStatus getStatus() throws SchedulerException {

		if (!this.scheduler.isStarted()) {
			return SchedulerServiceStatus.NotStarted;
		}

		if (this.scheduler.isInStandbyMode()) {
			return SchedulerServiceStatus.StandBy;
		}

		if (this.scheduler.isShutdown()) {
			return SchedulerServiceStatus.ShutDown;
		}

		return SchedulerServiceStatus.Running;
	}
	
	/**
	 * 
	 * @param trigger
	 * @throws ParseException
	 * @throws SchedulerException 
	 * @throws ClassNotFoundException 
	 */
	public void scheduleJob(JobTrigger trigger) throws ParseException, SchedulerException, 
	                                                   ClassNotFoundException{
		
		/* 1. if a job of same name is scheduled then unschedule it
		 * 2. get all the info from trigger to create JobDetail and JobTrigger
		 * 3. Schedule the job
		 * */
		this.scheduler.unscheduleJob(trigger.getName(), null);
		CronTrigger cronTrigger = new CronTrigger();
		cronTrigger.setCronExpression(trigger.getCronExpression());
		
		JobDetail jobDetail = new JobDetail();
		jobDetail.setGroup(trigger.getJobGroupName());
		jobDetail.setName(trigger.getName());
		jobDetail.setJobClass(Class.forName(trigger.getJobClassName()));
		
		/* populate all the attributes now of JobDataMap */
		List<JobTriggerParameter> parameters = trigger.getJobTriggerParameters();
		
		if(parameters != null){
			
			JobDataMap dataMap = new JobDataMap();
			for(JobTriggerParameter triggerParam : parameters){
				dataMap.put(triggerParam.getName(), triggerParam.getValue());
			}
			
			jobDetail.setJobDataMap(dataMap);
		}
		
		
		/* now schedule */
		this.scheduler.scheduleJob(jobDetail, cronTrigger);
	}

	public void afterPropertiesSet() throws Exception {
		
	}

	/**
	 * This controls the life cycle of scheduler service
	 */
	public void onApplicationEvent(ApplicationEvent appEvent) {
		
		/* if it is ContextRefreshedEvent then we start the Scheduler */
		if(appEvent instanceof ContextRefreshedEvent){
			log.info("Received ContextRefreshedEvent from spring");
			try {
				this.startService();
			} 
			catch (SchedulerException e) {
				log.error("Scheduling exception ", e);
			} 
			catch (ParseException e) {
				log.error("ParseException ", e);
			} 
			catch (ClassNotFoundException e) {
				log.error("ClassNotFoundException ", e);
			}
		}
		
		/* if context is closed then shut down the scheduler */
		if(appEvent instanceof ContextClosedEvent){
			log.info("Received ContextClosedEvent from spring");
			try {
				this.stopService(true);
			} 
			catch (SchedulerException e) {
				log.error("SchedulerException ", e);
			}
		}		
	}
}
