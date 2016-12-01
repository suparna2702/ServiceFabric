package com.similan.adminapp.view;

import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.LineChartSeries;

import com.similan.framework.dto.admin.AdminAccountDto;

@SessionScoped
@ManagedBean(name = "adminView")
@Slf4j
public class AdminView extends BaseAdminView {

	private static final long serialVersionUID = 1L;
	
	
	private static final int START_PERIOD = 30;
	
	private static final int FREQUENCY = 5;
	
	private String loginEmail;
	
	private String loginPassword;
	
	private AdminAccountDto admin = new AdminAccountDto();
	
	private CartesianChartModel memberLoginModel;
	
	private CartesianChartModel wallEventModel;
	
	private CartesianChartModel communitySearchModel;
	
	private CartesianChartModel partnerSearchModel;
	
	private CartesianChartModel pollModel;
	
	private CartesianChartModel leadTransferModel;
	
	private CartesianChartModel leadSyncModel;
	
	private CartesianChartModel leadImportModel;
	
	@PostConstruct
	public void init() {
		
		log.info("Post init in member view");
		
		this.populateMemberLoginModel();
		this.populateWallEventModel();
		this.populateCommunitySearchModel();
		this.populatePartnerSearchModel();
		this.populatePollModel();
		this.populateLeadTransferModel();
		this.populateLeadSyncModel();
		this.populateLeadImportModel();
		this.populateLeadImportModel();
	}
	
    private void populateLeadImportModel(){
    	
		Map<String, Long> leadImportCount = null;
		leadImportCount = this.getAdminService().getLeadImportCountOverTimeperiod(START_PERIOD, FREQUENCY);
		
		if(leadImportCount != null){
			
			Set<String> eventKeys = leadImportCount.keySet();
			this.leadImportModel = new CartesianChartModel(); 
			LineChartSeries leadImportSeries = new LineChartSeries();  
			leadImportSeries.setLabel("Lead Transfer"); 
			
			Integer counter=0;
			
			for(String key : eventKeys){
				Long count = leadImportCount.get(key);
				leadImportSeries.set(counter.toString(), count);
				counter++;
			}
			
			this.leadImportModel.addSeries(leadImportSeries);
		}
			
	}
	
    private void populateLeadTransferModel(){
    	
		Map<String, Long> leadTransferCount = null;
		leadTransferCount = this.getAdminService().getLeadTransferCountOverTimeperiod(START_PERIOD, FREQUENCY);
		
		if(leadTransferCount != null){
			
			Set<String> eventKeys = leadTransferCount.keySet();
			this.leadTransferModel = new CartesianChartModel(); 
			LineChartSeries leadTransferSeries = new LineChartSeries();  
			leadTransferSeries.setLabel("Lead Transfer"); 
			
			Integer counter=0;
			
			for(String key : eventKeys){
				Long count = leadTransferCount.get(key);
				leadTransferSeries.set(counter.toString(), count);
				counter++;
			}
			
			this.leadTransferModel.addSeries(leadTransferSeries);
		}
			
	}
    
    private void populateLeadSyncModel(){
    	
		Map<String, Long> leadSyncCount = null;
		leadSyncCount = this.getAdminService().getCrmSyncCountOverTimeperiod(START_PERIOD, FREQUENCY);
		
		if(leadSyncCount != null){
			
			Set<String> eventKeys = leadSyncCount.keySet();
			this.leadSyncModel = new CartesianChartModel(); 
			LineChartSeries leadSyncSeries = new LineChartSeries();  
			leadSyncSeries.setLabel("Lead Transfer"); 
			
			Integer counter=0;
			
			for(String key : eventKeys){
				Long count = leadSyncCount.get(key);
				leadSyncSeries.set(counter.toString(), count);
				counter++;
			}
			
			this.leadSyncModel.addSeries(leadSyncSeries);
		}
			
	}
    
