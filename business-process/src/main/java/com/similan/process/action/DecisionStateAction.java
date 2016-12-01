package com.similan.process.action;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.jbpm.api.jpdl.DecisionHandler;
import org.jbpm.api.model.OpenExecution;

import com.similan.framework.workflow.ProcessContextParameterConstants;

@Slf4j
public class DecisionStateAction implements DecisionHandler {

	private static final long serialVersionUID = 1L;
			
	private Map<String, String> decisionMap;
	
	public Map<String, String> getDecisionMap() {
		return decisionMap;
	}

	public void setDecisionMap(Map<String, String> decisionMap) {
		this.decisionMap = decisionMap;
	}

	public String decide(OpenExecution execution) {
		
		String decision = (String)execution.getVariable(ProcessContextParameterConstants.ACTION_RESULT);
		log.info("Decision variable name " + decision);
		
		Set<String> contextVarSet = this.decisionMap.keySet();
		Iterator<String> contextVarIterator = contextVarSet.iterator();
		String decisionValue = null;
		
		while(contextVarIterator.hasNext()){
			String contextVar = contextVarIterator.next();
			
			if(contextVar.equalsIgnoreCase(decision) == true){
				
				decisionValue = decisionMap.get(contextVar);
				log.info("Decision value " + decisionValue);
				break;
			}
		}
		
		return decisionValue;
	}

}
