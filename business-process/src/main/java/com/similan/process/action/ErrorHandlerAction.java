package com.similan.process.action;

import lombok.extern.slf4j.Slf4j;

import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.activity.ActivityExecution;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ErrorHandlerAction implements ActivityBehaviour  {

	private static final long serialVersionUID = 1L;

	public void execute(ActivityExecution execution) throws Exception {
		log.info("Error executing work flow " + execution.getId());
		
	}

}