    private void populatePollModel(){
    	
		this.pollModel = new CartesianChartModel(); 
		
		Map<String, Long> pollEventCount = null;
		pollEventCount = this.getAdminService().getPollEventCountOverTimeperiod(START_PERIOD, FREQUENCY);
		
		if(pollEventCount != null){
			
			Set<String> eventKeys = pollEventCount.keySet();
			LineChartSeries pollEventSeries = new LineChartSeries();  
			pollEventSeries.setLabel("Poll Event"); 
			
			Integer counter=0;
			
			for(String key : eventKeys){
				Long count = pollEventCount.get(key);
				pollEventSeries.set(counter.toString(), count);
				counter++;
			}
			
			this.pollModel.addSeries(pollEventSeries);
		}
		
		Map<String, Long> pollSubmissionCount = null;
		pollSubmissionCount = this.getAdminService().getPollSubmissionCountOverTimeperiod(START_PERIOD, FREQUENCY);
		
		if(pollSubmissionCount != null){
			
			Set<String> eventKeys = pollSubmissionCount.keySet();
			LineChartSeries pollSubmissionSeries = new LineChartSeries();  
			pollSubmissionSeries.setLabel("Poll Submission"); 
			
			Integer counter=0;
			
			for(String key : eventKeys){
				Long count = pollSubmissionCount.get(key);
				pollSubmissionSeries.set(counter.toString(), count);
				counter++;
			}
			
			this.pollModel.addSeries(pollSubmissionSeries);
		}		
			
	}
	
    private void populateMemberLoginModel(){
    	
		Map<String, Long> loginEventCount = null;
		loginEventCount = this.getAdminService().getMemberLoginCountOverTimeperiod(START_PERIOD, FREQUENCY);
		
		if(loginEventCount != null){
			
			Set<String> eventKeys = loginEventCount.keySet();
			this.memberLoginModel = new CartesianChartModel(); 
			LineChartSeries loginEventSeries = new LineChartSeries();  
			loginEventSeries.setLabel("Login"); 
			
			Integer counter=0;
			
			for(String key : eventKeys){
				Long count = loginEventCount.get(key);
				loginEventSeries.set(counter.toString(), count);
				counter++;
			}
			
			this.memberLoginModel.addSeries(loginEventSeries);
		}
			
	}
    
    private void populateWallEventModel(){
    	Map<String, Long> wallEventCount = null;
    	wallEventCount = this.getAdminService().getWallEventCountOverTimeperiod(START_PERIOD, FREQUENCY);
		
		if(wallEventCount != null){
			
			Set<String> eventKeys = wallEventCount.keySet();
			this.wallEventModel = new CartesianChartModel(); 
			LineChartSeries wallEventSeries = new LineChartSeries();  
			wallEventSeries.setLabel("Activity"); 
			
			Integer counter=0;
			
			for(String key : eventKeys){
				Long count = wallEventCount.get(key);
				wallEventSeries.set(counter.toString(), count);
				counter++;
			}
			
			this.wallEventModel.addSeries(wallEventSeries);
		}
    }
    
    private void populateCommunitySearchModel(){
    	Map<String, Long> communitySearchCount = null;
    	communitySearchCount = this.getAdminService()
    			                .getCommunitySearchEventCountOverTimeperiod(START_PERIOD, FREQUENCY);
		
		if(communitySearchCount != null){
			
			Set<String> eventKeys = communitySearchCount.keySet();
			this.communitySearchModel = new CartesianChartModel(); 
			LineChartSeries searchEventSeries = new LineChartSeries();  
			searchEventSeries.setLabel("Search"); 
			
			Integer counter = 0;
			
			for(String key : eventKeys){
				Long count = communitySearchCount.get(key);
				searchEventSeries.set(counter.toString(), count);
				counter++;
			}
			
			this.communitySearchModel.addSeries(searchEventSeries);
		}
    }
    
    private void populatePartnerSearchModel(){
    	Map<String, Long> partnerSearchCount = null;
    	partnerSearchCount = this.getAdminService()
    			                .getPartnerSearchEventCountOverTimeperiod(START_PERIOD, FREQUENCY);
		
		if(partnerSearchCount != null){
			
			Set<String> eventKeys = partnerSearchCount.keySet();
			this.partnerSearchModel = new CartesianChartModel(); 
			LineChartSeries partnerEventSeries = new LineChartSeries();  
			partnerEventSeries.setLabel("Partner"); 
			
			Integer counter = 0;
			
			for(String key : eventKeys){
				Long count = partnerSearchCount.get(key);
				partnerEventSeries.set(counter.toString(), count);
				counter++;
			}
			
			this.partnerSearchModel.addSeries(partnerEventSeries);
		}
    }
    
	public CartesianChartModel getLeadTransferModel() {
		return leadTransferModel;
	}

	public void setLeadTransferModel(CartesianChartModel leadTransferModel) {
		this.leadTransferModel = leadTransferModel;
	}

