package com.similan.framework.scheduler;

/**
 * responsible to create a Quartz job
 * @author suparnap
 *
 */
public interface JobExecutorFactory {

	/**
	 *  argument is a generic object so that any type of parameter
	 *  can be passed which is suitable for catering to many types 
	 *  of jobs
	 * @param argObj
	 * @return
	 */
	JobExecutionInfo getInfo(Object argObj);
}
