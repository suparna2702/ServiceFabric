package com.similan.adminapp.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import lombok.extern.slf4j.Slf4j;

import org.primefaces.model.SelectableDataModel;

@Slf4j
public class BusinessProcessSelectionModel extends ListDataModel<String> 
                                           implements SelectableDataModel<String>, Serializable {
	
	private static final long serialVersionUID = 1L;

	public BusinessProcessSelectionModel() {  
    }  
  
    public BusinessProcessSelectionModel(List<String> data) {  
        super(data);  
    }  

	public String getRowData(String processName) {
		
		@SuppressWarnings("unchecked")
    List<String> processList = (List<String>) getWrappedData();  
		log.info("Business process list " + processList);
		
        for(String process : processList) {  
            if(process.contentEquals(processName))  
                return process;  
        } 
        
		return null;
	}

	public Object getRowKey(String process) {
		return process;
	}

}
