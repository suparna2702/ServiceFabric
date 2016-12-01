package com.similan.portal.view.leadportal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("leadViewHeaderConfigView")
@Slf4j
public class LeadViewHeaderConfigView extends BaseView {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private MemberInfoDto member = null;
	
	private DualListModel<String> activeLeadViewSettingsModel;
	
	private String leadTypeView = "Active";
	
	@PostConstruct
    public void init() {
		this.refreshViewHeaderModel();
	}
	
	private void refreshViewHeaderModel(){
		
		/* Lead Headers */
		/*List<String> activeLeadViewSource = new ArrayList<String>();
		List<String> leadSearchActiveViewHeaders = this.getLeadService().getLeadSearchViewHeaderMap()
				                                                        .get(this.leadTypeView);
		
		for(String header : leadSearchActiveViewHeaders){
			activeLeadViewSource.add(header);
		}
		
        List<String> activeLeadviewTarget = new ArrayList<String>();
        activeLeadViewSettingsModel = new DualListModel<String>(activeLeadViewSource, activeLeadviewTarget);*/
		
	}

	public MemberInfoDto getMember() {
		return member;
	}

	public void setMember(MemberInfoDto member) {
		this.member = member;
	}

	public DualListModel<String> getActiveLeadViewSettingsModel() {
		return activeLeadViewSettingsModel;
	}

	public void setActiveLeadViewSettingsModel(
			DualListModel<String> activeLeadViewSettingsModel) {
		this.activeLeadViewSettingsModel = activeLeadViewSettingsModel;
	}

	public String getLeadTypeView() {
		return leadTypeView;
	}

	public void setLeadTypeView(String leadTypeView) {
		log.info("Setting lead view settings " + leadTypeView);
		this.leadTypeView = leadTypeView;
		
		this.refreshViewHeaderModel();
	}
	
	public void submitLeadViewSettings(){
		log.info("Saving lead view settings ");
	}

}
