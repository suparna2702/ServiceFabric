package com.similan.portal.view.leadportal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.metadata.IndustryType;
import com.similan.domain.entity.metadata.StateType;
import com.similan.framework.dto.lead.LeadSearchFilterKeywordDto;
import com.similan.framework.dto.lead.LeadSearchFilterSettingDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.util.JsfUtils;
import com.similan.portal.databean.CountryBean;
import com.similan.portal.databean.IndustryBean;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("leadViewSettingsView")
@Slf4j
public class LeadViewSettingsView extends BaseView {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private MemberInfoDto member = null;
	
    private DualListModel<String> industryList;
	
	private DualListModel<String> titleList;
	
	private LeadSearchFilterSettingDto leadSetting;
	
	private List<StateType> selectedStateList = null;
	
	private String keywordToAdd = null;
	
	private boolean showAdvanced = true;
	
	@PostConstruct
    public void init() {
		
		/* Industry pick list */
		/*String idStr = JsfUtils.getContextParam("fid");
		
		if(idStr != null){
			try {
				Long id = Long.valueOf(idStr);
				leadSetting = this.getLeadService().getLeadSearchFilterSettingsById(id);
			}
			catch(Exception exp){
				log.error("Cannot fetch search filter ", exp);
				FacesContext.getCurrentInstance().addMessage(null, 
	                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Cannot fetch search filter ", 
	                    "Cannot fetch search filter "));
			}
		}
		else {
			log.info("Creating a new search filter ");
	        leadSetting = new LeadSearchFilterSettingDto();
		}
		
		IndustryBean industryBean = (IndustryBean)this.findBackingBean("industryBean");
		List<IndustryType> indList = industryBean.getIndustryList();
		
		List<String> srcIndustryList = new ArrayList<String>();
		List<String> destIndustryList = new ArrayList<String>();
		List<String> industryFromLeadConfig = this.leadSetting.getIndustryList();
		
		for(String industryDest : industryFromLeadConfig){
			destIndustryList.add(industryDest);
		}
		
		for(IndustryType industry : indList){
			
			 if the title does not exist then add 
			if(containsIgnoreCase(destIndustryList, industry.getInsdustryName()) == false){
				srcIndustryList.add(industry.getInsdustryName());
			}
		}
		
		industryList = new DualListModel<String>(srcIndustryList, destIndustryList);
		
		 populate the title list 
		List<String> srcTitleList = new ArrayList<String>();
		List<String> destTitleList = new ArrayList<String>();
		List<String> titleListObj = this.getLeadService().getLeadSearchTitleList();
		List<String> titleFromLeadConfig = this.leadSetting.getTitleList();
				
		 for a small list this comparison is fine which later 
		 * we should create a map 
		for(String titleDest : titleFromLeadConfig){
			destTitleList.add(titleDest);
		}
		
		for(String title : titleListObj){
			
			 if the title does not exist then add 
			if(containsIgnoreCase(destTitleList, title) == false){
				srcTitleList.add(title);
			}
		}
		
		titleList = new DualListModel<String>(srcTitleList, destTitleList);
		
        CountryBean countryBean = (CountryBean)this.findBackingBean("countryBean");
		this.selectedStateList = countryBean.getStateTypeList(this.leadSetting.getCountry());*/
		
	}
	
	public boolean isShowAdvanced() {
		return showAdvanced;
	}

	public void setShowAdvanced(boolean showAdvanced) {
		this.showAdvanced = showAdvanced;
	}

	public MemberInfoDto getMember() {
		return member;
	}

	public void setMember(MemberInfoDto member) {
		this.member = member;
	}

	public void addKeyword(){
		log.info("adding keyword " + this.keywordToAdd);
		LeadSearchFilterKeywordDto keyword = new LeadSearchFilterKeywordDto();
		keyword.setKeyword(this.keywordToAdd);
		
		this.leadSetting.getKeywordFilters().add(keyword);
	}
	
	/**
	 * for now deleting by string later by id
	 * @param delKeyword
	 */
	public void deleteKeyword(String delKeyword){
		log.info("deleting keyword " + delKeyword);
		
		for(LeadSearchFilterKeywordDto keyword : this.leadSetting.getKeywordFilters()){
			if(keyword.getKeyword()
					  .equalsIgnoreCase(delKeyword) == true){
				this.leadSetting.getKeywordFilters().remove(keyword);
				break;
			}
		}
	}
	
	public String getKeywordToAdd() {
		return keywordToAdd;
	}

	public void setKeywordToAdd(String keywordToAdd) {
		this.keywordToAdd = keywordToAdd;
	}

	public DualListModel<String> getIndustryList() {
		return industryList;
	}

	public void setIndustryList(DualListModel<String> industryList) {
		this.industryList = industryList;
	}

	public DualListModel<String> getTitleList() {
		return titleList;
	}

	public void setTitleList(DualListModel<String> titleList) {
		this.titleList = titleList;
	}

	public List<StateType> getSelectedStateList() {
		return selectedStateList;
	}

	public void setSelectedStateList(List<StateType> selectedStateList) {
		this.selectedStateList = selectedStateList;
	}
	
	public LeadSearchFilterSettingDto getLeadSetting() {
		return leadSetting;
	}

	public void setLeadSetting(LeadSearchFilterSettingDto leadSetting) {
		this.leadSetting = leadSetting;
	}

	public void loadSelectedStates(){
		CountryBean countryBean = (CountryBean)this.findBackingBean("countryBean");
		this.selectedStateList = countryBean.getStateTypeList(this.leadSetting.getCountry());
	}
	
	public String searchAffiliateByFilter(){
		
		/*log.info("Searching affiliate leads " + this.leadSetting);
		leadSetting.setIndustryList(industryList.getTarget());
		leadSetting.setTitleList(titleList.getTarget());
		
		try {
			 save the search filter 1st 
			this.getLeadService().saveSearchFilterSetting(member, leadSetting);
		}
		catch(Exception exp){
			log.error("Cannot search with filter ", exp);
			FacesContext.getCurrentInstance().addMessage(null, 
				     new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", 
				     "Cannot search with filter " + exp.getMessage())); 
		}*/
		
		return "result";
	}
	
	public String saveFilter(){
		/*log.info("Saving lead search filter " + this.leadSetting);
		if(StringUtils.isBlank(leadSetting.getName())){
			leadSetting.setName("Adhoc Search Filter");
		}
		
		leadSetting.setActiveSince(new Date());
		
		if(this.showAdvanced == true){
			leadSetting.setIndustryList(industryList.getTarget());
			leadSetting.setTitleList(titleList.getTarget());
		}
		
		try {
			this.getLeadService().saveSearchFilterSetting(member, leadSetting);
			FacesContext.getCurrentInstance().addMessage(null, 
	                   new FacesMessage(FacesMessage.SEVERITY_INFO,"Search filter successfully saved", 
	                   "Search filter successfully saved"));
		}
		catch(Exception exp){
			log.error("Cannot save search filter ", exp);
			FacesContext.getCurrentInstance().addMessage(null, 
				     new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", 
				     "Cannot save search filter " + exp.getMessage()));  	
		}*/
		
		return "result";
		
	}
	
}
