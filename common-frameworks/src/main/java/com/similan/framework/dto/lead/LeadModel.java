package com.similan.framework.dto.lead;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import lombok.extern.slf4j.Slf4j;

import org.primefaces.model.SelectableDataModel;

@Slf4j
public class LeadModel extends ListDataModel<LeadDto> 
                       implements SelectableDataModel<LeadDto>, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public LeadModel() {
		log.info("Creating lead model default");
    }

    public LeadModel(List<LeadDto> data) {
        super(data);
        log.info("Creating lead model ");
    }

    public Object getRowKey(LeadDto lead) {
    	log.info("Lead to be fetched getRowKey " + lead.getId() + " id ");
		return lead.getId().toString();
	}

    @SuppressWarnings("unchecked")
	public LeadDto getRowData(String rowKey) {
    	
    	log.info("Lead to be fetched  getRowData " + rowKey);
        List<LeadDto> leads = (List<LeadDto>) getWrappedData();
        
        if(leads != null){
            log.info("Lead " + leads.size());
        }
        
        for(LeadDto lead : leads) {
            if(lead.getId().toString().equals(rowKey))
                return lead;
        }
        
        return null;
	}

}