	public CartesianChartModel getLeadSyncModel() {
		return leadSyncModel;
	}

	public void setLeadSyncModel(CartesianChartModel leadSyncModel) {
		this.leadSyncModel = leadSyncModel;
	}

	public CartesianChartModel getLeadImportModel() {
		return leadImportModel;
	}

	public void setLeadImportModel(CartesianChartModel leadImportModel) {
		this.leadImportModel = leadImportModel;
	}

	public CartesianChartModel getPollModel() {
		return pollModel;
	}

	public void setPollModel(CartesianChartModel pollModel) {
		this.pollModel = pollModel;
	}

	public CartesianChartModel getMemberLoginModel() {
		return memberLoginModel;
	}

	public void setMemberLoginModel(CartesianChartModel memberLoginModel) {
		this.memberLoginModel = memberLoginModel;
	}

	public CartesianChartModel getWallEventModel() {
		return wallEventModel;
	}

	public void setWallEventModel(CartesianChartModel wallEventModel) {
		this.wallEventModel = wallEventModel;
	}

	public CartesianChartModel getCommunitySearchModel() {
		return communitySearchModel;
	}

	public void setCommunitySearchModel(CartesianChartModel communitySearchModel) {
		this.communitySearchModel = communitySearchModel;
	}

	public CartesianChartModel getPartnerSearchModel() {
		return partnerSearchModel;
	}

	public void setPartnerSearchModel(CartesianChartModel partnerSearchModel) {
		this.partnerSearchModel = partnerSearchModel;
	}



	public String getLoginEmail() {
		return loginEmail;
	}

	public void setLoginEmail(String loginEmail) {
		this.loginEmail = loginEmail;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	
	public AdminAccountDto getAdmin() {
		return admin;
	}

	public void setAdmin(AdminAccountDto admin) {
		this.admin = admin;
	}
	
	public Long getTotalMemberCount(){
		return this.getAdminService().getTotalMemberCount();
	}
	
	public void setTotalMemberCount(Long totalMemberCount) {
		
	}
	
	public Long getTotalPendingMemberCount(){
		return this.getAdminService().getTotalPendingMemberCount();
	}
	
	public void setTotalPendingMemberCount(Long count){
		
	}
	
	public Long getTotalSuspendedMemberCount(){
		return this.getAdminService().getTotalSuspendedMemberCount();
	}
	
	public void setTotalSuspendedMemberCount(Long count){
		
	}
	
	public Long getTotalActiveMemberCount(){
		return this.getAdminService().getTotalActiveMemberCount();
	}
	
	public void setTotalActiveMemberCount(Long count){
		
	}
	
	public Long getTotalBusinessCount(){
		return this.getAdminService().getTotalBusinessCount();
	}
	
    public void setTotalPartnerProgramCount(Long progCnt){
		
	}
	
	public Long getTotalPartnerProgramCount(){
		return this.getAdminService().getTotalPartnerProgramCount();
	}
	
    public void setTotalPartnershipCount(Long cnt){
		
	}
	
	public Long getTotalPartnershipCount(){
		return this.getAdminService().getTotalPartnershipCount();
	}

	public void logout(){
		log.info("logout");
		this.admin = null;
		facesHelper.getSession().invalidate();
		facesHelper.redirect("/adminui/home.xhtml");
	}

	public void login(){
		log.info("login for email " + this.loginEmail + " password " + this.loginPassword);
		this.admin = this.getAdminService().login(this.loginEmail, this.loginPassword);

		if(this.admin != null && this.admin.getLogged() == true){
			this.facesHelper.redirect("/adminui/admin/adminHome.xhtml");
		}
		else {
			FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Failure", 
                    "Failure to login due to incorrect credentials"));  
		}
	}
	
	public void createAccount(){
		
		try {
			log.info("join admin");
			
			if(this.getAdminService().isEmailExists(this.admin.getEmail()) != true){
				this.getAdminService().createAdminAccount(this.admin);	
				facesHelper.redirect("/adminui/admin/manageAdminAccounts.xhtml");
			}
			else {
				FacesContext.getCurrentInstance().addMessage(null, 
	                    new FacesMessage(FacesMessage.SEVERITY_WARN,"Warning", 
	                    "An account with the same address already exists: " + this.admin.getEmail())); 
			}
		}
		catch(Exception exp){
			log.info("Error login ", exp);
			FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Failure", 
                    "Failure to login due to " + exp.getMessage()));  		
		}
	}

}
