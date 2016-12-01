package com.similan.portal.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import lombok.extern.slf4j.Slf4j;

import org.primefaces.model.SelectableDataModel;

@Slf4j
public class SelectableContactModel extends ListDataModel<SelectableContact> 
                               implements SelectableDataModel<SelectableContact>, Serializable{

	private static final long serialVersionUID = 1L;
	
	public SelectableContactModel(){
		
	}
	
	public SelectableContactModel(List<SelectableContact> contacts){
		super(contacts);		
		log.info("Creating selectable contact model");
	}

	@Override
	@SuppressWarnings("unchecked")
	public SelectableContact getRowData(String contactKey) {
		log.info("Selectable contact key " + contactKey);
        List<SelectableContact> contacts = (List<SelectableContact>) getWrappedData();
        
        if(contacts != null){
            log.info("Contacts " + contacts.size());
        }
        
        for(SelectableContact contact : contacts) {
            if(contact.getId().toString().equals(contactKey))
                return contact;
        }
        
 		return null;
	}

	@Override
	public Object getRowKey(SelectableContact contactObj) {
		log.info("Contact to be fetched getRowKey " + contactObj.getId() + " id ");
		return contactObj.getId().toString();
	}

}
