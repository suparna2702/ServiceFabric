package com.similan.framework.workflow.api;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class WorkflowResponse {
	private WorkflowResponseCode responseCode = WorkflowResponseCode.Success;
  private String processInstanceId;
  private String currentState;
	
	public boolean isSuccess(){
		boolean retCode = true;
		
		switch(responseCode){
		
		   case Failure:
		   case WonflowAlreadyEnded:
		   case FailureWithReturn:
			   retCode = false;
			   break;
		   default: break;
		}
		
		return retCode;
		
	}
	
}
