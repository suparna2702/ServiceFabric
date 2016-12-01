package com.similan.adminapp.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lombok.extern.slf4j.Slf4j;

import com.similan.framework.dto.metadata.IndustryTypeDto;
import com.similan.framework.dto.update.SystemUpdateEventDto;

@ViewScoped
@ManagedBean(name = "systemUpdateView")
@Slf4j
public class SystemUpdateView extends BaseAdminView {

	private static final long serialVersionUID = 1L;
	
	private SystemUpdateEventDto updateItem = new SystemUpdateEventDto();
	
	private List<SystemUpdateEventDto> systemUpdateList = new ArrayList<SystemUpdateEventDto>();
	
	private List<IndustryTypeDto> industryList = null;
	
	@PostConstruct
	public void init() {
		industryList = this.metaDataAdminService.getIndustryList();
		systemUpdateList = this.metaDataAdminService.getAllSystemUpdates();
	}
	
	public List<IndustryTypeDto> getIndustryList() {
		return industryList;
	}

	public void setIndustryList(List<IndustryTypeDto> industryList) {
		this.industryList = industryList;
	}

	public List<SystemUpdateEventDto> getSystemUpdateList() {
		return systemUpdateList;
	}

	public void setSystemUpdateList(List<SystemUpdateEventDto> systemUpdateList) {
		this.systemUpdateList = systemUpdateList;
	}

	public SystemUpdateEventDto getUpdateItem() {
		return updateItem;
	}

	public void setUpdateItem(SystemUpdateEventDto updateItem) {
		this.updateItem = updateItem;
	}

	public void postUpdate(){
		log.info("Posting updates " + updateItem);
		this.metaDataAdminService.saveSystemUpdate(updateItem);
		this.systemUpdateList.add(updateItem);
	}
	
	public void deleteSystemUpdateItem(String uuID){
		log.info("Deleting update " + uuID);
		SystemUpdateEventDto theChosenOne = null;
		
		Iterator<SystemUpdateEventDto> updateIterator = this.systemUpdateList.iterator();
		while(updateIterator.hasNext()){
			SystemUpdateEventDto updateEventDto = updateIterator.next();
			if(updateEventDto.getUuID().equalsIgnoreCase(uuID)){
				theChosenOne = updateEventDto;
				updateIterator.remove();
				break;
			}
		}
		
		if(theChosenOne != null){
			this.metaDataAdminService.deleteSystemUpdate(theChosenOne);
		}
	}

}
