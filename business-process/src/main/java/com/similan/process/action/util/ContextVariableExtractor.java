package com.similan.process.action.util;

import org.apache.commons.beanutils.ConvertUtils;
import org.jbpm.api.activity.ActivityExecution;

import com.similan.framework.workflow.ProcessContextParameterConstants;
import com.similan.process.action.ActionResultType;

public class ContextVariableExtractor {
	
	private String contextVarName;
	
	private String memberVarName;
	
	private Class<?> contextVarType;
	
	public String getContextVarName() {
		return contextVarName;
	}

	public void setContextVarName(String contextVarName) {
		this.contextVarName = contextVarName;
	}

	public String getMemberVarName() {
		return memberVarName;
	}

	public void setMemberVarName(String memberVarName) {
		this.memberVarName = memberVarName;
	}

	public Class<?> getContextVarType() {
		return contextVarType;
	}

	public void setContextVarType(Class<?> contextVarType) {
		this.contextVarType = contextVarType;
	}
	
	public void extract(ActivityExecution execution, Object actionVar){
		
		/**
		 * 1. Get the var from context and convert it properly
		 * 2. Copy in the destination var of the action
		 */
		Object contextVar = ConvertUtils.convert(execution.getVariable(this.getContextVarName()), 
				    this.getContextVarType());
		
		if(contextVar == null){
			execution.setVariable(ProcessContextParameterConstants.ACTION_RESULT, 
					                    ActionResultType.inValid.toString());
			
			String actionFailureCause = "Context variableextraction failed";
			execution.setVariable(ProcessContextParameterConstants.ACTION_FAILURE_CAUSE, 
					actionFailureCause);
			
			return;
		}
		
		actionVar =  contextVar;
	
	}
}
