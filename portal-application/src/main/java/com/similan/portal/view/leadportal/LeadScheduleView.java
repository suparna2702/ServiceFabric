package com.similan.portal.view.leadportal;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;

import lombok.extern.slf4j.Slf4j;

import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.lead.LeadActivityDto;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("leadScheduleView")
@Slf4j
public class LeadScheduleView extends BaseView {

	private static final long serialVersionUID = 1L;
	
	private ScheduleModel eventModel; 
	
	private List<LeadActivityDto> leadActivitys = null;
	
	private ScheduleEvent event;
	
	@Autowired
	private OrganizationDetailInfoDto orgInfo = null;
	
	@PostConstruct
    public void init() {
		log.info("Initializing Lead Schedule View");
		
		/*eventModel = new DefaultScheduleModel();
		
		try {
			
			leadActivitys = this.getLeadService().getLeadActivityByOrg(orgInfo);
			
			for(LeadActivityDto leadActivity : leadActivitys){
				ScheduleEvent activityEvent =  new DefaultScheduleEvent(leadActivity.getSubjectActivity(), 
						                                                leadActivity.getDueDate(), 
						                                                leadActivity.getDueDate(), leadActivity); 
				eventModel.addEvent(activityEvent);
			}
		}
		catch(Exception exp){
			log.error("Unable to fetch the activities", exp);
		}
		
		event = new DefaultScheduleEvent();*/
	}

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}
	
	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}

	public void onEventSelect(SelectEvent selectEvent) {  
    }
	
	public void onDateSelect(SelectEvent selectEvent) {  
    } 
	
	public void onEventMove(ScheduleEntryMoveEvent event) {  
		log.info("event moved " + event.toString());
    }  
      
    public void onEventResize(ScheduleEntryResizeEvent event) {  
    	log.info("event resized " + event.toString());    
    }  
	
	public void addEvent(ActionEvent actionEvent) {  
		log.info("action event in addEvent " + actionEvent);
		
        if(event.getId() == null)  
            eventModel.addEvent(event);  
        else  
            eventModel.updateEvent(event);  
          
        event = new DefaultScheduleEvent();  
    }  
	
}
